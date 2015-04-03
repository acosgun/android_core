package org.ros.android.android_tutorial_pubsub;



import android.hardware.Camera;


//import android.app.FragmentManager;



import android.os.Bundle;
import android.os.Handler;
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

//import std_msgs.*;
//import std_msgs.String;
import java.lang.String;

import sensor_msgs.CompressedImage;

public class MainActivity extends RosActivity implements NfcReaderInputListener
{

    private static String TAG = "MainActivity";

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);

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


        writeIntro();

        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);

        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);
        Button button7 = (Button) findViewById(R.id.button7);
        Button button8 = (Button) findViewById(R.id.button8);

        Button button9 = (Button) findViewById(R.id.button9);
        Button button10 = (Button) findViewById(R.id.button10);
        Button button11 = (Button) findViewById(R.id.button11);
        Button button12 = (Button) findViewById(R.id.button12);

        Button button13 = (Button) findViewById(R.id.button13);
        Button button14 = (Button) findViewById(R.id.button14);
        Button button15 = (Button) findViewById(R.id.button15);
        Button button16 = (Button) findViewById(R.id.button16);




        View.OnClickListener buttons_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                try {
                    mHandler.removeCallbacks(mRunnable);
                }
                catch (NullPointerException nl)
                {
                 //Log.d(TAG, "NullPointerExceptionnn!");
                }


                writeTextRandomPerson(floorNum);
                manipulateButtons(View.INVISIBLE);
                useTimerHandler();
                cmd_publisher_.publish("g");

            }
        };

        button1.setOnClickListener(buttons_listener);
        button2.setOnClickListener(buttons_listener);
        button3.setOnClickListener(buttons_listener);
        button4.setOnClickListener(buttons_listener);

        button5.setOnClickListener(buttons_listener);
        button6.setOnClickListener(buttons_listener);
        button7.setOnClickListener(buttons_listener);
        button8.setOnClickListener(buttons_listener);

        button9.setOnClickListener(buttons_listener);
        button10.setOnClickListener(buttons_listener);
        button11.setOnClickListener(buttons_listener);
        button12.setOnClickListener(buttons_listener);

        button13.setOnClickListener(buttons_listener);
        button14.setOnClickListener(buttons_listener);
        button15.setOnClickListener(buttons_listener);
        button16.setOnClickListener(buttons_listener);

        //ROS
        rosTextView = (RosTextView<std_msgs.String>) findViewById(R.id.text);
        rosTextView.setTopicName("/to_tablet");
        rosTextView.setMessageType(std_msgs.String._TYPE);
        rosTextView.setMessageToStringCallable(new MessageCallable<String, std_msgs.String>()
        {
            @Override
            public java.lang.String call(std_msgs.String message)
            {
                Log.d(TAG, "callback??");
                return message.getData();
            }
        });

        image = (RosImageView<sensor_msgs.CompressedImage>) findViewById(R.id.image);
        image.setTopicName("/usb_cam/image_raw/compressed");
        image.setMessageType(sensor_msgs.CompressedImage._TYPE);
        image.setMessageToBitmapCallable(new BitmapFromCompressedImage());

        rosCameraPreviewView = (RosCameraPreviewView) findViewById(R.id.ros_camera_preview_view);

    }

    private void setInfoText(TextView info_label, String in_string)
    {
        info_label.setText(in_string);
    }

    private void showToast(String in_string)
    {
        Toast temp_toast = Toast.makeText(this,in_string,Toast.LENGTH_SHORT);
        temp_toast.show();
    }




    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void useTimerHandler() {


        //mHandler.removeCallbacks(mRunnable);
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 3000);
    }

    private Runnable mRunnable = new Runnable() {

        @Override
        public void run()
        {
            manipulateButtons(View.VISIBLE);
            final TextView info_label = (TextView) findViewById(R.id.info_textView);
            info_label.setTextSize(TypedValue.COMPLEX_UNIT_DIP,50);
            writeIntro();
        }
    };

    private void manipulateButtons(int visibility)
    {
        View b = findViewById(R.id.button1);
        b.setVisibility(visibility);

        b=findViewById(R.id.button2);
        b.setVisibility(visibility);

            b=findViewById(R.id.button3);
            b.setVisibility(visibility);

            b=findViewById(R.id.button4);
            b.setVisibility(visibility);

            b=findViewById(R.id.button5);
            b.setVisibility(visibility);

            b=findViewById(R.id.button6);
            b.setVisibility(visibility);

            b=findViewById(R.id.button7);
            b.setVisibility(visibility);

            b=findViewById(R.id.button8);
            b.setVisibility(visibility);

            b=findViewById(R.id.button9);
            b.setVisibility(visibility);

            b=findViewById(R.id.button10);
            b.setVisibility(visibility);

            b=findViewById(R.id.button11);
            b.setVisibility(visibility);

            b=findViewById(R.id.button12);
            b.setVisibility(visibility);

            b=findViewById(R.id.button13);
            b.setVisibility(visibility);

            b=findViewById(R.id.button14);
            b.setVisibility(visibility);

            b=findViewById(R.id.button15);
            b.setVisibility(visibility);

            b=findViewById(R.id.button16);
            b.setVisibility(visibility);


    }


    private void writeTextKnownPerson(int floorNum, String name)
    {
        Random randomGen = new Random();
        int elevNum = randomGen.nextInt(6) + 1;
        this.writeText(name, floorNum, elevNum);
    }

    private void writeTextRandomPerson(int floorNum)
    {
        Random randomGen = new Random();
        int elevNum = randomGen.nextInt(6) + 1;
        this.writeText("Guest", floorNum, elevNum);
    }
    private void writeText(String name, int floorNum, int elevNum)
    {
        final TextView info_label = (TextView) findViewById(R.id.info_textView);
        info_label.setTextSize(TypedValue.COMPLEX_UNIT_DIP,75);
        setInfoText(info_label, "Welcome,\n" + name + "!\n\nEntered Floor:\n" + floorNum + "\n\nTake elevator:\n" + elevNum);
    }

    private void writeIntro()
    {
        final TextView info_label = (TextView) findViewById(R.id.info_textView);
        setInfoText(info_label, "To call the elevator\n\nTap your phone\nOR\nEnter floor number");
    }


    @Override
    public void nfcCallback(int floor, String name) {
        Log.d(TAG,"floor: "+floor);
        Log.d(TAG,"name: "+name);


        final int final_floor = floor;
        final String final_name = name;



        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                writeTextKnownPerson(final_floor,final_name);
                manipulateButtons(View.INVISIBLE);
                useTimerHandler();
            }
        });

    }




    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        Log.d(TAG, "init");

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
    }


}
