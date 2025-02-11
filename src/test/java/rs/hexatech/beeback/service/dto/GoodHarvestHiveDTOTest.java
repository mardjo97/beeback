package rs.hexatech.beeback.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class GoodHarvestHiveDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GoodHarvestHiveDTO.class);
        GoodHarvestHiveDTO goodHarvestHiveDTO1 = new GoodHarvestHiveDTO();
        goodHarvestHiveDTO1.setId(1L);
        GoodHarvestHiveDTO goodHarvestHiveDTO2 = new GoodHarvestHiveDTO();
        assertThat(goodHarvestHiveDTO1).isNotEqualTo(goodHarvestHiveDTO2);
        goodHarvestHiveDTO2.setId(goodHarvestHiveDTO1.getId());
        assertThat(goodHarvestHiveDTO1).isEqualTo(goodHarvestHiveDTO2);
        goodHarvestHiveDTO2.setId(2L);
        assertThat(goodHarvestHiveDTO1).isNotEqualTo(goodHarvestHiveDTO2);
        goodHarvestHiveDTO1.setId(null);
        assertThat(goodHarvestHiveDTO1).isNotEqualTo(goodHarvestHiveDTO2);
    }
}
