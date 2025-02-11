package rs.hexatech.beeback.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rs.hexatech.beeback.domain.ReproductionHiveTestSamples.*;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class ReproductionHiveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReproductionHive.class);
        ReproductionHive reproductionHive1 = getReproductionHiveSample1();
        ReproductionHive reproductionHive2 = new ReproductionHive();
        assertThat(reproductionHive1).isNotEqualTo(reproductionHive2);

        reproductionHive2.setId(reproductionHive1.getId());
        assertThat(reproductionHive1).isEqualTo(reproductionHive2);

        reproductionHive2 = getReproductionHiveSample2();
        assertThat(reproductionHive1).isNotEqualTo(reproductionHive2);
    }
}
