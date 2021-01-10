package com.example.demo.model;

import com.example.demo.payload.request.ManufacturerRequest;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Manufacturer {

    private final static int FIELD_LEN = 60;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "company_name", length = FIELD_LEN, nullable = false)
    private String companyName;

    @Column(name = "company_name_lower", length = FIELD_LEN, nullable = false, unique = true)
    private String companyNameLower;

    /**This allows company_name to be case insensitive unique*/
    @PrePersist
    @PreUpdate
    private void prepare() {
        this.companyNameLower = companyName == null ? null : companyName.toLowerCase();
    }

    @Column(name = "open_year", columnDefinition = "SMALLINT")
    @Type(type = "org.hibernate.type.ShortType")
    private Short openYear;

    @Column(name = "close_year", columnDefinition = "SMALLINT")
    @Type(type = "org.hibernate.type.ShortType")
    private Short closeYear;

    @Column(name = "summary", columnDefinition="MEDIUMTEXT")
    private String summary;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "manufacturer", cascade = CascadeType.ALL)
    private List<CapacitorType> capacitorTypes = new ArrayList<>();


    public Manufacturer(ManufacturerRequest manufacturerRequest) {
        this.edit(manufacturerRequest);
    }

    /**
     * Edit the fields of a Manufacturer but not the id.
     * @param manufacturerRequest a request from the client containing the fields for the manufacturer
     */
    public void edit(ManufacturerRequest manufacturerRequest) {
        ManufacturerRequest r = manufacturerRequest;

        setCompanyName(r.getCompanyName());
        setOpenYear(r.getOpenYear());
        setCloseYear(r.getCloseYear());
        setSummary(r.getBio());
    }

    public Manufacturer() { }

    public Long getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Short getOpenYear() {
        return openYear;
    }

    public void setOpenYear(Short openYear) {
        this.openYear = openYear;
    }

    public Short getCloseYear() {
        return closeYear;
    }

    public void setCloseYear(Short closeYear) {
        this.closeYear = closeYear;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manufacturer that = (Manufacturer) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(companyName, that.companyName) &&
                Objects.equals(companyNameLower, that.companyNameLower) &&
                Objects.equals(openYear, that.openYear) &&
                Objects.equals(closeYear, that.closeYear) &&
                Objects.equals(summary, that.summary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyName, companyNameLower, openYear, closeYear, summary);
    }
}
