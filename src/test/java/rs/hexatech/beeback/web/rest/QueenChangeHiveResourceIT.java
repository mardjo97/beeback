package rs.hexatech.beeback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rs.hexatech.beeback.domain.QueenChangeHiveAsserts.*;
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
import rs.hexatech.beeback.domain.QueenChangeHive;
import rs.hexatech.beeback.repository.QueenChangeHiveRepository;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.service.dto.QueenChangeHiveDTO;
import rs.hexatech.beeback.service.mapper.QueenChangeHiveMapper;

/**
 * Integration tests for the {@link QueenChangeHiveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QueenChangeHiveResourceIT {

    private static final Instant DEFAULT_DATE_QUEEN_CHANGE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_QUEEN_CHANGE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REMINDER_ID = "AAAAAAAAAA";
    private static final String UPDATED_REMINDER_ID = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/queen-change-hives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QueenChangeHiveRepository queenChangeHiveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QueenChangeHiveMapper queenChangeHiveMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQueenChangeHiveMockMvc;

    private QueenChangeHive queenChangeHive;

    private QueenChangeHive insertedQueenChangeHive;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QueenChangeHive createEntity() {
        return new QueenChangeHive()
            .dateQueenChange(DEFAULT_DATE_QUEEN_CHANGE)
            .reminderId(DEFAULT_REMINDER_ID)
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
    public static QueenChangeHive createUpdatedEntity() {
        return new QueenChangeHive()
            .dateQueenChange(UPDATED_DATE_QUEEN_CHANGE)
            .reminderId(UPDATED_REMINDER_ID)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
    }

    @BeforeEach
    public void initTest() {
        queenChangeHive = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedQueenChangeHive != null) {
            queenChangeHiveRepository.delete(insertedQueenChangeHive);
            insertedQueenChangeHive = null;
        }
    }

    @Test
    @Transactional
    void createQueenChangeHive() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QueenChangeHive
        QueenChangeHiveDTO queenChangeHiveDTO = queenChangeHiveMapper.toDto(queenChangeHive);
        var returnedQueenChangeHiveDTO = om.readValue(
            restQueenChangeHiveMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenChangeHiveDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QueenChangeHiveDTO.class
        );

        // Validate the QueenChangeHive in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQueenChangeHive = queenChangeHiveMapper.toEntity(returnedQueenChangeHiveDTO);
        assertQueenChangeHiveUpdatableFieldsEquals(returnedQueenChangeHive, getPersistedQueenChangeHive(returnedQueenChangeHive));

        insertedQueenChangeHive = returnedQueenChangeHive;
    }

    @Test
    @Transactional
    void createQueenChangeHiveWithExistingId() throws Exception {
        // Create the QueenChangeHive with an existing ID
        queenChangeHive.setId(1L);
        QueenChangeHiveDTO queenChangeHiveDTO = queenChangeHiveMapper.toDto(queenChangeHive);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQueenChangeHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenChangeHiveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QueenChangeHive in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        queenChangeHive.setExternalId(null);

        // Create the QueenChangeHive, which fails.
        QueenChangeHiveDTO queenChangeHiveDTO = queenChangeHiveMapper.toDto(queenChangeHive);

        restQueenChangeHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenChangeHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        queenChangeHive.setUuid(null);

        // Create the QueenChangeHive, which fails.
        QueenChangeHiveDTO queenChangeHiveDTO = queenChangeHiveMapper.toDto(queenChangeHive);

        restQueenChangeHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenChangeHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        queenChangeHive.setDateCreated(null);

        // Create the QueenChangeHive, which fails.
        QueenChangeHiveDTO queenChangeHiveDTO = queenChangeHiveMapper.toDto(queenChangeHive);

        restQueenChangeHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenChangeHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        queenChangeHive.setDateModified(null);

        // Create the QueenChangeHive, which fails.
        QueenChangeHiveDTO queenChangeHiveDTO = queenChangeHiveMapper.toDto(queenChangeHive);

        restQueenChangeHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenChangeHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSynchedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        queenChangeHive.setDateSynched(null);

        // Create the QueenChangeHive, which fails.
        QueenChangeHiveDTO queenChangeHiveDTO = queenChangeHiveMapper.toDto(queenChangeHive);

        restQueenChangeHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenChangeHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQueenChangeHives() throws Exception {
        // Initialize the database
        insertedQueenChangeHive = queenChangeHiveRepository.saveAndFlush(queenChangeHive);

        // Get all the queenChangeHiveList
        restQueenChangeHiveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(queenChangeHive.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateQueenChange").value(hasItem(DEFAULT_DATE_QUEEN_CHANGE.toString())))
            .andExpect(jsonPath("$.[*].reminderId").value(hasItem(DEFAULT_REMINDER_ID)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].dateSynched").value(hasItem(DEFAULT_DATE_SYNCHED.toString())))
            .andExpect(jsonPath("$.[*].dateDeleted").value(hasItem(DEFAULT_DATE_DELETED.toString())));
    }

    @Test
    @Transactional
    void getQueenChangeHive() throws Exception {
        // Initialize the database
        insertedQueenChangeHive = queenChangeHiveRepository.saveAndFlush(queenChangeHive);

        // Get the queenChangeHive
        restQueenChangeHiveMockMvc
            .perform(get(ENTITY_API_URL_ID, queenChangeHive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(queenChangeHive.getId().intValue()))
            .andExpect(jsonPath("$.dateQueenChange").value(DEFAULT_DATE_QUEEN_CHANGE.toString()))
            .andExpect(jsonPath("$.reminderId").value(DEFAULT_REMINDER_ID))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.dateSynched").value(DEFAULT_DATE_SYNCHED.toString()))
            .andExpect(jsonPath("$.dateDeleted").value(DEFAULT_DATE_DELETED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingQueenChangeHive() throws Exception {
        // Get the queenChangeHive
        restQueenChangeHiveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQueenChangeHive() throws Exception {
        // Initialize the database
        insertedQueenChangeHive = queenChangeHiveRepository.saveAndFlush(queenChangeHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the queenChangeHive
        QueenChangeHive updatedQueenChangeHive = queenChangeHiveRepository.findById(queenChangeHive.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQueenChangeHive are not directly saved in db
        em.detach(updatedQueenChangeHive);
        updatedQueenChangeHive
            .dateQueenChange(UPDATED_DATE_QUEEN_CHANGE)
            .reminderId(UPDATED_REMINDER_ID)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
        QueenChangeHiveDTO queenChangeHiveDTO = queenChangeHiveMapper.toDto(updatedQueenChangeHive);

        restQueenChangeHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, queenChangeHiveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(queenChangeHiveDTO))
            )
            .andExpect(status().isOk());

        // Validate the QueenChangeHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQueenChangeHiveToMatchAllProperties(updatedQueenChangeHive);
    }

    @Test
    @Transactional
    void putNonExistingQueenChangeHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        queenChangeHive.setId(longCount.incrementAndGet());

        // Create the QueenChangeHive
        QueenChangeHiveDTO queenChangeHiveDTO = queenChangeHiveMapper.toDto(queenChangeHive);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQueenChangeHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, queenChangeHiveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(queenChangeHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QueenChangeHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQueenChangeHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        queenChangeHive.setId(longCount.incrementAndGet());

        // Create the QueenChangeHive
        QueenChangeHiveDTO queenChangeHiveDTO = queenChangeHiveMapper.toDto(queenChangeHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueenChangeHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(queenChangeHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QueenChangeHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQueenChangeHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        queenChangeHive.setId(longCount.incrementAndGet());

        // Create the QueenChangeHive
        QueenChangeHiveDTO queenChangeHiveDTO = queenChangeHiveMapper.toDto(queenChangeHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueenChangeHiveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenChangeHiveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QueenChangeHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQueenChangeHiveWithPatch() throws Exception {
        // Initialize the database
        insertedQueenChangeHive = queenChangeHiveRepository.saveAndFlush(queenChangeHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the queenChangeHive using partial update
        QueenChangeHive partialUpdatedQueenChangeHive = new QueenChangeHive();
        partialUpdatedQueenChangeHive.setId(queenChangeHive.getId());

        partialUpdatedQueenChangeHive.dateQueenChange(UPDATED_DATE_QUEEN_CHANGE).uuid(UPDATED_UUID);

        restQueenChangeHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQueenChangeHive.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQueenChangeHive))
            )
            .andExpect(status().isOk());

        // Validate the QueenChangeHive in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQueenChangeHiveUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQueenChangeHive, queenChangeHive),
            getPersistedQueenChangeHive(queenChangeHive)
        );
    }

    @Test
    @Transactional
    void fullUpdateQueenChangeHiveWithPatch() throws Exception {
        // Initialize the database
        insertedQueenChangeHive = queenChangeHiveRepository.saveAndFlush(queenChangeHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the queenChangeHive using partial update
        QueenChangeHive partialUpdatedQueenChangeHive = new QueenChangeHive();
        partialUpdatedQueenChangeHive.setId(queenChangeHive.getId());

        partialUpdatedQueenChangeHive
            .dateQueenChange(UPDATED_DATE_QUEEN_CHANGE)
            .reminderId(UPDATED_REMINDER_ID)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);

        restQueenChangeHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQueenChangeHive.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQueenChangeHive))
            )
            .andExpect(status().isOk());

        // Validate the QueenChangeHive in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQueenChangeHiveUpdatableFieldsEquals(
            partialUpdatedQueenChangeHive,
            getPersistedQueenChangeHive(partialUpdatedQueenChangeHive)
        );
    }

    @Test
    @Transactional
    void patchNonExistingQueenChangeHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        queenChangeHive.setId(longCount.incrementAndGet());

        // Create the QueenChangeHive
        QueenChangeHiveDTO queenChangeHiveDTO = queenChangeHiveMapper.toDto(queenChangeHive);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQueenChangeHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, queenChangeHiveDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(queenChangeHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QueenChangeHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQueenChangeHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        queenChangeHive.setId(longCount.incrementAndGet());

        // Create the QueenChangeHive
        QueenChangeHiveDTO queenChangeHiveDTO = queenChangeHiveMapper.toDto(queenChangeHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueenChangeHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(queenChangeHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QueenChangeHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQueenChangeHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        queenChangeHive.setId(longCount.incrementAndGet());

        // Create the QueenChangeHive
        QueenChangeHiveDTO queenChangeHiveDTO = queenChangeHiveMapper.toDto(queenChangeHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueenChangeHiveMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(queenChangeHiveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QueenChangeHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQueenChangeHive() throws Exception {
        // Initialize the database
        insertedQueenChangeHive = queenChangeHiveRepository.saveAndFlush(queenChangeHive);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the queenChangeHive
        restQueenChangeHiveMockMvc
            .perform(delete(ENTITY_API_URL_ID, queenChangeHive.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return queenChangeHiveRepository.count();
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

    protected QueenChangeHive getPersistedQueenChangeHive(QueenChangeHive queenChangeHive) {
        return queenChangeHiveRepository.findById(queenChangeHive.getId()).orElseThrow();
    }

    protected void assertPersistedQueenChangeHiveToMatchAllProperties(QueenChangeHive expectedQueenChangeHive) {
        assertQueenChangeHiveAllPropertiesEquals(expectedQueenChangeHive, getPersistedQueenChangeHive(expectedQueenChangeHive));
    }

    protected void assertPersistedQueenChangeHiveToMatchUpdatableProperties(QueenChangeHive expectedQueenChangeHive) {
        assertQueenChangeHiveAllUpdatablePropertiesEquals(expectedQueenChangeHive, getPersistedQueenChangeHive(expectedQueenChangeHive));
    }
}
