package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AdminLogin extends AppCompatActivity {

    EditText emailId, pass;
    TextView forgetPassword;
    Button signup;
    DBHelper DB;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emailId = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        signup = (Button) findViewById(R.id.signup);
        forgetPassword = findViewById(R.id.forgetPassword);

        sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        DB = new DBHelper(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  emailid,lpass;
                emailid = String.valueOf(emailId.getText());
                lpass= String.valueOf(pass.getText());

                if(!isValidEmail(emailid))
                {
                    Toast.makeText(AdminLogin.this, "Not valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(lpass))
                {
                    Toast.makeText(AdminLogin.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // login code
                Boolean checkAdmin = DB.checkAdmin(emailid, lpass);
                if(checkAdmin)
                {
                    Toast.makeText(AdminLogin.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    editor.putString("sharedEmail", emailid);
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
                }
                else
                {
                    Toast.makeText(AdminLogin.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}