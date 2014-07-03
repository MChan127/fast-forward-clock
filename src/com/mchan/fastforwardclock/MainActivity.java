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
	// this is only for button highlighting
	ToggleButton currRateBtn;
	
	// the clock object
	Clock clock;
	// handler receives time values from other thread and 
	// displays them on the clock fragment as a TextView
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// retrieve the time values from the included Bundle object
			int currHour = msg.getData().getInt("hour");
			int currMin = msg.getData().getInt("minute");
			int currSec = msg.getData().getInt("second");
			
			// converts int values to Strings
			// single digit values are preceded with a "0"
			String hourStr = Integer.toString(currHour);
			String minStr = Integer.toString(currMin);
			String secStr = Integer.toString(currSec);
			
			if (currHour < 10) {
				hourStr = "0" + hourStr;
			}
			if (currMin < 10) {
				minStr = "0" + minStr;
			}
			if (currSec < 10) {
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
        
        // toggle first button on (for normal clock rate at 1x)
        currRateBtn = (ToggleButton)findViewById(R.id.speedbtn1x);
        currRateBtn.setChecked(true);
 		
 		// create the Clock object, passes it the handler
 		// create the thread and pass it the Clock (runnable)
 		clock = new Clock(handler);
 		Thread bg_timer = new Thread(clock);
 		bg_timer.start();
    }
    
    public void resetClock(View view) {
    	clock.resetClock();
    	
    	// reset to normal rate
    	currRateBtn.setChecked(false);
        currRateBtn = (ToggleButton)findViewById(R.id.speedbtn1x);
        currRateBtn.setChecked(true);
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
	    		clock.setRate(1000);
	    	} else if (currRateBtn == (ToggleButton)findViewById(R.id.speedbtn125x)) {
	    		clock.setRate(800);
	    	} else if (currRateBtn == (ToggleButton)findViewById(R.id.speedbtn15x)){
	    		clock.setRate(666);
	    	} else if (currRateBtn == (ToggleButton)findViewById(R.id.speedbtn2x)) {
	    		clock.setRate(500);
	    	}
	    	
	    // else if same button, nothing changes
	    // reselect (re-highlight) the button because toggle auto turns it off
    	} else {
    		currRateBtn.setChecked(true);
    	}
    }
}
