package edu.cvtc.bbrown32.itsdcourses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import edu.cvtc.bbrown32.itsdcourses.ITSDCoursesDatabaseContract.CourseInfoEntry;

import androidx.annotation.Nullable;

/**
 * This class allows the database to be created and updated
 */

public class ITSDCoursesOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ITSDCourses_bbrown32.db";
    public static final int DATABASE_VERSION = 1;

    public ITSDCoursesOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creates the database by adding the table and index
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CourseInfoEntry.SQL_CREATE_TABLE);
        db.execSQL(CourseInfoEntry.SQL_CREATE_INDEX1);
        ITSDCoursesDataWorker worker = new ITSDCoursesDataWorker(db);
        worker.insertCourses();
    }

    // Updates the database
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
