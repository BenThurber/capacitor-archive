package com.example.demo.payload.request;

import com.example.demo.annotation.NumbersInOrder;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NumbersInOrder(
        firstNumber = "openYear",
        secondNumber = "closeYear",
        message = "endYear is before startYear"
)
@Getter
@Setter
@EqualsAndHashCode
public class CapacitorTypeRequest {

    @NotNull(message = "CapacitorType is missing a name")
    @Size(min=1, message="CapacitorType is missing a name")
    @JsonProperty("typeName")
    private String typeName;

    @NotNull(message = "CapacitorType is missing a construction")
    @JsonProperty("construction")
    private String constructionName;

    @Max(value = 2025, message = "startYear is greater than 2025")
    @Min(value = 1000, message = "startYear is less than 1000")
    @JsonProperty("startYear")
    private Short startYear;

    @Max(value = 2025, message = "endYear is greater than 2025")
    @Min(value = 1000, message = "endYear is less than 1000")
    @JsonProperty("endYear")
    private Short endYear;

    @JsonProperty("description")
    private String description;

    @NotNull(message = "CapacitorType must be associated with a Manufacturer.  No companyName is given.")
    @JsonProperty("companyName")
    private String companyName;


    CapacitorTypeRequest() {}



}
