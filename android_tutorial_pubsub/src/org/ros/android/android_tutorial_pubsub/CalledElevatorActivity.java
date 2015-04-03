package org.ros.android.android_tutorial_pubsub;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.lang.String;


/**
 * Created by acosgun on 4/3/15.
 */
public class CalledElevatorActivity extends Activity  {
    private static final String TAG = "CalledElevatorActivity";
    static boolean active = false;
    static Handler mHandler;




    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elevator_called);

        LocalBroadcastManager.getInstance(this).registerReceiver(rosUniversalCallback,
                new IntentFilter("msgs_from_hub"));

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        int floor = intent.getIntExtra("floor",0);
        int elevNum = intent.getIntExtra("elevNum",0);
        int duration = intent.getIntExtra("duration",3);
        boolean showGuide = intent.getBooleanExtra("showGuide", true);

        String str = getElevatorInfoString(name, floor, elevNum);

        final TextView info_textview = (TextView) findViewById(R.id.elevatorInfoTextView);
        info_textview.setText(str);

        if(duration>0) {
            mHandler = new Handler();
            mHandler.postDelayed(mRunnable, duration * 1000);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }
    @Override
    protected void onDestroy()
    {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(rosUniversalCallback);
        super.onDestroy();
    }

    private String getElevatorInfoString(String name, int floorNum, int elevNum)
    {
        return "Welcome,\n" + name + "!\n\nEntered Floor:\n" + floorNum + "\n\nTake elevator:\n" + elevNum;
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run()
        {
        finish();
        }
    };

    private BroadcastReceiver rosUniversalCallback = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(!active)
                return;
            String message = intent.getStringExtra("msg");
            Log.i(TAG, "Got message: " + message);
        }
    };

    public void guideButtonClicked(View v){
        Log.i(TAG,"guideButtonClicked:");

        Intent intent = new Intent("msgs_to_hub");
        intent.putExtra("msg", "g");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}

