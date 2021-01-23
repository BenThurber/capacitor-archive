package com.example.demo.controller;

import com.example.demo.model.CapacitorType;
import com.example.demo.model.Construction;
import com.example.demo.model.Manufacturer;
import com.example.demo.payload.request.CapacitorTypeRequest;
import com.example.demo.payload.response.CapacitorTypeResponse;
import com.example.demo.repository.CapacitorTypeRepository;
import com.example.demo.repository.ConstructionRepository;
import com.example.demo.repository.ManufacturerRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/type")
public class CapacitorTypeController {

    private final static String TYPE_NAME_EXISTS_ERROR = "The name \"%s\" already exists within the \"%s\" manufacturer.";
    private final static String PARENT_MANUFACTURER_NOT_FOUND_ERROR = "The CapacitorType references a manufacturer \"%s\" that does not exist.";
    private final static String TYPE_NOT_FOUND_ERROR = "The CapacitorType with companyName %s and typeName %s could not be found.";

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
    public void createCapacitorType(@Validated @RequestBody CapacitorTypeRequest capacitorTypeRequest,
                                    HttpServletResponse response) {

        Manufacturer parentManufacturer = manufacturerRepository.findByCompanyNameLowerIgnoreCase(capacitorTypeRequest.getCompanyName());
        Construction construction = constructionRepository.findByConstructionName(capacitorTypeRequest.getConstructionName());

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


    /**
     * Edit a CapacitorType
     * @param capacitorTypeRequest the new CapacitorType to create
     */
    @PutMapping(value = "edit",
                params = { "companyName", "typeName" }
    )
    public void editCapacitorType(@Validated
                                  @RequestParam(value="companyName") String companyName,
                                  @RequestParam(value="typeName") String typeName,
                                  @RequestBody CapacitorTypeRequest capacitorTypeRequest,
                                  HttpServletResponse response) {

        CapacitorType capacitorType = capacitorTypeRepository.findByTypeNameIgnoreCaseAndCompanyNameIgnoreCase(typeName, companyName);
        Construction editedConstruction = null;

        if (capacitorType == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The capacitor type to edit can not be found");
        }

        // Has construction changed?
        if (!capacitorTypeRequest.getConstructionName().equals(capacitorType.getConstruction().getConstructionName())) {
            editedConstruction = constructionRepository.findByConstructionName(capacitorTypeRequest.getConstructionName());

            // Create new construction?
            if (editedConstruction == null) {
                editedConstruction = new Construction(capacitorTypeRequest.getConstructionName());
                constructionRepository.save(editedConstruction);
                logger.info("Created new construction " + editedConstruction.getConstructionName());
            }
        }

        capacitorType.edit(capacitorTypeRequest, editedConstruction);

        capacitorTypeRepository.save(capacitorType);
        response.setStatus(HttpServletResponse.SC_OK);
    }


    /**
     * Get CapacitorTypeResponse from the name of the owning Manufacturer and the Unique typeName.  Both are case insensitive.
     * @param companyName name of the owning Manufacturer.  If one can not be found a 400 error is returned
     * @param typeName unique key of CapacitorType
     * @return the found type.  If none is found a 404 error is returned.
     */
    @GetMapping(value = "name",
                params = { "companyName", "typeName" }
    )
    public CapacitorTypeResponse getCapacitorTypeByNameIgnoreCase(
                                                                 @RequestParam(value="companyName") String companyName,
                                                                 @RequestParam(value="typeName") String typeName,
                                                                 HttpServletResponse response) {

        Manufacturer parentManufacturer = manufacturerRepository.findByCompanyNameLowerIgnoreCase(companyName);

        if (parentManufacturer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(PARENT_MANUFACTURER_NOT_FOUND_ERROR,
                            companyName));
        }

        CapacitorType capacitorType = capacitorTypeRepository.findByTypeNameLowerIgnoreCaseAndManufacturer(typeName, parentManufacturer);

        if (capacitorType == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format(TYPE_NOT_FOUND_ERROR, companyName, typeName)
            );
        }

        response.setStatus(HttpServletResponse.SC_OK);
        return new CapacitorTypeResponse(capacitorType);

    }


    /**
     * Get List of all CapacitorTypeResponses from a Manufacturer, given companyName.
     * @param companyName name of the owning Manufacturer.  If one can not be found a 400 error is returned.
     * @return the list of found type, empty list if none.
     */
    @GetMapping(value = "all",
                params = { "companyName" }
    )
    public List<CapacitorTypeResponse> getAllCapacitorTypesFromManufacturer(
                                                  @RequestParam(value="companyName") String companyName,
                                                  HttpServletResponse response) {

        Manufacturer parentManufacturer = manufacturerRepository.findByCompanyNameLowerIgnoreCase(companyName);

        if (parentManufacturer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(PARENT_MANUFACTURER_NOT_FOUND_ERROR,
                            companyName));
        }

        List<CapacitorTypeResponse> capacitorTypeResponses = parentManufacturer.getCapacitorTypes()
                .stream()
                .map(CapacitorTypeResponse::new)
                .collect(Collectors.toList());


        response.setStatus(HttpServletResponse.SC_OK);
        return capacitorTypeResponses;
    }

}
