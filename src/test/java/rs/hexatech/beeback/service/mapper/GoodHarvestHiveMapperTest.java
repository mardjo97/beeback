package rs.hexatech.beeback.service.mapper;

import static rs.hexatech.beeback.domain.GoodHarvestHiveAsserts.*;
import static rs.hexatech.beeback.domain.GoodHarvestHiveTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GoodHarvestHiveMapperTest {

    private GoodHarvestHiveMapper goodHarvestHiveMapper;

    @BeforeEach
    void setUp() {
        goodHarvestHiveMapper = new GoodHarvestHiveMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGoodHarvestHiveSample1();
        var actual = goodHarvestHiveMapper.toEntity(goodHarvestHiveMapper.toDto(expected));
        assertGoodHarvestHiveAllPropertiesEquals(expected, actual);
    }
}
