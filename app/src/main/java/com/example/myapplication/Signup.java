package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Signup extends AppCompatActivity {

    EditText fullName, enrollmentNo, email, password;
    Button sighupButton;
    TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DBHelper db = new DBHelper(this);
        SQLiteDatabase sdb = db.getWritableDatabase();


        fullName = (EditText) findViewById(R.id.FullName);
        enrollmentNo = (EditText) findViewById(R.id.En);
        email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.password);
        sighupButton = (Button) findViewById(R.id.Login);
        loginLink=(TextView) findViewById(R.id.loginLink);

        sighupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = fullName.getText().toString();
                String enrollment = enrollmentNo.getText().toString();
                String semail = email.getText().toString();
                String passwd = password.getText().toString();

                if(TextUtils.isEmpty(name))
                {
                    Toast.makeText(Signup.this, "Please enter fullname", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(enrollment))
                {
                    Toast.makeText(Signup.this, "Please enter Enrollment no", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isValidEmail(semail))
                {
                    Toast.makeText(Signup.this, "Not valid  email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(passwd))
                {
                    Toast.makeText(Signup.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(passwd.length() < 8)
                {
                    Toast.makeText(Signup.this, "Password must be at least 8 length", Toast.LENGTH_SHORT).show();
                    return;
                }

                Boolean inserted = db.insertData(name, enrollment, semail, passwd);

                if(inserted)
                {
                    Toast.makeText(Signup.this, "Signup successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else
                {
                    Toast.makeText(Signup.this, "Failed! try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }
    public static boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}