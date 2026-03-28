import './Navbar.css'

export default function Navbar({ totalCount, currentDomain, onHome, onFlowDiagram, onMFEDiagram }) {
  return (
    <nav className="navbar">
      <div className="navbar-brand" onClick={onHome} style={{ cursor: 'pointer' }}>
        <span className="navbar-icon">🏦</span>
        <div className="navbar-titles">
          <span className="navbar-title">Enterprise Agent Catalog</span>
          <span className="navbar-subtitle">Financial Services AI Registry</span>
        </div>
      </div>
      <div className="navbar-breadcrumb">
        {currentDomain && (
          <>
            <span className="breadcrumb-sep">›</span>
            <span className="breadcrumb-item">{currentDomain}</span>
          </>
        )}
      </div>
      <div className="navbar-meta">
        <button className="navbar-flow-btn" onClick={onFlowDiagram} title="View user flow diagram">
          ◈ User Flow
        </button>
        <button className="navbar-mfe-label" onClick={onMFEDiagram} title="View MFE architecture">
          ⬡ MFE Shell
        </button>
        <span className="navbar-badge">{totalCount} Agents</span>
      </div>
    </nav>
  )
}