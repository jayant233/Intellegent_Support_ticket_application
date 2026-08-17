package com.support.ticket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/support_ticket_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void saveTicket(Ticket ticket) throws DatabaseException {
        String sql = "INSERT INTO tickets (ticket_id, customer_name, description, category, severity, priority, status, sla_hours) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
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
            closeResources(connection, preparedStatement, null);
        }
    }

    public Ticket getTicketById(String ticketId) throws DatabaseException {
        String sql = "SELECT * FROM tickets WHERE ticket_id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, ticketId);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToTicket(resultSet);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch ticket by ID", e);
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }

        return null;
    }

    public List<Ticket> getAllTickets() throws DatabaseException {
        String sql = "SELECT * FROM tickets";
        List<Ticket> tickets = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tickets.add(mapRowToTicket(resultSet));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch all tickets", e);
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }

        return tickets;
    }

    public void updateTicketStatus(String ticketId, String newStatus) throws DatabaseException {
        String sql = "UPDATE tickets SET status = ? WHERE ticket_id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, newStatus);
            preparedStatement.setString(2, ticketId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to update ticket status", e);
        } finally {
            closeResources(connection, preparedStatement, null);
        }
    }

    public List<Ticket> getTicketsByFilters(String severity, String priority, String status) throws DatabaseException {
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
            connection = getConnection();
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
            closeResources(connection, preparedStatement, resultSet);
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

    private void closeResources(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException exception) {
            System.out.println("Could not close ResultSet.");
        }

        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException exception) {
            System.out.println("Could not close PreparedStatement.");
        }

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException exception) {
            System.out.println("Could not close Connection.");
        }
    }
}
