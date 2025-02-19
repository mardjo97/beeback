package rs.hexatech.beeback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rs.hexatech.beeback.domain.MovedHiveAsserts.*;
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
import rs.hexatech.beeback.domain.MovedHive;
import rs.hexatech.beeback.repository.MovedHiveRepository;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.service.dto.MovedHiveDTO;
import rs.hexatech.beeback.service.mapper.MovedHiveMapper;

/**
 * Integration tests for the {@link MovedHiveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MovedHiveResourceIT {

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

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

    private static final Instant DEFAULT_DATE_FINISHED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FINISHED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/moved-hives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MovedHiveRepository movedHiveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovedHiveMapper movedHiveMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMovedHiveMockMvc;

    private MovedHive movedHive;

    private MovedHive insertedMovedHive;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MovedHive createEntity() {
        return new MovedHive()
            .location(DEFAULT_LOCATION)
            .externalId(DEFAULT_EXTERNAL_ID)
            .uuid(DEFAULT_UUID)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateModified(DEFAULT_DATE_MODIFIED)
            .dateSynched(DEFAULT_DATE_SYNCHED)
            .dateDeleted(DEFAULT_DATE_DELETED)
            .dateFinished(DEFAULT_DATE_FINISHED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MovedHive createUpdatedEntity() {
        return new MovedHive()
            .location(UPDATED_LOCATION)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED)
            .dateFinished(UPDATED_DATE_FINISHED);
    }

    @BeforeEach
    public void initTest() {
        movedHive = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMovedHive != null) {
            movedHiveRepository.delete(insertedMovedHive);
            insertedMovedHive = null;
        }
    }

    @Test
    @Transactional
    void createMovedHive() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MovedHive
        MovedHiveDTO movedHiveDTO = movedHiveMapper.toDto(movedHive);
        var returnedMovedHiveDTO = om.readValue(
            restMovedHiveMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(movedHiveDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MovedHiveDTO.class
        );

        // Validate the MovedHive in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMovedHive = movedHiveMapper.toEntity(returnedMovedHiveDTO);
        assertMovedHiveUpdatableFieldsEquals(returnedMovedHive, getPersistedMovedHive(returnedMovedHive));

        insertedMovedHive = returnedMovedHive;
    }

    @Test
    @Transactional
    void createMovedHiveWithExistingId() throws Exception {
        // Create the MovedHive with an existing ID
        movedHive.setId(1L);
        MovedHiveDTO movedHiveDTO = movedHiveMapper.toDto(movedHive);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovedHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(movedHiveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MovedHive in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        movedHive.setExternalId(null);

        // Create the MovedHive, which fails.
        MovedHiveDTO movedHiveDTO = movedHiveMapper.toDto(movedHive);

        restMovedHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(movedHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        movedHive.setUuid(null);

        // Create the MovedHive, which fails.
        MovedHiveDTO movedHiveDTO = movedHiveMapper.toDto(movedHive);

        restMovedHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(movedHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        movedHive.setDateCreated(null);

        // Create the MovedHive, which fails.
        MovedHiveDTO movedHiveDTO = movedHiveMapper.toDto(movedHive);

        restMovedHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(movedHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        movedHive.setDateModified(null);

        // Create the MovedHive, which fails.
        MovedHiveDTO movedHiveDTO = movedHiveMapper.toDto(movedHive);

        restMovedHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(movedHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSynchedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        movedHive.setDateSynched(null);

        // Create the MovedHive, which fails.
        MovedHiveDTO movedHiveDTO = movedHiveMapper.toDto(movedHive);

        restMovedHiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(movedHiveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMovedHives() throws Exception {
        // Initialize the database
        insertedMovedHive = movedHiveRepository.saveAndFlush(movedHive);

        // Get all the movedHiveList
        restMovedHiveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movedHive.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].dateSynched").value(hasItem(DEFAULT_DATE_SYNCHED.toString())))
            .andExpect(jsonPath("$.[*].dateDeleted").value(hasItem(DEFAULT_DATE_DELETED.toString())))
            .andExpect(jsonPath("$.[*].dateFinished").value(hasItem(DEFAULT_DATE_FINISHED.toString())));
    }

    @Test
    @Transactional
    void getMovedHive() throws Exception {
        // Initialize the database
        insertedMovedHive = movedHiveRepository.saveAndFlush(movedHive);

        // Get the movedHive
        restMovedHiveMockMvc
            .perform(get(ENTITY_API_URL_ID, movedHive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(movedHive.getId().intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.dateSynched").value(DEFAULT_DATE_SYNCHED.toString()))
            .andExpect(jsonPath("$.dateDeleted").value(DEFAULT_DATE_DELETED.toString()))
            .andExpect(jsonPath("$.dateFinished").value(DEFAULT_DATE_FINISHED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMovedHive() throws Exception {
        // Get the movedHive
        restMovedHiveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMovedHive() throws Exception {
        // Initialize the database
        insertedMovedHive = movedHiveRepository.saveAndFlush(movedHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the movedHive
        MovedHive updatedMovedHive = movedHiveRepository.findById(movedHive.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMovedHive are not directly saved in db
        em.detach(updatedMovedHive);
        updatedMovedHive
            .location(UPDATED_LOCATION)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED)
            .dateFinished(UPDATED_DATE_FINISHED);
        MovedHiveDTO movedHiveDTO = movedHiveMapper.toDto(updatedMovedHive);

        restMovedHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movedHiveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(movedHiveDTO))
            )
            .andExpect(status().isOk());

        // Validate the MovedHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMovedHiveToMatchAllProperties(updatedMovedHive);
    }

    @Test
    @Transactional
    void putNonExistingMovedHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movedHive.setId(longCount.incrementAndGet());

        // Create the MovedHive
        MovedHiveDTO movedHiveDTO = movedHiveMapper.toDto(movedHive);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovedHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movedHiveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(movedHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovedHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMovedHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movedHive.setId(longCount.incrementAndGet());

        // Create the MovedHive
        MovedHiveDTO movedHiveDTO = movedHiveMapper.toDto(movedHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovedHiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(movedHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovedHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMovedHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movedHive.setId(longCount.incrementAndGet());

        // Create the MovedHive
        MovedHiveDTO movedHiveDTO = movedHiveMapper.toDto(movedHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovedHiveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(movedHiveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MovedHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMovedHiveWithPatch() throws Exception {
        // Initialize the database
        insertedMovedHive = movedHiveRepository.saveAndFlush(movedHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the movedHive using partial update
        MovedHive partialUpdatedMovedHive = new MovedHive();
        partialUpdatedMovedHive.setId(movedHive.getId());

        partialUpdatedMovedHive
            .location(UPDATED_LOCATION)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateDeleted(UPDATED_DATE_DELETED)
            .dateFinished(UPDATED_DATE_FINISHED);

        restMovedHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovedHive.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMovedHive))
            )
            .andExpect(status().isOk());

        // Validate the MovedHive in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMovedHiveUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMovedHive, movedHive),
            getPersistedMovedHive(movedHive)
        );
    }

    @Test
    @Transactional
    void fullUpdateMovedHiveWithPatch() throws Exception {
        // Initialize the database
        insertedMovedHive = movedHiveRepository.saveAndFlush(movedHive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the movedHive using partial update
        MovedHive partialUpdatedMovedHive = new MovedHive();
        partialUpdatedMovedHive.setId(movedHive.getId());

        partialUpdatedMovedHive
            .location(UPDATED_LOCATION)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED)
            .dateFinished(UPDATED_DATE_FINISHED);

        restMovedHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovedHive.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMovedHive))
            )
            .andExpect(status().isOk());

        // Validate the MovedHive in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMovedHiveUpdatableFieldsEquals(partialUpdatedMovedHive, getPersistedMovedHive(partialUpdatedMovedHive));
    }

    @Test
    @Transactional
    void patchNonExistingMovedHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movedHive.setId(longCount.incrementAndGet());

        // Create the MovedHive
        MovedHiveDTO movedHiveDTO = movedHiveMapper.toDto(movedHive);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovedHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, movedHiveDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(movedHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovedHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMovedHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movedHive.setId(longCount.incrementAndGet());

        // Create the MovedHive
        MovedHiveDTO movedHiveDTO = movedHiveMapper.toDto(movedHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovedHiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(movedHiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovedHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMovedHive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movedHive.setId(longCount.incrementAndGet());

        // Create the MovedHive
        MovedHiveDTO movedHiveDTO = movedHiveMapper.toDto(movedHive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovedHiveMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(movedHiveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MovedHive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMovedHive() throws Exception {
        // Initialize the database
        insertedMovedHive = movedHiveRepository.saveAndFlush(movedHive);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the movedHive
        restMovedHiveMockMvc
            .perform(delete(ENTITY_API_URL_ID, movedHive.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return movedHiveRepository.count();
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

    protected MovedHive getPersistedMovedHive(MovedHive movedHive) {
        return movedHiveRepository.findById(movedHive.getId()).orElseThrow();
    }

    protected void assertPersistedMovedHiveToMatchAllProperties(MovedHive expectedMovedHive) {
        assertMovedHiveAllPropertiesEquals(expectedMovedHive, getPersistedMovedHive(expectedMovedHive));
    }

    protected void assertPersistedMovedHiveToMatchUpdatableProperties(MovedHive expectedMovedHive) {
        assertMovedHiveAllUpdatablePropertiesEquals(expectedMovedHive, getPersistedMovedHive(expectedMovedHive));
    }
}
