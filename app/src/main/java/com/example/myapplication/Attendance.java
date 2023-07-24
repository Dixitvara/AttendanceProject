package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Attendance extends AppCompatActivity {

    Spinner semSpinner, subSpinner;
    ArrayAdapter<CharSequence> adapter1, adapter2;
    String selectedSem;
    String selectedSub, OTP;
    TextView semError;
    EditText OTPET;

    // Database variables
    Button Attendance;

    // Location variables
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView address;
    Button getLocation;
    int flag, flag2;
    SharedPreferences sharedPreferences;

    // store current location coordinates in variables
    double lati, longi;

    private final static int REQUEST_CODE = 100;

    // Database name retrieval variables
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //location variables
        address = findViewById(R.id.location);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // other variables
        Attendance= findViewById(R.id.attendance);
        getLocation = findViewById(R.id.getLocation);
        OTPET = findViewById(R.id.Otp);

        // first spinner
        semSpinner = findViewById(R.id.spinner1);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.sem, R.layout.spinner_layout);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semSpinner.setAdapter(adapter1);
        semError = findViewById(R.id.errorSem);

        // shared preferences
        sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
        String enrollmentNo = sharedPreferences.getString("sharedEnrollment", "");

        // database
        DBHelper db = new DBHelper(this);
        SQLiteDatabase sdb = db.getWritableDatabase();

        // getLocation button code to get the user's current location
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
                flag2 = 1;
            }
        });

        // when sem spinner will be selected
        semSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                // get subject spinner
                subSpinner = (Spinner) findViewById(R.id.spinner2);

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

        Attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // showing error message if user doesn't select semester
                if(selectedSem.equals("Semester"))
                {
                    Toast.makeText(Attendance.this, "Please select semester!", Toast.LENGTH_SHORT).show();
                    semError.setError("Required!");
                    semError.requestFocus();
                    return;
                }
                else{
                    semError.setError(null);
                }

                String otp = OTPET.getText().toString();
                if(otp.isEmpty())
                {
                    Toast.makeText(Attendance.this, "Enter OTP!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(flag2 != 1)
                {
                    Toast.makeText(Attendance.this, "Get location first!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // get current date and time
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm:ss");
                String date = simpleDateFormat.format(calendar.getTime());
                String time = simpleTimeFormat.format(calendar.getTime());

                String StudentName = "";

                // function checks whether the user is inside college campus or not
                checkLocation();

                Cursor cursor = db.getStudentInfo(enrollmentNo);

                if (cursor.getCount() != 0)
                {
                    while(cursor.moveToNext())
                    {
                        StudentName = cursor.getString(0);
                    }
                }

                if(flag != 1)
                {
                    Cursor createdSession = db.checkSessionStud(selectedSem, selectedSub, date);

                    if(createdSession.getCount() != 1){
                        Toast.makeText(Attendance.this, "Session is not created by subject teacher!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    while(createdSession.moveToNext())
                        OTP = createdSession.getString(2);

                    if(OTP.equals(otp)) {
                        Cursor checkAttendance = db.getAttendanceData(enrollmentNo, selectedSub, date);

                        if (checkAttendance.getCount() > 0) {
                            Toast.makeText(Attendance.this, "Already marked for " + selectedSub, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Boolean insertData = db.insertAttendanceData(StudentName, enrollmentNo, selectedSem, selectedSub, date, time);
                        if (insertData) {
                            Toast.makeText(Attendance.this, "Attendance marked!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Attendance.this, "Failed to insert data", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(Attendance.this, "Wrong OTP!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
    }

    private void checkLocation()
    {
        if(lati >= 20.93991829759028 && lati <= 20.94088524927326 && longi >= 72.94985377035985 && longi <= 72.95135044292365)
        {
            Toast.makeText(Attendance.this, "You are in college campus", Toast.LENGTH_SHORT).show();
        }
        else
        {
            flag = 1;
            Toast.makeText(Attendance.this, "sorry! you are not in campus! change location and try again!", Toast.LENGTH_SHORT).show();
        }
    }


    // get location method
    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(Attendance.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            lati = addresses.get(0).getLatitude();
                            longi = addresses.get(0).getLongitude();
                            address.setText("Current location: " + addresses.get(0).getAddressLine(0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        else
        {
            askPermission();
        }
    }

    // ask permission method for checking the permission
    void askPermission()
    {
        ActivityCompat.requestPermissions(Attendance.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {

        if (requestCode == REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getLastLocation();
            }
            else
            {
                Toast.makeText(Attendance.this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}