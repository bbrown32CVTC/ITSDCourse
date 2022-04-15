package edu.cvtc.bbrown32.itsdcourses;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.cvtc.bbrown32.itsdcourses.ITSDCoursesDatabaseContract.CourseInfoEntry;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder> {

    // Member variables
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private Cursor mCursor;
    private int mCourseTitlePosition;
    private int mCourseDescriptionPosition;
    private int mIdPosition;

    public CourseRecyclerAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mLayoutInflater = LayoutInflater.from(context);

        // Gets the positions of the columns we want
        populateColumnPositions();
    }

    private void populateColumnPositions() {
        if (mCursor != null) {
            // Gets column indexes from mCursor
            mCourseTitlePosition = mCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);
            mCourseDescriptionPosition = mCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_DESCRIPTION);
            mIdPosition = mCursor.getColumnIndex(CourseInfoEntry._ID);
        }
    }

    public void changeCursor(Cursor cursor) {
        // If the cursor is open, close it
        if (mCursor != null) {
            mCursor.close();
        }

        // Creates a new cursor based upon the object passed in
        mCursor = cursor;

        // Gets the positions of the columns in the cursor
        populateColumnPositions();

        // Tells the activity the data set has changed
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Moves the cursor to the correct row
        mCursor.moveToPosition(position);

        // Gets the actual values
        String courseTitle = mCursor.getString(mCourseTitlePosition);
        String courseDescription = mCursor.getString(mCourseDescriptionPosition);
        int id = mCursor.getInt(mIdPosition);

        // Passes the information into the holder
        holder.mCourseTitle.setText(courseTitle);
        holder.mCourseDescription.setText(courseDescription);
        holder.mID = id;
    }

    @Override
    public int getItemCount() {
        // Returns 0 if the cursor is null.
        // Retruns the count if there are records in it.
        return mCursor == null ? 0 : mCursor.getCount();
    }

    // ViewHolder Class
    public class ViewHolder extends RecyclerView.ViewHolder{

        // Member variables for this inner class
        public final TextView mCourseTitle;
        public final TextView mCourseDescription;
        public int mID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCourseTitle = (TextView)itemView.findViewById(R.id.course_title);
            mCourseDescription = (TextView)itemView.findViewById(R.id.course_description);

            // OnClickListener will pass the ID of the record to the new activity
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CourseActivity.class);
                    intent.putExtra(CourseActivity.COURSE_ID, mID);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
