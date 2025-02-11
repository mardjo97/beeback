package rs.hexatech.beeback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rs.hexatech.beeback.domain.HarvestAsserts.*;
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
import rs.hexatech.beeback.domain.Harvest;
import rs.hexatech.beeback.repository.HarvestRepository;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.service.dto.HarvestDTO;
import rs.hexatech.beeback.service.mapper.HarvestMapper;

/**
 * Integration tests for the {@link HarvestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HarvestResourceIT {

    private static final Integer DEFAULT_HIVE_FRAMES = 1;
    private static final Integer UPDATED_HIVE_FRAMES = 2;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Instant DEFAULT_DATE_COLLECTED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_COLLECTED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_GROUP = "BBBBBBBBBB";

    private static final Integer DEFAULT_GROUP_RECORD_ID = 1;
    private static final Integer UPDATED_GROUP_RECORD_ID = 2;

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

    private static final String ENTITY_API_URL = "/api/harvests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HarvestRepository harvestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HarvestMapper harvestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHarvestMockMvc;

    private Harvest harvest;

    private Harvest insertedHarvest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Harvest createEntity() {
        return new Harvest()
            .hiveFrames(DEFAULT_HIVE_FRAMES)
            .amount(DEFAULT_AMOUNT)
            .dateCollected(DEFAULT_DATE_COLLECTED)
            .group(DEFAULT_GROUP)
            .groupRecordId(DEFAULT_GROUP_RECORD_ID)
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
    public static Harvest createUpdatedEntity() {
        return new Harvest()
            .hiveFrames(UPDATED_HIVE_FRAMES)
            .amount(UPDATED_AMOUNT)
            .dateCollected(UPDATED_DATE_COLLECTED)
            .group(UPDATED_GROUP)
            .groupRecordId(UPDATED_GROUP_RECORD_ID)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
    }

    @BeforeEach
    public void initTest() {
        harvest = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHarvest != null) {
            harvestRepository.delete(insertedHarvest);
            insertedHarvest = null;
        }
    }

    @Test
    @Transactional
    void createHarvest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Harvest
        HarvestDTO harvestDTO = harvestMapper.toDto(harvest);
        var returnedHarvestDTO = om.readValue(
            restHarvestMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HarvestDTO.class
        );

        // Validate the Harvest in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHarvest = harvestMapper.toEntity(returnedHarvestDTO);
        assertHarvestUpdatableFieldsEquals(returnedHarvest, getPersistedHarvest(returnedHarvest));

        insertedHarvest = returnedHarvest;
    }

    @Test
    @Transactional
    void createHarvestWithExistingId() throws Exception {
        // Create the Harvest with an existing ID
        harvest.setId(1L);
        HarvestDTO harvestDTO = harvestMapper.toDto(harvest);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHarvestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Harvest in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        harvest.setExternalId(null);

        // Create the Harvest, which fails.
        HarvestDTO harvestDTO = harvestMapper.toDto(harvest);

        restHarvestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        harvest.setUuid(null);

        // Create the Harvest, which fails.
        HarvestDTO harvestDTO = harvestMapper.toDto(harvest);

        restHarvestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        harvest.setDateCreated(null);

        // Create the Harvest, which fails.
        HarvestDTO harvestDTO = harvestMapper.toDto(harvest);

        restHarvestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        harvest.setDateModified(null);

        // Create the Harvest, which fails.
        HarvestDTO harvestDTO = harvestMapper.toDto(harvest);

        restHarvestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSynchedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        harvest.setDateSynched(null);

        // Create the Harvest, which fails.
        HarvestDTO harvestDTO = harvestMapper.toDto(harvest);

        restHarvestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHarvests() throws Exception {
        // Initialize the database
        insertedHarvest = harvestRepository.saveAndFlush(harvest);

        // Get all the harvestList
        restHarvestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(harvest.getId().intValue())))
            .andExpect(jsonPath("$.[*].hiveFrames").value(hasItem(DEFAULT_HIVE_FRAMES)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].dateCollected").value(hasItem(DEFAULT_DATE_COLLECTED.toString())))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP)))
            .andExpect(jsonPath("$.[*].groupRecordId").value(hasItem(DEFAULT_GROUP_RECORD_ID)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].dateSynched").value(hasItem(DEFAULT_DATE_SYNCHED.toString())))
            .andExpect(jsonPath("$.[*].dateDeleted").value(hasItem(DEFAULT_DATE_DELETED.toString())));
    }

    @Test
    @Transactional
    void getHarvest() throws Exception {
        // Initialize the database
        insertedHarvest = harvestRepository.saveAndFlush(harvest);

        // Get the harvest
        restHarvestMockMvc
            .perform(get(ENTITY_API_URL_ID, harvest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(harvest.getId().intValue()))
            .andExpect(jsonPath("$.hiveFrames").value(DEFAULT_HIVE_FRAMES))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.dateCollected").value(DEFAULT_DATE_COLLECTED.toString()))
            .andExpect(jsonPath("$.group").value(DEFAULT_GROUP))
            .andExpect(jsonPath("$.groupRecordId").value(DEFAULT_GROUP_RECORD_ID))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.dateSynched").value(DEFAULT_DATE_SYNCHED.toString()))
            .andExpect(jsonPath("$.dateDeleted").value(DEFAULT_DATE_DELETED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHarvest() throws Exception {
        // Get the harvest
        restHarvestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHarvest() throws Exception {
        // Initialize the database
        insertedHarvest = harvestRepository.saveAndFlush(harvest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the harvest
        Harvest updatedHarvest = harvestRepository.findById(harvest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHarvest are not directly saved in db
        em.detach(updatedHarvest);
        updatedHarvest
            .hiveFrames(UPDATED_HIVE_FRAMES)
            .amount(UPDATED_AMOUNT)
            .dateCollected(UPDATED_DATE_COLLECTED)
            .group(UPDATED_GROUP)
            .groupRecordId(UPDATED_GROUP_RECORD_ID)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
        HarvestDTO harvestDTO = harvestMapper.toDto(updatedHarvest);

        restHarvestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, harvestDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestDTO))
            )
            .andExpect(status().isOk());

        // Validate the Harvest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHarvestToMatchAllProperties(updatedHarvest);
    }

    @Test
    @Transactional
    void putNonExistingHarvest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        harvest.setId(longCount.incrementAndGet());

        // Create the Harvest
        HarvestDTO harvestDTO = harvestMapper.toDto(harvest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHarvestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, harvestDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Harvest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHarvest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        harvest.setId(longCount.incrementAndGet());

        // Create the Harvest
        HarvestDTO harvestDTO = harvestMapper.toDto(harvest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHarvestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(harvestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Harvest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHarvest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        harvest.setId(longCount.incrementAndGet());

        // Create the Harvest
        HarvestDTO harvestDTO = harvestMapper.toDto(harvest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHarvestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(harvestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Harvest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHarvestWithPatch() throws Exception {
        // Initialize the database
        insertedHarvest = harvestRepository.saveAndFlush(harvest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the harvest using partial update
        Harvest partialUpdatedHarvest = new Harvest();
        partialUpdatedHarvest.setId(harvest.getId());

        partialUpdatedHarvest
            .hiveFrames(UPDATED_HIVE_FRAMES)
            .dateCollected(UPDATED_DATE_COLLECTED)
            .group(UPDATED_GROUP)
            .groupRecordId(UPDATED_GROUP_RECORD_ID)
            .uuid(UPDATED_UUID)
            .dateDeleted(UPDATED_DATE_DELETED);

        restHarvestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHarvest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHarvest))
            )
            .andExpect(status().isOk());

        // Validate the Harvest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHarvestUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedHarvest, harvest), getPersistedHarvest(harvest));
    }

    @Test
    @Transactional
    void fullUpdateHarvestWithPatch() throws Exception {
        // Initialize the database
        insertedHarvest = harvestRepository.saveAndFlush(harvest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the harvest using partial update
        Harvest partialUpdatedHarvest = new Harvest();
        partialUpdatedHarvest.setId(harvest.getId());

        partialUpdatedHarvest
            .hiveFrames(UPDATED_HIVE_FRAMES)
            .amount(UPDATED_AMOUNT)
            .dateCollected(UPDATED_DATE_COLLECTED)
            .group(UPDATED_GROUP)
            .groupRecordId(UPDATED_GROUP_RECORD_ID)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);

        restHarvestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHarvest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHarvest))
            )
            .andExpect(status().isOk());

        // Validate the Harvest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHarvestUpdatableFieldsEquals(partialUpdatedHarvest, getPersistedHarvest(partialUpdatedHarvest));
    }

    @Test
    @Transactional
    void patchNonExistingHarvest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        harvest.setId(longCount.incrementAndGet());

        // Create the Harvest
        HarvestDTO harvestDTO = harvestMapper.toDto(harvest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHarvestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, harvestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(harvestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Harvest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHarvest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        harvest.setId(longCount.incrementAndGet());

        // Create the Harvest
        HarvestDTO harvestDTO = harvestMapper.toDto(harvest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHarvestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(harvestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Harvest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHarvest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        harvest.setId(longCount.incrementAndGet());

        // Create the Harvest
        HarvestDTO harvestDTO = harvestMapper.toDto(harvest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHarvestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(harvestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Harvest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHarvest() throws Exception {
        // Initialize the database
        insertedHarvest = harvestRepository.saveAndFlush(harvest);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the harvest
        restHarvestMockMvc
            .perform(delete(ENTITY_API_URL_ID, harvest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return harvestRepository.count();
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

    protected Harvest getPersistedHarvest(Harvest harvest) {
        return harvestRepository.findById(harvest.getId()).orElseThrow();
    }

    protected void assertPersistedHarvestToMatchAllProperties(Harvest expectedHarvest) {
        assertHarvestAllPropertiesEquals(expectedHarvest, getPersistedHarvest(expectedHarvest));
    }

    protected void assertPersistedHarvestToMatchUpdatableProperties(Harvest expectedHarvest) {
        assertHarvestAllUpdatablePropertiesEquals(expectedHarvest, getPersistedHarvest(expectedHarvest));
    }
}
