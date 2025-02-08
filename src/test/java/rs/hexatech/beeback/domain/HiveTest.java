package rs.hexatech.beeback.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rs.hexatech.beeback.domain.ApiaryTestSamples.*;
import static rs.hexatech.beeback.domain.HiveTestSamples.*;
import static rs.hexatech.beeback.domain.HiveTypeTestSamples.*;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class HiveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hive.class);
        Hive hive1 = getHiveSample1();
        Hive hive2 = new Hive();
        assertThat(hive1).isNotEqualTo(hive2);

        hive2.setId(hive1.getId());
        assertThat(hive1).isEqualTo(hive2);

        hive2 = getHiveSample2();
        assertThat(hive1).isNotEqualTo(hive2);
    }

    @Test
    void hiveTypeTest() {
        Hive hive = getHiveRandomSampleGenerator();
        HiveType hiveTypeBack = getHiveTypeRandomSampleGenerator();

        hive.setHiveType(hiveTypeBack);
        assertThat(hive.getHiveType()).isEqualTo(hiveTypeBack);

        hive.hiveType(null);
        assertThat(hive.getHiveType()).isNull();
    }

    @Test
    void apiaryTest() {
        Hive hive = getHiveRandomSampleGenerator();
        Apiary apiaryBack = getApiaryRandomSampleGenerator();

        hive.setApiary(apiaryBack);
        assertThat(hive.getApiary()).isEqualTo(apiaryBack);

        hive.apiary(null);
        assertThat(hive.getApiary()).isNull();
    }
}
