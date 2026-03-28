package org.example.model;

import java.util.List;

public class Agent {

    private String id;
    private String name;
    private String domain;
    private String description;
    private String owner;
    private RiskTier riskTier;
    private Status status;
    private String model;
    private DataClassification dataClassification;
    private AutonomyLevel autonomyLevel;
    private ComplianceStatus complianceStatus;
    private String regulatoryImpact;
    private String sla;
    private List<String> keyCapabilities;
    private List<String> controls;
    private AgentType agentType;
    private String productApi;
    private List<String> orchestrates;

    public enum RiskTier { LOW, MEDIUM, HIGH, CRITICAL }
    public enum Status { ACTIVE, EXPERIMENTAL, RETIRED }
    public enum DataClassification { PUBLIC, INTERNAL, CONFIDENTIAL, RESTRICTED }
    public enum AutonomyLevel { LOW, MEDIUM, HIGH, FULL }
    public enum ComplianceStatus { APPROVED, PENDING, UNDER_REVIEW }
    public enum AgentType { AI_AGENT, API_WRAPPER, PRODUCT_API_AGENT, ORCHESTRATOR }

    private Agent() {}

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDomain() { return domain; }
    public String getDescription() { return description; }
    public String getOwner() { return owner; }
    public RiskTier getRiskTier() { return riskTier; }
    public Status getStatus() { return status; }
    public String getModel() { return model; }
    public DataClassification getDataClassification() { return dataClassification; }
    public AutonomyLevel getAutonomyLevel() { return autonomyLevel; }
    public ComplianceStatus getComplianceStatus() { return complianceStatus; }
    public String getRegulatoryImpact() { return regulatoryImpact; }
    public String getSla() { return sla; }
    public List<String> getKeyCapabilities() { return keyCapabilities; }
    public List<String> getControls() { return controls; }
    public AgentType getAgentType() { return agentType; }
    public String getProductApi() { return productApi; }
    public List<String> getOrchestrates() { return orchestrates; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Agent agent = new Agent();

        public Builder id(String v) { agent.id = v; return this; }
        public Builder name(String v) { agent.name = v; return this; }
        public Builder domain(String v) { agent.domain = v; return this; }
        public Builder description(String v) { agent.description = v; return this; }
        public Builder owner(String v) { agent.owner = v; return this; }
        public Builder riskTier(RiskTier v) { agent.riskTier = v; return this; }
        public Builder status(Status v) { agent.status = v; return this; }
        public Builder model(String v) { agent.model = v; return this; }
        public Builder dataClassification(DataClassification v) { agent.dataClassification = v; return this; }
        public Builder autonomyLevel(AutonomyLevel v) { agent.autonomyLevel = v; return this; }
        public Builder complianceStatus(ComplianceStatus v) { agent.complianceStatus = v; return this; }
        public Builder regulatoryImpact(String v) { agent.regulatoryImpact = v; return this; }
        public Builder sla(String v) { agent.sla = v; return this; }
        public Builder keyCapabilities(List<String> v) { agent.keyCapabilities = v; return this; }
        public Builder controls(List<String> v) { agent.controls = v; return this; }
        public Builder agentType(AgentType v) { agent.agentType = v; return this; }
        public Builder productApi(String v) { agent.productApi = v; return this; }
        public Builder orchestrates(List<String> v) { agent.orchestrates = v; return this; }
        public Agent build() { return agent; }
    }
}