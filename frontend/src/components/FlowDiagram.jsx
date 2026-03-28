import { useEffect } from 'react'
import './FlowDiagram.css'

export default function FlowDiagram({ onClose }) {
  useEffect(() => {
    const h = (e) => { if (e.key === 'Escape') onClose() }
    window.addEventListener('keydown', h)
    return () => window.removeEventListener('keydown', h)
  }, [onClose])

  return (
    <>
      <div className="fd-backdrop" onClick={onClose} />
      <div className="fd-modal" role="dialog">

        {/* ── Header ── */}
        <div className="fd-header">
          <div className="fd-header-left">
            <span className="fd-header-icon">◈</span>
            <div>
              <div className="fd-header-title">User Flow — Enterprise Agent Catalog</div>
              <div className="fd-header-sub">End-to-end journey across all MFE modules</div>
            </div>
          </div>
          <button className="fd-close" onClick={onClose}>✕</button>
        </div>

        <div className="fd-body">

          {/* ════════════════════════════════════════════
              JOURNEY RAIL
          ════════════════════════════════════════════ */}
          <div className="fd-rail">
            <div className="fd-rail-step">
              <div className="fd-rail-bubble fd-bubble-navy">🌐</div>
              <div className="fd-rail-label">Launch App</div>
              <div className="fd-rail-sub">localhost:5173</div>
            </div>
            <div className="fd-rail-connector"><div className="fd-rail-line"/><div className="fd-rail-arrow">▶</div></div>
            <div className="fd-rail-step">
              <div className="fd-rail-bubble fd-bubble-blue">📊</div>
              <div className="fd-rail-label">Dashboard</div>
              <div className="fd-rail-sub">11 domains · 55 agents</div>
            </div>
            <div className="fd-rail-connector"><div className="fd-rail-line"/><div className="fd-rail-arrow">▶</div></div>
            <div className="fd-rail-step">
              <div className="fd-rail-bubble fd-bubble-teal">🏢</div>
              <div className="fd-rail-label">Select Domain</div>
              <div className="fd-rail-sub">Click domain card</div>
            </div>
            <div className="fd-rail-connector"><div className="fd-rail-line"/><div className="fd-rail-arrow">▶</div></div>
            <div className="fd-rail-step">
              <div className="fd-rail-bubble fd-bubble-purple">⚡</div>
              <div className="fd-rail-label">Domain View</div>
              <div className="fd-rail-sub">3 MFE tabs</div>
            </div>
            <div className="fd-rail-connector"><div className="fd-rail-line"/><div className="fd-rail-arrow">▶</div></div>
            <div className="fd-rail-step">
              <div className="fd-rail-bubble fd-bubble-green">✅</div>
              <div className="fd-rail-label">Take Action</div>
              <div className="fd-rail-sub">Explore · Govern · Run</div>
            </div>
          </div>

          {/* ════════════════════════════════════════════
              DASHBOARD DETAIL
          ════════════════════════════════════════════ */}
          <div className="fd-down-arrow">↓ opens</div>
          <div className="fd-detail-card fd-card-blue">
            <div className="fd-detail-header">
              <span className="fd-detail-icon">📊</span>
              <div>
                <div className="fd-detail-title">Dashboard</div>
                <div className="fd-detail-desc">Landing screen with stats and all 11 domain entry points</div>
              </div>
            </div>
            <div className="fd-detail-items">
              <div className="fd-item"><span className="fd-item-dot" style={{background:'#2563eb'}}/>Stats Bar — Total · Active · Critical Risk · Domains</div>
              <div className="fd-item"><span className="fd-item-dot" style={{background:'#2563eb'}}/>11 Domain Cards — icon · name · agent count · description</div>
              <div className="fd-item"><span className="fd-item-dot" style={{background:'#2563eb'}}/>Click any card → enters Domain View</div>
            </div>
          </div>

          {/* ════════════════════════════════════════════
              3-COLUMN MFE SECTION
          ════════════════════════════════════════════ */}
          <div className="fd-down-arrow">↓ Domain View opens 3 MFE tabs</div>
          <div className="fd-mfe-tabs-banner">
            <span className="fd-mfe-banner-icon">🗂️</span> Agent Catalog
            <span className="fd-mfe-banner-sep"/>
            <span className="fd-mfe-banner-icon">📋</span> Agent Registry
            <span className="fd-mfe-banner-sep"/>
            <span className="fd-mfe-banner-icon">🔀</span> Orchestration
          </div>

          <div className="fd-three-col">

            {/* ── Catalog MFE ── */}
            <div className="fd-col fd-col-blue">
              <div className="fd-col-header">
                <div className="fd-col-icon-wrap fd-icon-blue">🗂️</div>
                <div>
                  <div className="fd-col-title">Agent Catalog</div>
                  <div className="fd-col-chip">catalog-mfe</div>
                </div>
              </div>
              <div className="fd-col-body">
                <div className="fd-flow-node fd-fn-blue">
                  <div className="fd-fn-num">1</div>
                  <div>
                    <div className="fd-fn-title">Browse Agent Cards</div>
                    <div className="fd-fn-desc">Grid view · domain agents</div>
                  </div>
                </div>
                <div className="fd-col-arrow">↓</div>
                <div className="fd-flow-node fd-fn-blue">
                  <div className="fd-fn-num">2</div>
                  <div>
                    <div className="fd-fn-title">Filter & Search</div>
                    <div className="fd-fn-desc">Risk · Status · Type · Name</div>
                  </div>
                </div>
                <div className="fd-col-arrow">↓ click card</div>
                <div className="fd-flow-node fd-fn-indigo">
                  <div className="fd-fn-num">3</div>
                  <div>
                    <div className="fd-fn-title">Agent Detail Panel</div>
                    <div className="fd-fn-desc">Slide-in · full governance</div>
                  </div>
                </div>
              </div>
              <div className="fd-col-tags">
                <span className="fd-ct fd-ct-blue">ORCHESTRATOR</span>
                <span className="fd-ct fd-ct-blue">AI_AGENT</span>
                <span className="fd-ct fd-ct-blue">API_WRAPPER</span>
              </div>
            </div>

            {/* ── Registry MFE ── */}
            <div className="fd-col fd-col-purple">
              <div className="fd-col-header">
                <div className="fd-col-icon-wrap fd-icon-purple">📋</div>
                <div>
                  <div className="fd-col-title">Agent Registry</div>
                  <div className="fd-col-chip">registry-mfe</div>
                </div>
              </div>
              <div className="fd-col-body">
                <div className="fd-flow-node fd-fn-purple">
                  <div className="fd-fn-num">1</div>
                  <div>
                    <div className="fd-fn-title">Governance Table</div>
                    <div className="fd-fn-desc">Sortable · all 5 domain agents</div>
                  </div>
                </div>
                <div className="fd-col-arrow">↓</div>
                <div className="fd-flow-node fd-fn-purple">
                  <div className="fd-fn-num">2</div>
                  <div>
                    <div className="fd-fn-title">Full Metadata</div>
                    <div className="fd-fn-desc">Owner · Model · SLA · Compliance</div>
                  </div>
                </div>
                <div className="fd-col-arrow">↓ click row</div>
                <div className="fd-flow-node fd-fn-indigo">
                  <div className="fd-fn-num">3</div>
                  <div>
                    <div className="fd-fn-title">Agent Detail Panel</div>
                    <div className="fd-fn-desc">Same panel as Catalog tab</div>
                  </div>
                </div>
              </div>
              <div className="fd-col-tags">
                <span className="fd-ct fd-ct-purple">Risk Tier</span>
                <span className="fd-ct fd-ct-purple">Compliance</span>
                <span className="fd-ct fd-ct-purple">Data Class</span>
              </div>
            </div>

            {/* ── Orchestration MFE ── */}
            <div className="fd-col fd-col-orange">
              <div className="fd-col-header">
                <div className="fd-col-icon-wrap fd-icon-orange">🔀</div>
                <div>
                  <div className="fd-col-title">Orchestration</div>
                  <div className="fd-col-chip">orchestration-mfe</div>
                </div>
              </div>
              <div className="fd-col-body">
                <div className="fd-flow-node fd-fn-orange">
                  <div className="fd-fn-num">1</div>
                  <div>
                    <div className="fd-fn-title">Ask a Question</div>
                    <div className="fd-fn-desc">Natural language · sample chips</div>
                  </div>
                </div>
                <div className="fd-col-arrow">↓ ▶ Execute</div>
                <div className="fd-flow-node fd-fn-orange">
                  <div className="fd-fn-num">2</div>
                  <div>
                    <div className="fd-fn-title">Workflow Runs</div>
                    <div className="fd-fn-desc">Nodes animate · execution log</div>
                  </div>
                </div>
                <div className="fd-col-arrow">↓ completes</div>
                <div className="fd-flow-node fd-fn-green">
                  <div className="fd-fn-num">3</div>
                  <div>
                    <div className="fd-fn-title">Live Agent Outputs</div>
                    <div className="fd-fn-desc">RHS panel · per-agent results</div>
                  </div>
                </div>
                <div className="fd-col-arrow">↓ cross-tab</div>
                <div className="fd-flow-node fd-fn-green">
                  <div className="fd-fn-num">4</div>
                  <div>
                    <div className="fd-fn-title">Registry Highlights</div>
                    <div className="fd-fn-desc">Active agents glow green</div>
                  </div>
                </div>
              </div>
              <div className="fd-col-tags">
                <span className="fd-ct fd-ct-orange">n8n webhook</span>
                <span className="fd-ct fd-ct-orange">Simulation</span>
                <span className="fd-ct fd-ct-orange">Cross-tab state</span>
              </div>
            </div>

          </div>{/* end fd-three-col */}

          {/* ════════════════════════════════════════════
              AGENT DETAIL PANEL
          ════════════════════════════════════════════ */}
          <div className="fd-down-arrow">↓ from Catalog or Registry tab</div>
          <div className="fd-detail-card fd-card-indigo">
            <div className="fd-detail-header">
              <span className="fd-detail-icon">📋</span>
              <div>
                <div className="fd-detail-title">Agent Detail Panel</div>
                <div className="fd-detail-desc">Slide-in from right · close with ✕, Escape, or backdrop</div>
              </div>
            </div>
            <div className="fd-detail-grid">
              <div className="fd-dg-item"><span>🏷️</span> Badges — risk · status · compliance · data class</div>
              <div className="fd-dg-item"><span>📝</span> Description + Underlying Product API</div>
              <div className="fd-dg-item"><span>🏛️</span> Governance — ID · Owner · Model · Autonomy · SLA</div>
              <div className="fd-dg-item"><span>⚖️</span> Regulatory Impact (if applicable)</div>
              <div className="fd-dg-item"><span>🔧</span> Key Capabilities list</div>
              <div className="fd-dg-item"><span>🔗</span> Orchestration Chain (ORCHESTRATOR type only)</div>
            </div>
          </div>

          {/* ════════════════════════════════════════════
              ALWAYS AVAILABLE
          ════════════════════════════════════════════ */}
          <div className="fd-always">
            <div className="fd-always-header">
              <span className="fd-always-badge">Always On</span>
              <span className="fd-always-title">💬 Aria — AI Banking Assistant</span>
              <span className="fd-always-model">claude-sonnet-4-6 · Spring AI 1.0.3</span>
            </div>
            <div className="fd-always-pills">
              {['🏦 Balance','💸 Transfer','📜 Transactions','🏷️ Product Info','📣 Complaint','🆘 Lost Card','👤 Escalate'].map(t => (
                <span key={t} className="fd-pill">{t}</span>
              ))}
            </div>
          </div>

        </div>{/* fd-body */}
      </div>
    </>
  )
}