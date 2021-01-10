package com.example.demo.payload.response;

import com.example.demo.model.CapacitorUnit;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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

    @JsonProperty("notes")
    private String notes;

    @JsonProperty("typeName")
    private String typeName;

    @JsonProperty("companyName")
    private String companyName;


    CapacitorUnitResponse(CapacitorUnit capacitorUnit) {
        CapacitorUnit cu = capacitorUnit;

        setCapacitance(cu.getCapacitance());
        setVoltage(cu.getVoltage());
        setIdentifier(cu.getIdentifier());
        setNotes(cu.getNotes());
        setTypeName(cu.getCapacitorType().getTypeName());
        setCompanyName(cu.getCapacitorType().getManufacturer().getCompanyName());
    }

    CapacitorUnitResponse() {}
}
