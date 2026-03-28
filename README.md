# Enterprise Agent Catalog

A financial-services AI agent registry with an MFE shell frontend and a conversational banking assistant.

![Dashboard](diagrams/03_Agent_Catalog_FS_Detail.drawio)

## Overview

The **Enterprise Agent Catalog** is a governance and discovery platform for AI agents deployed across a financial institution. It provides:

- **55 agents** across **11 banking domains** (Retail, Wealth, Commercial & Industrial, Marketing, Customer Experience, HR, Data & Analytics, Finance, Risk & Compliance, Supply Chain, Digital Workplace)
- **MFE Shell** architecture — three independently-maintainable micro-frontend tabs per domain
- **Orchestration simulator** — trigger multi-agent workflows with live execution output
- **Aria** — a conversational banking assistant powered by Spring AI + Claude

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.4.3 · Java 25 · Spring AI 1.0.3 |
| AI Model | Anthropic Claude (`claude-sonnet-4-6`) |
| Frontend | React 18 · Vite · plain CSS |
| Orchestration | n8n (optional) · simulation mode built-in |

## Architecture

```
┌─────────────────────────────────────────────┐
│           MFE Shell  (App.jsx)               │
│  ┌───────────┐ ┌──────────┐ ┌─────────────┐ │
│  │ Catalog   │ │ Registry │ │Orchestration│ │
│  │   MFE     │ │   MFE    │ │    MFE      │ │
│  └───────────┘ └──────────┘ └─────────────┘ │
└──────────────────┬──────────────────────────┘
                   │ /api proxy
         ┌─────────▼──────────┐
         │  Spring Boot :8080  │
         │  AgentService       │
         │  ChatController     │
         │  OrchestrationCtrl  │
         └────────────────────┘
```

## Quick Start

### Prerequisites
- Java 25 (JDK 25)
- Maven 3.9+
- Node.js 18+
- Anthropic API key (for the Aria chat assistant)

### Run the backend

```bash
export ANTHROPIC_API_KEY=sk-ant-...
mvn spring-boot:run
# Serves on http://localhost:8080
```

### Run the frontend

```bash
cd frontend
npm install
npm run dev
# Opens http://localhost:5173
```

The Vite dev server proxies `/api` → `http://localhost:8080`.

## Features

### Domain Dashboard
- Stats bar: total agents, orchestrators, AI agents, API wrappers, critical risk count
- 11 domain cards — click any to enter the domain view
- **Critical Risk panel** — click the ⚠️ stat tile to see all 6 critical-risk agents across domains

### Catalog MFE (per domain)
- Agent card grid with risk badge, type, compliance status
- Filter by risk tier, status, agent type, data classification
- Search by name
- Click any card → full governance detail panel

### Registry MFE (per domain)
- Full governance table: ID, Owner, AI Model, SLA, Compliance, Risk Tier, Data Classification
- Active agents highlighted in green when an orchestration workflow is running (cross-tab state)

### Orchestration MFE (per domain)
- Select an orchestrator and ask a natural-language question
- Animated workflow execution (n8n live or built-in simulation)
- Live agent outputs panel on the right
- Execution log

### Aria — Banking Assistant
- Floating chat widget (bottom-right on all screens)
- Handles: balance enquiry, fund transfer, transaction history, product info, complaint logging, lost card, escalation
- Conversational memory within session (Spring AI `MessageWindowChatMemory`)

## n8n Integration (optional)

```bash
npx n8n
# Import a workflow JSON from src/main/resources/n8n-workflows/
# Activate the workflow, then click ▶ Execute in the UI
```

If n8n is offline the UI runs in **simulation mode** with synthetic data automatically.

## API Endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/api/agents` | All agents (supports `?domain=&riskTier=&status=&search=` filters) |
| GET | `/api/agents/{id}` | Single agent |
| GET | `/api/domains` | 11 domains with agent counts |
| GET | `/api/stats` | Aggregate stats |
| POST | `/api/orchestrate/{agentId}` | Trigger or simulate an orchestration workflow |
| POST | `/api/chat` | Chat with Aria (`{sessionId, message}`) |

## Agent Types

| Type | Description |
|---|---|
| `ORCHESTRATOR` | Coordinates a multi-agent pipeline |
| `AI_AGENT` | LLM-powered reasoning agent |
| `API_WRAPPER` | Thin wrapper over an internal/external API |
| `PRODUCT_API_AGENT` | Wraps a specific banking product API |

## Risk Tiers

| Tier | Meaning |
|---|---|
| `CRITICAL` | Direct financial impact · high autonomy · restricted data · regulatory exposure |
| `HIGH` | Significant business impact, compensating controls in place |
| `MEDIUM` | Limited blast radius, advisory or read-only |
| `LOW` | Informational, no direct system writes |

## License

MIT