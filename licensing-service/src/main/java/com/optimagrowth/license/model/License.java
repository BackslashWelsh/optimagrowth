package com.optimagrowth.license.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "licenses")
public class License extends RepresentationModel<License> {
    @Id
    private String licenseId;
    private String description;
    private String organizationId;
    private String productName;
    private String licenseType;
    private String comment;
    @Transient
    private String organizationName;
    @Transient
    private String contactName;
    @Transient
    private String contactPhone;
    @Transient
    private String contactEmail;

    public License withComment(String comment) {
        this.setComment(comment);
        return this;
    }
}
