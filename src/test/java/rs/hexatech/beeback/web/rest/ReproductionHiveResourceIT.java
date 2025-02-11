package rs.hexatech.beeback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rs.hexatech.beeback.domain.ReproductionHiveAsserts.*;
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
import rs.hexatech.beeback.domain.ReproductionHive;
import rs.hexatech.beeback.repository.ReproductionHiveRepository;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.service.dto.ReproductionHiveDTO;
import rs.hexatech.beeback.service.mapper.ReproductionHiveMapper;

/**
 * Integration tests for the {@link ReproductionHiveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReproductionHiveResourceIT {

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/reproduction-hives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReproductionHiveRepository reproductionHiveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReproductionHiveMapper reproductionHiveMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReproductionHiveMockMvc;

    private ReproductionHive reproductionHive;

    private ReproductionHive insertedReproductionHive;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReproductionHive createEntity() {
        return new ReproductionHive()
            .note(DEFAULT_NOTE)
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
    public static ReproductionHive createUpdatedEntity() {
        return new ReproductionHive()
            .note(UPDATED_NOTE)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
    }

    @BeforeEach
    public void initTest() {
        reproductionHive = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReproductionHive != null) {
            reproductionHiveRepository.delete(insertedReproductionHive);
            insertedReproductionHive = null;
        }
    }

    @Test
    @Transactional
    void createReproductionHive() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReproductionHive
        ReproductionHiveDTO reproductionHiveDTO = reproductionHiveMapper.toDto(reproductionHive);
        var returnedReproductionHiveDTO = om.readValue(
            restReproductionHiveMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reproductionHiveDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReproductionHiveDTO.class
        );

        // Validate the ReproductionHive in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReproductionHive = reproductionHiveMapper.toEntity(returnedReproductionHiveDTO);
        assertReproductionHiveUpdatableFieldsEquals(returnedReproductionHive, getPersistedReproductionHive(returnedReproductionHive));

        insertedReproductionHive = returnedReproductionHive;
    }

    @Test
    @Transactional
    void createReproductionHiveWithExistingId() throws Exception {
        // Create the ReproductionHive with an existing ID
        reproductionHive.setId(1L);
        ReproductionHiveDTO reproductionHiveDTO = reproductionHiveMapper.toDto(reproductionHive);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReproductionHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reproductionHiveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReproductionHive in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reproductionHive.setExternalId(null);

        // Create the ReproductionHive, which fails.
        ReproductionHiveDTO reproductionHiveDTO = reproductionHiveMapper.toDto(reproductionHive);

        restReproductionHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reproductionHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reproductionHive.setUuid(null);

        // Create the ReproductionHive, which fails.
        ReproductionHiveDTO reproductionHiveDTO = reproductionHiveMapper.toDto(reproductionHive);

        restReproductionHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reproductionHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reproductionHive.setDateCreated(null);

        // Create the ReproductionHive, which fails.
        ReproductionHiveDTO reproductionHiveDTO = reproductionHiveMapper.toDto(reproductionHive);

        restReproductionHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reproductionHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reproductionHive.setDateModified(null);

        // Create the ReproductionHive, which fails.
        ReproductionHiveDTO reproductionHiveDTO = reproductionHiveMapper.toDto(reproductionHive);

        restReproductionHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reproductionHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSynchedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reproductionHive.setDateSynched(null);

        // Create the ReproductionHive, which fails.
        ReproductionHiveDTO reproductionHiveDTO = reproductionHiveMapper.toDto(reproductionHive);

        restReproductionHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reproductionHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReproductionHives() throws Exception {
        // Initialize the database
        insertedReproductionHive = reproductionHiveRepository.saveAndFlush(reproductionHive);

        // Get all the reproductionHiveList
        restReproductionHiveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reproductionHive.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].dateSynched").value(hasItem(DEFAULT_DATE_SYNCHED.toString())))
            .andExpect(jsonPath("$.[*].dateDeleted").value(hasItem(DEFAULT_DATE_DELETED.toString())));
    }

    @Test
    @Transactional
    void getReproductionHive() throws Exception {
        // Initialize the database
        insertedReproductionHive = reproductionHiveRepository.saveAndFlush(reproductionHive);

        // Get the reproductionHive
        restReproductionHiveMockMvc
            .perform(get(ENTITY_API_URL_ID, reproductionHive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reproductionHive.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.dateSynched").value(DEFAULT_DATE_SYNCHED.toString()))
            .andExpect(jsonPath("$.dateDeleted").value(DEFAULT_DATE_DELETED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReproductionHive() throws Exception {
        // Get the reproductionHive
        restReproductionHiveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReproductionHive() throws Exception {
        // Initialize the database
        insertedReproductionHive = reproductionHiveRepository.saveAndFlush(reproductionHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reproductionHive
        ReproductionHive updatedReproductionHive = reproductionHiveRepository.findById(reproductionHive.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReproductionHive are not directly saved in db
        em.detach(updatedReproductionHive);
        updatedReproductionHive
            .note(UPDATED_NOTE)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
        ReproductionHiveDTO reproductionHiveDTO = reproductionHiveMapper.toDto(updatedReproductionHive);

        restReproductionHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reproductionHiveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reproductionHiveDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReproductionHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReproductionHiveToMatchAllProperties(updatedReproductionHive);
    }

    @Test
    @Transactional
    void putNonExistingReproductionHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reproductionHive.setId(longCount.incrementAndGet());

        // Create the ReproductionHive
        ReproductionHiveDTO reproductionHiveDTO = reproductionHiveMapper.toDto(reproductionHive);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReproductionHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reproductionHiveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reproductionHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReproductionHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReproductionHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reproductionHive.setId(longCount.incrementAndGet());

        // Create the ReproductionHive
        ReproductionHiveDTO reproductionHiveDTO = reproductionHiveMapper.toDto(reproductionHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReproductionHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reproductionHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReproductionHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReproductionHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reproductionHive.setId(longCount.incrementAndGet());

        // Create the ReproductionHive
        ReproductionHiveDTO reproductionHiveDTO = reproductionHiveMapper.toDto(reproductionHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReproductionHiveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reproductionHiveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReproductionHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReproductionHiveWithPatch() throws Exception {
        // Initialize the database
        insertedReproductionHive = reproductionHiveRepository.saveAndFlush(reproductionHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reproductionHive using partial update
        ReproductionHive partialUpdatedReproductionHive = new ReproductionHive();
        partialUpdatedReproductionHive.setId(reproductionHive.getId());

        partialUpdatedReproductionHive
            .note(UPDATED_NOTE)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restReproductionHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReproductionHive.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReproductionHive))
            )
            .andExpect(status().isOk());

        // Validate the ReproductionHive in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReproductionHiveUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReproductionHive, reproductionHive),
            getPersistedReproductionHive(reproductionHive)
        );
    }

    @Test
    @Transactional
    void fullUpdateReproductionHiveWithPatch() throws Exception {
        // Initialize the database
        insertedReproductionHive = reproductionHiveRepository.saveAndFlush(reproductionHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reproductionHive using partial update
        ReproductionHive partialUpdatedReproductionHive = new ReproductionHive();
        partialUpdatedReproductionHive.setId(reproductionHive.getId());

        partialUpdatedReproductionHive
            .note(UPDATED_NOTE)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);

        restReproductionHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReproductionHive.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReproductionHive))
            )
            .andExpect(status().isOk());

        // Validate the ReproductionHive in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReproductionHiveUpdatableFieldsEquals(
            partialUpdatedReproductionHive,
            getPersistedReproductionHive(partialUpdatedReproductionHive)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReproductionHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reproductionHive.setId(longCount.incrementAndGet());

        // Create the ReproductionHive
        ReproductionHiveDTO reproductionHiveDTO = reproductionHiveMapper.toDto(reproductionHive);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReproductionHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reproductionHiveDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reproductionHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReproductionHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReproductionHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reproductionHive.setId(longCount.incrementAndGet());

        // Create the ReproductionHive
        ReproductionHiveDTO reproductionHiveDTO = reproductionHiveMapper.toDto(reproductionHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReproductionHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reproductionHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReproductionHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReproductionHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reproductionHive.setId(longCount.incrementAndGet());

        // Create the ReproductionHive
        ReproductionHiveDTO reproductionHiveDTO = reproductionHiveMapper.toDto(reproductionHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReproductionHiveMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reproductionHiveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReproductionHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReproductionHive() throws Exception {
        // Initialize the database
        insertedReproductionHive = reproductionHiveRepository.saveAndFlush(reproductionHive);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reproductionHive
        restReproductionHiveMockMvc
            .perform(delete(ENTITY_API_URL_ID, reproductionHive.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reproductionHiveRepository.count();
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

    protected ReproductionHive getPersistedReproductionHive(ReproductionHive reproductionHive) {
        return reproductionHiveRepository.findById(reproductionHive.getId()).orElseThrow();
    }

    protected void assertPersistedReproductionHiveToMatchAllProperties(ReproductionHive expectedReproductionHive) {
        assertReproductionHiveAllPropertiesEquals(expectedReproductionHive, getPersistedReproductionHive(expectedReproductionHive));
    }

    protected void assertPersistedReproductionHiveToMatchUpdatableProperties(ReproductionHive expectedReproductionHive) {
        assertReproductionHiveAllUpdatablePropertiesEquals(
            expectedReproductionHive,
            getPersistedReproductionHive(expectedReproductionHive)
        );
    }
}
