import { useState, useEffect } from 'react'
import './styles/global.css'
import './App.css'
import Navbar from './components/Navbar'
import StatsBar from './components/StatsBar'
import DomainDashboard from './components/DomainDashboard'
import DomainView from './components/DomainView'
import AgentDetail from './components/AgentDetail'
import ChatWidget from './components/ChatWidget'
import FlowDiagram from './components/FlowDiagram'
import MFEDiagram from './components/MFEDiagram'
import CriticalRiskPanel from './components/CriticalRiskPanel'

const API_BASE = '/api'

export default function App() {
  const [agents, setAgents] = useState([])
  const [domains, setDomains] = useState([])
  const [stats, setStats] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [currentDomain, setCurrentDomain] = useState(null)
  const [selectedAgent, setSelectedAgent] = useState(null)
  const [showFlow, setShowFlow] = useState(false)
  const [showMFE, setShowMFE] = useState(false)
  const [showCritical, setShowCritical] = useState(false)

  useEffect(() => {
    Promise.all([
      fetch(`${API_BASE}/agents`).then(r => r.json()),
      fetch(`${API_BASE}/domains`).then(r => r.json()),
      fetch(`${API_BASE}/stats`).then(r => r.json()),
    ])
      .then(([agentData, domainData, statsData]) => {
        setAgents(agentData)
        setDomains(domainData)
        setStats(statsData)
        setLoading(false)
      })
      .catch(err => {
        setError(err.message)
        setLoading(false)
      })
  }, [])

  const handleSelectDomain = (domainName) => {
    const domain = domains.find(d => d.name === domainName)
    setCurrentDomain(domain || { name: domainName, icon: '🔷', description: '' })
  }

  const handleHome = () => {
    setCurrentDomain(null)
  }

  if (loading) {
    return (
      <div className="app">
        <Navbar totalCount={0} />
        <div className="app-loading">
          <span className="loading-spinner">⏳</span>
          Loading Agent Registry...
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="app">
        <Navbar totalCount={0} />
        <div className="app-error">
          ⚠️ Failed to load agents: {error}
          <br />
          <small>Is the Spring Boot backend running on port 8080?</small>
        </div>
      </div>
    )
  }

  return (
    <div className="app">
      <Navbar
        totalCount={stats?.totalAgents ?? agents.length}
        currentDomain={currentDomain?.name}
        onHome={handleHome}
        onFlowDiagram={() => setShowFlow(true)}
        onMFEDiagram={() => setShowMFE(true)}
      />
      {!currentDomain && <StatsBar stats={stats} onCriticalClick={() => setShowCritical(true)} />}

      <div className="app-body">
        {!currentDomain ? (
          <DomainDashboard
            domains={domains}
            agents={agents}
            onSelect={handleSelectDomain}
          />
        ) : (
          <DomainView
            domain={currentDomain}
            agents={agents}
            onBack={handleHome}
            onAgentClick={setSelectedAgent}
          />
        )}
      </div>

      {selectedAgent && (
        <AgentDetail agent={selectedAgent} onClose={() => setSelectedAgent(null)} />
      )}
      {showFlow && <FlowDiagram onClose={() => setShowFlow(false)} />}
      {showMFE && <MFEDiagram onClose={() => setShowMFE(false)} />}
      {showCritical && (
        <CriticalRiskPanel
          agents={agents}
          onClose={() => setShowCritical(false)}
          onAgentClick={setSelectedAgent}
        />
      )}}
      <ChatWidget />
    </div>
  )
}