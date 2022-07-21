package com.optimagrowth.license.service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.LicenseRepository;
import com.optimagrowth.license.service.client.OrganizationDiscoveryClient;
import com.optimagrowth.license.service.client.OrganizationFeignClient;
import com.optimagrowth.license.service.client.OrganizationRestTemplateClient;
import com.optimagrowth.license.service.utils.UserContextHolder;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.bulkhead.annotation.Bulkhead.Type;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
@Service
public class LicenseService {
    private final MessageSource messages;
    private final ServiceConfig config;
    private final LicenseRepository licenseRepository;
    private final OrganizationRestTemplateClient restTemplateClient;
    private final OrganizationDiscoveryClient discoveryClient;
    private final OrganizationFeignClient feignClient;

    public License getLicense(String organizationId, String licenseId, final String clientType) {
        final var license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format(messages.getMessage("license.search"
                                + ".error.message", null, null), licenseId, organizationId)));

        ofNullable(retrieveOrganizationInfo(organizationId, clientType))
                .ifPresent(org -> {
                    license.setOrganizationName(org.getName());
                    license.setContactName(org.getContactName());
                    license.setContactEmail(org.getContactEmail());
                    license.setContactPhone(org.getContactPhone());
                });

        return license.withComment(config.getProperty());
    }

    private Organization retrieveOrganizationInfo(final String organizationId, final String clientType) {
        switch (clientType) {
            case "discovery":
                log.info("I am using the discovery client");
                return discoveryClient.getOrganization(organizationId);
            case "feign":
                log.info("I am using the feign client");
                return feignClient.getOrganization(organizationId);
            default:
                log.info("I am using the rest client");
                return restTemplateClient.getOrganization(organizationId);
        }
    }

    public License createLicense(License license) {
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);
        return license.withComment(config.getProperty());
    }

    public License updateLicense(License license) {
        licenseRepository.save(license);

        return license.withComment(config.getProperty());
    }

    public String deleteLicense(String licenseId) {
        String responseMessage = null;
        License license = new License();
        license.setLicenseId(licenseId);
        licenseRepository.delete(license);
        responseMessage = String.format(messages.getMessage("license.delete.message", null, null), licenseId);
        return responseMessage;
    }

    @CircuitBreaker(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
    @RateLimiter(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
    @Retry(name = "retryLicenseService", fallbackMethod = "buildFallbackLicenseList")
    @Bulkhead(name = "bulkheadLicenseService", type= Type.SEMAPHORE, fallbackMethod = "buildFallbackLicenseList")
    public List<License> getLicensesByOrganization(String organizationId) throws TimeoutException {
        log.debug("getLicensesByOrganization Correlation id: {}",
                UserContextHolder.getContext().getCorrelationId());
        randomlyRunLong();
        return licenseRepository.findByOrganizationId(organizationId);
    }

    @SuppressWarnings("unused")
    private List<License> buildFallbackLicenseList(String organizationId, Throwable t) {
        var fallbackList = new ArrayList<License>();
        License license = new License();
        license.setLicenseId("0000000-00-00000");
        license.setOrganizationId(organizationId);
        license.setProductName("Sorry no licensing information currently available");
        fallbackList.add(license);
        return fallbackList;
    }

    private void randomlyRunLong() throws TimeoutException {
        int randomNum = ThreadLocalRandom.current().nextInt((3 - 1) + 1) + 1;
        if (randomNum == 3) {
            sleep();
        }
    }

    private void sleep() throws TimeoutException {
        try {
            log.info("Sleep");
            Thread.sleep(1000);
            throw new java.util.concurrent.TimeoutException();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

}
