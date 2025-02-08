package rs.hexatech.beeback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rs.hexatech.beeback.domain.HiveAsserts.*;
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
import rs.hexatech.beeback.domain.Hive;
import rs.hexatech.beeback.repository.HiveRepository;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.service.dto.HiveDTO;
import rs.hexatech.beeback.service.mapper.HiveMapper;

/**
 * Integration tests for the {@link HiveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HiveResourceIT {

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER_NUMBER = 1;
    private static final Integer UPDATED_ORDER_NUMBER = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXAMINATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXAMINATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ARCHIVED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ARCHIVED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ARCHIVED_REASON = "AAAAAAAAAA";
    private static final String UPDATED_ARCHIVED_REASON = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/hives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HiveRepository hiveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HiveMapper hiveMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHiveMockMvc;

    private Hive hive;

    private Hive insertedHive;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hive createEntity() {
        return new Hive()
            .barcode(DEFAULT_BARCODE)
            .orderNumber(DEFAULT_ORDER_NUMBER)
            .description(DEFAULT_DESCRIPTION)
            .examinationDate(DEFAULT_EXAMINATION_DATE)
            .archivedDate(DEFAULT_ARCHIVED_DATE)
            .archivedReason(DEFAULT_ARCHIVED_REASON)
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
    public static Hive createUpdatedEntity() {
        return new Hive()
            .barcode(UPDATED_BARCODE)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .examinationDate(UPDATED_EXAMINATION_DATE)
            .archivedDate(UPDATED_ARCHIVED_DATE)
            .archivedReason(UPDATED_ARCHIVED_REASON)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
    }

    @BeforeEach
    public void initTest() {
        hive = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHive != null) {
            hiveRepository.delete(insertedHive);
            insertedHive = null;
        }
    }

    @Test
    @Transactional
    void createHive() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Hive
        HiveDTO hiveDTO = hiveMapper.toDto(hive);
        var returnedHiveDTO = om.readValue(
            restHiveMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HiveDTO.class
        );

        // Validate the Hive in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHive = hiveMapper.toEntity(returnedHiveDTO);
        assertHiveUpdatableFieldsEquals(returnedHive, getPersistedHive(returnedHive));

        insertedHive = returnedHive;
    }

    @Test
    @Transactional
    void createHiveWithExistingId() throws Exception {
        // Create the Hive with an existing ID
        hive.setId(1L);
        HiveDTO hiveDTO = hiveMapper.toDto(hive);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hive in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBarcodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hive.setBarcode(null);

        // Create the Hive, which fails.
        HiveDTO hiveDTO = hiveMapper.toDto(hive);

        restHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExternalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hive.setExternalId(null);

        // Create the Hive, which fails.
        HiveDTO hiveDTO = hiveMapper.toDto(hive);

        restHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hive.setUuid(null);

        // Create the Hive, which fails.
        HiveDTO hiveDTO = hiveMapper.toDto(hive);

        restHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hive.setDateCreated(null);

        // Create the Hive, which fails.
        HiveDTO hiveDTO = hiveMapper.toDto(hive);

        restHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hive.setDateModified(null);

        // Create the Hive, which fails.
        HiveDTO hiveDTO = hiveMapper.toDto(hive);

        restHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSynchedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hive.setDateSynched(null);

        // Create the Hive, which fails.
        HiveDTO hiveDTO = hiveMapper.toDto(hive);

        restHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHives() throws Exception {
        // Initialize the database
        insertedHive = hiveRepository.saveAndFlush(hive);

        // Get all the hiveList
        restHiveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hive.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].examinationDate").value(hasItem(DEFAULT_EXAMINATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].archivedDate").value(hasItem(DEFAULT_ARCHIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].archivedReason").value(hasItem(DEFAULT_ARCHIVED_REASON)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].dateSynched").value(hasItem(DEFAULT_DATE_SYNCHED.toString())))
            .andExpect(jsonPath("$.[*].dateDeleted").value(hasItem(DEFAULT_DATE_DELETED.toString())));
    }

    @Test
    @Transactional
    void getHive() throws Exception {
        // Initialize the database
        insertedHive = hiveRepository.saveAndFlush(hive);

        // Get the hive
        restHiveMockMvc
            .perform(get(ENTITY_API_URL_ID, hive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hive.getId().intValue()))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.examinationDate").value(DEFAULT_EXAMINATION_DATE.toString()))
            .andExpect(jsonPath("$.archivedDate").value(DEFAULT_ARCHIVED_DATE.toString()))
            .andExpect(jsonPath("$.archivedReason").value(DEFAULT_ARCHIVED_REASON))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.dateSynched").value(DEFAULT_DATE_SYNCHED.toString()))
            .andExpect(jsonPath("$.dateDeleted").value(DEFAULT_DATE_DELETED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHive() throws Exception {
        // Get the hive
        restHiveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHive() throws Exception {
        // Initialize the database
        insertedHive = hiveRepository.saveAndFlush(hive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hive
        Hive updatedHive = hiveRepository.findById(hive.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHive are not directly saved in db
        em.detach(updatedHive);
        updatedHive
            .barcode(UPDATED_BARCODE)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .examinationDate(UPDATED_EXAMINATION_DATE)
            .archivedDate(UPDATED_ARCHIVED_DATE)
            .archivedReason(UPDATED_ARCHIVED_REASON)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
        HiveDTO hiveDTO = hiveMapper.toDto(updatedHive);

        restHiveMockMvc
            .perform(put(ENTITY_API_URL_ID, hiveDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveDTO)))
            .andExpect(status().isOk());

        // Validate the Hive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHiveToMatchAllProperties(updatedHive);
    }

    @Test
    @Transactional
    void putNonExistingHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hive.setId(longCount.incrementAndGet());

        // Create the Hive
        HiveDTO hiveDTO = hiveMapper.toDto(hive);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHiveMockMvc
            .perform(put(ENTITY_API_URL_ID, hiveDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hive.setId(longCount.incrementAndGet());

        // Create the Hive
        HiveDTO hiveDTO = hiveMapper.toDto(hive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hive.setId(longCount.incrementAndGet());

        // Create the Hive
        HiveDTO hiveDTO = hiveMapper.toDto(hive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHiveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHiveWithPatch() throws Exception {
        // Initialize the database
        insertedHive = hiveRepository.saveAndFlush(hive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hive using partial update
        Hive partialUpdatedHive = new Hive();
        partialUpdatedHive.setId(hive.getId());

        partialUpdatedHive
            .barcode(UPDATED_BARCODE)
            .archivedReason(UPDATED_ARCHIVED_REASON)
            .externalId(UPDATED_EXTERNAL_ID)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED);

        restHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHive.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHive))
            )
            .andExpect(status().isOk());

        // Validate the Hive in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHiveUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedHive, hive), getPersistedHive(hive));
    }

    @Test
    @Transactional
    void fullUpdateHiveWithPatch() throws Exception {
        // Initialize the database
        insertedHive = hiveRepository.saveAndFlush(hive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hive using partial update
        Hive partialUpdatedHive = new Hive();
        partialUpdatedHive.setId(hive.getId());

        partialUpdatedHive
            .barcode(UPDATED_BARCODE)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .examinationDate(UPDATED_EXAMINATION_DATE)
            .archivedDate(UPDATED_ARCHIVED_DATE)
            .archivedReason(UPDATED_ARCHIVED_REASON)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);

        restHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHive.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHive))
            )
            .andExpect(status().isOk());

        // Validate the Hive in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHiveUpdatableFieldsEquals(partialUpdatedHive, getPersistedHive(partialUpdatedHive));
    }

    @Test
    @Transactional
    void patchNonExistingHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hive.setId(longCount.incrementAndGet());

        // Create the Hive
        HiveDTO hiveDTO = hiveMapper.toDto(hive);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hiveDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hive.setId(longCount.incrementAndGet());

        // Create the Hive
        HiveDTO hiveDTO = hiveMapper.toDto(hive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hive.setId(longCount.incrementAndGet());

        // Create the Hive
        HiveDTO hiveDTO = hiveMapper.toDto(hive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHiveMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hiveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHive() throws Exception {
        // Initialize the database
        insertedHive = hiveRepository.saveAndFlush(hive);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hive
        restHiveMockMvc
            .perform(delete(ENTITY_API_URL_ID, hive.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hiveRepository.count();
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

    protected Hive getPersistedHive(Hive hive) {
        return hiveRepository.findById(hive.getId()).orElseThrow();
    }

    protected void assertPersistedHiveToMatchAllProperties(Hive expectedHive) {
        assertHiveAllPropertiesEquals(expectedHive, getPersistedHive(expectedHive));
    }

    protected void assertPersistedHiveToMatchUpdatableProperties(Hive expectedHive) {
        assertHiveAllUpdatablePropertiesEquals(expectedHive, getPersistedHive(expectedHive));
    }
}
