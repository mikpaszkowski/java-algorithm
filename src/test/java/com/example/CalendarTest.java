package test.java.com.example;

import main.java.com.example.Calendar;
import main.java.com.example.exceptions.InvalidTimeRange;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

class CalendarTest {

    @Test
    public void testInvalidInputParametersForCalendar(){

        var exception = Assert.assertThrows(DateTimeParseException.class, () ->
            new Calendar("1000", "2333")
        );
        var expectedMessage = "Invalid string input as start/end.";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testTheMostConfusingInvalidInputParametersForCalendar(){

        var exception = Assert.assertThrows(DateTimeParseException.class, () ->
            new Calendar("7:30", "15:30")
        );
        var expectedMessage = "Invalid string input as start/end.";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAddingMeetingOutOfWorkingHours() throws InvalidTimeRange {
        var calendar = new Calendar("09:00", "16:30");

        var exception = Assert.assertThrows(InvalidTimeRange.class, () ->
            calendar.addMeeting("12:00", "11:00")
        );

        var expectedMessage = "Invalid meetings ranges: meeting cannot end before the start.";
        var actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAddingMeetingBeforeTheWorkingHours() throws InvalidTimeRange{
        var calendar = new Calendar("09:30", "16:40");
        var exception = Assert.assertThrows(InvalidTimeRange.class, () ->
            calendar.addMeeting("08:00", "08:30")
        );

        var expectedMessage = "Cannot add meeting which starts before the working hours.";
        var actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void testAddingMeetingAfterTheWorkingHours() throws InvalidTimeRange{
        var calendar = new Calendar("09:00", "16:30");
        var exception = Assert.assertThrows(InvalidTimeRange.class, () ->
            calendar.addMeeting("17:00", "17:30")
        );

        var expectedMessage = "Cannot add meeting which ends before the end of working hours.";
        var actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}
