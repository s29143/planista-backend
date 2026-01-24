package edu.pjatk.planista.integration.models;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gus.regon")
public record GusRegonProperties(
        String apiKey,
        Mode mode,
        String prodUrl,
        String testUrl
) {

    public enum Mode { PROD, TEST }

    public String serviceUrl() {
        return mode == Mode.PROD ? prodUrl : testUrl;
    }
}