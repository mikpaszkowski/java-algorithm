package test.java.com.example;

import main.java.com.example.MeetAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;


class MeetAlgorithmTest {

    @Test
    public void simpleTest() {
        var calendar1 = new main.java.com.example.Calendar("10:30", "16:50");
        try {
            calendar1.addMeeting("12:30", "13:20");
            calendar1.addMeeting("14:40", "16:00");
            calendar1.addMeeting("16:15", "16:50");
        } catch (Exception e) {
            e.printStackTrace();
        }

        var calendar2 = new main.java.com.example.Calendar("10:30", "16:49");
        try {
            calendar2.addMeeting("11:00", "12:00");
            calendar2.addMeeting("12:30", "13:00");
            calendar2.addMeeting("13:40", "15:00");
            calendar2.addMeeting("15:20", "16:00");
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<List<String>> expectedMeetingRanges = Arrays.asList(
                Collections.singletonList("10:30, 11:00"),
                Collections.singletonList("12:00, 12:30")
        );

        final List<List<String>> possibleMeetingRanges = MeetAlgorithm.getPossibleMeetingRanges(calendar1, calendar2, "00:30");

        Assertions.assertEquals(possibleMeetingRanges.toString(), expectedMeetingRanges.toString());
    }
}
