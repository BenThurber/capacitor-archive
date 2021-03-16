package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.payload.response.CapacitorUnitResponse;
import com.example.demo.repository.CapacitorTypeRepository;
import com.example.demo.repository.CapacitorUnitRepository;
import com.example.demo.repository.ManufacturerRepository;
import com.example.demo.repository.PhotoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

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
    @MockBean
    private PhotoRepository photoRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final List<Manufacturer> manufacturerMockTable = new ArrayList<>();
    private final List<CapacitorType> capacitorTypeMockTable = new ArrayList<>();
    private final List<CapacitorUnit> capacitorUnitMockTable = new ArrayList<>();
    private final List<Photo> photoMockTable = new ArrayList<>();

    private static Long manufacturerCount = 0L;
    private static final Long DEFAULT_MANUFACTURER_ID = 1L;
    private static Long capacitorTypeCount = 0L;
    private static final Long DEFAULT_CAPACITOR_TYPE_ID = 1L;
    private static Long capacitorUnitCount = 0L;
    private static final Long DEFAULT_CAPACITOR_UNIT_ID = 1L;
    private static Long photoCount = 0L;
    private static final Long DEFAULT_PHOTO_ID = 1L;


    private Manufacturer manufacturer1;
    private Manufacturer manufacturer2;
    private CapacitorType capacitorType1;
    private CapacitorUnit capacitorUnitNullVoltageAndId;
    private CapacitorUnit capacitorUnit1;
    private CapacitorUnit capacitorUnit2;

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

        capacitorUnit2 = new CapacitorUnit();
        capacitorUnit2.setCapacitance(20000L);
        capacitorUnit2.setVoltage(600);
        capacitorUnit2.setIdentifier(null);
        capacitorUnit2.setNotes("Just another capacitor");
        capacitorUnit2.setCapacitorType(capacitorType1);

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

        when(capacitorTypeRepository.findByTypeNameIgnoreCaseAndCompanyNameIgnoreCase(
                Mockito.any(String.class), Mockito.any(String.class))).thenAnswer(i -> {
            String typeName = i.getArgument(0);
            String companyName = i.getArgument(1);

            Manufacturer manufacturer = manufacturerRepository.findByCompanyNameLowerIgnoreCase(companyName);

            if (manufacturer == null) {
                return null;
            }

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
            if (capacitorUnit.getId() == null && capacitorUnitRepository.findByCapacitanceAndVoltageAndIdentifier(
                    capacitorUnit.getCapacitance(),
                    capacitorUnit.getVoltage(),
                    capacitorUnit.getIdentifier()) != null) {
                throw new org.springframework.dao.DataIntegrityViolationException("");
            }

            if (capacitorUnit.getId() == null) {
                ReflectionTestUtils.setField(capacitorUnit, "id", (DEFAULT_CAPACITOR_UNIT_ID + capacitorUnitCount++));
            }

            ReflectionTestUtils.invokeMethod(capacitorUnit, "prepare");

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

        when(capacitorUnitRepository.findByTypeNameIgnoreCaseAndCompanyNameIgnoreCaseAndValue(
                Mockito.any(String.class),
                Mockito.any(String.class),
                Mockito.any(String.class))).thenAnswer(i -> {
            String companyName = i.getArgument(0);
            String typeName = i.getArgument(1);
            String value = i.getArgument(2);

            return capacitorUnitMockTable.stream().filter(
                    cu -> Objects.equals(cu.getCapacitorType().getManufacturer().getCompanyName().toLowerCase(), companyName.toLowerCase()) &&
                            Objects.equals(cu.getCapacitorType().getTypeName().toLowerCase(), typeName.toLowerCase()) &&
                            Objects.equals(cu.getValue(), value)
            ).findFirst().orElse(null);
        });

        when(capacitorUnitRepository.findAllByTypeNameIgnoreCaseAndCompanyNameIgnoreCase(
                Mockito.any(String.class),
                Mockito.any(String.class))).thenAnswer(i -> {
            String companyName = i.getArgument(0);
            String typeName = i.getArgument(1);

            return capacitorUnitMockTable.stream().filter(
                    cu -> Objects.equals(cu.getCapacitorType().getManufacturer().getCompanyName().toLowerCase(), companyName.toLowerCase()) &&
                            Objects.equals(cu.getCapacitorType().getTypeName().toLowerCase(), typeName.toLowerCase())
            ).collect(Collectors.toList());
        });


        //---- Photos ----

        when(photoRepository.save(Mockito.any(Photo.class))).thenAnswer(i -> {
            Photo photo = i.getArgument(0);
            this.photoMockTable.add(photo);
            return photo;
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
        photoMockTable.clear();
        photoCount = 0L;
    }


    // ----------- Tests -----------

    private final String newCapacitorUnit1Json = JsonConverter.toJson(true,
            "capacitance", 50000,
            "voltage", 400,
            "identifier", "35b",
            "notes", "A popular capacitor",
            "typeName", "sealdtite",
            "companyName", "Solar",
            "photos", new Object[]{
                    JsonConverter.toMap(
                            "order", 1,
                            "url", "www.example.com/images/example.jpg"
                    ),
                    JsonConverter.toMap(
                            "order", 2,
                            "url", "www.example.com/images/foobar.jpg"
                    )
            }
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

        assertEquals(50000, capacitorUnitMockTable.get(0).getCapacitance());
        assertEquals("35b", capacitorUnitMockTable.get(0).getIdentifier());
        assertEquals(400, capacitorUnitMockTable.get(0).getVoltage());
        assertEquals(2, capacitorUnitMockTable.get(0).getPhotos().size());
        assertEquals("www.example.com/images/example.jpg",
                capacitorUnitMockTable.get(0).getPhotos().get(0).getUrl());

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

        assertThat(result.getResolvedException().toString(), containsString("The CapacitorUnit references a CapacitorType"));

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

        assertThat(result.getResolvedException().toString(), containsString("The CapacitorUnit references a CapacitorType"));

    }


    /**
     * Test that a capacitor unit with the same value returns 409 Conflict.
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

        assertThat(result.getResolvedException().toString(), containsString("already exists for the CapacitorType"));
    }


    /**
     * Test successful creation of new CapacitorUnit.
     */
    @Test
    void getCapacitorUnit_success() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);
        capacitorType1.setManufacturer(manufacturer2);
        capacitorUnit1.setCapacitorType(capacitorType1);
        capacitorUnitRepository.save(capacitorUnit1);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get(
                "/unit/name?companyName=solar&typeName=sealdtite&value=50000C400V35b")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isOk()).andReturn();

        CapacitorUnitResponse cu = objectMapper.readValue(result.getResponse().getContentAsString(), CapacitorUnitResponse.class);
        assertEquals(cu, new CapacitorUnitResponse(capacitorUnit1));
    }


    /**
     * Test successful creation of new CapacitorUnit with mixed case companyName and typeName (which are case insensitive).
     */
    @Test
    void getCapacitorUnit_mixedCaseTypeCompany_success() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);
        capacitorType1.setManufacturer(manufacturer2);
        capacitorUnit1.setCapacitorType(capacitorType1);
        capacitorUnitRepository.save(capacitorUnit1);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get(
                "/unit/name?companyName=soLAR&typeName=SeaLdtite&value=50000C400V35b")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isOk()).andReturn();

        CapacitorUnitResponse cu = objectMapper.readValue(result.getResponse().getContentAsString(), CapacitorUnitResponse.class);
        assertEquals(cu, new CapacitorUnitResponse(capacitorUnit1));
    }


    /**
     * Test unsuccessful creation of new CapacitorUnit with mixed value (which is case sensitive).
     */
    @Test
    void getCapacitorUnit_mixedCaseValue_fail() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);
        capacitorType1.setManufacturer(manufacturer2);
        capacitorUnit1.setCapacitorType(capacitorType1);
        capacitorUnitRepository.save(capacitorUnit1);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get(
                "/unit/name?companyName=solar&typeName=sealdtite&value=50000c400v35b")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isNotFound());

    }


    /**
     * Test successful creation of new CapacitorUnit.
     */
    @Test
    void getAllCapacitorUnits_success() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);
        capacitorType1.setManufacturer(manufacturer2);
        capacitorUnit1.setCapacitorType(capacitorType1);
        capacitorUnitRepository.save(capacitorUnit1);
        capacitorUnitRepository.save(capacitorUnit2);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get(
                "/unit/all?companyName=solar&typeName=sealdtite")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isOk()).andReturn();

        List<CapacitorUnitResponse> receivedUnits = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                TypeFactory.defaultInstance().constructCollectionType(List.class, CapacitorUnitResponse.class));

        List<CapacitorUnitResponse> expectedUnits = Arrays.asList(new CapacitorUnitResponse(capacitorUnit1), new CapacitorUnitResponse(capacitorUnit2));

        assertEquals(expectedUnits, receivedUnits);
    }


    private final String editCapacitorUnitOnlyNotesJson = JsonConverter.toJson(true,
            "capacitance", 50000,
            "voltage", 400,
            "identifier", "35b",
            "notes", "A new edited note",
            "typeName", "sealdtite",
            "companyName", "Solar"
    );
    @Test
    void editCapacitorUnit_onlyEditNotes_success() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);
        capacitorUnitRepository.save(capacitorUnit1);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.put(
                "/unit/edit?companyName=solar&typeName=sealdtite&value=50000C400V35b")
                .content(editCapacitorUnitOnlyNotesJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isOk());

        assertEquals("A new edited note", capacitorUnitMockTable.get(0).getNotes());
    }


    private final String editCapacitorUnitCapacitanceOnlyJson = JsonConverter.toJson(true,
            "capacitance", 50001,
            "voltage", 400,
            "identifier", "35b",
            "notes", "A popular capacitor",
            "typeName", "sealdtite",
            "companyName", "Solar"
    );
    @Test
    void editCapacitorUnit_capacitanceOnly_success() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);
        capacitorUnitRepository.save(capacitorUnit1);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.put(
                "/unit/edit?companyName=solar&typeName=sealdtite&value=50000C400V35b")
                .content(editCapacitorUnitCapacitanceOnlyJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isOk());

        assertEquals(50001, capacitorUnitMockTable.get(0).getCapacitance());
        assertEquals("50001C400V35b", capacitorUnitMockTable.get(0).getValue());
    }


    private final String editCapacitorUnitCapacitanceVoltageIdJson = JsonConverter.toJson(true,
            "capacitance", 50001,
            "voltage", 401,
            "identifier", "35c",
            "notes", "A popular capacitor",
            "typeName", "sealdtite",
            "companyName", "Solar"
    );
    @Test
    void editCapacitorUnit_capacitanceVoltageId_success() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);
        capacitorUnitRepository.save(capacitorUnit1);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.put(
                "/unit/edit?companyName=solar&typeName=sealdtite&value=50000C400V35b")
                .content(editCapacitorUnitCapacitanceVoltageIdJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isOk());

        assertEquals(50001, capacitorUnitMockTable.get(0).getCapacitance());
        assertEquals(401, capacitorUnitMockTable.get(0).getVoltage());
        assertEquals("35c", capacitorUnitMockTable.get(0).getIdentifier());
        assertEquals("50001C401V35c", capacitorUnitMockTable.get(0).getValue());
    }


    @Test
    void editCapacitorUnit_notFound_unsuccessful() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);
        capacitorUnitRepository.save(capacitorUnit1);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.put(
                "/unit/edit?companyName=solar&typeName=sealdtite&value=bad-value")
                .content(editCapacitorUnitOnlyNotesJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isNotFound());
    }

}
