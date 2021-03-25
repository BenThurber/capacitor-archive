package com.example.demo.payload.response;

import com.example.demo.model.CapacitorUnit;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
public class CapacitorUnitResponse {

    @JsonProperty("capacitance")
    private Long capacitance;

    @JsonProperty("voltage")
    private Integer voltage;

    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("value")
    private String value;

    @JsonProperty("notes")
    private String notes;

    @JsonProperty("typeName")
    private String typeName;

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("photos")
    private Set<PhotoResponse> photos = new HashSet<>();


    public CapacitorUnitResponse(CapacitorUnit capacitorUnit) {
        CapacitorUnit cu = capacitorUnit;

        setCapacitance(cu.getCapacitance());
        setVoltage(cu.getVoltage());
        setIdentifier(cu.getIdentifier());
        setValue(cu.getValue());
        setNotes(cu.getNotes());
        setTypeName(cu.getCapacitorType().getTypeName());
        setCompanyName(cu.getCapacitorType().getManufacturer().getCompanyName());

        // Convert Photo List to PhotoResponse list
        setPhotos(cu.getPhotos().stream().map(PhotoResponse::new).collect(Collectors.toSet()));
    }

    public CapacitorUnitResponse() {}
}
