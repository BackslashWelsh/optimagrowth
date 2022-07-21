package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class OrganizationDiscoveryClient {
    private final DiscoveryClient discoveryClient;

    public Organization getOrganization(String organizationId) {
        final var instances = discoveryClient.getInstances("organization-service");
        if (instances.isEmpty()) {
            return null;
        }

        var serviceUri = String.format("%s/v1/organization/%s", instances.get(0).getUri(), organizationId);
        return new RestTemplate().exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, Organization.class, organizationId)
                .getBody();
    }

}
