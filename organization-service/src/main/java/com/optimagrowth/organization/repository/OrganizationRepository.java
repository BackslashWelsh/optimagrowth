package com.optimagrowth.organization.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.optimagrowth.organization.model.Organization;

public interface OrganizationRepository extends CrudRepository<Organization, String> {
}
