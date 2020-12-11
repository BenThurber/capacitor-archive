package com.example.demo.repository;

import com.example.demo.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository  extends JpaRepository<Manufacturer, Integer> {

    Manufacturer findById(Long id);
    Manufacturer findByCompanyNameLowerIgnoreCase(String companyName);

}
