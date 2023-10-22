package com.example.financialtracker;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
                + DATE_COL + " DATE, "
                + AMOUNT_COL + " INTEGER)";

        String expensesTableCreate = "CREATE TABLE " + TABLE_NAME_INCOME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DESC_COL + " TEXT, "
                + DATE_COL + " DATE, "
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

    public String getExpenses(String fromDateString, String toDateString){
        int[] fromDate, toDate;
        fromDateString = "9/17/2023";
        toDateString = "9/17/2023";

        fromDate = convertDates(fromDateString);
        toDate = convertDates(toDateString);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM expenses;", null );


        String[][] expenseData = new String[cursor.getCount()][3];
        if (cursor.moveToFirst()){
            if (cursor.getString(2) != null){
                System.out.println("From: " + fromDateString + " To: " + toDate[0] + "/" + toDate[1] + "/" + toDate[2]);

                int i = 0;
                do {
                    int[] checkDate = convertDates(cursor.getString(2));
                    System.out.println("Tested Date: " + checkDate[0] + "/" + checkDate[1] + "/" + checkDate[2]);
                    if (checkDate[0] >= fromDate[0] && checkDate[0] <= toDate[0]){
                        if (checkDate[1] >= fromDate[1] && checkDate[1] <= toDate[1]){
                            if (checkDate[2] >= fromDate[2] && checkDate[2] <= toDate[2]) {
                                expenseData[i][0] = cursor.getString(1);
                                expenseData[i][1] = cursor.getString(2);
                                expenseData[i][2] = cursor.getString(3);

                            }
                        }
                    }
                    i++;
                } while (cursor.moveToNext());
            }
        }

        System.out.println(stringifyData(expenseData, expenseData.length));
        db.close();
        return "5";
    }

    public void getIncome(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE income;");
    }

    public String getIncome(String fromDateString, String toDateString){
        int[] fromDate, toDate;
        fromDateString = "9/17/2023";
        toDateString = "9/17/2023";

        fromDate = convertDates(fromDateString);
        toDate = convertDates(toDateString);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM income;", null );
        //System.out.println(cursor.getCount());


//        if (cursor.isNull(0)){
//            System.out.println("Empty Table");
//        } else {
//            String[][] incomeData = new String[cursor.getCount()][3];
//            if (cursor.moveToFirst()){
//                if (cursor.getString(2) != null){
//                    System.out.println("From: " + fromDateString + " To: " + toDate[0] + "/" + toDate[1] + "/" + toDate[2]);
//
//                    int i = 0;
//                    do {
//                        int[] checkDate = convertDates(cursor.getString(2));
//                        System.out.println("Tested Date: " + checkDate[0] + "/" + checkDate[1] + "/" + checkDate[2]);
//                        if (checkDate[0] >= fromDate[0] && checkDate[0] <= toDate[0]){
//                            if (checkDate[1] >= fromDate[1] && checkDate[1] <= toDate[1]){
//                                if (checkDate[2] >= fromDate[2] && checkDate[2] <= toDate[2]) {
//                                    incomeData[i][0] = cursor.getString(1);
//                                    incomeData[i][1] = cursor.getString(2);
//                                    incomeData[i][2] = cursor.getString(3);
//                                }
//                            }
//                        }
//                        i++;
//                    } while (cursor.moveToNext());
//                }
//            }
//
//            System.out.println(stringifyData(incomeData, incomeData.length));
//        }
        db.close();
        return "5";
    }

    private String stringifyData(String[][] data, int cursorCount){
        String print = "";
        for (int i = 0; i < cursorCount; i++){
            for (int j = 0; j < 3; j++){
                if (data[i][j] != null){
                    if (j == 0){
                        print += "Desc: ";
                    } else if (j == 1){
                        print += " Date: ";
                    } else if (j == 2){
                        print += " Amount: ";
                    }
                    print += "" + data[i][j];
                } else {

                }
            }
            print += "\n";
        }
        return print;
    }

    private void recreateIncomeTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        String incomeTableCreate = "CREATE TABLE " + TABLE_NAME_EXPENSES + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DESC_COL + " TEXT, "
                + DATE_COL + " DATE, "
                + AMOUNT_COL + " INTEGER)";

        db.execSQL(incomeTableCreate);

    }

    private void recreateExpenseTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        String expensesTableCreate = "CREATE TABLE " + TABLE_NAME_INCOME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DESC_COL + " TEXT, "
                + DATE_COL + " DATE, "
                + AMOUNT_COL + " INTEGER)";
        db.execSQL(expensesTableCreate);

    }


    private int[] convertDates(String date){
        int[] convertedDate = new int[3];
        String[] dateString;
        dateString = date.split("/", -2);
        convertedDate[0] = Integer.parseInt(dateString[0]);
        convertedDate[1] = Integer.parseInt(dateString[1]);
        convertedDate[2] = Integer.parseInt(dateString[2]);

        return convertedDate;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
