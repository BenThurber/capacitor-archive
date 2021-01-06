package com.example.demo.controller;

import com.example.demo.model.Construction;
import com.example.demo.repository.ConstructionRepository;
import com.example.demo.service.TextUtil;
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
     * Create a new Construction.  Capitalizes the name so that the letter of each first word is capitalized.  This is
     * to reduce naming convention differences between construction types.
     * @param constructionName the name of the new construction type
     * @return the capitalized construction name
     */
    @PostMapping(value = "/create")
    public String addNewConstruction(@RequestBody String constructionName, HttpServletResponse response) {

        String capitalizedConstructionName = TextUtil.title(constructionName, new Character[]{' ', '-', '_'});
        Construction newConstruction = new Construction(capitalizedConstructionName);

        constructionRepository.save(newConstruction);
        response.setStatus(HttpServletResponse.SC_CREATED);

        return capitalizedConstructionName;
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
