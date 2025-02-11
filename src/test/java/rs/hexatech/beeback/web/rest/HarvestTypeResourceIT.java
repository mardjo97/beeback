package rs.hexatech.beeback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rs.hexatech.beeback.domain.HarvestTypeAsserts.*;
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
import rs.hexatech.beeback.domain.HarvestType;
import rs.hexatech.beeback.repository.HarvestTypeRepository;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.service.dto.HarvestTypeDTO;
import rs.hexatech.beeback.service.mapper.HarvestTypeMapper;

/**
 * Integration tests for the {@link HarvestTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HarvestTypeResourceIT {

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

    private static final String ENTITY_API_URL = "/api/harvest-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HarvestTypeRepository harvestTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HarvestTypeMapper harvestTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHarvestTypeMockMvc;

    private HarvestType harvestType;

    private HarvestType insertedHarvestType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HarvestType createEntity() {
        return new HarvestType()
            .name(DEFAULT_NAME)
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
    public static HarvestType createUpdatedEntity() {
        return new HarvestType()
            .name(UPDATED_NAME)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
    }

    @BeforeEach
    public void initTest() {
        harvestType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHarvestType != null) {
            harvestTypeRepository.delete(insertedHarvestType);
            insertedHarvestType = null;
        }
    }

    @Test
    @Transactional
    void createHarvestType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HarvestType
        HarvestTypeDTO harvestTypeDTO = harvestTypeMapper.toDto(harvestType);
        var returnedHarvestTypeDTO = om.readValue(
            restHarvestTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HarvestTypeDTO.class
        );

        // Validate the HarvestType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHarvestType = harvestTypeMapper.toEntity(returnedHarvestTypeDTO);
        assertHarvestTypeUpdatableFieldsEquals(returnedHarvestType, getPersistedHarvestType(returnedHarvestType));

        insertedHarvestType = returnedHarvestType;
    }

    @Test
    @Transactional
    void createHarvestTypeWithExistingId() throws Exception {
        // Create the HarvestType with an existing ID
        harvestType.setId(1L);
        HarvestTypeDTO harvestTypeDTO = harvestTypeMapper.toDto(harvestType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHarvestTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HarvestType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        harvestType.setExternalId(null);

        // Create the HarvestType, which fails.
        HarvestTypeDTO harvestTypeDTO = harvestTypeMapper.toDto(harvestType);

        restHarvestTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        harvestType.setUuid(null);

        // Create the HarvestType, which fails.
        HarvestTypeDTO harvestTypeDTO = harvestTypeMapper.toDto(harvestType);

        restHarvestTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        harvestType.setDateCreated(null);

        // Create the HarvestType, which fails.
        HarvestTypeDTO harvestTypeDTO = harvestTypeMapper.toDto(harvestType);

        restHarvestTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        harvestType.setDateModified(null);

        // Create the HarvestType, which fails.
        HarvestTypeDTO harvestTypeDTO = harvestTypeMapper.toDto(harvestType);

        restHarvestTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSynchedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        harvestType.setDateSynched(null);

        // Create the HarvestType, which fails.
        HarvestTypeDTO harvestTypeDTO = harvestTypeMapper.toDto(harvestType);

        restHarvestTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHarvestTypes() throws Exception {
        // Initialize the database
        insertedHarvestType = harvestTypeRepository.saveAndFlush(harvestType);

        // Get all the harvestTypeList
        restHarvestTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(harvestType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].dateSynched").value(hasItem(DEFAULT_DATE_SYNCHED.toString())))
            .andExpect(jsonPath("$.[*].dateDeleted").value(hasItem(DEFAULT_DATE_DELETED.toString())));
    }

    @Test
    @Transactional
    void getHarvestType() throws Exception {
        // Initialize the database
        insertedHarvestType = harvestTypeRepository.saveAndFlush(harvestType);

        // Get the harvestType
        restHarvestTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, harvestType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(harvestType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.dateSynched").value(DEFAULT_DATE_SYNCHED.toString()))
            .andExpect(jsonPath("$.dateDeleted").value(DEFAULT_DATE_DELETED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHarvestType() throws Exception {
        // Get the harvestType
        restHarvestTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHarvestType() throws Exception {
        // Initialize the database
        insertedHarvestType = harvestTypeRepository.saveAndFlush(harvestType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the harvestType
        HarvestType updatedHarvestType = harvestTypeRepository.findById(harvestType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHarvestType are not directly saved in db
        em.detach(updatedHarvestType);
        updatedHarvestType
            .name(UPDATED_NAME)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
        HarvestTypeDTO harvestTypeDTO = harvestTypeMapper.toDto(updatedHarvestType);

        restHarvestTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, harvestTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(harvestTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the HarvestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHarvestTypeToMatchAllProperties(updatedHarvestType);
    }

    @Test
    @Transactional
    void putNonExistingHarvestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        harvestType.setId(longCount.incrementAndGet());

        // Create the HarvestType
        HarvestTypeDTO harvestTypeDTO = harvestTypeMapper.toDto(harvestType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHarvestTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, harvestTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(harvestTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HarvestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHarvestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        harvestType.setId(longCount.incrementAndGet());

        // Create the HarvestType
        HarvestTypeDTO harvestTypeDTO = harvestTypeMapper.toDto(harvestType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHarvestTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(harvestTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HarvestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHarvestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        harvestType.setId(longCount.incrementAndGet());

        // Create the HarvestType
        HarvestTypeDTO harvestTypeDTO = harvestTypeMapper.toDto(harvestType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHarvestTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HarvestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHarvestTypeWithPatch() throws Exception {
        // Initialize the database
        insertedHarvestType = harvestTypeRepository.saveAndFlush(harvestType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the harvestType using partial update
        HarvestType partialUpdatedHarvestType = new HarvestType();
        partialUpdatedHarvestType.setId(harvestType.getId());

        partialUpdatedHarvestType.name(UPDATED_NAME).uuid(UPDATED_UUID).dateDeleted(UPDATED_DATE_DELETED);

        restHarvestTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHarvestType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHarvestType))
            )
            .andExpect(status().isOk());

        // Validate the HarvestType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHarvestTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHarvestType, harvestType),
            getPersistedHarvestType(harvestType)
        );
    }

    @Test
    @Transactional
    void fullUpdateHarvestTypeWithPatch() throws Exception {
        // Initialize the database
        insertedHarvestType = harvestTypeRepository.saveAndFlush(harvestType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the harvestType using partial update
        HarvestType partialUpdatedHarvestType = new HarvestType();
        partialUpdatedHarvestType.setId(harvestType.getId());

        partialUpdatedHarvestType
            .name(UPDATED_NAME)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);

        restHarvestTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHarvestType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHarvestType))
            )
            .andExpect(status().isOk());

        // Validate the HarvestType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHarvestTypeUpdatableFieldsEquals(partialUpdatedHarvestType, getPersistedHarvestType(partialUpdatedHarvestType));
    }

    @Test
    @Transactional
    void patchNonExistingHarvestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        harvestType.setId(longCount.incrementAndGet());

        // Create the HarvestType
        HarvestTypeDTO harvestTypeDTO = harvestTypeMapper.toDto(harvestType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHarvestTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, harvestTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(harvestTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HarvestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHarvestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        harvestType.setId(longCount.incrementAndGet());

        // Create the HarvestType
        HarvestTypeDTO harvestTypeDTO = harvestTypeMapper.toDto(harvestType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHarvestTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(harvestTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HarvestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHarvestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        harvestType.setId(longCount.incrementAndGet());

        // Create the HarvestType
        HarvestTypeDTO harvestTypeDTO = harvestTypeMapper.toDto(harvestType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHarvestTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(harvestTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HarvestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHarvestType() throws Exception {
        // Initialize the database
        insertedHarvestType = harvestTypeRepository.saveAndFlush(harvestType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the harvestType
        restHarvestTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, harvestType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return harvestTypeRepository.count();
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

    protected HarvestType getPersistedHarvestType(HarvestType harvestType) {
        return harvestTypeRepository.findById(harvestType.getId()).orElseThrow();
    }

    protected void assertPersistedHarvestTypeToMatchAllProperties(HarvestType expectedHarvestType) {
        assertHarvestTypeAllPropertiesEquals(expectedHarvestType, getPersistedHarvestType(expectedHarvestType));
    }

    protected void assertPersistedHarvestTypeToMatchUpdatableProperties(HarvestType expectedHarvestType) {
        assertHarvestTypeAllUpdatablePropertiesEquals(expectedHarvestType, getPersistedHarvestType(expectedHarvestType));
    }
}
