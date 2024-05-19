package com.example.attendancechecker3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmployeeCalendar extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CalendarAdapter adapter;
    private List<AttendanceRVModal> attendanceList = new ArrayList<>();
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_calendar);

        recyclerView = findViewById(R.id.idCalendar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CalendarAdapter(attendanceList);
        recyclerView.setAdapter(adapter);

// Initialize Firebase with the correct URL
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://awesome1111meow-default-rtdb.asia-southeast1.firebasedatabase.app");
        mDatabase = database.getReference().child("attendance");

        // Load attendance data from Firebase
        loadAttendanceData();
    }

    private void loadAttendanceData() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                attendanceList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AttendanceRVModal attendance = postSnapshot.getValue(AttendanceRVModal.class);
                    if (attendance != null) {
                        attendanceList.add(attendance);
                    }
                }
                // Fetch course data
                fetchCourseData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("EmployeeCalendar", "Failed to read data", databaseError.toException());
            }
        });
    }

    private void fetchCourseData() {
        DatabaseReference coursesRef = FirebaseDatabase.getInstance("https://awesome1111meow-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("courses");
        coursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (AttendanceRVModal attendance : attendanceList) {
                    String courseId = attendance.getCourseId();
                    CourseRVModal course = dataSnapshot.child(courseId).getValue(CourseRVModal.class);
                    if (course != null) {
                        attendance.setCourse(course); // Ensure setCourse method is present in AttendanceRVModal
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("EmployeeCalendar", "Failed to read courses", databaseError.toException());
            }
        });
    }

}
