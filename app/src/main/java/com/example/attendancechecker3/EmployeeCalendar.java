package com.example.attendancechecker3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView; // Import this instead of android.widget.SearchView
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
    private List<AttendanceRVModal> filteredList = new ArrayList<>();
    private DatabaseReference mDatabase;
    private DatabaseReference coursesRef;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_calendar);

        recyclerView = findViewById(R.id.idCalendar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CalendarAdapter(filteredList);
        recyclerView.setAdapter(adapter);

        searchView = findViewById(R.id.searchView); // Correct casting
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://awesome1111meow-default-rtdb.asia-southeast1.firebasedatabase.app");
        mDatabase = database.getReference().child("attendance");
        coursesRef = database.getReference().child("Courses");

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
                    } else {
                        Log.e("EmployeeCalendar", "Attendance data is null");
                    }
                }
                fetchCourseData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("EmployeeCalendar", "Failed to read data", databaseError.toException());
            }
        });
    }

    private void fetchCourseData() {
        coursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (AttendanceRVModal attendance : attendanceList) {
                    String courseId = attendance.getCourseId();
                    CourseRVModal course = dataSnapshot.child(courseId).getValue(CourseRVModal.class);
                    if (course != null) {
                        attendance.setCourse(course);
                    } else {
                        Log.e("EmployeeCalendar", "Course not found for courseId: " + courseId);
                    }
                }
                filteredList.clear();
                filteredList.addAll(attendanceList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("EmployeeCalendar", "Failed to read courses", databaseError.toException());
            }
        });
    }

    private void filter(String text) {
        filteredList.clear();
        for (AttendanceRVModal item : attendanceList) {
            if (item.getCourseId().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
