package com.example.demo.repository;

import com.example.demo.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ManufacturerRepository  extends JpaRepository<Manufacturer, Integer> {

    Manufacturer findByCompanyNameLowerIgnoreCase(String companyName);

    @Query(value = "SELECT company_name FROM manufacturer", nativeQuery = true)
    List<String> getAllCompanyNames();

}
