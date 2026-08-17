package com.support.ticket;

public class Ticket {

    private String ticketId;
    private String customerName;
    private String description;
    private String category;
    private String severity;
    private String priority;
    private String status;
    private Integer slaHours;

    public Ticket() {
    }

    public Ticket(String ticketId, String customerName, String description, String category,
                  String severity, String priority, String status, Integer slaHours) {
        this.ticketId = ticketId;
        this.customerName = customerName;
        this.description = description;
        this.category = category;
        this.severity = severity;
        this.priority = priority;
        this.status = status;
        this.slaHours = slaHours;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSlaHours() {
        return slaHours;
    }

    public void setSlaHours(Integer slaHours) {
        this.slaHours = slaHours;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", severity='" + severity + '\'' +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                ", slaHours=" + slaHours +
                '}';
    }
}
