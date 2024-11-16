package rs.hexatech.beeback.service.mapper;

import static rs.hexatech.beeback.domain.ApiaryAsserts.*;
import static rs.hexatech.beeback.domain.ApiaryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApiaryMapperTest {

    private ApiaryMapper apiaryMapper;

    @BeforeEach
    void setUp() {
        apiaryMapper = new ApiaryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getApiarySample1();
        var actual = apiaryMapper.toEntity(apiaryMapper.toDto(expected));
        assertApiaryAllPropertiesEquals(expected, actual);
    }
}
