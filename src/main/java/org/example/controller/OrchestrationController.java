package org.example.controller;

import org.example.model.Agent;
import org.example.service.AgentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OrchestrationController {

    private final AgentService agentService;
    private final RestTemplate restTemplate;

    @Value("${n8n.base.url:http://localhost:5678}")
    private String n8nBaseUrl;

    public OrchestrationController(AgentService agentService) {
        this.agentService = agentService;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(5000);
        this.restTemplate = new RestTemplate(factory);
    }

    /** Check if n8n is reachable */
    @GetMapping("/n8n/status")
    public Map<String, Object> n8nStatus() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("n8nUrl", n8nBaseUrl);
        try {
            restTemplate.getForObject(n8nBaseUrl + "/healthz", String.class);
            result.put("status", "online");
        } catch (Exception e) {
            result.put("status", "offline");
            result.put("hint", "Start n8n with: npx n8n");
        }
        return result;
    }

    /**
     * Trigger an n8n webhook for a given orchestrator agent ID.
     * n8n webhook path convention: /webhook/{agentId}
     */
    @PostMapping("/orchestrate/{agentId}")
    public ResponseEntity<Map<String, Object>> triggerOrchestration(
            @PathVariable String agentId,
            @RequestBody(required = false) Map<String, Object> payload) {

        Optional<Agent> agentOpt = agentService.getAgentById(agentId);
        if (agentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Agent agent = agentOpt.get();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("agentId", agentId);
        result.put("agentName", agent.getName());
        result.put("domain", agent.getDomain());
        result.put("orchestrates", agent.getOrchestrates());

        // Build the n8n webhook URL for this orchestrator
        String webhookUrl = n8nBaseUrl + "/webhook/" + agentId;

        // Prepare payload to send to n8n
        Map<String, Object> n8nPayload = new LinkedHashMap<>();
        n8nPayload.put("orchestratorId", agentId);
        n8nPayload.put("orchestratorName", agent.getName());
        n8nPayload.put("domain", agent.getDomain());
        n8nPayload.put("subAgents", agent.getOrchestrates());
        if (payload != null) n8nPayload.putAll(payload);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(n8nPayload, headers);

            ResponseEntity<Map> n8nResponse = restTemplate.postForEntity(webhookUrl, entity, Map.class);

            result.put("status", "triggered");
            result.put("n8nStatus", n8nResponse.getStatusCode().value());
            result.put("n8nResponse", n8nResponse.getBody());
            result.put("webhookUrl", webhookUrl);
            return ResponseEntity.ok(result);

        } catch (ResourceAccessException e) {
            // n8n is not running — return simulation mode
            result.put("status", "simulated");
            result.put("message", "n8n not reachable at " + n8nBaseUrl + ". Running in simulation mode.");
            result.put("webhookUrl", webhookUrl);
            result.put("simulatedSteps", buildSimulatedSteps(agent));
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    private List<Map<String, Object>> buildSimulatedSteps(Agent agent) {
        List<Map<String, Object>> steps = new ArrayList<>();
        steps.add(Map.of("step", 0, "name", "Trigger", "status", "done", "durationMs", 0,
                "output", Map.of("event", "Webhook received", "source", "AgentCatalog UI",
                        "orchestrator", agent.getName(), "domain", agent.getDomain())));

        if (agent.getOrchestrates() != null) {
            int i = 1;
            for (String subAgent : agent.getOrchestrates()) {
                Map<String, Object> output = buildAgentOutput(subAgent, agent.getDomain());
                steps.add(Map.of("step", i++, "name", subAgent, "status", "done",
                        "durationMs", 200 + (int) (Math.random() * 800), "output", output));
            }
        }

        steps.add(Map.of("step", steps.size(), "name", "Aggregate & Respond", "status", "done",
                "durationMs", 48, "output", Map.of(
                        "status", "COMPLETE",
                        "orchestrator", agent.getName(),
                        "subAgentsInvoked", agent.getOrchestrates() != null ? agent.getOrchestrates().size() : 0,
                        "summary", "All sub-agents completed successfully. Results aggregated.")));
        return steps;
    }

    private Map<String, Object> buildAgentOutput(String agentName, @SuppressWarnings("unused") String domain) {
        return switch (agentName) {
            // ── Retail Banking ──────────────────────────────────────────
            case "KYC Verification Agent" -> mapOf(
                    "result", "VERIFIED", "riskScore", 18,
                    "ofacStatus", "CLEAR", "documentType", "Passport",
                    "biometricCheck", "PASS", "pep", false,
                    "summary", "Identity verified. Low-risk profile. No sanctions hits.");
            case "Core Banking API Agent" -> mapOf(
                    "accountNumber", "4521-8832-001", "accountType", "Checking",
                    "sortCode", "20-14-42", "branchCode", "NYC-042",
                    "status", "CREATED", "t24Reference", "T24-20260328-00482",
                    "summary", "Account opened in Temenos T24. Ref T24-20260328-00482.");
            case "Loan Origination Agent" -> mapOf(
                    "decision", "PRE_QUALIFIED", "loanAmount", "$45,000",
                    "apr", "7.25%", "term", "60 months",
                    "creditScore", 724, "dti", "28%",
                    "summary", "Pre-qualified for $45K personal loan at 7.25% APR.");
            case "Branch Locator Agent" -> mapOf(
                    "nearestBranch", "42nd Street Branch", "distanceMiles", 0.3,
                    "waitTimeMin", 8, "atmAvailable", true,
                    "services", List.of("Notary", "Safe Deposit", "FX"),
                    "summary", "Nearest branch 0.3 mi away with ~8 min wait.");

            // ── Wealth Management ─────────────────────────────────────
            case "Portfolio Analyzer Agent" -> mapOf(
                    "totalValue", "$2,412,800", "ytdReturn", "+11.2%",
                    "allocation", "62% Equity / 38% Fixed", "volatilityRating", "Medium",
                    "sharpeRatio", 1.42, "benchmark", "S&P 500 +9.8%",
                    "summary", "Portfolio outperforming benchmark by 1.4%. Rebalance recommended.");
            case "Market Data Feed Agent" -> mapOf(
                    "sp500", "+0.43%", "nasdaq", "+0.61%",
                    "us10yr", "4.28%", "usdGbp", "0.7914",
                    "goldUsd", "$2,318/oz", "dataSource", "Bloomberg Terminal",
                    "summary", "Live market data pulled. Equities up, bonds flat.");
            case "Risk Profiling Agent" -> mapOf(
                    "riskScore", 68, "profile", "Moderate-Aggressive",
                    "suitability", "PASS", "capacityForLoss", "Medium-High",
                    "nextReview", "Q3 2026", "questionsAnswered", 14,
                    "summary", "Client risk profile: Moderate-Aggressive. Suitability confirmed.");
            case "Rebalancing Execution Agent" -> mapOf(
                    "ordersPlaced", 4, "equityBuy", "+$12,400",
                    "bondSell", "-$12,400", "cashImpact", "-$0",
                    "executionVenue", "NYSE / LSE", "status", "EXECUTED",
                    "summary", "4 rebalancing orders executed. Portfolio aligned to target.");

            // ── Commercial & Industrial ───────────────────────────────
            case "Trade Finance Agent" -> mapOf(
                    "lcNumber", "LC-2026-TF-0892", "amount", "$1,250,000",
                    "currency", "USD", "expiryDate", "2026-06-30",
                    "issuingBank", "Enterprise Bank NA", "status", "ISSUED",
                    "summary", "Letter of Credit issued for $1.25M. Expiry 30-Jun-2026.");
            case "Corporate Banking API Agent" -> mapOf(
                    "facilityId", "CBF-2026-00341", "creditLimit", "$5,000,000",
                    "utilised", "$1,250,000", "available", "$3,750,000",
                    "ratingGrade", "BB+", "systemRef", "FIS-CBS-00341",
                    "summary", "Credit facility verified. $3.75M available headroom.");
            case "Syndication Market Agent" -> mapOf(
                    "dealName", "ABC Corp TLB", "totalDealSize", "$180,000,000",
                    "ourParticipation", "$5,000,000", "spread", "SOFR+275bps",
                    "bookRunner", "JP Morgan", "status", "ALLOCATING",
                    "summary", "Syndication deal allocating. $5M ticket at SOFR+275bps.");
            case "Treasury Management Agent" -> mapOf(
                    "cashPosition", "$42,300,000", "fxExposure", "EUR 8.2M",
                    "hedgeRatio", "78%", "liquidityRatio", "142%",
                    "nextMaturity", "2026-04-15", "status", "COMPLIANT",
                    "summary", "Treasury position healthy. FX 78% hedged. LCR 142%.");

            // ── Marketing ─────────────────────────────────────────────
            case "Personalization Engine" -> mapOf(
                    "segmentId", "SEG-MORTGAGE-REFI-42", "audienceSize", 18_420,
                    "topProduct", "Fixed Rate Mortgage", "propensityScore", 0.74,
                    "nextBestAction", "Email + Push Notification", "model", "XGBoost v3.1",
                    "summary", "18,420 customers targeted. 74% propensity score.");
            case "CRM Integration Agent" -> mapOf(
                    "recordsUpdated", 18_420, "crmSystem", "Salesforce Financial Services Cloud",
                    "campaignId", "CMP-2026-Q1-REFI", "syncStatus", "COMPLETE",
                    "errorsFound", 0, "lastSync", "2026-03-28T14:32:00Z",
                    "summary", "18,420 CRM records updated for Q1 mortgage refi campaign.");
            case "Customer Segmentation Agent" -> mapOf(
                    "totalCustomers", 248_000, "segmentsCreated", 7,
                    "topSegment", "High-balance savers 35-55", "segmentSize", 31_200,
                    "modelAccuracy", "89.3%", "featureCount", 42,
                    "summary", "7 micro-segments identified. Top segment: 31,200 high-balance savers.");
            case "Social Listening Agent" -> mapOf(
                    "mentionsTracked", 4_821, "sentimentScore", 0.67,
                    "topTopic", "Mobile app experience", "npsSignal", "+42",
                    "alertsRaised", 2, "platforms", List.of("Twitter/X", "Reddit", "Trustpilot"),
                    "summary", "Positive sentiment 67%. NPS signal +42. 2 brand risk alerts.");

            // ── Customer Experience ────────────────────────────────────
            case "Conversational Assistant (Aria)" -> mapOf(
                    "sessionId", "ARIA-20260328-8841", "intent", "Balance Inquiry + Transfer",
                    "turnCount", 4, "resolutionStatus", "RESOLVED",
                    "escalated", false, "satisfactionScore", 4.8,
                    "summary", "Customer query resolved in 4 turns. CSAT 4.8/5. No escalation.");
            case "CRM Service Agent" -> mapOf(
                    "caseId", "CAS-2026-44192", "caseType", "Complaint",
                    "priority", "P2", "assignedTo", "Digital Resolution Team",
                    "slaStatus", "ON_TRACK", "estimatedClose", "2026-03-30",
                    "summary", "Case CAS-2026-44192 logged. P2 priority. SLA on track.");
            case "Sentiment Analysis Agent" -> mapOf(
                    "overallSentiment", "NEGATIVE", "sentimentScore", -0.62,
                    "topEmotion", "Frustration", "confidence", "91%",
                    "triggerWords", List.of("waiting", "still not resolved", "unacceptable"),
                    "summary", "High frustration detected. Recommend priority escalation path.");
            case "Complaint Management Agent" -> mapOf(
                    "complaintRef", "COMP-2026-0041", "category", "Service Failure",
                    "regulatoryFlag", false, "compensationOffered", "$25 credit",
                    "resolvedIn", "48 hours", "finalOutcome", "UPHELD",
                    "summary", "Complaint upheld. $25 credit applied. Resolved within 48h.");

            // ── Human Resources ────────────────────────────────────────
            case "Talent Acquisition Agent" -> mapOf(
                    "candidateId", "CAND-2026-00721", "role", "Senior Risk Analyst",
                    "screeningScore", 87, "interviewsScheduled", 2,
                    "backgroundCheck", "INITIATED", "offerStatus", "PENDING",
                    "summary", "Candidate screening score 87. 2 interviews scheduled.");
            case "HRMS API Agent" -> mapOf(
                    "employeeId", "EMP-20260328-4412", "hrmsSystem", "Workday",
                    "profileCreated", true, "costCentre", "CC-RISK-042",
                    "startDate", "2026-04-14", "workdayRef", "WD-2026-44120",
                    "summary", "Workday profile created. EMP-20260328-4412 ready for Day 1.");
            case "HR Policy Q&A Agent" -> mapOf(
                    "queryTopic", "Parental Leave", "policyVersion", "v2024.3",
                    "answer", "18 weeks full pay + 8 weeks 50% pay (UK)",
                    "sourceDocument", "Global Benefits Policy 2024", "confidence", "98%",
                    "summary", "Policy answer retrieved with 98% confidence from HR Knowledge Base.");
            case "Payroll API Agent" -> mapOf(
                    "payrollSystem", "ADP Workforce Now", "employeeId", "EMP-20260328-4412",
                    "salary", "£72,000 p.a.", "payFrequency", "Monthly",
                    "firstPayDate", "2026-04-30", "taxCode", "1257L",
                    "summary", "Payroll record created in ADP. First payment 30-Apr-2026.");

            // ── Data & Analytics ──────────────────────────────────────
            case "Data Quality Agent" -> mapOf(
                    "recordsScanned", 4_200_000, "errorsFound", 1_842,
                    "errorRate", "0.044%", "criticalIssues", 12,
                    "topIssue", "Duplicate customer IDs", "pipelineStatus", "REMEDIATION_QUEUED",
                    "summary", "0.044% error rate on 4.2M records. 12 critical issues queued.");
            case "Data Catalog API Agent" -> mapOf(
                    "catalogSystem", "Collibra", "assetsTagged", 2_841,
                    "newAssets", 124, "piiFields", 38,
                    "classificationComplete", "96%", "lastRefresh", "2026-03-28T12:00:00Z",
                    "summary", "2,841 data assets catalogued. 96% classification complete.");
            case "Lineage Tracker Agent" -> mapOf(
                    "sourceSystem", "Temenos T24", "targetReport", "DFAST Capital Report",
                    "hopsTracked", 7, "transformationsFound", 14,
                    "breakagePoints", 0, "lineageConfidence", "100%",
                    "summary", "Full lineage traced: T24 → GL → Risk Engine → DFAST. 0 breaks.");
            case "BI Reporting Agent" -> mapOf(
                    "reportName", "Q1 2026 Revenue Dashboard", "recordsProcessed", 18_920_000,
                    "executionTime", "4m 12s", "chartsGenerated", 24,
                    "publishedTo", "Power BI Workspace", "status", "PUBLISHED",
                    "summary", "Q1 revenue dashboard published to Power BI. 24 charts updated.");

            // ── Finance ───────────────────────────────────────────────
            case "GL Reconciliation Agent" -> mapOf(
                    "glSystem", "SAP S/4HANA", "accountsReconciled", 1_842,
                    "unmatchedItems", 7, "totalVariance", "$1,284.50",
                    "status", "EXCEPTIONS_RAISED", "runDate", "2026-03-28",
                    "summary", "1,842 GL accounts reconciled. 7 exceptions totalling $1,284.50.");
            case "FX Rate Feed Agent" -> mapOf(
                    "provider", "Reuters Elektron", "pairsUpdated", 48,
                    "usdEur", 0.9218, "usdGbp", 0.7914, "usdJpy", 151.42,
                    "staleness", "< 1 second", "status", "LIVE",
                    "summary", "48 FX pairs live from Reuters. Feeds current as of now.");
            case "Tax Compliance Agent" -> mapOf(
                    "jurisdiction", "US Federal + 12 States", "filingsDue", 3,
                    "estimatedLiability", "$4,821,000", "withholdingStatus", "CURRENT",
                    "nextDeadline", "2026-04-15", "auditRisk", "Low",
                    "summary", "3 filings due by 15-Apr. Estimated liability $4.82M. Low audit risk.");
            case "Financial Reporting Agent" -> mapOf(
                    "reportType", "10-Q Draft", "period", "Q1 2026",
                    "revenue", "$1.24B", "netIncome", "$184M",
                    "eps", "$2.41", "draftStatus", "READY_FOR_REVIEW",
                    "summary", "10-Q draft ready. Q1 revenue $1.24B, net income $184M.");

            // ── Risk & Compliance ─────────────────────────────────────
            case "AML / Fraud Detection Agent" -> mapOf(
                    "transactionsScreened", 142_000, "flaggedTransactions", 14,
                    "sarsFiled", 2, "falsePositiveRate", "0.008%",
                    "highRiskCustomers", 6, "modelVersion", "AML-v4.2",
                    "summary", "142K transactions screened. 14 flagged, 2 SARs filed.");
            case "Regulatory Reporting Agent" -> mapOf(
                    "reportType", "CCAR / DFAST", "submissionStatus", "DRAFT",
                    "dataPoints", 84_200, "validationErrors", 0,
                    "regulatorDeadline", "2026-04-30", "coverage", "100%",
                    "summary", "DFAST draft complete. 84,200 data points. Zero validation errors.");
            case "Credit Risk Scoring Agent" -> mapOf(
                    "portfolioSize", "$18.4B", "avgPd", "1.24%",
                    "avgLgd", "38%", "expectedLoss", "$86M",
                    "rcet1Impact", "-12bps", "modelVersion", "IRB Advanced v3",
                    "summary", "IRB model run complete. Expected loss $86M. CET1 impact -12bps.");
            case "Sanctions Screening Agent" -> mapOf(
                    "entitiesScreened", 28_400, "listsChecked", List.of("OFAC SDN", "EU", "UN", "HMT"),
                    "matches", 2, "falsePositives", 1, "confirmedHits", 1,
                    "escalated", true, "summary", "1 confirmed OFAC hit. Escalated to Compliance Officer.");

            // ── Supply Chain ──────────────────────────────────────────
            case "Vendor Risk Agent" -> mapOf(
                    "vendorName", "GlobalParts Inc.", "riskRating", "MEDIUM",
                    "financialScore", 72, "cyberScore", 68,
                    "geopoliticalRisk", "Low", "recommendedAction", "APPROVE_WITH_MONITORING",
                    "summary", "Vendor rated MEDIUM risk. Approved with enhanced monitoring.");
            case "ERP API Agent" -> mapOf(
                    "erpSystem", "SAP S/4HANA", "posCreated", 1,
                    "poNumber", "PO-2026-SC-4411", "totalValue", "$284,000",
                    "deliveryDate", "2026-04-18", "status", "APPROVED",
                    "summary", "PO-2026-SC-4411 raised in SAP for $284K. Delivery 18-Apr.");
            case "Logistics Tracking Agent" -> mapOf(
                    "trackingId", "SHIP-2026-TRK-0892", "carrier", "DHL Express",
                    "origin", "Shenzhen, CN", "destination", "New York, US",
                    "etaDate", "2026-04-10", "currentStatus", "IN_TRANSIT",
                    "summary", "Shipment in transit via DHL. ETA New York 10-Apr-2026.");
            case "Inventory Optimization Agent" -> mapOf(
                    "skusAnalyzed", 4_820, "reorderTriggered", 142,
                    "stockOutRisk", "8 SKUs", "overStockValue", "$1.2M",
                    "savingsIdentified", "$340K", "modelRun", "EOQ + Safety Stock",
                    "summary", "142 reorders triggered. $340K savings identified. 8 stock-out risks.");

            // ── Digital Workplace ──────────────────────────────────────
            case "IT Helpdesk Agent" -> mapOf(
                    "ticketId", "INC-2026-044120", "issue", "VPN Connectivity",
                    "priority", "P2", "resolutionTime", "14 minutes",
                    "solution", "Reset MFA token + client config push", "status", "RESOLVED",
                    "summary", "P2 VPN ticket resolved in 14 min via MFA reset + config push.");
            case "ITSM API Agent" -> mapOf(
                    "itsmSystem", "ServiceNow", "ticketRef", "INC0044120",
                    "slaBreached", false, "assignmentGroup", "Network Ops",
                    "escalationLevel", 0, "knowledgeArticle", "KB-VPN-0042",
                    "summary", "ServiceNow record INC0044120 closed. SLA maintained. CSAT pending.");
            case "Collaboration Agent" -> mapOf(
                    "platform", "Microsoft Teams", "channelCreated", "proj-q2-risk-review",
                    "membersAdded", 8, "documentsLinked", 3,
                    "meetingScheduled", true, "kickoffDate", "2026-03-31",
                    "summary", "Teams channel created. 8 members added. Kickoff meeting 31-Mar.");
            case "Identity & Access Agent" -> mapOf(
                    "adAccount", "ssmith@bank.com", "rolesAssigned", List.of("Risk Analyst", "BI Viewer", "SharePoint Read"),
                    "mfaEnabled", true, "accessReviewDate", "2026-09-28",
                    "privilegedAccess", false, "status", "PROVISIONED",
                    "summary", "AD account provisioned. 3 roles assigned. MFA enabled.");

            default -> mapOf("status", "COMPLETE", "summary", agentName + " executed successfully with synthetic data.");
        };
    }

    /** Helper since Map.of doesn't support >10 entries */
    private Map<String, Object> mapOf(Object... kv) {
        Map<String, Object> m = new LinkedHashMap<>();
        for (int i = 0; i < kv.length - 1; i += 2) m.put(kv[i].toString(), kv[i + 1]);
        return m;
    }
}