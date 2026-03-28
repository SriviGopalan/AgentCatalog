import { useState } from 'react'
import './RegistryView.css'
import Badge from './Badge'

const AGENT_TYPE_ICON = {
  ORCHESTRATOR:      '🔀',
  AI_AGENT:          '🧠',
  API_WRAPPER:       '🔌',
  PRODUCT_API_AGENT: '📦',
}

export default function RegistryView({ agents, onAgentClick, highlightedIds = new Set() }) {
  const [sortCol, setSortCol] = useState('name')
  const [sortDir, setSortDir] = useState('asc')
  const [search, setSearch] = useState('')

  const handleSort = (col) => {
    if (sortCol === col) setSortDir(d => d === 'asc' ? 'desc' : 'asc')
    else { setSortCol(col); setSortDir('asc') }
  }

  const filtered = agents
    .filter(a => !search ||
      a.name.toLowerCase().includes(search.toLowerCase()) ||
      a.description.toLowerCase().includes(search.toLowerCase()))
    .sort((a, b) => {
      const av = (a[sortCol] ?? '').toString().toLowerCase()
      const bv = (b[sortCol] ?? '').toString().toLowerCase()
      return sortDir === 'asc' ? av.localeCompare(bv) : bv.localeCompare(av)
    })

  const SortIcon = ({ col }) => (
    <span className="sort-icon">{sortCol === col ? (sortDir === 'asc' ? '↑' : '↓') : '↕'}</span>
  )

  return (
    <div className="registry-view">
      <div className="registry-toolbar">
        <input
          className="registry-search"
          placeholder="Search agents..."
          value={search}
          onChange={e => setSearch(e.target.value)}
        />
        <div className="registry-toolbar-right">
          {highlightedIds.size > 0 && (
            <span className="registry-active-label">
              <span className="registry-active-dot" />
              {highlightedIds.size} agent{highlightedIds.size !== 1 ? 's' : ''} active in workflow
            </span>
          )}
          <span className="registry-count">{filtered.length} agents</span>
        </div>
      </div>

      <div className="registry-table-wrap">
        <table className="registry-table">
          <thead>
            <tr>
              <th onClick={() => handleSort('id')}>ID <SortIcon col="id" /></th>
              <th onClick={() => handleSort('name')}>Agent Name <SortIcon col="name" /></th>
              <th>Type</th>
              <th onClick={() => handleSort('riskTier')}>Risk <SortIcon col="riskTier" /></th>
              <th onClick={() => handleSort('status')}>Status <SortIcon col="status" /></th>
              <th onClick={() => handleSort('model')}>Model <SortIcon col="model" /></th>
              <th>Product API</th>
              <th onClick={() => handleSort('complianceStatus')}>Compliance <SortIcon col="complianceStatus" /></th>
              <th onClick={() => handleSort('owner')}>Owner <SortIcon col="owner" /></th>
            </tr>
          </thead>
          <tbody>
            {filtered.map(agent => {
              const isActive = highlightedIds.has(agent.id)
              return (
                <tr
                  key={agent.id}
                  onClick={() => onAgentClick(agent)}
                  className={`registry-row ${isActive ? 'registry-row-active' : ''}`}
                >
                  <td>
                    <code className="registry-id">{agent.id}</code>
                    {isActive && <span className="registry-active-indicator" title="Active in workflow">●</span>}
                  </td>
                  <td>
                    <span className="registry-name">
                      {AGENT_TYPE_ICON[agent.agentType]} {agent.name}
                    </span>
                    {agent.agentType === 'ORCHESTRATOR' && agent.orchestrates?.length > 0 && (
                      <div className="registry-orchestrates">
                        {agent.orchestrates.slice(0, 3).join(' → ')}{agent.orchestrates.length > 3 ? ' ...' : ''}
                      </div>
                    )}
                  </td>
                  <td><Badge type="agentType" value={agent.agentType} /></td>
                  <td><Badge type="risk" value={agent.riskTier} /></td>
                  <td><Badge type="status" value={agent.status} /></td>
                  <td><span className="registry-model">{agent.model}</span></td>
                  <td>
                    {agent.productApi
                      ? <span className="registry-product-api" title={agent.productApi}>{agent.productApi}</span>
                      : <span className="registry-na">—</span>}
                  </td>
                  <td>
                    <Badge
                      type="compliance"
                      value={agent.complianceStatus}
                      label={
                        !agent.complianceStatus ? '—'
                        : agent.complianceStatus === 'UNDER_REVIEW' ? 'Under Review'
                        : agent.complianceStatus.charAt(0) + agent.complianceStatus.slice(1).toLowerCase()
                      }
                    />
                  </td>
                  <td><span className="registry-owner" title={agent.owner}>{agent.owner}</span></td>
                </tr>
              )
            })}
          </tbody>
        </table>
      </div>
    </div>
  )
}