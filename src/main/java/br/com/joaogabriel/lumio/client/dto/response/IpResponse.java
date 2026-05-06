package br.com.joaogabriel.lumio.client.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IpResponse(
        @JsonProperty("query")
        String ip,
        String status,
        String continent,
        String continentCode,
        String country,
        String countryCode,
        String region,
        String regionName,
        String city,
        @JsonProperty("zip")
        String postalCode,
        @JsonProperty("lat")
        double latitude,
        @JsonProperty("lon")
        double longitude,
        String timezone,
        @JsonProperty("offset")
        int utcOffset,
        String isp,
        @JsonProperty("org")
        String organization,
        @JsonProperty("as")
        String autonomousSystem,
        String autonomousSystemName,
        boolean mobile,
        boolean proxy,
        boolean hosting
) {
}
