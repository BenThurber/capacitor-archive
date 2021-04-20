package com.example.demo.payload.response;

import com.example.demo.model.CapacitorUnit;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
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

    @JsonProperty("length")
    private String length;

    @JsonProperty("diameter")
    private String diameter;

    @JsonProperty("mountingHoleDiameter")
    private String mountingHoleDiameter;

    @JsonProperty("thickness")
    private String thickness;

    @JsonProperty("typeName")
    private String typeName;

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("photos")
    private List<PhotoResponse> photos = new ArrayList<>();


    public CapacitorUnitResponse(CapacitorUnit capacitorUnit) {
        CapacitorUnit cu = capacitorUnit;

        setCapacitance(cu.getCapacitance());
        setVoltage(cu.getVoltage());
        setIdentifier(cu.getIdentifier());
        setValue(cu.getValue());
        setNotes(cu.getNotes());

        setLength(cu.getLength());
        setDiameter(cu.getDiameter());
        setMountingHoleDiameter(cu.getMountingHoleDiameter());
        setThickness(cu.getThickness());

        setTypeName(cu.getCapacitorType().getTypeName());
        setCompanyName(cu.getCapacitorType().getManufacturer().getCompanyName());

        // Convert Photo List to PhotoResponse list
        setPhotos(cu.getPhotos().stream().map(PhotoResponse::new).collect(Collectors.toList()));
    }

    public CapacitorUnitResponse() {}
}
