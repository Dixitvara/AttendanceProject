package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddFaculty extends AppCompatActivity {

    EditText facultyEmailET, facultyNameET, facultyId;
    Button addBtn;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        facultyNameET = findViewById(R.id.facultyNameET);
        facultyEmailET = findViewById(R.id.facultyEmailET);
        facultyId = findViewById(R.id.facultyId);
        addBtn = findViewById(R.id.addBtn);

        db = new DBHelper(this);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String facultyName = facultyNameET.getText().toString();
                String facultyEmail = facultyEmailET.getText().toString();
                String facultyID = facultyId.getText().toString();

                if(TextUtils.isEmpty(facultyName))
                {
                    Toast.makeText(AddFaculty.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(facultyID))
                {
                    Toast.makeText(AddFaculty.this, "Enter Faculty Id", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isValidEmail(facultyEmail))
                {
                    Toast.makeText(AddFaculty.this, "Not valid  email", Toast.LENGTH_SHORT).show();
                    return;
                }

                Boolean addFaculty = db.addFaculty( facultyName, facultyID, facultyEmail, facultyName + "123");
                if(addFaculty)
                {
                    Toast.makeText(AddFaculty.this, "Faculty inserted!", Toast.LENGTH_SHORT).show();
                    facultyNameET.setText("");
                    facultyEmailET.setText("");
                    facultyId.setText("");
                }
                else
                {
                    Toast.makeText(AddFaculty.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}