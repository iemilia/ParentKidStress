package com.androidbegin.parselogintutorial;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Emi on 21.06.2015.
 */
public class Breathing extends Fragment {

    LinearLayout layout;
    ParseApplication globalVariable;
    TextView breathing_instruction, breathing_instruction2,mTextField;
    TextView counter;
    Chronometer focus;
    Button start;
    Button stop;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.breathing_guide, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        breathing_instruction = (TextView) v.findViewById(R.id.breath_instruction);
        breathing_instruction2 = (TextView) v.findViewById(R.id.breath_instruction2);
        mTextField=(TextView)v.findViewById(R.id.seconds);
        breathing_instruction.setText("Relax!Practice breathing together with your kid!");
        breathing_instruction2.setText("START now");
        counter = (TextView) v.findViewById(R.id.counter);
        globalVariable = ((BeginHere) getActivity()).getGlobalVar();
        start = (Button) v.findViewById(R.id.start);//breath...
        stop = (Button) v.findViewById(R.id.stop);//breath...

        start.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                start.setVisibility(Button.GONE);
                stop.setVisibility(Button.VISIBLE);
                breathing_instruction2.setText("Breath in");
                new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        mTextField.setText(String.valueOf(millisUntilFinished / 1000));
                    }

                    public void onFinish() {

                        breathing_instruction2.setText(("Pause:)"));
                        new CountDownTimer(3000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                mTextField.setText(String.valueOf(millisUntilFinished / 1000));
                            }

                            public void onFinish() {
                                breathing_instruction2.setText(("Breath out"));
                                new CountDownTimer(5000, 1000) {

                                    public void onTick(long millisUntilFinished) {
                                        mTextField.setText(String.valueOf(millisUntilFinished / 1000));
                                    }

                                    public void onFinish() {
                                    }
                                }.start();
                            }
                        }.start();
                    }
                }.start();

        }
        });
        stop.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                start.setVisibility(Button.VISIBLE);
                stop.setVisibility(Button.GONE);
                breathing_instruction.setText("Relax!Practice breathing together with your kid!");
                breathing_instruction2.setText("START now ");

            }
        });


    }
    class Count extends TimerTask {
        int count=5;
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    while(count!=0)
                    counter.setText("for " + count + " seconds");
                    count--;
                    System.out.println("");
                }
            });
        }
    }


}
