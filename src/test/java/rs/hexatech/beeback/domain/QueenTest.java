package rs.hexatech.beeback.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rs.hexatech.beeback.domain.HiveTestSamples.*;
import static rs.hexatech.beeback.domain.QueenTestSamples.*;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class QueenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Queen.class);
        Queen queen1 = getQueenSample1();
        Queen queen2 = new Queen();
        assertThat(queen1).isNotEqualTo(queen2);

        queen2.setId(queen1.getId());
        assertThat(queen1).isEqualTo(queen2);

        queen2 = getQueenSample2();
        assertThat(queen1).isNotEqualTo(queen2);
    }

    @Test
    void hiveTest() {
        Queen queen = getQueenRandomSampleGenerator();
        Hive hiveBack = getHiveRandomSampleGenerator();

        queen.setHive(hiveBack);
        assertThat(queen.getHive()).isEqualTo(hiveBack);

        queen.hive(null);
        assertThat(queen.getHive()).isNull();
    }
}
