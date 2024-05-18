package com.example.attendancechecker3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class EmployeeCalendar extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CalendarAdapter calendarAdapter;
    private List<AttendanceRVModal> attendanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_calendar);

        recyclerView = findViewById(R.id.idCalendar);
        attendanceList = new ArrayList<>();

        // Populate attendanceList with data here
        // attendanceList.add(new AttendanceRVModal(...));

        calendarAdapter = new CalendarAdapter(attendanceList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(calendarAdapter);
    }
}
