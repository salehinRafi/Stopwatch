package com.salehinrafi.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class activity_stopwatch extends AppCompatActivity {

    //        Use seconds to records the number of seconds
    private int seconds = 0;
    //        Use running to records whether the stopwatch is running(true) or not running(false)
    private boolean running;
    //        Added to record whether the stopwatch was running before the onStop() method
    private boolean wasrunning;

    @Override
    /*onCreate() method will always take one parameter, a Bundle.
    * If activity create from scratch, Bundle parameter will be null.
    * Else, it will take value from Bundle object used by onSavedInstanceState()*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

//        Check if parameter Bundle is null or not.
        if (savedInstanceState != null) {
//          Retrieve tha values based on keys from Bundle object from onSaveInstanceState method.
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasrunning = savedInstanceState.getBoolean("wasrunning");
        }
        runTimer();
    }

    /*Android will destroy the activity when we rotate the screen because, Android sees the device configuration is change
    * (screen size, screen orientation, or configuration made by user). Android need to know
    * which configuration need to use to start the activity. When the configuration is changed, Android
    * need to update the UI, thus destroy current activity and re-create back. So, we need to save current state so that
    * the activity can re-create itself in the same state.*/

    @Override
    /*this method will always be called before onDestroy
    * Implement this method to save our data.*/
    public void onSaveInstanceState(Bundle saveInstanceState) {
//      Put value into string keys so that Bundle parameter has a value when activity is re-create back.
        /*bundle.get*("name",value)
        * where bundle is the name of the Bundle, * is the type of value we want to save,
        * name and value is the name & value we want to save*/
        saveInstanceState.putInt("seconds", seconds);
        saveInstanceState.putBoolean("running", running);
        saveInstanceState.putBoolean("wasrunning", wasrunning);
    }


//    @Override
    /*onStop() get called when activity has stopped being visible to the user. May cause :-
    1. by another activity appeared on top of your activity
    2. your activity is going to be destroyed. onSaveInstanceState always get called before onStop() */
//    protected void onStop() {
//        super.onStop();
//      //Record whether the stopwatch was running
//        wasrunning = running;
//        running = false;
//    }

//    @Override
    /*onStart() got called when activity becomes visible to the user*/
//    protected void onStart() {
//        super.onStart();
//        if (wasrunning) {
//            running = true;
//        }
//    }

    @Override
//    Get called when the activity is visible but another
//    activity has the focus(Eg. activity Slide down to open wifi)
//    Pause the stopwatch
    protected void onPause() {
        super.onPause();
        wasrunning = running;
        running = false;
    }

    @Override
//    Get called IMMEDIATELY before activity react with the user.
//    Resume stopwatch again if it was running previously
    protected void onResume() {
        super.onResume();
        if (wasrunning) {
            running = true;
        }
    }

    public void onClickStart(View view) {
//        Start the stopwatch running
        running = true;
    }

    public void onClickStop(View view) {
//        Stop the stopwatch running
        running = false;
    }

    public void onClickReset(View view) {
//        Stop the stopwatch running and set the seconds to 0;
        running = false;
        seconds = 0;
    }

    //    Sets the number of seconds on the timer
    private void runTimer() {
//        Get TextView reference
        final TextView timeView = (TextView) findViewById(R.id.time_view);

        /*We need app to continue looping and update textView every seconds (running continuously in background).
        * Unfortunately, Android only allow main thread ONLY to update the UI, else it will throw exception
        * 'CalledFromWrongThreadException.' Solution -> use 'Handler'.*/
        /*Handler:-
        * Android class to schedule code that should be run at some time in future.
        * Has post() and postDelayed() method*/

//        Create a new Handler
        final Handler handler = new Handler();
        /*Calling the post() method, passing a new Runnable. The method processes code without delay,
        * so the code in Runnable will run almost immediately*/
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
//                Format the seconds into hours:minutes:seconds
                String time = String.format("%d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);
//                If running variable is true, increment the seconds by 1
                if (running) {
                    seconds++;
                }
//                post the code again with delayed of 1 seconds.
                handler.postDelayed(this, 1000);
            }
        });
    }
}
