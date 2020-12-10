package com.example.demo.controller;

import com.example.demo.model.Manufacturer;
import com.example.demo.repository.ManufacturerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import utilities.JsonConverter;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManufacturerController.class)
class ManufacturerControllerTest {


    @Autowired
    private MockMvc mvc;
    @MockBean
    private ManufacturerRepository manufacturerRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Manufacturer> manufacturerMockTable = new ArrayList<>();

    @BeforeEach
    void mockRepository() {
        when(manufacturerRepository.save(Mockito.any(Manufacturer.class))).thenAnswer(i -> {
            Manufacturer newManufacturer = i.getArgument(0);
            ReflectionTestUtils.setField(newManufacturer, "id", (DEFAULT_MANUFACTURER_ID + manufacturerCount++));
            manufacturerMockTable.add(newManufacturer);
            return newManufacturer;
        });
    }

    private static Long manufacturerCount = 0L;
    private static final Long DEFAULT_MANUFACTURER_ID = 1L;


    // ----------- Tests -----------

    private final String newManufacturer1Json = JsonConverter.toJson(true,
            "companyName", "Hunts",
            "openYear", 1920,
            "closeYear", 1939,
            "summary", "Hunts wax paper capacitors today have a high failure rate compared to other manufacturers"
    );
    /**
     * Test successful creation of new manufacturer.
     */
    @Test
    void newManufacturer_success() throws Exception {

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/manufacturer/create")
                .content(newManufacturer1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isCreated());
    }


    private final String newManufacturerOnlyNameJson = JsonConverter.toJson(true,
            "companyName", "Hunts"
    );
    /**
     * Test successful creation of new manufacturer.
     */
    @Test
    void newManufacturer_onlyNameField_success() throws Exception {

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/manufacturer/create")
                .content(newManufacturerOnlyNameJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isCreated());
    }


    private final String manufacturerNoNameJson = JsonConverter.toJson(true,
            "openYear", 1918,
            "closeYear", 1939,
            "summary", "Hunts wax paper capacitors today have a high failure rate compared to other manufacturers"
    );
    /**
     * Test creation of new manufacturer without name fail.
     */
    @Test
    void newManufacturer_noName_fail() throws Exception {

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/manufacturer/create")
                .content(manufacturerNoNameJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isBadRequest());
    }


    private final String manufacturerDatesToLowJson = JsonConverter.toJson(true,
            "companyName", "Hunts",
            "openYear", 33,
            "closeYear", 67,
            "summary", "Hunts wax paper capacitors today have a high failure rate compared to other manufacturers"
    );
    /**
     * Test creation of new manufacturer with years before 1000.
     */
    @Test
    void newManufacturer_datesTooLow_fail() throws Exception {

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/manufacturer/create")
                .content(manufacturerDatesToLowJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isBadRequest());
    }

    private final String manufacturerDatesToHighJson = JsonConverter.toJson(true,
            "companyName", "Hunts",
            "openYear", 2099,
            "closeYear", 2101,
            "summary", "Hunts wax paper capacitors today have a high failure rate compared to other manufacturers"
    );
    /**
     * Test creation of new manufacturer with years after 2025.
     */
    @Test
    void newManufacturer_datesTooHigh_fail() throws Exception {

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/manufacturer/create")
                .content(manufacturerDatesToHighJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isBadRequest());
    }

    private final String manufacturerDatesInWrongOrder = JsonConverter.toJson(true,
            "companyName", "Hunts",
            "openYear", 1935,
            "closeYear", 1920,
            "summary", "Hunts wax paper capacitors today have a high failure rate compared to other manufacturers"
    );
    /**
     * Test creation of new manufacturer with years after 2025.
     */
    @Test
    void newManufacturer_datesWrongOrder_fail() throws Exception {

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/manufacturer/create")
                .content(manufacturerDatesToHighJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isBadRequest());
    }




}

