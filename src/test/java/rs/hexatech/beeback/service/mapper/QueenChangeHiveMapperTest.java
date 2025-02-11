package rs.hexatech.beeback.service.mapper;

import static rs.hexatech.beeback.domain.QueenChangeHiveAsserts.*;
import static rs.hexatech.beeback.domain.QueenChangeHiveTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QueenChangeHiveMapperTest {

    private QueenChangeHiveMapper queenChangeHiveMapper;

    @BeforeEach
    void setUp() {
        queenChangeHiveMapper = new QueenChangeHiveMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQueenChangeHiveSample1();
        var actual = queenChangeHiveMapper.toEntity(queenChangeHiveMapper.toDto(expected));
        assertQueenChangeHiveAllPropertiesEquals(expected, actual);
    }
}
