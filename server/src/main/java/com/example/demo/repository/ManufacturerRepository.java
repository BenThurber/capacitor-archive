package com.example.demo.repository;

import com.example.demo.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ManufacturerRepository  extends JpaRepository<Manufacturer, Integer> {

    Manufacturer findById(Long id);
    Manufacturer findByCompanyNameIgnoreCase(String companyName);

}
