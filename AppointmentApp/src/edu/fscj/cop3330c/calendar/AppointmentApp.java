// AppointmentApp.java
// D. Singletary
// 1/23/22
// creates an appointment for a contact

package edu.fscj.cop3330c.calendar;

import edu.fscj.cop3330c.dispatch.Dispatcher;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Stream;

// Singleton for Stream
class ReminderStreamWrapper {
    private static Queue<Reminder> queue;
    private static Stream<Reminder> stream;

    private static ReminderStreamWrapper reminderStreamWrapper =
            new ReminderStreamWrapper();

    private ReminderStreamWrapper() {
        queue = new LinkedList<Reminder>();
        stream = queue.stream();
    }

    public static Stream<Reminder> getStream() {
        return stream;
    }

    public static Queue<Reminder> getQueue() {
        return queue;
    }
}
// main application class
public class AppointmentApp implements ReminderSender, Dispatcher<Reminder> {

    private ArrayList<Appointment> appointments = new ArrayList<>();

    // Use a Queue<LinkedList> to act as message queue for the dispatcher
    //private Queue<Reminder> queue = new LinkedList<Reminder>();
    private Queue<Reminder> queue;
    private Stream<Reminder> stream;

    private Random rand = new Random();
    private int numAppointments = 0;

    // dispatch the reminder using the dispatcher
    public void dispatch(Reminder reminder) {
        this.queue.add(reminder);
        // System.out.println("current queue length is " + this.queue.size()); // debug
    }



    // send a reminder using contact's preferred notification method
    public void sendReminder(Reminder reminder) {
        Contact c = reminder.getContact();
        if (c.getRemindPref() == ReminderPreference.NONE)
            System.out.println(
                    "Error: no Reminder Preference set for " + c.getName());
        else {
            if (c.getRemindPref() == ReminderPreference.EMAIL)
                System.out.println(
                        "Sending the following email message to " + c.getName() + " at " +
                                c.getEmail());
                // all that's left is phone!
            else if (c.getRemindPref() == ReminderPreference.PHONE)
                System.out.println(
                        "Sending the following SMS message to " + c.getName() + " at " +
                                c.getPhone());


            dispatch(reminder);
        }
    }

    private Appointment createRandomAppointment(Contact c) {
        ZonedDateTime apptTime, reminder;
        int plusVal = rand.nextInt() % 12 + 1;
        // create a future appointment using random month value
        apptTime = ZonedDateTime.now().plusMonths(plusVal);

        // create the appt reminder for the appointment time minus random (<24) hours
        // use absolute value in case random is negative to prevent reminders > appt
        int minusVal = Math.abs(rand.nextInt()) % 24 + 1;
        reminder = apptTime.minusHours(minusVal);
        // create an appointment using the contact and appt time
        int apptNum = appointments.size() + 1;
        Appointment appt = new Appointment("Test Appointment " + ++numAppointments,
                "This is test appointment " + numAppointments,
                c, apptTime);
        appt.setReminder(reminder);
        return appt;
    }

    private void addAppointments(Appointment... appts) {
        for (Appointment a : appts)
            appointments.add(a);
    }

    private void checkReminderTime(Appointment appt) {
        ZonedDateTime current = ZonedDateTime.now();
        ZonedDateTime dt = appt.getReminder();

        // see if it's time to send a reminder
        // TODO: create a Reminder class and override equals()
        if (    dt.getYear() == current.getYear() &&
                dt.getMonth() == current.getMonth() &&
                dt.getDayOfMonth() == current.getDayOfMonth() &&
                dt.getHour() == current.getHour() &&
                dt.getMinute() == current.getMinute()) {
            Reminder reminder = new Reminder().buildReminder(appt);
            sendReminder(reminder);
            reminder = new LocalizedReminder(new Reminder()).buildReminder(appt);
            sendReminder(reminder);
        }

    }
    // unit test
    public static void main(String[] args) {
        ZonedDateTime apptTime, reminder;

        AppointmentApp apptApp = new AppointmentApp();

        ZonedDateTime current = ZonedDateTime.now();

        apptApp.queue = ReminderStreamWrapper.getQueue();
        apptApp.stream = ReminderStreamWrapper.getStream();

        // start with a contact
        Contact c = new Contact("Smith", "John", "JohnSmith@email.com",
                "(904) 555-1212", ReminderPreference.PHONE,
                ZoneId.of("America/New_York"), new Locale("en"));
        Appointment a1 = apptApp.createRandomAppointment(c);
        a1.setReminder(current);

        // create more contacts to test locales
        c = new Contact("Coutaz", "Joëlle", "Joëlle.Coutaz@email.com",
                "33 01 09 75 83 51", ReminderPreference.EMAIL,
                ZoneId.of("Europe/Paris"), new Locale("fr"));
        Appointment a2 = apptApp.createRandomAppointment(c);
        a2.setReminder(current);

        c = new Contact("Bechtolsheim", "Andy", "Andy.Bechtolsheim@email.com",
                "33 01 09 75 83 51", ReminderPreference.EMAIL,
                ZoneId.of("Europe/Berlin"), new Locale("de"));
        Appointment a3 = apptApp.createRandomAppointment(c);
        a3.setReminder(current);

        c = new Contact("Peisu", "Xia", "Xia.Peisu@email.com",
                "33 01 09 75 83 51", ReminderPreference.EMAIL,
                ZoneId.of("Asia/Shanghai"), new Locale("zh"));
        Appointment a4 = apptApp.createRandomAppointment(c);
        a4.setReminder(current);

        apptApp.addAppointments(a1, a2, a3, a4);

        // send reminders where needed
        for (Appointment a : apptApp.appointments)
            apptApp.checkReminderTime(a);

        apptApp.stream.forEach(System.out::print);
    }
}
