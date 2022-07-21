package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class OrganizationRestTemplateClient {
    private final RestTemplate restTemplate;

    public Organization getOrganization(String organizationId) {
        return restTemplate.exchange(
                        "http://organization-service/v1/organization/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId)
                .getBody();

    }
}
