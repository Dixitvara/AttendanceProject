package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdatePassword extends AppCompatActivity {

    EditText cpassET, newpassET, confirmpassET;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String sharedFid, dbPass;
    Button updateBtn;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sharedFid = sharedPreferences.getString("sharedFid", "");

        cpassET = findViewById(R.id.cpassword);
        newpassET = findViewById(R.id.password);
        confirmpassET = findViewById(R.id.confirmpassword);
        updateBtn = findViewById(R.id.update);

        db = new DBHelper(this);

        Cursor cursor = db.getFacultyInfo(sharedFid);

        if(cursor.getCount() == 1)
            while(cursor.moveToNext())
                dbPass = cursor.getString(3);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentpass = cpassET.getText().toString();
                String newpass = newpassET.getText().toString();
                String confpass = confirmpassET.getText().toString();

                if(currentpass.isEmpty())
                {
                    Toast.makeText(UpdatePassword.this, "enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(newpass.isEmpty())
                {
                    Toast.makeText(UpdatePassword.this, "New password can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(confpass.isEmpty())
                {
                    Toast.makeText(UpdatePassword.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(newpass.length() < 7)
                {
                    Toast.makeText(UpdatePassword.this, "New password must have at least 8 length!", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if(currentpass.equals(dbPass))
                {
                    if(!newpass.equals(confpass))
                    {
                        Toast.makeText(UpdatePassword.this, "Password and confirm passwords are not matching!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Boolean update = db.updatePassword(sharedFid, newpass);

                        if(update){
                            Toast.makeText(UpdatePassword.this, "Password updated!", Toast.LENGTH_SHORT).show();
                            editor.clear();
                            editor.commit();
                            startActivity(new Intent(getApplicationContext(), FacultyLogin.class));
                            finish();
                        }
                        else{
                            Toast.makeText(UpdatePassword.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                else{
                    Toast.makeText(UpdatePassword.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}