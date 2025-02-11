package rs.hexatech.beeback.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class FeedingHiveDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeedingHiveDTO.class);
        FeedingHiveDTO feedingHiveDTO1 = new FeedingHiveDTO();
        feedingHiveDTO1.setId(1L);
        FeedingHiveDTO feedingHiveDTO2 = new FeedingHiveDTO();
        assertThat(feedingHiveDTO1).isNotEqualTo(feedingHiveDTO2);
        feedingHiveDTO2.setId(feedingHiveDTO1.getId());
        assertThat(feedingHiveDTO1).isEqualTo(feedingHiveDTO2);
        feedingHiveDTO2.setId(2L);
        assertThat(feedingHiveDTO1).isNotEqualTo(feedingHiveDTO2);
        feedingHiveDTO1.setId(null);
        assertThat(feedingHiveDTO1).isNotEqualTo(feedingHiveDTO2);
    }
}
