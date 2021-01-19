package com.example.demo.controller;

import com.example.demo.model.CapacitorType;
import com.example.demo.model.Construction;
import com.example.demo.model.Manufacturer;
import com.example.demo.payload.response.CapacitorTypeResponse;
import com.example.demo.repository.CapacitorTypeRepository;
import com.example.demo.repository.ConstructionRepository;
import com.example.demo.repository.ManufacturerRepository;
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
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
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
    private CapacitorType capacitorType1;
    private CapacitorType capacitorType2;

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

        capacitorType1 = new CapacitorType();
        capacitorType1.setTypeName("Sealdtite");
        capacitorType1.setManufacturer(manufacturer2);
        capacitorType1.setConstruction(new Construction("Wax-Paper"));

        capacitorType2 = new CapacitorType();
        capacitorType2.setTypeName("Moulded Plastic Mica");
        capacitorType2.setManufacturer(manufacturer2);
        capacitorType2.setConstruction(new Construction("Mica"));

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
                    return capacitorTypeRepository.findByTypeNameIgnoreCaseAndCompanyNameIgnoreCase(
                            typeName, manufacturer.getCompanyName());
        });

        when(capacitorTypeRepository.findByTypeNameIgnoreCaseAndCompanyNameIgnoreCase(
                Mockito.any(String.class), Mockito.any(String.class))).thenAnswer(i -> {
            String typeName = i.getArgument(0);
            String companyName = i.getArgument(1);

            List<CapacitorType> capacitorTypesInManufacturer = capacitorTypeMockTable.stream().filter(
                    ct -> ct.getManufacturer().getCompanyName().toLowerCase().equals(companyName.toLowerCase())
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
     * Test unsuccessful creation of new CapacitorType when the Manufacturer it references does not exist.
     */
    @Test
    void newCapacitorType_noExistingManufacturer_fail() throws Exception {

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/type/create")
                .content(newCapacitorType1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isBadRequest()).andReturn();

        assertThat(result.getResolvedException().toString(), containsString("The CapacitorType references a manufacturer"));

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
     * Test unsuccessful creation of new CapacitorType when the typeName used already exists for the Manufacturer.
     */
    @Test
    void newCapacitorType_typeNameConflict_fail() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/type/create")
                .content(newCapacitorType1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isConflict()).andReturn();


        assertThat(result.getResolvedException().toString(), containsString("already exists"));
    }


    /**
     * Test successful retrieval of CapacitorType.
     */
    @Test
    void getCapacitorType_success() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/type/name/solar/sealdtite")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isOk()).andReturn();

        CapacitorTypeResponse receivedType = objectMapper.readValue(result.getResponse().getContentAsString(), CapacitorTypeResponse.class);
        assertEquals(new CapacitorTypeResponse(capacitorType1), receivedType);
    }


    /**
     * Test unsuccessful retrieval of CapacitorType when the Manufacturer it references does not exist.
     */
    @Test
    void getCapacitorType_cantFindManufacturer_fail() throws Exception {
        capacitorTypeRepository.save(capacitorType1);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/type/name/solar/sealdtite")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isBadRequest()).andReturn();


        assertThat(result.getResolvedException().toString(), containsString("does not exist"));
    }


    /**
     * Test unsuccessful creation of new CapacitorType when the Manufacturer it references does not exist.
     */
    @Test
    void getCapacitorType_notFound_fail() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/type/name/solar/wrong-name")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isNotFound()).andReturn();


        assertThat(result.getResolvedException().toString(), containsString("could not be found"));
    }


    /**
     * Test successful retrieval of CapacitorTypeResponse List.
     */
    @Test
    void getCapacitorTypes_success() throws Exception {
        capacitorTypeRepository.save(capacitorType1);
        capacitorTypeRepository.save(capacitorType2);
        manufacturer2.setCapacitorTypes(Arrays.asList(capacitorType1, capacitorType2));
        manufacturerRepository.save(manufacturer2);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/type/all/solar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isOk()).andReturn();

        List<CapacitorTypeResponse> receivedTypes = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                TypeFactory.defaultInstance().constructCollectionType(List.class, CapacitorTypeResponse.class));

        List<CapacitorTypeResponse> expectedTypes = Arrays.asList(new CapacitorTypeResponse(capacitorType1), new CapacitorTypeResponse(capacitorType2));

        assertEquals(expectedTypes, receivedTypes);
    }


    /**
     * Test unsuccessful retrieval of CapacitorTypeResponse List from Manufacturer that doesn't exist.
     */
    @Test
    void getCapacitorTypes_noExistingManufacturer_fail() throws Exception {
        capacitorTypeRepository.save(capacitorType1);
        capacitorTypeRepository.save(capacitorType2);
        manufacturer2.setCapacitorTypes(Arrays.asList(capacitorType1, capacitorType2));
        manufacturerRepository.save(manufacturer2);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/type/all/solarus")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isBadRequest());

    }


    private final String editCapacitorType1Json = JsonConverter.toJson(true,
            "typeName", "Sealdtite",
            "constructionName", "wax-paper",
            "startYear", 1936,
            "endYear", 1948,
            "description", "A new edited description",
            "companyName", "Solar"
    );
    /**
     * Test successful edit of CapacitorType.
     */
    @Test
    void editCapacitorType_success() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);
        constructionRepository.save(new Construction("Wax-Paper"));
        constructionRepository.save(new Construction("Mica"));

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.put("/type/edit/solar/sealdtite")
                .content(editCapacitorType1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isOk());

        assertEquals((short)1936, capacitorTypeMockTable.get(0).getStartYear());
        assertEquals((short)1948, capacitorTypeMockTable.get(0).getEndYear());
        assertEquals("A new edited description", capacitorTypeMockTable.get(0).getDescription());
    }

    private final String editCapacitorTypeNewTypeNameJson = JsonConverter.toJson(true,
            "typeName", "New-Type-Name",
            "constructionName", "wax-paper",
            "startYear", 1936,
            "endYear", 1948,
            "description", "A new edited description",
            "companyName", "Solar"
    );
    /**
     * Test successful edit of CapacitorType when changing typeName
     */
    @Test
    void editCapacitorType_newTypeName_success() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);
        constructionRepository.save(new Construction("Wax-Paper"));
        constructionRepository.save(new Construction("Mica"));

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.put("/type/edit/solar/sealdtite")
                .content(editCapacitorTypeNewTypeNameJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isOk());

        assertEquals("New-Type-Name", capacitorTypeMockTable.get(0).getTypeName());
    }


    private final String editCapacitorTypeChangedConstructionJson = JsonConverter.toJson(true,
            "typeName", "Sealdtite",
            "constructionName", "mica",
            "startYear", 1936,
            "endYear", 1948,
            "description", "A new edited description",
            "companyName", "Solar"
    );
    /**
     * Test successful edit of CapacitorType when changing construction to an existing construction in the database.
     */
    @Test
    void editCapacitorType_changedConstruction_success() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);
        constructionRepository.save(new Construction("Wax-Paper"));
        constructionRepository.save(new Construction("Mica"));

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.put("/type/edit/solar/sealdtite")
                .content(editCapacitorTypeChangedConstructionJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isOk());

        assertEquals("Mica", capacitorTypeMockTable.get(0).getConstruction().getConstructionName());
    }


    private final String editCapacitorTypeNewConstructionJson = JsonConverter.toJson(true,
            "typeName", "Sealdtite",
            "constructionName", "electrolytic",
            "startYear", 1936,
            "endYear", 1948,
            "description", "A new edited description",
            "companyName", "Solar"
    );
    /**
     * Test successful edit of CapacitorType when changing construction to a new construction not in the database.
     */
    @Test
    void editCapacitorType_newConstruction_success() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);
        constructionRepository.save(new Construction("Wax-Paper"));
        constructionRepository.save(new Construction("Mica"));

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.put("/type/edit/solar/sealdtite")
                .content(editCapacitorTypeNewConstructionJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isOk());

        assertEquals("Electrolytic", capacitorTypeMockTable.get(0).getConstruction().getConstructionName());
        assertEquals(3, constructionMockTable.size());
        assertEquals("Electrolytic", constructionMockTable.get(2).getConstructionName());
    }


    /**
     * Test unsuccessful edit of CapacitorType where the type does not exist.
     */
    @Test
    void editCapacitorType_noCapacitorTypeFound_fail() throws Exception {
        manufacturerRepository.save(manufacturer2);
        capacitorTypeRepository.save(capacitorType1);
        constructionRepository.save(new Construction("Wax-Paper"));
        constructionRepository.save(new Construction("Mica"));

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.put("/type/edit/solar/non-existing")
                .content(editCapacitorTypeNewConstructionJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isNotFound());
    }


}
