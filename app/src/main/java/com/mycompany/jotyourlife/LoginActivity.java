package com.mycompany.jotyourlife;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button login;

    DatabaseHelper helper;

    TextView registerLink;
    String email_txt;
    String password_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_design); // changes made here

        registerLink=(TextView) findViewById(R.id.txtLinkToRegister);
        registerLink.setOnClickListener(myListener);

        email =(EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);

        helper = new DatabaseHelper(LoginActivity.this);

        login = (Button) findViewById(R.id.btnLogin);

        login.setOnClickListener(myListener);
    }

    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            email_txt = email.getText().toString();
            password_txt = password.getText().toString();

            if(v.getId() == R.id.link_to_register){
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            } else if(v.getId() == R.id.btnLogin) {
                //login attempt
                if(validateFields(email_txt, password_txt)) {
                    if(helper.Login(email_txt , password_txt)){
                        HomeScreenAccess();
                    }
                }
            }
        }
    };

    public static final String USERID = "com.mycompany.jotyourlife.userid";

    void HomeScreenAccess() {

        Intent i = new Intent(LoginActivity.this,MainActivity.class );
        Cursor cursor = helper.getAll(email_txt);
        Log.d ("MYAPP" , "MOVING CURSOR TO FIRST POSITION: " +cursor.moveToFirst());

        Log.d("MYAPP" , "CURSOR SIZE: " + cursor.getCount());
        String user_ID = helper.getUID(cursor);
        Log.d("MYAPP" , " USERID: " + user_ID) ;
        i.putExtra(USERID , user_ID);
        startActivity(i);
    }

    boolean validateFields(String email, String password) {

        if (email.matches("")) {
            Log.d ("MYAPP", "email is empty");
            Toast.makeText(getApplicationContext(), "Email Field Empty", Toast.LENGTH_LONG).show();
            return false;
        } else if (password.matches("")) {
            Toast.makeText(getApplicationContext(), "Password Field Empty", Toast.LENGTH_LONG).show();
            Log.d("MYAPP" , "PASSWORD EMPTY");
            return false;
        } else {
            Log.d("MYAPP" , email + " " + password) ;
            return true;
        }
    }
};
