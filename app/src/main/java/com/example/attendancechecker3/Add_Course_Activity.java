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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        Button addCourseBtn = findViewById(R.id.idBtnAddCourse);
        courseNameEdt = findViewById(R.id.idEdtCourseName);
        coursePriceEdt = findViewById(R.id.idEdtCoursePrice);
        bestSuitedEdt = findViewById(R.id.idEdtSuitedFor);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance("https://awesome1111meow-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference("Courses");

        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    addCourse();
                }
            }
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (Objects.requireNonNull(courseNameEdt.getText()).toString().trim().isEmpty()) {
            courseNameEdt.setError("Course name is required");
            isValid = false;
        }

        if (Objects.requireNonNull(coursePriceEdt.getText()).toString().trim().isEmpty()) {
            coursePriceEdt.setError("Price is required");
            isValid = false;
        }

        if (Objects.requireNonNull(bestSuitedEdt.getText()).toString().trim().isEmpty()) {
            bestSuitedEdt.setError("This field is required");
            isValid = false;
        }

        return isValid;
    }

    private void addCourse() {
        loadingPB.setVisibility(View.VISIBLE);

        // getting data from our edit text.
        String courseName = Objects.requireNonNull(courseNameEdt.getText()).toString();
        String coursePrice = Objects.requireNonNull(coursePriceEdt.getText()).toString();
        String bestSuited = Objects.requireNonNull(bestSuitedEdt.getText()).toString();
        courseID = courseName;

        // creating a course object with the entered details
        CourseRVModal courseRVModal = new CourseRVModal(courseID, courseName, coursePrice, bestSuited);

        // adding the course to the database
        databaseReference.child(courseID).setValue(courseRVModal).addOnCompleteListener(task -> {
            loadingPB.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                Toast.makeText(Add_Course_Activity.this, "Course Added Successfully", Toast.LENGTH_SHORT).show();
                // starting a main activity.
                startActivity(new Intent(Add_Course_Activity.this, MainActivity2.class));
                finish();
            } else {
                Toast.makeText(Add_Course_Activity.this, "Failed to add Course", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
