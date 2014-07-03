package com.mchan.fastforwardclock;

import com.mchan.fastforwardclock.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
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
 		
 		// create the Clock object, passes it the handler
 		// create the thread and pass it the Clock (runnable)
 		Clock clock = new Clock(handler);
 		Thread bg_timer = new Thread(clock);
 		bg_timer.start();
    }
}
