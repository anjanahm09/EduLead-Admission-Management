package com.edulead.edulead.repository;

import com.edulead.edulead.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeadRepository extends JpaRepository<Lead, Long> {
    List<Lead> findByStatusIgnoreCase(String status);
    long countByStatusIgnoreCase(String status);
}
