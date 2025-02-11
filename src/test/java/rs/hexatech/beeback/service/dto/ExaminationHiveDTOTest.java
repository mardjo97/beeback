package rs.hexatech.beeback.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class ExaminationHiveDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExaminationHiveDTO.class);
        ExaminationHiveDTO examinationHiveDTO1 = new ExaminationHiveDTO();
        examinationHiveDTO1.setId(1L);
        ExaminationHiveDTO examinationHiveDTO2 = new ExaminationHiveDTO();
        assertThat(examinationHiveDTO1).isNotEqualTo(examinationHiveDTO2);
        examinationHiveDTO2.setId(examinationHiveDTO1.getId());
        assertThat(examinationHiveDTO1).isEqualTo(examinationHiveDTO2);
        examinationHiveDTO2.setId(2L);
        assertThat(examinationHiveDTO1).isNotEqualTo(examinationHiveDTO2);
        examinationHiveDTO1.setId(null);
        assertThat(examinationHiveDTO1).isNotEqualTo(examinationHiveDTO2);
    }
}
