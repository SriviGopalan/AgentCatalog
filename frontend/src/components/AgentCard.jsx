import './AgentCard.css'
import Badge from './Badge'

export default function AgentCard({ agent, onClick }) {
  const isOrchestrator = agent.agentType === 'ORCHESTRATOR'
  return (
    <div
      className={`agent-card${isOrchestrator ? ' agent-card-orchestrator' : ''}`}
      onClick={() => onClick(agent)}
      role="button"
      tabIndex={0}
      onKeyDown={e => e.key === 'Enter' && onClick(agent)}
    >
      <div className="card-header">
        <span className="card-name">{agent.name}</span>
        <Badge type="risk" value={agent.riskTier} />
      </div>
      <div className="card-type-row">
        <Badge type="agentType" value={agent.agentType} />
        {agent.productApi && (
          <span className="card-product-api" title={agent.productApi}>⚙ {agent.productApi}</span>
        )}
      </div>
      <p className="card-description">{agent.description}</p>
      {isOrchestrator && agent.orchestrates?.length > 0 && (
        <div className="card-orchestrates">
          <span className="card-orchestrates-label">Orchestrates:</span>
          <div className="card-orchestrates-list">
            {agent.orchestrates.map((name, i) => (
              <span key={i} className="card-orchestrates-chip">{name}</span>
            ))}
          </div>
        </div>
      )}
      <div className="card-badges">
        <Badge type="status" value={agent.status} />
        <Badge type="compliance" value={agent.complianceStatus} label={agent.complianceStatus === 'UNDER_REVIEW' ? 'Under Review' : agent.complianceStatus.charAt(0) + agent.complianceStatus.slice(1).toLowerCase()} />
        <Badge type="data" value={agent.dataClassification} />
      </div>
      <div className="card-footer">
        <span className="card-owner" title={agent.owner}>👤 {agent.owner}</span>
        <span className="card-sla">SLA: {agent.sla?.split('/')[0]?.trim()}</span>
      </div>
    </div>
  )
}