package com.psa.ranking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Psa Ranking.
 * <p>
 * Properties are configured in the {@code application.yml} file. See
 * {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    public String cronFixture;

    public String getCronFixture() {
        return cronFixture;
    }

    public void setCronFixture(String cronFixture) {
        this.cronFixture = cronFixture;
    }

    @Override
    public String toString() {
        return "ApplicationProperties [cronFixture=" + cronFixture + "]";
    }

}
