package rs.hexatech.beeback.service.mapper;

import static rs.hexatech.beeback.domain.HarvestAsserts.*;
import static rs.hexatech.beeback.domain.HarvestTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HarvestMapperTest {

    private HarvestMapper harvestMapper;

    @BeforeEach
    void setUp() {
        harvestMapper = new HarvestMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHarvestSample1();
        var actual = harvestMapper.toEntity(harvestMapper.toDto(expected));
        assertHarvestAllPropertiesEquals(expected, actual);
    }
}
