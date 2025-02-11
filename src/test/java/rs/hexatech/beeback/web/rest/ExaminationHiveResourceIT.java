package rs.hexatech.beeback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rs.hexatech.beeback.domain.ExaminationHiveAsserts.*;
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
import rs.hexatech.beeback.domain.ExaminationHive;
import rs.hexatech.beeback.repository.ExaminationHiveRepository;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.service.dto.ExaminationHiveDTO;
import rs.hexatech.beeback.service.mapper.ExaminationHiveMapper;

/**
 * Integration tests for the {@link ExaminationHiveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExaminationHiveResourceIT {

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_EXAMINATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_EXAMINATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

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

    private static final String ENTITY_API_URL = "/api/examination-hives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExaminationHiveRepository examinationHiveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExaminationHiveMapper examinationHiveMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExaminationHiveMockMvc;

    private ExaminationHive examinationHive;

    private ExaminationHive insertedExaminationHive;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExaminationHive createEntity() {
        return new ExaminationHive()
            .note(DEFAULT_NOTE)
            .dateExamination(DEFAULT_DATE_EXAMINATION)
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
    public static ExaminationHive createUpdatedEntity() {
        return new ExaminationHive()
            .note(UPDATED_NOTE)
            .dateExamination(UPDATED_DATE_EXAMINATION)
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
        examinationHive = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedExaminationHive != null) {
            examinationHiveRepository.delete(insertedExaminationHive);
            insertedExaminationHive = null;
        }
    }

    @Test
    @Transactional
    void createExaminationHive() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExaminationHive
        ExaminationHiveDTO examinationHiveDTO = examinationHiveMapper.toDto(examinationHive);
        var returnedExaminationHiveDTO = om.readValue(
            restExaminationHiveMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examinationHiveDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExaminationHiveDTO.class
        );

        // Validate the ExaminationHive in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExaminationHive = examinationHiveMapper.toEntity(returnedExaminationHiveDTO);
        assertExaminationHiveUpdatableFieldsEquals(returnedExaminationHive, getPersistedExaminationHive(returnedExaminationHive));

        insertedExaminationHive = returnedExaminationHive;
    }

    @Test
    @Transactional
    void createExaminationHiveWithExistingId() throws Exception {
        // Create the ExaminationHive with an existing ID
        examinationHive.setId(1L);
        ExaminationHiveDTO examinationHiveDTO = examinationHiveMapper.toDto(examinationHive);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExaminationHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examinationHiveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExaminationHive in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examinationHive.setExternalId(null);

        // Create the ExaminationHive, which fails.
        ExaminationHiveDTO examinationHiveDTO = examinationHiveMapper.toDto(examinationHive);

        restExaminationHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examinationHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examinationHive.setUuid(null);

        // Create the ExaminationHive, which fails.
        ExaminationHiveDTO examinationHiveDTO = examinationHiveMapper.toDto(examinationHive);

        restExaminationHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examinationHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examinationHive.setDateCreated(null);

        // Create the ExaminationHive, which fails.
        ExaminationHiveDTO examinationHiveDTO = examinationHiveMapper.toDto(examinationHive);

        restExaminationHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examinationHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examinationHive.setDateModified(null);

        // Create the ExaminationHive, which fails.
        ExaminationHiveDTO examinationHiveDTO = examinationHiveMapper.toDto(examinationHive);

        restExaminationHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examinationHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSynchedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examinationHive.setDateSynched(null);

        // Create the ExaminationHive, which fails.
        ExaminationHiveDTO examinationHiveDTO = examinationHiveMapper.toDto(examinationHive);

        restExaminationHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examinationHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExaminationHives() throws Exception {
        // Initialize the database
        insertedExaminationHive = examinationHiveRepository.saveAndFlush(examinationHive);

        // Get all the examinationHiveList
        restExaminationHiveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examinationHive.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].dateExamination").value(hasItem(DEFAULT_DATE_EXAMINATION.toString())))
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
    void getExaminationHive() throws Exception {
        // Initialize the database
        insertedExaminationHive = examinationHiveRepository.saveAndFlush(examinationHive);

        // Get the examinationHive
        restExaminationHiveMockMvc
            .perform(get(ENTITY_API_URL_ID, examinationHive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examinationHive.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.dateExamination").value(DEFAULT_DATE_EXAMINATION.toString()))
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
    void getNonExistingExaminationHive() throws Exception {
        // Get the examinationHive
        restExaminationHiveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExaminationHive() throws Exception {
        // Initialize the database
        insertedExaminationHive = examinationHiveRepository.saveAndFlush(examinationHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examinationHive
        ExaminationHive updatedExaminationHive = examinationHiveRepository.findById(examinationHive.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExaminationHive are not directly saved in db
        em.detach(updatedExaminationHive);
        updatedExaminationHive
            .note(UPDATED_NOTE)
            .dateExamination(UPDATED_DATE_EXAMINATION)
            .reminderId(UPDATED_REMINDER_ID)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
        ExaminationHiveDTO examinationHiveDTO = examinationHiveMapper.toDto(updatedExaminationHive);

        restExaminationHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examinationHiveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examinationHiveDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExaminationHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExaminationHiveToMatchAllProperties(updatedExaminationHive);
    }

    @Test
    @Transactional
    void putNonExistingExaminationHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationHive.setId(longCount.incrementAndGet());

        // Create the ExaminationHive
        ExaminationHiveDTO examinationHiveDTO = examinationHiveMapper.toDto(examinationHive);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExaminationHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examinationHiveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examinationHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExaminationHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExaminationHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationHive.setId(longCount.incrementAndGet());

        // Create the ExaminationHive
        ExaminationHiveDTO examinationHiveDTO = examinationHiveMapper.toDto(examinationHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExaminationHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examinationHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExaminationHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExaminationHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationHive.setId(longCount.incrementAndGet());

        // Create the ExaminationHive
        ExaminationHiveDTO examinationHiveDTO = examinationHiveMapper.toDto(examinationHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExaminationHiveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examinationHiveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExaminationHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExaminationHiveWithPatch() throws Exception {
        // Initialize the database
        insertedExaminationHive = examinationHiveRepository.saveAndFlush(examinationHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examinationHive using partial update
        ExaminationHive partialUpdatedExaminationHive = new ExaminationHive();
        partialUpdatedExaminationHive.setId(examinationHive.getId());

        partialUpdatedExaminationHive
            .note(UPDATED_NOTE)
            .dateExamination(UPDATED_DATE_EXAMINATION)
            .externalId(UPDATED_EXTERNAL_ID)
            .dateSynched(UPDATED_DATE_SYNCHED);

        restExaminationHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExaminationHive.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExaminationHive))
            )
            .andExpect(status().isOk());

        // Validate the ExaminationHive in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExaminationHiveUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExaminationHive, examinationHive),
            getPersistedExaminationHive(examinationHive)
        );
    }

    @Test
    @Transactional
    void fullUpdateExaminationHiveWithPatch() throws Exception {
        // Initialize the database
        insertedExaminationHive = examinationHiveRepository.saveAndFlush(examinationHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examinationHive using partial update
        ExaminationHive partialUpdatedExaminationHive = new ExaminationHive();
        partialUpdatedExaminationHive.setId(examinationHive.getId());

        partialUpdatedExaminationHive
            .note(UPDATED_NOTE)
            .dateExamination(UPDATED_DATE_EXAMINATION)
            .reminderId(UPDATED_REMINDER_ID)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);

        restExaminationHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExaminationHive.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExaminationHive))
            )
            .andExpect(status().isOk());

        // Validate the ExaminationHive in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExaminationHiveUpdatableFieldsEquals(
            partialUpdatedExaminationHive,
            getPersistedExaminationHive(partialUpdatedExaminationHive)
        );
    }

    @Test
    @Transactional
    void patchNonExistingExaminationHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationHive.setId(longCount.incrementAndGet());

        // Create the ExaminationHive
        ExaminationHiveDTO examinationHiveDTO = examinationHiveMapper.toDto(examinationHive);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExaminationHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examinationHiveDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(examinationHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExaminationHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExaminationHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationHive.setId(longCount.incrementAndGet());

        // Create the ExaminationHive
        ExaminationHiveDTO examinationHiveDTO = examinationHiveMapper.toDto(examinationHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExaminationHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(examinationHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExaminationHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExaminationHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationHive.setId(longCount.incrementAndGet());

        // Create the ExaminationHive
        ExaminationHiveDTO examinationHiveDTO = examinationHiveMapper.toDto(examinationHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExaminationHiveMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(examinationHiveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExaminationHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExaminationHive() throws Exception {
        // Initialize the database
        insertedExaminationHive = examinationHiveRepository.saveAndFlush(examinationHive);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the examinationHive
        restExaminationHiveMockMvc
            .perform(delete(ENTITY_API_URL_ID, examinationHive.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return examinationHiveRepository.count();
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

    protected ExaminationHive getPersistedExaminationHive(ExaminationHive examinationHive) {
        return examinationHiveRepository.findById(examinationHive.getId()).orElseThrow();
    }

    protected void assertPersistedExaminationHiveToMatchAllProperties(ExaminationHive expectedExaminationHive) {
        assertExaminationHiveAllPropertiesEquals(expectedExaminationHive, getPersistedExaminationHive(expectedExaminationHive));
    }

    protected void assertPersistedExaminationHiveToMatchUpdatableProperties(ExaminationHive expectedExaminationHive) {
        assertExaminationHiveAllUpdatablePropertiesEquals(expectedExaminationHive, getPersistedExaminationHive(expectedExaminationHive));
    }
}
