package com.capacitorarchive.payload.response;

import com.capacitorarchive.model.CapacitorType;
import com.capacitorarchive.model.CapacitorUnit;
import com.capacitorarchive.model.Manufacturer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManufacturerListItemResponseTest {

    private CapacitorUnit cu1;
    private CapacitorUnit cu2;
    private CapacitorUnit cu3;

    private CapacitorType ct1;
    private CapacitorType ct2;

    private Manufacturer manufacturer1;


    @BeforeEach
    void setup() {
        cu1 = new CapacitorUnit();
        cu2 = new CapacitorUnit();
        cu3 = new CapacitorUnit();

        ct1 = new CapacitorType();
        ct1.setCapacitorUnits(Arrays.asList(cu1, cu2, cu3));
        ct2 = new CapacitorType();
        ct2.setCapacitorUnits(Arrays.asList(cu1, cu2));

        manufacturer1 = new Manufacturer();
        manufacturer1.setCompanyName("Solar");
        manufacturer1.setOpenYear((short)1909);
        manufacturer1.setCloseYear((short)1948);
        manufacturer1.setCapacitorTypes(Arrays.asList(ct1, ct2));
    }

    @Test
    void testResponseParameters() {
        ManufacturerListItemResponse response = new ManufacturerListItemResponse(this.manufacturer1);

        assertEquals("Solar", response.getCompanyName());
        assertEquals((short)1909, response.getOpenYear());
        assertEquals(2, response.getNumCapacitorTypes());
        assertEquals(5, response.getNumCapacitorUnits());
    }


}
