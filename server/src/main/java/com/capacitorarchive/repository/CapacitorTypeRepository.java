package com.capacitorarchive.repository;

import com.capacitorarchive.model.CapacitorType;
import com.capacitorarchive.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CapacitorTypeRepository extends JpaRepository<CapacitorType, Long> {

    CapacitorType findByTypeNameLowerIgnoreCaseAndManufacturer(String typeName, Manufacturer manufacturer);

    @Query(value =
            "SELECT * FROM capacitor_type c INNER JOIN manufacturer m ON c.manufacturer_id = m.id " +
            "WHERE LOWER(:typeName) = c.type_name_lower AND LOWER(:companyName) = m.company_name_lower;",
            nativeQuery = true)
    CapacitorType findByTypeNameIgnoreCaseAndCompanyNameIgnoreCase(@Param("typeName") String typeName,
                                                                   @Param("companyName") String companyName);
}
