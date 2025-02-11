package rs.hexatech.beeback.service.mapper;

import static rs.hexatech.beeback.domain.FeedingHiveAsserts.*;
import static rs.hexatech.beeback.domain.FeedingHiveTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeedingHiveMapperTest {

    private FeedingHiveMapper feedingHiveMapper;

    @BeforeEach
    void setUp() {
        feedingHiveMapper = new FeedingHiveMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFeedingHiveSample1();
        var actual = feedingHiveMapper.toEntity(feedingHiveMapper.toDto(expected));
        assertFeedingHiveAllPropertiesEquals(expected, actual);
    }
}
