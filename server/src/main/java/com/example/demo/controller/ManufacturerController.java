package com.example.demo.controller;

import com.example.demo.model.Manufacturer;
import com.example.demo.payload.request.ManufacturerCreateRequest;
import com.example.demo.payload.response.ManufacturerResponse;
import com.example.demo.repository.ManufacturerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
     * @param id id of the manufacturer to get
     */
    @GetMapping("id/{id}")
    public ManufacturerResponse getManufacturerById(@PathVariable Long id, HttpServletResponse response) {
        Manufacturer manufacturer = manufacturerRepository.findById(id);

        if (manufacturer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid manufacturer id, manufacturer not found");
        }

        response.setStatus(HttpServletResponse.SC_OK);
        return new ManufacturerResponse(manufacturer);
    }


    /**
     * Get a manufacturer by companyName case insensitive.  Because company_name is unique in the database, there is
     * only ever one Manufacturer returned.
     * @param companyName name of the manufacturer to get
     * @return a Manufacturer with matching companyName
     */
    @GetMapping("name/{companyName}")
    public ManufacturerResponse getManufacturerByNameIgnoreCase(@PathVariable String companyName, HttpServletResponse response) {
        Manufacturer manufacturer = manufacturerRepository.findByCompanyNameIgnoreCase(companyName);

        if (manufacturer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Manufacturer named %s was not found", companyName));
        }

        response.setStatus(HttpServletResponse.SC_OK);
        return new ManufacturerResponse(manufacturer);
    }
}
