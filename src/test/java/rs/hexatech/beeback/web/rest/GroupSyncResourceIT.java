package rs.hexatech.beeback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.IntegrationTest;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.service.dto.GroupDTO;

/**
 * Integration tests for Group sync endpoints: GET /api/groups/all and POST /api/groups/sync.
 * All sync endpoints require a valid Device-Id header matching the current user's stored device id.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(username = "user")
class GroupSyncResourceIT {

    private static final String SYNC_DEVICE_ID = "sync-test-device";
    private static final String ENTITY_API_URL = "/api/groups";
    private static final String ALL_URL = ENTITY_API_URL + "/all";
    private static final String SYNC_URL = ENTITY_API_URL + "/sync";

    @Autowired
    private MockMvc restGroupMockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @Transactional
    void setDeviceIdForCurrentUser() {
        userRepository
            .findOneByLogin("user")
            .orElseThrow()
            .setDeviceId(SYNC_DEVICE_ID);
        userRepository.flush();
    }

    @Test
    void getAllWithValidDeviceId() throws Exception {
        ResultActions result = restGroupMockMvc
            .perform(get(ALL_URL).header("Device-Id", SYNC_DEVICE_ID).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));

        String json = result.andReturn().getResponse().getContentAsString();
        List<GroupDTO> groups = om.readValue(json, new TypeReference<>() {});
        assertThat(groups).isNotNull();
    }

    @Test
    void getAllWithoutDeviceId() throws Exception {
        restGroupMockMvc.perform(get(ALL_URL).accept(MediaType.APPLICATION_JSON)).andExpect(status().is(499));
    }

    @Test
    void getAllWithWrongDeviceId() throws Exception {
        restGroupMockMvc
            .perform(get(ALL_URL).header("Device-Id", "wrong-device").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is(499));
    }

    @Test
    void syncWithValidDeviceIdEmptyBody() throws Exception {
        ResultActions result = restGroupMockMvc
            .perform(
                post(SYNC_URL)
                    .header("Device-Id", SYNC_DEVICE_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("[]")
                    .accept(MediaType.APPLICATION_JSON)
            );

        result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));

        String json = result.andReturn().getResponse().getContentAsString();
        List<GroupDTO> synced = om.readValue(json, new TypeReference<>() {});
        assertThat(synced).isEmpty();
    }

    @Test
    void syncWithoutDeviceId() throws Exception {
        restGroupMockMvc
            .perform(
                post(SYNC_URL).contentType(MediaType.APPLICATION_JSON).content("[]").accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().is(499));
    }

    @Test
    void syncWithWrongDeviceId() throws Exception {
        restGroupMockMvc
            .perform(
                post(SYNC_URL)
                    .header("Device-Id", "wrong-device")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("[]")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().is(499));
    }

    @Test
    @Transactional
    void syncWithValidDeviceIdUpdatesExistingGroup() throws Exception {
        // Ensure user has groups (getAll creates default groups if none)
        String allJson = restGroupMockMvc
            .perform(get(ALL_URL).header("Device-Id", SYNC_DEVICE_ID).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<GroupDTO> groups = om.readValue(allJson, new TypeReference<>() {});
        assertThat(groups).isNotEmpty();

        GroupDTO toUpdate = groups.get(0);
        String originalName = toUpdate.getName();
        String updatedName = originalName + "-synced";
        toUpdate.setName(updatedName);

        ResultActions result = restGroupMockMvc.perform(
            post(SYNC_URL)
                .header("Device-Id", SYNC_DEVICE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(List.of(toUpdate)))
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());

        String syncJson = result.andReturn().getResponse().getContentAsString();
        List<GroupDTO> synced = om.readValue(syncJson, new TypeReference<>() {});
        assertThat(synced).hasSize(1);
        assertThat(synced.get(0).getName()).isEqualTo(updatedName);
        assertThat(synced.get(0).getUuid()).isEqualTo(toUpdate.getUuid());
    }
}
