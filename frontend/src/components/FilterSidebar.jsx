import './FilterSidebar.css'

const DOMAINS = [
  'Customer & Channels', 'Onboarding & KYC', 'Service & Case Management',
  'Payments', 'Cards', 'Lending', 'Fraud/AML & FinCrime', 'Enterprise Risk',
  'Treasury & Markets', 'Finance & Regulatory Reporting', 'Operations',
  'Wealth & Investments', 'Cyber & IAM', 'Data/Platforms/Engineering',
  'AI Governance & Controls'
]

export default function FilterSidebar({ filters, onChange, resultCount }) {
  const handleChange = (key, value) => {
    onChange({ ...filters, [key]: value })
  }

  const handleReset = () => {
    onChange({ domain: '', riskTier: '', status: '', dataClassification: '', model: '', complianceStatus: '' })
  }

  return (
    <aside className="filter-sidebar">
      <h3>Filters</h3>

      <div className="filter-group">
        <label className="filter-label">Domain</label>
        <select className="filter-select" value={filters.domain} onChange={e => handleChange('domain', e.target.value)}>
          <option value="">All Domains</option>
          {DOMAINS.map(d => <option key={d} value={d}>{d}</option>)}
        </select>
      </div>

      <div className="filter-group">
        <label className="filter-label">Risk Tier</label>
        <select className="filter-select" value={filters.riskTier} onChange={e => handleChange('riskTier', e.target.value)}>
          <option value="">All Risk Tiers</option>
          <option value="LOW">Low</option>
          <option value="MEDIUM">Medium</option>
          <option value="HIGH">High</option>
          <option value="CRITICAL">Critical</option>
        </select>
      </div>

      <div className="filter-group">
        <label className="filter-label">Status</label>
        <select className="filter-select" value={filters.status} onChange={e => handleChange('status', e.target.value)}>
          <option value="">All Statuses</option>
          <option value="ACTIVE">Active</option>
          <option value="EXPERIMENTAL">Experimental</option>
          <option value="RETIRED">Retired</option>
        </select>
      </div>

      <div className="filter-group">
        <label className="filter-label">Data Classification</label>
        <select className="filter-select" value={filters.dataClassification} onChange={e => handleChange('dataClassification', e.target.value)}>
          <option value="">All Classifications</option>
          <option value="PUBLIC">Public</option>
          <option value="INTERNAL">Internal</option>
          <option value="CONFIDENTIAL">Confidential</option>
          <option value="RESTRICTED">Restricted</option>
        </select>
      </div>

      <div className="filter-group">
        <label className="filter-label">AI Model</label>
        <select className="filter-select" value={filters.model} onChange={e => handleChange('model', e.target.value)}>
          <option value="">All Models</option>
          <option value="GPT-4o">GPT-4o</option>
          <option value="Claude 3.5">Claude 3.5</option>
          <option value="Gemini Pro">Gemini Pro</option>
          <option value="Internal">Internal</option>
          <option value="Llama 3">Llama 3</option>
        </select>
      </div>

      <div className="filter-group">
        <label className="filter-label">Compliance Status</label>
        <select className="filter-select" value={filters.complianceStatus} onChange={e => handleChange('complianceStatus', e.target.value)}>
          <option value="">All Statuses</option>
          <option value="APPROVED">Approved</option>
          <option value="PENDING">Pending</option>
          <option value="UNDER_REVIEW">Under Review</option>
        </select>
      </div>

      <hr className="filter-divider" />
      <button className="filter-reset-btn" onClick={handleReset}>Reset All Filters</button>
      <p className="filter-count">{resultCount} agent{resultCount !== 1 ? 's' : ''} shown</p>
    </aside>
  )
}
