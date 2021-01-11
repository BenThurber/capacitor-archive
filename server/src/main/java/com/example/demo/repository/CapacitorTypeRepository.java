package com.example.demo.repository;

import com.example.demo.model.CapacitorType;
import com.example.demo.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapacitorTypeRepository extends JpaRepository<CapacitorType, Long> {

    CapacitorType findByTypeNameLowerIgnoreCaseAndManufacturer(String typeName, Manufacturer manufacturer);
}
