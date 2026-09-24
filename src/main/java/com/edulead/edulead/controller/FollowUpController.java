package com.edulead.edulead.controller;

import com.edulead.edulead.entity.FollowUp;
import com.edulead.edulead.service.FollowUpService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/followups")
public class FollowUpController {

    private final FollowUpService followUpService;

    public FollowUpController(FollowUpService followUpService) {
        this.followUpService = followUpService;
    }

    @GetMapping
    public List<FollowUp> getAll() {
        return followUpService.getAll();
    }

    @GetMapping("/lead/{leadId}")
    public List<FollowUp> getByLead(@PathVariable Long leadId) {
        return followUpService.getByLead(leadId);
    }

    @GetMapping("/today")
    public List<FollowUp> getToday() {
        return followUpService.getToday();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FollowUp create(@RequestBody FollowUp followUp) {
        return followUpService.create(followUp);
    }

    @PutMapping("/{id}")
    public FollowUp update(@PathVariable Long id, @RequestBody FollowUp followUp) {
        return followUpService.update(id, followUp);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        followUpService.delete(id);
    }
}
