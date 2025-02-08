package rs.hexatech.beeback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rs.hexatech.beeback.domain.HiveTypeAsserts.*;
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
import rs.hexatech.beeback.domain.HiveType;
import rs.hexatech.beeback.repository.HiveTypeRepository;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.service.dto.HiveTypeDTO;
import rs.hexatech.beeback.service.mapper.HiveTypeMapper;

/**
 * Integration tests for the {@link HiveTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HiveTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/hive-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HiveTypeRepository hiveTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HiveTypeMapper hiveTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHiveTypeMockMvc;

    private HiveType hiveType;

    private HiveType insertedHiveType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HiveType createEntity() {
        return new HiveType()
            .Name(DEFAULT_NAME)
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
    public static HiveType createUpdatedEntity() {
        return new HiveType()
            .Name(UPDATED_NAME)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
    }

    @BeforeEach
    public void initTest() {
        hiveType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHiveType != null) {
            hiveTypeRepository.delete(insertedHiveType);
            insertedHiveType = null;
        }
    }

    @Test
    @Transactional
    void createHiveType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HiveType
        HiveTypeDTO hiveTypeDTO = hiveTypeMapper.toDto(hiveType);
        var returnedHiveTypeDTO = om.readValue(
            restHiveTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HiveTypeDTO.class
        );

        // Validate the HiveType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHiveType = hiveTypeMapper.toEntity(returnedHiveTypeDTO);
        assertHiveTypeUpdatableFieldsEquals(returnedHiveType, getPersistedHiveType(returnedHiveType));

        insertedHiveType = returnedHiveType;
    }

    @Test
    @Transactional
    void createHiveTypeWithExistingId() throws Exception {
        // Create the HiveType with an existing ID
        hiveType.setId(1L);
        HiveTypeDTO hiveTypeDTO = hiveTypeMapper.toDto(hiveType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHiveTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HiveType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hiveType.setName(null);

        // Create the HiveType, which fails.
        HiveTypeDTO hiveTypeDTO = hiveTypeMapper.toDto(hiveType);

        restHiveTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExternalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hiveType.setExternalId(null);

        // Create the HiveType, which fails.
        HiveTypeDTO hiveTypeDTO = hiveTypeMapper.toDto(hiveType);

        restHiveTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hiveType.setUuid(null);

        // Create the HiveType, which fails.
        HiveTypeDTO hiveTypeDTO = hiveTypeMapper.toDto(hiveType);

        restHiveTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hiveType.setDateCreated(null);

        // Create the HiveType, which fails.
        HiveTypeDTO hiveTypeDTO = hiveTypeMapper.toDto(hiveType);

        restHiveTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hiveType.setDateModified(null);

        // Create the HiveType, which fails.
        HiveTypeDTO hiveTypeDTO = hiveTypeMapper.toDto(hiveType);

        restHiveTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSynchedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hiveType.setDateSynched(null);

        // Create the HiveType, which fails.
        HiveTypeDTO hiveTypeDTO = hiveTypeMapper.toDto(hiveType);

        restHiveTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHiveTypes() throws Exception {
        // Initialize the database
        insertedHiveType = hiveTypeRepository.saveAndFlush(hiveType);

        // Get all the hiveTypeList
        restHiveTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hiveType.getId().intValue())))
            .andExpect(jsonPath("$.[*].Name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].dateSynched").value(hasItem(DEFAULT_DATE_SYNCHED.toString())))
            .andExpect(jsonPath("$.[*].dateDeleted").value(hasItem(DEFAULT_DATE_DELETED.toString())));
    }

    @Test
    @Transactional
    void getHiveType() throws Exception {
        // Initialize the database
        insertedHiveType = hiveTypeRepository.saveAndFlush(hiveType);

        // Get the hiveType
        restHiveTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, hiveType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hiveType.getId().intValue()))
            .andExpect(jsonPath("$.Name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.dateSynched").value(DEFAULT_DATE_SYNCHED.toString()))
            .andExpect(jsonPath("$.dateDeleted").value(DEFAULT_DATE_DELETED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHiveType() throws Exception {
        // Get the hiveType
        restHiveTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHiveType() throws Exception {
        // Initialize the database
        insertedHiveType = hiveTypeRepository.saveAndFlush(hiveType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hiveType
        HiveType updatedHiveType = hiveTypeRepository.findById(hiveType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHiveType are not directly saved in db
        em.detach(updatedHiveType);
        updatedHiveType
            .Name(UPDATED_NAME)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
        HiveTypeDTO hiveTypeDTO = hiveTypeMapper.toDto(updatedHiveType);

        restHiveTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hiveTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hiveTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the HiveType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHiveTypeToMatchAllProperties(updatedHiveType);
    }

    @Test
    @Transactional
    void putNonExistingHiveType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiveType.setId(longCount.incrementAndGet());

        // Create the HiveType
        HiveTypeDTO hiveTypeDTO = hiveTypeMapper.toDto(hiveType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHiveTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hiveTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hiveTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HiveType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHiveType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiveType.setId(longCount.incrementAndGet());

        // Create the HiveType
        HiveTypeDTO hiveTypeDTO = hiveTypeMapper.toDto(hiveType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHiveTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hiveTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HiveType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHiveType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiveType.setId(longCount.incrementAndGet());

        // Create the HiveType
        HiveTypeDTO hiveTypeDTO = hiveTypeMapper.toDto(hiveType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHiveTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HiveType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHiveTypeWithPatch() throws Exception {
        // Initialize the database
        insertedHiveType = hiveTypeRepository.saveAndFlush(hiveType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hiveType using partial update
        HiveType partialUpdatedHiveType = new HiveType();
        partialUpdatedHiveType.setId(hiveType.getId());

        partialUpdatedHiveType
            .Name(UPDATED_NAME)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED);

        restHiveTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHiveType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHiveType))
            )
            .andExpect(status().isOk());

        // Validate the HiveType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHiveTypeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedHiveType, hiveType), getPersistedHiveType(hiveType));
    }

    @Test
    @Transactional
    void fullUpdateHiveTypeWithPatch() throws Exception {
        // Initialize the database
        insertedHiveType = hiveTypeRepository.saveAndFlush(hiveType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hiveType using partial update
        HiveType partialUpdatedHiveType = new HiveType();
        partialUpdatedHiveType.setId(hiveType.getId());

        partialUpdatedHiveType
            .Name(UPDATED_NAME)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);

        restHiveTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHiveType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHiveType))
            )
            .andExpect(status().isOk());

        // Validate the HiveType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHiveTypeUpdatableFieldsEquals(partialUpdatedHiveType, getPersistedHiveType(partialUpdatedHiveType));
    }

    @Test
    @Transactional
    void patchNonExistingHiveType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiveType.setId(longCount.incrementAndGet());

        // Create the HiveType
        HiveTypeDTO hiveTypeDTO = hiveTypeMapper.toDto(hiveType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHiveTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hiveTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hiveTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HiveType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHiveType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiveType.setId(longCount.incrementAndGet());

        // Create the HiveType
        HiveTypeDTO hiveTypeDTO = hiveTypeMapper.toDto(hiveType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHiveTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hiveTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HiveType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHiveType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiveType.setId(longCount.incrementAndGet());

        // Create the HiveType
        HiveTypeDTO hiveTypeDTO = hiveTypeMapper.toDto(hiveType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHiveTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hiveTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HiveType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHiveType() throws Exception {
        // Initialize the database
        insertedHiveType = hiveTypeRepository.saveAndFlush(hiveType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hiveType
        restHiveTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, hiveType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hiveTypeRepository.count();
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

    protected HiveType getPersistedHiveType(HiveType hiveType) {
        return hiveTypeRepository.findById(hiveType.getId()).orElseThrow();
    }

    protected void assertPersistedHiveTypeToMatchAllProperties(HiveType expectedHiveType) {
        assertHiveTypeAllPropertiesEquals(expectedHiveType, getPersistedHiveType(expectedHiveType));
    }

    protected void assertPersistedHiveTypeToMatchUpdatableProperties(HiveType expectedHiveType) {
        assertHiveTypeAllUpdatablePropertiesEquals(expectedHiveType, getPersistedHiveType(expectedHiveType));
    }
}
