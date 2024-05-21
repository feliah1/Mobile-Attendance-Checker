package com.example.attendancechecker3;

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

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://awesome1111meow-default-rtdb.asia-southeast1.firebasedatabase.app");
        mDatabase = database.getReference();
    }

    // Method to handle time-in button click
    public void timeInClicked(View view) {
        String courseName = ((EditText) findViewById(R.id.idEmployeeFirstName)).getText().toString(); // Assuming you have an EditText for course name

        // Query to get the courseId based on courseName
        mDatabase.child("Courses").orderByChild("courseName").equalTo(courseName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                        String courseId = courseSnapshot.getKey(); // Get the courseId
                        String inTime = getCurrentTime();

                        AttendanceRVModal attendance = new AttendanceRVModal(courseId, 1, "Present", inTime, "");

                        // Save attendance to Firebase
                        mDatabase.child("attendance").push().setValue(attendance).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                runOnUiThread(() ->
                                        Toast.makeText(MainActivity.this, "You are now timed in", Toast.LENGTH_SHORT).show()
                                );

                                // Navigate to CalendarEmployeeActivity
                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                runOnUiThread(() ->
                                        Toast.makeText(MainActivity.this, "Failed to time in", Toast.LENGTH_SHORT).show()
                                );
                            }
                        });

                        // Break the loop as we found the course
                        break;
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(MainActivity.this, "Name not found", Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    public void startLoginActivity(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void startTimeOut(View view) {
        Intent intent = new Intent(this, MainActivityOut.class);
        startActivity(intent);
    }
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
