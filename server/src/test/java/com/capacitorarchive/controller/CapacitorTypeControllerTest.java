package com.capacitorarchive.controller;

import com.capacitorarchive.model.*;
import com.capacitorarchive.payload.response.CapacitorTypeResponse;
import com.capacitorarchive.payload.response.CapacitorTypeSearchResponse;
import com.capacitorarchive.repository.CapacitorTypeRepository;
import com.capacitorarchive.repository.ConstructionRepository;
import com.capacitorarchive.repository.ManufacturerRepository;
import com.fasterxml.jackson.databind.JsonNode;
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

import java.util.*;
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


    private Thumbnail thumbnail1;
    private Thumbnail thumbnail2;
    private Thumbnail thumbnail3;
    private Thumbnail thumbnail4;
    private Thumbnail thumbnail5;
    private Photo photo1;
    private Photo photo2;
    private Photo photo3;
    private Photo photo4;
    private Photo photo5;
    private CapacitorUnit capacitorUnit1;
    private CapacitorUnit capacitorUnit2;
    private CapacitorUnit capacitorUnit3;
    private CapacitorUnit capacitorUnit4;
    private CapacitorUnit capacitorUnit5;
    private CapacitorType capacitorType1;
    private CapacitorType capacitorType2;
    private Manufacturer manufacturer1;
    private Manufacturer manufacturer2;


    @BeforeEach
    void initializeTestEntities() {

        //------Thumbnails and Photos-------
        thumbnail1 = new Thumbnail();
        thumbnail1.setPhoto(photo1);
        thumbnail1.setUrl("url1_thumb");
        photo1 = new Photo();
        photo1.setOrder(1);
        photo1.setUrl("url1");
        photo1.setThumbnails(Collections.singleton(thumbnail1));

        thumbnail5 = new Thumbnail();
        thumbnail5.setPhoto(photo5);
        thumbnail5.setUrl("url5_thumb");
        photo5 = new Photo();
        photo5.setOrder(2);
        photo5.setUrl("url5");
        photo5.setThumbnails(Collections.singleton(thumbnail5));

        thumbnail2 = new Thumbnail();
        thumbnail2.setPhoto(photo2);
        thumbnail2.setUrl("url2_thumb");
        photo2 = new Photo();
        photo2.setOrder(1);
        photo2.setUrl("url2");
        photo2.setThumbnails(Collections.singleton(thumbnail2));

        thumbnail3 = new Thumbnail();
        thumbnail3.setPhoto(photo3);
        thumbnail3.setUrl("url3_thumb");
        photo3 = new Photo();
        photo3.setOrder(1);
        photo3.setUrl("url3");
        photo3.setThumbnails(Collections.singleton(thumbnail3));

        thumbnail4 = new Thumbnail();
        thumbnail4.setPhoto(photo4);
        thumbnail4.setUrl("url4_thumb");
        photo4 = new Photo();
        photo4.setOrder(1);
        photo4.setUrl("url4");
        photo4.setThumbnails(Collections.singleton(thumbnail4));

        //------Capacitor Units-------
        capacitorUnit1 = new CapacitorUnit();
        capacitorUnit1.setCapacitance(2000000L);
        capacitorUnit1.setVoltage(600);
        capacitorUnit1.setPhotos(new HashSet<>(Arrays.asList(photo1, photo5)));
        photo1.setCapacitorUnit(capacitorUnit1);
        photo5.setCapacitorUnit(capacitorUnit1);

        capacitorUnit2 = new CapacitorUnit();
        capacitorUnit2.setCapacitance(1000000L);
        capacitorUnit2.setVoltage(600);
        capacitorUnit2.setPhotos(Collections.singleton(photo2));
        photo2.setCapacitorUnit(capacitorUnit2);

        capacitorUnit3 = new CapacitorUnit();
        capacitorUnit3.setCapacitance(500000L);
        capacitorUnit3.setVoltage(600);
        capacitorUnit3.setPhotos(Collections.singleton(photo3));
        photo3.setCapacitorUnit(capacitorUnit3);

        capacitorUnit4 = new CapacitorUnit();
        capacitorUnit4.setCapacitance(100000L);
        capacitorUnit4.setVoltage(600);
        capacitorUnit4.setPhotos(Collections.singleton(photo4));
        photo4.setCapacitorUnit(capacitorUnit4);

        capacitorUnit5 = new CapacitorUnit();
        capacitorUnit5.setCapacitorType(capacitorType2);
        capacitorUnit5.setCapacitance(300L);
        capacitorUnit5.setVoltage(500);

        //------Capacitor Types-------
        capacitorType1 = new CapacitorType();
        capacitorType1.setTypeName("Sealdtite");
        capacitorType1.setConstruction(new Construction("Wax-Paper"));
        capacitorType1.setCapacitorUnits(Arrays.asList(capacitorUnit1, capacitorUnit2, capacitorUnit3, capacitorUnit4));
        capacitorUnit1.setCapacitorType(capacitorType1);
        capacitorUnit2.setCapacitorType(capacitorType1);
        capacitorUnit3.setCapacitorType(capacitorType1);
        capacitorUnit4.setCapacitorType(capacitorType1);

        capacitorType2 = new CapacitorType();
        capacitorType2.setTypeName("Moulded Plastic Mica");
        capacitorType2.setConstruction(new Construction("Mica"));
        capacitorType2.setCapacitorUnits(Collections.singletonList(capacitorUnit5));
        capacitorUnit5.setCapacitorType(capacitorType2);

        //------Manufacturers-------
        manufacturer1 = new Manufacturer();
        manufacturer1.setCompanyName("Cornell Dubilier");
        manufacturer1.setOpenYear((short)1909);
        manufacturer1.setSummary("A company still in business");

        manufacturer2 = new Manufacturer();
        manufacturer2.setCompanyName("Solar");
        manufacturer2.setOpenYear((short)1917);
        manufacturer2.setCloseYear((short)1948);
        manufacturer2.setSummary("A company that made high quality wax paper capacitors");
        capacitorType1.setManufacturer(manufacturer2);
        capacitorType2.setManufacturer(manufacturer2);
        manufacturer2.setCapacitorTypes(Arrays.asList(capacitorType1, capacitorType2));

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

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/type/name?companyName=solar&typeName=sealdtite")
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

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/type/name?companyName=solar&typeName=sealdtite")
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

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/type/name?companyName=solar&typeName=wrong-name")
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
    void getAllCapacitorTypes_success() throws Exception {
        capacitorTypeRepository.save(capacitorType1);
        capacitorTypeRepository.save(capacitorType2);
        manufacturer2.setCapacitorTypes(Arrays.asList(capacitorType1, capacitorType2));
        manufacturerRepository.save(manufacturer2);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/type/all?companyName=solar")
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
    void getAllCapacitorTypes_noExistingManufacturer_fail() throws Exception {
        capacitorTypeRepository.save(capacitorType1);
        capacitorTypeRepository.save(capacitorType2);
        manufacturer2.setCapacitorTypes(Arrays.asList(capacitorType1, capacitorType2));
        manufacturerRepository.save(manufacturer2);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/type/all?companyName=solarus")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isBadRequest());

    }


    /**
     * Test successful retrieval of CapacitorTypeSearchResponse List.
     */
    @Test
    void getAllCapacitorTypeSearchResponses_equality_success() throws Exception {
        capacitorTypeRepository.save(capacitorType1);
        capacitorTypeRepository.save(capacitorType2);
        manufacturer2.setCapacitorTypes(Arrays.asList(capacitorType1, capacitorType2));
        manufacturerRepository.save(manufacturer2);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/type/all-results?companyName=solar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isOk()).andReturn();

        JsonNode receivedTypesTree = objectMapper.readTree(
                result.getResponse().getContentAsString());

        List<CapacitorTypeSearchResponse> expectedTypes = Arrays.asList(new CapacitorTypeSearchResponse(capacitorType1), new CapacitorTypeSearchResponse(capacitorType2));
        JsonNode expectedTypesTree = objectMapper.valueToTree(expectedTypes);

        assertEquals(2, receivedTypesTree.size());
        assertEquals(expectedTypesTree.toString(), receivedTypesTree.toString());
    }


    /**
     * Test correct thumbnailUrl of CapacitorTypeResponse.
     */
    @Test
    void getAllCapacitorTypeSearchResponses_correctUrl_success() throws Exception {
        capacitorTypeRepository.save(capacitorType1);
        capacitorTypeRepository.save(capacitorType2);
        manufacturer2.setCapacitorTypes(Arrays.asList(capacitorType1, capacitorType2));
        manufacturerRepository.save(manufacturer2);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/type/all-results?companyName=solar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isOk()).andReturn();

        JsonNode receivedTypes = objectMapper.readTree(
                result.getResponse().getContentAsString());

        // Explanation: List of capacitor units is length 4.  Middle index is 4 / 2 == 2.
        // Capacitor units are sorted from low to high, i.e. CapacitorUnit4, CapacitorUnit3, CapacitorUnit2...
        // So the *third* element in the list at index 2 is CapacitorUnit2.
        assertEquals("url2_thumb", receivedTypes.get(0).get("thumbnailUrl").textValue());
        assertNull(receivedTypes.get(1).get("thumbnailUrl").textValue());
    }


    /**
     * Test unsuccessful retrieval of CapacitorTypeSearchResponse List from Manufacturer that doesn't exist.
     */
    @Test
    void getAllCapacitorTypeSearchResponses_noExistingManufacturer_fail() throws Exception {
        capacitorTypeRepository.save(capacitorType1);
        capacitorTypeRepository.save(capacitorType2);
        manufacturer2.setCapacitorTypes(Arrays.asList(capacitorType1, capacitorType2));
        manufacturerRepository.save(manufacturer2);

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/type/all-results?companyName=solarus")
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

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.put("/type/edit?companyName=solar&typeName=sealdtite")
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

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.put("/type/edit?companyName=solar&typeName=sealdtite")
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

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.put("/type/edit?companyName=solar&typeName=sealdtite")
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

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.put("/type/edit?companyName=solar&typeName=sealdtite")
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

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.put("/type/edit?companyName=solar&typeName=non-existing")
                .content(editCapacitorTypeNewConstructionJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isNotFound());
    }


}
