package rs.hexatech.beeback.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.IntegrationTest;
import rs.hexatech.beeback.domain.Group;
import rs.hexatech.beeback.repository.GroupRepository;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.service.dto.GroupDTO;

/**
 * Integration tests for {@link GroupService} sync and user-entities behaviour.
 */
@IntegrationTest
@WithMockUser(username = "user")
@Transactional
class GroupServiceIT {

    private static final String SYNC_DEVICE_ID = "sync-service-test-device";

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setDeviceIdForCurrentUser() {
        userRepository.findOneByLogin("user").orElseThrow().setDeviceId(SYNC_DEVICE_ID);
        userRepository.flush();
    }

    @Test
    void userEntitiesReturnsGroupsOrDefaults() {
        List<GroupDTO> dtos = groupService.userEntities(SYNC_DEVICE_ID);
        assertThat(dtos).isNotNull();
        // Either user already has groups or default groups are created from findByUserIsNull()
        assertThat(dtos).isNotEmpty();
    }

    @Test
    void syncEmptyListReturnsEmpty() {
        List<GroupDTO> result = groupService.sync(List.of());
        assertThat(result).isEmpty();
    }

    @Test
    void syncWithExistingUuidUpdatesEntity() {
        List<GroupDTO> existing = groupService.userEntities(SYNC_DEVICE_ID);
        assertThat(existing).isNotEmpty();

        GroupDTO dto = existing.get(0);
        String originalName = dto.getName();
        String updatedName = originalName + "-service-test";
        dto.setName(updatedName);

        List<GroupDTO> synced = groupService.sync(List.of(dto));
        assertThat(synced).hasSize(1);
        assertThat(synced.get(0).getName()).isEqualTo(updatedName);
        assertThat(synced.get(0).getUuid()).isEqualTo(dto.getUuid());

        Group inDb = groupRepository.findByUuidIs(dto.getUuid());
        assertThat(inDb).isNotNull();
        assertThat(inDb.getName()).isEqualTo(updatedName);
    }

    @Test
    void syncWithUnknownUuidReturnsToResetDto() {
        GroupDTO dto = new GroupDTO();
        dto.setId(9999L);
        dto.setUuid("unknown-uuid-not-in-db");
        dto.setName("Test");
        dto.setEnumValueName("enum");
        dto.setColor("#fff");
        dto.setDateCreated(Instant.now());
        dto.setDateModified(Instant.now());
        dto.setDateSynched(Instant.now());

        List<GroupDTO> synced = groupService.sync(List.of(dto));
        // Service returns one DTO with uuid and dateSynched null (toReset case)
        assertThat(synced).hasSize(1);
        assertThat(synced.get(0).getUuid()).isNull();
        assertThat(synced.get(0).getDateSynched()).isNull();
    }
}
