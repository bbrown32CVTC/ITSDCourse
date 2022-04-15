package edu.cvtc.bbrown32.itsdcourses;

import android.provider.BaseColumns;

/**
 * Helper class used to set up constants that will be used in the creation
 * of the database tables.
 */

public final class ITSDCoursesDatabaseContract {

    // Private Constructor - This class will never be instantiated
    // and will only be used to hold references to the database
    // tables and fields.
    private ITSDCoursesDatabaseContract() {}

    // Allows an ID to be automatically added to each table.
    // ID is used to select or update individual records
    public static final class CourseInfoEntry implements BaseColumns {

        // Holds the name of the table and fields within
        public static final String TABLE_NAME = "course_info";
        public static final String COLUMN_COURSE_TITLE = "course_title";
        public static final String COLUMN_COURSE_DESCRIPTION = "course_description";

        // Holds the values for an index name and value per course_title
        public static final String INDEX1 = TABLE_NAME + "_index1";
        public static final String SQL_CREATE_INDEX1 = "CREATE INDEX " + INDEX1 + " ON " +
                TABLE_NAME + "(" + COLUMN_COURSE_TITLE + ")";

        // Creates the table using the table name, fields, and id
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_COURSE_TITLE + " TEXT NOT NULL, " +
                        COLUMN_COURSE_DESCRIPTION + " TEXT)";
    }
}
