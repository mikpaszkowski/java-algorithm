package test.java.com.example;

import main.java.com.example.exceptions.InvalidTimeRange;
import main.java.com.example.MeetAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import main.java.com.example.Calendar;

import java.util.*;


class MeetAlgorithmTest {

    @Test
    public void testWithOneEmptyCalendar() throws InvalidTimeRange{
        var calendar1 = new Calendar("07:00", "10:30");

        var calendar2 = new Calendar("10:00", "16:30");
        calendar2.addMeeting("11:00", "12:00");
        calendar2.addMeeting("11:30", "12:00");
        calendar2.addMeeting("12:30", "13:00");
        calendar2.addMeeting("13:30", "14:00");

        final List<List<String>> possibleMeetingRanges = MeetAlgorithm.getPossibleMeetingRanges(calendar1, calendar2, "00:30");
        List<List<String>> expectedMeetingRanges = Collections.singletonList(
                Collections.singletonList("10:00, 10:30")
        );
        Assertions.assertEquals(possibleMeetingRanges.toString(), expectedMeetingRanges.toString());
    }

    @Test
    public void testForEmptyCalendars() throws InvalidTimeRange {
        var calendar1 = new Calendar("07:00", "15:30");
        var calendar2 = new Calendar("10:30", "16:30");

        final List<List<String>> possibleMeetingRanges = MeetAlgorithm.getPossibleMeetingRanges(calendar1, calendar2, "00:30");
        List<List<String>> expectedMeetingRanges = Collections.singletonList(
                Collections.singletonList("10:30, 15:30")
        );
        Assertions.assertEquals(possibleMeetingRanges.toString(), expectedMeetingRanges.toString());
    }

    @Test
    public void testForCalendarsWithNoCommonHours() throws InvalidTimeRange {

        var calendar1 = new Calendar("07:00", "15:30");
        calendar1.addMeeting("07:30", "09:20");
        calendar1.addMeeting("10:00", "12:00");
        calendar1.addMeeting("12:30", "15:00");

        var calendar2 = new Calendar("15:30", "20:30");
        calendar2.addMeeting("16:00", "17:00");
        calendar2.addMeeting("17:30", "18:30");
        calendar2.addMeeting("19:00", "20:00");
        calendar2.addMeeting("20:00", "20:30");

        final List<List<String>> possibleMeetingRanges = MeetAlgorithm.getPossibleMeetingRanges(calendar1, calendar2, "00:30");
        Assertions.assertEquals(possibleMeetingRanges.size(), 0);
    }

    @Test
    public void testForTwoRegularCalendars() throws InvalidTimeRange {
        var calendar1 = new Calendar("10:30", "16:50");

            calendar1.addMeeting("12:30", "13:20");
            calendar1.addMeeting("14:40", "16:00");
            calendar1.addMeeting("16:15", "16:50");


        var calendar2 = new Calendar("10:30", "16:49");

            calendar2.addMeeting("11:00", "12:00");
            calendar2.addMeeting("12:30", "13:00");
            calendar2.addMeeting("13:40", "15:00");
            calendar2.addMeeting("15:20", "16:00");

        List<List<String>> expectedMeetingRanges = Arrays.asList(
                Collections.singletonList("10:30, 11:00"),
                Collections.singletonList("12:00, 12:30")
        );

        final List<List<String>> possibleMeetingRanges = MeetAlgorithm.getPossibleMeetingRanges(calendar1, calendar2, "00:30");
        Assertions.assertEquals(possibleMeetingRanges.toString(), expectedMeetingRanges.toString());
    }

    @Test
    public void testForOneCommonMeetingAvailable() throws InvalidTimeRange {
        var calendar1 = new Calendar("09:30", "16:50");

        calendar1.addMeeting("10:30", "11:00");
        calendar1.addMeeting("12:30", "13:20");
        calendar1.addMeeting("14:40", "16:00");
        calendar1.addMeeting("16:15", "16:50");


        var calendar2 = new Calendar("10:30", "16:49");

        calendar2.addMeeting("11:00", "12:00");
        calendar2.addMeeting("12:30", "13:00");
        calendar2.addMeeting("13:40", "15:00");
        calendar2.addMeeting("15:20", "16:00");

        final List<List<String>> possibleMeetingRanges = MeetAlgorithm.getPossibleMeetingRanges(calendar1, calendar2, "00:30");
        List<List<String>> expectedMeetingRanges = Collections.singletonList(
                Collections.singletonList("12:00, 12:30")
        );
        Assertions.assertEquals(possibleMeetingRanges.toString(), expectedMeetingRanges.toString());
    }

    @Test
    public void testForCalendarsWithOneSchedulledMeeting() throws InvalidTimeRange{

        var calendar1 = new Calendar("09:30", "16:00");
        calendar1.addMeeting("10:30", "11:00");

        var calendar2 = new Calendar("10:30", "17:00");
        calendar2.addMeeting("12:30", "13:00");

        final List<List<String>> possibleMeetingRanges = MeetAlgorithm.getPossibleMeetingRanges(calendar1, calendar2, "00:30");
        List<List<String>> expectedMeetingRanges = Arrays.asList(
                Collections.singletonList("11:00, 12:30"),
                Collections.singletonList("13:00, 16:00")
        );

        Assertions.assertEquals(possibleMeetingRanges.toString(), expectedMeetingRanges.toString());
    }

    @Test
    public void testForSeveralCommonAvailableHours() throws InvalidTimeRange{
        var calendar1 = new Calendar("08:00", "16:00");
        calendar1.addMeeting("10:30", "11:00");
        calendar1.addMeeting("14:30", "15:30");

        var calendar2 = new Calendar("09:00", "17:00");
        calendar2.addMeeting("12:30", "13:00");
        calendar2.addMeeting("14:00", "14:30");


        final List<List<String>> possibleMeetingRanges = MeetAlgorithm.getPossibleMeetingRanges(calendar1, calendar2, "00:30");
        List<List<String>> expectedMeetingRanges = Arrays.asList(
                Collections.singletonList("09:00, 10:30"),
                Collections.singletonList("11:00, 12:30"),
                Collections.singletonList("13:00, 14:00"),
                Collections.singletonList("15:30, 16:00")
        );

        Assertions.assertEquals(possibleMeetingRanges.toString(), expectedMeetingRanges.toString());
    }
}
