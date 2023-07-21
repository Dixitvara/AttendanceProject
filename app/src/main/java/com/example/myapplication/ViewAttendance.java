package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ViewAttendance extends AppCompatActivity {

    TextView dateTxt;
    ImageButton pickDateBtn;
    Button getData;
    int year, month, day;
    String date, finalDate;
    int flag;
    TableLayout table;
    SharedPreferences sharedPreferences;
    DBHelper db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pickDateBtn = findViewById(R.id.pickDateImgBtn);
        getData = findViewById(R.id.getDataBtn);
        dateTxt = findViewById(R.id.dateTxt);
        table = findViewById(R.id.table);
        db = new DBHelper(this);

        sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
        String enrollmentNo = sharedPreferences.getString("sharedEnrollment", "");

        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewAttendance.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        date = i2 + "/" + (i1 + 1) + "/" + i;
                        try {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Date parsedDate = simpleDateFormat.parse(date);
                            SimpleDateFormat simpleDateFormatFinal = new SimpleDateFormat("dd/MM/yyyy");
                            finalDate = simpleDateFormatFinal.format(parsedDate);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        dateTxt.setText(finalDate);
                        flag = 1;
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag != 1) {
                    Toast.makeText(ViewAttendance.this, "Please select date!", Toast.LENGTH_SHORT).show();
                    return;
                }

                cursor = db.getStudentAttendance(enrollmentNo, finalDate);

                if (cursor.getCount() != 0) {
                    Toast.makeText(ViewAttendance.this, "Data found!", Toast.LENGTH_SHORT).show();
                    cursor.moveToFirst();
                    do {
                        View tableRow = LayoutInflater.from(ViewAttendance.this).inflate(R.layout.table_item, null, false);
                        TextView sr = tableRow.findViewById(R.id.sr);
                        TextView sem = tableRow.findViewById(R.id.semester);
                        TextView sub = tableRow.findViewById(R.id.subject);
                        TextView time = tableRow.findViewById(R.id.time);

                        sr.setText(cursor.getString(0));
                        sem.setText(cursor.getString(3));
                        sub.setText(cursor.getString(4));
                        time.setText(cursor.getString(6));
                        table.addView(tableRow);
                    }
                    while ((cursor.moveToNext()));
                    cursor.close();
                } else {
                    Toast.makeText(ViewAttendance.this, "No data found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}