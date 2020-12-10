package com.example.demo.payload.response;

import com.example.demo.model.Manufacturer;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ManufacturerResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("openYear")
    private Short openYear;

    @JsonProperty("closeYear")
    private Short closeYear;

    @JsonProperty("summary")
    private String summary;


    public ManufacturerResponse(Manufacturer manufacturer) {
        Manufacturer m = manufacturer;

        setId(m.getId());
        setCompanyName(m.getCompanyName());
        setOpenYear(m.getOpenYear());
        setCloseYear(m.getCloseYear());
        setSummary(m.getSummary());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
