package com.example.sosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class database extends AppCompatActivity {

    EditText phone_num;
    Button add, delete, viewAll;
    ListView number_list;
    ArrayAdapter numberArrayAdapter;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        databaseHelper = new DatabaseHelper(database.this);
        phone_num = findViewById(R.id.phone);
        add = findViewById(R.id.add);
        delete = findViewById(R.id.delete);
        viewAll = findViewById(R.id.viewAll);
        number_list = findViewById(R.id.number_list);
        showNumbers(databaseHelper);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper = new DatabaseHelper(database.this);
                String number = phone_num.getText().toString().trim();
                if(number.equals("")){
                    Toast.makeText(database.this, "Please enter a number into the field", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if(number.length() <= 20 && number.length() >=9){
                        boolean success = databaseHelper.addOne(number);
                        showNumbers(databaseHelper);
                        Toast.makeText(database.this, "Successfully added", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(database.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DatabaseHelper databaseHelper = new DatabaseHelper(database.this);
                    databaseHelper.delete();
                    showNumbers(databaseHelper);
                    Toast.makeText(database.this, "Successfully delete the database", Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(database.this, "Couldn't delete the database: FAILED", Toast.LENGTH_SHORT).show();
                }

            }
        });

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper = new DatabaseHelper(database.this);

                numberArrayAdapter = new ArrayAdapter<String>(database.this, android.R.layout.simple_list_item_1,databaseHelper.getEveryone());
                number_list.setAdapter(numberArrayAdapter);

            }
        });





    }

    private void showNumbers(DatabaseHelper databaseHelper2) {
        numberArrayAdapter = new ArrayAdapter<String>(database.this, android.R.layout.simple_list_item_1, databaseHelper2.getEveryone());
        number_list.setAdapter(numberArrayAdapter);
    }


}