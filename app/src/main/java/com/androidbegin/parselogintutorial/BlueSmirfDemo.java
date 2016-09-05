package com.androidbegin.parselogintutorial;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.androidbegin.parselogintutorial.BlueSmirfSPP;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class BlueSmirfDemo extends Activity implements Runnable, Handler.Callback, AdapterView.OnItemSelectedListener
{
    private static final String TAG = "BlueSmirfDemo";
    protected PowerManager.WakeLock mWakeLock;
    boolean connected=false;

    /* This code together with the one in onDestroy()
    /* will make the screen be always on until this Activity gets destroyed. */


    // app state
    private BlueSmirfSPP      mSPP;
    private boolean           mIsThreadRunning;
    private String            mBluetoothAddress;
    private ArrayList<String> mArrayListBluetoothAddress;
    private List<String> mReceived;

    //settings
    private InputStream inStream = null;
    Handler handler = new Handler();
    byte delimiter = 10;
    boolean stopWorker = false;
    int readBufferPosition = 0;
    byte[] readBuffer = new byte[1024];

    // UI
    TextView view_BMP;
    TextView view_BMP_mean;
    TextView view_IBI;
    TextView view_IBI_mean;
    TextView Result;
    TextView view_std_HRV;
    TextView stress_level;


    //private TextView     mTextViewStatus;
    private TextView     mTextViewStatus;
    private Spinner mSpinnerDevices;
    private Dialog dialog;
    private ArrayAdapter mArrayAdapterDevices;
    private Handler      mHandler;

    // Arduino state

    private int receive_size;
    private int BPM;//pulse
    private int IBI;//pulse interval

    private int BPM_total = 0;
    private int IBI_total_interval = 0;

    private int BPM_total_counts = 0;
    private int IBI_total_counts_interval = 0;

    private ArrayList<Integer> BPM_array = new ArrayList<Integer>();
    private ArrayList<Integer> IBI_interval_array = new ArrayList<Integer>();
    private ArrayList<Double> HRV = new ArrayList<Double>();

    private float current_mean_BPM;
    private float current_mean_IBI;

    private int count_data_collected = 0;
    private int count_data_collected_total=0;
    private int samples_number = 0;
    private int received_data_counter = 0; // used to clear textview of received data
    private int count_data_collected_each=0;
    private String class_label = "";



   // DataSamples dataSamples;
    //DtaGrabber dataGrabber;

    // features
    private int pulse_max = 0;
    private int pulse_min = 0;
    private int pulse_interval_max = 0;
    private int pulse_interval_min = 0;



    public BlueSmirfDemo()
    {
        mIsThreadRunning           = false;
        mBluetoothAddress          = null;
        mSPP                       = new BlueSmirfSPP();
        BPM                  = 0;
        IBI                  = 0;
        BPM_total=0;
        BPM_total_counts=0;
        IBI_total_interval=0;
        IBI_total_counts_interval=0;
        mArrayListBluetoothAddress = new ArrayList<String>();
    }

    //data current user
     ParseApplication globalVariable;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //child is connected, using the app

        globalVariable = (ParseApplication) getApplicationContext();


        // initialize UI
        setContentView(R.layout.bluetooth_setconnection);

       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mTextViewStatus         = (TextView) findViewById(R.id.ID_STATUS);
        ArrayList<String> items = new ArrayList<String>();
        //dialog = new Dialog(this);
       // dialog.setContentView(R.layout.itinerary_add_item_page);

       // Spinner airlineChoice = (Spinner) dialog.findViewById(R.id.airlineSpinner);
        mSpinnerDevices         = (Spinner) findViewById(R.id.ID_PAIRED_DEVICES);
        mArrayAdapterDevices    = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        mHandler                = new Handler(this);
        mSpinnerDevices.setOnItemSelectedListener(this);
        mArrayAdapterDevices.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDevices.setAdapter(mArrayAdapterDevices);
        view_BMP=(TextView) findViewById(R.id.view_BMP);
        view_BMP_mean=(TextView) findViewById(R.id.view_BMP_mean);
        view_IBI=(TextView) findViewById(R.id.view_IBI);
        view_std_HRV=(TextView) findViewById(R.id.view_std_HRV);
        view_IBI_mean=(TextView) findViewById(R.id.view_IBI_mean);
        stress_level= (TextView) findViewById(R.id.stress_level);
        Result=(TextView)findViewById(R.id.Result);
        //this.dataSamples = new DataSamples();
        //this.dataGrabber = new DtaGrabber(window_size, dataSamples);

        //this.dataSamples = new DataSamples();
        //this.dataGrabber = new DtaGrabber(window_size, dataSamples);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // update the paired device(s)
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        mArrayAdapterDevices.clear();
        mArrayListBluetoothAddress.clear();
        if(devices.size() > 0)
        {
            for(BluetoothDevice device : devices)
            {
                mArrayAdapterDevices.add(device.getName());
                mArrayListBluetoothAddress.add(device.getAddress());
            }

            // request that the user selects a device
            if(mBluetoothAddress == null)
            {
               // mSpinnerDevices.performClick();
            }
        }
        else
        {
            mBluetoothAddress = null;
        }

        UpdateUI();
    }

    @Override
    protected void onPause()
    {
        update_connectivity(false);
        mSPP.disconnect();
        close2();

        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

	/*
	 * Spinner callback
	 */

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
        mBluetoothAddress = mArrayListBluetoothAddress.get(pos);
    }

    public void onNothingSelected(AdapterView<?> parent)
    {
        mBluetoothAddress = null;
    }

	/*
	 * buttons
	 */

    public void onBluetoothSettings(View view)
    {
        Intent i = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(i);
    }

    public void onToggleLED(View view)
    {
        if(mSPP.isConnected())
        {

        }
    }

    public void onConnectLink(View view)
    {
        if(mIsThreadRunning == false)
        {
            mIsThreadRunning = true;
            UpdateUI();
            Thread t = new Thread(this);
            t.start();
        }
        else {
            Thread.currentThread().interrupt();
            UpdateUI();
            Thread t = new Thread(this);
            t.start();

        }
    }

    public void onDisconnectLink(View view)
    {
        update_connectivity(false);
        mSPP.disconnect();
        close2();
        mIsThreadRunning=false;
        UpdateUI();

    }

	/*
	 * main loop
	 */

    public void run() {
        int counter = 0;
        char c='\0';
        char d='\0';
        String s = "";
        int x = 0;
        count_data_collected = 0;
        samples_number = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Looper.prepare();
        Log.d("mSPP_state", Boolean.toString(mSPP.isConnected()));
        if (mSPP.isConnected()) {
            mSPP.disconnect();
            update_connectivity(false);
        }



        mSPP.connect(mBluetoothAddress);


        int bytes;
        Log.v("MR", "start listening....");

        // Keep listening to the InputStream while connected

        beginListenForData();
    }


	/*
	 * update UI
	 */



    public boolean handleMessage (Message msg)
    {
        // received update request from Bluetooth IO thread
        UpdateUI();
        return true;
    }

    public void beginListenForData()   {
        inStream = mSPP.mInputStream;



        update_connectivity(true);
        //DataInstanceList dlHeart;
        Thread workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    // Reusable buffer
                    //DataSample pulse, pulse_interval ;
                    // BPM_array
                    connected=true;
                    try {
                        if(mSPP.isError())
                        {
                            connected=false;
                            update_connectivity(false);
                            mSPP.disconnect();


                        }

                        if (inStream == null) {
                            stopWorker = true;
                            connected=false;



                        } else {
                            int bytesAvailable = inStream.available();

                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                inStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        //add data in arrays
                                        Log.d("tip data",data.substring(0,1));
                                        Log.d("tip data",data.substring(0));


                                        if(data.substring(0,1).equals("B")) {
                                            BPM_array.add(Integer.parseInt(data.substring(1).trim()));
                                            BPM=Integer.parseInt(data.substring(1).trim());
                                            current_mean_BPM=compute_mean(BPM_array);
                                        }
                                        if(data.substring(0,1).equals("Q")){
                                            IBI_interval_array.add(Integer.parseInt(data.substring(1).trim()));
                                            IBI=Integer.parseInt(data.substring(1).trim());
                                            current_mean_IBI=compute_mean(IBI_interval_array);
                                        }


                                        //label_stress=decide_label();
                                        count_data_collected_total++;
                                        Log.d("count_data_total",Integer.toString(count_data_collected_total));
                                        count_data_collected_each++;
                                        Log.d("count_data_each",Integer.toString(count_data_collected_each));
                                        Log.d("count_samples",Integer.toString(samples_number));
                                        //if 1 minute passed then compute standard deviation as HRV of IBI
                                        if(count_data_collected_each==180){
                                            samples_number++;
                                            count_data_collected_each=0;
                                            if(samples_number==1)
                                                HRV.add(standard_deviation_IBI(IBI_interval_array, count_data_collected_total - 180));
                                            else
                                                HRV.add(standard_deviation_IBI(IBI_interval_array, count_data_collected_total/2 - 90));
                                        }
                                        int age=10;
                                        String sex="M";
                                        final String stress=determine_stress(age,sex,HRV,samples_number);
                                        readBufferPosition = 0;
                                        handler.post(new Runnable() {
                                            public void run() {
                                                view_BMP.setText(Integer.toString(BPM));
                                                view_IBI.setText(Integer.toString(IBI));
                                                view_BMP_mean.setText(Float.toString(current_mean_BPM));
                                                view_IBI_mean.setText(Float.toString(current_mean_IBI));

                                                if(HRV!=null&&count_data_collected_each==0)
                                                    view_std_HRV.setText(Double.toString(HRV.get(HRV.size() - 1)));
                                                stress_level.setText(stress);

                                                //update_stress_online(stress);
                                                final Handler handler = new Handler();
                                                Timer timer = new Timer();
                                                TimerTask doAsynchronousTask = new TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        handler.post(new Runnable() {
                                                            public void run() {
                                                                try {
                                                                    update_stress_online(stress);


                                                                } catch (Exception e) {
                                                                }
                                                            }
                                                        });
                                                    }
                                                };
                                                timer.schedule(doAsynchronousTask, 0, 1000); //execute in every 10 m
                                                if (Result.getText().toString().equals("..")) {
                                                    Result.setText(data);
                                                } else {
                                                    Result.append("\n" + data);
                                                }

	                                        	/* You also can use Result.setText(data); it won't display multilines
	                                        	*/

                                            }
                                        });
                                        mHandler.sendEmptyMessage(0);

                                    } else {
                                        readBuffer[readBufferPosition++] = b;

                                    }
                                }
                            }
                        }}
                    catch(IOException ex)
                    {
                        stopWorker = true;
                        connected=false;

                    }

                }
                mHandler.sendEmptyMessage(0);
            }
        });
        if (inStream == null) {
            stopWorker = true;
        }
        else
            workerThread.start();
    }

    public void close2(){
        try { inStream.close();     } catch(Exception e) { }
        inStream      = null;

    }
    private void UpdateUI()
    {

        if(mSPP.isConnected())
        {
            connected=true;
            mTextViewStatus.setText("connected to " + mSPP.getBluetoothAddress() );


        }
        else if(mIsThreadRunning)
        {
            mTextViewStatus.setText("connecting to " + mBluetoothAddress);
        }
        else
        {
            mTextViewStatus.setText("disconnected");
            connected=false;
        }
    }

    // compute different features
    public float compute_mean( ArrayList<Integer> feature){
        float sum = 0;
        for(int i = 0; i < feature.size(); i++)
            sum=sum+feature.get(i);
        Log.d("suma_features", Float.toString(sum));
        Log.d("size_features",Float.toString(feature.size()));
        return sum / feature.size();
    }

    public int compute_max( ArrayList<Integer> feature){
        int max=0;
        for(int i = 0; i < feature.size(); i++)
            if(feature.get(i)>max)
                max=feature.get(i);
        return max;
    }

    public int compute_min( ArrayList<Integer> feature){
        int min=0;
        for(int i = 0; i < feature.size(); i++)
            if(feature.get(i)<min)
                min=feature.get(i);
        return min;
    }

    public float relation( ArrayList<Integer> feature){
        int min=0;
        for(int i = 0; i < feature.size(); i++)
            if(feature.get(i)<min)
                min=feature.get(i);
        return min;
    }

    public String determine_stress(int age, String sex,ArrayList<Double>HRV,int samples_number){
        String stress1= "normal";
        String stress2= "normal";
        if(age>=3&&age<=4) {
            if (current_mean_BPM < 120 && current_mean_BPM > 80)
                stress1="normal";
            else
            if (current_mean_BPM > 120)
                stress1="stressed";

        }
        if(age>=5&&age<=6) {
            if (current_mean_BPM < 115 && current_mean_BPM > 75)
                stress1="normal";
            else
            if (current_mean_BPM > 115)
                stress1="stressed";

        }
        if(age>=7&&age<=9) {
            if (current_mean_BPM < 110 && current_mean_BPM > 70)
                stress1="normal";
            else
            if (current_mean_BPM > 110)
                stress1="stressed";

        }
        if(age>=10) {
            if (current_mean_BPM < 100 && current_mean_BPM > 60)
                stress1="normal";
            else
            if (current_mean_BPM > 100)
                stress1="stressed";

        }
        if(current_mean_IBI<104.1&&current_mean_IBI>81.7)
            stress2="normal";
        else
        if(current_mean_IBI<81.7)
            stress2="stressed";
        if(stress1!=stress2)
            return stress1;
        return stress1;
    }

    public double standard_deviation_IBI( ArrayList<Integer> feature,int index_start) {

        double sum = 0;
        double sq_sum = 0;
        for(int i = index_start; i < index_start+89; i++) {
            sum += feature.get(i);
            sq_sum += feature.get(i) * feature.get(i);
        }
        double mean = sum / 90;
        double variance = sq_sum / 90 - mean * mean;
        return Math.sqrt(variance);
    }

    public void update_connectivity(final boolean connected) {
        //write status of connect/disconnect of child
        Log.d("set_connection",Boolean.toString(connected));


        final String ObjectId = globalVariable.getUserId();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(ObjectId, new GetCallback<ParseUser>() {

            @Override
            public void done(ParseUser parseUser, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("User",parseUser.getUsername());
                    //object will be User
                    parseUser.put("connected", connected);
                    //globalVariable.setUserName(currentUser.getUsername());
                    parseUser.saveInBackground();
                } else {
                    //something went wrong;
                    Log.e("TAG", e.getMessage());
                }
            }
        });
    }

    public void update_stress_online(final String stress) {
        //write status of connect/disconnect of child
        Log.d("updata_stress",stress);


        final String ObjectId = globalVariable.getUserId();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(ObjectId, new GetCallback<ParseUser>() {

            @Override
            public void done(ParseUser parseUser, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("User",parseUser.getUsername());
                    //object will be User
                    parseUser.put("latest_stress_status", stress);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    parseUser.put("latest_date",dateFormat.format(date));
                    //globalVariable.setUserName(currentUser.getUsername());
                    parseUser.saveInBackground(new SaveCallback() {

                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e != null) {
                                // Saved successfully
                                Log.d("SUCCCESSS","faefe");
                            } else {
                                // ParseException
                                Log.d("OH NOOOOOO","faefe");
                            }
                        }
                    });
                } else {
                    //something went wrong;
                    Log.e("TAG", e.getMessage());
                }
            }
        });
    }
/*
    public String decide_label(int age,int sex, ){

    }
*/

    }
