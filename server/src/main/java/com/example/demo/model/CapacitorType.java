package com.example.demo.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class CapacitorType {

    private final static int FIELD_LEN = 60;
    private static final int DESCRIPTION_LEN = 5000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type_name", length = FIELD_LEN, nullable = false)
    private String typeName;

    @Column(name = "type_name_lower", length = FIELD_LEN, nullable = false, unique = true)
    private String typeNameLower;

    /**This allows type_name to be case insensitive unique*/
    @PrePersist
    @PreUpdate
    private void prepare() {
        this.typeNameLower = typeName == null ? null : typeName.toLowerCase();
    }

    @Column(name = "start_year", columnDefinition = "SMALLINT")
    @Type(type = "org.hibernate.type.ShortType")
    private Short startYear;

    @Column(name = "end_year", columnDefinition = "SMALLINT")
    @Type(type = "org.hibernate.type.ShortType")
    private Short endYear;

    @Column(name = "description", length = DESCRIPTION_LEN)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "construction_name", nullable = false)
    private Construction construction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_name", nullable = false)
    private Manufacturer manufacturer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "capacitorType", cascade = CascadeType.ALL)
    private List<CapacitorUnit> capacitorUnits = new ArrayList<>();


    CapacitorType() {}


    public Long getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Short getStartYear() {
        return startYear;
    }

    public void setStartYear(Short startYear) {
        this.startYear = startYear;
    }

    public Short getEndYear() {
        return endYear;
    }

    public void setEndYear(Short endYear) {
        this.endYear = endYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Construction getConstruction() {
        return construction;
    }

    public void setConstruction(Construction construction) {
        this.construction = construction;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CapacitorType that = (CapacitorType) o;
        return id.equals(that.id) &&
                typeName.equals(that.typeName) &&
                typeNameLower.equals(that.typeNameLower) &&
                Objects.equals(startYear, that.startYear) &&
                Objects.equals(endYear, that.endYear) &&
                Objects.equals(description, that.description) &&
                construction.equals(that.construction) &&
                manufacturer.equals(that.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeName, typeNameLower, startYear, endYear, description, construction, manufacturer);
    }
}
