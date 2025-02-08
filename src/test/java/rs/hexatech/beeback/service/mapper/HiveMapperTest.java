package rs.hexatech.beeback.service.mapper;

import static rs.hexatech.beeback.domain.HiveAsserts.*;
import static rs.hexatech.beeback.domain.HiveTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HiveMapperTest {

    private HiveMapper hiveMapper;

    @BeforeEach
    void setUp() {
        hiveMapper = new HiveMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHiveSample1();
        var actual = hiveMapper.toEntity(hiveMapper.toDto(expected));
        assertHiveAllPropertiesEquals(expected, actual);
    }
}
