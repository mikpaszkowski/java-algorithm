package com.example;


import java.time.LocalTime;
import java.util.ArrayList;

public class Calendar {

    private String start;
    private String end;

    ArrayList<Meeting> arrayOfMeetings = new ArrayList<Meeting>();

    public Calendar(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public LocalTime[] getCalendarWorkingRange(){

        LocalTime[] meetingRange = new LocalTime[2];
        meetingRange[0] = LocalTime.parse(this.getStart());
        meetingRange[1] = LocalTime.parse(this.getEnd());
        return meetingRange;
    }

    public void addMeeting(String meetingStart, String meetingEnd) throws Exception {
        if(!isStartBeforeTheWorkingHours(meetingStart)){
            Meeting meeting = new Meeting(meetingStart, meetingEnd);
            this.arrayOfMeetings.add(meeting);
        }
        return;
    }

    private boolean isStartBeforeTheWorkingHours(String meetStart) throws Exception {
        LocalTime meetingStart = LocalTime.parse(meetStart);
        LocalTime workingHoursStart = LocalTime.parse(this.start);

        if(meetingStart.isBefore(workingHoursStart)){
            throw new Exception("Cannot create meeting before the start of working.");
        }
        return false;
    }

    public ArrayList<Meeting> getArrayOfMeetings() {
        return arrayOfMeetings;
    }

    public void setArrayOfMeetings(ArrayList<Meeting> arrayOfMeetings) {
        this.arrayOfMeetings = arrayOfMeetings;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Calendar: {" +
                "start: '" + start + '\'' +
                ", end: '" + end + '\'' +
                ", arrayOfMeetings: " + arrayOfMeetings +
                '}';
    }
}
