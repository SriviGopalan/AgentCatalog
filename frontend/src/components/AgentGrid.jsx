import './AgentGrid.css'
import AgentCard from './AgentCard'

const DOMAIN_ICONS = {
  // New 11 domains
  'Retail Banking':         '🏦',
  'Wealth Management':      '📈',
  'Commercial & Industrial':'🏢',
  'Marketing':              '📣',
  'Customer Experience':    '💬',
  'Human Resources':        '👥',
  'Data & Analytics':       '🗄️',
  'Finance':                '💰',
  'Risk & Compliance':      '⚠️',
  'Supply Chain':           '🔗',
  'Digital Workplace':      '💻',
  // Legacy domain names (kept for compatibility)
  'Customer & Channels':            '👤',
  'Onboarding & KYC':               '🪪',
  'Service & Case Management':      '🎫',
  'Payments':                       '💳',
  'Cards':                          '💰',
  'Lending':                        '🏦',
  'Fraud/AML & FinCrime':           '🔍',
  'Enterprise Risk':                '⚠️',
  'Treasury & Markets':             '📈',
  'Finance & Regulatory Reporting': '📊',
  'Operations':                     '⚙️',
  'Wealth & Investments':           '🏛️',
  'Cyber & IAM':                    '🔐',
  'Data/Platforms/Engineering':     '🗄️',
  'AI Governance & Controls':       '🤖',
}

export default function AgentGrid({ agents, onAgentClick }) {
  if (agents.length === 0) {
    return (
      <div className="agent-grid-container">
        <div className="empty-state">
          <h3>No agents found</h3>
          <p>Try adjusting your filters or search query.</p>
        </div>
      </div>
    )
  }

  // Group agents by domain
  const byDomain = {}
  for (const agent of agents) {
    if (!byDomain[agent.domain]) byDomain[agent.domain] = []
    byDomain[agent.domain].push(agent)
  }

  const domains = Object.keys(byDomain)

  return (
    <div className="agent-grid-container">
      {domains.map(domain => (
        <section className="domain-section" key={domain}>
          <div className="domain-header">
            <span className="domain-icon">{DOMAIN_ICONS[domain] ?? '🔷'}</span>
            <span className="domain-name">{domain}</span>
            <span className="domain-count">{byDomain[domain].length} agent{byDomain[domain].length !== 1 ? 's' : ''}</span>
          </div>
          <div className="cards-grid">
            {byDomain[domain].map(agent => (
              <AgentCard key={agent.id} agent={agent} onClick={onAgentClick} />
            ))}
          </div>
        </section>
      ))}
    </div>
  )
}