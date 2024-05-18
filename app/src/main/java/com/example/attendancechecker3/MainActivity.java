package com.example.attendancechecker3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startTimeOut(View view) {
        Intent intent = new Intent(this, MainActivityOut.class);
        startActivity(intent);
    }

    public void timeInClicked(View view) {
        // Handle the time out logic here
        Toast.makeText(this, "Time In Clicked", Toast.LENGTH_SHORT).show();
        // You can add logic to record the time out
    }
}