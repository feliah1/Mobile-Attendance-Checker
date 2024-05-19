package com.example.attendancechecker3;

import android.os.Parcel;
import android.os.Parcelable;

public class CourseRVModal implements Parcelable {
    // creating variables for our different fields.
    private String courseName;
    private String coursePrice;
    private String bestSuitedFor;
    private String courseId;


    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }


    // creating an empty constructor.
    public CourseRVModal() {

    }

    protected CourseRVModal(Parcel in) {
        courseName = in.readString();
        courseId = in.readString();
        coursePrice = in.readString();
        bestSuitedFor = in.readString();
    }

    public static final Creator<CourseRVModal> CREATOR = new Creator<CourseRVModal>() {
        @Override
        public CourseRVModal createFromParcel(Parcel in) {
            return new CourseRVModal(in);
        }

        @Override
        public CourseRVModal[] newArray(int size) {
            return new CourseRVModal[size];
        }
    };

    // creating getter and setter methods.
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(String coursePrice) {
        this.coursePrice = coursePrice;
    }

    public String getBestSuitedFor() {
        return bestSuitedFor;
    }

    public void setBestSuitedFor(String bestSuitedFor) {
        this.bestSuitedFor = bestSuitedFor;
    }

    public CourseRVModal(String courseId, String courseName, String coursePrice, String bestSuitedFor) {
        this.courseName = courseName;
        this.courseId = courseId;
        this.coursePrice = coursePrice;
        this.bestSuitedFor = bestSuitedFor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseName);
        dest.writeString(courseId);
        dest.writeString(coursePrice);
        dest.writeString(bestSuitedFor);
    }

    public String getPrice() {
        return coursePrice;
    }

}
