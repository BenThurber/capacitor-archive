package com.capacitorarchive.payload.response;

import com.capacitorarchive.model.CapacitorType;
import com.capacitorarchive.model.CapacitorUnit;
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
    private List<String> capacitorUnitValues;


    public CapacitorTypeResponse(CapacitorType capacitorType) {
        CapacitorType ct = capacitorType;
        setTypeName(ct.getTypeName());
        setConstructionName(ct.getConstruction().getConstructionName());
        setStartYear(ct.getStartYear());
        setEndYear(ct.getEndYear());
        setDescription(ct.getDescription());
        setCompanyName(ct.getManufacturer().getCompanyName());
        setCapacitorUnitValues(ct.getCapacitorUnits().stream().map(CapacitorUnit::getValue).collect(Collectors.toList()));
    }

    public CapacitorTypeResponse() {}



}
