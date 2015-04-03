package org.ros.android.android_tutorial_pubsub;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;


//import android.app.FragmentManager;



import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.android_tutorial_pubsub.CardReaderFragment.NfcReaderInputListener;
import org.ros.android.view.RosImageView;
import org.ros.android.view.RosTextView;
import org.ros.android.view.camera.RosCameraPreviewView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.util.Random;

import java.lang.String;

import sensor_msgs.CompressedImage;

public class MainActivity extends RosActivity implements NfcReaderInputListener
{

    private static String TAG = "MainActivity";
    private Context context;

    private RosTextView<std_msgs.String> rosTextView;
    private StdMsgPublisher cmd_publisher_;

    private RosImageView<CompressedImage> image;
    private RosCameraPreviewView rosCameraPreviewView;
    private int cameraId;

    Handler mHandler;
    boolean enable_camera;


    public MainActivity() {
        // The RosActivity constructor configures the notification title and ticker
        // messages.
        super("Elevator Interface", "Elevator Interface");
        Log.i(TAG, "Constructor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        setContentView(R.layout.main);
        enable_camera = false;




        if (savedInstanceState == null) {
            //FragmentManager fragmentManager = getFragmentManager();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            CardReaderFragment cardFragment = new CardReaderFragment();
            fragmentTransaction.add(cardFragment,TAG);
            fragmentTransaction.commit();
        }


        //ROS
        rosTextView = (RosTextView<std_msgs.String>) findViewById(R.id.text);
        rosTextView.setTopicName("/to_tablet");
        rosTextView.setMessageType(std_msgs.String._TYPE);
        rosTextView.setMessageToStringCallable(new MessageCallable<String, std_msgs.String>()
        {
            @Override
            public java.lang.String call(std_msgs.String message)
            {
                String msg = message.getData();
                showToast(msg);

                Log.i(TAG, "callback??");
                showToast("Rcvd: "+msg);

                Intent intent = new Intent("msgs_from_hub");
                intent.putExtra("msg", msg);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                return message.getData();
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("msgs_to_hub"));

        context = this;
    }

    //Receive internal msgs, send to robot
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String msg = intent.getStringExtra("msg");
            Log.i(TAG, "Got message: " + msg);

        try {
            cmd_publisher_.publish(msg);
            showToast("Sent: "+msg);
        }
        catch (NullPointerException e)
        {
            showToast("ROS Msg not sent!");
        }


        }
    };


    public void onRestart(){
        super.onRestart();
        Log.i(TAG, "onReStart");
    }
    public void onStart(){
        super.onStart();
        Log.i(TAG, "onStart");
    }
    public void onStop(){
        super.onStop();
        Log.i(TAG, "onStop");
    }
    public void onResume(){
        super.onResume();
        Log.i(TAG, "onResume");
    }
    public void onPause(){
        super.onPause();
        Log.i(TAG, "onPause");
    }
    public void onDestroy(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }


    public void elevatorButtonClicked(View v){
        int floorNum;

        switch(v.getId()) {
            case R.id.button1:
                floorNum = 1;
                break;
            case R.id.button2:
                floorNum = 2;
                break;
            case R.id.button3:
                floorNum = 3;
                break;
            case R.id.button4:
                floorNum = 4;
                break;
            case R.id.button5:
                floorNum = 5;
                break;
            case R.id.button6:
                floorNum = 6;
                break;
            case R.id.button7:
                floorNum = 7;
                break;
            case R.id.button8:
                floorNum = 8;
                break;
            case R.id.button9:
                floorNum = 9;
                break;
            case R.id.button10:
                floorNum = 10;
                break;
            case R.id.button11:
                floorNum = 11;
                break;
            case R.id.button12:
                floorNum = 12;
                break;
            case R.id.button13:
                floorNum = 13;
                break;
            case R.id.button14:
                floorNum = 14;
                break;
            case R.id.button15:
                floorNum = 15;
                break;
            case R.id.button16:
                floorNum = 16;
                break;
            default:
                floorNum = 0;
                break;
        }


        Intent intent = createElevatorIntent("Guest",floorNum, 4, getRandomElevatorNumber());
        startActivity(intent);
        overridePendingTransition(0,0);

    }

    private int getRandomElevatorNumber()
    {
        Random randomGen = new Random();
        return randomGen.nextInt(6) + 1;
    }

    private Intent createElevatorIntent(String str, int floorNum, int duration, int elevNum)
    {
        Intent intent = new Intent(this, CalledElevatorActivity.class);
        intent.putExtra("name",str);
        intent.putExtra("floor", floorNum);
        intent.putExtra("duration",duration);
        intent.putExtra("elevNum",elevNum);
        return intent;
    }


        private void showToast(String in_string)
        {
        Toast temp_toast = Toast.makeText(this,in_string,Toast.LENGTH_SHORT);
        temp_toast.show();
        }

    private void setInfoText(TextView info_label, String in_string)
    {
        info_label.setText(in_string);
    }

    private void writeIntro()
    {
        final TextView info_label = (TextView) findViewById(R.id.info_textView);
        setInfoText(info_label, "Tap phone\nOR\nEnter floor number");
    }

    @Override
    public void nfcCallback(int floor, String name) {
        Intent intent = createElevatorIntent(name,floor, 3, getRandomElevatorNumber());
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor)
    {
        Log.i(TAG, "init!");

        image = (RosImageView<sensor_msgs.CompressedImage>) findViewById(R.id.image);
        image.setTopicName("/usb_cam/image_raw/compressed");
        image.setMessageType(sensor_msgs.CompressedImage._TYPE);
        image.setMessageToBitmapCallable(new BitmapFromCompressedImage());

        rosCameraPreviewView = (RosCameraPreviewView) findViewById(R.id.ros_camera_preview_view);



        cmd_publisher_= new StdMsgPublisher("rosjava_tutorial_pubsub/std_msg_publisher", "/commands");

        // The user can easily use the selected ROS Hostname in the master chooser
        // activity.
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
        nodeConfiguration.setMasterUri(getMasterUri());

        nodeMainExecutor.execute(cmd_publisher_, nodeConfiguration);
        nodeMainExecutor.execute(rosTextView, nodeConfiguration);
        nodeMainExecutor.execute(image, nodeConfiguration.setNodeName("android/video_view"));

        if(!enable_camera) {
            cameraId = 0;
            rosCameraPreviewView.setCamera(Camera.open(cameraId));
            nodeMainExecutor.execute(rosCameraPreviewView, nodeConfiguration);
        }
    }

/*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int numberOfCameras = Camera.getNumberOfCameras();
            final Toast toast;
            if (numberOfCameras > 1) {
                cameraId = (cameraId + 1) % numberOfCameras;
                rosCameraPreviewView.releaseCamera();
                rosCameraPreviewView.setCamera(Camera.open(cameraId));
                toast = Toast.makeText(this, "Switching cameras.", Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(this, "No alternative cameras to switch to.", Toast.LENGTH_SHORT);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast.show();
                }
            });
        }
        return true;
    }*/


}
