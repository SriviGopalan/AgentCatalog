package org.example.agent;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

/**
 * Banking tool functions exposed to the Claude model via Spring AI function calling.
 * Each @Tool method is automatically discovered and made available to the LLM.
 */
@Component
public class BankingTools {

    // Mock account data
    private static final Map<String, Double> BALANCES = Map.of(
            "CHK-001", 12450.00,
            "SAV-001", 45230.50,
            "CHK-002", 3100.75,
            "SAV-002", 8920.00
    );

    @Tool(description = "Get the current balance for a customer account. Use this when the customer asks about their balance, funds, or how much money they have.")
    public String getAccountBalance(String accountId) {
        double balance = BALANCES.getOrDefault(accountId.toUpperCase(), 8750.00);
        return "Account %s current balance: $%,.2f (as of %s)".formatted(
                accountId.toUpperCase(), balance, LocalDate.now());
    }

    @Tool(description = "Get recent transactions for a customer account. Use when the customer asks about their transaction history, recent charges, or activity.")
    public String getRecentTransactions(String accountId, int count) {
        int limit = Math.min(count, 5);
        return """
                Recent %d transactions for account %s:
                1. %s  Amazon Purchase            -$89.99
                2. %s  Direct Deposit (Payroll)  +$3,500.00
                3. %s  Utility Bill (Con Edison)  -$124.50
                4. %s  Starbucks Coffee           -$6.75
                5. %s  ATM Withdrawal             -$200.00
                """.formatted(
                limit, accountId.toUpperCase(),
                LocalDate.now().minusDays(1),
                LocalDate.now().minusDays(2),
                LocalDate.now().minusDays(3),
                LocalDate.now().minusDays(4),
                LocalDate.now().minusDays(5)
        ).lines().limit(limit + 2L).reduce("", (a, b) -> a + b + "\n");
    }

    @Tool(description = "Transfer money between two accounts. Always confirm the amount and both account numbers with the customer before calling this tool.")
    public String transferFunds(String fromAccount, String toAccount, double amount) {
        if (amount <= 0) return "Transfer amount must be greater than zero.";
        if (amount > 10000) return "Transfers over $10,000 require in-branch verification. Please visit your nearest branch.";
        String confirmationId = "TXN-%d".formatted(System.currentTimeMillis() % 1_000_000);
        return "✅ Transfer successful! $%,.2f moved from %s → %s. Confirmation ID: %s. Funds available immediately."
                .formatted(amount, fromAccount.toUpperCase(), toAccount.toUpperCase(), confirmationId);
    }

    @Tool(description = "Get information about banking products: savings accounts, checking accounts, credit cards, personal loans, mortgages, or CDs.")
    public String getProductInfo(String productType) {
        return switch (productType.toLowerCase().trim()) {
            case "savings", "savings account", "high yield savings" ->
                    "💰 High-Yield Savings Account: 4.75% APY | No minimum balance | No monthly fees | FDIC insured up to $250K | Mobile check deposit";
            case "checking", "checking account" ->
                    "🏦 Premier Checking: No monthly fee with $500 min balance | Free ATM withdrawals worldwide | Zelle included | Overdraft protection available";
            case "credit card", "card", "rewards card" ->
                    "💳 Platinum Rewards Card: 3% cashback on dining & groceries, 1.5% everything else | No annual fee year 1 | 0% APR intro 15 months | $500 welcome bonus after $3K spend";
            case "mortgage", "home loan", "home" ->
                    "🏠 Home Loans: 30-yr fixed from 6.50% APR | 15-yr fixed from 5.85% APR | Up to $2M | Pre-approval in 24 hrs | No origination fee";
            case "personal loan", "loan" ->
                    "📋 Personal Loans: $1K–$50K | Rates from 8.49% APR | No prepayment penalty | Same-day funding | No collateral required";
            case "cd", "certificate of deposit" ->
                    "📈 Certificates of Deposit: 6-mo 4.90% APY | 12-mo 5.10% APY | 24-mo 4.75% APY | $500 minimum | FDIC insured";
            default ->
                    "For details on '%s', I can connect you with a product specialist. Shall I escalate this to a live banker?".formatted(productType);
        };
    }

    @Tool(description = "File a formal complaint or report a problem such as unauthorized charges, poor service, account errors, or fraud suspicion.")
    public String fileComplaint(String description) {
        String caseId = "CMP-%d".formatted(System.currentTimeMillis() % 1_000_000);
        return """
                📋 Complaint filed successfully.
                Case ID: %s
                Priority: Standard
                Expected response: Within 2 business days via email
                Escalation: If unresolved in 5 days, a senior specialist will contact you.
                Description logged: "%s"
                """.formatted(caseId, description);
    }

    @Tool(description = "Escalate the conversation to a live human banking specialist. Use when the customer is frustrated, has a complex issue, requests a human, or when you cannot resolve their issue.")
    public String escalateToLiveAgent(String reason) {
        return """
                🧑‍💼 Connecting you to a live banking specialist now.
                Estimated wait: 3–5 minutes
                Reason: %s

                While you wait, you can also:
                • Call us: 1-800-BANK-NOW (24/7)
                • Visit branch: Use our branch locator at enterprisebank.com/branches
                I'm handing off your conversation history so you won't need to repeat yourself.
                """.formatted(reason);
    }

    @Tool(description = "Report a lost or stolen debit/credit card and request a replacement. Immediately blocks the card.")
    public String reportLostCard(String cardType) {
        String reportId = "CARD-%d".formatted(System.currentTimeMillis() % 100_000);
        return """
                🔒 %s card has been BLOCKED immediately.
                Report ID: %s
                Replacement card: Mailed within 5-7 business days (or next-day delivery available at branch)
                Temporary access: Use digital wallet (Apple Pay / Google Pay) with your virtual card number
                """.formatted(cardType, reportId);
    }
}
