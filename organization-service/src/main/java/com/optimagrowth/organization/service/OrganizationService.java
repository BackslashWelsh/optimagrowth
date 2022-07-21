package com.optimagrowth.organization.service;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.repository.OrganizationRepository;

@RequiredArgsConstructor
@Service
public class OrganizationService {

    private final OrganizationRepository repository;

    public Organization findById(String organizationId) {
        return repository.findById(organizationId).orElse(null);
    }

    public Organization create(Organization organization) {
        organization.setId(UUID.randomUUID().toString());
        organization = repository.save(organization);
        return organization;

    }

    public void update(Organization organization) {
        repository.save(organization);
    }

    public void delete(Organization organization) {
        repository.deleteById(organization.getId());
    }
}