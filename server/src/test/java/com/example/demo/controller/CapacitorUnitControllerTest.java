package com.example.demo.controller;

import com.example.demo.model.CapacitorType;
import com.example.demo.model.CapacitorUnit;
import com.example.demo.model.Construction;
import com.example.demo.model.Manufacturer;
import com.example.demo.repository.CapacitorTypeRepository;
import com.example.demo.repository.CapacitorUnitRepository;
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
import org.springframework.dao.DataIntegrityViolationException;
import testUtilities.JsonConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(CapacitorUnitController.class)
class CapacitorUnitControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private ManufacturerRepository manufacturerRepository;
    @MockBean
    private CapacitorTypeRepository capacitorTypeRepository;
    @MockBean
    private CapacitorUnitRepository capacitorUnitRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final List<Manufacturer> manufacturerMockTable = new ArrayList<>();
    private final List<CapacitorType> capacitorTypeMockTable = new ArrayList<>();
    private final List<CapacitorUnit> capacitorUnitMockTable = new ArrayList<>();

    private static Long manufacturerCount = 0L;
    private static final Long DEFAULT_MANUFACTURER_ID = 1L;
    private static Long capacitorTypeCount = 0L;
    private static final Long DEFAULT_CAPACITOR_TYPE_ID = 1L;
    private static Long capacitorUnitCount = 0L;
    private static final Long DEFAULT_CAPACITOR_UNIT_ID = 1L;


    private Manufacturer manufacturer1;
    private Manufacturer manufacturer2;
    private CapacitorType capacitorType1;
    private CapacitorUnit capacitorUnitNullVoltageAndId;
    private CapacitorUnit capacitorUnit1;

    @BeforeEach
    void initializeTestEntities() {
        manufacturer1 = new Manufacturer();
        manufacturer1.setCompanyName("Cornell Dubilier");
        manufacturer1.setOpenYear((short) 1909);
        manufacturer1.setSummary("A company still in business");

        manufacturer2 = new Manufacturer();
        manufacturer2.setCompanyName("Solar");
        manufacturer2.setOpenYear((short) 1917);
        manufacturer2.setCloseYear((short) 1948);
        manufacturer2.setSummary("A company that made high quality wax paper capacitors");

        capacitorType1 = new CapacitorType();
        capacitorType1.setTypeName("Sealdtite");
        capacitorType1.setManufacturer(manufacturer2);
        capacitorType1.setConstruction(new Construction("Wax-Paper"));

        capacitorUnitNullVoltageAndId = new CapacitorUnit();
        capacitorUnitNullVoltageAndId.setCapacitance(50000L);
        capacitorUnitNullVoltageAndId.setVoltage(0);
        capacitorUnitNullVoltageAndId.setIdentifier("");
        capacitorUnitNullVoltageAndId.setNotes("A popular capacitor");
        capacitorUnitNullVoltageAndId.setCapacitorType(capacitorType1);

        capacitorUnit1 = new CapacitorUnit();
        capacitorUnit1.setCapacitance(50000L);
        capacitorUnit1.setVoltage(400);
        capacitorUnit1.setIdentifier("35b");
        capacitorUnit1.setNotes("A popular capacitor");
        capacitorUnit1.setCapacitorType(capacitorType1);

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

        when(manufacturerRepository.getAllCompanyNames()).thenAnswer(i -> manufacturerMockTable.stream()
                .map(Manufacturer::getCompanyName)
                .collect(Collectors.toList()));

        //---- CapacitorType ----

        when(capacitorTypeRepository.save(Mockito.any(CapacitorType.class))).thenAnswer(i -> {
            CapacitorType capacitorType = i.getArgument(0);
            if (capacitorType.getId() == null) {
                ReflectionTestUtils.setField(capacitorType, "id", (DEFAULT_CAPACITOR_TYPE_ID + capacitorTypeCount++));
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

        //---- CapacitorUnit ----

        when(capacitorUnitRepository.save(Mockito.any(CapacitorUnit.class))).thenAnswer(i -> {
            CapacitorUnit capacitorUnit = i.getArgument(0);

            // Check if unique
            if (capacitorUnitRepository.findByCapacitanceAndVoltageAndIdentifier(
                    capacitorUnit.getCapacitance(),
                    capacitorUnit.getVoltage(),
                    capacitorUnit.getIdentifier()) != null) {
                throw new org.springframework.dao.DataIntegrityViolationException("");
            }

            if (capacitorUnit.getId() == null) {
                ReflectionTestUtils.setField(capacitorUnit, "id", (DEFAULT_CAPACITOR_UNIT_ID + capacitorUnitCount++));
            }

            // Remove and replace existing entity
            Long id = capacitorUnit.getId();
            capacitorUnitMockTable.stream().filter(cu -> id != null && id.equals(cu.getId())).findFirst().ifPresent(capacitorUnitMockTable::remove);

            capacitorUnitMockTable.add(capacitorUnit);

            return capacitorUnit;
        });

        when(capacitorUnitRepository.findByCapacitanceAndVoltageAndIdentifier(
                Mockito.any(Long.class),
                Mockito.any(Integer.class),
                Mockito.any(String.class))).thenAnswer(i -> {
            Long capacitance = i.getArgument(0);
            Integer voltage = i.getArgument(1);
            String identifier = i.getArgument(2);

            return capacitorUnitMockTable.stream().filter(
                    cu -> Objects.equals(cu.getCapacitance(), capacitance) &&
                            Objects.equals(cu.getVoltage(), voltage) &&
                            Objects.equals(cu.getIdentifier(), identifier)
            ).findFirst().orElse(null);
        });

    }


    @AfterEach
    void setup() {
        manufacturerMockTable.clear();
        manufacturerCount = 0L;
        capacitorTypeMockTable.clear();
        capacitorTypeCount = 0L;
        capacitorUnitMockTable.clear();
        capacitorUnitCount = 0L;
    }


    // ----------- Tests -----------

    private final String newCapacitorUnit1Json = JsonConverter.toJson(true,
            "capacitance", 50000,
            "voltage", 400,
            "identifier", "35b",
            "notes", "A popular capacitor",
            "typeName", "sealdtite",
            "companyName", "Solar"
    );

    /**
     * Test successful creation of new CapacitorType that creates a new construction.
     */
    @Test
    void newCapacitorUnit_success() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/unit/create")
                .content(newCapacitorUnit1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isCreated()).andReturn();

        assertEquals("35b", capacitorUnitMockTable.get(0).getIdentifier());
        assertEquals(400, capacitorUnitMockTable.get(0).getVoltage());
    }

    private final String newCapacitorUnitNullableValuesJson = JsonConverter.toJson(true,
            "capacitance", 50000,
            "voltage", 0,
            "identifier", "",
            "notes", "A popular capacitor",
            "typeName", "sealdtite",
            "companyName", "Solar"
    );
    /**
     * Test successful creation of new CapacitorType that creates a new construction.
     */
    @Test
    void newCapacitorUnit_nullifiesValues_success() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/unit/create")
                .content(newCapacitorUnitNullableValuesJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isCreated());

        assertNull(capacitorUnitMockTable.get(0).getIdentifier());
        assertNull(capacitorUnitMockTable.get(0).getVoltage());
    }

    /**
     * Test successful creation of new CapacitorType that creates a new construction.
     */
    @Test
    void newCapacitorUnit_noManufacturer_fail() throws Exception {
        capacitorTypeRepository.save(capacitorType1);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/unit/create")
                .content(newCapacitorUnitNullableValuesJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isBadRequest()).andReturn();

        assertTrue(result.getResolvedException().toString().contains("The CapacitorUnit references a manufacturer"));

    }


    /**
     * Test successful creation of new CapacitorType that creates a new construction.
     */
    @Test
    void newCapacitorUnit_noCapacitorType_fail() throws Exception {
        manufacturerRepository.save(manufacturer2);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/unit/create")
                .content(newCapacitorUnitNullableValuesJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isBadRequest()).andReturn();

        assertTrue(result.getResolvedException().toString().contains("The CapacitorUnit references a CapacitorType"));

    }


    /**
     * Test successful creation of new CapacitorType that creates a new construction.
     */
    @Test
    void newCapacitorUnit_unitAlreadyExists_fail() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);
        capacitorUnitRepository.save(capacitorUnit1);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/unit/create")
                .content(newCapacitorUnit1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isConflict()).andReturn();

        assertTrue(result.getResolvedException().toString().contains("already exists for the CapacitorType"));
    }

}
