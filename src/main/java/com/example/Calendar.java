package main.java.com.example;

import main.java.com.example.exceptions.InvalidTimeRangeException;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Calendar {

    private final String start;
    private final String end;

    ArrayList<Meeting> arrayOfMeetings = new ArrayList<>();

    public Calendar(String start, String end) throws InvalidTimeRangeException, DateTimeParseException{
        try{
            LocalTime.parse(start);
            LocalTime.parse(end);
        }catch(DateTimeParseException ex){
            throw new DateTimeParseException("Invalid string input as start/end.", start, 1);
        }
         if(LocalTime.parse(end).isBefore(LocalTime.parse(start))){
            throw new InvalidTimeRangeException("Failed to create calendar. The end cannot be before the start.");
        }else{
            this.start = start;
            this.end = end;
        }
    }

    public TimeRange getCalendarTimeRange() throws InvalidTimeRangeException {
        var startOfWorking = LocalTime.parse(this.start);
        var endOfWorking = LocalTime.parse(this.end);
        return new TimeRange(startOfWorking, endOfWorking);
    }

    public void addMeeting(String meetingStart, String meetingEnd) throws InvalidTimeRangeException {
        if(LocalTime.parse(meetingStart).isBefore(LocalTime.parse(start))){
            throw new InvalidTimeRangeException("Cannot add meeting which starts before the working hours.");
        }else if(LocalTime.parse(meetingEnd).isAfter(LocalTime.parse(end))){
            throw new InvalidTimeRangeException("Cannot add meeting which ends before the end of working hours.");
        }else{
            var meeting = new Meeting(meetingStart, meetingEnd);
            this.arrayOfMeetings.add(meeting);
        }

    }

    public List<Meeting> getArrayOfMeetings() {
        return arrayOfMeetings;
    }

    public LocalTime getEndOfLastElementOfList() throws ArrayIndexOutOfBoundsException{
        if(this.arrayOfMeetings.isEmpty()){
            throw new ArrayIndexOutOfBoundsException("Cannot retrieve the last element from empty list.");
        }else{
            return LocalTime.parse(this.arrayOfMeetings.get(arrayOfMeetings.size() - 1).getEnd());
        }
    }

    public LocalTime getStartOfFirstElementOfList() throws ArrayIndexOutOfBoundsException{
        if(this.arrayOfMeetings.isEmpty()){
            throw new ArrayIndexOutOfBoundsException("Cannot retrieve the first element from empty list.");
        }else{
            return LocalTime.parse(this.arrayOfMeetings.get(0).getStart());
        }
    }

    public LocalTime getStartOfElementByIndex(int index) throws ArrayIndexOutOfBoundsException{
        if(this.arrayOfMeetings.isEmpty()){
            throw new ArrayIndexOutOfBoundsException("Cannot retrieve the first element from empty list.");
        }else{
            return LocalTime.parse(this.arrayOfMeetings.get(index).getStart());
        }
    }



    public boolean isEmpty() {
        return (arrayOfMeetings.isEmpty());
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
