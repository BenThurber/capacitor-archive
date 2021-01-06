package com.example.demo.repository;

import com.example.demo.model.Construction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConstructionRepository extends JpaRepository<Construction, Integer> {
}
