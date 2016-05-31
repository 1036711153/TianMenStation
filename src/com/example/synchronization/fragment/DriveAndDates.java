package com.example.synchronization.fragment;

/**
 * Created by admin on 2016/3/5.
 */
public class DriveAndDates {
    private String driveNum;
    private String date;

    public DriveAndDates(String driveNum, String date) {
        this.driveNum = driveNum;
        this.date = date;
    }

    public String getDriveNum() {
        return driveNum;
    }

    public void setDriveNum(String driveNum) {
        this.driveNum = driveNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
