package com.example.attendancechecker3;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancechecker3.AttendanceRVModal;
import com.example.attendancechecker3.R;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private List<AttendanceRVModal> attendanceList;

    public CalendarAdapter(List<AttendanceRVModal> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceRVModal attendance = attendanceList.get(position);
        CourseRVModal course = attendance.getCourse();

        if (course != null) {
            holder.studentName.setText(course.getCourseName());
            holder.courseBestSuitedFor.setText(course.getBestSuitedFor());
        } else {
            holder.studentName.setText("Loading...");
        }

        holder.studentRegNo.setText(attendance.getInTime());
        holder.radioPresent.setChecked("Present".equals(attendance.getStatus()));
        holder.radioAbsent.setChecked("Absent".equals(attendance.getStatus()));

        // Log the data being set for debugging
        Log.d("CalendarAdapter", "Student Name: " + holder.studentName.getText());
        Log.d("CalendarAdapter", "In Time: " + holder.studentRegNo.getText());
        Log.d("CalendarAdapter", "Status: " + attendance.getStatus());
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView studentName;
        public TextView studentRegNo;
        public RadioButton radioPresent;
        public RadioButton radioAbsent;
        public TextView courseBestSuitedFor;

        public ViewHolder(View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.idTVCOurseName);
            studentRegNo = itemView.findViewById(R.id.student_regNo_adapter);
            radioPresent = itemView.findViewById(R.id.radio_present);
            radioAbsent = itemView.findViewById(R.id.radio_absent);
            courseBestSuitedFor = itemView.findViewById(R.id.course_bestSuitedFor);
        }
    }
}
