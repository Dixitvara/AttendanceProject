package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class GenerateOtp extends AppCompatActivity {

    TextView otpTxt;
    Button generateOtpBtn, confirmBtn;
    ArrayAdapter<CharSequence> adapter1, adapter2;
    int flag;
    Spinner semSpinner, subSpinner;
    String sharedFid, selectedSem, selectedSub;
    SharedPreferences sharedPreferences;
    String Otp;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_otp);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
        sharedFid = sharedPreferences.getString("sharedFid", "");
        db = new DBHelper(this);

        otpTxt = findViewById(R.id.otpText);
        generateOtpBtn = findViewById(R.id.genOtpBtn);
        confirmBtn = findViewById(R.id.confirm);

        semSpinner = findViewById(R.id.spinner1);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.sem, R.layout.spinner_layout);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semSpinner.setAdapter(adapter1);

        // when sem spinner will be selected
        semSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                // get subject spinner
                subSpinner = findViewById(R.id.spinner2);

                // obtain selected semester
                selectedSem = semSpinner.getSelectedItem().toString();

                int parent = adapterView.getId();
                if (parent == R.id.spinner1)
                {
                    switch(selectedSem)
                    {
                        case "Semester": adapter2 = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.no_sem_sel, R.layout.spinner_layout);
                            break;
                        case "1": adapter2 = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.mca_sub_sem_1, R.layout.spinner_layout);
                            break;

                        case "2": adapter2 = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.mca_sub_sem_2, R.layout.spinner_layout);
                            break;

                        case "3": adapter2 = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.mca_sub_sem_3, R.layout.spinner_layout);
                            break;
                        default:
                            break;
                    }
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subSpinner.setAdapter(adapter2);

                    subSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedSub = subSpinner.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        generateOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 1;
                Otp = generateRandomOTP();
                otpTxt.setText(Otp);
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // showing error message if user doesn't select semester
                if(selectedSem.equals("Semester"))
                {
                    Toast.makeText(GenerateOtp.this, "Please select semester!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(flag != 1){
                    Toast.makeText(GenerateOtp.this, "Generate OTP first!", Toast.LENGTH_SHORT).show();
                    return;
                }


                // get current date and time
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm:ss");
                String date = simpleDateFormat.format(calendar.getTime());
                String time = simpleTimeFormat.format(calendar.getTime());

                // check whether the session is already created or not
                Cursor cursor = db.checkSession(sharedFid, selectedSem, selectedSub, date);

                if(cursor.getCount() > 0){
                    Toast.makeText(GenerateOtp.this, "Session is already created for " + selectedSub, Toast.LENGTH_SHORT).show();
                    return;
                }

                // inserting data in to session table
                Boolean checkSession = db.insertSession(sharedFid, Otp, selectedSem, selectedSub, date, time);
                if(checkSession){
                    Toast.makeText(GenerateOtp.this, "Session created for subject " + selectedSub, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), FacultyDashboard.class));
                }
                else{
                    Toast.makeText(GenerateOtp.this, "Failed to create session", Toast.LENGTH_SHORT).show();
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