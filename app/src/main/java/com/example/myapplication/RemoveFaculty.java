package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RemoveFaculty extends AppCompatActivity {

    EditText facultyId;
    Button remove;
    DBHelper db;
    String fid;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_faculty);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        facultyId = findViewById(R.id.facultyId);
        remove = findViewById(R.id.removeBtn);

        db = new DBHelper(this);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fid = facultyId.getText().toString();
                if (TextUtils.isEmpty(fid)) {
                    Toast.makeText(RemoveFaculty.this, "Enter Faculty ID!", Toast.LENGTH_SHORT).show();
                    return;
                }
                cursor = db.getFacultyInfo(fid);
                if (cursor.getCount() < 1) {
                    Toast.makeText(RemoveFaculty.this, "No data found!", Toast.LENGTH_SHORT).show();
                    return;
                }
                confirmDelete();
            }
        });
    }

    public void confirmDelete() {
        String facultyName;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        while (cursor.moveToNext())
            builder.setMessage("Are you sure you want to delete faculty " + cursor.getString(0) +"?");

        builder.setTitle("Confirm delete");

        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {

            boolean delete = db.removeFaculty(fid);
            if (delete) {
                Toast.makeText(RemoveFaculty.this, "Faculty Deleted!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
            }
        });

        builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}