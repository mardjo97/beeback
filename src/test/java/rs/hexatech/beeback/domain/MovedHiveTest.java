package rs.hexatech.beeback.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rs.hexatech.beeback.domain.HarvestTypeTestSamples.*;
import static rs.hexatech.beeback.domain.HiveTestSamples.*;
import static rs.hexatech.beeback.domain.MovedHiveTestSamples.*;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class MovedHiveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovedHive.class);
        MovedHive movedHive1 = getMovedHiveSample1();
        MovedHive movedHive2 = new MovedHive();
        assertThat(movedHive1).isNotEqualTo(movedHive2);

        movedHive2.setId(movedHive1.getId());
        assertThat(movedHive1).isEqualTo(movedHive2);

        movedHive2 = getMovedHiveSample2();
        assertThat(movedHive1).isNotEqualTo(movedHive2);
    }

    @Test
    void hiveTest() {
        MovedHive movedHive = getMovedHiveRandomSampleGenerator();
        Hive hiveBack = getHiveRandomSampleGenerator();

        movedHive.setHive(hiveBack);
        assertThat(movedHive.getHive()).isEqualTo(hiveBack);

        movedHive.hive(null);
        assertThat(movedHive.getHive()).isNull();
    }

    @Test
    void harvestTypeTest() {
        MovedHive movedHive = getMovedHiveRandomSampleGenerator();
        HarvestType harvestTypeBack = getHarvestTypeRandomSampleGenerator();

        movedHive.setHarvestType(harvestTypeBack);
        assertThat(movedHive.getHarvestType()).isEqualTo(harvestTypeBack);

        movedHive.harvestType(null);
        assertThat(movedHive.getHarvestType()).isNull();
    }
}
