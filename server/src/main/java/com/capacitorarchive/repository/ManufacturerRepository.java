package com.capacitorarchive.repository;

import com.capacitorarchive.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ManufacturerRepository  extends JpaRepository<Manufacturer, Long> {

    Manufacturer findByCompanyNameLowerIgnoreCase(String companyName);

    @Query(value = "SELECT company_name FROM manufacturer", nativeQuery = true)
    List<String> getAllCompanyNames();

}
