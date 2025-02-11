package rs.hexatech.beeback.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class MovedHiveDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovedHiveDTO.class);
        MovedHiveDTO movedHiveDTO1 = new MovedHiveDTO();
        movedHiveDTO1.setId(1L);
        MovedHiveDTO movedHiveDTO2 = new MovedHiveDTO();
        assertThat(movedHiveDTO1).isNotEqualTo(movedHiveDTO2);
        movedHiveDTO2.setId(movedHiveDTO1.getId());
        assertThat(movedHiveDTO1).isEqualTo(movedHiveDTO2);
        movedHiveDTO2.setId(2L);
        assertThat(movedHiveDTO1).isNotEqualTo(movedHiveDTO2);
        movedHiveDTO1.setId(null);
        assertThat(movedHiveDTO1).isNotEqualTo(movedHiveDTO2);
    }
}
