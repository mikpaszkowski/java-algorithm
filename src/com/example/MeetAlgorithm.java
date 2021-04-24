package com.example;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class MeetAlgorithm {

    private Calendar firstCalendar;
    private Calendar secondCalendar;
    private String meetingDuration;

    public MeetAlgorithm(Calendar firstCalendar, Calendar secondCalendar, String meetingDuartion) {
        this.firstCalendar = firstCalendar;
        this.secondCalendar = secondCalendar;
        this.meetingDuration = meetingDuartion;
    }

    public ArrayList<ArrayList<String>> getPossibleMeetingRanges(){

        LocalTime[] finalTimeRanges = getPossibleTimeRangeForMeeting();

//        System.out.println(finalTimeRanges[0]);
//        System.out.println(finalTimeRanges[1]);

        ArrayList<ArrayList<String>> outputMeetings = new ArrayList<>();

        ArrayList<Meeting> spareTimeSlotsFirstCal = getSpareTimeSlotsBasedOnMeetingDuration(firstCalendar.getArrayOfMeetings(), this.meetingDuration, finalTimeRanges);
        ArrayList<Meeting> spareTimeSlotsSecondCal = getSpareTimeSlotsBasedOnMeetingDuration(secondCalendar.getArrayOfMeetings(), this.meetingDuration, finalTimeRanges);

        for (Meeting meetingsOfFirstCal : spareTimeSlotsFirstCal) {
            for (Meeting meetingsOfSecondCal : spareTimeSlotsSecondCal) {
                if (meetingsOfFirstCal.getStart().equals(meetingsOfSecondCal.getStart())) {
                    ArrayList<String> singleMeeting = new ArrayList<>();
                    singleMeeting.add(meetingsOfFirstCal.getStart());
                    singleMeeting.add(meetingsOfFirstCal.getEnd());
                    outputMeetings.add(singleMeeting);
                }
            }
        }
        System.out.println(outputMeetings);
        return outputMeetings;
    }

    private LocalTime[] getPossibleTimeRangeForMeeting(){

        LocalTime[] firstMeetingRange = this.firstCalendar.getCalendarWorkingRange();
        LocalTime[] secondMeetingRange = this.secondCalendar.getCalendarWorkingRange();

        LocalTime[] finalTimeRange = new LocalTime[2];

        finalTimeRange[0] = getLaterHour(firstMeetingRange[0], secondMeetingRange[0]);
        finalTimeRange[1] = getEarlierHour(firstMeetingRange[1], secondMeetingRange[1]);

        return finalTimeRange;
    }

    private LocalTime getLaterHour(@NotNull LocalTime firstTime, @NotNull LocalTime secondTime){

        LocalTime laterHour;

        if(firstTime.getHour() == secondTime.getHour()){

            if(firstTime.getMinute() == secondTime.getMinute()){
                laterHour = firstTime; // or finalTimeRange[0] = secondMeetingRange[0], it doesn't matter at this point
            }else if(firstTime.getMinute() > secondTime.getMinute()){
                laterHour = firstTime;
            }else{
                laterHour = secondTime;
            }

        }else if(firstTime.getHour() > secondTime.getHour()){
            laterHour = firstTime;
        }else{
            laterHour = secondTime;
        }

        return laterHour;
    }

    private LocalTime getEarlierHour(@NotNull LocalTime firstTime, @NotNull LocalTime secondTime){

        LocalTime earlierHour;

        if(firstTime.getHour() == secondTime.getHour()){

            if(firstTime.getMinute() == secondTime.getMinute()){
                earlierHour = firstTime; // or finalTimeRange[0] = secondMeetingRange[0], it doesn't matter at this point
            }else if(firstTime.getMinute() < secondTime.getMinute()){
                earlierHour = firstTime;
            }else{
                earlierHour = secondTime;
            }

        }else if(firstTime.getHour() < secondTime.getHour()){
            earlierHour = firstTime;
        }else{
            earlierHour = secondTime;
        }

        return earlierHour;
    }

    private ArrayList<Meeting> getSpareTimeSlotsBasedOnMeetingDuration(ArrayList<Meeting> meetingArray, String duration, LocalTime[] finalTimeRanges){

        ArrayList<Meeting> newMeetingSlots = new ArrayList<>();

        long durationInMinutes = Duration.between(LocalTime.parse("00:00"), LocalTime.parse(duration)).toMinutes();
        long minutesBeforeFirstMeeting= Duration.between(finalTimeRanges[0], LocalTime.parse(meetingArray.get(0).getStart())).toMinutes();
        long minutesBeforeEndOfWorking= Duration.between(LocalTime.parse(meetingArray.get(meetingArray.size() - 1).getEnd()), finalTimeRanges[1]).toMinutes();

        if(minutesBeforeFirstMeeting >= durationInMinutes){
            newMeetingSlots.addAll(createMeetingsFromMinutesAndStartTime(durationInMinutes, minutesBeforeFirstMeeting, finalTimeRanges[0]));
        }
        if(minutesBeforeEndOfWorking >= durationInMinutes){
            newMeetingSlots.addAll(createMeetingsFromMinutesAndStartTime(durationInMinutes, minutesBeforeEndOfWorking, LocalTime.parse(meetingArray.get(meetingArray.size() - 1).getEnd())));
        }
//        System.out.println("LocalTime.parse(meetingArray.get(meetingArray.size() - 1).getEnd()) : " + LocalTime.parse(meetingArray.get(meetingArray.size() - 1).getEnd()));
//        System.out.println("finalTimeRanges[0] : " + finalTimeRanges[0]);
//        System.out.println("LocalTime.parse(meetingArray.get(0).getStart()) : " + LocalTime.parse(meetingArray.get(0).getStart()));
//        System.out.println("beforeForstMeeting " + minutesBeforeFirstMeeting);
//        System.out.println("beforeSecondMeeting " + minutesBeforeEndOfWorking);

        for(int i = 1; i < meetingArray.size(); i++){
//                System.out.println("meetingArray.get(j) : " + meetingArray.get(i - 1));
//                System.out.println("meetingArray.get(i)" + meetingArray.get(i));
//                System.out.println("END : " + meetingArray.get(i - 1).getEnd() + "  START : " + meetingArray.get(i).getStart());
            long spareTime = Duration.between(LocalTime.parse(meetingArray.get(i - 1).getEnd()), LocalTime.parse(meetingArray.get(i).getStart())).toMinutes();
            //System.out.println(time);

            if(spareTime >= durationInMinutes){
                newMeetingSlots.addAll(createMeetingsFromMinutesAndStartTime(durationInMinutes, spareTime, LocalTime.parse(meetingArray.get(i - 1).getEnd())));
            }
        }

        System.out.println(newMeetingSlots);

        return newMeetingSlots;
    }

    private ArrayList<Meeting> createMeetingsFromMinutesAndStartTime(long durationInMinutes, long spareTimeInMinutes,LocalTime start){

        ArrayList<Meeting> meetingArrayList = new ArrayList<>();

        long MinutesToBeSpent = spareTimeInMinutes - (spareTimeInMinutes % durationInMinutes);

        LocalTime startVar = start;
        LocalTime end;

        for(int i = 0; i < MinutesToBeSpent / durationInMinutes; i++){
            end = startVar.plusMinutes(durationInMinutes);
            Meeting nextMeeting = new Meeting(startVar.toString(), end.toString());
            meetingArrayList.add(nextMeeting);
            startVar = startVar.plusMinutes(durationInMinutes);
        }
        //System.out.println("meetingArrayList : " + meetingArrayList);
        return meetingArrayList;
    }


    public Calendar getFirstCalendar() {
        return firstCalendar;
    }

    public void setFirstCalendar(Calendar firstCalendar) {
        this.firstCalendar = firstCalendar;
    }

    public Calendar getSecondCalendar() {
        return secondCalendar;
    }

    public void setSecondCalendar(Calendar secondCalendar) {
        this.secondCalendar = secondCalendar;
    }

    public String getMeetingDuration() {
        return meetingDuration;
    }

    public void setMeetingDuration(String meetingDuration) {
        this.meetingDuration = meetingDuration;
    }

    public static void main(String[] args) {

        Calendar calendar1 = new Calendar("10:30", "16:50");
        try{
            calendar1.addMeeting("12:30", "13:20");
            calendar1.addMeeting("14:40", "16:00");
            calendar1.addMeeting("16:15", "16:50");
        }catch(Exception e){
            e.printStackTrace();
        }

        Calendar calendar2 = new Calendar("10:30", "16:49");
        try{
            calendar2.addMeeting("11:00", "12:00");
            calendar2.addMeeting("12:30", "13:00");
            calendar2.addMeeting("13:40", "15:00");
            calendar2.addMeeting("15:20", "16:00");
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println(calendar1);
        System.out.println(calendar2);

        MeetAlgorithm algorithm = new MeetAlgorithm(calendar1, calendar2, "00:30");

        algorithm.getPossibleMeetingRanges();
    }
}
