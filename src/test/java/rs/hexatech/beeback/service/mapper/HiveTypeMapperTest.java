package rs.hexatech.beeback.service.mapper;

import static rs.hexatech.beeback.domain.HiveTypeAsserts.*;
import static rs.hexatech.beeback.domain.HiveTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HiveTypeMapperTest {

    private HiveTypeMapper hiveTypeMapper;

    @BeforeEach
    void setUp() {
        hiveTypeMapper = new HiveTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHiveTypeSample1();
        var actual = hiveTypeMapper.toEntity(hiveTypeMapper.toDto(expected));
        assertHiveTypeAllPropertiesEquals(expected, actual);
    }
}
