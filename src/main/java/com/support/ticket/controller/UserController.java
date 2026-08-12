package com.support.ticket.controller;

import com.support.ticket.exception.DatabaseException;
import com.support.ticket.exception.TicketNotFoundException;
import com.support.ticket.model.Ticket;
import com.support.ticket.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final TicketService ticketService;

    public UserController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/")
    public String showHomepage() {
        return "user";
    }

    @PostMapping("/ticket")
    public String submitTicket(@RequestParam String customerName, 
                               @RequestParam String description, 
                               Model model) {
        if (customerName == null || customerName.trim().isEmpty() || 
            description == null || description.trim().isEmpty()) {
            model.addAttribute("error", "Customer name and description are required.");
            return "user";
        }

        try {
            Ticket ticket = ticketService.createTicket(customerName, description);
            model.addAttribute("successMessage", "Ticket submitted successfully!");
            model.addAttribute("generatedTicketId", ticket.getTicketId());
        } catch (DatabaseException e) {
            model.addAttribute("error", "An error occurred while saving the ticket to the database.");
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred.");
        }
        
        return "user";
    }

    @GetMapping("/status")
    public String showStatusPage() {
        return "status";
    }

    @PostMapping("/status")
    public String checkTicketStatus(@RequestParam String ticketId, Model model) {
        if (ticketId == null || ticketId.trim().isEmpty()) {
            model.addAttribute("error", "Please enter a valid Ticket ID.");
            return "status";
        }

        try {
            Ticket ticket = ticketService.getTicketById(ticketId.trim());
            model.addAttribute("ticket", ticket);
        } catch (TicketNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (DatabaseException ex) {
            model.addAttribute("error", "Database error occurred while fetching the ticket.");
        } catch (Exception ex) {
            model.addAttribute("error", "An unexpected error occurred.");
        }
        
        return "status";
    }
}
