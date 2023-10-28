package com.example.financialtracker;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
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

    private static final int columnCount = 3;

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Called once on creation of DB. Used to create income and expenses tables.
     * @param db
     */
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

    /**
     * Information sent from UI is inputted into database with current date grabbed and added.
     * @param desc String description of income entry.
     * @param amount Integer amount of income entry.
     */
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

    /**
     * Information sent from UI is inputted into database with current date grabbed and added.
     * @param desc String description of expense entry.
     * @param amount Integer amount of expense entry.
     */
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

    /**
     * Used to grab expense table data.
     * @param fromDateString ex. "9/10/2023" Start of wanted dates.
     * @param toDateString ex. "9/20/2023" End of wanted dates.
     * @return 2-Dimensional string array of valid rows.
     */
    public String getExpenses(String fromDateString, String toDateString){
        int[] fromDate, toDate;
        //Temporary, delete after creating date functionality in UI.
        fromDateString = "9/17/2023";
        toDateString = "9/29/2023";

        fromDate = convertDates(fromDateString);
        toDate = convertDates(toDateString);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM expenses;", null );


        String[][] expenseData = new String[cursor.getCount()][columnCount];
        if (cursor.moveToFirst()){
            if (cursor.getString(2) != null){
                int i = 0;
                do {
                    int[] checkDate = convertDates(cursor.getString(2));
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
        expenseData = cleanEmptyCells(expenseData);
        System.out.println(stringifyData(expenseData, expenseData.length));
        db.close();
        return "Temporary Return";
    }

    /**
     * Used to grab income table data.
     * @param fromDateString ex. "9/10/2023" Start of wanted dates.
     * @param toDateString ex. "9/20/2023" End of wanted dates.
     * @return 2-Dimensional string array of valid rows.
     */
    public String getIncome(String fromDateString, String toDateString){
        int[] fromDate, toDate;
        //Temporary, delete after creating date functionality in UI.
        fromDateString = "9/17/2023";
        toDateString = "9/29/2023";

        fromDate = convertDates(fromDateString);
        toDate = convertDates(toDateString);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM income;", null );


        String[][] incomeData = new String[cursor.getCount()][columnCount];
        if (cursor.moveToFirst()){
            if (cursor.getString(2) != null){
                int i = 0;
                do {
                    int[] checkDate = convertDates(cursor.getString(2));
                    if (checkDate[0] >= fromDate[0] && checkDate[0] <= toDate[0]){
                        if (checkDate[1] >= fromDate[1] && checkDate[1] <= toDate[1]){
                            if (checkDate[2] >= fromDate[2] && checkDate[2] <= toDate[2]) {
                                incomeData[i][0] = cursor.getString(1);
                                incomeData[i][1] = cursor.getString(2);
                                incomeData[i][2] = cursor.getString(3);

                            }
                        }
                    }
                    i++;
                } while (cursor.moveToNext());
            }
        }
        incomeData = cleanEmptyCells(incomeData);
        System.out.println(stringifyData(incomeData, incomeData.length));
        db.close();
        return "Temporary Return";
    }

    /**
     * Takes a 2-Dimensional array pulled from database and converts the data
     * to a readable string.
     * @param data
     * @param cursorCount
     * @return
     */
    private String stringifyData(String[][] data, int cursorCount){
        String print = "";
        for (int i = 0; i < cursorCount; i++){
            for (int j = 0; j < columnCount; j++){
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

    /**
     * Takes in a 2-Dimensional string array that is pulled from a database call
     * then removes all null rows to return a cleaned 2-Dimensional array.
     * @param toClean
     * @return
     */
    private String[][] cleanEmptyCells(String[][] toClean){
        String[][] cleanedArray;
        int count = 0;
        List<Integer> cleanCellList =new ArrayList<Integer>();

        for(int i = 0; i < toClean.length; i++){
            if(toClean[i][0] != null){
                count++;
                cleanCellList.add(i);
            }
        }

        cleanedArray = new String[count][columnCount];

        Object[] test = cleanCellList.toArray();
        for(int i = 0; i < test.length; i++){
            cleanedArray[i] = toClean[Integer.parseInt(test[i].toString())];
        }

        return cleanedArray;
    }

    /**
     * Clears and calls recreateIncomeTable() to rebuild a clean table.
     */
    public void clearIncome(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE income;");
        recreateIncomeTable();
    }

    /**
     * Clears and calls recreateExpenseTable() to rebuild a clean table.
     */
    public void clearExpenses(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE expenses;");
        recreateExpenseTable();
    }


    /**
     * Used to recreate income table in case it is cleared for testing.
     */
    private void recreateIncomeTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        String incomeTableCreate = "CREATE TABLE " + TABLE_NAME_INCOME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DESC_COL + " TEXT, "
                + DATE_COL + " DATE, "
                + AMOUNT_COL + " INTEGER)";

        db.execSQL(incomeTableCreate);

    }

    /**
     * Used to recreate expense table in case it is cleared for testing.
     */
    private void recreateExpenseTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        String expensesTableCreate = "CREATE TABLE " + TABLE_NAME_EXPENSES + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DESC_COL + " TEXT, "
                + DATE_COL + " DATE, "
                + AMOUNT_COL + " INTEGER)";
        db.execSQL(expensesTableCreate);

    }

    /**
     * Takes a date string and converts it into an integer array, to be used in
     * calculations determining if pulled dates are within given range.
     * @param date ex. "9/20/2023'
     * @return [9, 20, 2023]
     */
    private int[] convertDates(String date){
        int[] convertedDate = new int[3];
        String[] dateString;
        dateString = date.split("/", -2);
        convertedDate[0] = Integer.parseInt(dateString[0]);
        convertedDate[1] = Integer.parseInt(dateString[1]);
        convertedDate[2] = Integer.parseInt(dateString[2]);

        return convertedDate;
    }

    /**
     * Default method that just exists here, not currently used or assumed to be needed in future.
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
