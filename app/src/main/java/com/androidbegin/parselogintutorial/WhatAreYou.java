package com.androidbegin.parselogintutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Emi on 09.06.2015.
 */
public class WhatAreYou extends Activity {

    Button parent;
    Button child;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.parent_or_child);
        final String usernametxt=getIntent().getStringExtra("usernametxt");
        final String passwordtxt=getIntent().getStringExtra("passwordtxt");

    parent = (Button) findViewById(R.id.parent);

    // Sign up Button Click Listener
    parent.setOnClickListener(new View.OnClickListener() {

        public void onClick(View arg0) {
            Intent intent = new Intent(
                    WhatAreYou.this,
                    SignUp.class);
            intent.putExtra("usernametxt",usernametxt);
            intent.putExtra("passwordtxt",passwordtxt);
            startActivity(intent);
        }
    });

    child = (Button) findViewById(R.id.child);

        // Sign up Button Click Listener
    child.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(
                        WhatAreYou.this,
                        SignUpChild.class);
                intent.putExtra("usernametxt",usernametxt);
                intent.putExtra("passwordtxt",passwordtxt);
                startActivity(intent);
            }
        });
    }
}