package com.example.demo.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
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

    @NotNull(message = "CapacitorUnit must be associated with a Manufacturer.  No companyName is given.")
    @JsonProperty("companyName")
    private String companyName;


    CapacitorUnitRequest() {}



}
