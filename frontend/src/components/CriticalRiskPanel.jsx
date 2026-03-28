import { useEffect } from 'react'
import Badge from './Badge'
import './CriticalRiskPanel.css'

export default function CriticalRiskPanel({ agents, onClose, onAgentClick }) {
  const critical = agents.filter(a => a.riskTier === 'CRITICAL')

  useEffect(() => {
    const h = (e) => { if (e.key === 'Escape') onClose() }
    window.addEventListener('keydown', h)
    return () => window.removeEventListener('keydown', h)
  }, [onClose])

  return (
    <>
      <div className="crp-backdrop" onClick={onClose} />
      <aside className="crp-panel" role="dialog">

        <div className="crp-header">
          <div className="crp-header-left">
            <div className="crp-header-icon-wrap">⚠️</div>
            <div>
              <div className="crp-header-title">Critical Risk Agents</div>
              <div className="crp-header-sub">High autonomy · Restricted data · Regulatory exposure</div>
            </div>
          </div>
          <div className="crp-header-right">
            <span className="crp-count-badge">{critical.length} agents</span>
            <button className="crp-close" onClick={onClose}>✕</button>
          </div>
        </div>

        {/* What is critical risk */}
        <div className="crp-explainer">
          <div className="crp-explainer-title">What qualifies as Critical Risk?</div>
          <div className="crp-explainer-pills">
            <span className="crp-pill">💸 Direct financial impact</span>
            <span className="crp-pill">⚡ High autonomy level</span>
            <span className="crp-pill">🔒 Restricted data access</span>
            <span className="crp-pill">⚖️ Regulatory exposure (AML · OFAC · Basel)</span>
            <span className="crp-pill">↩️ Irreversible actions</span>
          </div>
        </div>

        <div className="crp-list">
          {critical.map(agent => (
            <div
              key={agent.id}
              className="crp-row"
              onClick={() => { onAgentClick(agent); onClose() }}
              role="button"
              tabIndex={0}
              onKeyDown={e => e.key === 'Enter' && (onAgentClick(agent), onClose())}
            >
              <div className="crp-row-left">
                <div className="crp-row-header">
                  <span className="crp-agent-name">{agent.name}</span>
                  <Badge type="agentType" value={agent.agentType} />
                </div>
                <div className="crp-agent-domain">
                  <span className="crp-domain-dot" />
                  {agent.domain}
                </div>
                <div className="crp-agent-desc">{agent.description}</div>
                <div className="crp-row-badges">
                  <Badge type="status" value={agent.status} />
                  <Badge type="compliance" value={agent.complianceStatus}
                    label={agent.complianceStatus === 'UNDER_REVIEW' ? 'Under Review'
                      : agent.complianceStatus.charAt(0) + agent.complianceStatus.slice(1).toLowerCase()} />
                  <Badge type="data" value={agent.dataClassification} />
                </div>
              </div>
              <div className="crp-row-right">
                <div className="crp-meta-item">
                  <span className="crp-meta-label">Owner</span>
                  <span className="crp-meta-val">{agent.owner}</span>
                </div>
                <div className="crp-meta-item">
                  <span className="crp-meta-label">Model</span>
                  <span className="crp-meta-val">{agent.model}</span>
                </div>
                <div className="crp-meta-item">
                  <span className="crp-meta-label">Autonomy</span>
                  <span className="crp-meta-val crp-autonomy">{agent.autonomyLevel}</span>
                </div>
                <div className="crp-chevron">›</div>
              </div>
            </div>
          ))}
        </div>

      </aside>
    </>
  )
}