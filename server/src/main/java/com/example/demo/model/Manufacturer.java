package com.example.demo.model;

import com.example.demo.payload.request.ManufacturerCreateRequest;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
public class Manufacturer {

    private final static int FIELD_LEN = 60;
    private final static int SUMMARY_LEN = 5000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "company_name", length = FIELD_LEN, nullable = false, unique = true)
    private String companyName;

    @Column(name = "open_year", columnDefinition = "SMALLINT")
    @Type(type = "org.hibernate.type.ShortType")
    private Short openYear;

    @Column(name = "close_year", columnDefinition = "SMALLINT")
    @Type(type = "org.hibernate.type.ShortType")
    private Short closeYear;

    @Column(name = "summary", length = SUMMARY_LEN)
    private String summary;


    public Manufacturer(ManufacturerCreateRequest manufacturerCreateRequest) {
        ManufacturerCreateRequest r = manufacturerCreateRequest;

        setCompanyName(r.getCompanyName());
        setOpenYear(r.getOpenYear());
        setCloseYear(r.getCloseYear());
        setBio(r.getBio());
    }

    public Manufacturer() { }


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

    public String getBio() {
        return summary;
    }

    public void setBio(String bio) {
        this.summary = bio;
    }
}
