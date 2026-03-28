import { useState, useEffect, useRef } from 'react'
import './OrchestrationView.css'

const TYPE_CONFIG = {
  ORCHESTRATOR:      { icon: '🔀', color: '#f97316', bg: '#fff7ed', border: '#fdba74', label: 'Orchestrator' },
  AI_AGENT:          { icon: '🧠', color: '#7c3aed', bg: '#f3e8ff', border: '#c084fc', label: 'AI Agent' },
  API_WRAPPER:       { icon: '🔌', color: '#059669', bg: '#ecfdf5', border: '#6ee7b7', label: 'API Wrapper' },
  PRODUCT_API_AGENT: { icon: '📦', color: '#1d4ed8', bg: '#dbeafe', border: '#93c5fd', label: 'Product API' },
}

// Rich synthetic output per agent name (client-side, no backend dependency)
const AGENT_OUTPUTS = {
  'KYC Verification Agent':          { result:'VERIFIED', riskScore:18, ofacStatus:'CLEAR', documentType:'Passport', biometricCheck:'PASS', pepFlag:false, summary:'Identity verified. Low-risk profile. No sanctions hits.' },
  'Core Banking API Agent':          { accountNumber:'4521-8832-001', accountType:'Checking', branchCode:'NYC-042', t24Reference:'T24-20260328-00482', status:'CREATED', summary:'Account opened in Temenos T24. Ref T24-20260328-00482.' },
  'Loan Origination Agent':          { decision:'PRE_QUALIFIED', loanAmount:'$45,000', apr:'7.25%', term:'60 months', creditScore:724, dti:'28%', summary:'Pre-qualified for $45K personal loan at 7.25% APR.' },
  'Branch Locator Agent':            { nearestBranch:'42nd Street Branch', distanceMiles:0.3, waitTimeMin:8, atmAvailable:true, summary:'Nearest branch 0.3 mi away. ~8 min wait. ATM available.' },
  'Portfolio Analyzer Agent':        { totalValue:'$2,412,800', ytdReturn:'+11.2%', allocation:'62% Equity / 38% Fixed', sharpeRatio:1.42, benchmark:'S&P 500 +9.8%', summary:'Portfolio outperforming benchmark by 1.4%. Rebalance recommended.' },
  'Market Data Feed Agent':          { sp500:'+0.43%', nasdaq:'+0.61%', us10yr:'4.28%', usdGbp:'0.7914', goldUsd:'$2,318/oz', dataSource:'Bloomberg Terminal', summary:'Live market data pulled. Equities up, bonds flat.' },
  'Risk Profiling Agent':            { riskScore:68, profile:'Moderate-Aggressive', suitability:'PASS', capacityForLoss:'Medium-High', nextReview:'Q3 2026', summary:'Client profile: Moderate-Aggressive. Suitability confirmed.' },
  'Rebalancing Execution Agent':     { ordersPlaced:4, equityBuy:'+$12,400', bondSell:'-$12,400', venue:'NYSE / LSE', status:'EXECUTED', summary:'4 rebalancing orders executed. Portfolio aligned to target.' },
  'Trade Finance Agent':             { lcNumber:'LC-2026-TF-0892', amount:'$1,250,000', currency:'USD', expiryDate:'2026-06-30', status:'ISSUED', summary:'Letter of Credit issued for $1.25M. Expiry 30-Jun-2026.' },
  'Corporate Banking API Agent':     { facilityId:'CBF-2026-00341', creditLimit:'$5,000,000', utilised:'$1,250,000', available:'$3,750,000', ratingGrade:'BB+', summary:'Credit facility verified. $3.75M available headroom.' },
  'Syndication Market Agent':        { dealName:'ABC Corp TLB', totalDealSize:'$180,000,000', participation:'$5,000,000', spread:'SOFR+275bps', status:'ALLOCATING', summary:'Syndication deal allocating. $5M ticket at SOFR+275bps.' },
  'Treasury Management Agent':       { cashPosition:'$42.3M', fxExposure:'EUR 8.2M', hedgeRatio:'78%', liquidityRatio:'142%', status:'COMPLIANT', summary:'Treasury healthy. FX 78% hedged. LCR 142%.' },
  'Personalization Engine':          { segmentId:'SEG-MORTGAGE-REFI-42', audienceSize:18420, topProduct:'Fixed Rate Mortgage', propensityScore:'74%', model:'XGBoost v3.1', summary:'18,420 customers targeted. 74% propensity score.' },
  'CRM Integration Agent':           { recordsUpdated:18420, crmSystem:'Salesforce FSC', campaignId:'CMP-2026-Q1-REFI', syncStatus:'COMPLETE', errors:0, summary:'18,420 CRM records updated for Q1 refi campaign.' },
  'Customer Segmentation Agent':     { totalCustomers:248000, segmentsCreated:7, topSegment:'High-balance savers 35-55', segmentSize:31200, modelAccuracy:'89.3%', summary:'7 micro-segments identified. Top: 31,200 high-balance savers.' },
  'Social Listening Agent':          { mentionsTracked:4821, sentimentScore:'67%', topTopic:'Mobile app UX', npsSignal:'+42', alertsRaised:2, summary:'Positive sentiment 67%. NPS +42. 2 brand risk alerts raised.' },
  'Conversational Assistant (Aria)': { sessionId:'ARIA-20260328-8841', intent:'Balance + Transfer', turnCount:4, resolutionStatus:'RESOLVED', escalated:false, csat:'4.8/5', summary:'Query resolved in 4 turns. CSAT 4.8/5. No escalation needed.' },
  'CRM Service Agent':               { caseId:'CAS-2026-44192', caseType:'Complaint', priority:'P2', slaStatus:'ON_TRACK', estimatedClose:'2026-03-30', summary:'Case P2 logged. SLA on track. Est. close 30-Mar.' },
  'Sentiment Analysis Agent':        { sentiment:'NEGATIVE', score:'-0.62', topEmotion:'Frustration', confidence:'91%', triggerWords:'waiting, unacceptable', summary:'High frustration detected. Recommend priority escalation.' },
  'Complaint Management Agent':      { complaintRef:'COMP-2026-0041', category:'Service Failure', compensation:'$25 credit', resolvedIn:'48 hours', outcome:'UPHELD', summary:'Complaint upheld. $25 credit applied. Resolved 48h.' },
  'Talent Acquisition Agent':        { candidateId:'CAND-2026-00721', role:'Senior Risk Analyst', screeningScore:87, interviewsScheduled:2, backgroundCheck:'INITIATED', summary:'Screening score 87. 2 interviews scheduled.' },
  'HRMS API Agent':                  { employeeId:'EMP-20260328-4412', hrmsSystem:'Workday', profileCreated:true, costCentre:'CC-RISK-042', startDate:'2026-04-14', summary:'Workday profile created. Ready for Day 1.' },
  'HR Policy Q&A Agent':             { queryTopic:'Parental Leave', policyVersion:'v2024.3', answer:'18 weeks full + 8 weeks 50% (UK)', sourceDoc:'Global Benefits 2024', confidence:'98%', summary:'Policy answer retrieved with 98% confidence.' },
  'Payroll API Agent':               { payrollSystem:'ADP Workforce Now', employeeId:'EMP-20260328-4412', salary:'£72,000 p.a.', payFrequency:'Monthly', firstPayDate:'2026-04-30', summary:'Payroll created in ADP. First payment 30-Apr-2026.' },
  'Data Quality Agent':              { recordsScanned:4200000, errorsFound:1842, errorRate:'0.044%', criticalIssues:12, topIssue:'Duplicate customer IDs', summary:'0.044% error rate on 4.2M records. 12 critical issues queued.' },
  'Data Catalog API Agent':          { catalogSystem:'Collibra', assetsTagged:2841, newAssets:124, piiFields:38, classificationComplete:'96%', summary:'2,841 assets catalogued. 96% classification complete.' },
  'Lineage Tracker Agent':           { sourceSystem:'Temenos T24', targetReport:'DFAST Capital', hopsTracked:7, transformations:14, breakagePoints:0, lineageConfidence:'100%', summary:'Full lineage traced: T24 → GL → Risk → DFAST. 0 breaks.' },
  'BI Reporting Agent':              { reportName:'Q1 2026 Revenue Dashboard', recordsProcessed:'18.9M', chartsGenerated:24, publishedTo:'Power BI', status:'PUBLISHED', summary:'Q1 revenue dashboard published to Power BI. 24 charts.' },
  'GL Reconciliation Agent':         { glSystem:'SAP S/4HANA', accountsReconciled:1842, unmatchedItems:7, totalVariance:'$1,284.50', status:'EXCEPTIONS_RAISED', summary:'1,842 GL accounts reconciled. 7 exceptions totalling $1,284.50.' },
  'FX Rate Feed Agent':              { provider:'Reuters Elektron', pairsUpdated:48, usdEur:0.9218, usdGbp:0.7914, usdJpy:151.42, status:'LIVE', summary:'48 FX pairs live from Reuters. All feeds current.' },
  'Tax Compliance Agent':            { jurisdiction:'US Federal + 12 States', filingsDue:3, estimatedLiability:'$4,821,000', nextDeadline:'2026-04-15', auditRisk:'Low', summary:'3 filings due 15-Apr. Liability $4.82M. Low audit risk.' },
  'Financial Reporting Agent':       { reportType:'10-Q Draft', period:'Q1 2026', revenue:'$1.24B', netIncome:'$184M', eps:'$2.41', draftStatus:'READY_FOR_REVIEW', summary:'10-Q draft ready. Q1 revenue $1.24B, net income $184M.' },
  'AML / Fraud Detection Agent':     { transactionsScreened:142000, flagged:14, sarsFiled:2, falsePositiveRate:'0.008%', highRiskCustomers:6, summary:'142K txns screened. 14 flagged, 2 SARs filed. FP rate 0.008%.' },
  'Regulatory Reporting Agent':      { reportType:'CCAR / DFAST', status:'DRAFT', dataPoints:84200, validationErrors:0, regulatorDeadline:'2026-04-30', summary:'DFAST draft complete. 84,200 data points. Zero errors.' },
  'Credit Risk Scoring Agent':       { portfolioSize:'$18.4B', avgPd:'1.24%', avgLgd:'38%', expectedLoss:'$86M', rcet1Impact:'-12bps', summary:'IRB model run. Expected loss $86M. CET1 impact -12bps.' },
  'Sanctions Screening Agent':       { entitiesScreened:28400, listsChecked:'OFAC / EU / UN / HMT', matches:2, confirmedHits:1, escalated:true, summary:'1 confirmed OFAC hit. Escalated to Compliance Officer.' },
  'Vendor Risk Agent':               { vendorName:'GlobalParts Inc.', riskRating:'MEDIUM', financialScore:72, cyberScore:68, recommendation:'APPROVE_WITH_MONITORING', summary:'Vendor MEDIUM risk. Approved with enhanced monitoring.' },
  'ERP API Agent':                   { erpSystem:'SAP S/4HANA', poNumber:'PO-2026-SC-4411', totalValue:'$284,000', deliveryDate:'2026-04-18', status:'APPROVED', summary:'PO raised in SAP for $284K. Delivery 18-Apr.' },
  'Logistics Tracking Agent':        { trackingId:'SHIP-2026-TRK-0892', carrier:'DHL Express', origin:'Shenzhen, CN', destination:'New York, US', eta:'2026-04-10', status:'IN_TRANSIT', summary:'Shipment in transit via DHL. ETA New York 10-Apr.' },
  'Inventory Optimization Agent':    { skusAnalyzed:4820, reorderTriggered:142, stockOutRisk:'8 SKUs', overStockValue:'$1.2M', savingsIdentified:'$340K', summary:'142 reorders triggered. $340K savings identified.' },
  'IT Helpdesk Agent':               { ticketId:'INC-2026-044120', issue:'VPN Connectivity', priority:'P2', resolutionTime:'14 minutes', solution:'MFA reset + config push', status:'RESOLVED', summary:'P2 VPN ticket resolved in 14 min via MFA reset.' },
  'ITSM API Agent':                  { itsmSystem:'ServiceNow', ticketRef:'INC0044120', slaBreached:false, assignmentGroup:'Network Ops', knowledgeArticle:'KB-VPN-0042', summary:'ServiceNow INC0044120 closed. SLA maintained.' },
  'Collaboration Agent':             { platform:'Microsoft Teams', channelCreated:'proj-q2-risk-review', membersAdded:8, documentsLinked:3, kickoffDate:'2026-03-31', summary:'Teams channel created. 8 members added. Kickoff 31-Mar.' },
  'Identity & Access Agent':         { adAccount:'ssmith@bank.com', rolesAssigned:'Risk Analyst, BI Viewer, SharePoint Read', mfaEnabled:true, privilegedAccess:false, status:'PROVISIONED', summary:'AD account provisioned. 3 roles assigned. MFA enabled.' },
}

// Sample questions per domain keyword
const SAMPLE_QUESTIONS = {
  'Retail Banking':         ['Onboard a new retail customer — John Smith, SSN ending 4521', 'Run credit check and open checking account for Jane Doe', 'Process KYC verification for a walk-in customer'],
  'Wealth Management':      ['Rebalance portfolio for client ID WM-2041 to 60/40 allocation', 'Analyze risk exposure for high-net-worth client portfolio', 'Generate quarterly wealth advisory report for top-tier clients'],
  'Commercial & Industrial':['Assess credit facility of $5M for ABC Corp', 'Process trade finance LC for import shipment #TF-0892', 'Review syndication participation for new deal'],
  'Marketing':              ['Launch targeted campaign for mortgage refinance segment', 'Segment customers with >$50K savings for premium product offer', 'Analyze campaign performance for Q1 digital push'],
  'Customer Experience':    ['Resolve escalated complaint from customer ID CX-7741', 'Analyze NPS drop in mobile banking segment', 'Route complex query to specialized support agent'],
  'Human Resources':        ['Onboard new hire Sarah Johnson — IT, Finance, HR setup', 'Process annual performance review cycle for Risk team', 'Answer policy query: parental leave entitlement UK'],
  'Data & Analytics':       ['Run data quality check on customer master data pipeline', 'Refresh BI dashboard with Q1 revenue metrics', 'Trace data lineage for regulatory capital report'],
  'Finance':                ['Execute month-end GL reconciliation for cost centers', 'Calculate FX exposure for USD/EUR positions', 'Generate regulatory filing for Q1 financial statements'],
  'Risk & Compliance':      ['Screen transaction batch for AML/OFAC flags', 'Run credit risk assessment for commercial portfolio', 'Generate DFAST stress test report for regulators'],
  'Supply Chain':           ['Assess vendor risk for new supplier: GlobalParts Inc.', 'Optimize inventory reorder for high-velocity SKUs', 'Track shipment status for PO #SC-4411'],
  'Digital Workplace':      ['Provision IT access for new joiner — role: analyst', 'Resolve P2 ticket: VPN connectivity issue for remote team', 'Audit access permissions for departing employee'],
}

const STEP_DELAY = 900

export default function OrchestrationView({ agents, onAgentsActive }) {
  const orchestrators = agents.filter(a => a.agentType === 'ORCHESTRATOR')
  const [activeOrch, setActiveOrch] = useState(orchestrators[0] || null)
  const [running, setRunning] = useState(false)
  const [activeStep, setActiveStep] = useState(-1)
  const [stepResults, setStepResults] = useState({})
  const [n8nStatus, setN8nStatus] = useState(null)
  const [executionLog, setExecutionLog] = useState([])
  const [agentOutputs, setAgentOutputs] = useState([])
  const [question, setQuestion] = useState('')
  const logBodyRef = useRef(null)

  // Determine domain from agents list
  const domainName = agents[0]?.domain || ''
  const sampleQs = SAMPLE_QUESTIONS[domainName] || [
    'Initiate workflow for this domain',
    'Run end-to-end orchestration',
    'Execute agent pipeline with synthetic data',
  ]

  useEffect(() => {
    fetch('/api/n8n/status')
      .then(r => r.json())
      .then(setN8nStatus)
      .catch(() => setN8nStatus({ status: 'offline' }))
  }, [])

  // Auto-scroll log
  useEffect(() => {
    if (logBodyRef.current) {
      logBodyRef.current.scrollTop = logBodyRef.current.scrollHeight
    }
  }, [executionLog])

  const subAgentNames = activeOrch?.orchestrates || []
  const allSteps = activeOrch
    ? ['Webhook Trigger', ...subAgentNames, 'Aggregate & Respond']
    : []

  const runWorkflow = async (userQuestion) => {
    if (!activeOrch || running) return
    const q = userQuestion || question || `Execute ${activeOrch.name} workflow`

    setRunning(true)
    setActiveStep(-1)
    setStepResults({})
    setExecutionLog([])
    setAgentOutputs([])

    // Identify participating agent IDs for registry highlight
    const participatingIds = new Set(
      agents
        .filter(a => subAgentNames.includes(a.name))
        .map(a => a.id)
    )
    // Include orchestrator itself
    if (activeOrch.id) participatingIds.add(activeOrch.id)
    if (onAgentsActive) onAgentsActive(participatingIds)

    try {
      const res = await fetch(`/api/orchestrate/${activeOrch.id}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ triggeredFrom: 'AgentCatalog UI', question: q }),
      })
      const data = await res.json()
      const isSimulated = data.status === 'simulated'
      const steps = data.simulatedSteps || []

      setExecutionLog([
        `❓ Question: "${q}"`,
        `─────────────────────────────────`,
        isSimulated
          ? `⚠️  n8n offline — simulation mode`
          : `✅  n8n triggered: ${data.webhookUrl}`,
        `📍 Orchestrator: ${activeOrch.name}`,
        `🔗 Sub-agents: ${subAgentNames.join(', ')}`,
        `─────────────────────────────────`,
      ])

      // Animate steps
      for (let i = 0; i < allSteps.length; i++) {
        setActiveStep(i)
        await new Promise(r => setTimeout(r, STEP_DELAY))
        const ms = steps[i]?.durationMs || Math.floor(200 + Math.random() * 600)
        // Use client-side synthetic output (always available); backend output enriches if present
        const backendOutput = steps[i]?.output || {}
        const clientOutput = AGENT_OUTPUTS[allSteps[i]] || null
        const output = clientOutput
          ? { ...clientOutput, ...backendOutput }  // backend data wins on key conflict
          : Object.keys(backendOutput).length > 0 ? backendOutput : null
        setStepResults(prev => ({ ...prev, [i]: { status: 'done', ms } }))
        setExecutionLog(prev => [...prev, `  ✓ ${allSteps[i]} — ${ms}ms`])
        const isTerminal = allSteps[i] === 'Webhook Trigger' || allSteps[i] === 'Aggregate & Respond'
        if (output && !isTerminal) {
          setAgentOutputs(prev => [...prev, { name: allSteps[i], ms, output }])
        }
        await new Promise(r => setTimeout(r, 150))
      }

      setExecutionLog(prev => [
        ...prev,
        `─────────────────────────────────`,
        `✅ Workflow complete — all ${allSteps.length} steps done`,
      ])
      setActiveStep(-1)
    } catch {
      setExecutionLog(prev => [...prev, '❌ Execution failed. Check backend.'])
      if (onAgentsActive) onAgentsActive(new Set())
    } finally {
      setRunning(false)
    }
  }

  const resetWorkflow = () => {
    setActiveStep(-1)
    setStepResults({})
    setExecutionLog([])
    setAgentOutputs([])
    setRunning(false)
    setQuestion('')
    if (onAgentsActive) onAgentsActive(new Set())
  }

  if (!activeOrch) {
    return <div className="orch-empty">No orchestrators found in this domain.</div>
  }

  const isDone = Object.keys(stepResults).length === allSteps.length && !running

  const outputPanel = (
    <div className="orch-right">
      <div className="orch-right-title">
        📊 Agent Outputs
        {agentOutputs.length > 0 && (
          <span className="orch-outputs-count">{agentOutputs.length} of {subAgentNames.length}</span>
        )}
      </div>
      {agentOutputs.length === 0 ? (
        <div className="orch-right-empty">
          {running
            ? <><span className="orch-right-spinner">⏳</span> Waiting for agent results…</>
            : <><span>💡</span> Ask a question and execute the workflow to see live agent outputs here.</>
          }
        </div>
      ) : (
        <div className="orch-right-cards">
          {agentOutputs.map((item, idx) => {
            const cfg = TYPE_CONFIG[agents.find(a => a.name === item.name)?.agentType] || TYPE_CONFIG.AI_AGENT
            const { summary, ...fields } = item.output
            return (
              <div
                key={idx}
                className="orch-output-card"
                style={{ '--out-color': cfg.color, '--out-border': cfg.border, '--out-bg': cfg.bg }}
              >
                <div className="orch-output-card-header">
                  <span className="orch-output-icon">{cfg.icon}</span>
                  <div className="orch-output-name">{item.name}</div>
                  <span className="orch-output-ms">{item.ms}ms</span>
                </div>
                {summary && <div className="orch-output-summary">{summary}</div>}
                <div className="orch-output-fields">
                  {Object.entries(fields).slice(0, 4).map(([k, v]) => (
                    <div key={k} className="orch-output-field">
                      <span className="orch-output-key">{k.replace(/([A-Z])/g, ' $1').toLowerCase()}</span>
                      <span className="orch-output-val">{Array.isArray(v) ? v.join(', ') : String(v)}</span>
                    </div>
                  ))}
                </div>
              </div>
            )
          })}
        </div>
      )}
    </div>
  )

  return (
    <div className="orch-view">
      {/* Sidebar */}
      <div className="orch-sidebar">
        <div className="orch-sidebar-title">Orchestrators</div>
        {orchestrators.map(o => (
          <div
            key={o.id}
            className={`orch-selector-item ${o.id === activeOrch.id ? 'active' : ''}`}
            onClick={() => { setActiveOrch(o); resetWorkflow() }}
          >
            <span className="orch-selector-icon">🔀</span>
            <div>
              <div className="orch-selector-name">{o.name}</div>
              <div className="orch-selector-domain">{o.domain}</div>
            </div>
          </div>
        ))}
        <div className="orch-n8n-status">
          <div className="orch-n8n-label">n8n Status</div>
          {n8nStatus
            ? <div className={`orch-n8n-badge ${n8nStatus.status}`}>{n8nStatus.status === 'online' ? '🟢 Online' : '🔴 Offline'}</div>
            : <div className="orch-n8n-badge">Checking...</div>
          }
          {n8nStatus?.status === 'offline' && <div className="orch-n8n-hint">Run: <code>npx n8n</code></div>}
        </div>
      </div>

      {/* Main: left flow pane + right output pane */}
      <div className="orch-main">

        {/* ── Left pane: question + flow + log ── */}
        <div className="orch-left">
          <div className="orch-canvas-header">
            <div>
              <h3 className="orch-canvas-title">{activeOrch.name}</h3>
              <span className="orch-canvas-domain">{activeOrch.domain}</span>
            </div>
            <div className="orch-canvas-actions">
              <a href={n8nStatus?.n8nUrl || 'http://localhost:5678'} target="_blank" rel="noreferrer" className="btn-n8n-open">
                Open in n8n ↗
              </a>
              {Object.keys(stepResults).length > 0 && !running && (
                <button className="btn-reset" onClick={resetWorkflow}>Reset</button>
              )}
            </div>
          </div>

          {/* Ask a Question */}
          <div className="orch-question-box">
            <div className="orch-question-label">
              <span className="orch-question-icon">💬</span>
              Ask a question to trigger this workflow
            </div>
            <div className="orch-question-input-row">
              <input
                className="orch-question-input"
                placeholder={sampleQs[0]}
                value={question}
                onChange={e => setQuestion(e.target.value)}
                onKeyDown={e => e.key === 'Enter' && !running && runWorkflow()}
                disabled={running}
              />
              <button className={`btn-run ${running ? 'running' : ''}`} onClick={() => runWorkflow()} disabled={running}>
                {running ? '⏳ Running…' : '▶ Execute'}
              </button>
            </div>
            <div className="orch-question-samples">
              {sampleQs.map((q, i) => (
                <button key={i} className="orch-sample-chip" onClick={() => { setQuestion(q); runWorkflow(q) }} disabled={running}>
                  {q}
                </button>
              ))}
            </div>
          </div>

          {/* Workflow flow diagram */}
          <div className="orch-flow">
            {allSteps.map((stepName, i) => {
              const isFirst = i === 0
              const isLast = i === allSteps.length - 1
              const isActive = activeStep === i
              const isStepDone = stepResults[i]?.status === 'done'
              const ms = stepResults[i]?.ms
              let agentForStep = null
              let cfg
              if (isFirst)      cfg = { icon: '⚡', color: '#2563eb', bg: '#dbeafe', border: '#93c5fd', label: 'Trigger' }
              else if (isLast)  cfg = { icon: '📤', color: '#059669', bg: '#dcfce7', border: '#86efac', label: 'Response' }
              else {
                agentForStep = agents.find(a => a.name === stepName)
                cfg = TYPE_CONFIG[agentForStep?.agentType] || TYPE_CONFIG.AI_AGENT
              }
              return (
                <div key={i} className="orch-step-wrap">
                  <div className={`orch-node ${isActive ? 'node-active' : ''} ${isStepDone ? 'node-done' : ''}`}
                    style={{ '--node-color': cfg.color, '--node-bg': cfg.bg, '--node-border': cfg.border }}>
                    <div className="node-icon">{cfg.icon}</div>
                    <div className="node-body">
                      <div className="node-name">{stepName}</div>
                      {agentForStep?.productApi && <div className="node-api">{agentForStep.productApi}</div>}
                      <div className="node-type-label">{cfg.label}</div>
                    </div>
                    <div className="node-status">
                      {isActive && <span className="status-running">●</span>}
                      {isStepDone && <span className="status-done">✓ {ms}ms</span>}
                    </div>
                  </div>
                  {i < allSteps.length - 1 && (
                    <div className={`orch-arrow ${isStepDone ? 'arrow-done' : ''}`}>
                      <div className="arrow-line" /><div className="arrow-head">▶</div>
                    </div>
                  )}
                </div>
              )
            })}
          </div>

          {/* Execution log */}
          {executionLog.length > 0 && (
            <div className="orch-log">
              <div className="orch-log-title">
                Execution Log
                {isDone && <span className="orch-log-done-badge">✓ Complete</span>}
              </div>
              <div className="orch-log-body" ref={logBodyRef}>
                {executionLog.map((line, i) => <div key={i} className="orch-log-line">{line}</div>)}
                {running && <div className="orch-log-line orch-log-cursor">▌</div>}
              </div>
            </div>
          )}

          {/* n8n info */}
          <div className="orch-n8n-info">
            <div className="orch-n8n-info-title">🔗 n8n Integration</div>
            <div className="orch-n8n-info-body">
              <ol>
                <li>Start n8n: <code>npx n8n</code></li>
                <li>Import <code>/n8n-workflows/{activeOrch.id}.json</code></li>
                <li>Activate workflow, then click <strong>▶ Execute</strong></li>
              </ol>
              <p>Offline = simulation mode with synthetic data.</p>
            </div>
          </div>
        </div>

        {/* ── Right pane: agent outputs ── */}
        {outputPanel}
      </div>
    </div>
  )
}