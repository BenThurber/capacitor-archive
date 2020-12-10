package com.example.demo.controller;

import com.example.demo.model.Manufacturer;
import com.example.demo.payload.request.ManufacturerCreateRequest;
import com.example.demo.payload.response.ManufacturerResponse;
import com.example.demo.repository.ManufacturerRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/manufacturer")
public class ManufacturerController {

    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerController(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }


    /**
     * Create a manufacturer
     * @param manufacturerCreateRequest the new manufacturer to create
     */
    @PostMapping(value = "create")
    public void createManufacturer(@Validated @RequestBody ManufacturerCreateRequest manufacturerCreateRequest, HttpServletResponse response) {
        manufacturerRepository.save(new Manufacturer(manufacturerCreateRequest));
        response.setStatus(HttpServletResponse.SC_CREATED);
    }


    /**
     * Get a manufacturer by id
     * @param manufacturerId id of the manufacturer to get
     */
    @GetMapping("id/{manufacturerId}")
    public ManufacturerResponse getManufacturerById(@PathVariable Long manufacturerId, HttpServletResponse response) {
        Manufacturer manufacturer = manufacturerRepository.findById(manufacturerId);
        response.setStatus(HttpServletResponse.SC_OK);
        return new ManufacturerResponse(manufacturer);
    }
}
