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

import com.androidbegin.parselogintutorial.ParseApplication;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;


/**
 * Created by Emi on 05.05.2015.
 */

public class SignUpChild extends Activity {
    EditText code_from_parent;
    // private static final String USER_OBJECT_QUESTIONS = "questions";
    //private static final String USER_OBJECT_POINTS = "points";

    EditText password;
    EditText email; //this is the email
    EditText name;
    Button signup;
    private static final String USER_PAIR = "USER_PAIR";

    private static final String USER_TYPE = "USER_TYPE";
    // private static final String USER_OBJECT_QUESTIONS = "questions";
    //private static final String USER_OBJECT_POINTS = "points";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from main.xml
        final ParseApplication globalVariable = (ParseApplication) getApplicationContext();
        setContentView(R.layout.signup_child);

        String usernametxt = getIntent().getStringExtra("usernametxt");
        String passwordtxt = getIntent().getStringExtra("passwordtxt");

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.name);

        email.setText(usernametxt);
        password.setText(passwordtxt);
        code_from_parent = (EditText) findViewById(R.id.code);
        signup = (Button) findViewById(R.id.signup);
        // Sign up Button Click Listener
        signup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                final String emailtxtfinal = email.getText().toString();
                final String passwordtxtfinal = password.getText().toString();
                final String nametxtfinal = name.getText().toString();
                final String USER_PAIR_final = code_from_parent.getText().toString();

                // Force user to fill up the form
                if (emailtxtfinal.equals("") || passwordtxtfinal.equals("") || nametxtfinal.equals("") || USER_PAIR_final.equals("")) {

                    Toast.makeText(getApplicationContext(),
                            "Please complete the sign up form",
                            Toast.LENGTH_LONG).show();

                } else {
                    Log.d("de ce intra aici",emailtxtfinal+ " "+ passwordtxtfinal+ " "+ nametxtfinal+ " "+ USER_PAIR_final);
                    //check that there is a user with that kind of ObjectId
                    if (checkCode(USER_PAIR_final) == true) {
                        // Save new user data into Parse.com Data Storage
                        final ParseUser user = new ParseUser();

                        user.setUsername(nametxtfinal);
                        user.setPassword(passwordtxtfinal);
                        user.setEmail(emailtxtfinal);
                        user.put("USER_TYPE","child");
                        user.put("USER_PAIR",USER_PAIR_final);

                        user.signUpInBackground(new SignUpCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Show a simple Toast message upon successful registration
                                    Toast.makeText(getApplicationContext(),
                                            "Successfully Signed up, please log in.",
                                            Toast.LENGTH_LONG).show();
                                    connectParent(USER_PAIR_final,user.getObjectId());
                                    Intent intent = new Intent(getApplicationContext(), Empty.class);
                                    String id = user.getObjectId();
                                    Log.d("iiiiiiiiiiiiiiiiiidddd", id);
                                    globalVariable.setUserId(id);
                                    Log.d("iiiiiiiiiiiiiiiiiidddd", globalVariable.getUserId());
                                    intent.putExtra("objectId", id);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Sign up Error", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpChild.this);
                        builder.setTitle("Eroare")
                                .setMessage("The code is not correct! Retry")
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }


                }

            }
        });
    }


    public boolean checkCode(String code) {
        final boolean[] success = {true};
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", code);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.

                } else {
                    // Something went wrong.
                    success[0] = false;
                }
            }

        });
        return success[0];

    }

    public void connectParent(String code, final String kid){

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(code, new GetCallback<ParseUser>() {

            @Override
            public void done(ParseUser parseUser, com.parse.ParseException e) {
                if (e == null) {
                    //object will be User
                    Log.d("user", parseUser.getUsername());

                    parseUser.put("USER_PAIR", kid);
                    parseUser.saveInBackground();
                } else {
                    //something went wrong;
                    Log.e("TAG", e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpChild.this);
                    builder.setTitle("Eroare")
                            .setMessage(e.getMessage())
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }


}