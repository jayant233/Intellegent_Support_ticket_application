package com.support.ticket.repository;

import com.support.ticket.exception.DatabaseException;
import com.support.ticket.model.Ticket;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TicketRepository {

    private final DataSource dataSource;

    public TicketRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Ticket ticket) throws DatabaseException {
        String sql = "INSERT INTO tickets (ticket_id, customer_name, description, category, severity, priority, status, sla_hours) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setString(1, ticket.getTicketId());
            preparedStatement.setString(2, ticket.getCustomerName());
            preparedStatement.setString(3, ticket.getDescription());
            preparedStatement.setString(4, ticket.getCategory());
            preparedStatement.setString(5, ticket.getSeverity());
            preparedStatement.setString(6, ticket.getPriority());
            preparedStatement.setString(7, ticket.getStatus());
            preparedStatement.setInt(8, ticket.getSlaHours());
            
            preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save ticket", e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // Ignore closing exceptions
            }
        }
    }

    public Ticket findById(String ticketId) throws DatabaseException {
        String sql = "SELECT * FROM tickets WHERE ticket_id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, ticketId);
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToTicket(resultSet);
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch ticket by ID", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // Ignore closing exceptions
            }
        }
        
        return null;
    }

    public List<Ticket> findAll() throws DatabaseException {
        String sql = "SELECT * FROM tickets";
        List<Ticket> tickets = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                tickets.add(mapRowToTicket(resultSet));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch all tickets", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // Ignore closing exceptions
            }
        }
        
        return tickets;
    }

    public void updateStatus(String ticketId, String newStatus) throws DatabaseException {
        String sql = "UPDATE tickets SET status = ? WHERE ticket_id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setString(1, newStatus);
            preparedStatement.setString(2, ticketId);
            
            preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update ticket status", e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // Ignore closing exceptions
            }
        }
    }
    
    public List<Ticket> findByFilters(String severity, String priority, String status) throws DatabaseException {
        StringBuilder sql = new StringBuilder("SELECT * FROM tickets WHERE 1=1");
        List<String> parameters = new ArrayList<>();
        
        if (severity != null && !severity.isEmpty()) {
            sql.append(" AND severity = ?");
            parameters.add(severity);
        }
        if (priority != null && !priority.isEmpty()) {
            sql.append(" AND priority = ?");
            parameters.add(priority);
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
            parameters.add(status);
        }
        
        List<Ticket> tickets = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql.toString());
            
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setString(i + 1, parameters.get(i));
            }
            
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tickets.add(mapRowToTicket(resultSet));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch tickets by filters", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // Ignore closing exceptions
            }
        }
        
        return tickets;
    }

    private Ticket mapRowToTicket(ResultSet rs) throws SQLException {
        return new Ticket(
                rs.getString("ticket_id"),
                rs.getString("customer_name"),
                rs.getString("description"),
                rs.getString("category"),
                rs.getString("severity"),
                rs.getString("priority"),
                rs.getString("status"),
                rs.getInt("sla_hours")
        );
    }
}
