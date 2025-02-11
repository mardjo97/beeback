package rs.hexatech.beeback.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class QueenDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QueenDTO.class);
        QueenDTO queenDTO1 = new QueenDTO();
        queenDTO1.setId(1L);
        QueenDTO queenDTO2 = new QueenDTO();
        assertThat(queenDTO1).isNotEqualTo(queenDTO2);
        queenDTO2.setId(queenDTO1.getId());
        assertThat(queenDTO1).isEqualTo(queenDTO2);
        queenDTO2.setId(2L);
        assertThat(queenDTO1).isNotEqualTo(queenDTO2);
        queenDTO1.setId(null);
        assertThat(queenDTO1).isNotEqualTo(queenDTO2);
    }
}
