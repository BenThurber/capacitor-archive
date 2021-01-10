package com.example.demo.payload.request;

import com.example.demo.annotation.NumbersInOrder;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NumbersInOrder(
        firstNumber = "openYear",
        secondNumber = "closeYear",
        message = "endYear is before startYear"
)
public class CapacitorTypeRequest {

    @NotNull(message = "CapacitorType is missing a name")
    @Size(min=1, message="CapacitorType is missing a name")
    @JsonProperty("typeName")
    private String typeName;

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

    @JsonProperty("companyName")
    private String companyName;


    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getConstructionName() {
        return constructionName;
    }

    public void setConstructionName(String constructionName) {
        this.constructionName = constructionName;
    }

    public Short getStartYear() {
        return startYear;
    }

    public void setStartYear(Short startYear) {
        this.startYear = startYear;
    }

    public Short getEndYear() {
        return endYear;
    }

    public void setEndYear(Short endYear) {
        this.endYear = endYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
