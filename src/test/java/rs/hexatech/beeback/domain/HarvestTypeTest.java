package rs.hexatech.beeback.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rs.hexatech.beeback.domain.HarvestTypeTestSamples.*;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class HarvestTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HarvestType.class);
        HarvestType harvestType1 = getHarvestTypeSample1();
        HarvestType harvestType2 = new HarvestType();
        assertThat(harvestType1).isNotEqualTo(harvestType2);

        harvestType2.setId(harvestType1.getId());
        assertThat(harvestType1).isEqualTo(harvestType2);

        harvestType2 = getHarvestTypeSample2();
        assertThat(harvestType1).isNotEqualTo(harvestType2);
    }
}
