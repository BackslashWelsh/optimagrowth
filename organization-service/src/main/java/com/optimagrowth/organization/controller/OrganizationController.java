package com.optimagrowth.organization.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.service.OrganizationService;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/organization")
public class OrganizationController {
    private final OrganizationService service;

    @GetMapping("/{organizationId}")
    public Organization getOrganization( @PathVariable String organizationId) {
        return service.findById(organizationId);
    }

    @PutMapping
    public void updateOrganization(@RequestBody Organization organization) {
        service.update(organization);
    }

    @PostMapping
    public Organization  saveOrganization(@RequestBody Organization organization) {
    	return service.create(organization);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(@RequestBody Organization organization) {
        service.delete(organization);
    }

}
