package com.example.attendancechecker3;

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
        holder.studentName.setText(course != null ? course.getCourseName() : "Loading...");
        holder.studentRegNo.setText(attendance.getInTime());
        holder.radioPresent.setChecked("Present".equals(attendance.getStatus()));
        holder.radioAbsent.setChecked("Absent".equals(attendance.getStatus()));

        if (course != null) {
            holder.courseBestSuitedFor.setText(course.getBestSuitedFor());
            // Remove the following line
            // holder.coursePrice.setText(course.getPrice());
        }
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
            // Remove the following line
            // coursePrice = itemView.findViewById(R.id.course_price);
        }
    }
}
