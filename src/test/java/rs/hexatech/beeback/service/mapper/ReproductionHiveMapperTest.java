package rs.hexatech.beeback.service.mapper;

import static rs.hexatech.beeback.domain.ReproductionHiveAsserts.*;
import static rs.hexatech.beeback.domain.ReproductionHiveTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReproductionHiveMapperTest {

    private ReproductionHiveMapper reproductionHiveMapper;

    @BeforeEach
    void setUp() {
        reproductionHiveMapper = new ReproductionHiveMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReproductionHiveSample1();
        var actual = reproductionHiveMapper.toEntity(reproductionHiveMapper.toDto(expected));
        assertReproductionHiveAllPropertiesEquals(expected, actual);
    }
}
