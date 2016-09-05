package com.androidbegin.parselogintutorial;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

import android.app.Application;
import android.util.Log;

//import model.Question;

public class ParseApplication extends Application {
    public String UserId;
    public String UserName;

    public String getUserId() {

        return UserId;
    }

    public void setUserId(String aUserId) {

        UserId = aUserId;

    }

    public String getUserName() {

        return UserName;
    }

    public void setUserName(String aUserName) {

        UserName = aUserName;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        //ParseObject.registerSubclass(Child.class);

        // Add your initialization code here
        Parse.initialize(this, "t06iom6t2FXzsBUUo77vXd9NAY3KBGT1IGZXzjSN", "0NctlQk4se2ug2CgBXhT4DOU2tM1oynm2PvY1SfD");




        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
 
        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);
 
        ParseACL.setDefaultACL(defaultACL, true);


    }
 
}