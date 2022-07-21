package com.optimagrowth.organization.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "organizations")
public class Organization {
    @Id
    @Column(name = "organization_id")
    private String id;
    private String name;
    private String contactName;
    private String contactEmail;
    private String contactPhone;

}
