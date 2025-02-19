package rs.hexatech.beeback.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rs.hexatech.beeback.domain.FeedingHiveTestSamples.*;
import static rs.hexatech.beeback.domain.HiveTestSamples.*;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class FeedingHiveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeedingHive.class);
        FeedingHive feedingHive1 = getFeedingHiveSample1();
        FeedingHive feedingHive2 = new FeedingHive();
        assertThat(feedingHive1).isNotEqualTo(feedingHive2);

        feedingHive2.setId(feedingHive1.getId());
        assertThat(feedingHive1).isEqualTo(feedingHive2);

        feedingHive2 = getFeedingHiveSample2();
        assertThat(feedingHive1).isNotEqualTo(feedingHive2);
    }

    @Test
    void hiveTest() {
        FeedingHive feedingHive = getFeedingHiveRandomSampleGenerator();
        Hive hiveBack = getHiveRandomSampleGenerator();

        feedingHive.setHive(hiveBack);
        assertThat(feedingHive.getHive()).isEqualTo(hiveBack);

        feedingHive.hive(null);
        assertThat(feedingHive.getHive()).isNull();
    }
}
