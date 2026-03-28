import { useEffect } from 'react'
import './AgentDetail.css'
import Badge from './Badge'

export default function AgentDetail({ agent, onClose }) {
  useEffect(() => {
    const handler = (e) => { if (e.key === 'Escape') onClose() }
    window.addEventListener('keydown', handler)
    return () => window.removeEventListener('keydown', handler)
  }, [onClose])

  if (!agent) return null

  const complianceLabel = !agent.complianceStatus ? '—'
    : agent.complianceStatus === 'UNDER_REVIEW' ? 'Under Review'
    : agent.complianceStatus.charAt(0) + agent.complianceStatus.slice(1).toLowerCase()

  return (
    <>
      <div className="detail-backdrop" onClick={onClose} />
      <aside className="detail-panel" role="dialog" aria-label={`Agent details: ${agent.name}`}>
        <div className="detail-header">
          <div className="detail-header-top">
            <div>
              <h2 className="detail-title">{agent.name}</h2>
              <span className="detail-domain-chip">{agent.domain}</span>
            </div>
            <button className="detail-close" onClick={onClose} aria-label="Close">✕</button>
          </div>
          <div className="detail-badges">
            {agent.agentType && <Badge type="agentType" value={agent.agentType} />}
            <Badge type="risk" value={agent.riskTier} label={`Risk: ${agent.riskTier}`} />
            <Badge type="status" value={agent.status} />
            <Badge type="compliance" value={agent.complianceStatus} label={complianceLabel} />
            <Badge type="data" value={agent.dataClassification} label={`Data: ${agent.dataClassification}`} />
          </div>
        </div>

        <div className="detail-body">
          <div className="detail-section">
            <span className="detail-section-title">Description</span>
            <p className="detail-description">{agent.description}</p>
          </div>

          {agent.productApi && (
            <div className="detail-section">
              <span className="detail-section-title">Underlying Product API</span>
              <div className="detail-product-api">
                <span className="detail-product-api-icon">⚙️</span>
                <span className="detail-product-api-name">{agent.productApi}</span>
              </div>
            </div>
          )}

          {agent.agentType === 'ORCHESTRATOR' && agent.orchestrates?.length > 0 && (
            <div className="detail-section">
              <span className="detail-section-title">Orchestration Chain</span>
              <div className="detail-orch-chain">
                <div className="detail-orch-root">
                  <span className="detail-orch-node detail-orch-root-node">{agent.name}</span>
                </div>
                <div className="detail-orch-arrow-down">↓</div>
                <div className="detail-orch-children">
                  {agent.orchestrates.map((name, i) => (
                    <div key={i} className="detail-orch-child-wrap">
                      <div className="detail-orch-connector" />
                      <span className="detail-orch-node detail-orch-child-node">{name}</span>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}

          <div className="detail-section">
            <span className="detail-section-title">Governance Metadata</span>
            <div className="detail-meta-grid">
              <div className="detail-meta-item">
                <span className="detail-meta-label">Agent ID</span>
                <span className="detail-id">{agent.id}</span>
              </div>
              <div className="detail-meta-item">
                <span className="detail-meta-label">Owner</span>
                <span className="detail-meta-value">{agent.owner}</span>
              </div>
              <div className="detail-meta-item">
                <span className="detail-meta-label">AI Model</span>
                <span className="detail-meta-value">{agent.model}</span>
              </div>
              <div className="detail-meta-item">
                <span className="detail-meta-label">Autonomy Level</span>
                <span className="detail-meta-value">{agent.autonomyLevel}</span>
              </div>
              <div className="detail-meta-item">
                <span className="detail-meta-label">SLA</span>
                <span className="detail-meta-value">{agent.sla}</span>
              </div>
            </div>
          </div>

          {agent.regulatoryImpact && agent.regulatoryImpact !== 'None' && (
            <div className="detail-section">
              <span className="detail-section-title">Regulatory Impact</span>
              <div className="detail-regulatory">⚖️ {agent.regulatoryImpact}</div>
            </div>
          )}

          {agent.keyCapabilities?.length > 0 && (
            <div className="detail-section">
              <span className="detail-section-title">Key Capabilities</span>
              <ul className="detail-list">
                {agent.keyCapabilities.map((cap, i) => <li key={i}>{cap}</li>)}
              </ul>
            </div>
          )}

          {agent.controls?.length > 0 && (
            <div className="detail-section">
              <span className="detail-section-title">Controls & Governance</span>
              <ul className="detail-list detail-controls-list">
                {agent.controls.map((ctrl, i) => <li key={i}>{ctrl}</li>)}
              </ul>
            </div>
          )}
        </div>
      </aside>
    </>
  )
}