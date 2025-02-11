package rs.hexatech.beeback.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class ReproductionHiveDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReproductionHiveDTO.class);
        ReproductionHiveDTO reproductionHiveDTO1 = new ReproductionHiveDTO();
        reproductionHiveDTO1.setId(1L);
        ReproductionHiveDTO reproductionHiveDTO2 = new ReproductionHiveDTO();
        assertThat(reproductionHiveDTO1).isNotEqualTo(reproductionHiveDTO2);
        reproductionHiveDTO2.setId(reproductionHiveDTO1.getId());
        assertThat(reproductionHiveDTO1).isEqualTo(reproductionHiveDTO2);
        reproductionHiveDTO2.setId(2L);
        assertThat(reproductionHiveDTO1).isNotEqualTo(reproductionHiveDTO2);
        reproductionHiveDTO1.setId(null);
        assertThat(reproductionHiveDTO1).isNotEqualTo(reproductionHiveDTO2);
    }
}
