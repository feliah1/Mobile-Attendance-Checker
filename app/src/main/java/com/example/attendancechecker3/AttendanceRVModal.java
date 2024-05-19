package com.example.attendancechecker3;

import android.os.Parcel;
import android.os.Parcelable;

public class AttendanceRVModal implements Parcelable {
    // Fields
    private String courseId;
    private int amountOfDaysPresent;
    private String status;
    private String inTime;
    private String outTime;
    private CourseRVModal course;

    // Constructor
    public AttendanceRVModal() {}

    public AttendanceRVModal(String courseId, int amountOfDaysPresent, String status, String inTime, String outTime) {
        this.courseId = courseId;
        this.amountOfDaysPresent = amountOfDaysPresent;
        this.status = status;
        this.inTime = inTime;
        this.outTime = outTime;
    }

    // Parcelable implementation
    protected AttendanceRVModal(Parcel in) {
        courseId = in.readString();
        amountOfDaysPresent = in.readInt();
        status = in.readString();
        inTime = in.readString();
        outTime = in.readString();
    }

    public static final Creator<AttendanceRVModal> CREATOR = new Creator<AttendanceRVModal>() {
        @Override
        public AttendanceRVModal createFromParcel(Parcel in) {
            return new AttendanceRVModal(in);
        }

        @Override
        public AttendanceRVModal[] newArray(int size) {
            return new AttendanceRVModal[size];
        }
    };

    // Getters and Setters
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getAmountOfDaysPresent() {
        return amountOfDaysPresent;
    }

    public void setAmountOfDaysPresent(int amountOfDaysPresent) {
        this.amountOfDaysPresent = amountOfDaysPresent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseId);
        dest.writeInt(amountOfDaysPresent);
        dest.writeString(status);
        dest.writeString(inTime);
        dest.writeString(outTime);
    }

    public CourseRVModal getCourse() {
        return course;
    }

    public void setCourse(CourseRVModal course) {
        this.course = course;
    }

}
