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
        message = "closeYear is before openYear"
)
@Getter
@Setter
@EqualsAndHashCode
public class ManufacturerRequest {

    @NotNull(message = "Manufacturer is missing a name")
    @Size(min=1, message="Manufacturer is missing a name")
    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("country")
    private String country;

    @Max(value = 2050, message = "openYear is greater than 2050")
    @Min(value = 1000, message = "openYear is less than 1000")
    @JsonProperty("openYear")
    private Short openYear;

    @Max(value = 2050, message = "closeYear is greater than 2050")
    @Min(value = 1000, message = "closeYear is less than 1000")
    @JsonProperty("closeYear")
    private Short closeYear;

    @JsonProperty("summary")
    private String summary;


    public ManufacturerRequest() { }




}
