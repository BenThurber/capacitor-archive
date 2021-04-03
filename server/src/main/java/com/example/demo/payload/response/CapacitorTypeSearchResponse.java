package com.example.demo.payload.response;

import com.example.demo.model.CapacitorType;
import com.example.demo.model.CapacitorUnit;
import com.example.demo.utility.QuickSelect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
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
        } else {
            setNumberOfUnits(capacitorUnits.size());
            setLowestCapacitance(capacitorUnits.stream().min(Comparator.comparing(CapacitorUnit::getCapacitance)).get().getCapacitance());
            setHighestCapacitance(capacitorUnits.stream().max(Comparator.comparing(CapacitorUnit::getCapacitance)).get().getCapacitance());
        }

        setThumbnailUrl(capacitorUnits);
    }

    public CapacitorTypeSearchResponse() {}


    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setThumbnailUrl(CapacitorType capacitorType) {
        setThumbnailUrl(calculateThumbnailUrl(capacitorType.getCapacitorUnits()));
    }

    public void setThumbnailUrl(Collection<CapacitorUnit> capacitorUnits) {
        setThumbnailUrl(calculateThumbnailUrl(capacitorUnits));
    }

    /**
     * Get a single thumbnail URL a given A list of CapacitorUnits.  Efficiently sorts all CapacitorUnits from
     * and chooses the CapacitorUnit in the middle to take the thumbnail from.  Takes the thumbnail from the
     * primary Photo (Photo with the lowest order property).
     * @param capacitorUnits a List of capacitor units to choose from.
     * @return a url string of a thumbnail
     */
    private String calculateThumbnailUrl(Collection<CapacitorUnit> capacitorUnits) {
        if (capacitorUnits == null || capacitorUnits.size() <= 0) {
            return null;
        }

        List<CapacitorUnit> capacitorUnitsWithPhotos = capacitorUnits
                .stream()
                .filter(cu -> cu.getPhotos() != null && cu.getPhotos().size() > 0)
                .collect(Collectors.toList());

        if (capacitorUnitsWithPhotos.size() <= 0) {
            return null;
        }

        // Get the median capacitor unit "In the middle" of all the sorted units
        CapacitorUnit medianCapacitorUnit = QuickSelect.select(capacitorUnitsWithPhotos.toArray(new CapacitorUnit[0]), capacitorUnitsWithPhotos.size() / 2);
        if (medianCapacitorUnit == null || medianCapacitorUnit.getPrimaryThumbnail() == null) {
            return null;
        }
        return medianCapacitorUnit.getPrimaryThumbnail().getUrl();
    }


}
