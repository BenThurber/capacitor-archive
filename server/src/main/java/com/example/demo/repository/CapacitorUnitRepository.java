package com.example.demo.repository;

import com.example.demo.model.CapacitorUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapacitorUnitRepository extends JpaRepository<CapacitorUnit, Long> {

    CapacitorUnit findByCapacitanceAndVoltageAndIdentifier(Long capacitance, Integer voltage, String identifier);

}
