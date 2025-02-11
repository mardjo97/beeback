package rs.hexatech.beeback.service.mapper;

import static rs.hexatech.beeback.domain.HiveLocationAsserts.*;
import static rs.hexatech.beeback.domain.HiveLocationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HiveLocationMapperTest {

    private HiveLocationMapper hiveLocationMapper;

    @BeforeEach
    void setUp() {
        hiveLocationMapper = new HiveLocationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHiveLocationSample1();
        var actual = hiveLocationMapper.toEntity(hiveLocationMapper.toDto(expected));
        assertHiveLocationAllPropertiesEquals(expected, actual);
    }
}
