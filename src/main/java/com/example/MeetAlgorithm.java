package main.java.com.example;

import main.java.com.example.exceptions.InvalidTimeRange;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MeetAlgorithm {


    private MeetAlgorithm() {
        //static methods only
    }

    public static List<List<String>> getPossibleMeetingRanges(@NotNull Calendar firstCalendar, @NotNull Calendar secondCalendar, String meetingDuration) throws InvalidTimeRange {

        var commonWorkingRange = getPossibleTimeRangeForMeeting(firstCalendar, secondCalendar);

        List<Meeting> spareTimeSlotsFirstCal = getAvailableTimeSlotsBasedOnMeetingDuration(firstCalendar, meetingDuration, commonWorkingRange);
        List<Meeting> spareTimeSlotsSecondCal = getAvailableTimeSlotsBasedOnMeetingDuration(secondCalendar, meetingDuration, commonWorkingRange);

        List<Meeting> listOfMeeting = new ArrayList<>();
        for (Meeting meetingsOfFirstCal : spareTimeSlotsFirstCal) {
            for (Meeting meetingsOfSecondCal : spareTimeSlotsSecondCal) {
                if (meetingsOfFirstCal.getStart().equals(meetingsOfSecondCal.getStart())) {
                    var newMeeting = new Meeting(meetingsOfFirstCal.getStart(), meetingsOfFirstCal.getEnd());
                    listOfMeeting.add(newMeeting);
                }
            }
        }
        return minimizeMeetingsRangesInList(listOfMeeting);
    }

    private static List<List<String>> minimizeMeetingsRangesInList(List<Meeting> listOfMeetings){

        List<List<String>> minimizedList = new ArrayList<>();
        List<Meeting> meetings = listOfMeetings;

        for(var i = 0; i < meetings.size() - 1; i++){
            if(meetings.get(i).getEnd().equals(meetings.get(i + 1).getStart())){
                meetings.get(i).setEnd(meetings.get(i + 1).getEnd());
                meetings.remove(i + 1);
                --i;
            }
        }

        meetings = sortTheListOfMeetingsAsc(meetings);

        for (Meeting meeting : meetings) {
                   List<String> singleMeeting = new ArrayList<>();
                   singleMeeting.add(meeting.getStart());
                   singleMeeting.add(meeting.getEnd());
                   minimizedList.add(singleMeeting);
        }
        return minimizedList;
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

    private static TimeRange getPossibleTimeRangeForMeeting(Calendar firstCalendar, Calendar secondCalendar) {

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

    private static List<Meeting> getAvailableTimeSlotsBasedOnMeetingDuration(Calendar calendar, String duration, TimeRange commonWorkingRange) throws InvalidTimeRange {

        List<Meeting> newMeetingSlots = new ArrayList<>();
        long durationInMinutes = Duration.between(LocalTime.parse("00:00"), LocalTime.parse(duration)).toMinutes();

        if(calendar.getArrayOfMeetings().isEmpty()){
            newMeetingSlots.addAll(createMeetingsFromMinutesAndStartTime(durationInMinutes, Duration.between(commonWorkingRange.getStart(), commonWorkingRange.getEnd()).toMinutes(), commonWorkingRange.getStart()));
            return newMeetingSlots;
        }
        long minutesBeforeFirstMeeting = Duration.between(commonWorkingRange.getStart(), calendar.getStartOfFirstElementOfList()).toMinutes();
        long minutesBeforeEndOfWorking = Duration.between(calendar.getEndOfLastElementOfList(), commonWorkingRange.getEnd()).toMinutes();

            if (minutesBeforeFirstMeeting >= durationInMinutes) {
                newMeetingSlots.addAll(createMeetingsFromMinutesAndStartTime(durationInMinutes, minutesBeforeFirstMeeting, commonWorkingRange.getStart()));
            }
            if (minutesBeforeEndOfWorking >= durationInMinutes) {
                newMeetingSlots.addAll(createMeetingsFromMinutesAndStartTime(durationInMinutes, minutesBeforeEndOfWorking, calendar.getEndOfLastElementOfList()));
            }

        for (var i = 1; i < calendar.getArrayOfMeetings().size(); i++) {
            long spareTime = Duration.between(LocalTime.parse(calendar.getArrayOfMeetings().get(i - 1).getEnd()), calendar.getStartOfElementByIndex(i)).toMinutes();

            if (spareTime >= durationInMinutes) {
                newMeetingSlots.addAll(createMeetingsFromMinutesAndStartTime(durationInMinutes, spareTime, LocalTime.parse(calendar.getArrayOfMeetings().get(i - 1).getEnd())));
            }
        }
        return newMeetingSlots;
    }

    private static ArrayList<Meeting> createMeetingsFromMinutesAndStartTime(long durationInMinutes, long spareTimeInMinutes, LocalTime start) throws InvalidTimeRange {

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
