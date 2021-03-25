package com.example.demo.controller;

import com.example.demo.model.CapacitorType;
import com.example.demo.model.CapacitorUnit;
import com.example.demo.model.Photo;
import com.example.demo.payload.request.CapacitorUnitRequest;
import com.example.demo.payload.response.CapacitorUnitResponse;
import com.example.demo.repository.CapacitorTypeRepository;
import com.example.demo.repository.CapacitorUnitRepository;
import com.example.demo.repository.PhotoRepository;
import com.example.demo.repository.ThumbnailRepository;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/unit")
public class CapacitorUnitController {

    private final static String PARENT_TYPE_NOT_FOUND_ERROR = "The CapacitorUnit references a CapacitorType \"%s\" that does not exist.";
    private final static String UNIT_NAME_EXISTS_ERROR = "The CapacitorUnit with the value (%s) already exists for the CapacitorType \"%s\"";
    private final static String UNIT_NOT_FOUND_ERROR = "The CapacitorUnit with companyName %s, typeName %s and value %s could not be found.";
    private final static String PHOTOS_ERROR = "Error saving photos and thumbnails to the database.";

    private final CapacitorTypeRepository capacitorTypeRepository;
    private final CapacitorUnitRepository capacitorUnitRepository;
    private final PhotoRepository photoRepository;
    private final ThumbnailRepository thumbnailRepository;

    CapacitorUnitController(CapacitorTypeRepository capacitorTypeRepository,
                            CapacitorUnitRepository capacitorUnitRepository,
                            PhotoRepository photoRepository,
                            ThumbnailRepository thumbnailRepository) {
        this.capacitorTypeRepository = capacitorTypeRepository;
        this.capacitorUnitRepository = capacitorUnitRepository;
        this.photoRepository = photoRepository;
        this.thumbnailRepository = thumbnailRepository;
    }


    /**
     * Create a CapacitorUnit
     * @param capacitorUnitRequest the new CapacitorUnit to create
     */
    @PostMapping(value = "create")
    public CapacitorUnitResponse createCapacitorUnit(@Validated @RequestBody CapacitorUnitRequest capacitorUnitRequest,
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

        try {
            savePhotosAndThumbnails(newCapacitorUnit, capacitorUnitRequest);
        } catch (Exception e) {
            // If there is an error with photos, un-save the capacitor unit
            this.capacitorUnitRepository.delete(newCapacitorUnit);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    PHOTOS_ERROR
            );
        }

        response.setStatus(HttpServletResponse.SC_CREATED);
        return new CapacitorUnitResponse(newCapacitorUnit);
    }


    /**
     * Edit a CapacitorUnit
     * @param capacitorUnitRequest the new CapacitorUnit to create
     */
    @PutMapping(value = "edit",
                params = { "companyName", "typeName", "value" }
    )
    public CapacitorUnitResponse editCapacitorUnit(@Validated
                                    @RequestBody CapacitorUnitRequest capacitorUnitRequest,
                                    @RequestParam(value="companyName") String companyName,
                                    @RequestParam(value="typeName") String typeName,
                                    @RequestParam(value="value") String value,
                                    HttpServletResponse response) {

        CapacitorUnit capacitorUnit = capacitorUnitRepository.findByTypeNameIgnoreCaseAndCompanyNameIgnoreCaseAndValue(
                companyName, typeName, value
        );

        if (capacitorUnit == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The capacitor unit to edit can not be found");
        }

        capacitorUnit.edit(capacitorUnitRequest);
        savePhotosAndThumbnails(capacitorUnit, capacitorUnitRequest);

        try {
            capacitorUnitRepository.save(capacitorUnit);

            // Catch Duplicate Entry
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format(UNIT_NAME_EXISTS_ERROR,
                            capacitorUnit,
                            capacitorUnit.getCapacitorType().getTypeName())
            );
        }

        response.setStatus(HttpServletResponse.SC_OK);
        return new CapacitorUnitResponse(capacitorUnit);
    }


    /**
     * Helper function to save all Thumbnails and Photos to the database and attach them to parentCapacitorUnit
     * @param parentCapacitorUnit the CapacitorUnit to attach photos to
     * @param capacitorUnitRequest the request with photoRequests
     */
    private void savePhotosAndThumbnails(CapacitorUnit parentCapacitorUnit, CapacitorUnitRequest capacitorUnitRequest) {
        Set<Photo> newPhotos = capacitorUnitRequest.getPhotos().stream().map(Photo::new).collect(Collectors.toSet());
        newPhotos.forEach(photo -> photo.setCapacitorUnit(parentCapacitorUnit));
        Set<Photo> currentPhotos = new HashSet<>(parentCapacitorUnit.getPhotos());
        currentPhotos.removeAll(newPhotos);
        this.photoRepository.deleteAll(currentPhotos);

        parentCapacitorUnit.setPhotos(newPhotos);
        this.photoRepository.saveAll(newPhotos);

        newPhotos.forEach(photo -> this.thumbnailRepository.saveAll(photo.getThumbnails()));
    }


    @GetMapping(value = "name",
                params = { "companyName", "typeName", "value" }
    )
    public CapacitorUnitResponse getCapacitorUnitByValue(@RequestParam(value="companyName") String companyName,
                                                         @RequestParam(value="typeName") String typeName,
                                                         @RequestParam(value="value") String value,
                                                         HttpServletResponse response) {

        CapacitorUnit capacitorUnit = capacitorUnitRepository.findByTypeNameIgnoreCaseAndCompanyNameIgnoreCaseAndValue(
                companyName, typeName, value
        );

        if (capacitorUnit == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format(UNIT_NOT_FOUND_ERROR, companyName, typeName, value)
            );
        }

        response.setStatus(HttpServletResponse.SC_OK);
        return new CapacitorUnitResponse(capacitorUnit);

    }

    /**
     * Get List of all CapacitorTypeResponses from a Manufacturer, given companyName.
     * @param companyName name of the owning Manufacturer.
     * @return the list of found type, empty list if none.
     */
    @GetMapping(value = "all",
                params = { "companyName", "typeName" })
    public List<CapacitorUnitResponse> getAllCapacitorUnitsFromCapacitorType(
                                                       @RequestParam(value="companyName") String companyName,
                                                       @RequestParam(value="typeName") String typeName,
                                                       HttpServletResponse response) {

        List<CapacitorUnit> capacitorUnits = this.capacitorUnitRepository.findAllByTypeNameIgnoreCaseAndCompanyNameIgnoreCase(
                companyName, typeName
        );

        List<CapacitorUnitResponse> capacitorTypeResponses = capacitorUnits
                .stream()
                .map(CapacitorUnitResponse::new)
                .collect(Collectors.toList());

        response.setStatus(HttpServletResponse.SC_OK);
        return capacitorTypeResponses;
    }

}
