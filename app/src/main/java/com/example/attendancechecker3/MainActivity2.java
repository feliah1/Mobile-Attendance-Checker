package com.example.attendancechecker3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements CourseRVAdapter.CourseClickInterface {

    private static final String TAG = "MainActivity2";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private ArrayList<CourseRVModal> courseRVModalArrayList;
    private CourseRVAdapter courseRVAdapter;
    private RelativeLayout homeRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // initializing all our variables.
        RecyclerView courseRV = findViewById(R.id.idRVCourses);
        homeRL = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);

        // creating variables for fab, firebase database,
        // progress bar, list, adapter, firebase auth,
        // recycler view, and relative layout.
        FloatingActionButton addCourseFAB = findViewById(R.id.idFABAddCourse);
        firebaseDatabase = FirebaseDatabase.getInstance("https://awesome1111meow-default-rtdb.asia-southeast1.firebasedatabase.app");
        mAuth = FirebaseAuth.getInstance();
        courseRVModalArrayList = new ArrayList<>();

        // getting database reference.
        databaseReference = firebaseDatabase.getReference("Courses");

        // adding a click listener for our floating action button.
        addCourseFAB.setOnClickListener(v -> {
            // opening a new activity for adding a course.
            Intent i = new Intent(MainActivity2.this, Add_Course_Activity.class);
            startActivity(i);
        });

        // initializing our adapter class.
        courseRVAdapter = new CourseRVAdapter(courseRVModalArrayList, this, this::onCourseClick);

        // setting layout manager to recycler view.
        courseRV.setLayoutManager(new LinearLayoutManager(this));

        // setting adapter to recycler view.
        courseRV.setAdapter(courseRVAdapter);

        // calling a method to fetch courses from the database.
        getCourses();
    }

    private void getCourses() {
        // clearing our list.
        courseRVModalArrayList.clear();

        // adding child event listener method to read the data.
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // hiding our progress bar.
                loadingPB.setVisibility(View.GONE);

                // checking if snapshot exists.
                if (snapshot.exists()) {
                    CourseRVModal course = snapshot.getValue(CourseRVModal.class);
                    if (course != null) {
                        // adding snapshot to our array list.
                        courseRVModalArrayList.add(course);
                        // notifying our adapter that data has changed.
                        courseRVAdapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Course data is null!");
                    }
                } else {
                    Log.e(TAG, "Snapshot does not exist!");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // hiding our progress bar.
                loadingPB.setVisibility(View.GONE);
                courseRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                courseRVAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                courseRVAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handling database error.
                Log.e(TAG, "Database error: " + error.getMessage());
                Toast.makeText(MainActivity2.this, "Failed to load courses. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCourseClick(int position) {
        // calling a method to display a bottom sheet.
        displayBottomSheet(courseRVModalArrayList.get(position));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // adding a click listener for option selected.
        int id = item.getItemId();
        if (id == R.id.idLogOut) {
            // displaying a toast message on user logged out.
            Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_LONG).show();

            // signing out the user.
            mAuth.signOut();

            // opening the login activity.
            Intent i = new Intent(MainActivity2.this, Login.class);
            startActivity(i);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflating our menu file for displaying our menu options.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void displayBottomSheet(CourseRVModal modal) {
        // Creating our bottom sheet dialog.
        final BottomSheetDialog bottomSheetTeachersDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);

        // Inflating our layout file for our bottom sheet.
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout, homeRL);

        // Setting content view for bottom sheet.
        bottomSheetTeachersDialog.setContentView(layout);

        // Making the bottom sheet cancelable.
        bottomSheetTeachersDialog.setCancelable(false);
        bottomSheetTeachersDialog.setCanceledOnTouchOutside(true);

        // Displaying the bottom sheet.
        bottomSheetTeachersDialog.show();

        // Creating variables for our text view and image view inside bottom sheet
        // and initializing them with their IDs.
        TextView courseNameTV = layout.findViewById(R.id.idTVCourseName);
        TextView suitedForTV = layout.findViewById(R.id.idTVSuitedFor);
        TextView priceTV = layout.findViewById(R.id.idTVCoursePrice);
        ImageView courseIV = layout.findViewById(R.id.idIVCourse);
        Button viewBtn = layout.findViewById(R.id.idBtnVIewDetails);
        Button editBtn = layout.findViewById(R.id.idBtnEditCourse);

        // Setting data to different views.
        courseNameTV.setText(modal.getCourseName());
        suitedForTV.setText("Suited for " + modal.getBestSuitedFor());
        priceTV.setText("Rs." + modal.getCoursePrice());

        // Adding click listener for the edit button.
        editBtn.setOnClickListener(v -> {
            // Opening the EditCourseActivity.
            Intent i = new Intent(MainActivity2.this, EditCourseActivity.class);
            i.putExtra("course", modal);
            startActivity(i);
        });

        // Adding click listener for the view button.
        viewBtn.setOnClickListener(v -> {
            // Navigating to the browser for displaying course details from its URL.
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(modal.getCourseName()));
            startActivity(i);
        });

        // Adding an OnDismissListener to release any resources when the dialog is dismissed.
        bottomSheetTeachersDialog.setOnDismissListener(dialog -> {
            // Perform any resource cleanup here if needed.
        });
    }

}
