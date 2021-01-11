package com.example.demo.payload.response;

import com.example.demo.model.CapacitorType;
import com.example.demo.model.CapacitorUnit;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
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


    public CapacitorTypeResponse(CapacitorType capacitorType) {
        CapacitorType ct = capacitorType;
        setTypeName(ct.getTypeName());
        setConstructionName(ct.getConstruction().getConstructionName());
        setStartYear(ct.getStartYear());
        setEndYear(ct.getEndYear());
        setDescription(ct.getDescription());
        setCompanyName(ct.getManufacturer().getCompanyName());
        setCapacitorUnitIds(ct.getCapacitorUnits().stream().map(CapacitorUnit::getId).collect(Collectors.toList()));
    }

    public CapacitorTypeResponse() {}



}
