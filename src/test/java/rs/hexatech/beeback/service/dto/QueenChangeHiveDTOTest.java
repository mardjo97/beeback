package rs.hexatech.beeback.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class QueenChangeHiveDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QueenChangeHiveDTO.class);
        QueenChangeHiveDTO queenChangeHiveDTO1 = new QueenChangeHiveDTO();
        queenChangeHiveDTO1.setId(1L);
        QueenChangeHiveDTO queenChangeHiveDTO2 = new QueenChangeHiveDTO();
        assertThat(queenChangeHiveDTO1).isNotEqualTo(queenChangeHiveDTO2);
        queenChangeHiveDTO2.setId(queenChangeHiveDTO1.getId());
        assertThat(queenChangeHiveDTO1).isEqualTo(queenChangeHiveDTO2);
        queenChangeHiveDTO2.setId(2L);
        assertThat(queenChangeHiveDTO1).isNotEqualTo(queenChangeHiveDTO2);
        queenChangeHiveDTO1.setId(null);
        assertThat(queenChangeHiveDTO1).isNotEqualTo(queenChangeHiveDTO2);
    }
}
