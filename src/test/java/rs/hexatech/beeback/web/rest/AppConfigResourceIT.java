package rs.hexatech.beeback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rs.hexatech.beeback.domain.AppConfigAsserts.*;
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
import rs.hexatech.beeback.domain.AppConfig;
import rs.hexatech.beeback.repository.AppConfigRepository;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.service.dto.AppConfigDTO;
import rs.hexatech.beeback.service.mapper.AppConfigMapper;

/**
 * Integration tests for the {@link AppConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppConfigResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/app-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppConfigMapper appConfigMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppConfigMockMvc;

    private AppConfig appConfig;

    private AppConfig insertedAppConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppConfig createEntity() {
        return new AppConfig()
            .key(DEFAULT_KEY)
            .type(DEFAULT_TYPE)
            .value(DEFAULT_VALUE)
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
    public static AppConfig createUpdatedEntity() {
        return new AppConfig()
            .key(UPDATED_KEY)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
    }

    @BeforeEach
    public void initTest() {
        appConfig = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAppConfig != null) {
            appConfigRepository.delete(insertedAppConfig);
            insertedAppConfig = null;
        }
    }

    @Test
    @Transactional
    void createAppConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AppConfig
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);
        var returnedAppConfigDTO = om.readValue(
            restAppConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appConfigDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AppConfigDTO.class
        );

        // Validate the AppConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAppConfig = appConfigMapper.toEntity(returnedAppConfigDTO);
        assertAppConfigUpdatableFieldsEquals(returnedAppConfig, getPersistedAppConfig(returnedAppConfig));

        insertedAppConfig = returnedAppConfig;
    }

    @Test
    @Transactional
    void createAppConfigWithExistingId() throws Exception {
        // Create the AppConfig with an existing ID
        appConfig.setId(1L);
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AppConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appConfig.setExternalId(null);

        // Create the AppConfig, which fails.
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        restAppConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appConfig.setUuid(null);

        // Create the AppConfig, which fails.
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        restAppConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appConfig.setDateCreated(null);

        // Create the AppConfig, which fails.
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        restAppConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appConfig.setDateModified(null);

        // Create the AppConfig, which fails.
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        restAppConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSynchedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appConfig.setDateSynched(null);

        // Create the AppConfig, which fails.
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        restAppConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppConfigs() throws Exception {
        // Initialize the database
        insertedAppConfig = appConfigRepository.saveAndFlush(appConfig);

        // Get all the appConfigList
        restAppConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].dateSynched").value(hasItem(DEFAULT_DATE_SYNCHED.toString())))
            .andExpect(jsonPath("$.[*].dateDeleted").value(hasItem(DEFAULT_DATE_DELETED.toString())));
    }

    @Test
    @Transactional
    void getAppConfig() throws Exception {
        // Initialize the database
        insertedAppConfig = appConfigRepository.saveAndFlush(appConfig);

        // Get the appConfig
        restAppConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, appConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appConfig.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.dateSynched").value(DEFAULT_DATE_SYNCHED.toString()))
            .andExpect(jsonPath("$.dateDeleted").value(DEFAULT_DATE_DELETED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAppConfig() throws Exception {
        // Get the appConfig
        restAppConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppConfig() throws Exception {
        // Initialize the database
        insertedAppConfig = appConfigRepository.saveAndFlush(appConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appConfig
        AppConfig updatedAppConfig = appConfigRepository.findById(appConfig.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppConfig are not directly saved in db
        em.detach(updatedAppConfig);
        updatedAppConfig
            .key(UPDATED_KEY)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(updatedAppConfig);

        restAppConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the AppConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppConfigToMatchAllProperties(updatedAppConfig);
    }

    @Test
    @Transactional
    void putNonExistingAppConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appConfig.setId(longCount.incrementAndGet());

        // Create the AppConfig
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appConfig.setId(longCount.incrementAndGet());

        // Create the AppConfig
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appConfig.setId(longCount.incrementAndGet());

        // Create the AppConfig
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppConfigWithPatch() throws Exception {
        // Initialize the database
        insertedAppConfig = appConfigRepository.saveAndFlush(appConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appConfig using partial update
        AppConfig partialUpdatedAppConfig = new AppConfig();
        partialUpdatedAppConfig.setId(appConfig.getId());

        partialUpdatedAppConfig
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED);

        restAppConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppConfig))
            )
            .andExpect(status().isOk());

        // Validate the AppConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAppConfig, appConfig),
            getPersistedAppConfig(appConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdateAppConfigWithPatch() throws Exception {
        // Initialize the database
        insertedAppConfig = appConfigRepository.saveAndFlush(appConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appConfig using partial update
        AppConfig partialUpdatedAppConfig = new AppConfig();
        partialUpdatedAppConfig.setId(appConfig.getId());

        partialUpdatedAppConfig
            .key(UPDATED_KEY)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);

        restAppConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppConfig))
            )
            .andExpect(status().isOk());

        // Validate the AppConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppConfigUpdatableFieldsEquals(partialUpdatedAppConfig, getPersistedAppConfig(partialUpdatedAppConfig));
    }

    @Test
    @Transactional
    void patchNonExistingAppConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appConfig.setId(longCount.incrementAndGet());

        // Create the AppConfig
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appConfig.setId(longCount.incrementAndGet());

        // Create the AppConfig
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appConfig.setId(longCount.incrementAndGet());

        // Create the AppConfig
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppConfigMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppConfig() throws Exception {
        // Initialize the database
        insertedAppConfig = appConfigRepository.saveAndFlush(appConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appConfig
        restAppConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, appConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appConfigRepository.count();
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

    protected AppConfig getPersistedAppConfig(AppConfig appConfig) {
        return appConfigRepository.findById(appConfig.getId()).orElseThrow();
    }

    protected void assertPersistedAppConfigToMatchAllProperties(AppConfig expectedAppConfig) {
        assertAppConfigAllPropertiesEquals(expectedAppConfig, getPersistedAppConfig(expectedAppConfig));
    }

    protected void assertPersistedAppConfigToMatchUpdatableProperties(AppConfig expectedAppConfig) {
        assertAppConfigAllUpdatablePropertiesEquals(expectedAppConfig, getPersistedAppConfig(expectedAppConfig));
    }
}
