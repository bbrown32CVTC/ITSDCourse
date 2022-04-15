package edu.cvtc.bbrown32.itsdcourses;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import edu.cvtc.bbrown32.itsdcourses.ITSDCoursesDatabaseContract.CourseInfoEntry;

/**
 * This class communicates with the database.
 * Used to load and save information to the database.
 */

public class DataManager {
    //Member attributes that hold the instance of the DataManager and list of courses
    private static DataManager ourInstance = null;
    private List<CourseInfo> mCourses = new ArrayList<>();

    // Sets a reference to the new instance using the getInstance method.
    // A DataManager instance will be assigned by this method if not already assigned.
    public static DataManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataManager();
        }
        return ourInstance;
    }

    // Returns a list of courses
    public List<CourseInfo> getCourses() {
        return mCourses;
    }

    private static void loadCoursesFromDatabase(Cursor cursor) {
        // Retrieves the field positions in the database.
        // The positions of fields may change over time as the database grows, so
        // you want to use your constants to reference where those positions are in
        // the table.
        int listTitlePosition = cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);
        int listDescriptionPosition = cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_DESCRIPTION);
        int idPosition = cursor.getColumnIndex(CourseInfoEntry._ID);

        // Creates an instance of the DataManager and uses the DataManager
        // to clear any information from the array list.
        DataManager dm = getInstance();
        dm.mCourses.clear();

        // Loops through the cursor rows and adds new course objects to the array list.
        while (cursor.moveToNext()) {
            String listTitle = cursor.getString(listTitlePosition);
            String listDescription = cursor.getString(listDescriptionPosition);
            int id = cursor.getInt(idPosition);
            CourseInfo list = new CourseInfo(id, listTitle, listDescription);
            dm.mCourses.add(list);
        }

        // Close the cursor (to prevent memory leaks)
        cursor.close();
    }

    // Populates the Cursor object before calling the loadCoursesFromDatabase method
    public static void loadFromDatabase(ITSDCoursesOpenHelper dbHelper) {
        // Opens the database in read mode
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Creates a list of columns that should be returned
        String[] courseColumns = {
                CourseInfoEntry.COLUMN_COURSE_TITLE,
                CourseInfoEntry.COLUMN_COURSE_DESCRIPTION,
                CourseInfoEntry._ID};

        // Creates an order by field for sorting purposes
        String courseOrderBy = CourseInfoEntry.COLUMN_COURSE_TITLE;

        // Populates the cursor with the results of the query
        final Cursor courseCursor = db.query(CourseInfoEntry.TABLE_NAME, courseColumns,
                null, null, null, null, courseOrderBy);

        // Calls the method to load the array list
        loadCoursesFromDatabase(courseCursor);
    }

    public int createNewCourse() {
        // Creates an empty course object to use on the activity screen
        // when you want a "blank" record to show up. It will return the
        // size of the new course array list.
        CourseInfo course = new CourseInfo(null, null);
        mCourses.add(course);
        return mCourses.size();
    }

    public void removeCourse(int index) {
        mCourses.remove(index);
    }
}
