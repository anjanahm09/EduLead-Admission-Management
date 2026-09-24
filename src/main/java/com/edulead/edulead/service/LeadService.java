package com.edulead.edulead.service;

import com.edulead.edulead.entity.Lead;
import com.edulead.edulead.repository.LeadRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeadService {

    private final LeadRepository leadRepository;

    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    public List<Lead> getAll() {
        return leadRepository.findAll();
    }

    public Lead getById(Long id) {
        return leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + id));
    }

    public Lead create(Lead lead) {
        lead.setId(null);
        lead.setCreatedAt(LocalDateTime.now());
        lead.setUpdatedAt(LocalDateTime.now());
        if (lead.getStatus() == null || lead.getStatus().isBlank()) {
            lead.setStatus("NEW");
        }
        return leadRepository.save(lead);
    }

    public Lead update(Long id, Lead input) {
        Lead lead = getById(id);
        lead.setName(input.getName());
        lead.setEmail(input.getEmail());
        lead.setPhone(input.getPhone());
        lead.setSource(input.getSource());
        lead.setStatus(input.getStatus());
        lead.setLocation(input.getLocation());
        lead.setCourse(input.getCourse());
        lead.setCounsellor(input.getCounsellor());
        return leadRepository.save(lead);
    }

    public void delete(Long id) {
        if (!leadRepository.existsById(id)) {
            throw new RuntimeException("Lead not found with id: " + id);
        }
        leadRepository.deleteById(id);
    }
}
