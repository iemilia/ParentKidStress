package com.androidbegin.parselogintutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

import java.text.ParseException;

public class MainActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {


        //Log.d("passsing", "2-ssssssssssssssssssecond");
		super.onCreate(savedInstanceState);
        getActionBar().hide();
        // Track app opens.
        ParseAnalytics.trackAppOpened(getIntent());

        final ParseApplication globalVariable = (ParseApplication) getApplicationContext();

        //push service
        PushService.setDefaultPushCallback(this, MainActivity.class);
        //register the device for enabling notification
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

        super.onCreate(savedInstanceState);

		// Determine whether the current user is an anonymous user
		if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
			// If user is anonymous, send the user to LoginSignupActivity.class
			Intent intent = new Intent(MainActivity.this,
					LoginSignupActivity.class);
			startActivity(intent);
			finish();
		} else {
			// If current user is NOT anonymous user
			// Get current user data from Parse.com
			ParseUser currentUser = ParseUser.getCurrentUser();
			if (currentUser != null) {
				// Send logged in users or boar (depends on if he knows or not Korean)
                String id= currentUser.getObjectId();
                globalVariable.setUserId(id);
                Log.d("GLOBALVAR",globalVariable.getUserId().toString());
                if(currentUser.get("USER_TYPE").equals("parent")){
                    Intent intent = new Intent(MainActivity.this, BeginHere.class);
                    intent.putExtra("objectId",id);
                    startActivity(intent);
                }
                if(currentUser.get("USER_TYPE").equals("child")){
                    Intent intent = new Intent(MainActivity.this, BlueSmirfDemo.class);
                    intent.putExtra("objectId",id);
                    startActivity(intent);
                }
				finish();
			} else {
				// Send user to LoginSignupActivity.class
				Intent intent = new Intent(MainActivity.this,
						LoginSignupActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		}

	}


}
