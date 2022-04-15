package edu.cvtc.bbrown32.itsdcourses;

import android.os.Parcel;
import android.os.Parcelable;

public class CourseInfo implements Parcelable {

    // Member attributes
    private String mTitle;
    private String mDescription;
    private int mId;

    // Overloaded Constructors
    public CourseInfo(String title, String description) {
        mTitle = title;
        mDescription = description;
    }
    public CourseInfo(int id, String title, String description) {
        mId = id;
        mTitle = title;
        mDescription = description;
    }

    // Getters and Setters
    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    // Returns a concatenated title and description
    private String getCompareKey() {
        return mTitle + "|" + mDescription;
    }

    // Stops duplicate courses from being added
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseInfo that = (CourseInfo) o;
        return getCompareKey().equals(that.getCompareKey());
    }

    //Pulls out rows of data for comparison
    @Override
    public int hashCode() {
        return getCompareKey().hashCode();
    }
    @Override
    public String toString() {
        return getCompareKey();
    }

    // Parcelable implementation and methods
    protected CourseInfo(Parcel parcel) {
        setTitle(parcel.readString());
        setDescription(parcel.readString());
    }

    public static final Creator<CourseInfo> CREATOR = new Creator<CourseInfo>() {
        @Override
        public CourseInfo createFromParcel(Parcel parcel) {
            return new CourseInfo(parcel);
        }

        @Override
        public CourseInfo[] newArray(int size) {
            return new CourseInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    // Writes the title and description to the parcel package
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mTitle);
        parcel.writeString(mDescription);
    }
}
