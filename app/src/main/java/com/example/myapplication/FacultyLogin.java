package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FacultyLogin extends AppCompatActivity {

    EditText facultyId, password;
    TextView adminLogin;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button signin;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkLogin();

        DB = new DBHelper(this);

        sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        facultyId = findViewById(R.id.fid);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signup);
        adminLogin = findViewById(R.id.adminLogin);

        adminLogin.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AdminLogin.class)));

        signin.setOnClickListener(v -> {
            String  fid,pass;
            fid = String.valueOf(facultyId.getText());
            pass= String.valueOf(password.getText());

            if(TextUtils.isEmpty(fid))
            {
                Toast.makeText(FacultyLogin.this, "Enter faculty id", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(pass))
            {
                Toast.makeText(FacultyLogin.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            // login code
            Boolean checkFidPassword = DB.checkFidPassword(fid, pass);
            if(checkFidPassword)
            {
                Toast.makeText(FacultyLogin.this, "Login successfully", Toast.LENGTH_SHORT).show();
                editor.putString("sharedFid", fid);
                editor.putString("sharedPass", pass);
                editor.apply();
                startActivity(new Intent(getApplicationContext(), FacultyDashboard.class));
            }
            else
            {
                Toast.makeText(FacultyLogin.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void checkLogin() {
        sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
        String fid = sharedPreferences.getString("sharedFid","");
        if(sharedPreferences.contains("sharedFid") && sharedPreferences.contains("sharedPass")){
            startActivity(new Intent(getApplicationContext(),FacultyDashboard.class));
            finish();
        }
    }
}