package com.example.attendancechecker3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Add_Course_Activity extends AppCompatActivity {

    private TextInputEditText courseNameEdt, coursePriceEdt, bestSuitedEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ProgressBar loadingPB;
    private String courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        // initializing all our variables.
        // creating variables for our button, edit text,
        // firebase database, database reference, progress bar.
        Button addCourseBtn = findViewById(R.id.idBtnAddCourse);
        courseNameEdt = findViewById(R.id.idEdtCourseName);
        coursePriceEdt = findViewById(R.id.idEdtCoursePrice);
        bestSuitedEdt = findViewById(R.id.idEdtSuitedFor);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance("https://awesome1111meow-default-rtdb.asia-southeast1.firebasedatabase.app");
        // on below line creating our database reference.
        databaseReference = firebaseDatabase.getReference("Courses");
        // adding click listener for our add course button.
        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                // getting data from our edit text.
                String courseName = Objects.requireNonNull(courseNameEdt.getText()).toString();
                String coursePrice = Objects.requireNonNull(coursePriceEdt.getText()).toString();
                String bestSuited = Objects.requireNonNull(bestSuitedEdt.getText()).toString();
                courseID = courseName;
                // on below line we are passing all data to our modal class.
                CourseRVModal courseRVModal = new CourseRVModal(courseID, courseName, coursePrice, bestSuited);
                // on below line we are calling a add value event
                // to pass data to firebase database.
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // on below line we are setting data in our firebase database.
                        databaseReference.child(courseID).setValue(courseRVModal);
                        // displaying a toast message.
                        Toast.makeText(Add_Course_Activity.this, "Name Added..", Toast.LENGTH_SHORT).show();
                        // starting a main activity.
                        startActivity(new Intent(Add_Course_Activity.this, MainActivity2.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // displaying a failure message on below line.
                        Toast.makeText(Add_Course_Activity.this, "Fail to add Name..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

