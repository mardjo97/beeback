package rs.hexatech.beeback.service.mapper;

import static rs.hexatech.beeback.domain.ExaminationHiveAsserts.*;
import static rs.hexatech.beeback.domain.ExaminationHiveTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExaminationHiveMapperTest {

    private ExaminationHiveMapper examinationHiveMapper;

    @BeforeEach
    void setUp() {
        examinationHiveMapper = new ExaminationHiveMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExaminationHiveSample1();
        var actual = examinationHiveMapper.toEntity(examinationHiveMapper.toDto(expected));
        assertExaminationHiveAllPropertiesEquals(expected, actual);
    }
}
