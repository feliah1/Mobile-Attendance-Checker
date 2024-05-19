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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditCourseActivity extends AppCompatActivity {

    private TextInputEditText courseNameEdt, coursePriceEdt, bestSuitedEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    CourseRVModal courseRVModal;
    private ProgressBar loadingPB;
    private String courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        // initializing all our variables on below line.
        Button addCourseBtn = findViewById(R.id.idBtnAddCourse);
        courseNameEdt = findViewById(R.id.idEdtCourseName);
        coursePriceEdt = findViewById(R.id.idEdtCoursePrice);
        bestSuitedEdt = findViewById(R.id.idEdtSuitedFor);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance("https://awesome1111meow-default-rtdb.asia-southeast1.firebasedatabase.app");

        // getting our modal class on which we have passed.
        courseRVModal = getIntent().getParcelableExtra("course");
        Button deleteCourseBtn = findViewById(R.id.idBtnDeleteCourse);

        if (courseRVModal != null) {
            // setting data to our edit text from our modal class.
            courseNameEdt.setText(courseRVModal.getCourseName());
            coursePriceEdt.setText(courseRVModal.getCoursePrice());
            bestSuitedEdt.setText(courseRVModal.getBestSuitedFor());
            courseID = courseRVModal.getCourseId();
        }

        // initializing our database reference and adding a child as our course id.
        databaseReference = firebaseDatabase.getReference("Courses").child(courseID);

        // adding click listener for our add course button.
        addCourseBtn.setOnClickListener(v -> {
            if (validateInputs()) {
                updateCourse();
            }
        });

        // adding click listener for our delete course button.
        deleteCourseBtn.setOnClickListener(v -> deleteCourse());
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

    private void updateCourse() {
        loadingPB.setVisibility(View.VISIBLE);

        // getting data from our edit text.
        String courseName = Objects.requireNonNull(courseNameEdt.getText()).toString();
        String coursePrice = Objects.requireNonNull(coursePriceEdt.getText()).toString();
        String bestSuited = Objects.requireNonNull(bestSuitedEdt.getText()).toString();

        // creating a map for passing data using key and value pair.
        Map<String, Object> map = new HashMap<>();
        map.put("courseName", courseName);
        map.put("coursePrice", coursePrice);
        map.put("bestSuitedFor", bestSuited);
        map.put("courseId", courseID);

        // updating the course in the database.
        databaseReference.updateChildren(map).addOnCompleteListener(task -> {
            loadingPB.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                Toast.makeText(EditCourseActivity.this, "Course Updated..", Toast.LENGTH_SHORT).show();
                // opening a new activity after updating our course.
                startActivity(new Intent(EditCourseActivity.this, MainActivity2.class));
                finish();
            } else {
                Toast.makeText(EditCourseActivity.this, "Failed to update course..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteCourse() {
        // deleting the course.
        databaseReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // displaying a toast message.
                Toast.makeText(this, "Course Deleted..", Toast.LENGTH_SHORT).show();
                // opening main activity.
                startActivity(new Intent(EditCourseActivity.this, MainActivity2.class));
                finish();
            } else {
                Toast.makeText(this, "Failed to delete course..", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
