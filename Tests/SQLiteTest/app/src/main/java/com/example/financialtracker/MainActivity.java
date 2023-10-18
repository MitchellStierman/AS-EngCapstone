package com.example.financialtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int income, expense;
    EditText incomeInput, expenseInput;

    Button incomeButton, expenseButton;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        incomeInput = (EditText) findViewById(R.id.incomeInput);
        incomeButton = (Button) findViewById(R.id.incomeButton);
        incomeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (TextUtils.isEmpty(incomeInput.getText().toString())){
                    incomeInput.setError("You must enter an amount.");
                } else {
                    income = Integer.parseInt(incomeInput.getText().toString());

                    db = new DatabaseHandler(MainActivity.this);
                    db.addNewIncome("test", income);
                    showToast("Income Added");
                }
            }
        });

        expenseInput = (EditText) findViewById(R.id.expenseInput);
        expenseButton = (Button) findViewById(R.id.submitButton2);
        expenseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (TextUtils.isEmpty(expenseInput.getText().toString())) {
                    expenseInput.setError("You must enter an amount.");

                } else {
                    expense = Integer.parseInt(expenseInput.getText().toString());

                    db = new DatabaseHandler(MainActivity.this);
                    db.addNewExpense("test", expense);
                    showToast("Expense Submitted");
                }
            }
        });
    }

    private void showToast(String text){
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();

    }


}