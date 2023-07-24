package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Dashboard extends AppCompatActivity {

    TextView profileName;
    CardView logout, markAttendance, updateProfile, viewAttendance, aboutUs;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // shared preferences variables
        sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        profileName = findViewById(R.id.name);

        String enrollment = sharedPreferences.getString("sharedEnrollment", "");

        db = new DBHelper(this);
        Cursor cursor = db.getStudentInfo(enrollment);
        if(cursor.getCount() == 1 ){
            while (cursor.moveToNext())
                profileName.setText(cursor.getString(0));
        }

        logout = findViewById(R.id.cardviewlogout);
        markAttendance = findViewById(R.id.cardviewMarkAttendance);
        updateProfile = findViewById(R.id.cardviewUpdateprofile);
        viewAttendance = findViewById(R.id.cardviewViewAttendance);
        aboutUs = findViewById(R.id.cardviewAboutUs);

        markAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Attendance.class));
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UpdateProfile.class));
            }
        });

        viewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewAttendance.class));
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AboutUs.class));
            }
        });

        // logout button for logout user from appy
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }
}