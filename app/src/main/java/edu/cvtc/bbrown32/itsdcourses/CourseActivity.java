package edu.cvtc.bbrown32.itsdcourses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import edu.cvtc.bbrown32.itsdcourses.ITSDCoursesDatabaseContract.CourseInfoEntry;

public class CourseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Constant variables
    public static final String COURSE_ID = "edu.cvtc.bbrown32.itsdcourses.COURSE_ID";
    public static final String ORIGINAL_COURSE_TITLE = "edu.cvtc.bbrown32.itsdcourses." +
            "ORIGINAL_COURSE_TITLE";
    public static final String ORIGINAL_COURSE_DESCRIPTION = "edu.cvtc.bbrown32.itsdcourses." +
            "ORIGINAL_COURSE_DESCRIPTION";
    public static final int ID_NOT_SET = -1;
    public static final int LOADER_COURSES = 0;

    // Initializes new CourseInfo to empty
    private CourseInfo mCourse = new CourseInfo(0, "", "");

    // Member variables
    private boolean mIsNewCourse;
    private boolean mIsCancelling;
    private int mCourseId;
    private int mCourseTitlePosition;
    private int mCourseDescriptionPosition;
    private String mOriginalCourseTitle;
    private String mOriginalCourseDescription;

    // Member objects
    private EditText mTextCourseTitle;
    private EditText mTextCourseDescription;
    private ITSDCoursesOpenHelper mDbOpenHelper;
    private Cursor mCourseCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        // Initializes mDbOpenHelper and sets the context to CourseActivity
        mDbOpenHelper = new ITSDCoursesOpenHelper(this);

        readDisplayStateValues();

        // Saves the values if the bundle is null.
        // Restores the original values if not null.
        if (savedInstanceState == null) {
            saveOriginalCourseValues();
        } else {
            restoreOriginalCourseValues(savedInstanceState);
        }

        mTextCourseTitle = findViewById(R.id.text_course_title);
        mTextCourseDescription = findViewById(R.id.text_course_description);

        // If it is not a new course, load the course data into the layout
        if (!mIsNewCourse) {
            // Initializes the loader manager. Passes in the id and the callback class
            LoaderManager.getInstance(this).initLoader(LOADER_COURSES, null, this);
        }
    }

    private void loadCourseData() {
        // Opens a connection to the database
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

        // Builds the selection criteria.
        // Sets the ID of the course to the passed in course id from the Intent.
        String selection = CourseInfoEntry._ID + " = ?";
        String[] selectionArgs = {Integer.toString(mCourseId)};

        // Create a list of the columns you are pulling from
        // the database.
        String[] courseColumns = {
                CourseInfoEntry.COLUMN_COURSE_TITLE,
                CourseInfoEntry.COLUMN_COURSE_DESCRIPTION
        };

        // Fills the cursor with the information provided.
        mCourseCursor = db.query(CourseInfoEntry.TABLE_NAME, courseColumns,
                selection, selectionArgs, null, null, null);

        // Gets the positions of the fields in the cursor so that
        // you are able to retrieve them into the layout.
        mCourseTitlePosition = mCourseCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);
        mCourseDescriptionPosition = mCourseCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_DESCRIPTION);

        // Makes sure you have moved to the correct record.
        // The cursor will not have populated any of the
        // fields until you move it.
        mCourseCursor.moveToNext();

        // Calls the method to display the course.
        displayCourse();
    }

    private void displayCourse() {
        // Retrieves the values from the cursor based on position of columns
        String courseTitle = mCourseCursor.getString(mCourseTitlePosition);
        String courseDescription = mCourseCursor.getString(mCourseDescriptionPosition);

        // Uses the information to populate the layout
        mTextCourseTitle.setText(courseTitle);
        mTextCourseDescription.setText(courseDescription);
    }

    private void restoreOriginalCourseValues(Bundle savedInstanceState) {
        // Gets the original values from the savedInstanceState
        mOriginalCourseTitle = savedInstanceState.getString(ORIGINAL_COURSE_TITLE);
        mOriginalCourseDescription = savedInstanceState.getString(ORIGINAL_COURSE_DESCRIPTION);
    }

    private void saveOriginalCourseValues() {
        // Only saves values if not a new course
        if (!mIsNewCourse) {
            mOriginalCourseTitle = mCourse.getTitle();
            mOriginalCourseDescription = mCourse.getDescription();
        }
    }

    // Retrieves the information from the Intent passed to the activity
    private void readDisplayStateValues() {
        // Gets the intent passed into the activity
        Intent intent = getIntent();

        // Get the course id passed into the intent
        mCourseId = intent.getIntExtra(COURSE_ID, ID_NOT_SET);

        // If the course id is not set, create a new course
        mIsNewCourse = mCourseId == ID_NOT_SET;
        if (mIsNewCourse) {
            createNewCourse();
        }
    }

    // Closes the mDbOpenHelper object
    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        // Creates a local cursor loader
        CursorLoader loader = null;

        // Checks to see if the id is for your loader
        if (id == LOADER_COURSES) {
            loader = createLoaderCourses();
        }
        return loader;
    }

    private CursorLoader createLoaderCourses() {
        return new CursorLoader(this) {
            @Override
                    public Cursor loadInBackground() {
                // Opens a connection to the database
                SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

                // Builds the selection criteria.
                // Sets the ID of the course to the passed in course id from the Intent.
                String selection = CourseInfoEntry._ID + " = ?";
                String[] selectionArgs = {Integer.toString(mCourseId)};

                // Create a list of the columns you are pulling from
                // the database.
                String[] courseColumns = {
                        CourseInfoEntry.COLUMN_COURSE_TITLE,
                        CourseInfoEntry.COLUMN_COURSE_DESCRIPTION
                };

                // Fills the cursor with the information provided.
                return db.query(CourseInfoEntry.TABLE_NAME, courseColumns,
                        selection, selectionArgs, null, null, null);
            }
        };
    }

    // Called when data is loaded
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // Check to see if this is your cursor for your loader
        if (loader.getId() == LOADER_COURSES) {
            loadFinishedCourses(data);
        }
    }

    private void loadFinishedCourses(Cursor data) {
        // Populates member cursor with the data
        mCourseCursor = data;

        // Gets the positions of the fields in the cursor so that
        // you are able to retrieve them into the layout.
        mCourseTitlePosition = mCourseCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);
        mCourseDescriptionPosition = mCourseCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_DESCRIPTION);

        // Makes sure you have moved to the correct record.
        // The cursor will not have populated any of the
        // fields until you move it.
        mCourseCursor.moveToNext();

        // Calls the method to display the course.
        displayCourse();
    }

    // Called during cleanup
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // Checks to see if this is the cursor for the loader
        if (loader.getId() == LOADER_COURSES) {
            // If the cursor is not null, close it
            if (mCourseCursor != null) {
                mCourseCursor.close();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handles the action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();

        if (id == R.id.action_cancel) {
            mIsCancelling = true;
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    // Saves the changes to the data
    private void saveCourseToDatabase(String courseTitle, String courseDescription) {
        // Creates selection criteria
        final String selection = CourseInfoEntry._ID + " = ?";
        final String[] selectionArgs = {Integer.toString(mCourseId)};

        // Uses a ContentValues object to put our information into.
        ContentValues values = new ContentValues();
        values.put(CourseInfoEntry.COLUMN_COURSE_TITLE, courseTitle);
        values.put(CourseInfoEntry.COLUMN_COURSE_DESCRIPTION, courseDescription);

        AsyncTaskLoader<String> task = new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {

                // Gets a connection to the database. Uses the writable method since we are changing
                // the data
                SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

                // Calls the update method
                db.update(CourseInfoEntry.TABLE_NAME, values, selection, selectionArgs);
                return null;
            }
        };

        task.loadInBackground();
    }

    // Stores the original values
    private void storePreviousCourseValues() {
        mCourse.setTitle(mOriginalCourseTitle);
        mCourse.setDescription(mOriginalCourseDescription);
    }

    // Saves the course information
    private void saveCourse() {
        // Gets the values from the layout
        String courseTitle = mTextCourseTitle.getText().toString();
        String courseDescription = mTextCourseDescription.getText().toString();

        // Calls the method to write to the database
        saveCourseToDatabase(courseTitle, courseDescription);
    }

    // Creates a new instance of a course
    private void createNewCourse() {
        // Creates ContentValues object to hold the fields
        ContentValues values = new ContentValues();

        // For a new course, we don't know what the values will be, so we set the columns to
        // empty strings.
        values.put(CourseInfoEntry.COLUMN_COURSE_TITLE, "");
        values.put(CourseInfoEntry.COLUMN_COURSE_DESCRIPTION, "");

        // Gets connection to the database. Uses the writable method since we are changing the data.
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

        // Insert the new row in the database and assign the new id to our member variable for
        // course id. Cast the 'long' return value to an int.
        mCourseId = (int)db.insert(CourseInfoEntry.TABLE_NAME, null, values);
    }

    // Removes the row of the current ID from the database
    private void deleteCourseFromDatabase() {
        // Creates selection criteria
        final String selection = CourseInfoEntry._ID + " = ?";
        final String[] selectionArgs = {Integer.toString(mCourseId)};

        AsyncTaskLoader<String> task = new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {

                // Gets a connection to the database. Use the writable
                // method since we are changing the data.
                SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

                // Call the delete method
                db.delete(CourseInfoEntry.TABLE_NAME, selection, selectionArgs);
                return null;
            }
        };

        task.loadInBackground();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Did the user cancel the process?
        if (mIsCancelling) {
            // Is this a new course?
            if (mIsNewCourse) {
                // Deletes the new course.
                deleteCourseFromDatabase();
            } else {
                // Put the original values on the screen.
                storePreviousCourseValues();
            }
        } else {
            // Saves the data when leaving the activity
            saveCourse();
        }
    }

}