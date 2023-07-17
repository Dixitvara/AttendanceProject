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
import android.widget.Toast;

public class AdminDashboard extends AppCompatActivity {

    TextView adminName;
    CardView logout, addFaculty, removeFaculty, updatePassword, viewFaculty;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String sharedEmail;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adminName = findViewById(R.id.adminName);

        addFaculty = findViewById(R.id.cardviewAddFaculty);
        removeFaculty = findViewById(R.id.cardviewRemoveFaculty);
        viewFaculty = findViewById(R.id.cardviewViewFaculty);
        updatePassword = findViewById(R.id.cardviewUpdatePassword);
        logout = findViewById(R.id.cardviewLogout);

        sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sharedEmail = sharedPreferences.getString("sharedEmail", "");

        DBHelper db = new DBHelper(this);
        cursor = db.getAdminInfo(sharedEmail);

        if (cursor.getCount() != 0)
        {
            while (cursor.moveToNext())
            {
                adminName.setText(cursor.getString(0));
            }
        }

        addFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddFaculty.class));
            }
        });

        removeFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RemoveFaculty.class));
            }
        });

        viewFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewFaculty.class));
            }
        });

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