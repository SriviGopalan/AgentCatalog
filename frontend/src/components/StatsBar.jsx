import './StatsBar.css'

export default function StatsBar({ stats, onCriticalClick }) {
  if (!stats) return null
  return (
    <div className="stats-bar">
      <div className="stat-tile stat-total">
        <span className="stat-icon">🤖</span>
        <div className="stat-info">
          <span className="stat-value">{stats.totalAgents ?? '—'}</span>
          <span className="stat-label">Total Agents</span>
        </div>
      </div>
      <div className="stat-tile stat-orchestrator">
        <span className="stat-icon">🔀</span>
        <div className="stat-info">
          <span className="stat-value">{stats.orchestrators ?? '—'}</span>
          <span className="stat-label">Orchestrators</span>
        </div>
      </div>
      <div className="stat-tile stat-ai">
        <span className="stat-icon">🧠</span>
        <div className="stat-info">
          <span className="stat-value">{stats.aiAgents ?? '—'}</span>
          <span className="stat-label">AI Agents</span>
        </div>
      </div>
      <div className="stat-tile stat-api">
        <span className="stat-icon">🔌</span>
        <div className="stat-info">
          <span className="stat-value">{stats.apiWrappers ?? '—'}</span>
          <span className="stat-label">API Wrappers</span>
        </div>
      </div>
      <div className="stat-tile stat-product">
        <span className="stat-icon">📦</span>
        <div className="stat-info">
          <span className="stat-value">{stats.productApiAgents ?? '—'}</span>
          <span className="stat-label">Product APIs</span>
        </div>
      </div>
      <div className="stat-tile stat-critical stat-tile-clickable" onClick={onCriticalClick} title="View all critical risk agents">
        <span className="stat-icon">⚠️</span>
        <div className="stat-info">
          <span className="stat-value">{stats.criticalRiskAgents ?? '—'}</span>
          <span className="stat-label">Critical Risk ›</span>
        </div>
      </div>
      <div className="stat-tile stat-domains">
        <span className="stat-icon">🏛️</span>
        <div className="stat-info">
          <span className="stat-value">{stats.domains ?? '—'}</span>
          <span className="stat-label">Domains</span>
        </div>
      </div>
    </div>
  )
}