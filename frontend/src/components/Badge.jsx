import './Badge.css'

const AGENT_TYPE_LABELS = {
  AI_AGENT: '🧠 AI Agent',
  API_WRAPPER: '🔌 API Wrapper',
  PRODUCT_API_AGENT: '📦 Product API',
  ORCHESTRATOR: '🔀 Orchestrator',
}

export default function Badge({ type, value, label }) {
  const text = label ?? (type === 'agentType' ? (AGENT_TYPE_LABELS[value] ?? value) : value)
  let className = 'badge'

  if (type === 'risk') className += ` badge-risk-${value}`
  else if (type === 'status') className += ` badge-status-${value}`
  else if (type === 'compliance') className += ` badge-compliance-${value}`
  else if (type === 'data') className += ` badge-data-${value}`
  else if (type === 'model') className += ' badge-model'
  else if (type === 'agentType') className += ` badge-agent-${value}`
  else className += ' badge-default'

  return <span className={className}>{text}</span>
}