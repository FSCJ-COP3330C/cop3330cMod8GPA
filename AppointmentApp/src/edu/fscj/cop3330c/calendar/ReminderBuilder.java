// CalendarReminder.java
// D. Singletary
// 2/19/23
// Interface which supports building and sending reminders

package edu.fscj.cop3330c.calendar;

public interface ReminderBuilder {
    // build a reminder in the form of a formatted String
    public Reminder buildReminder(Appointment appt);
}

interface ReminderSender {
    // send a reminder using contact's preferred notification method
    public void sendReminder(Reminder reminder);
}
