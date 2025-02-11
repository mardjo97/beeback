package rs.hexatech.beeback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rs.hexatech.beeback.domain.QueenAsserts.*;
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
import rs.hexatech.beeback.domain.Queen;
import rs.hexatech.beeback.repository.QueenRepository;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.service.dto.QueenDTO;
import rs.hexatech.beeback.service.mapper.QueenMapper;

/**
 * Integration tests for the {@link QueenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QueenResourceIT {

    private static final String DEFAULT_ORIGIN = "AAAAAAAAAA";
    private static final String UPDATED_ORIGIN = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Boolean DEFAULT_IS_MARKED = false;
    private static final Boolean UPDATED_IS_MARKED = true;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Instant DEFAULT_ACTIVE_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTIVE_FROM_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ACTIVE_TO_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTIVE_TO_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_QUEEN_CHANGE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_QUEEN_CHANGE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

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

    private static final String ENTITY_API_URL = "/api/queens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QueenRepository queenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QueenMapper queenMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQueenMockMvc;

    private Queen queen;

    private Queen insertedQueen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Queen createEntity() {
        return new Queen()
            .origin(DEFAULT_ORIGIN)
            .year(DEFAULT_YEAR)
            .isMarked(DEFAULT_IS_MARKED)
            .active(DEFAULT_ACTIVE)
            .activeFromDate(DEFAULT_ACTIVE_FROM_DATE)
            .activeToDate(DEFAULT_ACTIVE_TO_DATE)
            .queenChangeDate(DEFAULT_QUEEN_CHANGE_DATE)
            .externalId(DEFAULT_EXTERNAL_ID)
            .uuid(DEFAULT_UUID)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateModified(DEFAULT_DATE_MODIFIED)
            .dateSynched(DEFAULT_DATE_SYNCHED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Queen createUpdatedEntity() {
        return new Queen()
            .origin(UPDATED_ORIGIN)
            .year(UPDATED_YEAR)
            .isMarked(UPDATED_IS_MARKED)
            .active(UPDATED_ACTIVE)
            .activeFromDate(UPDATED_ACTIVE_FROM_DATE)
            .activeToDate(UPDATED_ACTIVE_TO_DATE)
            .queenChangeDate(UPDATED_QUEEN_CHANGE_DATE)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED);
    }

    @BeforeEach
    public void initTest() {
        queen = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedQueen != null) {
            queenRepository.delete(insertedQueen);
            insertedQueen = null;
        }
    }

    @Test
    @Transactional
    void createQueen() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Queen
        QueenDTO queenDTO = queenMapper.toDto(queen);
        var returnedQueenDTO = om.readValue(
            restQueenMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QueenDTO.class
        );

        // Validate the Queen in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQueen = queenMapper.toEntity(returnedQueenDTO);
        assertQueenUpdatableFieldsEquals(returnedQueen, getPersistedQueen(returnedQueen));

        insertedQueen = returnedQueen;
    }

    @Test
    @Transactional
    void createQueenWithExistingId() throws Exception {
        // Create the Queen with an existing ID
        queen.setId(1L);
        QueenDTO queenDTO = queenMapper.toDto(queen);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQueenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Queen in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        queen.setExternalId(null);

        // Create the Queen, which fails.
        QueenDTO queenDTO = queenMapper.toDto(queen);

        restQueenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        queen.setUuid(null);

        // Create the Queen, which fails.
        QueenDTO queenDTO = queenMapper.toDto(queen);

        restQueenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        queen.setDateCreated(null);

        // Create the Queen, which fails.
        QueenDTO queenDTO = queenMapper.toDto(queen);

        restQueenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        queen.setDateModified(null);

        // Create the Queen, which fails.
        QueenDTO queenDTO = queenMapper.toDto(queen);

        restQueenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSynchedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        queen.setDateSynched(null);

        // Create the Queen, which fails.
        QueenDTO queenDTO = queenMapper.toDto(queen);

        restQueenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQueens() throws Exception {
        // Initialize the database
        insertedQueen = queenRepository.saveAndFlush(queen);

        // Get all the queenList
        restQueenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(queen.getId().intValue())))
            .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].isMarked").value(hasItem(DEFAULT_IS_MARKED.booleanValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].activeFromDate").value(hasItem(DEFAULT_ACTIVE_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].activeToDate").value(hasItem(DEFAULT_ACTIVE_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].queenChangeDate").value(hasItem(DEFAULT_QUEEN_CHANGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].dateSynched").value(hasItem(DEFAULT_DATE_SYNCHED.toString())));
    }

    @Test
    @Transactional
    void getQueen() throws Exception {
        // Initialize the database
        insertedQueen = queenRepository.saveAndFlush(queen);

        // Get the queen
        restQueenMockMvc
            .perform(get(ENTITY_API_URL_ID, queen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(queen.getId().intValue()))
            .andExpect(jsonPath("$.origin").value(DEFAULT_ORIGIN))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.isMarked").value(DEFAULT_IS_MARKED.booleanValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.activeFromDate").value(DEFAULT_ACTIVE_FROM_DATE.toString()))
            .andExpect(jsonPath("$.activeToDate").value(DEFAULT_ACTIVE_TO_DATE.toString()))
            .andExpect(jsonPath("$.queenChangeDate").value(DEFAULT_QUEEN_CHANGE_DATE.toString()))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.dateSynched").value(DEFAULT_DATE_SYNCHED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingQueen() throws Exception {
        // Get the queen
        restQueenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQueen() throws Exception {
        // Initialize the database
        insertedQueen = queenRepository.saveAndFlush(queen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the queen
        Queen updatedQueen = queenRepository.findById(queen.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQueen are not directly saved in db
        em.detach(updatedQueen);
        updatedQueen
            .origin(UPDATED_ORIGIN)
            .year(UPDATED_YEAR)
            .isMarked(UPDATED_IS_MARKED)
            .active(UPDATED_ACTIVE)
            .activeFromDate(UPDATED_ACTIVE_FROM_DATE)
            .activeToDate(UPDATED_ACTIVE_TO_DATE)
            .queenChangeDate(UPDATED_QUEEN_CHANGE_DATE)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED);
        QueenDTO queenDTO = queenMapper.toDto(updatedQueen);

        restQueenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, queenDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenDTO))
            )
            .andExpect(status().isOk());

        // Validate the Queen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQueenToMatchAllProperties(updatedQueen);
    }

    @Test
    @Transactional
    void putNonExistingQueen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        queen.setId(longCount.incrementAndGet());

        // Create the Queen
        QueenDTO queenDTO = queenMapper.toDto(queen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQueenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, queenDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Queen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQueen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        queen.setId(longCount.incrementAndGet());

        // Create the Queen
        QueenDTO queenDTO = queenMapper.toDto(queen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(queenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Queen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQueen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        queen.setId(longCount.incrementAndGet());

        // Create the Queen
        QueenDTO queenDTO = queenMapper.toDto(queen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(queenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Queen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQueenWithPatch() throws Exception {
        // Initialize the database
        insertedQueen = queenRepository.saveAndFlush(queen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the queen using partial update
        Queen partialUpdatedQueen = new Queen();
        partialUpdatedQueen.setId(queen.getId());

        partialUpdatedQueen
            .year(UPDATED_YEAR)
            .activeToDate(UPDATED_ACTIVE_TO_DATE)
            .queenChangeDate(UPDATED_QUEEN_CHANGE_DATE)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED);

        restQueenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQueen.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQueen))
            )
            .andExpect(status().isOk());

        // Validate the Queen in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQueenUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedQueen, queen), getPersistedQueen(queen));
    }

    @Test
    @Transactional
    void fullUpdateQueenWithPatch() throws Exception {
        // Initialize the database
        insertedQueen = queenRepository.saveAndFlush(queen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the queen using partial update
        Queen partialUpdatedQueen = new Queen();
        partialUpdatedQueen.setId(queen.getId());

        partialUpdatedQueen
            .origin(UPDATED_ORIGIN)
            .year(UPDATED_YEAR)
            .isMarked(UPDATED_IS_MARKED)
            .active(UPDATED_ACTIVE)
            .activeFromDate(UPDATED_ACTIVE_FROM_DATE)
            .activeToDate(UPDATED_ACTIVE_TO_DATE)
            .queenChangeDate(UPDATED_QUEEN_CHANGE_DATE)
            .externalId(UPDATED_EXTERNAL_ID)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .dateSynched(UPDATED_DATE_SYNCHED);

        restQueenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQueen.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQueen))
            )
            .andExpect(status().isOk());

        // Validate the Queen in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQueenUpdatableFieldsEquals(partialUpdatedQueen, getPersistedQueen(partialUpdatedQueen));
    }

    @Test
    @Transactional
    void patchNonExistingQueen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        queen.setId(longCount.incrementAndGet());

        // Create the Queen
        QueenDTO queenDTO = queenMapper.toDto(queen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQueenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, queenDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(queenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Queen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQueen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        queen.setId(longCount.incrementAndGet());

        // Create the Queen
        QueenDTO queenDTO = queenMapper.toDto(queen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(queenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Queen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQueen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        queen.setId(longCount.incrementAndGet());

        // Create the Queen
        QueenDTO queenDTO = queenMapper.toDto(queen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueenMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(queenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Queen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQueen() throws Exception {
        // Initialize the database
        insertedQueen = queenRepository.saveAndFlush(queen);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the queen
        restQueenMockMvc
            .perform(delete(ENTITY_API_URL_ID, queen.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return queenRepository.count();
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

    protected Queen getPersistedQueen(Queen queen) {
        return queenRepository.findById(queen.getId()).orElseThrow();
    }

    protected void assertPersistedQueenToMatchAllProperties(Queen expectedQueen) {
        assertQueenAllPropertiesEquals(expectedQueen, getPersistedQueen(expectedQueen));
    }

    protected void assertPersistedQueenToMatchUpdatableProperties(Queen expectedQueen) {
        assertQueenAllUpdatablePropertiesEquals(expectedQueen, getPersistedQueen(expectedQueen));
    }
}
