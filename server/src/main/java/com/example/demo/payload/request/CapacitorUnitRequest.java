package com.example.demo.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CapacitorUnitRequest {
    
    @NotNull(message = "CapacitorUnit is missing a name")
    @Size(min=1, message="CapacitorUnit is missing a name")
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
