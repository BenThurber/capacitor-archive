package com.example.demo.controller;

import com.example.demo.model.Construction;
import com.example.demo.repository.ConstructionRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/construction")
public class ConstructionController {

    private final ConstructionRepository constructionRepository;

    public ConstructionController(ConstructionRepository constructionRepository) {
        this.constructionRepository = constructionRepository;
    }


    /**
     * Create a new Construction
     * @param constructionName the name of the new construction type
     */
    @PostMapping(value = "/create")
    public void addNewConstruction(@RequestBody String constructionName, HttpServletResponse response) {

        Construction newConstruction = new Construction(constructionName);
        constructionRepository.save(newConstruction);
        response.setStatus(HttpServletResponse.SC_CREATED);

    }


    /**
     * Get all construction names as strings from the database
     * @return String the names of construction types like wax-paper or mica
     */
    @GetMapping(value = "/all")
    public List<String> getAllConstructions() {
        return constructionRepository.findAll().stream().map(Construction::getConstructionName).collect(Collectors.toList());
    }


}
