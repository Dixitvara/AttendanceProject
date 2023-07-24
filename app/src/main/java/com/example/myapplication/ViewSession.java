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

public class ViewSession extends AppCompatActivity {

    ImageButton pickDateBtn;
    int year, month, day;
    int flag;
    DBHelper db;
    TextView dateTxt;
    String date, finalDate;
    TableLayout table;
    Cursor cursor;
    Button getDataBtn;
    String sharedFid;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_session);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getDataBtn = findViewById(R.id.getDataBtn);
        table = findViewById(R.id.table);
        pickDateBtn = findViewById(R.id.pickDateImgBtn);
        dateTxt = findViewById(R.id.dateTxt);

        sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
        sharedFid = sharedPreferences.getString("sharedFid", "");

        db = new DBHelper(this);

        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewSession.this, new DatePickerDialog.OnDateSetListener() {
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

        getDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag != 1) {
                    Toast.makeText(ViewSession.this, "Please select date!", Toast.LENGTH_SHORT).show();
                    return;
                }

                cursor = db.getSessionInfo(sharedFid,finalDate);

                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        View tableRow = LayoutInflater.from(ViewSession.this).inflate(R.layout.session_view, null, false);
                        TextView sr = tableRow.findViewById(R.id.sr);
                        TextView otp = tableRow.findViewById(R.id.otp);
                        TextView sem = tableRow.findViewById(R.id.sem);
                        TextView sub = tableRow.findViewById(R.id.subject);
                        TextView time = tableRow.findViewById(R.id.time);

                        sr.setText(cursor.getString(0));
                        otp.setText(cursor.getString(2));
                        sem.setText(cursor.getString(3));
                        sub.setText(cursor.getString(4));
                        time.setText(cursor.getString(6));
                        table.addView(tableRow);

                        // update password and generate otp for student
                    }

                    cursor.close();
                } else {
                    Toast.makeText(ViewSession.this, "No data found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}