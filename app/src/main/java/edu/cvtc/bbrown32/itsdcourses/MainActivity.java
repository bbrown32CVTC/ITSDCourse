package edu.cvtc.bbrown32.itsdcourses;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import edu.cvtc.bbrown32.itsdcourses.databinding.ActivityMainBinding;
import edu.cvtc.bbrown32.itsdcourses.ITSDCoursesDatabaseContract.CourseInfoEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Member variables
    private ITSDCoursesOpenHelper mDbOpenHelper;
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mCoursesLayoutManager;
    private CourseRecyclerAdapter mCourseRecyclerAdapter;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    public static final int LOADER_COURSES = 0;

    // Boolean to check if the 'onCreateLoader' method has run
    private boolean mIsCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializes mDbOpenHelper and sets the context to MainActivity
        mDbOpenHelper = new ITSDCoursesOpenHelper(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starts a new CourseActivity
                startActivity(new Intent(MainActivity.this, CourseActivity.class));
            }
        });

        initializeDisplayContent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Use restartLoader instead of initLoader to make sure you re-query the database each
        // time the activity is loaded in the app.
        LoaderManager.getInstance(this).restartLoader(LOADER_COURSES, null, this);
    }

    private void loadCourses() {
        // Opens the database in read mode
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

        // Creates a list of columns that should be returned
        String[] courseColumns = {
                CourseInfoEntry.COLUMN_COURSE_TITLE,
                CourseInfoEntry.COLUMN_COURSE_DESCRIPTION,
                CourseInfoEntry._ID};

        // Creates an order by field for sorting purposes
        String courseOrderBy = CourseInfoEntry.COLUMN_COURSE_TITLE;

        // Populate your cursor with the results of the query.
        final Cursor courseCursor = db.query(CourseInfoEntry.TABLE_NAME, courseColumns,
                null, null, null, null,
                courseOrderBy);

        // Associates the cursor with your RecyclerAdapter
        mCourseRecyclerAdapter.changeCursor(courseCursor);
    }

    private void initializeDisplayContent() {
        // Retrieves the information from the database
        DataManager.loadFromDatabase(mDbOpenHelper);

        // Sets a reference to your list of items layout
        mRecyclerItems = (RecyclerView) findViewById(R.id.list_items);
        mCoursesLayoutManager = new LinearLayoutManager(this);

        // We do not have a cursor yet, so pass null.
        mCourseRecyclerAdapter = new CourseRecyclerAdapter(this, null);

        // Displays the courses
        displayCourses();
    }

    private void displayCourses() {
        mRecyclerItems.setLayoutManager(mCoursesLayoutManager);
        mRecyclerItems.setAdapter(mCourseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        // Creates a new cursor loader
        CursorLoader loader = null;
        if (id == LOADER_COURSES) {
            loader = new CursorLoader(this) {
                @Override
                public Cursor loadInBackground() {
                    mIsCreated = true;

                    // Opens the database in read mode
                    SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

                    // Creates a list of columns that should be returned
                    String[] courseColumns = {
                            CourseInfoEntry.COLUMN_COURSE_TITLE,
                            CourseInfoEntry.COLUMN_COURSE_DESCRIPTION,
                            CourseInfoEntry._ID};

                    // Creates an order by field for sorting purposes
                    String courseOrderBy = CourseInfoEntry.COLUMN_COURSE_TITLE;

                    // Populate your cursor with the results of the query.
                    return db.query(CourseInfoEntry.TABLE_NAME, courseColumns,
                            null, null, null, null,
                            courseOrderBy);
                }
            };
        }
        return loader;
    }

    // Changes the cursor to the current data set if onCreateLoader method has ran
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOADER_COURSES && mIsCreated) {
            // Associate the cursor with your RecyclerAdapter
            mCourseRecyclerAdapter.changeCursor(data);
            mIsCreated = false;
        }
    }

    // Changes the cursor to null
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (loader.getId() == LOADER_COURSES) {
            // Change the cursor to null (cleanup)
            mCourseRecyclerAdapter.changeCursor(null);
        }
    }
}