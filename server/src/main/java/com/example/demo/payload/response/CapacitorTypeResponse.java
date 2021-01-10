package com.example.demo.payload.response;

import com.example.demo.model.CapacitorType;
import com.example.demo.model.CapacitorUnit;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CapacitorTypeResponse {

    @JsonProperty("typeName")
    private String typeName;

    @JsonProperty("constructionName")
    private String constructionName;

    @JsonProperty("startYear")
    private Short startYear;

    @JsonProperty("endYear")
    private Short endYear;

    @JsonProperty("description")
    private String description;

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("capacitorUnitIds")
    private List<Long> capacitorUnitIds;


    CapacitorTypeResponse(CapacitorType capacitorType) {
        CapacitorType ct = capacitorType;
        setTypeName(ct.getTypeName());
        setConstructionName(ct.getConstruction().getConstructionName());
        setStartYear(ct.getStartYear());
        setEndYear(ct.getEndYear());
        setDescription(ct.getDescription());
        setCompanyName(ct.getManufacturer().getCompanyName());
        setCapacitorUnitIds(ct.getCapacitorUnits().stream().map(CapacitorUnit::getId).collect(Collectors.toList()));
    }

    CapacitorTypeResponse() {}


    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getConstructionName() {
        return constructionName;
    }

    public void setConstructionName(String constructionName) {
        this.constructionName = constructionName;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<Long> getCapacitorUnitIds() {
        return capacitorUnitIds;
    }

    public void setCapacitorUnitIds(List<Long> capacitorUnitIds) {
        this.capacitorUnitIds = capacitorUnitIds;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CapacitorTypeResponse that = (CapacitorTypeResponse) o;
        return typeName.equals(that.typeName) &&
                Objects.equals(constructionName, that.constructionName) &&
                Objects.equals(startYear, that.startYear) &&
                Objects.equals(endYear, that.endYear) &&
                Objects.equals(description, that.description) &&
                Objects.equals(companyName, that.companyName) &&
                Objects.equals(capacitorUnitIds, that.capacitorUnitIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, constructionName, startYear, endYear, description, companyName, capacitorUnitIds);
    }
}
