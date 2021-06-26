package com.capacitorarchive.model;

import com.capacitorarchive.payload.request.ManufacturerRequest;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Manufacturer {

    private final static int FIELD_LEN = 60;


    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "company_name", length = FIELD_LEN, nullable = false)
    private String companyName;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "company_name_lower", length = FIELD_LEN, nullable = false, unique = true)
    private String companyNameLower;

    /**This allows company_name to be case insensitive unique*/
    @PrePersist
    @PreUpdate
    private void prepare() {
        this.companyNameLower = companyName == null ? null : companyName.toLowerCase();
    }

    @Column(name = "country", length = FIELD_LEN)
    private String country;

    @Column(name = "open_year")
    private Short openYear;

    @Column(name = "close_year")
    private Short closeYear;

    @Column(name = "summary")
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
        setCountry(r.getCountry());
        setOpenYear(r.getOpenYear());
        setCloseYear(r.getCloseYear());
        setSummary(r.getSummary());
    }

    public Manufacturer() { }


}
