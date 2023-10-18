package com.example.financialtracker;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DatabaseHandler extends SQLiteOpenHelper{
    private static final String DB_NAME = "finance";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME_INCOME = "income";
    private static final String TABLE_NAME_EXPENSES = "expenses";
    private static final String ID_COL = "id";
    private static final String DESC_COL = "description";
    private static final String DATE_COL = "date";
    private static final String AMOUNT_COL = "amount";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String incomeTableCreate = "CREATE TABLE " + TABLE_NAME_EXPENSES + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DESC_COL + " TEXT, "
                + DATE_COL + " TEXT, "
                + AMOUNT_COL + " INTEGER)";

        String expensesTableCreate = "CREATE TABLE " + TABLE_NAME_INCOME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DESC_COL + " TEXT, "
                + DATE_COL + " TEXT, "
                + AMOUNT_COL + " INTEGER)";

        db.execSQL(incomeTableCreate);
        db.execSQL(expensesTableCreate);
    }

    public void addNewIncome(String desc, Integer amount) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            TimeZone tz = TimeZone.getDefault();
            Calendar calendar = new GregorianCalendar(tz);
            String date = calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DATE)
                    + "/" + calendar.get(Calendar.YEAR);

            values.put(DESC_COL, desc);
            values.put(DATE_COL, date);
            values.put(AMOUNT_COL, amount);

            db.insert(TABLE_NAME_INCOME, null, values);
            db.close();

    }

    public void addNewExpense(String desc, Integer amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        TimeZone tz = TimeZone.getDefault();
        Calendar calendar = new GregorianCalendar(tz);
        String date = calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DATE)
                + "/" + calendar.get(Calendar.YEAR);

        values.put(DESC_COL, desc);
        values.put(DATE_COL, date);
        values.put(AMOUNT_COL, amount);

        db.insert(TABLE_NAME_EXPENSES, null, values);
        db.close();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
