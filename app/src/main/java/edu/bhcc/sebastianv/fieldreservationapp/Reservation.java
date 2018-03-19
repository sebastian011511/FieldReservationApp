package edu.bhcc.sebastianv.fieldreservationapp;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by svasco on 11/2/2017.
 * class holds information for that one reservation.
 */

public class Reservation {

    private Date mDate;
    private Date mTime;
    private String mName;
    private long contact;//contact id
    private String mPhoneNumber;

    public Reservation(String name) {
        mName = name;
        mDate = new Date();
        mTime = new Date();
    }

    public Date getDateStamp() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }


    public Date getTimeStamp() {
        return mTime;
    }

    public String getDate() { //Turns a regular Date time stamp in Date class into a readble string.  Monday, September 25, 2017

        //SHORT=9/27/2017; MEDIUM=Sep 27, 2017;LONG=September 27,2017; Full=Wednesday September 27, 2017; Locale default grabs the location of the device and displays time accordingly
        DateFormat formatDate = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
        return formatDate.format(mDate); //After initiating DateFormat and passes Date instance.

    }

    public String getTime() {
        //Display Time 1;45pm (or according to local time
        DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        return formatTime.format(mTime);
    }

    public void setTime(Date time) {
        mTime = time;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getContact() {
        return contact;
    }

    public void setContact(long contact) {
        this.contact = contact;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }
}
