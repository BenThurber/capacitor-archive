package com.example.demo.payload.response;

import com.example.demo.model.CapacitorType;
import com.example.demo.model.CapacitorUnit;
import com.example.demo.utility.QuickSelect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
public class CapacitorTypeSearchResponse {

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

    //-------------------------

    @JsonProperty("numberOfUnits")
    private Integer numberOfUnits;

    @JsonProperty("lowestCapacitance")
    private Long lowestCapacitance;

    @JsonProperty("highestCapacitance")
    private Long highestCapacitance;

    @JsonProperty("thumbnailUrl")
    private String thumbnailUrl;


    public CapacitorTypeSearchResponse(CapacitorType capacitorType) {
        CapacitorType ct = capacitorType;
        setTypeName(ct.getTypeName());
        setConstructionName(ct.getConstruction().getConstructionName());
        setStartYear(ct.getStartYear());
        setEndYear(ct.getEndYear());
        setDescription(ct.getDescription());
        setCompanyName(ct.getManufacturer().getCompanyName());

        List<CapacitorUnit> capacitorUnits = ct.getCapacitorUnits();
        if (capacitorUnits == null || capacitorUnits.size() <= 0) {
            setNumberOfUnits(0);
            return;
        } else {
            setNumberOfUnits(capacitorUnits.size());
            setLowestCapacitance(capacitorUnits.stream().min(Comparator.comparing(CapacitorUnit::getCapacitance)).get().getCapacitance());
            setHighestCapacitance(capacitorUnits.stream().max(Comparator.comparing(CapacitorUnit::getCapacitance)).get().getCapacitance());
        }

        List<CapacitorUnit> capacitorUnitsWithPhotos = capacitorUnits
                .stream()
                .filter(cu -> cu.getPhotos() != null && cu.getPhotos().size() > 0)
                .collect(Collectors.toList());

        if (capacitorUnitsWithPhotos.size() <= 0) {
            return;
        }

        // Get the capacitor unit "In the middle" of all the units
        System.out.println("CUs w/photos: " + capacitorUnitsWithPhotos.stream().map(cu -> cu.getPhotos().iterator().next()).collect(Collectors.toList()));
        CapacitorUnit medianCapacitorUnit = QuickSelect.select(capacitorUnitsWithPhotos.toArray(new CapacitorUnit[0]), capacitorUnitsWithPhotos.size() / 2);
        setThumbnailUrl(medianCapacitorUnit == null ? null : medianCapacitorUnit.getPrimaryThumbnail().getUrl());
    }

    public CapacitorTypeSearchResponse() {}



}
