import './DomainDashboard.css'

const TYPE_COLORS = {
  ORCHESTRATOR: { bg: '#fff7ed', text: '#9a3412', label: 'Orch' },
  AI_AGENT:     { bg: '#f3e8ff', text: '#6b21a8', label: 'AI' },
  API_WRAPPER:  { bg: '#ecfdf5', text: '#065f46', label: 'API' },
  PRODUCT_API_AGENT: { bg: '#dbeafe', text: '#1e40af', label: 'Prod' },
}

export default function DomainDashboard({ domains, agents, onSelect }) {
  const agentsByDomain = agents.reduce((acc, a) => {
    if (!acc[a.domain]) acc[a.domain] = []
    acc[a.domain].push(a)
    return acc
  }, {})

  return (
    <div className="domain-dashboard">
      <div className="dashboard-header">
        <h2 className="dashboard-title">Agent Registry — Select a Domain</h2>
        <p className="dashboard-subtitle">
          Click a domain card to explore its Agent Catalog, Registry, and n8n Orchestration workflows
        </p>
      </div>

      <div className="domain-grid">
        {domains.map(domain => {
          const domainAgents = agentsByDomain[domain.name] || []
          const typeCounts = domainAgents.reduce((acc, a) => {
            if (a.agentType) acc[a.agentType] = (acc[a.agentType] || 0) + 1
            return acc
          }, {})

          return (
            <div
              key={domain.id}
              className="domain-card"
              onClick={() => onSelect(domain.name)}
              role="button"
              tabIndex={0}
              onKeyDown={e => e.key === 'Enter' && onSelect(domain.name)}
            >
              <div className="domain-card-header">
                <span className="domain-card-icon">{domain.icon}</span>
                <div className="domain-card-count">{domainAgents.length}</div>
              </div>
              <div className="domain-card-name">{domain.name}</div>
              <div className="domain-card-desc">{domain.description}</div>
              <div className="domain-card-types">
                {Object.entries(typeCounts).map(([type, count]) => (
                  <span
                    key={type}
                    className="domain-type-pill"
                    style={{ background: TYPE_COLORS[type]?.bg, color: TYPE_COLORS[type]?.text }}
                  >
                    {TYPE_COLORS[type]?.label} {count}
                  </span>
                ))}
              </div>
              <div className="domain-card-footer">
                <span className="domain-card-explore">Explore →</span>
              </div>
            </div>
          )
        })}
      </div>
    </div>
  )
}