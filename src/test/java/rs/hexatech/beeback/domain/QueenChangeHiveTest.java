package rs.hexatech.beeback.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rs.hexatech.beeback.domain.QueenChangeHiveTestSamples.*;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class QueenChangeHiveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QueenChangeHive.class);
        QueenChangeHive queenChangeHive1 = getQueenChangeHiveSample1();
        QueenChangeHive queenChangeHive2 = new QueenChangeHive();
        assertThat(queenChangeHive1).isNotEqualTo(queenChangeHive2);

        queenChangeHive2.setId(queenChangeHive1.getId());
        assertThat(queenChangeHive1).isEqualTo(queenChangeHive2);

        queenChangeHive2 = getQueenChangeHiveSample2();
        assertThat(queenChangeHive1).isNotEqualTo(queenChangeHive2);
    }
}
