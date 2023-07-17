package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ViewStudentAttendance extends AppCompatActivity {

    ImageButton searchBtn;
    EditText searchET;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_attendance);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchBtn = findViewById(R.id.searchBtn);
        searchET = findViewById(R.id.searchText);

        db = new DBHelper(this);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = searchET.getText().toString();

                Cursor cursor = db.getAllData(searchText);
                if(cursor.getCount() > 0)
                {
                    while (cursor.moveToNext())
                    {
                        Toast.makeText(ViewStudentAttendance.this, cursor.getString(1), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}