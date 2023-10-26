// Reminder.java
// D. Singletary
// 2/19/23
// Class which represents a reminder for an appointment

package edu.fscj.cop3330c.calendar;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Reminder implements ReminderBuilder {

    protected String message;
    protected ZonedDateTime dateTime;
    protected Contact contact;

    public Reminder() { }

    public Reminder(String message, ZonedDateTime dateTime, Contact contact) {
        this.message = message;
        this.dateTime = dateTime;
        this.contact = contact;
    }

    public String getMessage() {
        return message;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public Contact getContact() {
        return contact;
    }

    @Override public String toString() {
        return message;
    }

    // build a reminder in the form of a formatted String
    public  Reminder buildReminder(Appointment appt) {

//        ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//        + Tuesday, February 28, 2023 at 6:32:44 PM Eastern Standard Time +
//        + You have an appointment!                                       +
//        + John Smith                                                     +
//        + Title: Dentist                                                 +
//        + Description: Cleaning appointment with Dr. Kildaire            +
//        ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        final String NEWLINE = "\n";

        Contact c = appt.getContact();

        // Get the dates/times
        ZonedDateTime appTime = appt.getApptTime();
        ZonedDateTime remTime = appt.getReminder();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy");
        String dateStr = appTime.format(formatter);
        formatter = DateTimeFormatter.ofPattern("hh:mm a VV");
        String timeStr = appTime.format(formatter);

        // build the reminder message
        // embed newlines so we can split per line and use token (line) lengths
        String msg =
                "Hello, " + c.getFName() + " " + c.getLName() + "!" + NEWLINE +
                        "This is a reminder that you have an upcoming appointment." + NEWLINE +
                        NEWLINE +
                        "Title: " + appt.getTitle() + NEWLINE +
                        "Description: " + appt.getDescription() + NEWLINE +
                        "Date: " + dateStr + NEWLINE +
                        "Time: " + timeStr + NEWLINE;
        // split and get the max length
        String[] msgSplit = msg.split(NEWLINE);
        int maxLen = 0;
        for (String s : msgSplit)
            if (s.length() > maxLen)
                maxLen = s.length();
        maxLen += 4; // Adjust for padding and new line

        // create our header/footer (all plus signs)
        char[] plusChars = new char[maxLen];
        Arrays.fill(plusChars, '+');
        String headerFooter = new String(plusChars);

        // add the header to our output
        String newMsg = headerFooter + "\n";

        // reuse the header template for our body lines (plus/spaces/plus)
        Arrays.fill(plusChars, ' ');
        plusChars[0] = plusChars[maxLen - 1] = '+';
        String bodyLine = new String(plusChars);

        // for each string in the output, insert into a body line
        for (String s : msgSplit) {
            StringBuilder sBld = new StringBuilder(bodyLine);
            // add 2 to end position in body line replace
            // operation so final space/plus don't get pushed out
            sBld.replace(2,s.length() + 2, s);
            // add to our output
            newMsg += new String(sBld) + "\n";
        }
        newMsg += headerFooter + "\n";
        //System.out.println(newMsg); //debug

        Reminder newReminder = new Reminder(newMsg, appt.getReminder(), c);
        return newReminder;

    }
}

class LocalizedReminder extends Reminder {

    // Add your code here

    // build a reminder in the form of a formatted String
    public Reminder buildReminder(Appointment appt) {

//        ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//        + Tuesday, February 28, 2023 at 6:32:44 PM Eastern Standard Time +
//        + You have an appointment!                                       +
//        + John Smith                                                     +
//        + Title: Dentist                                                 +
//        + Description: Cleaning appointment with Dr. Kildaire            +
//        ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        final String NEWLINE = "\n";

        Contact c = appt.getContact();

        // Get the dates/times
        ZonedDateTime appTime = appt.getApptTime();
        ZonedDateTime remTime = appt.getReminder();

        // build the reminder message
        // embed newlines so we can split per line and use token (line) lengths
        String msg = "";
        try {
            // load the property and create the localized greeting
            ResourceBundle res = ResourceBundle.getBundle(
                    "edu.fscj.cop3330c.calendar.Reminder", c.getLocale());
            String youHaveAnAppointment = res.getString("YouHaveAnAppointment");

            // format and display the date
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL);
            formatter =
                    formatter.localizedBy(c.getLocale());
            msg = appt.getReminder().format(formatter) + NEWLINE;

            // add the localized reminder
            msg += youHaveAnAppointment + "\n" + c.getName() + NEWLINE;
        } catch (java.util.MissingResourceException e) {
            System.err.println(e);
            msg = "You Have An Appointment!" + NEWLINE + c.getName() + NEWLINE;
        }

        msg +=
                "Title: " + appt.getTitle() + NEWLINE +
                        "Description: " + appt.getDescription() + NEWLINE;
        // split and get the max length
        String[] msgSplit = msg.split(NEWLINE);
        int maxLen = 0;
        for (String s : msgSplit)
            if (s.length() > maxLen)
                maxLen = s.length();
        maxLen += 4; // Adjust for padding and new line

        // create our header/footer (all plus signs)
        char[] plusChars = new char[maxLen];
        Arrays.fill(plusChars, '+');
        String headerFooter = new String(plusChars);

        // add the header to our output
        String newMsg = headerFooter + "\n";

        // reuse the header template for our body lines (plus/spaces/plus)
        Arrays.fill(plusChars, ' ');
        plusChars[0] = plusChars[maxLen - 1] = '+';
        String bodyLine = new String(plusChars);

        // for each string in the output, insert into a body line
        for (String s : msgSplit) {
            StringBuilder sBld = new StringBuilder(bodyLine);
            // add 2 to end position in body line replace
            // operation so final space/plus don't get pushed out
            sBld.replace(2,s.length() + 2, s);
            // add to our output
            newMsg += new String(sBld) + "\n";
        }
        newMsg += headerFooter + "\n";
        //System.out.println(newMsg); //debug

        Reminder newReminder = new Reminder(newMsg, appt.getReminder(), c);
        return newReminder;
    }
}