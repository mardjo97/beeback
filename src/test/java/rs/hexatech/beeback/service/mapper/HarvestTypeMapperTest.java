package rs.hexatech.beeback.service.mapper;

import static rs.hexatech.beeback.domain.HarvestTypeAsserts.*;
import static rs.hexatech.beeback.domain.HarvestTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HarvestTypeMapperTest {

    private HarvestTypeMapper harvestTypeMapper;

    @BeforeEach
    void setUp() {
        harvestTypeMapper = new HarvestTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHarvestTypeSample1();
        var actual = harvestTypeMapper.toEntity(harvestTypeMapper.toDto(expected));
        assertHarvestTypeAllPropertiesEquals(expected, actual);
    }
}
