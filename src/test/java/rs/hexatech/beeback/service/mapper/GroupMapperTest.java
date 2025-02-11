package rs.hexatech.beeback.service.mapper;

import static rs.hexatech.beeback.domain.GroupAsserts.*;
import static rs.hexatech.beeback.domain.GroupTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupMapperTest {

    private GroupMapper groupMapper;

    @BeforeEach
    void setUp() {
        groupMapper = new GroupMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGroupSample1();
        var actual = groupMapper.toEntity(groupMapper.toDto(expected));
        assertGroupAllPropertiesEquals(expected, actual);
    }
}
