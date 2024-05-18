package com.example.attendancechecker3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private List<AttendanceRVModal> attendanceList;

    public CalendarAdapter(List<AttendanceRVModal> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceRVModal attendance = attendanceList.get(position);
//        holder.courseName.setText(attendance.getCourse().getCourseName());
//        holder.suitedFor.setText(attendance.getCourse().getBestSuitedFor());
        holder.amountOfDaysPresent.setText(String.valueOf(attendance.getAmountOfDaysPresent()));
        holder.status.setText(attendance.getStatus());
        holder.inTime.setText(attendance.getInTime());
        holder.outTime.setText(attendance.getOutTime());
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseName;
        TextView suitedFor;
        TextView amountOfDaysPresent;
        TextView status;
        TextView inTime;
        TextView outTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.idTVCOurseName);
            suitedFor = itemView.findViewById(R.id.idTVSuitedFor);
            amountOfDaysPresent = itemView.findViewById(R.id.idAmountofDaysPresent);
            status = itemView.findViewById(R.id.idStatus);
            inTime = itemView.findViewById(R.id.idInTime);
            outTime = itemView.findViewById(R.id.idOutTime);
        }
    }
}
