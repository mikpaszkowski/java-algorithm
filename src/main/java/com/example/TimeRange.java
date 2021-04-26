package main.java.com.example;

import java.time.LocalTime;

public class TimeRange {

    private LocalTime start;
    private LocalTime end;

    public TimeRange(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }
}
