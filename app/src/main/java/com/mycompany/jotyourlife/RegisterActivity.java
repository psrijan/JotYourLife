package com.mycompany.jotyourlife;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    TextView loginLink;
    EditText name;
    EditText email;
    EditText password;


    DatabaseHelper helper;

    Button onRegister;
    Button clearDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        loginLink = (TextView) findViewById(R.id.link_to_login);
        name =(EditText) findViewById(R.id.reg_fullname);
        email=(EditText) findViewById(R.id.reg_email);
        password=(EditText) findViewById(R.id.reg_password);
        clearDB = (Button) findViewById(R.id.btnClearDB);

        onRegister = (Button) findViewById(R.id.btnRegister);
        helper = new DatabaseHelper(RegisterActivity.this);

        onRegister.setOnClickListener(myListener);
        loginLink.setOnClickListener(myListener);
        clearDB.setOnClickListener(myListener);


    }
    View.OnClickListener myListener = new View.OnClickListener() {



        @Override
        public void onClick(View v) {
            String name_txt = name.getText().toString();
            String email_txt = email.getText().toString();
            String password_txt = password.getText().toString();

            if(v.getId() == R.id.btnClearDB) {
                Log.d("MYAPP" , "DB BTN");
                helper.clearDB();
            }

            if(v.getId() == R.id.link_to_login){
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            } else if(v.getId() == R.id.btnRegister){
                Log.d("MYAPP" , "Register BTN CLICKED");
                if(validateFields(name_txt,email_txt,password_txt)) {
                    Log.d("MYAPP" , "Vlidation success ");
                    if (!emailInDatabaseCheck(email_txt)) {
                        Log.d("MYAPP" , "email not in DB");
                        helper.AddUser(name_txt, email_txt, password_txt);
                        Toast.makeText(RegisterActivity.this, "Registered Successfully" , Toast.LENGTH_LONG).show();
                        clearTextFields();

                    } else {
                        Toast.makeText(RegisterActivity.this, "Email Id Already Present" , Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

    };

    void clearTextFields() {

        name.setText("");
        email.setText("");
        password.setText("");
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
    }

    boolean emailInDatabaseCheck(String email)
    {
        Cursor c = helper.getAll(email);
        Log.d("MYAPP" , "COUNT: " + c.getCount()+"");
        if (c.getCount()==0) {
            return false;
        }
        return  true;
    }

    boolean validateFields(String name, String email, String password) {
        if (name.matches("")) {
            Log.d ("MYAPP", "name is null");
            Toast.makeText(getApplicationContext(), "Name Field Empty", Toast.LENGTH_LONG).show();
        }
        if (email.matches("")) {
            Log.d ("MYAPP", "email is empty");
            Toast.makeText(getApplicationContext(), "Email Field Empty", Toast.LENGTH_LONG).show();
            return false;
        } else if (password.matches("")) {
            Toast.makeText(getApplicationContext(), "Password Field Empty", Toast.LENGTH_LONG).show();
            Log.d("MYAPP" , "PASSWORD EMPTY");
            return false;
        } else {
            return true;
        }
    }
};