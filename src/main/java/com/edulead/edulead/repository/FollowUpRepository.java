package com.edulead.edulead.repository;

import com.edulead.edulead.entity.FollowUp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {
    List<FollowUp> findByLeadIdOrderByFollowUpDateAsc(Long leadId);
    List<FollowUp> findAllByOrderByFollowUpDateAsc();
    List<FollowUp> findByFollowUpDateOrderByFollowUpDateAsc(LocalDate date);
}
