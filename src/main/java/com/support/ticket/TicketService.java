package com.support.ticket;

import java.util.List;

public class TicketService {

    private final TicketDAO ticketDAO;
    private final KeywordClassifierService classifierService;
    private final TicketIdGenerator ticketIdGenerator;

    public TicketService() {
        this.ticketDAO = new TicketDAO();
        this.classifierService = new KeywordClassifierService();
        this.ticketIdGenerator = new TicketIdGenerator();
    }

    public Ticket createTicket(String customerName, String description) throws DatabaseException {
        String ticketId = ticketIdGenerator.generateId();
        
        Ticket ticket = new Ticket();
        ticket.setTicketId(ticketId);
        ticket.setCustomerName(customerName);
        ticket.setDescription(description);
        
        ticket.setCategory(classifierService.classifyCategory(description));
        ticket.setSeverity(classifierService.classifySeverity(description));
        ticket.setPriority(classifierService.classifyPriority(description));
        ticket.setSlaHours(classifierService.classifySLA(description));
        
        ticket.setStatus("Open");
        
        ticketDAO.saveTicket(ticket);
        
        return ticket;
    }

    public Ticket getTicketById(String ticketId) throws TicketNotFoundException, DatabaseException {
        Ticket ticket = ticketDAO.getTicketById(ticketId);
        if (ticket == null) {
            throw new TicketNotFoundException("Ticket not found with ID: " + ticketId);
        }
        return ticket;
    }

    public List<Ticket> getAllTickets() throws DatabaseException {
        return ticketDAO.getAllTickets();
    }
    
    public List<Ticket> getTicketsByFilters(String severity, String priority, String status) throws DatabaseException {
        return ticketDAO.getTicketsByFilters(severity, priority, status);
    }

    public void updateStatus(String ticketId, String newStatus) throws TicketNotFoundException, DatabaseException {
        // Validate if ticket exists first
        getTicketById(ticketId);
        ticketDAO.updateTicketStatus(ticketId, newStatus);
    }
}
