package rs.hexatech.beeback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rs.hexatech.beeback.domain.GoodHarvestHiveAsserts.*;
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
import rs.hexatech.beeback.domain.GoodHarvestHive;
import rs.hexatech.beeback.repository.GoodHarvestHiveRepository;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.service.dto.GoodHarvestHiveDTO;
import rs.hexatech.beeback.service.mapper.GoodHarvestHiveMapper;

/**
 * Integration tests for the {@link GoodHarvestHiveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GoodHarvestHiveResourceIT {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

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

    private static final Instant DEFAULT_DATE_DELETED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DELETED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/good-harvest-hives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GoodHarvestHiveRepository goodHarvestHiveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoodHarvestHiveMapper goodHarvestHiveMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGoodHarvestHiveMockMvc;

    private GoodHarvestHive goodHarvestHive;

    private GoodHarvestHive insertedGoodHarvestHive;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GoodHarvestHive createEntity() {
        return new GoodHarvestHive()
            .amount(DEFAULT_AMOUNT)
            .externalId(DEFAULT_EXTERNAL_ID)
            .uuid(DEFAULT_UUID)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateModified(DEFAULT_DATE_MODIFIED)
            .dateSynched(DEFAULT_DATE_SYNCHED)
            .dateDeleted(DEFAULT_DATE_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GoodHarvestHive createUpdatedEntity() {
        return new GoodHarvestHive()
            .amount(UPDATED_AMOUNT)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
    }

    @BeforeEach
    public void initTest() {
        goodHarvestHive = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedGoodHarvestHive != null) {
            goodHarvestHiveRepository.delete(insertedGoodHarvestHive);
            insertedGoodHarvestHive = null;
        }
    }

    @Test
    @Transactional
    void createGoodHarvestHive() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the GoodHarvestHive
        GoodHarvestHiveDTO goodHarvestHiveDTO = goodHarvestHiveMapper.toDto(goodHarvestHive);
        var returnedGoodHarvestHiveDTO = om.readValue(
            restGoodHarvestHiveMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(goodHarvestHiveDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GoodHarvestHiveDTO.class
        );

        // Validate the GoodHarvestHive in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGoodHarvestHive = goodHarvestHiveMapper.toEntity(returnedGoodHarvestHiveDTO);
        assertGoodHarvestHiveUpdatableFieldsEquals(returnedGoodHarvestHive, getPersistedGoodHarvestHive(returnedGoodHarvestHive));

        insertedGoodHarvestHive = returnedGoodHarvestHive;
    }

    @Test
    @Transactional
    void createGoodHarvestHiveWithExistingId() throws Exception {
        // Create the GoodHarvestHive with an existing ID
        goodHarvestHive.setId(1L);
        GoodHarvestHiveDTO goodHarvestHiveDTO = goodHarvestHiveMapper.toDto(goodHarvestHive);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGoodHarvestHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(goodHarvestHiveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GoodHarvestHive in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        goodHarvestHive.setExternalId(null);

        // Create the GoodHarvestHive, which fails.
        GoodHarvestHiveDTO goodHarvestHiveDTO = goodHarvestHiveMapper.toDto(goodHarvestHive);

        restGoodHarvestHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(goodHarvestHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        goodHarvestHive.setUuid(null);

        // Create the GoodHarvestHive, which fails.
        GoodHarvestHiveDTO goodHarvestHiveDTO = goodHarvestHiveMapper.toDto(goodHarvestHive);

        restGoodHarvestHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(goodHarvestHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        goodHarvestHive.setDateCreated(null);

        // Create the GoodHarvestHive, which fails.
        GoodHarvestHiveDTO goodHarvestHiveDTO = goodHarvestHiveMapper.toDto(goodHarvestHive);

        restGoodHarvestHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(goodHarvestHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        goodHarvestHive.setDateModified(null);

        // Create the GoodHarvestHive, which fails.
        GoodHarvestHiveDTO goodHarvestHiveDTO = goodHarvestHiveMapper.toDto(goodHarvestHive);

        restGoodHarvestHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(goodHarvestHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSynchedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        goodHarvestHive.setDateSynched(null);

        // Create the GoodHarvestHive, which fails.
        GoodHarvestHiveDTO goodHarvestHiveDTO = goodHarvestHiveMapper.toDto(goodHarvestHive);

        restGoodHarvestHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(goodHarvestHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGoodHarvestHives() throws Exception {
        // Initialize the database
        insertedGoodHarvestHive = goodHarvestHiveRepository.saveAndFlush(goodHarvestHive);

        // Get all the goodHarvestHiveList
        restGoodHarvestHiveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goodHarvestHive.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].dateSynched").value(hasItem(DEFAULT_DATE_SYNCHED.toString())))
            .andExpect(jsonPath("$.[*].dateDeleted").value(hasItem(DEFAULT_DATE_DELETED.toString())));
    }

    @Test
    @Transactional
    void getGoodHarvestHive() throws Exception {
        // Initialize the database
        insertedGoodHarvestHive = goodHarvestHiveRepository.saveAndFlush(goodHarvestHive);

        // Get the goodHarvestHive
        restGoodHarvestHiveMockMvc
            .perform(get(ENTITY_API_URL_ID, goodHarvestHive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(goodHarvestHive.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.dateSynched").value(DEFAULT_DATE_SYNCHED.toString()))
            .andExpect(jsonPath("$.dateDeleted").value(DEFAULT_DATE_DELETED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingGoodHarvestHive() throws Exception {
        // Get the goodHarvestHive
        restGoodHarvestHiveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGoodHarvestHive() throws Exception {
        // Initialize the database
        insertedGoodHarvestHive = goodHarvestHiveRepository.saveAndFlush(goodHarvestHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the goodHarvestHive
        GoodHarvestHive updatedGoodHarvestHive = goodHarvestHiveRepository.findById(goodHarvestHive.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGoodHarvestHive are not directly saved in db
        em.detach(updatedGoodHarvestHive);
        updatedGoodHarvestHive
            .amount(UPDATED_AMOUNT)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
        GoodHarvestHiveDTO goodHarvestHiveDTO = goodHarvestHiveMapper.toDto(updatedGoodHarvestHive);

        restGoodHarvestHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, goodHarvestHiveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(goodHarvestHiveDTO))
            )
            .andExpect(status().isOk());

        // Validate the GoodHarvestHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGoodHarvestHiveToMatchAllProperties(updatedGoodHarvestHive);
    }

    @Test
    @Transactional
    void putNonExistingGoodHarvestHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        goodHarvestHive.setId(longCount.incrementAndGet());

        // Create the GoodHarvestHive
        GoodHarvestHiveDTO goodHarvestHiveDTO = goodHarvestHiveMapper.toDto(goodHarvestHive);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoodHarvestHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, goodHarvestHiveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(goodHarvestHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GoodHarvestHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGoodHarvestHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        goodHarvestHive.setId(longCount.incrementAndGet());

        // Create the GoodHarvestHive
        GoodHarvestHiveDTO goodHarvestHiveDTO = goodHarvestHiveMapper.toDto(goodHarvestHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoodHarvestHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(goodHarvestHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GoodHarvestHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGoodHarvestHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        goodHarvestHive.setId(longCount.incrementAndGet());

        // Create the GoodHarvestHive
        GoodHarvestHiveDTO goodHarvestHiveDTO = goodHarvestHiveMapper.toDto(goodHarvestHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoodHarvestHiveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(goodHarvestHiveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GoodHarvestHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGoodHarvestHiveWithPatch() throws Exception {
        // Initialize the database
        insertedGoodHarvestHive = goodHarvestHiveRepository.saveAndFlush(goodHarvestHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the goodHarvestHive using partial update
        GoodHarvestHive partialUpdatedGoodHarvestHive = new GoodHarvestHive();
        partialUpdatedGoodHarvestHive.setId(goodHarvestHive.getId());

        partialUpdatedGoodHarvestHive.amount(UPDATED_AMOUNT).dateCreated(UPDATED_DATE_CREATED).dateModified(UPDATED_DATE_MODIFIED);

        restGoodHarvestHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGoodHarvestHive.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGoodHarvestHive))
            )
            .andExpect(status().isOk());

        // Validate the GoodHarvestHive in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGoodHarvestHiveUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedGoodHarvestHive, goodHarvestHive),
            getPersistedGoodHarvestHive(goodHarvestHive)
        );
    }

    @Test
    @Transactional
    void fullUpdateGoodHarvestHiveWithPatch() throws Exception {
        // Initialize the database
        insertedGoodHarvestHive = goodHarvestHiveRepository.saveAndFlush(goodHarvestHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the goodHarvestHive using partial update
        GoodHarvestHive partialUpdatedGoodHarvestHive = new GoodHarvestHive();
        partialUpdatedGoodHarvestHive.setId(goodHarvestHive.getId());

        partialUpdatedGoodHarvestHive
            .amount(UPDATED_AMOUNT)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);

        restGoodHarvestHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGoodHarvestHive.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGoodHarvestHive))
            )
            .andExpect(status().isOk());

        // Validate the GoodHarvestHive in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGoodHarvestHiveUpdatableFieldsEquals(
            partialUpdatedGoodHarvestHive,
            getPersistedGoodHarvestHive(partialUpdatedGoodHarvestHive)
        );
    }

    @Test
    @Transactional
    void patchNonExistingGoodHarvestHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        goodHarvestHive.setId(longCount.incrementAndGet());

        // Create the GoodHarvestHive
        GoodHarvestHiveDTO goodHarvestHiveDTO = goodHarvestHiveMapper.toDto(goodHarvestHive);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoodHarvestHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, goodHarvestHiveDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(goodHarvestHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GoodHarvestHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGoodHarvestHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        goodHarvestHive.setId(longCount.incrementAndGet());

        // Create the GoodHarvestHive
        GoodHarvestHiveDTO goodHarvestHiveDTO = goodHarvestHiveMapper.toDto(goodHarvestHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoodHarvestHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(goodHarvestHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GoodHarvestHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGoodHarvestHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        goodHarvestHive.setId(longCount.incrementAndGet());

        // Create the GoodHarvestHive
        GoodHarvestHiveDTO goodHarvestHiveDTO = goodHarvestHiveMapper.toDto(goodHarvestHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoodHarvestHiveMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(goodHarvestHiveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GoodHarvestHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGoodHarvestHive() throws Exception {
        // Initialize the database
        insertedGoodHarvestHive = goodHarvestHiveRepository.saveAndFlush(goodHarvestHive);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the goodHarvestHive
        restGoodHarvestHiveMockMvc
            .perform(delete(ENTITY_API_URL_ID, goodHarvestHive.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return goodHarvestHiveRepository.count();
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

    protected GoodHarvestHive getPersistedGoodHarvestHive(GoodHarvestHive goodHarvestHive) {
        return goodHarvestHiveRepository.findById(goodHarvestHive.getId()).orElseThrow();
    }

    protected void assertPersistedGoodHarvestHiveToMatchAllProperties(GoodHarvestHive expectedGoodHarvestHive) {
        assertGoodHarvestHiveAllPropertiesEquals(expectedGoodHarvestHive, getPersistedGoodHarvestHive(expectedGoodHarvestHive));
    }

    protected void assertPersistedGoodHarvestHiveToMatchUpdatableProperties(GoodHarvestHive expectedGoodHarvestHive) {
        assertGoodHarvestHiveAllUpdatablePropertiesEquals(expectedGoodHarvestHive, getPersistedGoodHarvestHive(expectedGoodHarvestHive));
    }
}
