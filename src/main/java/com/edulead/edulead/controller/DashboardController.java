package com.edulead.edulead.controller;

import com.edulead.edulead.entity.Lead;
import com.edulead.edulead.repository.LeadRepository;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final LeadRepository leadRepository;

    public DashboardController(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    @GetMapping
    public Map<String, Object> dashboard() {
        List<Lead> leads = leadRepository.findAll();

        long normal = 0;
        long ageing = 0;
        long attention = 0;

        for (Lead lead : leads) {
            if (lead.getCreatedAt() == null) continue;
            long days = Duration.between(lead.getCreatedAt(), LocalDateTime.now()).toDays();
            if (days <= 2) normal++;
            else if (days <= 7) ageing++;
            else attention++;
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalLeads", leads.size());
        result.put("newLeads", leadRepository.countByStatusIgnoreCase("NEW"));
        result.put("contactedLeads", leadRepository.countByStatusIgnoreCase("CONTACTED"));
        result.put("followUpLeads", leadRepository.countByStatusIgnoreCase("FOLLOW_UP"));
        result.put("interestedLeads", leadRepository.countByStatusIgnoreCase("INTERESTED"));
        result.put("applicationLeads", leadRepository.countByStatusIgnoreCase("APPLICATION"));
        result.put("convertedLeads", leadRepository.countByStatusIgnoreCase("CONVERTED"));
        result.put("notInterestedLeads", leadRepository.countByStatusIgnoreCase("NOT_INTERESTED"));
        result.put("lostLeads", leadRepository.countByStatusIgnoreCase("LOST"));
        result.put("normalAgeing", normal);
        result.put("ageing", ageing);
        result.put("attentionRequired", attention);
        return result;
    }
}
