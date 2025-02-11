package rs.hexatech.beeback.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rs.hexatech.beeback.domain.HiveTestSamples.*;
import static rs.hexatech.beeback.domain.NoteTestSamples.*;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class NoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Note.class);
        Note note1 = getNoteSample1();
        Note note2 = new Note();
        assertThat(note1).isNotEqualTo(note2);

        note2.setId(note1.getId());
        assertThat(note1).isEqualTo(note2);

        note2 = getNoteSample2();
        assertThat(note1).isNotEqualTo(note2);
    }

    @Test
    void hiveTest() {
        Note note = getNoteRandomSampleGenerator();
        Hive hiveBack = getHiveRandomSampleGenerator();

        note.setHive(hiveBack);
        assertThat(note.getHive()).isEqualTo(hiveBack);

        note.hive(null);
        assertThat(note.getHive()).isNull();
    }
}
