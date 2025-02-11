package rs.hexatech.beeback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rs.hexatech.beeback.domain.HiveLocationAsserts.*;
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
import rs.hexatech.beeback.domain.HiveLocation;
import rs.hexatech.beeback.repository.HiveLocationRepository;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.service.dto.HiveLocationDTO;
import rs.hexatech.beeback.service.mapper.HiveLocationMapper;

/**
 * Integration tests for the {@link HiveLocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HiveLocationResourceIT {

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

    private static final String ENTITY_API_URL = "/api/hive-locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HiveLocationRepository hiveLocationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HiveLocationMapper hiveLocationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHiveLocationMockMvc;

    private HiveLocation hiveLocation;

    private HiveLocation insertedHiveLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HiveLocation createEntity() {
        return new HiveLocation()
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
    public static HiveLocation createUpdatedEntity() {
        return new HiveLocation()
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
        hiveLocation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHiveLocation != null) {
            hiveLocationRepository.delete(insertedHiveLocation);
            insertedHiveLocation = null;
        }
    }

    @Test
    @Transactional
    void createHiveLocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HiveLocation
        HiveLocationDTO hiveLocationDTO = hiveLocationMapper.toDto(hiveLocation);
        var returnedHiveLocationDTO = om.readValue(
            restHiveLocationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveLocationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HiveLocationDTO.class
        );

        // Validate the HiveLocation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHiveLocation = hiveLocationMapper.toEntity(returnedHiveLocationDTO);
        assertHiveLocationUpdatableFieldsEquals(returnedHiveLocation, getPersistedHiveLocation(returnedHiveLocation));

        insertedHiveLocation = returnedHiveLocation;
    }

    @Test
    @Transactional
    void createHiveLocationWithExistingId() throws Exception {
        // Create the HiveLocation with an existing ID
        hiveLocation.setId(1L);
        HiveLocationDTO hiveLocationDTO = hiveLocationMapper.toDto(hiveLocation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHiveLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveLocationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HiveLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hiveLocation.setExternalId(null);

        // Create the HiveLocation, which fails.
        HiveLocationDTO hiveLocationDTO = hiveLocationMapper.toDto(hiveLocation);

        restHiveLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveLocationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hiveLocation.setUuid(null);

        // Create the HiveLocation, which fails.
        HiveLocationDTO hiveLocationDTO = hiveLocationMapper.toDto(hiveLocation);

        restHiveLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveLocationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hiveLocation.setDateCreated(null);

        // Create the HiveLocation, which fails.
        HiveLocationDTO hiveLocationDTO = hiveLocationMapper.toDto(hiveLocation);

        restHiveLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveLocationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hiveLocation.setDateModified(null);

        // Create the HiveLocation, which fails.
        HiveLocationDTO hiveLocationDTO = hiveLocationMapper.toDto(hiveLocation);

        restHiveLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveLocationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSynchedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hiveLocation.setDateSynched(null);

        // Create the HiveLocation, which fails.
        HiveLocationDTO hiveLocationDTO = hiveLocationMapper.toDto(hiveLocation);

        restHiveLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveLocationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHiveLocations() throws Exception {
        // Initialize the database
        insertedHiveLocation = hiveLocationRepository.saveAndFlush(hiveLocation);

        // Get all the hiveLocationList
        restHiveLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hiveLocation.getId().intValue())))
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
    void getHiveLocation() throws Exception {
        // Initialize the database
        insertedHiveLocation = hiveLocationRepository.saveAndFlush(hiveLocation);

        // Get the hiveLocation
        restHiveLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, hiveLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hiveLocation.getId().intValue()))
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
    void getNonExistingHiveLocation() throws Exception {
        // Get the hiveLocation
        restHiveLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHiveLocation() throws Exception {
        // Initialize the database
        insertedHiveLocation = hiveLocationRepository.saveAndFlush(hiveLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hiveLocation
        HiveLocation updatedHiveLocation = hiveLocationRepository.findById(hiveLocation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHiveLocation are not directly saved in db
        em.detach(updatedHiveLocation);
        updatedHiveLocation
            .name(UPDATED_NAME)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
        HiveLocationDTO hiveLocationDTO = hiveLocationMapper.toDto(updatedHiveLocation);

        restHiveLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hiveLocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hiveLocationDTO))
            )
            .andExpect(status().isOk());

        // Validate the HiveLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHiveLocationToMatchAllProperties(updatedHiveLocation);
    }

    @Test
    @Transactional
    void putNonExistingHiveLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiveLocation.setId(longCount.incrementAndGet());

        // Create the HiveLocation
        HiveLocationDTO hiveLocationDTO = hiveLocationMapper.toDto(hiveLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHiveLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hiveLocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hiveLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HiveLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHiveLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiveLocation.setId(longCount.incrementAndGet());

        // Create the HiveLocation
        HiveLocationDTO hiveLocationDTO = hiveLocationMapper.toDto(hiveLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHiveLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hiveLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HiveLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHiveLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiveLocation.setId(longCount.incrementAndGet());

        // Create the HiveLocation
        HiveLocationDTO hiveLocationDTO = hiveLocationMapper.toDto(hiveLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHiveLocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiveLocationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HiveLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHiveLocationWithPatch() throws Exception {
        // Initialize the database
        insertedHiveLocation = hiveLocationRepository.saveAndFlush(hiveLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hiveLocation using partial update
        HiveLocation partialUpdatedHiveLocation = new HiveLocation();
        partialUpdatedHiveLocation.setId(hiveLocation.getId());

        partialUpdatedHiveLocation
            .name(UPDATED_NAME)
            .externalId(UPDATED_EXTERNAL_ID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);

        restHiveLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHiveLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHiveLocation))
            )
            .andExpect(status().isOk());

        // Validate the HiveLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHiveLocationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHiveLocation, hiveLocation),
            getPersistedHiveLocation(hiveLocation)
        );
    }

    @Test
    @Transactional
    void fullUpdateHiveLocationWithPatch() throws Exception {
        // Initialize the database
        insertedHiveLocation = hiveLocationRepository.saveAndFlush(hiveLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hiveLocation using partial update
        HiveLocation partialUpdatedHiveLocation = new HiveLocation();
        partialUpdatedHiveLocation.setId(hiveLocation.getId());

        partialUpdatedHiveLocation
            .name(UPDATED_NAME)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);

        restHiveLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHiveLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHiveLocation))
            )
            .andExpect(status().isOk());

        // Validate the HiveLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHiveLocationUpdatableFieldsEquals(partialUpdatedHiveLocation, getPersistedHiveLocation(partialUpdatedHiveLocation));
    }

    @Test
    @Transactional
    void patchNonExistingHiveLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiveLocation.setId(longCount.incrementAndGet());

        // Create the HiveLocation
        HiveLocationDTO hiveLocationDTO = hiveLocationMapper.toDto(hiveLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHiveLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hiveLocationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hiveLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HiveLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHiveLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiveLocation.setId(longCount.incrementAndGet());

        // Create the HiveLocation
        HiveLocationDTO hiveLocationDTO = hiveLocationMapper.toDto(hiveLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHiveLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hiveLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HiveLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHiveLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiveLocation.setId(longCount.incrementAndGet());

        // Create the HiveLocation
        HiveLocationDTO hiveLocationDTO = hiveLocationMapper.toDto(hiveLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHiveLocationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hiveLocationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HiveLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHiveLocation() throws Exception {
        // Initialize the database
        insertedHiveLocation = hiveLocationRepository.saveAndFlush(hiveLocation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hiveLocation
        restHiveLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, hiveLocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hiveLocationRepository.count();
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

    protected HiveLocation getPersistedHiveLocation(HiveLocation hiveLocation) {
        return hiveLocationRepository.findById(hiveLocation.getId()).orElseThrow();
    }

    protected void assertPersistedHiveLocationToMatchAllProperties(HiveLocation expectedHiveLocation) {
        assertHiveLocationAllPropertiesEquals(expectedHiveLocation, getPersistedHiveLocation(expectedHiveLocation));
    }

    protected void assertPersistedHiveLocationToMatchUpdatableProperties(HiveLocation expectedHiveLocation) {
        assertHiveLocationAllUpdatablePropertiesEquals(expectedHiveLocation, getPersistedHiveLocation(expectedHiveLocation));
    }
}
