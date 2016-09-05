package com.androidbegin.parselogintutorial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


/**
 * Created by Emi on 05.05.2015.
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class SignUp extends Activity {
    EditText password;
    EditText email; //this is the email
    EditText name;
    Button signup;
    private static final String USER_PAIR = "USER_PAIR";
    private static final String USER_TYPE = "USER_TYPE";
    private static final String latest_stress_status = "null";
    private static final Date latest_date = null;
    private static final int age=0;
    private static final String sex="sex";



    public void onCreate(Bundle savedInstanceState) {
        Log.d("SIGNUP","PARINTEEEEEE");
        super.onCreate(savedInstanceState);
        // Get the view from main.xml
        final ParseApplication globalVariable=(ParseApplication) getApplicationContext();
        setContentView(R.layout.signup);

        String usernametxt=getIntent().getStringExtra("usernametxt");
        String passwordtxt=getIntent().getStringExtra("passwordtxt");

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        name=(EditText) findViewById(R.id.name);

        email.setText(usernametxt);
        password.setText(passwordtxt);


        signup = (Button) findViewById(R.id.signup);
        // Sign up Button Click Listener
        signup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                final String emailtxtfinal = email.getText().toString();
                final String passwordtxtfinal = password.getText().toString();
                final String nametxtfinal=name.getText().toString();

                Log.d("emailtxtfilnal",emailtxtfinal.toString());
                Log.d("passwordtxtfinal", passwordtxtfinal+ "nametxtfinal"+nametxtfinal);


                // Force user to fill up the form
                if (emailtxtfinal.equals("") || passwordtxtfinal.equals("") || nametxtfinal.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete the sign up form",
                            Toast.LENGTH_LONG).show();

                } else {
                    Log.d("de ce intra aici",emailtxtfinal+ " "+ passwordtxtfinal+ " "+ nametxtfinal);
                    // Save new user data into Parse.com Data Storage
                    final ParseUser user = new ParseUser();
                    user.setEmail(emailtxtfinal);
                    user.setPassword(passwordtxtfinal);
                    user.setUsername(nametxtfinal);
                    user.put(USER_TYPE, "parent");
                    user.put(USER_PAIR, "none");
                    user.put("latest_stress_status",latest_stress_status);
                    user.put("age",10);
                    Date d=new Date();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    user.put("latest_date", dateFormat.format(d));
                    user.put("sex","M");

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Show a simple Toast message upon successful registration
                                Toast.makeText(getApplicationContext(),
                                        "Successfully Signed up, please log in.",
                                        Toast.LENGTH_LONG).show();
                                Intent intent= new Intent(getApplicationContext(),InstructionsSetAccountAfterSignup.class);
                                String id= user.getObjectId();
                                Log.d("iiiiiiiiiiiiiiiiiidddd", id);
                                globalVariable.setUserId(id);
                                Log.d("iiiiiiiiiiiiiiiiiidddd", globalVariable.getUserId());
                                intent.putExtra("objectId",id);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Sign up Error"+e.toString(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
                }


            }
        });


    }}
