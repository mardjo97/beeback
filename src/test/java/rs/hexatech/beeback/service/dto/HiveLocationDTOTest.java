package rs.hexatech.beeback.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class HiveLocationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HiveLocationDTO.class);
        HiveLocationDTO hiveLocationDTO1 = new HiveLocationDTO();
        hiveLocationDTO1.setId(1L);
        HiveLocationDTO hiveLocationDTO2 = new HiveLocationDTO();
        assertThat(hiveLocationDTO1).isNotEqualTo(hiveLocationDTO2);
        hiveLocationDTO2.setId(hiveLocationDTO1.getId());
        assertThat(hiveLocationDTO1).isEqualTo(hiveLocationDTO2);
        hiveLocationDTO2.setId(2L);
        assertThat(hiveLocationDTO1).isNotEqualTo(hiveLocationDTO2);
        hiveLocationDTO1.setId(null);
        assertThat(hiveLocationDTO1).isNotEqualTo(hiveLocationDTO2);
    }
}
