package com.timetap.app;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventRepository {
    private static final String PREFS = "events";
    private static EventRepository instance;
    private SharedPreferences prefs;
    
    private EventRepository(Context context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
    
    public static synchronized EventRepository getInstance(Context context) {
        if (instance == null) instance = new EventRepository(context);
        return instance;
    }
    
    public void addEvent(long timestamp) {
        List<EventEntry> events = getAllEvents();
        long newId = events.isEmpty() ? 1 : events.get(0).getId() + 1;
        events.add(0, new EventEntry(newId, timestamp));
        saveEvents(events);
    }
    
    public List<EventEntry> getAllEvents() {
        List<EventEntry> events = new ArrayList<>();
        try {
            String json = prefs.getString("list", "[]");
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                events.add(new EventEntry(obj.getLong("id"), obj.getLong("ts")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }
    
    public EventEntry getLastEvent() {
        List<EventEntry> events = getAllEvents();
        return events.isEmpty() ? null : events.get(0);
    }
    
    public void deleteEvent(long id) {
        List<EventEntry> events = getAllEvents();
        events.removeIf(e -> e.getId() == id);
        saveEvents(events);
    }
    
    private void saveEvents(List<EventEntry> events) {
        JSONArray array = new JSONArray();
        try {
            for (EventEntry e : events) {
                JSONObject obj = new JSONObject();
                obj.put("id", e.getId());
                obj.put("ts", e.getTimestamp());
                array.put(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        prefs.edit().putString("list", array.toString()).apply();
    }
}
