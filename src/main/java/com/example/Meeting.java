package main.java.com.example;

import main.java.com.example.exceptions.InvalidTimeRange;

import java.time.LocalTime;

public class Meeting {

    private String start;
    private String end;

    public Meeting(String start, String end) throws InvalidTimeRange {
        if(LocalTime.parse(end).isBefore(LocalTime.parse(start))){
            throw new InvalidTimeRange("Invalid meetings ranges: meeting cannot end before the start.");
        }else{
            this.start = start;
            this.end = end;
        }
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}
