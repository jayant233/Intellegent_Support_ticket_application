package com.support.ticket;

import java.util.concurrent.atomic.AtomicInteger;
public class TicketIdGenerator {
    
    // In a real application, we would retrieve the max ID from the database, 
    // but for simplicity, we use an AtomicInteger starting at 1000.
    private final AtomicInteger counter = new AtomicInteger(1000);

    public String generateId() {
        return "TKT" + counter.incrementAndGet();
    }
}
