package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateProfile extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String enrollment, sname, email, password;
    Cursor cursor;
    DBHelper db;
    EditText fullnameET, enrollET, emailET, passwordET;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
        enrollment = sharedPreferences.getString("sharedEnrollment", "");
        db = new DBHelper(this);

        // edit text implementation
        fullnameET = findViewById(R.id.FullName);
        enrollET = findViewById(R.id.En);
        emailET = findViewById(R.id.Email);
        passwordET = findViewById(R.id.password);

        // btn
        update = findViewById(R.id.update);

        cursor = db.getStudentInfo(enrollment);

        if (cursor.getCount() == 1) {
            while (cursor.moveToNext()) {
                fullnameET.setText(cursor.getString(0));
                enrollET.setText(cursor.getString(1));
                emailET.setText(cursor.getString(2));
                passwordET.setText(cursor.getString(3));

                sname = cursor.getString(0);
                email = cursor.getString(2);
                password = cursor.getString(3);
            }
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname = fullnameET.getText().toString();
                String emailEText = emailET.getText().toString();
                String passwordEText = passwordET.getText().toString();

                if (fullname.isEmpty()) {
                    Toast.makeText(UpdateProfile.this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(emailEText.isEmpty())
                {
                    Toast.makeText(UpdateProfile.this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(passwordEText.isEmpty())
                {
                    Toast.makeText(UpdateProfile.this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwordEText.length() <= 7) {
                    Toast.makeText(UpdateProfile.this, "Password must have at least 8 length", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sname.equals(fullname) && email.equals(emailEText) && password.equals(passwordEText)) {
                    Toast.makeText(UpdateProfile.this, "No changes found!", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean updateDetails = db.updateStudDetails(enrollment, fullname, emailEText, passwordEText);

                    if (updateDetails) {
                        Toast.makeText(UpdateProfile.this, "Details updated!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    } else {
                        Toast.makeText(UpdateProfile.this, "Failed to update!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}