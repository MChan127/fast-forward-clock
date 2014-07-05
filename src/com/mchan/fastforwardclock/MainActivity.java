package com.mchan.fastforwardclock;

import com.mchan.fastforwardclock.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends FragmentActivity {
	// currently selected speed (button)
	// used for button highlighting
	// passed through bundle when activity restarts
	ToggleButton currRateBtn;
	
	// rate in milliseconds
	// passed through bundle when activity restarts
	int rateMilli;
	
	// array of current time values
	int currTime[] = new int[3];
	
	// the clock object
	Clock clock;
	
	// handler receives time values from other thread and 
	// displays them on the clock fragment as a TextView
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// retrieve the time values from the included Bundle object
			currTime[0] = msg.getData().getInt("hour");
			currTime[1] = msg.getData().getInt("minute");
			currTime[2] = msg.getData().getInt("second");
			
			// converts int values to Strings
			// single digit values are preceded with a "0"
		    String hourStr = Integer.toString(currTime[0]);
			String minStr = Integer.toString(currTime[1]);
			String secStr = Integer.toString(currTime[2]);
			
			if (currTime[0] < 10) {
				hourStr = "0" + hourStr;
			}
			if (currTime[1] < 10) {
				minStr = "0" + minStr;
			}
			if (currTime[2] < 10) {
				secStr = "0" + secStr;
			}
			
			// displays the current time
			TextView clockFace = (TextView)findViewById(R.id.clock_face);
			clockFace.setText(hourStr + ":" + minStr + ":" + secStr);
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // set this activity's view to activity_main.xml
        setContentView(R.layout.activity_main);
        
        // if app starting for first time (not restarting)
        if (savedInstanceState == null) {
	        // toggle first button on (for normal clock rate at 1x)
	        currRateBtn = (ToggleButton)findViewById(R.id.speedbtn1x);
	        currRateBtn.setChecked(true);
	        rateMilli = 1000;
	        
	        // create the Clock object (with default now time)
	        // pass handler to clock
	 		// create the thread and pass it the Clock (runnable)
	 		clock = new Clock(handler);
	 		Thread bg_timer = new Thread(clock);
	 		bg_timer.start();
        } else {
        	// set to whatever previously selected rate was
        	rateMilli = savedInstanceState.getInt("rateMilli");
        	if (rateMilli == 1000) {
        		currRateBtn = (ToggleButton)findViewById(R.id.speedbtn1x);
        	} else if (rateMilli == 666) {
        		currRateBtn = (ToggleButton)findViewById(R.id.speedbtn15x);
        	} else if (rateMilli == 500) {
        		currRateBtn = (ToggleButton)findViewById(R.id.speedbtn2x);
        	} else if (rateMilli == 333) {
        		currRateBtn = (ToggleButton)findViewById(R.id.speedbtn3x);
        	}
        	
        	// create clock with previous time and rate
        	clock = new Clock(handler, savedInstanceState.getIntArray("currTime"), rateMilli);
        	Thread bg_timer = new Thread(clock);
	 		bg_timer.start();
        }
    }
    
    // store current configuration in case of orientation change
    // (phone rotation)
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
    	super.onSaveInstanceState(savedInstanceState);
    	
    	// stores rate
    	savedInstanceState.putInt("rateMilli", rateMilli);
    	
    	// stores time array (of hour, min, sec)
    	savedInstanceState.putIntArray("currTime", currTime);
    }
    
    public void resetClock(View view) {
    	clock.resetClock();
    	
    	// reset to normal rate
    	currRateBtn.setChecked(false);
        currRateBtn = (ToggleButton)findViewById(R.id.speedbtn1x);
        currRateBtn.setChecked(true);
        rateMilli = 1000;
    }
	
    public void setRate(View view) {
    	// check if button is already selected
    	if (currRateBtn != (ToggleButton)view) {
    		// set last-selected button to unchecked
    		// set new current selected btn
	    	currRateBtn.setChecked(false);
	    	currRateBtn = (ToggleButton)view;
	    	
	    	// change clock speed based on button pressed
	    	if (currRateBtn == (ToggleButton)findViewById(R.id.speedbtn1x)) {
	    		rateMilli = 1000;
	    		clock.setRate(rateMilli);
	    	} else if (currRateBtn == (ToggleButton)findViewById(R.id.speedbtn15x)) {
	    		rateMilli = 666;
	    		clock.setRate(rateMilli);
	    	} else if (currRateBtn == (ToggleButton)findViewById(R.id.speedbtn2x)){
	    		rateMilli = 500;
	    		clock.setRate(rateMilli);
	    	} else if (currRateBtn == (ToggleButton)findViewById(R.id.speedbtn3x)) {
	    		rateMilli = 333;
	    		clock.setRate(rateMilli);
	    	}
	    	
	    // else if same button, nothing changes
	    // reselect (re-highlight) the button because toggle auto turns it off
    	} else {
    		currRateBtn.setChecked(true);
    	}
    }
}
