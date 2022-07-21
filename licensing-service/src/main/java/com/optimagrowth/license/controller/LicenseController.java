package com.optimagrowth.license.controller;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;
import com.optimagrowth.license.service.utils.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/organization/{organizationId}/license")
public class LicenseController {
    private final LicenseService licenseService;

    @GetMapping("/{licenseId}")
    public License getLicense(@PathVariable String organizationId, @PathVariable String licenseId) {
        final var license = licenseService.getLicense(organizationId, licenseId, "");
        license.add(
                linkTo(methodOn(LicenseController.class).getLicense(organizationId, license.getLicenseId())).withSelfRel(),
                linkTo(methodOn(LicenseController.class).createLicense(license)).withRel("createLicense"),
                linkTo(methodOn(LicenseController.class).updateLicense(license)).withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class).deleteLicense(license.getLicenseId())).withRel("deleteLicense")
        );
        return license;
    }

    @GetMapping("/{licenseId}/{clientType}")
    public License getLicensesWithClient(
            @PathVariable String organizationId,
            @PathVariable String licenseId,
            @PathVariable String clientType
    ) {
        return licenseService.getLicense(organizationId, licenseId, clientType);
    }

    @PutMapping
    public License updateLicense(@RequestBody License request) {
        return licenseService.updateLicense(request);
    }

    @PostMapping
    public License createLicense(@RequestBody License request) {
        return licenseService.createLicense(request);
    }

    @DeleteMapping("/{licenseId}")
    public CharSequence deleteLicense(@PathVariable("licenseId") String licenseId) {
        return licenseService.deleteLicense(licenseId);
    }

    @GetMapping
    public List<License> getLicenses(@PathVariable("organizationId") String organizationId) throws TimeoutException {
        log.debug("LicenseServiceController Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        return licenseService.getLicensesByOrganization(organizationId);
    }

}
