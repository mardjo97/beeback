package rs.hexatech.beeback.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rs.hexatech.beeback.domain.HiveLocationTestSamples.*;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class HiveLocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HiveLocation.class);
        HiveLocation hiveLocation1 = getHiveLocationSample1();
        HiveLocation hiveLocation2 = new HiveLocation();
        assertThat(hiveLocation1).isNotEqualTo(hiveLocation2);

        hiveLocation2.setId(hiveLocation1.getId());
        assertThat(hiveLocation1).isEqualTo(hiveLocation2);

        hiveLocation2 = getHiveLocationSample2();
        assertThat(hiveLocation1).isNotEqualTo(hiveLocation2);
    }
}
