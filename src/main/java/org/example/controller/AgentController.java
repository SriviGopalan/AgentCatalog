package org.example.controller;

import org.example.model.Agent;
import org.example.model.Domain;
import org.example.service.AgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/agents")
    public List<Agent> getAgents(
            @RequestParam(required = false) String domain,
            @RequestParam(required = false) String riskTier,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String dataClassification,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String complianceStatus,
            @RequestParam(required = false) String agentType,
            @RequestParam(required = false) String search) {
        return agentService.getAgents(domain, riskTier, status, dataClassification, model, complianceStatus, agentType, search);
    }

    @GetMapping("/agents/{id}")
    public ResponseEntity<Agent> getAgentById(@PathVariable String id) {
        return agentService.getAgentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/domains")
    public List<Domain> getDomains() {
        return agentService.getDomains();
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return agentService.getStats();
    }
}
