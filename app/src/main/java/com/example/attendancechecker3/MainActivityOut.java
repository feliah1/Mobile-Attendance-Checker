package com.example.attendancechecker3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivityOut extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_timeout);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://awesome1111meow-default-rtdb.asia-southeast1.firebasedatabase.app");
        mDatabase = database.getReference();
    }

    public void startLoginActivity(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void startTimeIn(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void timeOutClicked(View view) {
        String courseName = ((EditText) findViewById(R.id.idEmployeeFirstName)).getText().toString(); // Assuming you have an EditText for course name

        // Query to get the courseId based on courseName
        mDatabase.child("Courses").orderByChild("courseName").equalTo(courseName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                        String courseId = courseSnapshot.getKey(); // Get the courseId
                        String outTime = getCurrentTime();

                        // Check if there's an existing attendance record for the same courseId
                        mDatabase.child("attendance").orderByChild("courseId").equalTo(courseId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot attendanceSnapshot) {
                                if (attendanceSnapshot.exists()) {
                                    // Update existing record with time out
                                    for (DataSnapshot attendanceRecord : attendanceSnapshot.getChildren()) {
                                        attendanceRecord.getRef().child("outTime").setValue(outTime)
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        runOnUiThread(() ->
                                                                Toast.makeText(MainActivityOut.this, "You are now timed out", Toast.LENGTH_SHORT).show()
                                                        );
                                                    } else {
                                                        runOnUiThread(() ->
                                                                Toast.makeText(MainActivityOut.this, "Failed to update timeout", Toast.LENGTH_SHORT).show()
                                                        );
                                                    }
                                                });
                                    }
                                } else {
                                    // Create new attendance record with time out
                                    AttendanceRVModal attendance = new AttendanceRVModal(courseId, 1, "Out", "", outTime);
                                    mDatabase.child("attendance").push().setValue(attendance)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    runOnUiThread(() ->
                                                            Toast.makeText(MainActivityOut.this, "You are now timed out", Toast.LENGTH_SHORT).show()
                                                    );
                                                } else {
                                                    runOnUiThread(() ->
                                                            Toast.makeText(MainActivityOut.this, "Failed to create attendance record", Toast.LENGTH_SHORT).show()
                                                    );
                                                }
                                            });
                                }

                                // Navigate to MainActivityOut
                                Intent intent = new Intent(MainActivityOut.this, MainActivityOut.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                runOnUiThread(() ->
                                        Toast.makeText(MainActivityOut.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show()
                                );
                            }
                        });

                        // Break the loop as we found the course
                        break;
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(MainActivityOut.this, "Name not found", Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivityOut.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        });
    }


    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
