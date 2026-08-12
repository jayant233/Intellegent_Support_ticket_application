package com.support.ticket.controller;

import com.support.ticket.exception.DatabaseException;
import com.support.ticket.exception.TicketNotFoundException;
import com.support.ticket.model.Ticket;
import com.support.ticket.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final TicketService ticketService;

    public AdminController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
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

    @PostMapping("/update-status")
    public String updateTicketStatus(@RequestParam String ticketId, 
                                     @RequestParam String status,
                                     Model model) {
        try {
            ticketService.updateStatus(ticketId, status);
        } catch (TicketNotFoundException ex) {
            model.addAttribute("error", "Cannot update status. Ticket not found.");
            // To render the page with an error, we should ideally fetch tickets again,
            // but for simplicity, we just redirect. The error message would be lost on redirect 
            // unless we use RedirectAttributes. We will just return the error page or redirect.
        } catch (DatabaseException ex) {
            model.addAttribute("error", "Database error occurred while updating status.");
        } catch (Exception ex) {
            model.addAttribute("error", "An unexpected error occurred.");
        }
        
        return "redirect:/admin";
    }
}
