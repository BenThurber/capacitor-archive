package com.capacitorarchive.controller;

import com.capacitorarchive.model.Construction;
import com.capacitorarchive.repository.ConstructionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConstructionController.class)
class ConstructionControllerTest {


    @Autowired
    private MockMvc mvc;
    @MockBean
    private ConstructionRepository constructionRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Construction> constructionMockTable = new ArrayList<>();

    @BeforeEach
    void mockRepository() {
        constructionMockTable.clear();

        when(constructionRepository.save(Mockito.any(Construction.class))).thenAnswer(i -> {
            Construction newConstruction = i.getArgument(0);
            constructionMockTable.add(newConstruction);
            return newConstruction;
        });
        when(constructionRepository.findAll()).thenAnswer(i -> {
            return constructionMockTable;
        });
    }


    // ----------- Tests -----------

    /**
     * Test successful creation of new construction.
     */
    @Test
    void newConstruction() throws Exception {

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/construction/create")
                .content("wax-paper")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals("Wax-Paper", result.getResponse().getContentAsString());
    }


    /**
     * Test successful retrieval of all constructions.
     */
    @Test
    void getAllConstructions() throws Exception {
        List<String> constructionNames = Arrays.asList("Mica", "Wax-Paper", "Multipart Electrolytic");
        constructionMockTable = constructionNames.stream().map(Construction::new).collect(Collectors.toList());

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.get("/construction/all")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(httpReq)
                .andExpect(status().isOk())
                .andReturn();

        String jsonArray = result.getResponse().getContentAsString();
        List<String> constructionNamesReceived = objectMapper.readValue(jsonArray, new TypeReference<>(){});
        assertEquals(constructionNames, constructionNamesReceived);
    }

}

