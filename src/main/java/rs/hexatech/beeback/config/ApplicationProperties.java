package rs.hexatech.beeback.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Beeback.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Liquibase liquibase = new Liquibase();
    private final Firebase firebase = new Firebase();
    private final ReminderScheduler reminderScheduler = new ReminderScheduler();

    // jhipster-needle-application-properties-property

    public Liquibase getLiquibase() {
        return liquibase;
    }

    public Firebase getFirebase() {
        return firebase;
    }

    public ReminderScheduler getReminderScheduler() {
        return reminderScheduler;
    }

    // jhipster-needle-application-properties-property-getter

    public static class Liquibase {

        private Boolean asyncStart;

        public Boolean getAsyncStart() {
            return asyncStart;
        }

        public void setAsyncStart(Boolean asyncStart) {
            this.asyncStart = asyncStart;
        }
    }

    public static class Firebase {

        private String credentialsPath = "";

        public String getCredentialsPath() {
            return credentialsPath;
        }

        public void setCredentialsPath(String credentialsPath) {
            this.credentialsPath = credentialsPath != null ? credentialsPath : "";
        }
    }

    public static class ReminderScheduler {

        private long intervalMs = 60000;

        public long getIntervalMs() {
            return intervalMs;
        }

        public void setIntervalMs(long intervalMs) {
            this.intervalMs = intervalMs;
        }
    }
    // jhipster-needle-application-properties-property-class
}
