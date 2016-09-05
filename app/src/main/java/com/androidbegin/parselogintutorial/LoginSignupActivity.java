package com.androidbegin.parselogintutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class LoginSignupActivity extends Activity {
    public static String ObjectId;
    // Declare UI
	Button loginbutton;
    Button signup;
	String usernametxt;
	String passwordtxt;
	EditText password;
	EditText username;
    ParseApplication globalVariable;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
        Log.d("passsing", "1-fffffffffffirst");
		super.onCreate(savedInstanceState);
		// Get the view from main.xml
		setContentView(R.layout.loginsignup);
        globalVariable = (ParseApplication) getApplicationContext();

		// Locate EditTexts in main.xml
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);


		// Locate Buttons in main.xml
		loginbutton = (Button) findViewById(R.id.login);
		signup = (Button) findViewById(R.id.signup);

		// Login Button Click Listener
		loginbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// Retrieve the text entered from the EditText
				usernametxt = username.getText().toString();
				passwordtxt = password.getText().toString();



				// Send data to Parse.com for verification
				ParseUser.logInInBackground(usernametxt, passwordtxt,
						new LogInCallback() {
							public void done(ParseUser user, ParseException e) {
								if (user != null) {
									// If user exist and authenticated, send user to Welcome.class
									String id=user.getObjectId();
                                    globalVariable.setUserId(id);
                                    ObjectId=user.getObjectId();
                                    // Creating Bundle object
                                    if(user.get("USER_TYPE").equals("parent")) {
                                        Intent intent = new Intent(
                                                LoginSignupActivity.this,
                                                BeginHere.class);
                                        // intent.putExtra("objectId",id);
                                        startActivity(intent);
                                    }
                                    if(user.get("USER_TYPE").equals("child")) {
                                        Intent intent = new Intent(
                                                LoginSignupActivity.this,
                                                BlueSmirfDemo.class);
                                        // intent.putExtra("objectId",id);
                                        startActivity(intent);
                                    }
									Toast.makeText(getApplicationContext(),
											"Successfully Logged in",
											Toast.LENGTH_LONG).show();
									finish();
								} else {
									Toast.makeText(
											getApplicationContext(),
											"No such user exist, please signup",
											Toast.LENGTH_LONG).show();
								}
							}
						});
			}
		});
		// Sign up Button Click Listener
		signup.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// Retrieve the text entered from the EditText
				usernametxt = username.getText().toString();
				passwordtxt = password.getText().toString();

                Intent intent= new Intent(getApplicationContext(),WhatAreYou.class);
                intent.putExtra("usernametxt",usernametxt);
                intent.putExtra("passwordtxt",passwordtxt);
                startActivity(intent);

				}
		});
	}


}
