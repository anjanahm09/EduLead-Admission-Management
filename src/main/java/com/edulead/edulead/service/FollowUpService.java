package com.edulead.edulead.service;

import com.edulead.edulead.entity.FollowUp;
import com.edulead.edulead.repository.FollowUpRepository;
import com.edulead.edulead.repository.LeadRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FollowUpService {

    private final FollowUpRepository followUpRepository;
    private final LeadRepository leadRepository;

    public FollowUpService(FollowUpRepository followUpRepository, LeadRepository leadRepository) {
        this.followUpRepository = followUpRepository;
        this.leadRepository = leadRepository;
    }

    public List<FollowUp> getAll() {
        return followUpRepository.findAllByOrderByFollowUpDateAsc();
    }

    public FollowUp create(FollowUp followUp) {
        if (!leadRepository.existsById(followUp.getLeadId())) {
            throw new RuntimeException("Lead not found with id: " + followUp.getLeadId());
        }
        if (followUp.getStatus() == null || followUp.getStatus().isBlank()) {
            followUp.setStatus("PENDING");
        }
        return followUpRepository.save(followUp);
    }

    public FollowUp update(Long id, FollowUp input) {
        FollowUp followUp = followUpRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Follow-up not found with id: " + id));
        if (!leadRepository.existsById(input.getLeadId())) {
            throw new RuntimeException("Lead not found with id: " + input.getLeadId());
        }
        followUp.setLeadId(input.getLeadId());
        followUp.setFollowUpDate(input.getFollowUpDate());
        followUp.setAction(input.getAction());
        followUp.setNotes(input.getNotes());
        followUp.setStatus(input.getStatus());
        return followUpRepository.save(followUp);
    }

    public void delete(Long id) {
        followUpRepository.deleteById(id);
    }

    public List<FollowUp> getByLead(Long leadId) {
        return followUpRepository.findByLeadIdOrderByFollowUpDateAsc(leadId);
    }

    public List<FollowUp> getToday() {
        return followUpRepository.findByFollowUpDateOrderByFollowUpDateAsc(LocalDate.now());
    }
}
