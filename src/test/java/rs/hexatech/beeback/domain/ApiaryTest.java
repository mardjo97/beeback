package rs.hexatech.beeback.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rs.hexatech.beeback.domain.ApiaryTestSamples.*;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class ApiaryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Apiary.class);
        Apiary apiary1 = getApiarySample1();
        Apiary apiary2 = new Apiary();
        assertThat(apiary1).isNotEqualTo(apiary2);

        apiary2.setId(apiary1.getId());
        assertThat(apiary1).isEqualTo(apiary2);

        apiary2 = getApiarySample2();
        assertThat(apiary1).isNotEqualTo(apiary2);
    }
}
