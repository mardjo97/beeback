package rs.hexatech.beeback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rs.hexatech.beeback.domain.FeedingHiveAsserts.*;
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
import rs.hexatech.beeback.domain.FeedingHive;
import rs.hexatech.beeback.repository.FeedingHiveRepository;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.service.dto.FeedingHiveDTO;
import rs.hexatech.beeback.service.mapper.FeedingHiveMapper;

/**
 * Integration tests for the {@link FeedingHiveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeedingHiveResourceIT {

    private static final Double DEFAULT_FOOD_AMOUNT = 1D;
    private static final Double UPDATED_FOOD_AMOUNT = 2D;

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

    private static final String ENTITY_API_URL = "/api/feeding-hives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeedingHiveRepository feedingHiveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeedingHiveMapper feedingHiveMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeedingHiveMockMvc;

    private FeedingHive feedingHive;

    private FeedingHive insertedFeedingHive;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedingHive createEntity() {
        return new FeedingHive()
            .foodAmount(DEFAULT_FOOD_AMOUNT)
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
    public static FeedingHive createUpdatedEntity() {
        return new FeedingHive()
            .foodAmount(UPDATED_FOOD_AMOUNT)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
    }

    @BeforeEach
    public void initTest() {
        feedingHive = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFeedingHive != null) {
            feedingHiveRepository.delete(insertedFeedingHive);
            insertedFeedingHive = null;
        }
    }

    @Test
    @Transactional
    void createFeedingHive() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FeedingHive
        FeedingHiveDTO feedingHiveDTO = feedingHiveMapper.toDto(feedingHive);
        var returnedFeedingHiveDTO = om.readValue(
            restFeedingHiveMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedingHiveDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FeedingHiveDTO.class
        );

        // Validate the FeedingHive in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFeedingHive = feedingHiveMapper.toEntity(returnedFeedingHiveDTO);
        assertFeedingHiveUpdatableFieldsEquals(returnedFeedingHive, getPersistedFeedingHive(returnedFeedingHive));

        insertedFeedingHive = returnedFeedingHive;
    }

    @Test
    @Transactional
    void createFeedingHiveWithExistingId() throws Exception {
        // Create the FeedingHive with an existing ID
        feedingHive.setId(1L);
        FeedingHiveDTO feedingHiveDTO = feedingHiveMapper.toDto(feedingHive);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedingHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedingHiveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FeedingHive in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedingHive.setExternalId(null);

        // Create the FeedingHive, which fails.
        FeedingHiveDTO feedingHiveDTO = feedingHiveMapper.toDto(feedingHive);

        restFeedingHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedingHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedingHive.setUuid(null);

        // Create the FeedingHive, which fails.
        FeedingHiveDTO feedingHiveDTO = feedingHiveMapper.toDto(feedingHive);

        restFeedingHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedingHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedingHive.setDateCreated(null);

        // Create the FeedingHive, which fails.
        FeedingHiveDTO feedingHiveDTO = feedingHiveMapper.toDto(feedingHive);

        restFeedingHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedingHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedingHive.setDateModified(null);

        // Create the FeedingHive, which fails.
        FeedingHiveDTO feedingHiveDTO = feedingHiveMapper.toDto(feedingHive);

        restFeedingHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedingHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSynchedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedingHive.setDateSynched(null);

        // Create the FeedingHive, which fails.
        FeedingHiveDTO feedingHiveDTO = feedingHiveMapper.toDto(feedingHive);

        restFeedingHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedingHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFeedingHives() throws Exception {
        // Initialize the database
        insertedFeedingHive = feedingHiveRepository.saveAndFlush(feedingHive);

        // Get all the feedingHiveList
        restFeedingHiveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedingHive.getId().intValue())))
            .andExpect(jsonPath("$.[*].foodAmount").value(hasItem(DEFAULT_FOOD_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].dateSynched").value(hasItem(DEFAULT_DATE_SYNCHED.toString())))
            .andExpect(jsonPath("$.[*].dateDeleted").value(hasItem(DEFAULT_DATE_DELETED.toString())));
    }

    @Test
    @Transactional
    void getFeedingHive() throws Exception {
        // Initialize the database
        insertedFeedingHive = feedingHiveRepository.saveAndFlush(feedingHive);

        // Get the feedingHive
        restFeedingHiveMockMvc
            .perform(get(ENTITY_API_URL_ID, feedingHive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feedingHive.getId().intValue()))
            .andExpect(jsonPath("$.foodAmount").value(DEFAULT_FOOD_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.dateSynched").value(DEFAULT_DATE_SYNCHED.toString()))
            .andExpect(jsonPath("$.dateDeleted").value(DEFAULT_DATE_DELETED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFeedingHive() throws Exception {
        // Get the feedingHive
        restFeedingHiveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeedingHive() throws Exception {
        // Initialize the database
        insertedFeedingHive = feedingHiveRepository.saveAndFlush(feedingHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedingHive
        FeedingHive updatedFeedingHive = feedingHiveRepository.findById(feedingHive.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeedingHive are not directly saved in db
        em.detach(updatedFeedingHive);
        updatedFeedingHive
            .foodAmount(UPDATED_FOOD_AMOUNT)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
        FeedingHiveDTO feedingHiveDTO = feedingHiveMapper.toDto(updatedFeedingHive);

        restFeedingHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedingHiveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedingHiveDTO))
            )
            .andExpect(status().isOk());

        // Validate the FeedingHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeedingHiveToMatchAllProperties(updatedFeedingHive);
    }

    @Test
    @Transactional
    void putNonExistingFeedingHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedingHive.setId(longCount.incrementAndGet());

        // Create the FeedingHive
        FeedingHiveDTO feedingHiveDTO = feedingHiveMapper.toDto(feedingHive);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedingHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedingHiveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedingHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedingHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeedingHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedingHive.setId(longCount.incrementAndGet());

        // Create the FeedingHive
        FeedingHiveDTO feedingHiveDTO = feedingHiveMapper.toDto(feedingHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedingHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedingHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedingHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeedingHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedingHive.setId(longCount.incrementAndGet());

        // Create the FeedingHive
        FeedingHiveDTO feedingHiveDTO = feedingHiveMapper.toDto(feedingHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedingHiveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedingHiveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedingHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeedingHiveWithPatch() throws Exception {
        // Initialize the database
        insertedFeedingHive = feedingHiveRepository.saveAndFlush(feedingHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedingHive using partial update
        FeedingHive partialUpdatedFeedingHive = new FeedingHive();
        partialUpdatedFeedingHive.setId(feedingHive.getId());

        partialUpdatedFeedingHive.foodAmount(UPDATED_FOOD_AMOUNT).externalId(UPDATED_EXTERNAL_ID).uuid(UPDATED_UUID);

        restFeedingHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedingHive.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedingHive))
            )
            .andExpect(status().isOk());

        // Validate the FeedingHive in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedingHiveUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFeedingHive, feedingHive),
            getPersistedFeedingHive(feedingHive)
        );
    }

    @Test
    @Transactional
    void fullUpdateFeedingHiveWithPatch() throws Exception {
        // Initialize the database
        insertedFeedingHive = feedingHiveRepository.saveAndFlush(feedingHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedingHive using partial update
        FeedingHive partialUpdatedFeedingHive = new FeedingHive();
        partialUpdatedFeedingHive.setId(feedingHive.getId());

        partialUpdatedFeedingHive
            .foodAmount(UPDATED_FOOD_AMOUNT)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);

        restFeedingHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedingHive.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedingHive))
            )
            .andExpect(status().isOk());

        // Validate the FeedingHive in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedingHiveUpdatableFieldsEquals(partialUpdatedFeedingHive, getPersistedFeedingHive(partialUpdatedFeedingHive));
    }

    @Test
    @Transactional
    void patchNonExistingFeedingHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedingHive.setId(longCount.incrementAndGet());

        // Create the FeedingHive
        FeedingHiveDTO feedingHiveDTO = feedingHiveMapper.toDto(feedingHive);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedingHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feedingHiveDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedingHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedingHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeedingHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedingHive.setId(longCount.incrementAndGet());

        // Create the FeedingHive
        FeedingHiveDTO feedingHiveDTO = feedingHiveMapper.toDto(feedingHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedingHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedingHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedingHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeedingHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedingHive.setId(longCount.incrementAndGet());

        // Create the FeedingHive
        FeedingHiveDTO feedingHiveDTO = feedingHiveMapper.toDto(feedingHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedingHiveMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(feedingHiveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedingHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeedingHive() throws Exception {
        // Initialize the database
        insertedFeedingHive = feedingHiveRepository.saveAndFlush(feedingHive);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the feedingHive
        restFeedingHiveMockMvc
            .perform(delete(ENTITY_API_URL_ID, feedingHive.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return feedingHiveRepository.count();
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

    protected FeedingHive getPersistedFeedingHive(FeedingHive feedingHive) {
        return feedingHiveRepository.findById(feedingHive.getId()).orElseThrow();
    }

    protected void assertPersistedFeedingHiveToMatchAllProperties(FeedingHive expectedFeedingHive) {
        assertFeedingHiveAllPropertiesEquals(expectedFeedingHive, getPersistedFeedingHive(expectedFeedingHive));
    }

    protected void assertPersistedFeedingHiveToMatchUpdatableProperties(FeedingHive expectedFeedingHive) {
        assertFeedingHiveAllUpdatablePropertiesEquals(expectedFeedingHive, getPersistedFeedingHive(expectedFeedingHive));
    }
}
