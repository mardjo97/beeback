package rs.hexatech.beeback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rs.hexatech.beeback.domain.GroupAsserts.*;
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
import rs.hexatech.beeback.domain.Group;
import rs.hexatech.beeback.repository.GroupRepository;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.service.dto.GroupDTO;
import rs.hexatech.beeback.service.mapper.GroupMapper;

/**
 * Integration tests for the {@link GroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ENUM_VALUE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENUM_VALUE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_HIVE_COUNT = 1;
    private static final Integer UPDATED_HIVE_COUNT = 2;

    private static final Integer DEFAULT_HIVE_COUNT_FINISHED = 1;
    private static final Integer UPDATED_HIVE_COUNT_FINISHED = 2;

    private static final String DEFAULT_ADDITIONAL_INFO = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_INFO = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER_NUMBER = 1;
    private static final Integer UPDATED_ORDER_NUMBER = 2;

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

    private static final String ENTITY_API_URL = "/api/groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGroupMockMvc;

    private Group group;

    private Group insertedGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Group createEntity() {
        return new Group()
            .name(DEFAULT_NAME)
            .enumValueName(DEFAULT_ENUM_VALUE_NAME)
            .color(DEFAULT_COLOR)
            .hiveCount(DEFAULT_HIVE_COUNT)
            .hiveCountFinished(DEFAULT_HIVE_COUNT_FINISHED)
            .additionalInfo(DEFAULT_ADDITIONAL_INFO)
            .orderNumber(DEFAULT_ORDER_NUMBER)
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
    public static Group createUpdatedEntity() {
        return new Group()
            .name(UPDATED_NAME)
            .enumValueName(UPDATED_ENUM_VALUE_NAME)
            .color(UPDATED_COLOR)
            .hiveCount(UPDATED_HIVE_COUNT)
            .hiveCountFinished(UPDATED_HIVE_COUNT_FINISHED)
            .additionalInfo(UPDATED_ADDITIONAL_INFO)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
    }

    @BeforeEach
    public void initTest() {
        group = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedGroup != null) {
            groupRepository.delete(insertedGroup);
            insertedGroup = null;
        }
    }

    @Test
    @Transactional
    void createGroup() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Group
        GroupDTO groupDTO = groupMapper.toDto(group);
        var returnedGroupDTO = om.readValue(
            restGroupMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(groupDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GroupDTO.class
        );

        // Validate the Group in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGroup = groupMapper.toEntity(returnedGroupDTO);
        assertGroupUpdatableFieldsEquals(returnedGroup, getPersistedGroup(returnedGroup));

        insertedGroup = returnedGroup;
    }

    @Test
    @Transactional
    void createGroupWithExistingId() throws Exception {
        // Create the Group with an existing ID
        group.setId(1L);
        GroupDTO groupDTO = groupMapper.toDto(group);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(groupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        group.setExternalId(null);

        // Create the Group, which fails.
        GroupDTO groupDTO = groupMapper.toDto(group);

        restGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(groupDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        group.setUuid(null);

        // Create the Group, which fails.
        GroupDTO groupDTO = groupMapper.toDto(group);

        restGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(groupDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        group.setDateCreated(null);

        // Create the Group, which fails.
        GroupDTO groupDTO = groupMapper.toDto(group);

        restGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(groupDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        group.setDateModified(null);

        // Create the Group, which fails.
        GroupDTO groupDTO = groupMapper.toDto(group);

        restGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(groupDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSynchedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        group.setDateSynched(null);

        // Create the Group, which fails.
        GroupDTO groupDTO = groupMapper.toDto(group);

        restGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(groupDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGroups() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get all the groupList
        restGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(group.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].enumValueName").value(hasItem(DEFAULT_ENUM_VALUE_NAME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].hiveCount").value(hasItem(DEFAULT_HIVE_COUNT)))
            .andExpect(jsonPath("$.[*].hiveCountFinished").value(hasItem(DEFAULT_HIVE_COUNT_FINISHED)))
            .andExpect(jsonPath("$.[*].additionalInfo").value(hasItem(DEFAULT_ADDITIONAL_INFO)))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].dateSynched").value(hasItem(DEFAULT_DATE_SYNCHED.toString())))
            .andExpect(jsonPath("$.[*].dateDeleted").value(hasItem(DEFAULT_DATE_DELETED.toString())));
    }

    @Test
    @Transactional
    void getGroup() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get the group
        restGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, group.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(group.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.enumValueName").value(DEFAULT_ENUM_VALUE_NAME))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.hiveCount").value(DEFAULT_HIVE_COUNT))
            .andExpect(jsonPath("$.hiveCountFinished").value(DEFAULT_HIVE_COUNT_FINISHED))
            .andExpect(jsonPath("$.additionalInfo").value(DEFAULT_ADDITIONAL_INFO))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.dateSynched").value(DEFAULT_DATE_SYNCHED.toString()))
            .andExpect(jsonPath("$.dateDeleted").value(DEFAULT_DATE_DELETED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingGroup() throws Exception {
        // Get the group
        restGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGroup() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the group
        Group updatedGroup = groupRepository.findById(group.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGroup are not directly saved in db
        em.detach(updatedGroup);
        updatedGroup
            .name(UPDATED_NAME)
            .enumValueName(UPDATED_ENUM_VALUE_NAME)
            .color(UPDATED_COLOR)
            .hiveCount(UPDATED_HIVE_COUNT)
            .hiveCountFinished(UPDATED_HIVE_COUNT_FINISHED)
            .additionalInfo(UPDATED_ADDITIONAL_INFO)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);
        GroupDTO groupDTO = groupMapper.toDto(updatedGroup);

        restGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, groupDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(groupDTO))
            )
            .andExpect(status().isOk());

        // Validate the Group in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGroupToMatchAllProperties(updatedGroup);
    }

    @Test
    @Transactional
    void putNonExistingGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        group.setId(longCount.incrementAndGet());

        // Create the Group
        GroupDTO groupDTO = groupMapper.toDto(group);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, groupDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(groupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        group.setId(longCount.incrementAndGet());

        // Create the Group
        GroupDTO groupDTO = groupMapper.toDto(group);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(groupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        group.setId(longCount.incrementAndGet());

        // Create the Group
        GroupDTO groupDTO = groupMapper.toDto(group);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(groupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Group in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGroupWithPatch() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the group using partial update
        Group partialUpdatedGroup = new Group();
        partialUpdatedGroup.setId(group.getId());

        partialUpdatedGroup
            .enumValueName(UPDATED_ENUM_VALUE_NAME)
            .color(UPDATED_COLOR)
            .hiveCount(UPDATED_HIVE_COUNT)
            .hiveCountFinished(UPDATED_HIVE_COUNT_FINISHED)
            .additionalInfo(UPDATED_ADDITIONAL_INFO)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);

        restGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGroup))
            )
            .andExpect(status().isOk());

        // Validate the Group in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGroupUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedGroup, group), getPersistedGroup(group));
    }

    @Test
    @Transactional
    void fullUpdateGroupWithPatch() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the group using partial update
        Group partialUpdatedGroup = new Group();
        partialUpdatedGroup.setId(group.getId());

        partialUpdatedGroup
            .name(UPDATED_NAME)
            .enumValueName(UPDATED_ENUM_VALUE_NAME)
            .color(UPDATED_COLOR)
            .hiveCount(UPDATED_HIVE_COUNT)
            .hiveCountFinished(UPDATED_HIVE_COUNT_FINISHED)
            .additionalInfo(UPDATED_ADDITIONAL_INFO)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED)
            .dateDeleted(UPDATED_DATE_DELETED);

        restGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGroup))
            )
            .andExpect(status().isOk());

        // Validate the Group in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGroupUpdatableFieldsEquals(partialUpdatedGroup, getPersistedGroup(partialUpdatedGroup));
    }

    @Test
    @Transactional
    void patchNonExistingGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        group.setId(longCount.incrementAndGet());

        // Create the Group
        GroupDTO groupDTO = groupMapper.toDto(group);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, groupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(groupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        group.setId(longCount.incrementAndGet());

        // Create the Group
        GroupDTO groupDTO = groupMapper.toDto(group);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(groupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        group.setId(longCount.incrementAndGet());

        // Create the Group
        GroupDTO groupDTO = groupMapper.toDto(group);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(groupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Group in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGroup() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the group
        restGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, group.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return groupRepository.count();
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

    protected Group getPersistedGroup(Group group) {
        return groupRepository.findById(group.getId()).orElseThrow();
    }

    protected void assertPersistedGroupToMatchAllProperties(Group expectedGroup) {
        assertGroupAllPropertiesEquals(expectedGroup, getPersistedGroup(expectedGroup));
    }

    protected void assertPersistedGroupToMatchUpdatableProperties(Group expectedGroup) {
        assertGroupAllUpdatablePropertiesEquals(expectedGroup, getPersistedGroup(expectedGroup));
    }
}
