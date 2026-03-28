import { useState, useRef, useEffect, useCallback } from 'react'
import './ChatWidget.css'

const QUICK_ACTIONS = [
  { label: '💰 Check balance', text: 'What is my account balance for CHK-001?' },
  { label: '📋 Recent transactions', text: 'Show me my last 5 transactions for CHK-001' },
  { label: '💳 Credit card info', text: 'Tell me about your credit card options' },
  { label: '🏠 Mortgage rates', text: 'What are your current mortgage rates?' },
  { label: '🧑‍💼 Talk to agent', text: 'I would like to speak with a live agent please' },
]

function generateSessionId() {
  return 'session-' + Math.random().toString(36).slice(2, 11)
}

function formatTime(date) {
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}

const WELCOME = {
  role: 'assistant',
  content: "👋 Hi, I'm **Aria**, your Enterprise Banking Assistant. I can help you with account balances, transfers, product questions, and more.\n\nHow can I help you today?",
  time: new Date(),
}

export default function ChatWidget() {
  const [open, setOpen] = useState(false)
  const [messages, setMessages] = useState([WELCOME])
  const [input, setInput] = useState('')
  const [loading, setLoading] = useState(false)
  const [sessionId] = useState(generateSessionId)
  const [hasUnread, setHasUnread] = useState(false)
  const messagesEndRef = useRef(null)
  const inputRef = useRef(null)

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages, loading])

  useEffect(() => {
    if (open) {
      setHasUnread(false)
      setTimeout(() => inputRef.current?.focus(), 100)
    }
  }, [open])

  const sendMessage = useCallback(async (text) => {
    const userText = text ?? input.trim()
    if (!userText || loading) return
    setInput('')

    setMessages(prev => [...prev, { role: 'user', content: userText, time: new Date() }])
    setLoading(true)

    try {
      const res = await fetch('/api/chat', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ sessionId, message: userText }),
      })
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      const data = await res.json()
      setMessages(prev => [...prev, { role: 'assistant', content: data.response, time: new Date() }])
      if (!open) setHasUnread(true)
    } catch (err) {
      setMessages(prev => [...prev, {
        role: 'assistant',
        content: '⚠️ Sorry, I\'m having trouble connecting right now. Please try again in a moment.',
        time: new Date(),
      }])
    } finally {
      setLoading(false)
    }
  }, [input, loading, sessionId, open])

  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      sendMessage()
    }
  }

  // Render markdown-style bold (**text**) simply
  function renderContent(text) {
    const parts = text.split(/(\*\*[^*]+\*\*)/g)
    return parts.map((part, i) =>
      part.startsWith('**') && part.endsWith('**')
        ? <strong key={i}>{part.slice(2, -2)}</strong>
        : part
    )
  }

  return (
    <>
      {/* Floating action button */}
      <button className="chat-fab" onClick={() => setOpen(o => !o)} aria-label="Open banking assistant">
        {open ? '✕' : '💬'}
        {hasUnread && !open && <span className="chat-fab-badge">!</span>}
      </button>

      {/* Chat panel */}
      {open && (
        <div className="chat-panel">
          {/* Header */}
          <div className="chat-header">
            <div className="chat-avatar">🤖</div>
            <div className="chat-header-info">
              <div className="chat-agent-name">Aria — Banking Assistant</div>
              <div className="chat-agent-status">
                <span className="chat-status-dot" />
                Powered by Claude · Enterprise Bank
              </div>
            </div>
            <button className="chat-close" onClick={() => setOpen(false)}>✕</button>
          </div>

          {/* Messages */}
          <div className="chat-messages">
            {messages.map((msg, i) => (
              <div key={i} className={`chat-message ${msg.role}`}>
                <div className="chat-bubble">{renderContent(msg.content)}</div>
                <span className="chat-timestamp">{formatTime(msg.time)}</span>
              </div>
            ))}
            {loading && (
              <div className="chat-message assistant">
                <div className="chat-typing">
                  <span className="typing-dot" />
                  <span className="typing-dot" />
                  <span className="typing-dot" />
                </div>
              </div>
            )}
            <div ref={messagesEndRef} />
          </div>

          {/* Quick actions — only show if only the welcome message */}
          {messages.length === 1 && (
            <div className="chat-quick-actions">
              {QUICK_ACTIONS.map(action => (
                <button
                  key={action.label}
                  className="quick-action-btn"
                  onClick={() => sendMessage(action.text)}
                  disabled={loading}
                >
                  {action.label}
                </button>
              ))}
            </div>
          )}

          {/* Input */}
          <div className="chat-input-area">
            <textarea
              ref={inputRef}
              className="chat-input"
              rows={1}
              placeholder="Ask about your account, products, transfers..."
              value={input}
              onChange={e => setInput(e.target.value)}
              onKeyDown={handleKeyDown}
              disabled={loading}
            />
            <button
              className="chat-send-btn"
              onClick={() => sendMessage()}
              disabled={!input.trim() || loading}
              aria-label="Send message"
            >
              ➤
            </button>
          </div>
        </div>
      )}
    </>
  )
}
