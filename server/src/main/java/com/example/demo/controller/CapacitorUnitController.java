package com.example.demo.controller;

import com.example.demo.model.CapacitorType;
import com.example.demo.model.CapacitorUnit;
import com.example.demo.payload.request.CapacitorUnitRequest;
import com.example.demo.repository.CapacitorTypeRepository;
import com.example.demo.repository.CapacitorUnitRepository;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/unit")
public class CapacitorUnitController {

    private final static String PARENT_TYPE_NOT_FOUND_ERROR = "The CapacitorUnit references a CapacitorType \"%s\" that does not exist.";
    private final static String UNIT_NAME_EXISTS_ERROR = "The CapacitorUnit with the value (%s) already exists for the CapacitorType \"%s\"";

    private final CapacitorTypeRepository capacitorTypeRepository;
    private final CapacitorUnitRepository capacitorUnitRepository;

    CapacitorUnitController(CapacitorTypeRepository capacitorTypeRepository,
                            CapacitorUnitRepository capacitorUnitRepository) {
        this.capacitorTypeRepository = capacitorTypeRepository;
        this.capacitorUnitRepository = capacitorUnitRepository;
    }


    /**
     * Create a CapacitorUnit
     * @param capacitorUnitRequest the new CapacitorUnit to create
     */
    @PostMapping(value = "create")
    public void createCapacitorUnit(@Validated @RequestBody CapacitorUnitRequest capacitorUnitRequest,
                                   HttpServletResponse response) {

        CapacitorType parentType = capacitorTypeRepository.findByTypeNameIgnoreCaseAndCompanyNameIgnoreCase(
                capacitorUnitRequest.getTypeName(), capacitorUnitRequest.getCompanyName());

        if (parentType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(PARENT_TYPE_NOT_FOUND_ERROR,
                            capacitorUnitRequest.getTypeName()));
        }

        // Create and attach to CapacitorType
        CapacitorUnit newCapacitorUnit = new CapacitorUnit(capacitorUnitRequest);
        newCapacitorUnit.setCapacitorType(parentType);

        try {
            capacitorUnitRepository.save(newCapacitorUnit);

        // Catch Duplicate Entry
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format(UNIT_NAME_EXISTS_ERROR,
                            newCapacitorUnit,
                            newCapacitorUnit.getCapacitorType().getTypeName())
            );
        }

        response.setStatus(HttpServletResponse.SC_CREATED);
    }

}
