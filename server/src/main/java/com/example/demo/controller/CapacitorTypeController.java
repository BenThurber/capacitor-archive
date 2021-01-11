package com.example.demo.controller;

import com.example.demo.model.CapacitorType;
import com.example.demo.model.Construction;
import com.example.demo.model.Manufacturer;
import com.example.demo.payload.request.CapacitorTypeRequest;
import com.example.demo.repository.CapacitorTypeRepository;
import com.example.demo.repository.ConstructionRepository;
import com.example.demo.repository.ManufacturerRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/type")
public class CapacitorTypeController {

    private final static String TYPE_NAME_EXISTS_ERROR = "The name \"%s\" already exists within the \"%s\" manufacturer.";
    private final static String PARENT_MANUFACTURER_NOT_FOUND_ERROR = "The CapacitorType references a manufacturer \"%s\" that doesn't exist.";

    private static Log logger = LogFactory.getLog(CapacitorTypeController.class);

    private final ManufacturerRepository manufacturerRepository;
    private final ConstructionRepository constructionRepository;
    private final CapacitorTypeRepository capacitorTypeRepository;

    CapacitorTypeController(ManufacturerRepository manufacturerRepository,
                            ConstructionRepository constructionRepository,
                            CapacitorTypeRepository capacitorTypeRepository) {
        this.manufacturerRepository = manufacturerRepository;
        this.constructionRepository = constructionRepository;
        this.capacitorTypeRepository = capacitorTypeRepository;
    }


    /**
     * Create a CapacitorType
     * @param capacitorTypeRequest the new CapacitorType to create
     */
    @PostMapping(value = "create")
    public void createManufacturer(@Validated @RequestBody CapacitorTypeRequest capacitorTypeRequest,
                                   HttpServletResponse response) {

        Manufacturer parentManufacturer = manufacturerRepository.findByCompanyNameLowerIgnoreCase(capacitorTypeRequest.getCompanyName());
        Construction construction = constructionRepository.findByConstructionName(capacitorTypeRequest.getConstructionName());

        // Handle Errors
        if (parentManufacturer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(PARENT_MANUFACTURER_NOT_FOUND_ERROR,
                            capacitorTypeRequest.getCompanyName()));
        }

        if (capacitorTypeRepository.findByTypeNameLowerIgnoreCaseAndManufacturer(capacitorTypeRequest.getTypeName(), parentManufacturer) != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format(TYPE_NAME_EXISTS_ERROR,
                            capacitorTypeRequest.getTypeName(),
                            capacitorTypeRequest.getCompanyName())
            );
        }

        // Create new construction
        if (construction == null) {
            construction = new Construction(capacitorTypeRequest.getConstructionName());
            constructionRepository.save(construction);
            logger.info("Created new construction " + construction.getConstructionName());
        }


        // Create and attach to manufacturer and construction
        CapacitorType newCapacitorType = new CapacitorType(capacitorTypeRequest);
        newCapacitorType.setManufacturer(parentManufacturer);
        newCapacitorType.setConstruction(construction);

        capacitorTypeRepository.save(newCapacitorType);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

}
