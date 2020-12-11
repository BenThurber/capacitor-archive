package com.example.demo.controller;

import com.example.demo.model.Manufacturer;
import com.example.demo.payload.response.ManufacturerResponse;
import com.example.demo.repository.ManufacturerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import utilities.JsonConverter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(ManufacturerController.class)
class ManufacturerControllerTest {


    @Autowired
    private MockMvc mvc;
    @MockBean
    private ManufacturerRepository manufacturerRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final List<Manufacturer> manufacturerMockTable = new ArrayList<>();

    private final Manufacturer manufacturer1 = new Manufacturer();


    @BeforeAll
    void initializeTestEntities() {
        manufacturer1.setCompanyName("Cornell Dubilier");
        manufacturer1.setOpenYear((short)1909);
        manufacturer1.setSummary("A company still in business");

    }


    @BeforeEach
    void mockRepository() {
        when(manufacturerRepository.save(Mockito.any(Manufacturer.class))).thenAnswer(i -> {
            Manufacturer newManufacturer = i.getArgument(0);
            ReflectionTestUtils.setField(newManufacturer, "id", (DEFAULT_MANUFACTURER_ID + manufacturerCount++));
            manufacturerMockTable.add(newManufacturer);
            return newManufacturer;
        });

        when(manufacturerRepository.findById(Mockito.any(Long.class))).thenAnswer(i -> {
            Long id = i.getArgument(0);
            return manufacturerMockTable.stream().filter(m -> id.equals(m.getId())).findFirst().orElse(null);
        });

        when(manufacturerRepository.findByCompanyNameLowerIgnoreCase(Mockito.any(String.class))).thenAnswer(i -> {
            String companyName = i.getArgument(0);
            return manufacturerMockTable.stream().filter(
                    m -> companyName.toLowerCase().equals(m.getCompanyName().toLowerCase())
            ).findFirst().orElse(null);
        });
    }

    @AfterEach
    void setup() {
        manufacturerMockTable.clear();
        manufacturerCount = 0L;
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



    /**
     * Test get manufacturer by id success.
     */
    @Test
    void getManufacturerById_success() throws Exception {
        manufacturerRepository.save(manufacturer1);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/manufacturer/id/1")
                .content(newManufacturer1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponseStr = result.getResponse().getContentAsString();
        ManufacturerResponse manufacturerReceived = objectMapper.readValue(jsonResponseStr, ManufacturerResponse.class);
        assertEquals(new ManufacturerResponse(manufacturer1), manufacturerReceived);
    }


    /**
     * Test no manufacturer with id fail.
     */
    @Test
    void getManufacturerById_doesNotExist_fail() throws Exception {
        manufacturerRepository.save(manufacturer1);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/manufacturer/id/3")
                .content(newManufacturer1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq).andExpect(status().isNotFound());
    }



    /**
     * Test get manufacturer by name success.
     */
    @Test
    void getManufacturerByName_success() throws Exception {
        manufacturerRepository.save(manufacturer1);
                                                                                    // Testing with mixed case
        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/manufacturer/name/corNell dubiLIer")
                .content(newManufacturer1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponseStr = result.getResponse().getContentAsString();
        ManufacturerResponse manufacturerReceived = objectMapper.readValue(jsonResponseStr, ManufacturerResponse.class);
        assertEquals(new ManufacturerResponse(manufacturer1), manufacturerReceived);
    }


    /**
     * Test no manufacturer with given name fail.
     */
    @Test
    void getManufacturerByName_doesNotExist_fail() throws Exception {
        manufacturerRepository.save(manufacturer1);
        // Test with mixed case
        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/manufacturer/name/vaught")
                .content(newManufacturer1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq).andExpect(status().isNotFound());
    }


}

