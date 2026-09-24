package com.edulead.edulead.controller;

import com.edulead.edulead.entity.Lead;
import com.edulead.edulead.service.LeadService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @GetMapping
    public List<Lead> getAll() {
        return leadService.getAll();
    }

    @GetMapping("/{id}")
    public Lead getById(@PathVariable Long id) {
        return leadService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Lead create(@RequestBody Lead lead) {
        return leadService.create(lead);
    }

    @PutMapping("/{id}")
    public Lead update(@PathVariable Long id, @RequestBody Lead lead) {
        return leadService.update(id, lead);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        leadService.delete(id);
    }
}
