package rs.hexatech.beeback.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class HarvestTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HarvestTypeDTO.class);
        HarvestTypeDTO harvestTypeDTO1 = new HarvestTypeDTO();
        harvestTypeDTO1.setId(1L);
        HarvestTypeDTO harvestTypeDTO2 = new HarvestTypeDTO();
        assertThat(harvestTypeDTO1).isNotEqualTo(harvestTypeDTO2);
        harvestTypeDTO2.setId(harvestTypeDTO1.getId());
        assertThat(harvestTypeDTO1).isEqualTo(harvestTypeDTO2);
        harvestTypeDTO2.setId(2L);
        assertThat(harvestTypeDTO1).isNotEqualTo(harvestTypeDTO2);
        harvestTypeDTO1.setId(null);
        assertThat(harvestTypeDTO1).isNotEqualTo(harvestTypeDTO2);
    }
}
