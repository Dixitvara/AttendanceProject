package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {

    EditText enrollment, password;
    TextView registerLink, otherLogin, forgetPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button signup;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkLogin();

        sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        enrollment = (EditText) findViewById(R.id.enrollment);
        password = (EditText) findViewById(R.id.pass);
        signup = (Button) findViewById(R.id.signup);
        registerLink = (TextView) findViewById(R.id.registerLink);
        otherLogin = findViewById(R.id.otherLogin);
        forgetPassword = findViewById(R.id.forgetPassword);
        DB = new DBHelper(this);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Signup.class));
            }
        });

        otherLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FacultyLogin.class));
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enrollmentNo, pass;
                enrollmentNo = String.valueOf(enrollment.getText());
                pass = String.valueOf(password.getText());

                if (TextUtils.isEmpty(enrollmentNo)) {
                    Toast.makeText(MainActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(MainActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // login code
                Boolean checkEnrollmentPassword = DB.checkEnrollmentPassword(enrollmentNo, pass);
                if(checkEnrollmentPassword)
                {
                    Toast.makeText(MainActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    editor.putString("sharedEnrollment", enrollmentNo);
                    editor.putString("sharedPass", pass);
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkLogin() {
        sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
        if(sharedPreferences.contains("sharedEnrollment") && sharedPreferences.contains("sharedPass")){
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
            finish();
        }
    }

}