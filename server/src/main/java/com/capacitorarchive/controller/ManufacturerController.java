package com.capacitorarchive.controller;

import com.capacitorarchive.payload.response.ManufacturerListItemResponse;
import com.capacitorarchive.repository.ManufacturerRepository;
import com.capacitorarchive.model.Manufacturer;
import com.capacitorarchive.payload.request.ManufacturerRequest;
import com.capacitorarchive.payload.response.ManufacturerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/manufacturer")
public class ManufacturerController {

    private final static String COMPANY_NAME_EXISTS_ERROR = "The name \"%s\" is taken by an existing manufacturer.";

    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerController(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }


    /**
     * Create a manufacturer
     * @param manufacturerRequest the new manufacturer to create
     */
    @PostMapping(value = "create")
    public void createManufacturer(@Validated @RequestBody ManufacturerRequest manufacturerRequest,
                                   HttpServletResponse response) {

        if (manufacturerRepository.findByCompanyNameLowerIgnoreCase(manufacturerRequest.getCompanyName()) != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format(COMPANY_NAME_EXISTS_ERROR, manufacturerRequest.getCompanyName())
            );
        }
        manufacturerRepository.save(new Manufacturer(manufacturerRequest));
        response.setStatus(HttpServletResponse.SC_CREATED);
    }


    /**
     * Edit a manufacturer
     * @param manufacturerRequest the new manufacturer to create
     */
    @PutMapping(value = "edit",
                params = { "companyName" }
    )
    public void editManufacturer(@RequestParam(value="companyName") String companyName,
                                 @Validated @RequestBody ManufacturerRequest manufacturerRequest,
                                 HttpServletResponse response) {
        Manufacturer manufacturer = manufacturerRepository.findByCompanyNameLowerIgnoreCase(companyName);

        // Check if changed companyName conflicts
        if (!companyName.equals(manufacturerRequest.getCompanyName()) &&
                manufacturerRepository.findByCompanyNameLowerIgnoreCase(manufacturerRequest.getCompanyName()) != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format(COMPANY_NAME_EXISTS_ERROR, manufacturerRequest.getCompanyName())
            );
        }

        if (manufacturer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The manufacturer to edit can not be found");
        }

        manufacturer.edit(manufacturerRequest);

        manufacturerRepository.save(manufacturer);
        response.setStatus(HttpServletResponse.SC_OK);
    }


    /**
     * Get a manufacturer by companyName case insensitive.  Because company_name is unique in the database, there is
     * only ever one Manufacturer returned.
     * @param companyName name of the manufacturer to get
     * @return a Manufacturer with matching companyName
     */
    @GetMapping(value = "name",
                params = { "companyName" }
    )
    public ManufacturerResponse getManufacturerByNameIgnoreCase(@RequestParam(value="companyName") String companyName,
                                                                HttpServletResponse response) {
        Manufacturer manufacturer = manufacturerRepository.findByCompanyNameLowerIgnoreCase(companyName);

        if (manufacturer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Manufacturer named %s was not found", companyName));
        }

        response.setStatus(HttpServletResponse.SC_OK);
        return new ManufacturerResponse(manufacturer);
    }


    /**
     * Get a List of all manufacturer names in the Database.
     * @return a list of manufacturer names as strings
     */
    @GetMapping("all-names")
    public List<String> getAllManufacturerNames(HttpServletResponse response) {
        List<String> manufacturers = manufacturerRepository.getAllCompanyNames();

        response.setStatus(HttpServletResponse.SC_OK);
        return manufacturers;
    }


    /**
     * Get a List of all manufacturer names in the Database.
     * @return a list of manufacturer names as strings
     */
    @GetMapping("sidebar-list")
    public List<ManufacturerListItemResponse> getAllManufacturerListItems(HttpServletResponse response) {

        List<ManufacturerListItemResponse> manufacturersListItems = manufacturerRepository
                .findAll()
                .stream()
                .map(ManufacturerListItemResponse::new)
                .collect(Collectors.toList());

        response.setStatus(HttpServletResponse.SC_OK);
        return manufacturersListItems;
    }
}
