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
    void newManufacturer() throws Exception {

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/manufacturer/create")
                .content(newManufacturer1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isCreated());
    }

}

