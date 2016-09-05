package com.androidbegin.parselogintutorial;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Emi on 12.06.2015.
 */
public class StatusMood  extends Fragment {
    LinearLayout layout;
    ParseApplication globalVariable;
    TextView moodtxt;
    TextView text_connection;
    ImageView moodimg;
    private Handler mHandler = new Handler();
    boolean connect=false;
    String status_stress="excited";
    String kidId;
    int passings=0;
    final int[] ICONS = new int[] {
            R.drawable.status,
            R.drawable.together,
            R.drawable.graphic,
            R.drawable.menu_settings
    };
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.status_mood, container, false);

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        globalVariable=((BeginHere)getActivity()).getGlobalVar();
        String mood[]={"relaxed","stressed","extra-stressed","excited"};
        moodtxt=(TextView) v.findViewById(R.id.text_mood);
        text_connection=(TextView) v.findViewById(R.id.text_connection);
        layout=(LinearLayout) v.findViewById(R.id.layout_status);
        moodimg=(ImageView) v.findViewById(R.id.image_mood);
        final int[] src_images = new int[] {
                R.drawable.relaxed,
                R.drawable.stressup,
                R.drawable.stressed,
                R.drawable.excited
        };
        String parinteId=globalVariable.getUserId();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(parinteId, new GetCallback<ParseUser>() {

            @Override
            public void done(ParseUser parseUser, com.parse.ParseException e) {

                if (e == null) {
                    Log.d("User",parseUser.getUsername());
                    //object will be User
                    kidId= parseUser.getString("USER_PAIR");
                    Log.d("kidID",kidId);
                } else {
                    //something went wrong;
                    Log.e("TAG", e.getMessage());
                }
            }
        });

        //retrieve status connection child
        connect=get_status_connection(kidId);
        Log.d("connected",Boolean.toString(connect));
        //retrieve status child
        status_stress=get_status_stress(kidId);
        Log.d(status_stress,status_stress);
        int i=1;
        if (status_stress.equals("moderate")) {//stressup
            layout.setBackgroundColor(getResources().getColor(R.color.stressup));
            i=1;
        }

        else if (status_stress.equals("normal")) {//relaxed
            layout.setBackgroundColor(getResources().getColor(R.color.relaxed));
            i=0;
        }
        else if (status_stress.equals("stressed")) {//extrastressed
            layout.setBackgroundColor(getResources().getColor(R.color.stressed));
            i=2;
        }
        else if (status_stress.equals("excited")) {//excited
            layout.setBackgroundColor(getResources().getColor(R.color.excited));
            i=3;
        }

        moodimg.setImageResource(src_images[i]);
        //moodimg.setImageURI(Uri.parse("R.drawable." + src_images[1]));
        if(mood[i].equals("relaxed")||mood[i].equals("extra-stressed"))
        moodtxt.setText(mood[i]);
        else
            moodtxt.setText("...");
        Timer myTimer = new Timer();
        Refresh refresh = new Refresh();
        //        public void schedule (TimerTask task, long delay, long period)
//        Schedule a task for repeated fixed-delay execution after a specific delay.
//
//        Parameters
//        task  the task to schedule.
//        delay  amount of time in milliseconds before first execution.
//        period  amount of time in milliseconds between subsequent executions.

        myTimer.schedule(refresh, 0, 60000);//once every minute

    }
    class Refresh extends TimerTask {
        public void run() {
            passings++;
            Log.d("pass",Integer.toString(passings));
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    // ERROR
                    String parinteId=globalVariable.getUserId();


                    //retrieve status connection child
                    boolean connection;
                    connection=get_status_connection(kidId);

                    Log.d("connected",Boolean.toString(connect));
                    //retrieve status child
                    if(connection==true){
                        text_connection.setText("Your kid is connected");
                    }
                    else{
                        text_connection.setText("Your kid is disconnected");
                    }
                    if(connection==true){
                    String status_stress_kid;
                    status_stress_kid=get_status_stress(kidId);
                    Log.d("status_stress",status_stress_kid);

                    int i=1;
                    if (status_stress_kid.equals("moderate")) {//stressup
                        layout.setBackgroundColor(getResources().getColor(R.color.stressup));
                        i=1;
                    }

                    else if (status_stress_kid.equals("normal")) {//relaxed
                        layout.setBackgroundColor(getResources().getColor(R.color.relaxed));
                        i=0;
                    }
                    else if (status_stress_kid.equals("stressed")) {//extrastressed
                        layout.setBackgroundColor(getResources().getColor(R.color.stressed));
                        i=2;
                    }
                    else if (status_stress_kid.equals("excited")) {//excited
                        layout.setBackgroundColor(getResources().getColor(R.color.excited));
                        i=3;
                    }
                    String mood[] = {"relaxed", "stressed", "extra-stressed", "excited"};
                    final int[] src_images = new int[]{
                            R.drawable.relaxed,
                            R.drawable.stressup,
                            R.drawable.stressed,
                            R.drawable.excited
                    };
                    moodimg.setImageResource(src_images[i]);
                    //moodimg.setImageURI(Uri.parse("R.drawable." + src_images[1]));
                    if(mood[i].equals("relaxed")||mood[i].equals("extra-stressed"))
                        moodtxt.setText(mood[i]);
                    else
                        moodtxt.setText("...");

                    System.out.println("");
                }}
            });
        }
    }
   public boolean get_status_connection(String kidId){
       ParseQuery<ParseUser> query = ParseUser.getQuery();
           query.getInBackground(kidId, new GetCallback<ParseUser>() {

               @Override
               public void done(ParseUser parseUser, com.parse.ParseException e) {

                   if (e == null) {
                       Log.d("User",parseUser.getUsername());
                       //object will be User
                       connect= parseUser.getBoolean("connected");

                   } else {
                       //something went wrong;
                       Log.e("TAG", e.getMessage());
                   }
               }
       });
       return connect;
    }

    public String get_status_stress(String kidId){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(kidId, new GetCallback<ParseUser>() {

            @Override
            public void done(ParseUser parseUser, com.parse.ParseException e) {

                if (e == null) {
                    Log.d("User",parseUser.getUsername());
                    //object will be User
                    status_stress= parseUser.getString("latest_stress_status");

                } else {
                    //something went wrong;
                    Log.e("TAG", e.getMessage());
                }
            }
        });
        return status_stress;
    }

}
