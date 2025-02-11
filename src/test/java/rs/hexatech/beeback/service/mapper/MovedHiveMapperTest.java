package rs.hexatech.beeback.service.mapper;

import static rs.hexatech.beeback.domain.MovedHiveAsserts.*;
import static rs.hexatech.beeback.domain.MovedHiveTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovedHiveMapperTest {

    private MovedHiveMapper movedHiveMapper;

    @BeforeEach
    void setUp() {
        movedHiveMapper = new MovedHiveMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMovedHiveSample1();
        var actual = movedHiveMapper.toEntity(movedHiveMapper.toDto(expected));
        assertMovedHiveAllPropertiesEquals(expected, actual);
    }
}
