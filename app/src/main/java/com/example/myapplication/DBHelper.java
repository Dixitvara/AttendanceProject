package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "AttendanceManagementProject.db";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE attendance(srno integer primary key autoincrement,sname text, enrollment string, semester text, subject text, date text, time text)");
        db.execSQL("CREATE TABLE student(sname text, enrollment text primary key, email email unique, password text)");
        db.execSQL("CREATE TABLE admin(aname text unique not null,email email primary key, password text)");
        db.execSQL("CREATE TABLE faculty(fname text not null, fid text primary key, email email unique not null,password text)");
        db.execSQL("INSERT INTO admin VALUES (?,?,?)",new String[]{"admin","admin@gmail.com", "admin123"});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS student");
        db.execSQL("DROP TABLE IF EXISTS attendance");
        db.execSQL("DROP TABLE IF EXISTS admin");
        db.execSQL("DROP TABLE IF EXISTS faculty");
    }

    // signup user
    public Boolean insertData(String studentName, String enrollment, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();

        c.put("sname", studentName);
        c.put("enrollment", enrollment);
        c.put("email", email);
        c.put("password", password);

        long l = db.insert("student", null, c);
        if (l == -1) return false;
        else
            return true;
    }

    // Insert data in to attendance table
    public Boolean insertAttendanceData(String sName, String enrollmentNo, String semester, String subject, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();

        c.put("sname", sName);
        c.put("enrollment", enrollmentNo);
        c.put("semester", semester);
        c.put("subject", subject);
        c.put("date", date);
        c.put("time", time);

        long l = db.insert("attendance", null, c);
        if (l == -1) return false;
        else
            return true;
    }

    // login user
    public Boolean checkEnrollmentPassword(String enrollment, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from student where enrollment = ? and password = ?", new String[]{enrollment, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    // get student information
    public Cursor getStudentInfo(String enrollment) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from student where enrollment = ?", new String[]{enrollment});
        return cursor;
    }

    // view attendance of students
    public Cursor getStudentAttendance( String enrollment,String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from attendance where enrollment = ? and date = ?", new String[]{enrollment, date});
        return cursor;
    }

    // insert default admin
    public Boolean addFaculty(String facultyName, String facultyId, String facultyEmail,String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();

        c.put("fname", facultyName);
        c.put("fid", facultyId);
        c.put("email", facultyEmail);
        c.put("password", password);

        long l = db.insert("faculty", null, c);
        if (l == -1) return false;
        else
            return true;
    }

    // get student information
    public Boolean checkAdmin(String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from admin where email = ? and password = ?", new String[]{email, password});

        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Cursor getAdminInfo(String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from admin where email = ?", new String[]{email});
        return cursor;
    }

    public Cursor getFacultyName(String fid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from faculty where fid = ?", new String[]{fid});
        return cursor;
    }

    public Cursor getFacultyInfo(String fid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from faculty where fid = ?",new String[]{fid});
        return cursor;
    }

    public Boolean removeFaculty(String fid){
        SQLiteDatabase db = this.getWritableDatabase();
        long delete = db.delete("faculty","fid = ?", new String[]{fid});
        if(delete == -1)
            return false;
        else
            return true;

    }

    public Cursor viewFaculty(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from faculty", null);
        return cursor;
    }

    // login user
    public Boolean checkFidPassword(String fid, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from faculty where fid = ? and password = ?", new String[]{fid, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Cursor getAllData(String text){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Attendance where enrollment like ? or sname like ?", new String[]{text, text});
        return cursor;
    }

    public Cursor getAttendance(String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from attendance where date = ?", new String[]{date});
        return cursor;
    }

    public Cursor getAttendanceData(String text, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from attendance where enrollment like ? or sname like ? and date like ?", new String[]{text, text, date});
        return cursor;
    }

    // update user details
    public Boolean updateStudDetails(String enrollment, String studName, String studEmail, String studPassword) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();

        c.put("sname", studName);
        c.put("email", studEmail);
        c.put("password", studPassword);

        long l = db.update("student", c, "enrollment=?", new String[]{enrollment});
        if (l == -1) return false;
        else
            return true;
    }

    // update faculty password
    public Boolean updatePassword(String fid, String newPassword) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();

        c.put("password", newPassword);

        long l = db.update("faculty", c, "fid=?", new String[]{fid});
        if (l == -1) return false;
        else
            return true;
    }
}
