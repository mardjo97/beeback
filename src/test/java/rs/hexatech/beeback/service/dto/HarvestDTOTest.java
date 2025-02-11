package rs.hexatech.beeback.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class HarvestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HarvestDTO.class);
        HarvestDTO harvestDTO1 = new HarvestDTO();
        harvestDTO1.setId(1L);
        HarvestDTO harvestDTO2 = new HarvestDTO();
        assertThat(harvestDTO1).isNotEqualTo(harvestDTO2);
        harvestDTO2.setId(harvestDTO1.getId());
        assertThat(harvestDTO1).isEqualTo(harvestDTO2);
        harvestDTO2.setId(2L);
        assertThat(harvestDTO1).isNotEqualTo(harvestDTO2);
        harvestDTO1.setId(null);
        assertThat(harvestDTO1).isNotEqualTo(harvestDTO2);
    }
}
