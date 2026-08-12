package com.support.ticket.service;

import com.support.ticket.exception.DatabaseException;
import com.support.ticket.exception.TicketNotFoundException;
import com.support.ticket.model.Ticket;
import com.support.ticket.repository.TicketRepository;
import com.support.ticket.util.TicketIdGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final KeywordClassifierService classifierService;
    private final TicketIdGenerator ticketIdGenerator;

    public TicketService(TicketRepository ticketRepository, 
                         KeywordClassifierService classifierService, 
                         TicketIdGenerator ticketIdGenerator) {
        this.ticketRepository = ticketRepository;
        this.classifierService = classifierService;
        this.ticketIdGenerator = ticketIdGenerator;
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
        
        ticketRepository.save(ticket);
        
        return ticket;
    }

    public Ticket getTicketById(String ticketId) throws TicketNotFoundException, DatabaseException {
        Ticket ticket = ticketRepository.findById(ticketId);
        if (ticket == null) {
            throw new TicketNotFoundException("Ticket not found with ID: " + ticketId);
        }
        return ticket;
    }

    public List<Ticket> getAllTickets() throws DatabaseException {
        return ticketRepository.findAll();
    }
    
    public List<Ticket> getTicketsByFilters(String severity, String priority, String status) throws DatabaseException {
        return ticketRepository.findByFilters(severity, priority, status);
    }

    public void updateStatus(String ticketId, String newStatus) throws TicketNotFoundException, DatabaseException {
        // Validate if ticket exists first
        getTicketById(ticketId);
        ticketRepository.updateStatus(ticketId, newStatus);
    }
}
