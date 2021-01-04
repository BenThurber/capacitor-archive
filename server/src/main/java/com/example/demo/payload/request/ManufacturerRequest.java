package com.example.demo.payload.request;

import com.example.demo.annotation.NumbersInOrder;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;


@NumbersInOrder(
        firstNumber = "openYear",
        secondNumber = "closeYear",
        message = "closeYear is before openYear"
)
public class ManufacturerRequest {

    @NotNull(message = "Manufacturer is missing a name")
    @Size(min=1, message="Manufacturer is missing a name")
    @JsonProperty("companyName")
    private String companyName;

    @Max(value = 2025, message = "openYear is greater than 2025")
    @Min(value = 1000, message = "openYear is less than 1000")
    @JsonProperty("openYear")
    private Short openYear;

    @Max(value = 2025, message = "closeYear is greater than 2025")
    @Min(value = 1000, message = "closeYear is less than 1000")
    @JsonProperty("closeYear")
    private Short closeYear;

    @JsonProperty("summary")
    private String summary;


    public ManufacturerRequest() { }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Short getOpenYear() {
        return openYear;
    }

    public void setOpenYear(Short openYear) {
        this.openYear = openYear;
    }

    public Short getCloseYear() {
        return closeYear;
    }

    public void setCloseYear(Short closeYear) {
        this.closeYear = closeYear;
    }

    public String getBio() {
        return summary;
    }

    public void setBio(String bio) {
        this.summary = bio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManufacturerRequest that = (ManufacturerRequest) o;
        return Objects.equals(companyName, that.companyName) &&
                Objects.equals(openYear, that.openYear) &&
                Objects.equals(closeYear, that.closeYear) &&
                Objects.equals(summary, that.summary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, openYear, closeYear, summary);
    }
}
