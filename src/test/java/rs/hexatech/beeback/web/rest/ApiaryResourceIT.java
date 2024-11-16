package rs.hexatech.beeback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rs.hexatech.beeback.domain.ApiaryAsserts.*;
import static rs.hexatech.beeback.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.IntegrationTest;
import rs.hexatech.beeback.domain.Apiary;
import rs.hexatech.beeback.repository.ApiaryRepository;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.service.dto.ApiaryDTO;
import rs.hexatech.beeback.service.mapper.ApiaryMapper;

/**
 * Integration tests for the {@link ApiaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApiaryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Integer DEFAULT_ORDER_NUMBER = 1;
    private static final Integer UPDATED_ORDER_NUMBER = 2;

    private static final Integer DEFAULT_HIVE_COUNT = 1;
    private static final Integer UPDATED_HIVE_COUNT = 2;

    private static final Integer DEFAULT_EXTERNAL_ID = 1;
    private static final Integer UPDATED_EXTERNAL_ID = 2;

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_SYNCHED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_SYNCHED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/apiaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ApiaryRepository apiaryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApiaryMapper apiaryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApiaryMockMvc;

    private Apiary apiary;

    private Apiary insertedApiary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apiary createEntity() {
        return new Apiary()
            .name(DEFAULT_NAME)
            .idNumber(DEFAULT_ID_NUMBER)
            .color(DEFAULT_COLOR)
            .location(DEFAULT_LOCATION)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .orderNumber(DEFAULT_ORDER_NUMBER)
            .hiveCount(DEFAULT_HIVE_COUNT)
            .externalId(DEFAULT_EXTERNAL_ID)
            .uuid(DEFAULT_UUID)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateModified(DEFAULT_DATE_MODIFIED)
            .dateSynched(DEFAULT_DATE_SYNCHED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apiary createUpdatedEntity() {
        return new Apiary()
            .name(UPDATED_NAME)
            .idNumber(UPDATED_ID_NUMBER)
            .color(UPDATED_COLOR)
            .location(UPDATED_LOCATION)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .hiveCount(UPDATED_HIVE_COUNT)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED);
    }

    @BeforeEach
    public void initTest() {
        apiary = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedApiary != null) {
            apiaryRepository.delete(insertedApiary);
            insertedApiary = null;
        }
    }

    @Test
    @Transactional
    void createApiary() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Apiary
        ApiaryDTO apiaryDTO = apiaryMapper.toDto(apiary);
        var returnedApiaryDTO = om.readValue(
            restApiaryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiaryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ApiaryDTO.class
        );

        // Validate the Apiary in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedApiary = apiaryMapper.toEntity(returnedApiaryDTO);
        assertApiaryUpdatableFieldsEquals(returnedApiary, getPersistedApiary(returnedApiary));

        insertedApiary = returnedApiary;
    }

    @Test
    @Transactional
    void createApiaryWithExistingId() throws Exception {
        // Create the Apiary with an existing ID
        apiary.setId(1L);
        ApiaryDTO apiaryDTO = apiaryMapper.toDto(apiary);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApiaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Apiary in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        apiary.setExternalId(null);

        // Create the Apiary, which fails.
        ApiaryDTO apiaryDTO = apiaryMapper.toDto(apiary);

        restApiaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiaryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        apiary.setUuid(null);

        // Create the Apiary, which fails.
        ApiaryDTO apiaryDTO = apiaryMapper.toDto(apiary);

        restApiaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiaryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        apiary.setDateCreated(null);

        // Create the Apiary, which fails.
        ApiaryDTO apiaryDTO = apiaryMapper.toDto(apiary);

        restApiaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiaryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        apiary.setDateModified(null);

        // Create the Apiary, which fails.
        ApiaryDTO apiaryDTO = apiaryMapper.toDto(apiary);

        restApiaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiaryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSynchedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        apiary.setDateSynched(null);

        // Create the Apiary, which fails.
        ApiaryDTO apiaryDTO = apiaryMapper.toDto(apiary);

        restApiaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiaryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApiaries() throws Exception {
        // Initialize the database
        insertedApiary = apiaryRepository.saveAndFlush(apiary);

        // Get all the apiaryList
        restApiaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apiary.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].hiveCount").value(hasItem(DEFAULT_HIVE_COUNT)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].dateSynched").value(hasItem(DEFAULT_DATE_SYNCHED.toString())));
    }

    @Test
    @Transactional
    void getApiary() throws Exception {
        // Initialize the database
        insertedApiary = apiaryRepository.saveAndFlush(apiary);

        // Get the apiary
        restApiaryMockMvc
            .perform(get(ENTITY_API_URL_ID, apiary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apiary.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.idNumber").value(DEFAULT_ID_NUMBER))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER))
            .andExpect(jsonPath("$.hiveCount").value(DEFAULT_HIVE_COUNT))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.dateSynched").value(DEFAULT_DATE_SYNCHED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingApiary() throws Exception {
        // Get the apiary
        restApiaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApiary() throws Exception {
        // Initialize the database
        insertedApiary = apiaryRepository.saveAndFlush(apiary);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the apiary
        Apiary updatedApiary = apiaryRepository.findById(apiary.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApiary are not directly saved in db
        em.detach(updatedApiary);
        updatedApiary
            .name(UPDATED_NAME)
            .idNumber(UPDATED_ID_NUMBER)
            .color(UPDATED_COLOR)
            .location(UPDATED_LOCATION)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .hiveCount(UPDATED_HIVE_COUNT)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED);
        ApiaryDTO apiaryDTO = apiaryMapper.toDto(updatedApiary);

        restApiaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apiaryDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiaryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Apiary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedApiaryToMatchAllProperties(updatedApiary);
    }

    @Test
    @Transactional
    void putNonExistingApiary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiary.setId(longCount.incrementAndGet());

        // Create the Apiary
        ApiaryDTO apiaryDTO = apiaryMapper.toDto(apiary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apiaryDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apiary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApiary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiary.setId(longCount.incrementAndGet());

        // Create the Apiary
        ApiaryDTO apiaryDTO = apiaryMapper.toDto(apiary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(apiaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apiary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApiary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiary.setId(longCount.incrementAndGet());

        // Create the Apiary
        ApiaryDTO apiaryDTO = apiaryMapper.toDto(apiary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiaryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiaryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Apiary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApiaryWithPatch() throws Exception {
        // Initialize the database
        insertedApiary = apiaryRepository.saveAndFlush(apiary);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the apiary using partial update
        Apiary partialUpdatedApiary = new Apiary();
        partialUpdatedApiary.setId(apiary.getId());

        partialUpdatedApiary
            .idNumber(UPDATED_ID_NUMBER)
            .color(UPDATED_COLOR)
            .location(UPDATED_LOCATION)
            .latitude(UPDATED_LATITUDE)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .hiveCount(UPDATED_HIVE_COUNT)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED);

        restApiaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApiary.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApiary))
            )
            .andExpect(status().isOk());

        // Validate the Apiary in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApiaryUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedApiary, apiary), getPersistedApiary(apiary));
    }

    @Test
    @Transactional
    void fullUpdateApiaryWithPatch() throws Exception {
        // Initialize the database
        insertedApiary = apiaryRepository.saveAndFlush(apiary);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the apiary using partial update
        Apiary partialUpdatedApiary = new Apiary();
        partialUpdatedApiary.setId(apiary.getId());

        partialUpdatedApiary
            .name(UPDATED_NAME)
            .idNumber(UPDATED_ID_NUMBER)
            .color(UPDATED_COLOR)
            .location(UPDATED_LOCATION)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .hiveCount(UPDATED_HIVE_COUNT)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED);

        restApiaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApiary.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApiary))
            )
            .andExpect(status().isOk());

        // Validate the Apiary in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApiaryUpdatableFieldsEquals(partialUpdatedApiary, getPersistedApiary(partialUpdatedApiary));
    }

    @Test
    @Transactional
    void patchNonExistingApiary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiary.setId(longCount.incrementAndGet());

        // Create the Apiary
        ApiaryDTO apiaryDTO = apiaryMapper.toDto(apiary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apiaryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(apiaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apiary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApiary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiary.setId(longCount.incrementAndGet());

        // Create the Apiary
        ApiaryDTO apiaryDTO = apiaryMapper.toDto(apiary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(apiaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apiary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApiary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiary.setId(longCount.incrementAndGet());

        // Create the Apiary
        ApiaryDTO apiaryDTO = apiaryMapper.toDto(apiary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiaryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(apiaryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Apiary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApiary() throws Exception {
        // Initialize the database
        insertedApiary = apiaryRepository.saveAndFlush(apiary);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the apiary
        restApiaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, apiary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return apiaryRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Apiary getPersistedApiary(Apiary apiary) {
        return apiaryRepository.findById(apiary.getId()).orElseThrow();
    }

    protected void assertPersistedApiaryToMatchAllProperties(Apiary expectedApiary) {
        assertApiaryAllPropertiesEquals(expectedApiary, getPersistedApiary(expectedApiary));
    }

    protected void assertPersistedApiaryToMatchUpdatableProperties(Apiary expectedApiary) {
        assertApiaryAllUpdatablePropertiesEquals(expectedApiary, getPersistedApiary(expectedApiary));
    }
}
