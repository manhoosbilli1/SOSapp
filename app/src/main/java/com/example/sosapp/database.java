package com.example.sosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class database extends AppCompatActivity {

    EditText phone_num;
    Button add, delete, viewAll;
    ListView number_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        phone_num = findViewById(R.id.phone);
        add = findViewById(R.id.add);
        delete = findViewById(R.id.delete);
        viewAll = findViewById(R.id.viewAll);
        number_list = findViewById(R.id.number_list);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper = new DatabaseHelper(database.this);
                String number = phone_num.getText().toString().trim();
                if(number.equals("")){
                    Toast.makeText(database.this, "Please enter a number into the field", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    boolean success = databaseHelper.addOne(number);
                    Toast.makeText(database.this, "Successfully added", Toast.LENGTH_SHORT).show();
                }

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




    }
}