package main.java.com.example;

import main.java.com.example.exceptions.InvalidTimeRangeException;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class TimeRange {

    private LocalTime start;
    private LocalTime end;

    public TimeRange(LocalTime start, LocalTime end) throws InvalidTimeRangeException {
        if(end.isBefore(start)){
            throw new InvalidTimeRangeException("Invalid LocalTime parameters.");
        }
        this.start = start;
        this.end = end;
    }

    public TimeRange(String start, String end) throws InvalidTimeRangeException, DateTimeParseException {
        try{
            LocalTime.parse(start);
            LocalTime.parse(end);
        }catch(DateTimeParseException ex){
            throw new DateTimeParseException("Invalid string input as start/end.", start, 1);
        }
        if(LocalTime.parse(end).isBefore(LocalTime.parse(start))){
            throw new InvalidTimeRangeException("Failed to create TimeRange. The end cannot be before the start.");
        }else{
            this.start = LocalTime.parse(start);
            this.end = LocalTime.parse(end);
        }

    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }


    @Override
    public String toString() {
        return "[" + start + ", " + end + "]";
    }
}
