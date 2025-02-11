package rs.hexatech.beeback.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rs.hexatech.beeback.domain.ExaminationHiveTestSamples.*;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class ExaminationHiveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExaminationHive.class);
        ExaminationHive examinationHive1 = getExaminationHiveSample1();
        ExaminationHive examinationHive2 = new ExaminationHive();
        assertThat(examinationHive1).isNotEqualTo(examinationHive2);

        examinationHive2.setId(examinationHive1.getId());
        assertThat(examinationHive1).isEqualTo(examinationHive2);

        examinationHive2 = getExaminationHiveSample2();
        assertThat(examinationHive1).isNotEqualTo(examinationHive2);
    }
}
