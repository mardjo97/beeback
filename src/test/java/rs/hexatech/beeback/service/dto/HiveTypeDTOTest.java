package rs.hexatech.beeback.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class HiveTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HiveTypeDTO.class);
        HiveTypeDTO hiveTypeDTO1 = new HiveTypeDTO();
        hiveTypeDTO1.setId(1L);
        HiveTypeDTO hiveTypeDTO2 = new HiveTypeDTO();
        assertThat(hiveTypeDTO1).isNotEqualTo(hiveTypeDTO2);
        hiveTypeDTO2.setId(hiveTypeDTO1.getId());
        assertThat(hiveTypeDTO1).isEqualTo(hiveTypeDTO2);
        hiveTypeDTO2.setId(2L);
        assertThat(hiveTypeDTO1).isNotEqualTo(hiveTypeDTO2);
        hiveTypeDTO1.setId(null);
        assertThat(hiveTypeDTO1).isNotEqualTo(hiveTypeDTO2);
    }
}
