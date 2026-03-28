package org.example.service;

import org.example.model.Agent;
import org.example.model.Agent.AgentType;
import org.example.model.Agent.RiskTier;
import org.example.model.Agent.Status;
import org.example.model.Agent.DataClassification;
import org.example.model.Agent.AutonomyLevel;
import org.example.model.Agent.ComplianceStatus;
import org.example.model.Domain;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgentService {

    private final List<Agent> agents;

    public AgentService() {
        agents = new ArrayList<>();
        loadAgents();
    }

    private void loadAgents() {

        // ── 1. RETAIL BANKING ────────────────────────────────────────────────
        agents.add(Agent.builder()
                .id("ret-orch-001").name("Retail Onboarding Orchestrator").domain("Retail Banking")
                .description("Coordinates the end-to-end new customer onboarding journey: KYC verification → credit check → core banking account creation → welcome communication.")
                .owner("Retail Banking - Digital").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("Claude Sonnet").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.ORCHESTRATOR)
                .orchestrates(List.of("KYC Verification Agent", "Core Banking API Agent", "Loan Origination Agent", "Branch Locator Agent"))
                .regulatoryImpact("CIP/BSA, UDAAP, Reg B").sla("99.9% / <3 min end-to-end")
                .keyCapabilities(List.of("Multi-agent pipeline coordination", "State machine orchestration", "Fallback and retry logic", "Audit trail generation"))
                .controls(List.of("Human review for KYC failures", "Compliance sign-off for new corridors", "Quarterly orchestration logic audit")).build());

        agents.add(Agent.builder()
                .id("ret-001").name("KYC Verification Agent").domain("Retail Banking")
                .description("Performs AI-driven identity verification using document OCR, biometric liveness checks, and sanctions list screening.")
                .owner("Retail Banking - Compliance").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("Claude Sonnet").dataClassification(DataClassification.RESTRICTED)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("BSA/AML, CIP Rule, OFAC").sla("99.9% / <30s verification")
                .keyCapabilities(List.of("Document OCR and authenticity check", "Biometric liveness detection", "OFAC/PEP/sanctions screening", "Risk score generation"))
                .controls(List.of("Manual review for risk score >70", "OFAC hit escalation workflow", "Annual CIP policy review")).build());

        agents.add(Agent.builder()
                .id("ret-002").name("Core Banking API Agent").domain("Retail Banking")
                .description("Agent wrapper over the Temenos T24 core banking system — exposes account creation, balance queries, and transaction posting as agent-callable tools.")
                .owner("Retail Banking - Technology").riskTier(RiskTier.CRITICAL).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.RESTRICTED)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.PRODUCT_API_AGENT)
                .productApi("Temenos T24 Core Banking")
                .regulatoryImpact("Reg E, Reg D, FFIEC").sla("99.99% / <100ms p99")
                .keyCapabilities(List.of("Account open/close via T24 API", "Real-time balance retrieval", "Transaction posting and reversal", "Standing order management"))
                .controls(List.of("Change control for T24 API upgrades", "Dual-control for high-value postings", "Daily reconciliation to core ledger")).build());

        agents.add(Agent.builder()
                .id("ret-003").name("Loan Origination Agent").domain("Retail Banking")
                .description("Agent wrapper over Finastra Fusion for consumer loan origination — pre-qualification, decisioning, and loan booking via API calls.")
                .owner("Consumer Lending").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.PRODUCT_API_AGENT)
                .productApi("Finastra Fusion Loan IQ")
                .regulatoryImpact("ECOA Reg B, TILA, HMDA").sla("99.9% / <5 min decisioning")
                .keyCapabilities(List.of("Credit bureau pull via Finastra connector", "Automated decisioning rules engine", "Loan booking and disbursement", "Adverse action notice generation"))
                .controls(List.of("Reg B adverse action review", "Fair lending model audit quarterly", "Manual review for borderline decisions")).build());

        agents.add(Agent.builder()
                .id("ret-004").name("Branch Locator Agent").domain("Retail Banking")
                .description("Wraps the Google Maps Platform API to provide intelligent branch and ATM recommendations based on customer location, services needed, and wait times.")
                .owner("Retail Distribution").riskTier(RiskTier.LOW).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.PUBLIC)
                .autonomyLevel(AutonomyLevel.LOW).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.API_WRAPPER)
                .productApi("Google Maps Platform API")
                .regulatoryImpact("None").sla("99.5% / <500ms")
                .keyCapabilities(List.of("Geo-proximity search", "Service availability filtering", "Real-time wait time display", "Directions generation"))
                .controls(List.of("API key rotation every 90 days", "Monthly cost and usage review")).build());

        // ── 2. WEALTH MANAGEMENT ─────────────────────────────────────────────
        agents.add(Agent.builder()
                .id("wlth-orch-001").name("Wealth Advisory Orchestrator").domain("Wealth Management")
                .description("Coordinates the full wealth advisory workflow: market data ingestion → portfolio analysis → risk profiling → rebalancing execution → client report generation.")
                .owner("Wealth Management - Digital").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("Claude Sonnet").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.ORCHESTRATOR)
                .orchestrates(List.of("Portfolio Analyzer Agent", "Market Data Feed Agent", "Risk Profiling Agent", "Rebalancing Execution Agent"))
                .regulatoryImpact("SEC IA Act, Reg BI, FINRA").sla("99.9% / daily advisory cycle")
                .keyCapabilities(List.of("Advisory pipeline orchestration", "Market condition gating", "Suitability pre-check before rebalancing", "Client notification coordination"))
                .controls(List.of("Advisor review required before execution", "Annual Reg BI best-interest attestation", "CIO approval for strategy changes")).build());

        agents.add(Agent.builder()
                .id("wlth-001").name("Portfolio Analyzer Agent").domain("Wealth Management")
                .description("AI agent that analyzes portfolio composition, drift from target allocation, tax-loss harvesting opportunities, and performance attribution.")
                .owner("Wealth - Investment Management").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("Claude Sonnet").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("SEC IA Act, Reg BI").sla("99.9% / T+1 analysis")
                .keyCapabilities(List.of("Asset allocation drift detection", "Tax-loss harvesting identification", "Performance attribution (Brinson)", "Risk-adjusted return metrics"))
                .controls(List.of("Investment policy statement compliance check", "Annual suitability review")).build());

        agents.add(Agent.builder()
                .id("wlth-002").name("Market Data Feed Agent").domain("Wealth Management")
                .description("Wraps Bloomberg Terminal API to stream real-time and historical price data, economic indicators, and earnings estimates to downstream agents.")
                .owner("Wealth - Technology").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.LOW).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.API_WRAPPER)
                .productApi("Bloomberg Terminal API (B-PIPE)")
                .regulatoryImpact("None").sla("99.99% / real-time streaming")
                .keyCapabilities(List.of("Real-time price streaming", "Historical OHLCV retrieval", "Earnings and dividend data", "Economic indicator feeds"))
                .controls(List.of("Bloomberg license compliance", "API usage cost monitoring", "Failover to Reuters on outage")).build());

        agents.add(Agent.builder()
                .id("wlth-003").name("Risk Profiling Agent").domain("Wealth Management")
                .description("AI agent that dynamically assesses client risk tolerance using behavioral finance signals, life events, and portfolio stress test outcomes.")
                .owner("Wealth - Client Experience").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("Claude Sonnet").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.LOW).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("Reg BI, FINRA 2111").sla("99.9% / <2 min profiling")
                .keyCapabilities(List.of("Behavioral finance questionnaire analysis", "Life event risk adjustment", "Stress test scenario exposure", "Risk tolerance change detection"))
                .controls(List.of("Advisor override capability", "Client acknowledgment required", "Annual profile refresh trigger")).build());

        agents.add(Agent.builder()
                .id("wlth-004").name("Rebalancing Execution Agent").domain("Wealth Management")
                .description("Connects to Charles River IMS to execute portfolio rebalancing orders — generates trade tickets, checks compliance rules, and submits to OMS.")
                .owner("Wealth - Trading Operations").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.RESTRICTED)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.PRODUCT_API_AGENT)
                .productApi("Charles River Investment Management System (IMS)")
                .regulatoryImpact("SEC Best Execution, Reg BI").sla("99.99% / T+0 order submission")
                .keyCapabilities(List.of("Trade order generation via CRD API", "Pre-trade compliance check", "OMS submission and tracking", "Wash sale rule enforcement"))
                .controls(List.of("Pre-trade compliance gate mandatory", "Advisor approval for >$100K trades", "Post-trade reporting to compliance")).build());

        // ── 3. COMMERCIAL & INDUSTRIAL ───────────────────────────────────────
        agents.add(Agent.builder()
                .id("ci-orch-001").name("Corporate Credit Orchestrator").domain("Commercial & Industrial")
                .description("Orchestrates C&I credit origination: financial spreading → credit risk scoring → syndication market check → SAP booking → treasury notification.")
                .owner("Commercial Banking - Credit").riskTier(RiskTier.CRITICAL).status(Status.ACTIVE)
                .model("Claude Sonnet").dataClassification(DataClassification.RESTRICTED)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.ORCHESTRATOR)
                .orchestrates(List.of("Trade Finance Agent", "Corporate Banking API Agent", "Syndication Market Agent", "Treasury Management Agent"))
                .regulatoryImpact("Reg Y, OCC Lending Guidelines, Basel III").sla("99.9% / <48h credit decision")
                .keyCapabilities(List.of("Credit workflow orchestration", "Parallel agent invocation for data gathering", "Credit memo assembly", "Approval routing automation"))
                .controls(List.of("Credit Committee approval for >$10M", "CRO sign-off for leveraged loans", "Annual portfolio review")).build());

        agents.add(Agent.builder()
                .id("ci-001").name("Trade Finance Agent").domain("Commercial & Industrial")
                .description("AI agent that analyzes trade finance documents (LCs, bills of lading, invoices) for compliance, discrepancies, and fraud signals.")
                .owner("Commercial Banking - Trade Finance").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("GPT-4o").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("UCP 600, OFAC, BIS Export Controls").sla("99.5% / <4h document review")
                .keyCapabilities(List.of("LC document discrepancy detection", "Dual-use goods screening", "Counterparty sanctions check", "Fraud pattern recognition"))
                .controls(List.of("Trade ops specialist review for discrepancies", "OFAC escalation workflow", "Annual UCP training for model")).build());

        agents.add(Agent.builder()
                .id("ci-002").name("Corporate Banking API Agent").domain("Commercial & Industrial")
                .description("Agent wrapper over SAP S/4HANA Banking module for corporate account management, facility booking, and covenant tracking via API.")
                .owner("Commercial Banking - Technology").riskTier(RiskTier.CRITICAL).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.RESTRICTED)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.PRODUCT_API_AGENT)
                .productApi("SAP S/4HANA Banking & Loans Management")
                .regulatoryImpact("Reg Y, OCC Lending, SOX").sla("99.99% / <200ms")
                .keyCapabilities(List.of("Credit facility booking in SAP", "Covenant breach monitoring", "Draw-down and repayment processing", "Corporate account ledger queries"))
                .controls(List.of("SAP authorization matrix enforcement", "Dual approval for facility amendments", "Monthly SAP audit log review")).build());

        agents.add(Agent.builder()
                .id("ci-003").name("Syndication Market Agent").domain("Commercial & Industrial")
                .description("Wraps the Markit LoanConnector API to access real-time syndicated loan pricing, allocations, and secondary market data.")
                .owner("Syndications Desk").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.LOW).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.API_WRAPPER)
                .productApi("Markit LoanConnector API")
                .regulatoryImpact("Reg U, OCC Leveraged Lending").sla("99.5% / real-time pricing")
                .keyCapabilities(List.of("Real-time LIBOR/SOFR spread data", "Loan allocation messaging", "Secondary market price discovery", "Trade settlement via LSTA"))
                .controls(List.of("Markit entitlement management", "Trading desk approval for pricing decisions")).build());

        agents.add(Agent.builder()
                .id("ci-004").name("Treasury Management Agent").domain("Commercial & Industrial")
                .description("AI agent that analyzes corporate client cash flow patterns and recommends treasury products (sweeps, repo, FX hedges) to optimize liquidity.")
                .owner("Commercial Banking - Treasury Solutions").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("Claude Sonnet").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("Reg D, Dodd-Frank Title VII").sla("99.9% / daily cash position")
                .keyCapabilities(List.of("Cash flow forecasting", "Sweep account optimization", "FX exposure identification", "Liquidity product recommendation"))
                .controls(List.of("Relationship manager approval for new products", "Annual treasury review")).build());

        // ── 4. MARKETING ─────────────────────────────────────────────────────
        agents.add(Agent.builder()
                .id("mkt-orch-001").name("Campaign Orchestrator").domain("Marketing")
                .description("Coordinates multi-channel marketing campaigns: segment customers → personalize offers → push via CRM → listen for social signals → measure attribution.")
                .owner("Marketing - Digital").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("GPT-4o").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.ORCHESTRATOR)
                .orchestrates(List.of("Personalization Engine", "CRM Integration Agent", "Customer Segmentation Agent", "Social Listening Agent"))
                .regulatoryImpact("UDAAP, CAN-SPAM, CCPA").sla("99.5% / campaign SLA <2h activation")
                .keyCapabilities(List.of("Campaign workflow sequencing", "A/B test coordination", "Channel budget allocation", "Attribution modeling"))
                .controls(List.of("Marketing compliance review before launch", "UDAAP fairness check", "Monthly campaign performance audit")).build());

        agents.add(Agent.builder()
                .id("mkt-001").name("Personalization Engine").domain("Marketing")
                .description("AI agent that generates hyper-personalized product offers using customer 360 data, behavioral signals, and propensity models.")
                .owner("Marketing - Analytics").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("GPT-4o").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("UDAAP, Fair Lending, CCPA").sla("99.5% / <200ms p99")
                .keyCapabilities(List.of("Next-best-offer prediction", "Channel preference routing", "Offer eligibility pre-screening", "Real-time segmentation"))
                .controls(List.of("Fair lending review quarterly", "UDAAP officer approval for new offer types", "Opt-out enforcement")).build());

        agents.add(Agent.builder()
                .id("mkt-002").name("CRM Integration Agent").domain("Marketing")
                .description("Wraps Salesforce Marketing Cloud API to automate journey activation, list management, and campaign performance data retrieval.")
                .owner("Marketing - Technology").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.API_WRAPPER)
                .productApi("Salesforce Marketing Cloud API")
                .regulatoryImpact("CAN-SPAM, CCPA, GDPR").sla("99.5% / <1s API response")
                .keyCapabilities(List.of("Journey builder API activation", "Contact list segmentation sync", "Email/SMS send via Journey Builder", "Open/click analytics retrieval"))
                .controls(List.of("Opt-out list synchronization every 15min", "CCPA data deletion workflow", "Annual Marketing Cloud security review")).build());

        agents.add(Agent.builder()
                .id("mkt-003").name("Customer Segmentation Agent").domain("Marketing")
                .description("AI agent that dynamically clusters customers into behavioral and value segments using unsupervised ML and lifecycle signals.")
                .owner("Marketing - Analytics").riskTier(RiskTier.LOW).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("Fair Lending, ECOA").sla("Daily batch / <4h run")
                .keyCapabilities(List.of("RFM segmentation", "Lifecycle stage classification", "Propensity-to-buy scoring", "Segment migration tracking"))
                .controls(List.of("Bias audit for protected classes", "Quarterly model retraining", "Business owner segment sign-off")).build());

        agents.add(Agent.builder()
                .id("mkt-004").name("Social Listening Agent").domain("Marketing")
                .description("Wraps Sprinklr API to monitor brand mentions, sentiment trends, and competitive signals across social media channels in real time.")
                .owner("Marketing - Brand").riskTier(RiskTier.LOW).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.INTERNAL)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.API_WRAPPER)
                .productApi("Sprinklr Unified CXM API")
                .regulatoryImpact("None").sla("99% / <15min sentiment refresh")
                .keyCapabilities(List.of("Brand mention tracking", "Sentiment classification", "Competitor share-of-voice analysis", "Viral risk alerting"))
                .controls(List.of("Communications team review of escalations", "Annual social media policy review")).build());

        // ── 5. CUSTOMER EXPERIENCE ────────────────────────────────────────────
        agents.add(Agent.builder()
                .id("cx-orch-001").name("Customer Journey Orchestrator").domain("Customer Experience")
                .description("Coordinates the customer interaction lifecycle: conversational AI → CRM update → sentiment analysis → complaint triage → escalation or resolution.")
                .owner("Digital Channels - CX").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("Claude Sonnet").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.ORCHESTRATOR)
                .orchestrates(List.of("Conversational Assistant (Aria)", "CRM Service Agent", "Sentiment Analysis Agent", "Complaint Management Agent"))
                .regulatoryImpact("CFPB, UDAAP, Reg E").sla("99.9% / <500ms orchestration overhead")
                .keyCapabilities(List.of("Intent-driven agent routing", "Context preservation across handoffs", "Escalation threshold management", "CSAT trigger automation"))
                .controls(List.of("CFPB complaint SLA monitoring", "Monthly journey analytics review", "Customer consent management")).build());

        agents.add(Agent.builder()
                .id("cx-001").name("Conversational Assistant (Aria)").domain("Customer Experience")
                .description("Omni-channel AI banking assistant handling balance inquiries, fund transfers, product FAQs, complaint triage, and card management.")
                .owner("Digital Channels").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("Claude Sonnet").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("CFPB, Reg E, UDAAP").sla("99.9% / <500ms response")
                .keyCapabilities(List.of("NLU intent classification", "Account servicing actions", "Fund transfer execution", "Escalation to live agent"))
                .controls(List.of("PII masking in logs", "Escalation threshold monitoring", "Monthly intent accuracy review")).build());

        agents.add(Agent.builder()
                .id("cx-002").name("CRM Service Agent").domain("Customer Experience")
                .description("Agent wrapper over Salesforce Service Cloud — creates cases, updates customer records, and retrieves interaction history via API.")
                .owner("CX - Technology").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.PRODUCT_API_AGENT)
                .productApi("Salesforce Service Cloud API")
                .regulatoryImpact("CCPA, CFPB record retention").sla("99.9% / <300ms API response")
                .keyCapabilities(List.of("Case creation and routing via SF API", "Customer 360 profile retrieval", "Interaction history logging", "SLA timer management"))
                .controls(List.of("CCPA data access logging", "Salesforce profile minimum privilege review", "Annual CRM security audit")).build());

        agents.add(Agent.builder()
                .id("cx-003").name("Sentiment Analysis Agent").domain("Customer Experience")
                .description("AI agent that analyzes customer messages, call transcripts, and survey responses for sentiment, frustration signals, and churn indicators.")
                .owner("CX Analytics").riskTier(RiskTier.LOW).status(Status.ACTIVE)
                .model("Claude Sonnet").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("None").sla("99% / real-time scoring")
                .keyCapabilities(List.of("Real-time sentiment scoring", "Frustration escalation trigger", "Topic and theme extraction", "CSAT prediction"))
                .controls(List.of("CX leadership review of escalated sessions", "Monthly model accuracy audit")).build());

        agents.add(Agent.builder()
                .id("cx-004").name("Complaint Management Agent").domain("Customer Experience")
                .description("Agent wrapper over ServiceNow to auto-create, route, and track CFPB-reportable complaints with SLA enforcement and regulatory escalation.")
                .owner("CX - Complaints & Disputes").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.PRODUCT_API_AGENT)
                .productApi("ServiceNow Customer Service Management API")
                .regulatoryImpact("CFPB Regulation, UDAAP, Reg E dispute").sla("100% / CFPB 15-day acknowledgment SLA")
                .keyCapabilities(List.of("Complaint intake and classification", "CFPB-reportable flag and routing", "SLA countdown automation", "Regulatory response drafting"))
                .controls(List.of("CCO dashboard for open complaints", "CFPB SLA breach alert", "Legal review for pattern complaints")).build());

        // ── 6. HUMAN RESOURCES ───────────────────────────────────────────────
        agents.add(Agent.builder()
                .id("hr-orch-001").name("HR Onboarding Orchestrator").domain("Human Resources")
                .description("Coordinates new employee onboarding: background check → Workday provisioning → policy acknowledgment → payroll enrollment → IT access setup.")
                .owner("HR - People Operations").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("GPT-4o").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.ORCHESTRATOR)
                .orchestrates(List.of("Talent Acquisition Agent", "HRMS API Agent", "HR Policy Q&A Agent", "Payroll API Agent"))
                .regulatoryImpact("I-9, EEOC, FLSA").sla("99.5% / Day-1 readiness")
                .keyCapabilities(List.of("Onboarding checklist orchestration", "Cross-system provisioning coordination", "Compliance task sequencing", "Manager notification automation"))
                .controls(List.of("HR business partner sign-off", "I-9 completion verification", "SOX access control review")).build());

        agents.add(Agent.builder()
                .id("hr-001").name("Talent Acquisition Agent").domain("Human Resources")
                .description("AI agent that screens resumes, generates interview scorecards, drafts offer letters, and predicts candidate fit using behavioral and skills data.")
                .owner("HR - Talent Acquisition").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("GPT-4o").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("EEOC, Title VII, ADA, OFCCP").sla("99% / <24h screening")
                .keyCapabilities(List.of("Resume parsing and scoring", "Skills gap analysis", "Interview question generation", "Offer letter drafting"))
                .controls(List.of("Bias audit on screening model quarterly", "EEOC adverse impact analysis", "OFCCP compliance logging")).build());

        agents.add(Agent.builder()
                .id("hr-002").name("HRMS API Agent").domain("Human Resources")
                .description("Agent wrapper over Workday HCM API — provisions employee records, manages role assignments, and syncs org chart changes in real time.")
                .owner("HR - Technology").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.PRODUCT_API_AGENT)
                .productApi("Workday Human Capital Management API")
                .regulatoryImpact("SOX 404 HR Controls, I-9, FLSA").sla("99.9% / <2s API response")
                .keyCapabilities(List.of("Employee lifecycle management via Workday", "Role and compensation changes", "Org chart hierarchy sync", "Termination and offboarding workflow"))
                .controls(List.of("Dual approval for compensation changes", "SOX HR control evidence generation", "Annual Workday entitlement review")).build());

        agents.add(Agent.builder()
                .id("hr-003").name("HR Policy Q&A Agent").domain("Human Resources")
                .description("AI agent trained on HR policies, benefits plans, and employment law to answer employee questions accurately and escalate edge cases to HR BP.")
                .owner("HR - Employee Experience").riskTier(RiskTier.LOW).status(Status.ACTIVE)
                .model("Claude Sonnet").dataClassification(DataClassification.INTERNAL)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("ERISA, FMLA, ADA").sla("99.5% / <3s response")
                .keyCapabilities(List.of("Policy document RAG retrieval", "Benefits eligibility Q&A", "Leave entitlement calculation", "HR case escalation trigger"))
                .controls(List.of("HR BP review of escalated queries", "Quarterly policy document refresh", "Legal review for FMLA/ADA responses")).build());

        agents.add(Agent.builder()
                .id("hr-004").name("Payroll API Agent").domain("Human Resources")
                .description("Wraps the ADP Workforce Now API to submit payroll data, retrieve pay stubs, and manage tax withholding changes on behalf of HR and employees.")
                .owner("HR - Payroll").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.RESTRICTED)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.API_WRAPPER)
                .productApi("ADP Workforce Now API")
                .regulatoryImpact("FLSA, IRS Payroll, SOX 404").sla("99.99% / payroll cycle deadlines")
                .keyCapabilities(List.of("Payroll run submission via ADP API", "W-4 withholding updates", "Pay stub retrieval and distribution", "Year-end W-2 generation trigger"))
                .controls(List.of("Payroll manager final approval required", "SOX payroll control audit", "IRS filing deadline monitoring")).build());

        // ── 7. DATA & ANALYTICS ───────────────────────────────────────────────
        agents.add(Agent.builder()
                .id("data-orch-001").name("Data Pipeline Orchestrator").domain("Data & Analytics")
                .description("Coordinates the enterprise data pipeline: quality checks → catalog registration → lineage tracking → BI report generation → data steward alerting.")
                .owner("Data Governance - CDO Office").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("Claude Sonnet").dataClassification(DataClassification.INTERNAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.ORCHESTRATOR)
                .orchestrates(List.of("Data Quality Agent", "Data Catalog API Agent", "Lineage Tracker Agent", "BI Reporting Agent"))
                .regulatoryImpact("BCBS 239, SR 11-7 Data Quality").sla("99.9% / SLA per pipeline schedule")
                .keyCapabilities(List.of("DAG-style pipeline orchestration", "Quality gate enforcement", "SLA breach escalation", "Data steward notification"))
                .controls(List.of("CDO approval for new pipelines", "BCBS 239 compliance checkpoint", "Monthly pipeline health dashboard")).build());

        agents.add(Agent.builder()
                .id("data-001").name("Data Quality Agent").domain("Data & Analytics")
                .description("AI agent that continuously monitors data pipelines for completeness, accuracy, timeliness, and consistency using rule-based and ML anomaly detection.")
                .owner("Data Governance").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.INTERNAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("BCBS 239, SR 11-7").sla("99.9% / <30min anomaly detection")
                .keyCapabilities(List.of("Rule-based DQ validation", "Statistical drift detection", "Data freshness monitoring", "Business owner alerting"))
                .controls(List.of("Data steward remediation SLA", "CDO escalation for critical failures", "Quarterly DQ threshold review")).build());

        agents.add(Agent.builder()
                .id("data-002").name("Data Catalog API Agent").domain("Data & Analytics")
                .description("Agent wrapper over Collibra Data Intelligence Cloud API for automated asset registration, classification tagging, and lineage metadata publishing.")
                .owner("Data Governance - Metadata").riskTier(RiskTier.LOW).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.INTERNAL)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.PRODUCT_API_AGENT)
                .productApi("Collibra Data Intelligence Cloud API")
                .regulatoryImpact("BCBS 239, GDPR Article 30").sla("99.5% / <1h registration")
                .keyCapabilities(List.of("Asset auto-registration in Collibra", "Business glossary term linking", "Sensitivity classification tagging", "Lineage edge creation"))
                .controls(List.of("Data steward approval for sensitive assets", "Annual catalog completeness audit", "GDPR record-of-processing sync")).build());

        agents.add(Agent.builder()
                .id("data-003").name("Lineage Tracker Agent").domain("Data & Analytics")
                .description("AI agent that automatically reconstructs end-to-end data lineage from ETL logs, SQL parsing, and API call tracing for impact analysis.")
                .owner("Data Engineering").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.INTERNAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("BCBS 239, SR 11-7 Model Inputs").sla("99.5% / <1h post-deploy")
                .keyCapabilities(List.of("SQL parse-based lineage extraction", "ETL log correlation", "Downstream impact graph generation", "Breaking change detection"))
                .controls(List.of("Engineering manager review for breaking changes", "Regulatory report lineage audit")).build());

        agents.add(Agent.builder()
                .id("data-004").name("BI Reporting Agent").domain("Data & Analytics")
                .description("Wraps the Tableau REST API to automate dashboard publishing, refresh scheduling, and distribution of business intelligence reports.")
                .owner("Data & Analytics - BI").riskTier(RiskTier.LOW).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.INTERNAL)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.API_WRAPPER)
                .productApi("Tableau REST API")
                .regulatoryImpact("None").sla("99.5% / scheduled refresh SLA")
                .keyCapabilities(List.of("Workbook publish and version control", "Data source refresh scheduling", "Subscriptions and alerts management", "Usage analytics retrieval"))
                .controls(List.of("BI team approval for executive dashboards", "Quarterly Tableau license audit")).build());

        // ── 8. FINANCE ────────────────────────────────────────────────────────
        agents.add(Agent.builder()
                .id("fin-orch-001").name("Financial Close Orchestrator").domain("Finance")
                .description("Coordinates the monthly financial close: GL reconciliation → FX rate ingestion → tax provision → financial reporting → CFO attestation workflow.")
                .owner("Finance - Controller").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("Claude Sonnet").dataClassification(DataClassification.RESTRICTED)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.ORCHESTRATOR)
                .orchestrates(List.of("GL Reconciliation Agent", "FX Rate Feed Agent", "Tax Compliance Agent", "Financial Reporting Agent"))
                .regulatoryImpact("SOX 302/404, GAAP, SEC Reporting").sla("100% / close calendar deadlines")
                .keyCapabilities(List.of("Close calendar task sequencing", "Dependency-aware execution", "CFO certification workflow", "Audit evidence package assembly"))
                .controls(List.of("CFO sign-off required", "External auditor access to pipeline", "SOX control documentation")).build());

        agents.add(Agent.builder()
                .id("fin-001").name("GL Reconciliation Agent").domain("Finance")
                .description("Agent wrapper over Oracle Financials Cloud for automated GL account reconciliation, journal entry validation, and inter-company elimination.")
                .owner("Finance - Accounting").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.RESTRICTED)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.PRODUCT_API_AGENT)
                .productApi("Oracle Financials Cloud REST API")
                .regulatoryImpact("SOX 404, GAAP ASC 230").sla("99.9% / T+1 reconciliation")
                .keyCapabilities(List.of("Account reconciliation via Oracle API", "Journal entry validation rules", "Inter-company balance matching", "Break aging and auto-escalation"))
                .controls(List.of("Controller sign-off for aged breaks", "SOX control evidence generation", "External audit access")).build());

        agents.add(Agent.builder()
                .id("fin-002").name("FX Rate Feed Agent").domain("Finance")
                .description("Wraps Reuters Eikon API to ingest official FX spot/forward rates for multi-currency financial reporting and hedge accounting purposes.")
                .owner("Finance - Treasury Accounting").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.INTERNAL)
                .autonomyLevel(AutonomyLevel.LOW).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.API_WRAPPER)
                .productApi("Reuters Eikon Data API")
                .regulatoryImpact("ASC 830 Foreign Currency, IFRS 9").sla("99.99% / EOD rate ingestion")
                .keyCapabilities(List.of("Daily spot and forward rate ingestion", "Central bank rate cross-validation", "Rate audit trail generation", "Failover to ECB backup feed"))
                .controls(List.of("Treasury controller rate approval", "Annual ISDA rate source review")).build());

        agents.add(Agent.builder()
                .id("fin-003").name("Tax Compliance Agent").domain("Finance")
                .description("AI agent that automates ASC 740 tax provision calculations, estimates quarterly tax payments, and identifies uncertain tax positions (UTPs).")
                .owner("Finance - Tax").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.RESTRICTED)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("ASC 740, IRS, OECD Pillar Two").sla("99.5% / quarterly deadline")
                .keyCapabilities(List.of("ETR forecasting", "Deferred tax rollforward", "UTP identification and measurement", "Pillar Two GloBE calculation"))
                .controls(List.of("VP Tax sign-off required", "External tax advisor review", "IRS filing deadline monitoring")).build());

        agents.add(Agent.builder()
                .id("fin-004").name("Financial Reporting Agent").domain("Finance")
                .description("AI agent that drafts MD&A sections, variance analyses, and Board reporting packages from structured financial data.")
                .owner("Finance - FP&A").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("GPT-4o").dataClassification(DataClassification.RESTRICTED)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("SEC Reg S-K, GAAP Disclosures").sla("99% / report cycle")
                .keyCapabilities(List.of("MD&A narrative generation", "Variance commentary", "KPI dashboard population", "Board pack assembly"))
                .controls(List.of("CFO and Controller review required", "Legal review of public disclosures", "Audit committee approval for Board pack")).build());

        // ── 9. RISK & COMPLIANCE ─────────────────────────────────────────────
        agents.add(Agent.builder()
                .id("risk-orch-001").name("Risk Assessment Orchestrator").domain("Risk & Compliance")
                .description("Coordinates enterprise risk workflows: AML transaction screening → credit risk scoring → regulatory report submission → sanctions check → risk escalation.")
                .owner("Chief Risk Officer - Office").riskTier(RiskTier.CRITICAL).status(Status.ACTIVE)
                .model("Claude Sonnet").dataClassification(DataClassification.RESTRICTED)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.ORCHESTRATOR)
                .orchestrates(List.of("AML / Fraud Detection Agent", "Regulatory Reporting Agent", "Credit Risk Scoring Agent", "Sanctions Screening Agent"))
                .regulatoryImpact("BSA/AML, OFAC, Basel III, CCAR").sla("99.99% / real-time risk gating")
                .keyCapabilities(List.of("Risk workflow gating logic", "Parallel risk check coordination", "Escalation chain management", "Regulatory SLA enforcement"))
                .controls(List.of("CRO approval for risk model changes", "Independent validation annually", "Fed exam readiness maintained")).build());

        agents.add(Agent.builder()
                .id("risk-001").name("AML / Fraud Detection Agent").domain("Risk & Compliance")
                .description("AI agent that performs real-time transaction monitoring for money laundering patterns, structuring, rapid fund movement, and account takeover fraud.")
                .owner("Financial Crimes - AML").riskTier(RiskTier.CRITICAL).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.RESTRICTED)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("BSA/AML, FinCEN SAR, OFAC, Reg GG").sla("99.99% / real-time")
                .keyCapabilities(List.of("Graph-based transaction network analysis", "Structuring pattern detection", "Rapid fund movement alerts", "SAR narrative generation"))
                .controls(List.of("AML analyst SAR review and filing", "MLRO sign-off on model changes", "Independent model validation annually")).build());

        agents.add(Agent.builder()
                .id("risk-002").name("Regulatory Reporting Agent").domain("Risk & Compliance")
                .description("Agent wrapper over Wolters Kluwer OneSumX for automated regulatory report generation: FR Y-9C, Call Report, DFAST, LCR submissions.")
                .owner("Risk - Regulatory Reporting").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.RESTRICTED)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.PRODUCT_API_AGENT)
                .productApi("Wolters Kluwer OneSumX Regulatory Reporting API")
                .regulatoryImpact("FFIEC Call Report, FR Y-9C, DFAST, LCR").sla("100% / zero missed deadlines")
                .keyCapabilities(List.of("Report generation via OneSumX API", "XBRL taxonomy mapping", "Edit check resolution", "Submission confirmation tracking"))
                .controls(List.of("CFO certification required", "Controller review of material items", "CCO SLA dashboard")).build());

        agents.add(Agent.builder()
                .id("risk-003").name("Credit Risk Scoring Agent").domain("Risk & Compliance")
                .description("AI agent that calculates PD, LGD, and EAD for the entire loan portfolio using multi-model ensemble and macroeconomic overlay.")
                .owner("Credit Risk Management").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.RESTRICTED)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("Basel III IRB, CECL, DFAST").sla("99.9% / daily batch")
                .keyCapabilities(List.of("PD/LGD/EAD estimation", "Macro-economic scenario weighting", "CECL reserve calculation", "Portfolio concentration analysis"))
                .controls(List.of("CRO approval for model updates", "Independent model validation", "Annual DFAST scenario review")).build());

        agents.add(Agent.builder()
                .id("risk-004").name("Sanctions Screening Agent").domain("Risk & Compliance")
                .description("Wraps Dow Jones Risk & Compliance API to perform real-time OFAC, UN, EU, and country-specific sanctions screening on counterparties and transactions.")
                .owner("Compliance - Sanctions").riskTier(RiskTier.CRITICAL).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.RESTRICTED)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.API_WRAPPER)
                .productApi("Dow Jones Risk & Compliance API")
                .regulatoryImpact("OFAC, UN Sanctions, EU Sanctions, 31 CFR Part 500").sla("99.999% / <100ms real-time")
                .keyCapabilities(List.of("Real-time OFAC SDN list screening", "Fuzzy name matching with threshold tuning", "PEP and adverse media screening", "Alert generation and audit trail"))
                .controls(List.of("Compliance officer review of all hits", "Zero-tolerance policy for OFAC violations", "Daily list update verification")).build());

        // ── 10. SUPPLY CHAIN ──────────────────────────────────────────────────
        agents.add(Agent.builder()
                .id("sc-orch-001").name("Procurement Orchestrator").domain("Supply Chain")
                .description("Coordinates end-to-end procurement: vendor risk assessment → ERP purchase order creation → logistics tracking → inventory reconciliation.")
                .owner("Supply Chain - Procurement").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("GPT-4o").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.ORCHESTRATOR)
                .orchestrates(List.of("Vendor Risk Agent", "ERP API Agent", "Logistics Tracking Agent", "Inventory Optimization Agent"))
                .regulatoryImpact("OCC TPRM Bulletin 2013-29, SOX AP Controls").sla("99.5% / PO-to-receipt SLA")
                .keyCapabilities(List.of("Procurement workflow orchestration", "Three-way match coordination", "Vendor SLA monitoring", "Spend analytics aggregation"))
                .controls(List.of("Procurement manager approval for >$50K", "Annual TPRM review for critical vendors", "SOX AP control evidence")).build());

        agents.add(Agent.builder()
                .id("sc-001").name("Vendor Risk Agent").domain("Supply Chain")
                .description("AI agent that performs automated vendor due diligence: financial health analysis, SOC 2 review, cybersecurity posture, and concentration risk scoring.")
                .owner("Supply Chain - Vendor Risk").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("Claude Sonnet").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("OCC TPRM, FFIEC Outsourcing, SR 13-19").sla("99% / <10 business days DDQ")
                .keyCapabilities(List.of("DDQ auto-scoring", "SOC 2 report analysis", "Financial health assessment", "Concentration risk detection"))
                .controls(List.of("Procurement and Legal review for critical vendors", "Board reporting on top 20 vendors", "Annual re-assessment trigger")).build());

        agents.add(Agent.builder()
                .id("sc-002").name("ERP API Agent").domain("Supply Chain")
                .description("Agent wrapper over SAP Ariba for purchase order creation, goods receipt confirmation, invoice matching, and supplier collaboration.")
                .owner("Supply Chain - Technology").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.CONFIDENTIAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.PRODUCT_API_AGENT)
                .productApi("SAP Ariba Procurement API")
                .regulatoryImpact("SOX 404 AP Controls, FCPA").sla("99.9% / <500ms API response")
                .keyCapabilities(List.of("PO creation and amendment via Ariba API", "GR confirmation and three-way match", "Invoice submission and approval", "Supplier catalog management"))
                .controls(List.of("Dual approval for POs >$10K", "SAP audit log monitoring", "FCPA third-party payment controls")).build());

        agents.add(Agent.builder()
                .id("sc-003").name("Logistics Tracking Agent").domain("Supply Chain")
                .description("Wraps FedEx and UPS Tracking APIs to provide real-time shipment visibility, delivery exception alerts, and carrier SLA compliance monitoring.")
                .owner("Supply Chain - Logistics").riskTier(RiskTier.LOW).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.INTERNAL)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.API_WRAPPER)
                .productApi("FedEx / UPS Tracking REST APIs")
                .regulatoryImpact("None").sla("99.5% / <5min tracking refresh")
                .keyCapabilities(List.of("Real-time shipment status streaming", "Delivery exception alerting", "ETA recalculation on delays", "Carrier SLA breach reporting"))
                .controls(List.of("API key management and rotation", "Carrier contract SLA enforcement")).build());

        agents.add(Agent.builder()
                .id("sc-004").name("Inventory Optimization Agent").domain("Supply Chain")
                .description("AI agent that forecasts demand, calculates optimal reorder points, and recommends safety stock levels using ML time-series models.")
                .owner("Supply Chain - Inventory").riskTier(RiskTier.LOW).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.INTERNAL)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("None").sla("99% / daily batch")
                .keyCapabilities(List.of("Demand forecasting (ARIMA/XGBoost)", "Reorder point calculation", "Safety stock optimization", "Stockout risk alerting"))
                .controls(List.of("Supply chain manager review of recommendations", "Quarterly model accuracy audit")).build());

        // ── 11. DIGITAL WORKPLACE ─────────────────────────────────────────────
        agents.add(Agent.builder()
                .id("dw-orch-001").name("IT Service Orchestrator").domain("Digital Workplace")
                .description("Coordinates IT service management: IT helpdesk triage → ServiceNow ticket creation → Microsoft 365 provisioning → Okta identity management.")
                .owner("IT Operations - Service Desk").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("Claude Sonnet").dataClassification(DataClassification.INTERNAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.ORCHESTRATOR)
                .orchestrates(List.of("IT Helpdesk Agent", "ITSM API Agent", "Collaboration Agent", "Identity & Access Agent"))
                .regulatoryImpact("GLBA, SOX IT Controls, NYDFS 500").sla("99.9% / P1 response <15min")
                .keyCapabilities(List.of("Incident triage and routing", "Auto-remediation workflow", "Access provisioning coordination", "SLA escalation management"))
                .controls(List.of("CISO approval for security-impacting changes", "CAB review for major changes", "SOX ITGC evidence collection")).build());

        agents.add(Agent.builder()
                .id("dw-001").name("IT Helpdesk Agent").domain("Digital Workplace")
                .description("AI agent that triages IT support requests, resolves Tier-1 issues autonomously (password resets, software access), and routes complex issues to specialists.")
                .owner("IT Operations - Support").riskTier(RiskTier.LOW).status(Status.ACTIVE)
                .model("Claude Sonnet").dataClassification(DataClassification.INTERNAL)
                .autonomyLevel(AutonomyLevel.MEDIUM).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.AI_AGENT)
                .regulatoryImpact("GLBA Safeguards Rule").sla("99.5% / Tier-1 <5min resolution")
                .keyCapabilities(List.of("Issue classification and triage", "Self-service resolution automation", "Knowledge base article retrieval", "Specialist routing with context"))
                .controls(List.of("Password reset requires MFA verification", "Privileged access escalation human review", "Monthly CSAT monitoring")).build());

        agents.add(Agent.builder()
                .id("dw-002").name("ITSM API Agent").domain("Digital Workplace")
                .description("Agent wrapper over ServiceNow ITSM API for automated incident, change, and problem record management with workflow orchestration.")
                .owner("IT Operations - ITSM").riskTier(RiskTier.MEDIUM).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.INTERNAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.PRODUCT_API_AGENT)
                .productApi("ServiceNow ITSM REST API")
                .regulatoryImpact("SOX ITGC, FFIEC Business Continuity").sla("99.9% / <500ms ticket operations")
                .keyCapabilities(List.of("Incident creation and auto-assignment", "Change record management", "Problem linking and root cause", "SLA timer management"))
                .controls(List.of("CAB approval for RFC changes", "SOX ITGC evidence auto-capture", "CISO review for security incidents")).build());

        agents.add(Agent.builder()
                .id("dw-003").name("Collaboration Agent").domain("Digital Workplace")
                .description("Wraps Microsoft 365 Graph API to automate Teams channel creation, SharePoint provisioning, email routing, and meeting scheduling.")
                .owner("Digital Workplace - M365").riskTier(RiskTier.LOW).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.INTERNAL)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.API_WRAPPER)
                .productApi("Microsoft 365 Graph API")
                .regulatoryImpact("GLBA, Email Retention Policy").sla("99.9% / <1s API response")
                .keyCapabilities(List.of("Teams channel and site provisioning", "SharePoint document library management", "Email rules and distribution list management", "Calendar scheduling automation"))
                .controls(List.of("Teams governance policy enforcement", "Email retention rule compliance", "Annual M365 license audit")).build());

        agents.add(Agent.builder()
                .id("dw-004").name("Identity & Access Agent").domain("Digital Workplace")
                .description("Agent wrapper over Okta Identity API for automated user provisioning, SSO entitlement management, MFA enrollment, and access certification.")
                .owner("Cyber - Identity & Access Management").riskTier(RiskTier.HIGH).status(Status.ACTIVE)
                .model("Internal").dataClassification(DataClassification.RESTRICTED)
                .autonomyLevel(AutonomyLevel.HIGH).complianceStatus(ComplianceStatus.APPROVED)
                .agentType(AgentType.PRODUCT_API_AGENT)
                .productApi("Okta Identity Cloud API")
                .regulatoryImpact("SOX 404, GLBA, NYDFS 500, FFIEC IS Handbook").sla("99.99% / <200ms provisioning")
                .keyCapabilities(List.of("User lifecycle provisioning via Okta", "SSO application assignment", "MFA enrollment automation", "Access certification workflow"))
                .controls(List.of("Dual approval for privileged access", "Quarterly access certification required", "CISO exception approval for MFA bypass")).build());
    }

    public List<Agent> getAgents(String domain, String riskTier, String status,
                                  String dataClassification, String model,
                                  String complianceStatus, String agentType, String search) {
        return agents.stream()
                .filter(a -> domain == null || domain.isBlank() || a.getDomain().equalsIgnoreCase(domain))
                .filter(a -> riskTier == null || riskTier.isBlank() || a.getRiskTier().name().equalsIgnoreCase(riskTier))
                .filter(a -> status == null || status.isBlank() || a.getStatus().name().equalsIgnoreCase(status))
                .filter(a -> dataClassification == null || dataClassification.isBlank() || a.getDataClassification().name().equalsIgnoreCase(dataClassification))
                .filter(a -> model == null || model.isBlank() || a.getModel().equalsIgnoreCase(model))
                .filter(a -> complianceStatus == null || complianceStatus.isBlank() || a.getComplianceStatus().name().equalsIgnoreCase(complianceStatus))
                .filter(a -> agentType == null || agentType.isBlank() || (a.getAgentType() != null && a.getAgentType().name().equalsIgnoreCase(agentType)))
                .filter(a -> search == null || search.isBlank() ||
                        a.getName().toLowerCase().contains(search.toLowerCase()) ||
                        a.getDescription().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Optional<Agent> getAgentById(String id) {
        return agents.stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    public List<Domain> getDomains() {
        Map<String, Long> countByDomain = agents.stream()
                .collect(Collectors.groupingBy(Agent::getDomain, Collectors.counting()));

        return List.of(
                Domain.builder().id("retail").name("Retail Banking").description("Onboarding, KYC, core banking, and loan origination").icon("🏦").agentCount(countByDomain.getOrDefault("Retail Banking", 0L).intValue()).build(),
                Domain.builder().id("wealth").name("Wealth Management").description("Portfolio advisory, market data, and rebalancing").icon("📈").agentCount(countByDomain.getOrDefault("Wealth Management", 0L).intValue()).build(),
                Domain.builder().id("ci").name("Commercial & Industrial").description("C&I lending, trade finance, and syndications").icon("🏢").agentCount(countByDomain.getOrDefault("Commercial & Industrial", 0L).intValue()).build(),
                Domain.builder().id("marketing").name("Marketing").description("Campaigns, personalization, and customer segmentation").icon("📣").agentCount(countByDomain.getOrDefault("Marketing", 0L).intValue()).build(),
                Domain.builder().id("cx").name("Customer Experience").description("Conversational AI, CRM, and complaint management").icon("💬").agentCount(countByDomain.getOrDefault("Customer Experience", 0L).intValue()).build(),
                Domain.builder().id("hr").name("Human Resources").description("Talent, HRMS, payroll, and policy Q&A").icon("👥").agentCount(countByDomain.getOrDefault("Human Resources", 0L).intValue()).build(),
                Domain.builder().id("data").name("Data & Analytics").description("Data quality, catalog, lineage, and BI reporting").icon("🗄️").agentCount(countByDomain.getOrDefault("Data & Analytics", 0L).intValue()).build(),
                Domain.builder().id("finance").name("Finance").description("GL reconciliation, FX, tax, and financial reporting").icon("💰").agentCount(countByDomain.getOrDefault("Finance", 0L).intValue()).build(),
                Domain.builder().id("risk").name("Risk & Compliance").description("AML, credit risk, regulatory reporting, and sanctions").icon("⚠️").agentCount(countByDomain.getOrDefault("Risk & Compliance", 0L).intValue()).build(),
                Domain.builder().id("supplychain").name("Supply Chain").description("Vendor risk, procurement, logistics, and inventory").icon("🔗").agentCount(countByDomain.getOrDefault("Supply Chain", 0L).intValue()).build(),
                Domain.builder().id("dw").name("Digital Workplace").description("IT helpdesk, ITSM, collaboration, and identity").icon("💻").agentCount(countByDomain.getOrDefault("Digital Workplace", 0L).intValue()).build()
        );
    }

    public Map<String, Object> getStats() {
        long total = agents.size();
        long active = agents.stream().filter(a -> a.getStatus() == Status.ACTIVE).count();
        long domains = agents.stream().map(Agent::getDomain).distinct().count();
        long critical = agents.stream().filter(a -> a.getRiskTier() == RiskTier.CRITICAL).count();
        long high = agents.stream().filter(a -> a.getRiskTier() == RiskTier.HIGH).count();
        long orchestrators = agents.stream().filter(a -> a.getAgentType() == AgentType.ORCHESTRATOR).count();
        long apiWrappers = agents.stream().filter(a -> a.getAgentType() == AgentType.API_WRAPPER).count();
        long productApiAgents = agents.stream().filter(a -> a.getAgentType() == AgentType.PRODUCT_API_AGENT).count();
        long aiAgents = agents.stream().filter(a -> a.getAgentType() == AgentType.AI_AGENT).count();

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalAgents", total);
        stats.put("activeAgents", active);
        stats.put("domains", domains);
        stats.put("criticalRiskAgents", critical);
        stats.put("highRiskAgents", high);
        stats.put("orchestrators", orchestrators);
        stats.put("aiAgents", aiAgents);
        stats.put("apiWrappers", apiWrappers);
        stats.put("productApiAgents", productApiAgents);
        return stats;
    }
}