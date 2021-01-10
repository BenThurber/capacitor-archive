package com.example.demo.payload.response;

import com.example.demo.model.CapacitorType;
import com.example.demo.model.Manufacturer;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ManufacturerResponse {

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("openYear")
    private Short openYear;

    @JsonProperty("closeYear")
    private Short closeYear;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("typeNames")
    private List<String> typeNames;


    public ManufacturerResponse(Manufacturer manufacturer) {
        Manufacturer m = manufacturer;

        setCompanyName(m.getCompanyName());
        setOpenYear(m.getOpenYear());
        setCloseYear(m.getCloseYear());
        setSummary(m.getSummary());
        setTypeNames(m.getCapacitorTypes().stream().map(CapacitorType::getTypeName).collect(Collectors.toList()));
    }

    public ManufacturerResponse() { }


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

    public List<String> getTypeNames() {
        return typeNames;
    }

    public void setTypeNames(List<String> typeNames) {
        this.typeNames = typeNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManufacturerResponse that = (ManufacturerResponse) o;
        return Objects.equals(companyName, that.companyName) &&
                Objects.equals(openYear, that.openYear) &&
                Objects.equals(closeYear, that.closeYear) &&
                Objects.equals(summary, that.summary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, openYear, closeYear, summary);
    }
}
