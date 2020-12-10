package com.example.demo.controller;

import com.example.demo.model.Manufacturer;
import com.example.demo.payload.request.ManufacturerCreateRequest;
import com.example.demo.repository.ManufacturerRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public void registerUser(@Validated @RequestBody ManufacturerCreateRequest manufacturerCreateRequest, HttpServletResponse response) {
        manufacturerRepository.save(new Manufacturer(manufacturerCreateRequest));
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}
