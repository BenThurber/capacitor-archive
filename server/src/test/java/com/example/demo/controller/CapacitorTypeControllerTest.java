package com.example.demo.controller;

import com.example.demo.model.CapacitorType;
import com.example.demo.model.Construction;
import com.example.demo.model.Manufacturer;
import com.example.demo.repository.CapacitorTypeRepository;
import com.example.demo.repository.ConstructionRepository;
import com.example.demo.repository.ManufacturerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import testUtilities.JsonConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(CapacitorTypeController.class)
class CapacitorTypeControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private ManufacturerRepository manufacturerRepository;
    @MockBean
    private ConstructionRepository constructionRepository;
    @MockBean
    private CapacitorTypeRepository capacitorTypeRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final List<Manufacturer> manufacturerMockTable = new ArrayList<>();
    private final List<Construction> constructionMockTable = new ArrayList<>();
    private final List<CapacitorType> capacitorTypeMockTable = new ArrayList<>();

    private static Long manufacturerCount = 0L;
    private static final Long DEFAULT_MANUFACTURER_ID = 1L;
    private static Long capacitorTypeCount = 0L;
    private static final Long DEFAULT_CAPACITOR_ID = 1L;


    private Manufacturer manufacturer1;
    private Manufacturer manufacturer2;

    @BeforeEach
    void initializeTestEntities() {
        manufacturer1 = new Manufacturer();
        manufacturer1.setCompanyName("Cornell Dubilier");
        manufacturer1.setOpenYear((short)1909);
        manufacturer1.setSummary("A company still in business");

        manufacturer2 = new Manufacturer();
        manufacturer2.setCompanyName("Solar");
        manufacturer2.setOpenYear((short)1917);
        manufacturer2.setCloseYear((short)1948);
        manufacturer2.setSummary("A company that made high quality wax paper capacitors");

    }


    @BeforeEach
    void mockRepository() {

        //---- Manufacturer ----

        when(manufacturerRepository.save(Mockito.any(Manufacturer.class))).thenAnswer(i -> {
            Manufacturer manufacturer = i.getArgument(0);
            if (manufacturer.getId() == null) {
                ReflectionTestUtils.setField(manufacturer, "id", (DEFAULT_MANUFACTURER_ID + manufacturerCount++));
            }

            // Remove and replace existing entity
            Long id = manufacturer.getId();
            manufacturerMockTable.stream().filter(m -> id != null && id.equals(m.getId())).findFirst().ifPresent(manufacturerMockTable::remove);

            manufacturerMockTable.add(manufacturer);

            return manufacturer;
        });


        when(manufacturerRepository.findByCompanyNameLowerIgnoreCase(Mockito.any(String.class))).thenAnswer(i -> {
            String companyName = i.getArgument(0);
            return manufacturerMockTable.stream().filter(
                    m -> companyName.toLowerCase().equals(m.getCompanyName().toLowerCase())
            ).findFirst().orElse(null);
        });

        when(manufacturerRepository.getAllCompanyNames()).thenAnswer(i ->  manufacturerMockTable.stream()
                .map(Manufacturer::getCompanyName)
                .collect(Collectors.toList()));

        //---- Construction ----

        when(constructionRepository.save(Mockito.any(Construction.class))).thenAnswer(i -> {
            Construction construction = i.getArgument(0);

            // Remove and replace existing entity
            String constructionName = construction.getConstructionName();
            constructionMockTable.stream().filter(ct -> constructionName != null && constructionName.equals(ct.getConstructionName())).findFirst().ifPresent(constructionMockTable::remove);

            constructionMockTable.add(construction);
            return construction;
        });

        when(constructionRepository.findByConstructionName(Mockito.any(String.class))).thenAnswer(i -> {
            String constructionName = i.getArgument(0);
            return constructionMockTable.stream().filter(c -> c.getConstructionName().equals(constructionName)).findFirst().orElse(null);
        });

        //---- CapacitorType ----

        when(capacitorTypeRepository.save(Mockito.any(CapacitorType.class))).thenAnswer(i -> {
            CapacitorType capacitorType = i.getArgument(0);
            if (capacitorType.getId() == null) {
                ReflectionTestUtils.setField(capacitorType, "id", (DEFAULT_CAPACITOR_ID + capacitorTypeCount++));
            }

            // Remove and replace existing entity
            Long id = capacitorType.getId();
            capacitorTypeMockTable.stream().filter(ct -> id != null && id.equals(ct.getId())).findFirst().ifPresent(capacitorTypeMockTable::remove);

            capacitorTypeMockTable.add(capacitorType);

            return capacitorType;
        });

        when(capacitorTypeRepository.findByTypeNameLowerIgnoreCaseAndManufacturer(
                Mockito.any(String.class), Mockito.any(Manufacturer.class))).thenAnswer(i -> {
                    String typeName = i.getArgument(0);
                    Manufacturer manufacturer = i.getArgument(1);

                    List<CapacitorType> capacitorTypesInManufacturer = capacitorTypeMockTable.stream().filter(
                            ct -> ct.getManufacturer().getCompanyName().equals(manufacturer.getCompanyName())
                    ).collect(Collectors.toList());

                    return capacitorTypesInManufacturer.stream().filter(
                            ct -> ct.getTypeName().toLowerCase().equals(typeName.toLowerCase())
                    ).findFirst().orElse(null);
        });

    }


    @AfterEach
    void setup() {
        manufacturerMockTable.clear();
        manufacturerCount = 0L;
        constructionMockTable.clear();
        capacitorTypeMockTable.clear();
        capacitorTypeCount = 0L;
    }


    // ----------- Tests -----------

    private final String newCapacitorType1Json = JsonConverter.toJson(true,
            "typeName", "Sealdtite",
            "constructionName", "wax-paper",
            "startYear", 1934,
            "endYear", 1939,
            "description", "Hermetically sealed",
            "companyName", "Solar"
    );
    /**
     * Test successful creation of new CapacitorType that creates a new construction.
     */
    @Test
    void newCapacitorType_newConstruction_success() throws Exception {
        manufacturerRepository.save(manufacturer2);

        assertEquals(0, constructionMockTable.size());

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/type/create")
                .content(newCapacitorType1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isCreated());

        assertEquals(new Construction("Wax-Paper"), constructionMockTable.get(0));
    }


    /**
     * Test successful creation of new CapacitorType with existing construction.
     */
    @Test
    void newCapacitorType_existingConstruction_success() throws Exception {
        manufacturerRepository.save(manufacturer2);
        constructionRepository.save(new Construction("wAx-pApEr"));

        assertEquals(new Construction("Wax-Paper"), constructionMockTable.get(0));

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/type/create")
                .content(newCapacitorType1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isCreated());

        assertEquals(1, constructionMockTable.size());
    }


    /**
     * Test unsuccessful creation of new CapacitorType when the Manufacturer it references doesn't exist.
     */
    @Test
    void newCapacitorType_noExistingManufacturer_fail() throws Exception {

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/type/create")
                .content(newCapacitorType1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isBadRequest()).andReturn();

        assertTrue(result.getResolvedException().toString().contains("The CapacitorType references a manufacturer"));

    }


    private final String newCapacitorType2Json = JsonConverter.toJson(true,
            "typeName", "Sealdtite",
            "constructionName", "wax-paper",
            "companyName", "Solar"
    );
    /**
     * Test successful creation of new CapacitorType with only mandatory fields.
     */
    @Test
    void newCapacitorType_onlyMandatoryFields_success() throws Exception {
        manufacturerRepository.save(manufacturer2);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/type/create")
                .content(newCapacitorType2Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isCreated());

    }


    private final String capacitorTypeNoNameJson = JsonConverter.toJson(true,
            "constructionName", "wax-paper",
            "startYear", 1934,
            "endYear", 1939,
            "description", "Hermetically sealed",
            "companyName", "Solar"
    );
    /**
     * Test creation of new CapacitorType without name fail.
     */
    @Test
    void newCapacitorType_noName_fail() throws Exception {
        manufacturerRepository.save(manufacturer2);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/type/create")
                .content(capacitorTypeNoNameJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isBadRequest());
    }


    private final String capacitorTypeDatesToLowJson = JsonConverter.toJson(true,
            "typeName", "Sealdtite",
            "constructionName", "wax-paper",
            "startYear", 33,
            "endYear", 67,
            "description", "Hermetically sealed",
            "companyName", "Solar"
    );
    /**
     * Test creation of new CapacitorType with years before 1000.
     */
    @Test
    void newCapacitorType_datesTooLow_fail() throws Exception {
        manufacturerRepository.save(manufacturer2);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/type/create")
                .content(capacitorTypeDatesToLowJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isBadRequest());
    }

    private final String capacitorTypeDatesToHighJson = JsonConverter.toJson(true,
            "typeName", "Sealdtite",
            "constructionName", "wax-paper",
            "startYear", 2099,
            "endYear", 2101,
            "description", "Hermetically sealed",
            "companyName", "Solar"
    );
    /**
     * Test creation of new capacitorType with years after 2050.
     */
    @Test
    void newCapacitorType_datesTooHigh_fail() throws Exception {
        manufacturerRepository.save(manufacturer2);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/type/create")
                .content(capacitorTypeDatesToHighJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isBadRequest());
    }

    private final String capacitorTypeDatesInWrongOrder = JsonConverter.toJson(true,
            "typeName", "Sealdtite",
            "constructionName", "wax-paper",
            "startYear", 1935,
            "endYear", 1920,
            "description", "Hermetically sealed",
            "companyName", "Solar"
    );
    /**
     * Test creation of new capacitorType with years after 2050.
     */
    @Test
    void newCapacitorType_datesWrongOrder_fail() throws Exception {
        manufacturerRepository.save(manufacturer2);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/type/create")
                .content(capacitorTypeDatesInWrongOrder)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isBadRequest());
    }


    /**
     * Test unsuccessful creation of new CapacitorType when the Manufacturer it references doesn't exist.
     */
    @Test
    void newCapacitorType_typeNameConflict_fail() throws Exception {
        manufacturerRepository.save(manufacturer2);

        CapacitorType capacitorType = new CapacitorType();
        capacitorType.setTypeName("Sealdtite");
        capacitorType.setManufacturer(manufacturer2);
        capacitorType.setConstruction(new Construction("Wax-Paper"));
        capacitorTypeRepository.save(capacitorType);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/type/create")
                .content(newCapacitorType1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isConflict()).andReturn();

        assertTrue(result.getResolvedException().toString().contains("already exists"));

    }


}
