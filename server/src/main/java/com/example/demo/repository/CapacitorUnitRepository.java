package com.example.demo.repository;

import com.example.demo.model.CapacitorUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CapacitorUnitRepository extends JpaRepository<CapacitorUnit, Long> {

    CapacitorUnit findByCapacitanceAndVoltageAndIdentifier(Long capacitance, Integer voltage, String identifier);

    /**
     * Gets a weak entity CapacitorUnit using its identifying entities typeName and companyName.
     *
     * NOTE: MySQL Queries are case insensitive by default.  This is why in the query here we can compare
     * :typeName = ct.type_name without having to use LOWER() or UPPER().  They keyword BINARY is used to make a case
     * sensitive query as used for value.
     * @param companyName Manufacturer owning the CapacitorType of the CapacitorUnit
     * @param typeName The CapacitorType owning the CapacitorUnit
     * @param value The value of the CapacitorUnit (Unique Key)
     * @return A unique CapacitorUnit
     */
    @Query(value =
            "SELECT * FROM capacitor_unit cu INNER JOIN capacitor_type ct ON cu.capacitor_type_id = ct.id " +
                    "INNER JOIN manufacturer m ON ct.manufacturer_id = m.id " +
                    "WHERE :typeName = ct.type_name AND :companyName = m.company_name AND BINARY :value = cu.value;",
            nativeQuery = true)
    CapacitorUnit findByTypeNameIgnoreCaseAndCompanyNameIgnoreCaseAndValue(@Param("companyName") String companyName,
                                                                           @Param("typeName") String typeName,
                                                                           @Param("value") String value);

    @Query(value =
            "SELECT * FROM capacitor_unit cu INNER JOIN capacitor_type ct ON cu.capacitor_type_id = ct.id " +
                    "INNER JOIN manufacturer m ON ct.manufacturer_id = m.id " +
                    "WHERE :typeName = ct.type_name AND :companyName = m.company_name;",
            nativeQuery = true)
    List<CapacitorUnit> findAllByTypeNameIgnoreCaseAndCompanyNameIgnoreCase(@Param("companyName") String companyName,
                                                                            @Param("typeName") String typeName);

}
