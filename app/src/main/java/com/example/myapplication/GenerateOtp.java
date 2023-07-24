package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GenerateOtp extends AppCompatActivity {

    TextView otpTxt;
    Button generateOtpBtn, confirmBtn;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_otp);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        otpTxt = findViewById(R.id.otpText);
        generateOtpBtn = findViewById(R.id.genOtpBtn);
        confirmBtn = findViewById(R.id.confirm);

        generateOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 1;
                String Otp = generateRandomOTP();
                otpTxt.setText(Otp);
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag != 1){
                    Toast.makeText(GenerateOtp.this, "Generate OTP first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                
            }
        });

    }

    public String generateRandomOTP() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}