package edu.lmu.cs.wutup.android.model;

import java.util.ArrayList;

import org.joda.time.DateTime;

public class Occurrence {
    
    private Integer id;
    private Event event;
    private Venue venue;
    private DateTime start;
    private DateTime end;
    
    private ArrayList<User> attendees =  new ArrayList<User>();
    private ArrayList<Comment> comments = new ArrayList<Comment>();
    
    public Occurrence(Event event, Venue venue, DateTime start, DateTime end) {
    	this.event = event;
    	this.venue = venue;
    	this.start = start;
    	this.end = end;
    }
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Event getEvent() {
        return event;
    }
    public void setEvent(Event event) {
        this.event = event;
    }
    public Venue getVenue() {
        return venue;
    }
    public void setVenue(Venue venue) {
        this.venue = venue;
    }
    public DateTime getStart() {
        return start;
    }
    public void setStart(DateTime start) {
        this.start = start;
    }
    public DateTime getEnd() {
        return end;
    }
    public void setEnd(DateTime end) {
        this.end = end;
    }
    public ArrayList<User> getAttendees() {
        return attendees;
    }
    public void setAttendees(ArrayList<User> attendees) {
        this.attendees = attendees;
    }
    public ArrayList<Comment> getComments() {
        return comments;
    }
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
    
    @Override
    public String toString() {
		return "blarkar";
    	
    }
    
}
