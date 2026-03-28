import { useState } from 'react'
import './DomainView.css'
import AgentGrid from './AgentGrid'
import RegistryView from './RegistryView'
import OrchestrationView from './OrchestrationView'

const TABS = [
  { id: 'catalog',       label: 'Agent Catalog',  icon: '🗂️', mfe: 'catalog-mfe',        desc: 'Browse & filter agents',    color: 'blue'   },
  { id: 'registry',      label: 'Agent Registry', icon: '📋', mfe: 'registry-mfe',       desc: 'Governance & compliance',   color: 'purple' },
  { id: 'orchestration', label: 'Orchestration',  icon: '🔀', mfe: 'orchestration-mfe',  desc: 'Execute agent workflows',   color: 'orange' },
]

export default function DomainView({ domain, agents, onBack, onAgentClick }) {
  const [activeTab, setActiveTab] = useState('catalog')
  const [highlightedIds, setHighlightedIds] = useState(new Set())

  const domainAgents = agents.filter(a => a.domain === domain.name)

  const handleAgentsActive = (ids) => {
    setHighlightedIds(ids)
    // Switch to registry tab briefly to show highlighted agents, then switch back
  }

  return (
    <div className="domain-view">
      {/* Domain header */}
      <div className="domain-view-header">
        <button className="btn-back" onClick={onBack}>← All Domains</button>
        <span className="domain-view-icon">{domain.icon}</span>
        <div className="domain-view-meta">
          <h2 className="domain-view-title">{domain.name}</h2>
          <p className="domain-view-desc">{domain.description}</p>
        </div>
        <div className="domain-view-stats">
          <span className="domain-view-count">{domainAgents.length}</span>
          <span className="domain-view-count-label">agents</span>
        </div>
      </div>

      {/* MFE Tab Bar */}
      <div className="domain-tab-bar">
        {TABS.map(tab => (
          <button
            key={tab.id}
            className={`domain-tab domain-tab-${tab.color} ${activeTab === tab.id ? 'active' : ''}`}
            onClick={() => setActiveTab(tab.id)}
          >
            <span className="tab-icon-wrap">
              <span className="tab-icon">{tab.icon}</span>
            </span>
            <div className="tab-text">
              <div className="tab-label-row">
                <span className="tab-label">{tab.label}</span>
                {tab.id === 'registry' && highlightedIds.size > 0 && (
                  <span className="tab-active-badge">{highlightedIds.size} active</span>
                )}
              </div>
              <div className="tab-desc">{tab.desc}</div>
            </div>
            <span className="tab-mfe-chip">{tab.mfe}</span>
          </button>
        ))}
        <div className="tab-bar-mfe-label">
          <span className="mfe-shell-indicator">MFE Shell</span>
        </div>
      </div>

      {/* MFE Content Pane */}
      <div className="domain-mfe-content">
        {activeTab === 'catalog' && (
          <AgentGrid agents={domainAgents} onAgentClick={onAgentClick} />
        )}
        {activeTab === 'registry' && (
          <RegistryView
            agents={domainAgents}
            onAgentClick={onAgentClick}
            highlightedIds={highlightedIds}
          />
        )}
        {activeTab === 'orchestration' && (
          <OrchestrationView
            agents={domainAgents}
            onAgentsActive={handleAgentsActive}
          />
        )}
      </div>

      {/* Active workflow ribbon (shown when agents are highlighted from orchestration) */}
      {highlightedIds.size > 0 && activeTab !== 'orchestration' && (
        <div className="workflow-ribbon">
          <span className="ribbon-dot" />
          <span>
            Workflow active — {highlightedIds.size} agent{highlightedIds.size !== 1 ? 's' : ''} executing.
            {' '}
            <button className="ribbon-tab-link" onClick={() => setActiveTab('registry')}>
              View in Registry
            </button>
            {' · '}
            <button className="ribbon-tab-link" onClick={() => setActiveTab('orchestration')}>
              View Orchestration
            </button>
          </span>
          <button className="ribbon-dismiss" onClick={() => setHighlightedIds(new Set())}>✕</button>
        </div>
      )}
    </div>
  )
}