package rs.hexatech.beeback.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rs.hexatech.beeback.domain.GoodHarvestHiveTestSamples.*;
import static rs.hexatech.beeback.domain.HarvestTypeTestSamples.*;
import static rs.hexatech.beeback.domain.HiveTestSamples.*;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class GoodHarvestHiveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GoodHarvestHive.class);
        GoodHarvestHive goodHarvestHive1 = getGoodHarvestHiveSample1();
        GoodHarvestHive goodHarvestHive2 = new GoodHarvestHive();
        assertThat(goodHarvestHive1).isNotEqualTo(goodHarvestHive2);

        goodHarvestHive2.setId(goodHarvestHive1.getId());
        assertThat(goodHarvestHive1).isEqualTo(goodHarvestHive2);

        goodHarvestHive2 = getGoodHarvestHiveSample2();
        assertThat(goodHarvestHive1).isNotEqualTo(goodHarvestHive2);
    }

    @Test
    void hiveTest() {
        GoodHarvestHive goodHarvestHive = getGoodHarvestHiveRandomSampleGenerator();
        Hive hiveBack = getHiveRandomSampleGenerator();

        goodHarvestHive.setHive(hiveBack);
        assertThat(goodHarvestHive.getHive()).isEqualTo(hiveBack);

        goodHarvestHive.hive(null);
        assertThat(goodHarvestHive.getHive()).isNull();
    }

    @Test
    void harvestTypeTest() {
        GoodHarvestHive goodHarvestHive = getGoodHarvestHiveRandomSampleGenerator();
        HarvestType harvestTypeBack = getHarvestTypeRandomSampleGenerator();

        goodHarvestHive.setHarvestType(harvestTypeBack);
        assertThat(goodHarvestHive.getHarvestType()).isEqualTo(harvestTypeBack);

        goodHarvestHive.harvestType(null);
        assertThat(goodHarvestHive.getHarvestType()).isNull();
    }
}
