package com.androidbegin.parselogintutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Emi on 10.06.2015.
 */
public class Empty extends Activity {
    TextView connected_check;
    Button signout;
    //ParseApplication globalVariable;


    public void onCreate(Bundle savedInstanceState) {

        //Log.d("passsing", "2-ssssssssssssssssssecond");
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        final ParseApplication globalVariable = (ParseApplication) getApplicationContext();
        setContentView(R.layout.empty);
        connected_check= (TextView) findViewById(R.id.connected_check);
        //check if the currentuser(child or parent) is connect to his(parent of child)
        if (checkPair(globalVariable.getUserId())==true)
            connected_check.setText("You are now connected!");
        else
            connected_check.setText("You are not yet connected! Set up an account for you kid!");

        //signout
        signout = (Button) findViewById(R.id.signout);
        // Sign up Button Click Listener
        signout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(Empty.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
    });
  }

       //check if connected
    public boolean checkPair(String code) {
        final boolean[] success = {false};
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(code, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if (e == null) {
                    // The query was successful.
                    if(user.get("USER_PAIR")!="none") success[0]=true;
                    Log.d("USER_PAIR", user.get("USER_PAIR").toString());
                } else {
                    // Something went wrong.
                    success[0] = false;
                }
            }
        });
        return success[0];
    }

}
