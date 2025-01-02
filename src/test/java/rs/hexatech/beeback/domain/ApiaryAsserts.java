package rs.hexatech.beeback.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiaryAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual   the actual entity
     */
    public static void assertApiaryAllPropertiesEquals(Apiary expected, Apiary actual) {
        assertApiaryAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual   the actual entity
     */
    public static void assertApiaryAllUpdatablePropertiesEquals(Apiary expected, Apiary actual) {
        assertApiaryUpdatableFieldsEquals(expected, actual);
        assertApiaryUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual   the actual entity
     */
    public static void assertApiaryUpdatableFieldsEquals(Apiary expected, Apiary actual) {
        assertThat(expected)
                .as("Verify Apiary relevant properties")
                .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
                .satisfies(e -> assertThat(e.getIdNumber()).as("check idNumber").isEqualTo(actual.getIdNumber()))
                .satisfies(e -> assertThat(e.getColor()).as("check color").isEqualTo(actual.getColor()))
                .satisfies(e -> assertThat(e.getLocation()).as("check location").isEqualTo(actual.getLocation()))
                .satisfies(e -> assertThat(e.getLatitude()).as("check latitude").isEqualTo(actual.getLatitude()))
                .satisfies(e -> assertThat(e.getLongitude()).as("check longitude").isEqualTo(actual.getLongitude()))
                .satisfies(e -> assertThat(e.getOrderNumber()).as("check orderNumber").isEqualTo(actual.getOrderNumber()))
                .satisfies(e -> assertThat(e.getHiveCount()).as("check hiveCount").isEqualTo(actual.getHiveCount()))
                .satisfies(e -> assertThat(e.getExternalId()).as("check externalId").isEqualTo(actual.getExternalId()))
                .satisfies(e -> assertThat(e.getDateCreated()).as("check dateCreated").isEqualTo(actual.getDateCreated()))
                .satisfies(e -> assertThat(e.getDateModified()).as("check dateModified").isEqualTo(actual.getDateModified()))
                .satisfies(e -> assertThat(e.getDateSynched()).as("check dateSynched").isEqualTo(actual.getDateSynched()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual   the actual entity
     */
    public static void assertApiaryUpdatableRelationshipsEquals(Apiary expected, Apiary actual) {
        // empty method
    }
}
