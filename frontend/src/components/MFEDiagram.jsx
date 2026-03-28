import { useEffect } from 'react'
import './MFEDiagram.css'

function PropArrow({ label, direction = 'down' }) {
  return (
    <div className={`mfe-prop-arrow mfe-arrow-${direction}`}>
      <div className="mfe-arrow-line" />
      <div className="mfe-arrow-tip">{direction === 'down' ? '▼' : '▲'}</div>
      {label && <div className="mfe-arrow-label">{label}</div>}
    </div>
  )
}

function Prop({ name, type, dir = 'in' }) {
  return (
    <div className={`mfe-prop mfe-prop-${dir}`}>
      <span className="mfe-prop-dir">{dir === 'in' ? '↓' : '↑'}</span>
      <span className="mfe-prop-name">{name}</span>
      <span className="mfe-prop-type">{type}</span>
    </div>
  )
}

export default function MFEDiagram({ onClose }) {
  useEffect(() => {
    const h = (e) => { if (e.key === 'Escape') onClose() }
    window.addEventListener('keydown', h)
    return () => window.removeEventListener('keydown', h)
  }, [onClose])

  return (
    <>
      <div className="mfed-backdrop" onClick={onClose} />
      <div className="mfed-modal" role="dialog">

        {/* ── Header ── */}
        <div className="mfed-header">
          <div className="mfed-header-left">
            <div className="mfed-header-icon-wrap">
              <span className="mfed-header-icon">⬡</span>
            </div>
            <div>
              <div className="mfed-header-title">MFE Shell Architecture</div>
              <div className="mfed-header-sub">Component hierarchy · State flow · Cross-tab communication</div>
            </div>
          </div>
          <div className="mfed-header-right">
            <span className="mfed-tech-tag">React + Vite</span>
            <span className="mfed-tech-tag">Spring Boot 3.4</span>
            <button className="mfed-close" onClick={onClose}>✕</button>
          </div>
        </div>

        <div className="mfed-body">

          {/* ════════════════ TIER 1: API LAYER ════════════════ */}
          <div className="mfed-tier-label">Backend · Data Source</div>
          <div className="mfed-api-row">
            <div className="mfed-api-node">
              <span className="mfed-api-icon">☕</span>
              <div>
                <div className="mfed-api-title">Spring Boot 3.4 · Port 8080</div>
                <div className="mfed-api-endpoints">
                  <span>GET /api/agents</span>
                  <span>GET /api/domains</span>
                  <span>GET /api/stats</span>
                  <span>POST /api/orchestrate/{'{id}'}</span>
                  <span>POST /api/chat</span>
                </div>
              </div>
            </div>
          </div>

          {/* arrow up to shell */}
          <div className="mfed-v-connector">
            <div className="mfed-vc-line" />
            <div className="mfed-vc-label">Promise.all fetch on mount · /api proxy → :8080</div>
            <div className="mfed-vc-tip">▼</div>
          </div>

          {/* ════════════════ TIER 2: MFE SHELL ════════════════ */}
          <div className="mfed-tier-label">MFE Shell · App.jsx</div>
          <div className="mfed-shell-node">
            <div className="mfed-shell-header">
              <span className="mfed-shell-icon">🏠</span>
              <div className="mfed-shell-title">MFE Shell — App.jsx</div>
              <span className="mfed-shell-badge">Host Application</span>
            </div>
            <div className="mfed-shell-body">
              <div className="mfed-shell-section">
                <div className="mfed-shell-section-title">State (useState)</div>
                <div className="mfed-state-grid">
                  <div className="mfed-state-item"><span className="mfed-state-var">agents</span><span className="mfed-state-type">Agent[]</span></div>
                  <div className="mfed-state-item"><span className="mfed-state-var">domains</span><span className="mfed-state-type">Domain[]</span></div>
                  <div className="mfed-state-item"><span className="mfed-state-var">stats</span><span className="mfed-state-type">StatsDTO</span></div>
                  <div className="mfed-state-item"><span className="mfed-state-var">currentDomain</span><span className="mfed-state-type">Domain | null</span></div>
                  <div className="mfed-state-item"><span className="mfed-state-var">selectedAgent</span><span className="mfed-state-type">Agent | null</span></div>
                  <div className="mfed-state-item"><span className="mfed-state-var">showFlow</span><span className="mfed-state-type">boolean</span></div>
                </div>
              </div>
              <div className="mfed-shell-section">
                <div className="mfed-shell-section-title">Routing (conditional render)</div>
                <div className="mfed-routing-row">
                  <div className="mfed-route">
                    <span className="mfed-route-cond">currentDomain === null</span>
                    <span className="mfed-route-arrow">→</span>
                    <span className="mfed-route-target">DomainDashboard</span>
                  </div>
                  <div className="mfed-route">
                    <span className="mfed-route-cond">currentDomain !== null</span>
                    <span className="mfed-route-arrow">→</span>
                    <span className="mfed-route-target">DomainView (MFE Container)</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* ════════════════ TIER 3: DOMAIN VIEW ════════════════ */}
          <div className="mfed-v-connector">
            <div className="mfed-vc-line" />
            <div className="mfed-vc-label mfed-vc-props">props: domain · agents[] · onAgentClick · onBack</div>
            <div className="mfed-vc-tip">▼</div>
          </div>

          <div className="mfed-tier-label">MFE Container · DomainView.jsx</div>
          <div className="mfed-container-node">
            <div className="mfed-container-header">
              <span className="mfed-container-icon">🏢</span>
              <div className="mfed-container-title">DomainView — MFE Container</div>
              <span className="mfed-container-badge">Tab Router + Cross-tab State</span>
            </div>
            <div className="mfed-container-body">
              <div className="mfed-container-state">
                <div className="mfed-state-item"><span className="mfed-state-var">activeTab</span><span className="mfed-state-type">'catalog' | 'registry' | 'orchestration'</span></div>
                <div className="mfed-state-item mfed-crossstate"><span className="mfed-state-var">highlightedIds</span><span className="mfed-state-type">Set&lt;string&gt; — cross-tab bridge</span></div>
              </div>
              <div className="mfed-cross-note">
                <span className="mfed-cross-icon">↔</span>
                When Orchestration executes a workflow, it calls <code>onAgentsActive(ids)</code> which sets <code>highlightedIds</code>
                — Registry MFE reads this to highlight active agent rows in green.
              </div>
            </div>
          </div>

          {/* ════════════════ TIER 4: THE 3 MFEs ════════════════ */}
          <div className="mfed-v-connector">
            <div className="mfed-vc-line" />
            <div className="mfed-vc-tip">▼</div>
          </div>

          <div className="mfed-tier-label">Micro-Frontends — independently maintainable modules</div>
          <div className="mfed-mfe-row">

            {/* Catalog MFE */}
            <div className="mfed-mfe mfed-mfe-blue">
              <div className="mfed-mfe-header">
                <span className="mfed-mfe-icon">🗂️</span>
                <div>
                  <div className="mfed-mfe-title">Catalog MFE</div>
                  <div className="mfed-mfe-file">AgentGrid.jsx</div>
                </div>
              </div>
              <div className="mfed-mfe-props">
                <Prop name="agents" type="Agent[]" dir="in" />
                <Prop name="onAgentClick" type="fn(agent)" dir="in" />
                <Prop name="onAgentClick" type="→ AgentDetail" dir="out" />
              </div>
              <div className="mfed-mfe-features">
                <div className="mfed-mfe-feat">Card grid layout</div>
                <div className="mfed-mfe-feat">Filter sidebar</div>
                <div className="mfed-mfe-feat">Search bar</div>
                <div className="mfed-mfe-feat">Risk badge system</div>
              </div>
            </div>

            {/* Registry MFE */}
            <div className="mfed-mfe mfed-mfe-purple">
              <div className="mfed-mfe-header">
                <span className="mfed-mfe-icon">📋</span>
                <div>
                  <div className="mfed-mfe-title">Registry MFE</div>
                  <div className="mfed-mfe-file">RegistryView.jsx</div>
                </div>
              </div>
              <div className="mfed-mfe-props">
                <Prop name="agents" type="Agent[]" dir="in" />
                <Prop name="onAgentClick" type="fn(agent)" dir="in" />
                <Prop name="highlightedIds" type="Set&lt;string&gt;" dir="in" />
                <Prop name="onAgentClick" type="→ AgentDetail" dir="out" />
              </div>
              <div className="mfed-mfe-features">
                <div className="mfed-mfe-feat">Full governance table</div>
                <div className="mfed-mfe-feat">All metadata columns</div>
                <div className="mfed-mfe-feat mfed-feat-highlight">🟢 Row highlight (cross-tab)</div>
              </div>
            </div>

            {/* Orchestration MFE */}
            <div className="mfed-mfe mfed-mfe-orange">
              <div className="mfed-mfe-header">
                <span className="mfed-mfe-icon">🔀</span>
                <div>
                  <div className="mfed-mfe-title">Orchestration MFE</div>
                  <div className="mfed-mfe-file">OrchestrationView.jsx</div>
                </div>
              </div>
              <div className="mfed-mfe-props">
                <Prop name="agents" type="Agent[]" dir="in" />
                <Prop name="onAgentsActive" type="fn(Set)" dir="in" />
                <Prop name="onAgentsActive" type="→ highlightedIds" dir="out" />
              </div>
              <div className="mfed-mfe-features">
                <div className="mfed-mfe-feat">Question input</div>
                <div className="mfed-mfe-feat">Flow diagram + animation</div>
                <div className="mfed-mfe-feat">Execution log</div>
                <div className="mfed-mfe-feat mfed-feat-highlight">📊 Live agent outputs</div>
              </div>
            </div>

          </div>{/* end mfe-row */}

          {/* ════════════════ SHARED OVERLAY ════════════════ */}
          <div className="mfed-shared-row">
            <div className="mfed-shared-node mfed-shared-indigo">
              <span>📋</span>
              <div>
                <div className="mfed-shared-title">AgentDetail Panel</div>
                <div className="mfed-shared-desc">Shared overlay · opened by Catalog or Registry MFE · state lifted to App.jsx</div>
              </div>
            </div>
            <div className="mfed-shared-node mfed-shared-teal">
              <span>💬</span>
              <div>
                <div className="mfed-shared-title">ChatWidget — Aria</div>
                <div className="mfed-shared-desc">Global floating widget · independent of domain/tab · Spring AI + Claude</div>
              </div>
            </div>
          </div>

          {/* ════════════════ LEGEND ════════════════ */}
          <div className="mfed-legend">
            <span className="mfed-leg-item mfed-leg-blue">🗂️ Catalog MFE</span>
            <span className="mfed-leg-item mfed-leg-purple">📋 Registry MFE</span>
            <span className="mfed-leg-item mfed-leg-orange">🔀 Orchestration MFE</span>
            <span className="mfed-leg-sep"/>
            <span className="mfed-leg-item mfed-leg-green">↔ Cross-tab state bridge</span>
            <span className="mfed-leg-item mfed-leg-gray">↓ Props in · ↑ Events up</span>
          </div>

        </div>
      </div>
    </>
  )
}