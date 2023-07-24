package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FacultyDashboard extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String sharedFid;
    Cursor cursor;
    TextView facultyName;
    CardView logout, viewAttendance, updatepassword, generateOTP, viewSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sharedFid = sharedPreferences.getString("sharedFid", "");

        facultyName = findViewById(R.id.facultyName);
        logout = findViewById(R.id.cardviewLogout);
        viewAttendance = findViewById(R.id.cardviewViewAttendance);
        updatepassword = findViewById(R.id.cardviewUpdatePassword);
        generateOTP = findViewById(R.id.cardviewGAO);
        viewSession = findViewById(R.id.cardviewViewSession);

        DBHelper db = new DBHelper(this);

        // get faculty name in dashboard
        cursor = db.getFacultyName(sharedFid);

        if (cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {
                facultyName.setText(cursor.getString(0));
            }
        }

        viewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewStudentAttendance.class));
            }
        });

        generateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GenerateOtp.class));
            }
        });

        viewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewSession.class));
            }
        });

        updatepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UpdatePassword.class));
            }
        });

        // Logout card
        logout.setOnClickListener(view -> {
            editor.clear();
            editor.commit();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

    }
}