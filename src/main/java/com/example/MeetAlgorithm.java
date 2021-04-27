package main.java.com.example;

import main.java.com.example.exceptions.InvalidTimeRangeException;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MeetAlgorithm {


    private MeetAlgorithm() {
        //static methods only
    }

    public static List<TimeRange> getPossibleMeetingRanges(@NotNull Calendar firstCalendar, @NotNull Calendar secondCalendar, String meetingDuration) throws InvalidTimeRangeException {

        var commonWorkingRange = getPossibleTimeRangeForMeeting(firstCalendar, secondCalendar);

        List<Meeting> availableTimeSlotsOfFirstCal = getAvailableTimeSlotsBasedOnMeetingDuration(firstCalendar, meetingDuration, commonWorkingRange);
        List<Meeting> availableTimeSlotsOfSecondCal = getAvailableTimeSlotsBasedOnMeetingDuration(secondCalendar, meetingDuration, commonWorkingRange);

        List<Meeting> listOfMeeting = getListOfMeetingsBasedOnAvailableTimeSlots(availableTimeSlotsOfFirstCal, availableTimeSlotsOfSecondCal);
        return minimizeMeetingsRangesInList(listOfMeeting);
    }

    @NotNull
    private static List<Meeting> getListOfMeetingsBasedOnAvailableTimeSlots(List<Meeting> spareTimeSlotsFirstCal, List<Meeting> spareTimeSlotsSecondCal) throws InvalidTimeRangeException {
        List<Meeting> listOfMeeting = new ArrayList<>();
        for (Meeting meetingsOfFirstCal : spareTimeSlotsFirstCal) {
            for (Meeting meetingsOfSecondCal : spareTimeSlotsSecondCal) {
                if (meetingsOfFirstCal.getStart().equals(meetingsOfSecondCal.getStart())) {
                    var newMeeting = new Meeting(meetingsOfFirstCal.getStart(), meetingsOfFirstCal.getEnd());
                    listOfMeeting.add(newMeeting);
                }
            }
        }
        return listOfMeeting;
    }

    private static List<TimeRange> minimizeMeetingsRangesInList(List<Meeting> listOfMeetings) throws InvalidTimeRangeException {

        List<Meeting> meetings = sortTheListOfMeetingsAsc(listOfMeetings);

        for(var i = 0; i < meetings.size(); i++) {
            for (var j = 1; j < meetings.size(); j++) {
                if (uniteMeetingsIfPossible(meetings, i, j)) {
                    --j; //decrementing the j value because of deleting one meeting because of uniting two of them
                }
            }
        }
        return changeMeetingsToTimeRangesInList(meetings);
    }

    @NotNull
    private static List<TimeRange> changeMeetingsToTimeRangesInList(List<Meeting> meetings) throws InvalidTimeRangeException {
        List<TimeRange> minimizedList = new ArrayList<>();
        for (Meeting meeting : meetings) {
                   var singleMeetingTimeRange = new TimeRange(meeting.getStart(), meeting.getEnd());
                   minimizedList.add(singleMeetingTimeRange);
        }
        return minimizedList;
    }

    private static boolean uniteMeetingsIfPossible(List<Meeting> meetings, int i, int j) {
        if(meetings.get(i).getEnd().equals(meetings.get(j).getStart())){
            meetings.get(i).setEnd(meetings.get(j).getEnd());
            meetings.remove(j);
            return true;
        }
        return false;
    }

    private static List<Meeting> sortTheListOfMeetingsAsc(List<Meeting> listOfMeetings){

        ArrayList<Meeting> arrayOfMeetings = new ArrayList<>(listOfMeetings);
        Meeting temp;

        for(var i = 0; i < arrayOfMeetings.size(); i++){
            for(var j = 1; j < arrayOfMeetings.size(); j++){
                 if(LocalTime.parse(arrayOfMeetings.get(j - 1).getStart()).isAfter(LocalTime.parse(arrayOfMeetings.get(j).getStart()))){
                        temp = arrayOfMeetings.get(j - 1);
                        arrayOfMeetings.set(j - 1, arrayOfMeetings.get(j));
                        arrayOfMeetings.set(j, temp);
                 }
            }
        }
        return arrayOfMeetings;
    }

    private static TimeRange getPossibleTimeRangeForMeeting(Calendar firstCalendar, Calendar secondCalendar) throws InvalidTimeRangeException {

        var firstMeetingRange = firstCalendar.getCalendarTimeRange();
        var secondMeetingRange = secondCalendar.getCalendarTimeRange();

        LocalTime commonStartingHour = getLaterHour(firstMeetingRange.getStart(), secondMeetingRange.getStart());
        LocalTime commonEndHour = getEarlierHour(firstMeetingRange.getEnd(), secondMeetingRange.getEnd());

        return new TimeRange(commonStartingHour, commonEndHour);
    }

    private static LocalTime getLaterHour(@NotNull LocalTime firstTime, @NotNull LocalTime secondTime) {

        LocalTime laterHour;

        if (firstTime.getHour() == secondTime.getHour()) {

            if (firstTime.getMinute() == secondTime.getMinute()) {
                laterHour = firstTime; // or finalTimeRange[0] = secondMeetingRange[0], it doesn't matter at this point
            } else if (firstTime.getMinute() > secondTime.getMinute()) {
                laterHour = firstTime;
            } else {
                laterHour = secondTime;
            }

        } else if (firstTime.getHour() > secondTime.getHour()) {
            laterHour = firstTime;
        } else {
            laterHour = secondTime;
        }

        return laterHour;
    }

    private static LocalTime getEarlierHour(@NotNull LocalTime firstTime, @NotNull LocalTime secondTime) {

        LocalTime earlierHour;

        if (firstTime.getHour() == secondTime.getHour()) {

            if (firstTime.getMinute() == secondTime.getMinute()) {
                earlierHour = firstTime; // or finalTimeRange[0] = secondMeetingRange[0], it doesn't matter at this point
            } else if (firstTime.getMinute() < secondTime.getMinute()) {
                earlierHour = firstTime;
            } else {
                earlierHour = secondTime;
            }

        } else if (firstTime.getHour() < secondTime.getHour()) {
            earlierHour = firstTime;
        } else {
            earlierHour = secondTime;
        }

        return earlierHour;
    }

    private static List<Meeting> getAvailableTimeSlotsBasedOnMeetingDuration(Calendar calendar, String duration, TimeRange commonWorkingRange) throws InvalidTimeRangeException {

        List<Meeting> newMeetingSlots = new ArrayList<>();
        long durationInMinutes = Duration.between(LocalTime.parse("00:00"), LocalTime.parse(duration)).toMinutes();

        if(calendar.getArrayOfMeetings().isEmpty()){
            newMeetingSlots.addAll(createMeetingsFromMinutesAndStartTime(durationInMinutes, Duration.between(commonWorkingRange.getStart(), commonWorkingRange.getEnd()).toMinutes(), commonWorkingRange.getStart()));
            return newMeetingSlots;
        }
        createMeetingsSlotsBeforeFirstMeetingOrTheLastOne(calendar, commonWorkingRange, newMeetingSlots, durationInMinutes);
        createNewMeetingsSlots(calendar, newMeetingSlots, durationInMinutes);
        return newMeetingSlots;
    }

    private static void createMeetingsSlotsBeforeFirstMeetingOrTheLastOne(Calendar calendar, TimeRange commonWorkingRange, List<Meeting> newMeetingSlots, long durationInMinutes) throws InvalidTimeRangeException {
        long minutesBeforeFirstMeeting = Duration.between(commonWorkingRange.getStart(), calendar.getStartOfFirstElementOfList()).toMinutes();
        long minutesBeforeEndOfWorking = Duration.between(calendar.getEndOfLastElementOfList(), commonWorkingRange.getEnd()).toMinutes();

        if (minutesBeforeFirstMeeting >= durationInMinutes) {
            newMeetingSlots.addAll(createMeetingsFromMinutesAndStartTime(durationInMinutes, minutesBeforeFirstMeeting, commonWorkingRange.getStart()));
        }
        if (minutesBeforeEndOfWorking >= durationInMinutes) {
            newMeetingSlots.addAll(createMeetingsFromMinutesAndStartTime(durationInMinutes, minutesBeforeEndOfWorking, calendar.getEndOfLastElementOfList()));
        }
    }

    private static void createNewMeetingsSlots(Calendar calendar, List<Meeting> newMeetingSlots, long durationInMinutes) throws InvalidTimeRangeException {
        for (var i = 1; i < calendar.getArrayOfMeetings().size(); i++) {
            long spareTime = Duration.between(LocalTime.parse(calendar.getArrayOfMeetings().get(i - 1).getEnd()), calendar.getStartOfElementByIndex(i)).toMinutes();

            if (spareTime >= durationInMinutes) {
                newMeetingSlots.addAll(createMeetingsFromMinutesAndStartTime(durationInMinutes, spareTime, LocalTime.parse(calendar.getArrayOfMeetings().get(i - 1).getEnd())));
            }
        }
    }

    private static ArrayList<Meeting> createMeetingsFromMinutesAndStartTime(long durationInMinutes, long spareTimeInMinutes, LocalTime start) throws InvalidTimeRangeException {

        ArrayList<Meeting> meetingArrayList = new ArrayList<>();
        long minutesToBeSpent = spareTimeInMinutes - (spareTimeInMinutes % durationInMinutes);

        LocalTime startVar = start;
        LocalTime end;

        for (var i = 0; i < minutesToBeSpent / durationInMinutes; i++) {
            end = startVar.plusMinutes(durationInMinutes);
                var nextMeeting = new Meeting(startVar.toString(), end.toString());
                meetingArrayList.add(nextMeeting);
                startVar = startVar.plusMinutes(durationInMinutes);
        }
        return meetingArrayList;
    }
}
