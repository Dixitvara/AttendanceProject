package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ViewStudentAttendance extends AppCompatActivity {

    ImageButton searchBtn, pickDateBtn;
    EditText searchET;
    int year, month, day;
    int flag;
    DBHelper db;
    TextView dateTxt;
    String date, finalDate;
    TableLayout table;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_attendance);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchBtn = findViewById(R.id.searchBtn);
        searchET = findViewById(R.id.searchText);
        table = findViewById(R.id.table);
        pickDateBtn = findViewById(R.id.pickDateImgBtn);
        dateTxt = findViewById(R.id.dateTxt);

        db = new DBHelper(this);

        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewStudentAttendance.this, new DatePickerDialog.OnDateSetListener() {
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

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = searchET.getText().toString();

                if (searchText.isEmpty()) {
                    Toast.makeText(ViewStudentAttendance.this, "Please enter enrollment or student name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (flag != 1) {
                    Toast.makeText(ViewStudentAttendance.this, "Please select date!", Toast.LENGTH_SHORT).show();
                    return;
                }

                cursor = db.getAttendanceData(searchText, finalDate);

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (cursor.moveToNext()) {
                        View tableRow = LayoutInflater.from(ViewStudentAttendance.this).inflate(R.layout.student_attendance_table_view, null, false);
                        TextView sname = tableRow.findViewById(R.id.sname);
                        TextView enrollment = tableRow.findViewById(R.id.enrollment);
                        TextView sem = tableRow.findViewById(R.id.semester);
                        TextView sub = tableRow.findViewById(R.id.subject);
                        TextView date = tableRow.findViewById(R.id.date);
                        TextView time = tableRow.findViewById(R.id.time);

                        sname.setText(cursor.getString(1));
                        enrollment.setText(cursor.getString(2));
                        sem.setText(cursor.getString(3));
                        sub.setText(cursor.getString(4));
                        date.setText(cursor.getString(5));
                        time.setText(cursor.getString(6));
                        table.addView(tableRow);

                        // date filtering , update password and generate otp for student
                    }

                    cursor.close();
                } else {
                    Toast.makeText(ViewStudentAttendance.this, "No data found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}