package com.capacitorarchive.payload.response;

import com.capacitorarchive.model.Manufacturer;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode
public class ManufacturerListItemResponse {


    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("numCapacitorTypes")
    private Integer numCapacitorTypes;

    @JsonProperty("numCapacitorUnits")
    private Integer numCapacitorUnits;

    @JsonProperty("openYear")
    private Short openYear;

    @JsonProperty("closeYear")
    private Short closeYear;


    public ManufacturerListItemResponse(Manufacturer manufacturer) {
        Manufacturer m = manufacturer;

        setCompanyName(m.getCompanyName());
        setNumCapacitorTypes(m.getCapacitorTypes().size());
        setOpenYear(m.getOpenYear());
        setCloseYear(m.getCloseYear());

        // Count all Capacitor Units
        setNumCapacitorUnits(m.getCapacitorTypes()
                .stream()
                .mapToInt(ct -> ct.getCapacitorUnits().size())
                .sum()
        );
    }

    public ManufacturerListItemResponse() {
    }


}
