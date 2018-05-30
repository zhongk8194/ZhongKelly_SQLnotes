package com.example.zhongk8194.mycontactapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    DatabaseHelper myDb;
    EditText editName;
    EditText editNumber;
    EditText editAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editText_name);
        editNumber = findViewById(R.id.editText_number);
        editAddress = findViewById(R.id.editText_address);

        myDb = new DatabaseHelper(this);
        Log.d("MyContactApp", "MainActivity: instantiated DatabaseHelper");

    }

    public void addData(View view) {
        Log.d("MyContactApp", "MainActivity: Add contact button pressed");

        boolean isInserted = myDb.insertData(editName.getText().toString(), editNumber.getText().toString(), editAddress.getText().toString());

        if (isInserted == true){
            Toast.makeText(MainActivity.this, "Success - contact inserted", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(MainActivity.this, "FAILED - contact not inserted", Toast.LENGTH_LONG).show();
        }

    }

    public void viewData(View view){
        Cursor res = myDb.getAllData();
        Log.d("MyContactApp", "MainActivity: viewData: received cursor " + res.getCount());
        if(res.getCount() == 0){
            showMessage("Error", "No data found in database");
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){
            //Append res column 0,1,2,3 to the buffer, delimited by "\n"
            buffer.append(res.getString(0) + "\n" + res.getString(1) + "\n" + res.getString(2) + "\n" + res.getString(3) + "\n");
        }
        Log.d("MyContactApp", "MainActivity: viewData: assembled stringbuffer");
        showMessage("Data", buffer.toString());
    }

    public void showMessage(String title, String message) {
        Log.d("MyContactApp", "MainActivity: showMessage: building alert dialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public static final String EXTRA_MESSAGE = "com.example.zhongk8194.mycontactapp.MESSAGE";
    public void SearchRecord(View view){
        Log.d("MyContactApp", "MainActivity: launching SearchActivity");
        Cursor res = myDb.getAllData();
        Intent intent = new Intent(this, SearchActivity.class);
        StringBuffer buffer = new StringBuffer();

        while (res.moveToNext()){
            if (res.getString(1).matches(editName.getText().toString()))
            {
                buffer.append(res.getString(0) + "\n" + res.getString(1) + "\n" + res.getString(2) + "\n" + res.getString(3) + "\n" + "\n");
            }
            else if (res.getString(2).matches(editNumber.getText().toString()))
            {
                buffer.append(res.getString(0) + "\n" + res.getString(1) + "\n" + res.getString(2) + "\n" + res.getString(3) + "\n" + "\n");
            }
            else if (res.getString(3).matches(editAddress.getText().toString()))
            {
                buffer.append(res.getString(0) + "\n" + res.getString(1) + "\n" + res.getString(2) + "\n" + res.getString(3) + "\n" + "\n");
            }
        }

        intent.putExtra(EXTRA_MESSAGE, buffer.toString());
        startActivity(intent);
    }

}
