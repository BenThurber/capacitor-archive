package com.example.demo.repository;

import com.example.demo.model.CapacitorUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CapacitorUnitRepository extends JpaRepository<CapacitorUnit, Long> {

    CapacitorUnit findByCapacitanceAndVoltageAndIdentifier(Long capacitance, Integer voltage, String identifier);

    @Query(value =
            "SELECT * FROM capacitor_unit cu INNER JOIN capacitor_type ct ON cu.capacitor_type_id = ct.id " +
                    "INNER JOIN manufacturer m ON ct.manufacturer_id = m.id " +
                    "WHERE LOWER(:typeName) = ct.type_name_lower AND LOWER(:companyName) = m.company_name_lower " +
                    "AND :value = cu.value;",
            nativeQuery = true)
    CapacitorUnit findByTypeNameIgnoreCaseAndCompanyNameIgnoreCaseAndValue(@Param("companyName") String companyName,
                                                                           @Param("typeName") String typeName,
                                                                           @Param("value") String value);

}
