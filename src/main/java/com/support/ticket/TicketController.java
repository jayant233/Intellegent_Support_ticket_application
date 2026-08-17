package com.support.ticket;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
public class TicketController {

    private TicketService ticketService = new TicketService();

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

    @GetMapping("/admin")
    public String showAdminDashboard(
            @RequestParam(required = false) String searchId,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String status,
            Model model) {
        
        List<Ticket> tickets = Collections.emptyList();
        
        try {
            if (searchId != null && !searchId.trim().isEmpty()) {
                try {
                    tickets = Collections.singletonList(ticketService.getTicketById(searchId.trim()));
                } catch (TicketNotFoundException e) {
                    model.addAttribute("error", "Ticket not found with ID: " + searchId);
                }
            } else if ((severity != null && !severity.isEmpty()) || 
                       (priority != null && !priority.isEmpty()) || 
                       (status != null && !status.isEmpty())) {
                tickets = ticketService.getTicketsByFilters(severity, priority, status);
            } else {
                tickets = ticketService.getAllTickets();
            }
        } catch (DatabaseException ex) {
            model.addAttribute("error", "A database error occurred while fetching tickets.");
        } catch (Exception ex) {
            model.addAttribute("error", "An unexpected error occurred.");
        }

        model.addAttribute("tickets", tickets);
        return "admin";
    }

    @PostMapping("/admin/update-status")
    public String updateTicketStatus(@RequestParam String ticketId, 
                                     @RequestParam String status,
                                     Model model) {
        try {
            ticketService.updateStatus(ticketId, status);
        } catch (TicketNotFoundException ex) {
            model.addAttribute("error", "Cannot update status. Ticket not found.");
        } catch (DatabaseException ex) {
            model.addAttribute("error", "Database error occurred while updating status.");
        } catch (Exception ex) {
            model.addAttribute("error", "An unexpected error occurred.");
        }
        
        return "redirect:/admin";
    }
}
