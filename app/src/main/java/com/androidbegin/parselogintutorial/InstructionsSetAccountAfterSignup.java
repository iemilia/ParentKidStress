package com.androidbegin.parselogintutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Emi on 10.06.2015.
 */
public class InstructionsSetAccountAfterSignup extends Activity {
    Button done;
    TextView cod_parinte;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseApplication globalVariable= ((ParseApplication) getApplicationContext());
        getActionBar().hide();
        String cod=globalVariable.getUserId();
        setContentView(R.layout.instructions_setkid);
        done = (Button) findViewById(R.id.done);
        cod_parinte = (TextView) findViewById(R.id.code);
        cod_parinte.setText(cod);

        done.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent intent= new Intent(getApplicationContext(),BeginHere.class);
                //String id= user.getObjectId();
                //Log.d("iiiiiiiiiiiiiiiiiidddd", id);
                //globalVariable.setUserId(id);
                //intent.putExtra("objectId",id);
                startActivity(intent);
            }
        });

    }
}
