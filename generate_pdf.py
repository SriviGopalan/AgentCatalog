from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import cm
from reportlab.lib import colors
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle,
    HRFlowable, KeepTogether, PageBreak
)
from reportlab.lib.enums import TA_LEFT, TA_CENTER
from reportlab.platypus.flowables import Flowable
from datetime import date

OUTPUT = "/Users/srividya/IdeaProjects/AgentCatalog/AgentCatalog_Architecture.pdf"

# ── Colour palette ────────────────────────────────────────────────
BG_NAV   = colors.HexColor("#0d1117")   # deepest dark
BG_CARD  = colors.HexColor("#161b22")
BORDER   = colors.HexColor("#30363d")
BLUE     = colors.HexColor("#58a6ff")
BLUE_EM  = colors.HexColor("#1f6feb")
GREEN    = colors.HexColor("#3fb950")
AMBER    = colors.HexColor("#d29922")
ORANGE   = colors.HexColor("#f0883e")
RED      = colors.HexColor("#f85149")
TEXT_PRI = colors.HexColor("#e6edf3")
TEXT_SEC = colors.HexColor("#8b949e")
TEXT_MUT = colors.HexColor("#6e7681")
WHITE    = colors.white

# ── Styles ────────────────────────────────────────────────────────
styles = getSampleStyleSheet()

def S(name, **kw):
    return ParagraphStyle(name, **kw)

H1 = S("H1", fontSize=22, textColor=TEXT_PRI, fontName="Helvetica-Bold",
        spaceAfter=6, leading=28)
H2 = S("H2", fontSize=15, textColor=BLUE, fontName="Helvetica-Bold",
        spaceAfter=4, spaceBefore=14, leading=20)
H3 = S("H3", fontSize=12, textColor=TEXT_SEC, fontName="Helvetica-Bold",
        spaceAfter=4, spaceBefore=8, leading=16)
BODY = S("BODY", fontSize=9, textColor=TEXT_SEC, fontName="Helvetica",
         leading=14, spaceAfter=4)
CODE = S("CODE", fontSize=8, textColor=GREEN, fontName="Courier",
         backColor=BG_CARD, borderPadding=(4,6,4,6),
         leading=11, spaceAfter=2)
CAPTION = S("CAPTION", fontSize=8, textColor=TEXT_MUT, fontName="Helvetica-Oblique",
            alignment=TA_CENTER, spaceAfter=8)
SMALL = S("SMALL", fontSize=8, textColor=TEXT_MUT, fontName="Helvetica", leading=12)
LABEL = S("LABEL", fontSize=8, textColor=WHITE, fontName="Helvetica-Bold",
          backColor=BLUE_EM, borderPadding=(2,6,2,6), leading=12)

def sp(h=8): return Spacer(1, h)
def hr(): return HRFlowable(width="100%", thickness=1, color=BORDER, spaceAfter=8, spaceBefore=4)

def h1(t): return Paragraph(t, H1)
def h2(t): return Paragraph(t, H2)
def h3(t): return Paragraph(t, H3)
def body(t): return Paragraph(t, BODY)
def code(t): return Paragraph(t.replace(" ", "&nbsp;").replace("\n","<br/>"), CODE)
def caption(t): return Paragraph(t, CAPTION)
def small(t): return Paragraph(t, SMALL)

# ── Reusable table style builder ──────────────────────────────────
def dark_table(data, col_widths, header_bg=BLUE_EM, row_alt=BG_CARD):
    ts = TableStyle([
        ("BACKGROUND",   (0,0), (-1,0), header_bg),
        ("TEXTCOLOR",    (0,0), (-1,0), WHITE),
        ("FONTNAME",     (0,0), (-1,0), "Helvetica-Bold"),
        ("FONTSIZE",     (0,0), (-1,0), 9),
        ("BOTTOMPADDING",(0,0), (-1,0), 8),
        ("TOPPADDING",   (0,0), (-1,0), 8),
        ("BACKGROUND",   (0,1), (-1,-1), BG_NAV),
        ("ROWBACKGROUNDS",(0,1),(-1,-1),[BG_NAV, BG_CARD]),
        ("TEXTCOLOR",    (0,1), (-1,-1), TEXT_SEC),
        ("FONTNAME",     (0,1), (-1,-1), "Helvetica"),
        ("FONTSIZE",     (0,1), (-1,-1), 8),
        ("GRID",         (0,0), (-1,-1), 0.5, BORDER),
        ("VALIGN",       (0,0), (-1,-1), "MIDDLE"),
        ("TOPPADDING",   (0,1), (-1,-1), 5),
        ("BOTTOMPADDING",(0,1), (-1,-1), 5),
        ("LEFTPADDING",  (0,0), (-1,-1), 8),
        ("RIGHTPADDING", (0,0), (-1,-1), 8),
    ])
    t = Table(data, colWidths=col_widths, repeatRows=1)
    t.setStyle(ts)
    return t

def badge_cell(text, bg, fg=WHITE):
    return Paragraph(
        f'<font color="#{fg.hexval()[2:]}" name="Helvetica-Bold">{text}</font>',
        ParagraphStyle("badge", backColor=bg, borderPadding=(2,5,2,5),
                       fontSize=7, textColor=fg, fontName="Helvetica-Bold",
                       alignment=TA_CENTER)
    )

# ── ASCII-art box helper ──────────────────────────────────────────
def ascii_box(lines, title=""):
    mono = ParagraphStyle("mono", fontSize=7.5, fontName="Courier",
                          textColor=GREEN, backColor=BG_CARD,
                          leading=10, leftIndent=8, rightIndent=8,
                          borderPadding=(8,10,8,10), spaceAfter=4)
    content = "\n".join(lines)
    return Paragraph(content.replace(" ","&nbsp;").replace("\n","<br/>"), mono)


# ═══════════════════════════════════════════════════════════════════
# BUILD DOCUMENT
# ═══════════════════════════════════════════════════════════════════
def build():
    doc = SimpleDocTemplate(
        OUTPUT,
        pagesize=A4,
        leftMargin=1.8*cm, rightMargin=1.8*cm,
        topMargin=2*cm, bottomMargin=2*cm,
        title="Enterprise Agent Catalog — Architecture & Flow",
        author="AgentCatalog",
    )

    story = []
    W = A4[0] - 3.6*cm   # usable width

    # ── Cover ────────────────────────────────────────────────────
    cover = Table(
        [[Paragraph(
            '<font color="#58a6ff" size="26"><b>Enterprise Agent Catalog</b></font><br/>'
            '<font color="#e6edf3" size="13">Architecture &amp; Flow Reference</font><br/><br/>'
            f'<font color="#6e7681" size="9">Financial Services AI Registry  ·  Generated {date.today().strftime("%B %d, %Y")}</font>',
            ParagraphStyle("cover", alignment=TA_CENTER, leading=32)
        )]],
        colWidths=[W],
    )
    cover.setStyle(TableStyle([
        ("BACKGROUND", (0,0), (-1,-1), BG_NAV),
        ("TOPPADDING",  (0,0), (-1,-1), 40),
        ("BOTTOMPADDING",(0,0),(-1,-1), 40),
        ("BOX", (0,0), (-1,-1), 1, BLUE_EM),
    ]))
    story += [cover, sp(20)]

    # Table of contents
    toc_data = [
        ["#", "Section"],
        ["1", "System Overview"],
        ["2", "Full Tech Stack"],
        ["3", "Flow 1 — Catalog Page Load"],
        ["4", "Flow 2 — Filter & Search"],
        ["5", "Flow 3 — Agent Detail Panel"],
        ["6", "Flow 4 — Conversational Banking Assistant (Aria)"],
        ["7", "Flow 5 — Multi-turn Conversation & Memory"],
        ["8", "Flow 6 — Tool Calling Deep Dive"],
        ["9", "Data Flow Summary"],
        ["10", "Security & Compliance Controls"],
    ]
    story.append(h2("Table of Contents"))
    story.append(dark_table(toc_data, [1.2*cm, W-1.2*cm]))
    story.append(PageBreak())

    # ────────────────────────────────────────────────────────────
    # SECTION 1 — SYSTEM OVERVIEW
    # ────────────────────────────────────────────────────────────
    story += [h1("1.  System Overview"), hr()]
    story.append(body(
        "The Enterprise Agent Catalog is a Financial Services AI governance platform consisting of "
        "a <b>Spring Boot REST backend</b>, a <b>React/Vite single-page frontend</b>, and an embedded "
        "<b>Conversational Banking Assistant</b> (Aria) powered by Anthropic Claude via Spring AI. "
        "All three tiers communicate over HTTP/HTTPS using JSON."
    ))
    story.append(sp(10))

    arch = [
        "┌──────────────────────────────────────────────────────────────────┐",
        "│               BROWSER  (http://localhost:5173)                    │",
        "│                                                                  │",
        "│   ┌──────────┐  ┌───────────┐  ┌──────────────┐  ┌──────────┐  │",
        "│   │  Navbar  │  │ StatsBar  │  │FilterSidebar │  │ ChatWidget│  │",
        "│   └──────────┘  └───────────┘  └──────────────┘  └──────────┘  │",
        "│            ┌────────────┐   ┌───────────────────┐               │",
        "│            │ SearchBar  │   │AgentGrid / AgentCard│              │",
        "│            └────────────┘   └───────────────────┘               │",
        "└──────────────────────┬───────────────────┬───────────────────────┘",
        "                       │ GET /api/agents    │ POST /api/chat         ",
        "                       │ GET /api/stats     │ {sessionId, message}   ",
        "              Vite proxy rewrites /api/* → http://localhost:8080     ",
        "                       │                   │                        ",
        "┌──────────────────────▼───────────────────▼───────────────────────┐",
        "│             SPRING BOOT  (http://localhost:8080)                  │",
        "│                                                                   │",
        "│  AgentController          ChatController                          │",
        "│      │                         │                                  │",
        "│  AgentService             Spring AI ChatClient                    │",
        "│  List<Agent> (77)              │                                  │",
        "│  Java Streams filter      MessageChatMemoryAdvisor                │",
        "│                           MessageWindowChatMemory (20 msgs)       │",
        "│                           BankingTools (@Tool methods)            │",
        "└───────────────────────────────┬──────────────────────────────────┘",
        "                                │ HTTPS POST api.anthropic.com      ",
        "                                │ model: claude-sonnet-4-6          ",
        "┌───────────────────────────────▼──────────────────────────────────┐",
        "│                   ANTHROPIC CLAUDE API                            │",
        "│            Reasoning · Tool Selection · Tool Calling              │",
        "│            Conversation Context · Safety Guardrails               │",
        "└───────────────────────────────────────────────────────────────────┘",
    ]
    story.append(ascii_box(arch))
    story.append(caption("Figure 1 — Three-tier architecture: Browser → Spring Boot → Anthropic Claude"))
    story.append(PageBreak())

    # ────────────────────────────────────────────────────────────
    # SECTION 2 — TECH STACK
    # ────────────────────────────────────────────────────────────
    story += [h1("2.  Full Tech Stack"), hr()]

    stack = [
        ["Layer", "Technology", "Version", "Role"],
        ["Frontend Framework", "React", "18", "UI components & state management"],
        ["Frontend Build Tool", "Vite", "7.3", "Dev server, HMR, /api proxy, bundler"],
        ["Styling", "CSS Variables + Modules", "—", "Dark GitHub-style theme system"],
        ["Backend Framework", "Spring Boot", "3.4.3", "REST API, IoC container, auto-config"],
        ["AI Orchestration", "Spring AI", "1.0.3", "LLM client, tool calling, chat memory"],
        ["Large Language Model", "Anthropic Claude", "sonnet-4-6", "Reasoning, NLU, tool selection"],
        ["HTTP Server", "Apache Tomcat (embedded)", "10.1.36", "Servlet container inside Spring Boot"],
        ["Build Tool", "Apache Maven", "3.9.12", "Dependency management, packaging"],
        ["JVM Runtime", "OpenJDK", "25.0", "Java execution environment"],
        ["Chat Memory Store", "InMemoryChatMemoryRepository", "Spring AI 1.0.3", "Per-session message history"],
        ["Transport (UI↔API)", "HTTP/1.1 JSON", "—", "Vite proxy → Spring Boot REST"],
        ["Transport (API↔LLM)", "HTTPS/TLS 1.3", "—", "Spring AI → Anthropic API"],
    ]
    story.append(dark_table(stack, [3.8*cm, 4*cm, 2.2*cm, W-10*cm]))
    story.append(sp(10))

    story.append(h3("Frontend Component Tree"))
    comp_data = [
        ["Component", "File", "Responsibility"],
        ["App", "App.jsx", "Root state: agents, filters, search, selectedAgent. Orchestrates all API calls"],
        ["Navbar", "Navbar.jsx", "Title bar with total agent count badge"],
        ["StatsBar", "StatsBar.jsx", "4 metric tiles: Total, Active, High Risk, Critical Risk"],
        ["FilterSidebar", "FilterSidebar.jsx", "6 filter dropdowns: domain, risk, status, data class, model, compliance"],
        ["SearchBar", "SearchBar.jsx", "Full-width text search input (name + description matching)"],
        ["AgentGrid", "AgentGrid.jsx", "Groups agents by domain, renders section headers + card grid"],
        ["AgentCard", "AgentCard.jsx", "Individual agent tile with badges, description, owner, SLA"],
        ["AgentDetail", "AgentDetail.jsx", "Slide-over panel (right): full metadata, capabilities, controls"],
        ["Badge", "Badge.jsx", "Reusable colored badge for risk tier, status, compliance, data class"],
        ["ChatWidget", "ChatWidget.jsx", "Floating 💬 button → chat panel: Aria banking assistant UI"],
    ]
    story.append(dark_table(comp_data, [2.8*cm, 3.2*cm, W-6*cm]))
    story.append(PageBreak())

    # ────────────────────────────────────────────────────────────
    # SECTION 3 — FLOW 1: PAGE LOAD
    # ────────────────────────────────────────────────────────────
    story += [h1("3.  Flow 1 — Catalog Page Load"), hr()]
    story.append(body(
        "When the user navigates to <b>http://localhost:5173</b>, Vite serves the React bundle. "
        "App.jsx fires two parallel API calls on mount: <b>/api/stats</b> and <b>/api/agents</b>. "
        "Both are proxied by Vite to the Spring Boot backend."
    ))
    story.append(sp(8))

    load_flow = [
        "Browser                    Vite Proxy             Spring Boot              ",
        "  │                           │                        │                   ",
        "  │── GET :5173 ─────────────►│                        │                   ",
        "  │◄─ index.html + JS bundle ─│                        │                   ",
        "  │                           │                        │                   ",
        "  │  React mounts, App.jsx useEffect() fires           │                   ",
        "  │                           │                        │                   ",
        "  ├── GET /api/stats ─────────►── forward ────────────►│                   ",
        "  │                           │              AgentController.getStats()     ",
        "  │                           │              AgentService.getStats()        ",
        "  │                           │              → count active/critical agents ",
        "  │◄─ {totalAgents:77,        │◄─────────────────────────                  ",
        "  │    activeAgents:76,        │                        │                   ",
        "  │    domains:15,             │                        │                   ",
        "  │    criticalRiskAgents:16}  │                        │                   ",
        "  │  StatsBar renders         │                        │                   ",
        "  │                           │                        │                   ",
        "  ├── GET /api/agents ─────────►── forward ────────────►│                  ",
        "  │                           │              AgentController.getAgents()    ",
        "  │                           │              AgentService.getAgents(        ",
        "  │                           │                null,null,null,null,null,null,null)",
        "  │                           │              agents.stream()                ",
        "  │                           │                .filter(... all pass ...)    ",
        "  │                           │                .collect(toList())  → 77     ",
        "  │◄─ JSON array [77 agents] ──◄────────────────────────                   ",
        "  │                           │                        │                   ",
        "  │  AgentGrid.jsx:                                    │                   ",
        "  │    group by domain → 15 sections                   │                   ",
        "  │    render 77 AgentCard components                  │                   ",
        "  │  Page fully rendered                               │                   ",
    ]
    story.append(ascii_box(load_flow))
    story.append(caption("Figure 2 — Initial page load sequence"))
    story.append(PageBreak())

    # ────────────────────────────────────────────────────────────
    # SECTION 4 — FLOW 2: FILTER & SEARCH
    # ────────────────────────────────────────────────────────────
    story += [h1("4.  Flow 2 — Filter & Search"), hr()]
    story.append(body(
        "All filter state lives in <b>App.jsx</b>. Any change triggers a <b>useEffect</b> dependency, "
        "which rebuilds the query string and re-fetches <b>/api/agents</b>. The backend uses "
        "<b>Java Streams</b> to chain-filter the in-memory agent list. No database is involved."
    ))
    story.append(sp(8))

    filter_flow = [
        "User selects  Risk Tier = CRITICAL  in FilterSidebar                       ",
        "         │                                                                  ",
        "         ▼  onChange({...filters, riskTier: 'CRITICAL'})                   ",
        "    App.jsx state update                                                    ",
        "         │                                                                  ",
        "         ▼  useEffect([filters, search]) fires                             ",
        "         │                                                                  ",
        "    GET /api/agents?riskTier=CRITICAL                                       ",
        "         │                                                                  ",
        "         ▼  Vite proxy rewrites → http://localhost:8080/api/agents?...      ",
        "         │                                                                  ",
        "    AgentController.getAgents(                                              ",
        "       domain=null, riskTier='CRITICAL', status=null, ...)                 ",
        "         │                                                                  ",
        "         ▼                                                                  ",
        "    AgentService.getAgents()                                                ",
        "       agents.stream()                                                      ",
        "         .filter(a -> 'CRITICAL'.equalsIgnoreCase(a.getRiskTier().name()))  ",
        "         .collect(Collectors.toList())  →  16 agents pass                  ",
        "         │                                                                  ",
        "         ▼  JSON [16 agents]                                               ",
        "    React: setAgents(data)  →  AgentGrid re-renders  →  16 cards shown     ",
        "    FilterSidebar shows: '16 agents shown'                                  ",
        "                                                                            ",
        "   ─────── Search: User types 'AML' ─────────────────────────────────────  ",
        "                                                                            ",
        "    GET /api/agents?search=AML                                              ",
        "         │                                                                  ",
        "    AgentService: .filter(a ->                                              ",
        "       a.getName().toLowerCase().contains('aml') ||                         ",
        "       a.getDescription().toLowerCase().contains('aml'))                    ",
        "    →  2 agents returned                                                    ",
    ]
    story.append(ascii_box(filter_flow))
    story.append(caption("Figure 3 — Filter and search flow (client state → server-side Java Stream filtering)"))
    story.append(PageBreak())

    # ────────────────────────────────────────────────────────────
    # SECTION 5 — FLOW 3: AGENT DETAIL
    # ────────────────────────────────────────────────────────────
    story += [h1("5.  Flow 3 — Agent Detail Panel"), hr()]
    story.append(body(
        "Clicking an agent card opens a <b>slide-over detail panel</b>. This is a pure client-side "
        "operation — no additional API call is made because the full agent object is already loaded "
        "in the React state from the initial /api/agents fetch."
    ))
    story.append(sp(8))

    detail_flow = [
        "User clicks AgentCard                                                       ",
        "         │                                                                  ",
        "         ▼  onClick(agent) prop                                             ",
        "    App.jsx: setSelectedAgent(agent)                                        ",
        "         │   ← no API call — object already in memory                      ",
        "         ▼                                                                  ",
        "    {selectedAgent && <AgentDetail agent={selectedAgent} />}                ",
        "         │                                                                  ",
        "         ▼                                                                  ",
        "    AgentDetail.jsx renders:                                                ",
        "       ┌────────────────────────────────────────────────┐                  ",
        "       │ [slide-over panel from right]                  │                  ",
        "       │  Header: name + risk/status/compliance badges  │                  ",
        "       │  Description paragraph                         │                  ",
        "       │  Governance metadata grid (6 fields)           │                  ",
        "       │  Regulatory impact banner (if applicable)      │                  ",
        "       │  Key Capabilities list                         │                  ",
        "       │  Controls & Governance list                    │                  ",
        "       └────────────────────────────────────────────────┘                  ",
        "         │                                                                  ",
        "    useEffect: window.addEventListener('keydown', Escape handler)           ",
        "                                                                            ",
        "   ─────── Close actions ────────────────────────────────────────────────  ",
        "    ✕ button click    →  onClose()  →  setSelectedAgent(null)              ",
        "    Backdrop click    →  onClose()  →  setSelectedAgent(null)              ",
        "    Escape key press  →  onClose()  →  setSelectedAgent(null)              ",
        "         │                                                                  ",
        "    AgentDetail unmounts, panel slides out                                  ",
    ]
    story.append(ascii_box(detail_flow))
    story.append(caption("Figure 4 — Agent detail panel: client-side only, no extra network call"))
    story.append(PageBreak())

    # ────────────────────────────────────────────────────────────
    # SECTION 6 — FLOW 4: ARIA AGENT
    # ────────────────────────────────────────────────────────────
    story += [h1("6.  Flow 4 — Conversational Banking Assistant (Aria)"), hr()]
    story.append(body(
        "The ChatWidget sends a <b>POST /api/chat</b> to Spring Boot. The <b>ChatController</b> "
        "passes the message to the <b>Spring AI ChatClient</b>, which prepends conversation history "
        "via the <b>MessageChatMemoryAdvisor</b>, then calls the <b>Anthropic Claude API</b> "
        "with a system prompt, tool definitions, and the full conversation context."
    ))
    story.append(sp(8))

    aria_flow = [
        "User opens 💬 ChatWidget                                                    ",
        "  SessionId generated: 'session-abc123'                                    ",
        "  Welcome message shown (no API call)                                       ",
        "                                                                            ",
        "User types: 'What is my balance for CHK-001?'                              ",
        "         │                                                                  ",
        "         ▼                                                                  ",
        "POST /api/chat                                                              ",
        "Body: { sessionId: 'session-abc123',                                        ",
        "        message:   'What is my balance for CHK-001?' }                      ",
        "         │                                                                  ",
        "         ▼  Vite proxy → http://localhost:8080/api/chat                    ",
        "         │                                                                  ",
        "ChatController.chat(request)                                                ",
        "         │                                                                  ",
        "         ▼                                                                  ",
        "chatClient.prompt()                                                         ",
        "  .advisors(a -> a.param('chat_memory_conversation_id', 'session-abc123'))  ",
        "  .user('What is my balance for CHK-001?')                                  ",
        "  .call()                                                                   ",
        "         │                                                                  ",
        "         ▼                                                                  ",
        "MessageChatMemoryAdvisor.before()                                           ",
        "  Load history for 'session-abc123' from InMemoryChatMemoryRepository      ",
        "  Prepend prior messages to prompt (up to 20 messages window)              ",
        "         │                                                                  ",
        "         ▼  HTTPS POST → api.anthropic.com/v1/messages                    ",
        "Payload:                                                                    ",
        "  { model: 'claude-sonnet-4-6',                                             ",
        "    system: '<Aria persona + compliance rules>',                             ",
        "    messages: [ {role:'user', content:'What is my balance...'} ],           ",
        "    tools: [ getAccountBalance, transferFunds, getProductInfo, ... ],       ",
        "    max_tokens: 1024, temperature: 0.3 }                                    ",
        "         │                                                                  ",
        "Claude reasons → decides to call getAccountBalance                          ",
        "         │                                                                  ",
        "         ▼  Response (stop_reason: 'tool_use')                             ",
        "  { content: [{type:'tool_use', name:'getAccountBalance',                   ",
        "               input:{accountId:'CHK-001'}}] }                              ",
        "         │                                                                  ",
        "Spring AI executes: BankingTools.getAccountBalance('CHK-001')              ",
        "  → 'Account CHK-001 current balance: $12,450.00 (as of 2026-03-01)'       ",
        "         │                                                                  ",
        "         ▼  HTTPS POST → api.anthropic.com/v1/messages  (2nd call)        ",
        "  messages: [ user msg, assistant tool_use, user tool_result ]             ",
        "         │                                                                  ",
        "Claude generates final response (stop_reason: 'end_turn')                  ",
        "  'Your CHK-001 balance is $12,450.00 as of today. Anything else?'         ",
        "         │                                                                  ",
        "MessageChatMemoryAdvisor.after()                                            ",
        "  Save [UserMessage, AssistantMessage] to InMemoryChatMemoryRepository     ",
        "         │                                                                  ",
        "ChatController returns:                                                     ",
        "  { response: 'Your CHK-001 balance is...', sessionId: 'session-abc123' }  ",
        "         │                                                                  ",
        "ChatWidget: append assistant bubble, hide typing indicator                  ",
    ]
    story.append(ascii_box(aria_flow))
    story.append(caption("Figure 5 — Aria banking assistant: full request lifecycle including tool call"))
    story.append(PageBreak())

    # ────────────────────────────────────────────────────────────
    # SECTION 7 — FLOW 5: MULTI-TURN MEMORY
    # ────────────────────────────────────────────────────────────
    story += [h1("7.  Flow 5 — Multi-turn Conversation & Memory"), hr()]
    story.append(body(
        "Every turn stores messages in <b>MessageWindowChatMemory</b> (backed by "
        "<b>InMemoryChatMemoryRepository</b>). On subsequent turns the advisor prepends the "
        "conversation history so Claude maintains full context — enabling pronoun resolution "
        "and implicit account references."
    ))
    story.append(sp(8))

    memory_data = [
        ["Turn", "User Input", "Tool Called", "Memory State After"],
        ["1", "What's my balance for CHK-001?", "getAccountBalance('CHK-001')", "[U1, A1]"],
        ["2", "Transfer $500 to SAV-001", "transferFunds('CHK-001','SAV-001', 500)", "[U1,A1, U2,A2]"],
        ["3", "Show me my transactions now", "getRecentTransactions('CHK-001', 5)", "[U1,A1, U2,A2, U3,A3]"],
        ["4", "I lost my debit card", "reportLostCard('debit')", "[U1..A1, ... U4,A4]"],
        ["5", "Talk to a human please", "escalateToLiveAgent('customer request')", "[U1..A5] — 20 msg cap"],
    ]
    story.append(dark_table(memory_data, [1.2*cm, 4.5*cm, 5*cm, W-10.7*cm]))
    story.append(sp(8))

    mem_flow = [
        "Turn N — ChatClient prompt construction:                                    ",
        "                                                                            ",
        "  MessageChatMemoryAdvisor.before()                                         ",
        "       │                                                                    ",
        "       ▼                                                                    ",
        "  InMemoryChatMemoryRepository.findByConversationId('session-abc123')       ",
        "  → List<Message> [U1, A1, U2, A2, ... U(N-1), A(N-1)]                    ",
        "       │                                                                    ",
        "       ▼                                                                    ",
        "  MessageWindowChatMemory trims to last 20 messages if needed               ",
        "       │                                                                    ",
        "       ▼  Prepended to messages[] sent to Claude                           ",
        "  Claude sees FULL conversation context                                     ",
        "  → can resolve 'my account' from Turn 1 context in Turn 3                 ",
        "                                                                            ",
        "  After response:                                                           ",
        "  MessageChatMemoryAdvisor.after()                                          ",
        "  → InMemoryChatMemoryRepository.saveAll('session-abc123',                  ",
        "       [...existing, UserMessage(N), AssistantMessage(N)])                  ",
    ]
    story.append(ascii_box(mem_flow))
    story.append(caption("Figure 6 — Per-session memory lifecycle: MessageWindowChatMemory (max 20 messages)"))
    story.append(PageBreak())

    # ────────────────────────────────────────────────────────────
    # SECTION 8 — FLOW 6: TOOL CALLING DEEP DIVE
    # ────────────────────────────────────────────────────────────
    story += [h1("8.  Flow 6 — Tool Calling Deep Dive"), hr()]
    story.append(body(
        "Spring AI exposes each <b>@Tool</b>-annotated method in <b>BankingTools.java</b> as a "
        "JSON Schema tool definition sent to Claude. Claude chooses which tool to call (or none). "
        "Spring AI executes the Java method and sends the result back in a second API call."
    ))
    story.append(sp(8))

    tools_data = [
        ["@Tool Method", "Trigger Intent", "Parameters", "Mock Return"],
        ["getAccountBalance", "balance / funds / how much", "accountId: String", "$12,450.00 for CHK-001"],
        ["getRecentTransactions", "history / activity / charges", "accountId, count: int", "Last N transactions list"],
        ["transferFunds", "transfer / send / move money", "fromAccount, toAccount, amount", "TXN-XXXXXX confirmation"],
        ["getProductInfo", "mortgage / loan / card / savings", "productType: String", "Product terms & rates"],
        ["fileComplaint", "complaint / problem / issue", "description: String", "CMP-XXXXXX case ID"],
        ["reportLostCard", "lost / stolen / block card", "cardType: String", "Card blocked + replacement info"],
        ["escalateToLiveAgent", "human / agent / frustrated", "reason: String", "Wait time + contact options"],
    ]
    story.append(dark_table(tools_data, [3.8*cm, 3.8*cm, 3.5*cm, W-11.1*cm]))
    story.append(sp(10))

    tool_flow = [
        "Spring AI → Anthropic API payload (tools array):                            ",
        "  [                                                                         ",
        "    { name: 'getAccountBalance',                                            ",
        "      description: 'Get the current balance for a customer account...',     ",
        "      input_schema: { type:'object',                                        ",
        "        properties: { accountId: {type:'string'} },                         ",
        "        required: ['accountId'] } },                                        ",
        "    { name: 'transferFunds',                                                ",
        "      description: 'Transfer money between two accounts...',               ",
        "      input_schema: { type:'object',                                        ",
        "        properties: { fromAccount:{type:'string'},                          ",
        "                       toAccount:{type:'string'},                           ",
        "                       amount:{type:'number'} },                            ",
        "        required: ['fromAccount','toAccount','amount'] } },                 ",
        "    ... 5 more tools                                                        ",
        "  ]                                                                         ",
        "                                                                            ",
        "Claude response (tool_use):                                                 ",
        "  { stop_reason: 'tool_use',                                               ",
        "    content: [{ type:'tool_use', id:'toolu_abc',                            ",
        "                name:'getAccountBalance',                                   ",
        "                input:{ accountId:'CHK-001' } }] }                          ",
        "                                                                            ",
        "Spring AI:                                                                  ",
        "  reflect → BankingTools.getAccountBalance('CHK-001')                       ",
        "  result → 'Account CHK-001 current balance: $12,450.00'                   ",
        "                                                                            ",
        "Spring AI sends tool_result back:                                           ",
        "  { role:'user', content:[{ type:'tool_result', tool_use_id:'toolu_abc',   ",
        "                            content:'Account CHK-001 balance: $12,450.00'}]}",
    ]
    story.append(ascii_box(tool_flow))
    story.append(caption("Figure 7 — Tool calling: JSON Schema definition → Claude selection → Java execution → result"))
    story.append(PageBreak())

    # ────────────────────────────────────────────────────────────
    # SECTION 9 — DATA FLOW SUMMARY
    # ────────────────────────────────────────────────────────────
    story += [h1("9.  Data Flow Summary"), hr()]
    story.append(body(
        "End-to-end data flow showing every transformation from user action to screen render."
    ))
    story.append(sp(8))

    summary_flow = [
        "USER ACTION                                                                 ",
        "    │                                                                       ",
        "    ▼                                                                       ",
        "React Component  (state change via useState/useCallback)                    ",
        "    │                                                                       ",
        "    ▼                                                                       ",
        "fetch('/api/...')  →  Vite Dev Server proxy  →  http://localhost:8080       ",
        "    │                                                                       ",
        "    ├──[Catalog requests]────────────────────────────────────────────────  ",
        "    │       │                                                               ",
        "    │       ▼                                                               ",
        "    │   Spring @RestController  (@CrossOrigin CORS headers set)             ",
        "    │       │                                                               ",
        "    │       ▼                                                               ",
        "    │   @Service AgentService  (Java Streams: filter → collect)            ",
        "    │   in-memory List<Agent> (77 objects, no DB)                           ",
        "    │       │                                                               ",
        "    │       ▼  Jackson serializes POJOs → JSON                             ",
        "    │   HTTP 200 application/json                                            ",
        "    │       │                                                               ",
        "    │       ▼                                                               ",
        "    │   React: setAgents() / setStats() → re-render                        ",
        "    │                                                                       ",
        "    ├──[Chat requests]─────────────────────────────────────────────────── ",
        "    │       │                                                               ",
        "    │       ▼                                                               ",
        "    │   ChatController  →  ChatClient.prompt().call()                       ",
        "    │       │                                                               ",
        "    │       ▼  (1) Memory advisor loads history                            ",
        "    │   MessageChatMemoryAdvisor → InMemoryChatMemoryRepository             ",
        "    │       │                                                               ",
        "    │       ▼  (2) HTTPS to Anthropic                                      ",
        "    │   api.anthropic.com/v1/messages                                      ",
        "    │   [system + history + user msg + tool definitions]                    ",
        "    │       │                                                               ",
        "    │       ▼  (3) Tool use loop (0..N rounds)                             ",
        "    │   Claude → tool_use → BankingTools.method() → tool_result            ",
        "    │       │                                                               ",
        "    │       ▼  (4) Final text response                                     ",
        "    │   Memory advisor saves [UserMsg, AssistantMsg]                        ",
        "    │       │                                                               ",
        "    │       ▼  HTTP 200 {response, sessionId}                              ",
        "    │   ChatWidget: append bubble, scroll to bottom                         ",
        "    │                                                                       ",
        "SCREEN UPDATED                                                              ",
    ]
    story.append(ascii_box(summary_flow))
    story.append(caption("Figure 8 — Unified data flow: catalog path vs chat path"))
    story.append(PageBreak())

    # ────────────────────────────────────────────────────────────
    # SECTION 10 — SECURITY & COMPLIANCE
    # ────────────────────────────────────────────────────────────
    story += [h1("10.  Security & Compliance Controls"), hr()]
    story.append(body(
        "The following controls are embedded in the system design to align with financial services "
        "regulatory requirements modelled in the catalog."
    ))
    story.append(sp(8))

    sec_data = [
        ["Control", "Implementation", "Regulatory Alignment"],
        ["CORS restriction", "@CrossOrigin(origins=['localhost:5173','localhost:3000'])", "OWASP API Security"],
        ["API key via env var", "spring.ai.anthropic.api-key=${ANTHROPIC_API_KEY}", "GLBA, Secret management best practice"],
        ["No PII in tool logs", "Mock data only; tools never log real account data", "GLBA Safeguards Rule"],
        ["Transfer limit", "transferFunds: rejects amount > $10,000 without branch verification", "BSA / FinCEN CTR threshold"],
        ["Conversation cap", "MessageWindowChatMemory: max 20 messages per session", "Data minimization (CCPA/GDPR)"],
        ["System prompt guardrails", "Aria never gives investment advice or approves credit", "Reg BI, UDAAP, ECOA"],
        ["Escalation tool", "escalateToLiveAgent() available for complex / frustrated cases", "CFPB complaint handling"],
        ["Low temperature", "temperature=0.3 reduces hallucination risk", "OCC AI Principles, SR 11-7"],
        ["Session isolation", "Each browser tab gets a unique sessionId; memory is per-session", "FFIEC Authentication"],
    ]
    story.append(dark_table(sec_data, [3.5*cm, 5.5*cm, W-9*cm]))
    story.append(sp(16))

    # Footer note
    story.append(hr())
    story.append(body(
        "<b>Stack versions:</b>  Spring Boot 3.4.3  ·  Spring AI 1.0.3  ·  React 18  ·  Vite 7.3  ·  "
        "Claude claude-sonnet-4-6  ·  OpenJDK 25  ·  Apache Maven 3.9.12"
    ))
    story.append(body(
        f"<b>Generated:</b> {date.today().strftime('%B %d, %Y')}  ·  "
        "<b>Project:</b> /Users/srividya/IdeaProjects/AgentCatalog"
    ))

    # ── Build ─────────────────────────────────────────────────────
    def on_page(canvas, doc):
        canvas.saveState()
        canvas.setFillColor(TEXT_MUT)
        canvas.setFont("Helvetica", 8)
        canvas.drawString(1.8*cm, 1.2*cm,
            "Enterprise Agent Catalog — Architecture Reference")
        canvas.drawRightString(A4[0] - 1.8*cm, 1.2*cm,
            f"Page {doc.page}")
        canvas.restoreState()

    doc.build(story, onFirstPage=on_page, onLaterPages=on_page)
    print(f"PDF saved: {OUTPUT}")

build()
