package com.support.ticket;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;

public class KeywordClassifierService {

    private final Map<String, List<String>> categoryKeywords;
    private final Map<String, String> categorySeverity;

    public KeywordClassifierService() {
        categoryKeywords = new HashMap<>();
        categorySeverity = new HashMap<>();

        initializeCategories();
        initializeConfiguration();
    }

    private void initializeCategories() {
        categoryKeywords.put("Login Issue", Arrays.asList("login", "signin", "sign in", "authentication", "account access", "unable to login"));
        categoryKeywords.put("Outage Issue", Arrays.asList("outage", "down", "offline", "downtime", "service unavailable", "system down"));
        categoryKeywords.put("Password Issue", Arrays.asList("password", "forgot password", "reset password", "password change", "incorrect password", "password expired"));
        categoryKeywords.put("Payment Issue", Arrays.asList("payment", "billing", "invoice", "charge", "credit card", "refund"));
        categoryKeywords.put("Performance Issue", Arrays.asList("slow", "performance", "lag", "hanging", "freezing", "latency"));
        categoryKeywords.put("Application Error", Arrays.asList("error", "exception", "crash", "bug", "stacktrace", "internal error"));
        categoryKeywords.put("Database Issue", Arrays.asList("database", "sql", "connection pool", "data loss", "corruption", "timeout"));
        categoryKeywords.put("UI Bug", Arrays.asList("ui", "button", "layout", "alignment", "rendering", "display"));
        categoryKeywords.put("Access Issue", Arrays.asList("access denied", "forbidden", "permissions", "unauthorized", "role", "locked out"));
        categoryKeywords.put("Email Issue", Arrays.asList("email", "spam", "not receiving", "bounce", "smtp", "delivery"));
    }

    private void initializeConfiguration() {
        categorySeverity.put("Login Issue", "Medium");
        categorySeverity.put("Outage Issue", "Critical");
        categorySeverity.put("Password Issue", "Low");
        categorySeverity.put("Payment Issue", "High");
        categorySeverity.put("Performance Issue", "Medium");
        categorySeverity.put("Application Error", "High");
        categorySeverity.put("Database Issue", "Critical");
        categorySeverity.put("UI Bug", "Low");
        categorySeverity.put("Access Issue", "High");
        categorySeverity.put("Email Issue", "Medium");
    }

    public String classifyCategory(String description) {
        if (description != null) {
            String lowerDesc = description.toLowerCase();
            for (Map.Entry<String, List<String>> entry : categoryKeywords.entrySet()) {
                for (String keyword : entry.getValue()) {
                    if (lowerDesc.contains(keyword)) {
                        return entry.getKey();
                    }
                }
            }
        }
        return "General Support";
    }

    private TicketConfig getConfigBySeverity(String severity) {
        switch (severity) {
            case "Critical":
                return new TicketConfig(severity, "P1", 2);
            case "High":
                return new TicketConfig(severity, "P2", 4);
            case "Medium":
                return new TicketConfig(severity, "P3", 8);
            case "Low":
                return new TicketConfig(severity, "P4", 24);
            default:
                return new TicketConfig(severity, "P4", 48);
        }
    }

    public String classifySeverity(String description) {
        String category = classifyCategory(description);
        return categorySeverity.getOrDefault(category, "Low");
    }

    public String classifyPriority(String description) {
        String severity = classifySeverity(description);
        return getConfigBySeverity(severity).getPriority();
    }

    public Integer classifySLA(String description) {
        String severity = classifySeverity(description);
        return getConfigBySeverity(severity).getSlaHours();
    }

    private static class TicketConfig {
        private final String severity;
        private final String priority;
        private final int slaHours;

        public TicketConfig(String severity, String priority, int slaHours) {
            this.severity = severity;
            this.priority = priority;
            this.slaHours = slaHours;
        }

        public String getSeverity() {
            return severity;
        }

        public String getPriority() {
            return priority;
        }

        public int getSlaHours() {
            return slaHours;
        }
    }
}
