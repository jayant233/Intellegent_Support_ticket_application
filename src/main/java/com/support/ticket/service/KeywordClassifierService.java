package com.support.ticket.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class KeywordClassifierService {

    // Define rules map. The key is the keyword, and the value is a string array:
    // [Category, Severity, Priority, SLA (hours)]
    private final Map<String, String[]> keywordRules;

    public KeywordClassifierService() {
        keywordRules = new HashMap<>();
        // 10 specific categories based on requirements
        keywordRules.put("login", new String[]{"Login Issue", "Medium", "P3", "8"});
        keywordRules.put("password", new String[]{"Login Issue", "Medium", "P3", "8"});
        keywordRules.put("server down", new String[]{"Server Outage", "Critical", "P1", "1"});
        keywordRules.put("outage", new String[]{"Server Outage", "Critical", "P1", "1"});
        keywordRules.put("payment failed", new String[]{"Payment Issue", "High", "P2", "4"});
        keywordRules.put("slow", new String[]{"Performance Issue", "Medium", "P3", "12"});
        keywordRules.put("performance", new String[]{"Performance Issue", "Medium", "P3", "12"});
        keywordRules.put("error", new String[]{"Application Error", "High", "P2", "3"});
        keywordRules.put("exception", new String[]{"Application Error", "High", "P2", "3"});
        keywordRules.put("database", new String[]{"Database Issue", "Critical", "P1", "2"});
        keywordRules.put("UI", new String[]{"UI Bug", "Low", "P4", "24"});
        keywordRules.put("button", new String[]{"UI Bug", "Low", "P4", "24"});
        keywordRules.put("access denied", new String[]{"Access Issue", "High", "P2", "6"});
        keywordRules.put("email issue", new String[]{"Email Issue", "Medium", "P3", "10"});
        keywordRules.put("report", new String[]{"Reporting Issue", "Low", "P4", "48"});
        keywordRules.put("download", new String[]{"Reporting Issue", "Low", "P4", "48"});
    }

    private String[] classify(String description) {
        if (description != null) {
            String lowerDesc = description.toLowerCase();
            for (Map.Entry<String, String[]> entry : keywordRules.entrySet()) {
                if (lowerDesc.contains(entry.getKey().toLowerCase())) {
                    return entry.getValue();
                }
            }
        }
        // Default classification if no keyword matches
        return new String[]{"General Support", "Low", "P4", "48"};
    }

    public String classifyCategory(String description) {
        return classify(description)[0];
    }

    public String classifySeverity(String description) {
        return classify(description)[1];
    }

    public String classifyPriority(String description) {
        return classify(description)[2];
    }

    public Integer classifySLA(String description) {
        return Integer.parseInt(classify(description)[3]);
    }
}
