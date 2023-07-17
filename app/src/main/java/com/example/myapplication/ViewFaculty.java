package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewFaculty extends AppCompatActivity {

    Cursor cursor;
    DBHelper db;
    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_faculty);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DBHelper(this);
        table = findViewById(R.id.table);

        cursor = db.viewFaculty();

        if (cursor.getCount() != 0) {
            Toast.makeText(ViewFaculty.this, "Data found!", Toast.LENGTH_SHORT).show();
            cursor.moveToFirst();
            do {
                View tableRow = LayoutInflater.from(ViewFaculty.this).inflate(R.layout.faculty_view_table_item, null, false);
                TextView fid = (TextView) tableRow.findViewById(R.id.fid);
                TextView fname = (TextView) tableRow.findViewById(R.id.fname);
                TextView femail = (TextView) tableRow.findViewById(R.id.femail);

                fid.setText(cursor.getString(1));
                fname.setText(cursor.getString(0));
                femail.setText(cursor.getString(2));
                table.addView(tableRow);
            }
            while ((cursor.moveToNext()));
            cursor.close();
        } else {
            Toast.makeText(ViewFaculty.this, "No data found!", Toast.LENGTH_SHORT).show();
        }

    }
}