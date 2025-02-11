package rs.hexatech.beeback.service.mapper;

import static rs.hexatech.beeback.domain.QueenAsserts.*;
import static rs.hexatech.beeback.domain.QueenTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QueenMapperTest {

    private QueenMapper queenMapper;

    @BeforeEach
    void setUp() {
        queenMapper = new QueenMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQueenSample1();
        var actual = queenMapper.toEntity(queenMapper.toDto(expected));
        assertQueenAllPropertiesEquals(expected, actual);
    }
}
