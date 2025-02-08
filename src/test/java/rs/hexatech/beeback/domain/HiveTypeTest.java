package rs.hexatech.beeback.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rs.hexatech.beeback.domain.HiveTypeTestSamples.*;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class HiveTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HiveType.class);
        HiveType hiveType1 = getHiveTypeSample1();
        HiveType hiveType2 = new HiveType();
        assertThat(hiveType1).isNotEqualTo(hiveType2);

        hiveType2.setId(hiveType1.getId());
        assertThat(hiveType1).isEqualTo(hiveType2);

        hiveType2 = getHiveTypeSample2();
        assertThat(hiveType1).isNotEqualTo(hiveType2);
    }
}
