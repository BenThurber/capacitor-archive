package com.example.demo.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CapacitorUnitRequest {
    
    @NotNull(message = "CapacitorUnit is missing a capacitance value")
    @Min(value = 0, message = "CapacitorUnit capacitance must be non-negative")
    @JsonProperty("capacitance")
    private Long capacitance;

    @Min(value = 0, message = "CapacitorUnit voltage must be non-negative")
    @JsonProperty("voltage")
    private Integer voltage;

    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("notes")
    private String notes;

    @NotNull(message = "CapacitorUnit must be associated with a CapacitorType.  No typeName is given.")
    @JsonProperty("typeName")
    private String typeName;


    CapacitorUnitRequest() {}


    public Long getCapacitance() {
        return capacitance;
    }

    public void setCapacitance(Long capacitance) {
        this.capacitance = capacitance;
    }

    public Integer getVoltage() {
        return voltage;
    }

    public void setVoltage(Integer voltage) {
        this.voltage = voltage;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
