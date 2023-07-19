package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewStudentAttendance extends AppCompatActivity {

    ImageButton searchBtn;
    EditText searchET;
    DBHelper db;
    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_attendance);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchBtn = findViewById(R.id.searchBtn);
        searchET = findViewById(R.id.searchText);
        table = findViewById(R.id.table);

        View tableRow = LayoutInflater.from(ViewStudentAttendance.this).inflate(R.layout.student_attendance_table_view, null, false);
        TextView sr = tableRow.findViewById(R.id.srNo);
        TextView sname = tableRow.findViewById(R.id.sname);
        TextView enrollment = tableRow.findViewById(R.id.enrollment);
        TextView sem = tableRow.findViewById(R.id.semester);
        TextView sub = tableRow.findViewById(R.id.subject);
        TextView date = tableRow.findViewById(R.id.date);
        TextView time = tableRow.findViewById(R.id.time);

        db = new DBHelper(this);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = searchET.getText().toString();

                if (searchText.isEmpty()) {
                    Toast.makeText(ViewStudentAttendance.this, "Please enter enrollment or student name", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = db.getAllData(searchText);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        sr.setText(cursor.getString(0));
                        sname.setText(cursor.getString(1));
                        enrollment.setText(cursor.getString(2));
                        sem.setText(cursor.getString(3));
                        sub.setText(cursor.getString(4));
                        date.setText(cursor.getString(5));
                        time.setText(cursor.getString(6));
                        table.addView(tableRow);
                    }
                    while ((cursor.moveToNext()));
                    cursor.close();
                } else {
                    Toast.makeText(ViewStudentAttendance.this, "No data found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}