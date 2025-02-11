package rs.hexatech.beeback.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rs.hexatech.beeback.domain.GroupTestSamples.*;

import org.junit.jupiter.api.Test;
import rs.hexatech.beeback.web.rest.TestUtil;

class GroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Group.class);
        Group group1 = getGroupSample1();
        Group group2 = new Group();
        assertThat(group1).isNotEqualTo(group2);

        group2.setId(group1.getId());
        assertThat(group1).isEqualTo(group2);

        group2 = getGroupSample2();
        assertThat(group1).isNotEqualTo(group2);
    }
}
