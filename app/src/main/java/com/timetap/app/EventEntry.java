package com.timetap.app;

public class EventEntry {
    private final long id;
    private final long timestamp;
    
    public EventEntry(long id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }
    
    public long getId() { return id; }
    public long getTimestamp() { return timestamp; }
}
