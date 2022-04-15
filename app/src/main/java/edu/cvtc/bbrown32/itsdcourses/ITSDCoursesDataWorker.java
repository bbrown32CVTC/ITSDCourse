package edu.cvtc.bbrown32.itsdcourses;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import edu.cvtc.bbrown32.itsdcourses.ITSDCoursesDatabaseContract.CourseInfoEntry;

/**
 * Class is designed to help create the database by establishing
 * a connection to the database and insert the default data into
 * the table.
 */

public class ITSDCoursesDataWorker {

    // Member attributes
    private SQLiteDatabase mDb;

    // Constructor
    public ITSDCoursesDataWorker(SQLiteDatabase db) {
        mDb = db;
    }

    // Used to populate a row of data in the database
    private void insertCourse(String title, String description) {
        ContentValues values = new ContentValues();
        values.put(CourseInfoEntry.COLUMN_COURSE_TITLE, title);
        values.put(CourseInfoEntry.COLUMN_COURSE_DESCRIPTION, description);
        long newRowId = mDb.insert(CourseInfoEntry.TABLE_NAME, null, values);
    }

    // Used to populate database with rows of initial data
    public void insertCourses() {
        insertCourse("Intro to Computers & Programming", "Introductory " +
                "Computer Course");
        insertCourse("Web 1 - HTML & CSS", "Introductory HTML course");
        insertCourse("Programming Fundamentals", "Introductory Programming Course");
        insertCourse("Database 1", "Introductory Database Course");
        insertCourse("Object Oriented Programming", "Second Semester Programming " +
                "Course using Java");
        insertCourse(".NET Application Development", "Second Semester Programming " +
                "Course using .NET");
        insertCourse("Database 2", "Intermediate Database Course");
        insertCourse("Android Development", "Application Development Course with " +
                "Android");
    }
}
