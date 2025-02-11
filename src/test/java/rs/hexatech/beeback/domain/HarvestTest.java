package rs.hexatech.beeback.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rs.hexatech.beeback.domain.HarvestTestSamples.*;
import static rs.hexatech.beeback.domain.HarvestTypeTestSamples.*;
import static rs.hexatech.beeback.domain.HiveTestSamples.*;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class HarvestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Harvest.class);
        Harvest harvest1 = getHarvestSample1();
        Harvest harvest2 = new Harvest();
        assertThat(harvest1).isNotEqualTo(harvest2);

        harvest2.setId(harvest1.getId());
        assertThat(harvest1).isEqualTo(harvest2);

        harvest2 = getHarvestSample2();
        assertThat(harvest1).isNotEqualTo(harvest2);
    }

    @Test
    void hiveTest() {
        Harvest harvest = getHarvestRandomSampleGenerator();
        Hive hiveBack = getHiveRandomSampleGenerator();

        harvest.setHive(hiveBack);
        assertThat(harvest.getHive()).isEqualTo(hiveBack);

        harvest.hive(null);
        assertThat(harvest.getHive()).isNull();
    }

    @Test
    void harvestTypeTest() {
        Harvest harvest = getHarvestRandomSampleGenerator();
        HarvestType harvestTypeBack = getHarvestTypeRandomSampleGenerator();

        harvest.setHarvestType(harvestTypeBack);
        assertThat(harvest.getHarvestType()).isEqualTo(harvestTypeBack);

        harvest.harvestType(null);
        assertThat(harvest.getHarvestType()).isNull();
    }
}
