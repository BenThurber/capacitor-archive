package com.capacitorarchive.repository;

import com.capacitorarchive.model.Construction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConstructionRepository extends JpaRepository<Construction, Long> {
    Construction findByConstructionName(String constructionName);
}
