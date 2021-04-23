package com.example.demo.payload.response;

import com.example.demo.model.CapacitorType;
import com.example.demo.model.Manufacturer;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
public class ManufacturerResponse {

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("country")
    private String country;

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
        setCountry(m.getCountry());
        setOpenYear(m.getOpenYear());
        setCloseYear(m.getCloseYear());
        setSummary(m.getSummary());
        setTypeNames(m.getCapacitorTypes().stream().map(CapacitorType::getTypeName).collect(Collectors.toList()));
    }

    public ManufacturerResponse() { }



}
