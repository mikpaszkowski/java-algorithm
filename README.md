# java-algorithm

Algorithm implemented in Java which is responsible for finding available common hours when two people can meet together.
Calendars consist of data such as:

* workingHours - time-range during which person works, it contains: 'start', 'end'
* meetings - array of meetings-objects schedulled in the day which consists of 'start', 'end' of specific meeting.

Based on these data the algorithm is creating array of time-ranges (List<List<String>>) during which these people can meet.

The project contains also:
* testing with JUnit
* defined custom exceptions
