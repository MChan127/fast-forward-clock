package com.mchan.fastforwardclock;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;

public class Clock implements Runnable {
	// handler that refers to the activity
	// passed into by the calling ui thread
	Handler handler;
	
	// is the clock running?
	private boolean running;
	
	// timer is based on the below values
	// and constantly updates/posts them
	private int currHour;
	private int currMin;
	private int currSec;

	// speed of the timer
	private String clockRate;
	
	public Clock(Handler handler) {
		this.handler = handler;
		
		// start the clock
		running = true;
		
		// initialize time to now
		// sets hour, minute and second
		resetClock();
		
		// initial rate is 1x (normal clock rate)
		clockRate = "1x";
	}
	
	@Override
	public void run() {
		// while clock is running, update time values based 
		// on user-chosen rate
		while (running) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// updates the time values
			updateTime();
			
			// send Bundle of time values to handler
			// on android side of app
			Message msg = handler.obtainMessage();
			Bundle bundle = new Bundle();
			bundle.putInt("hour", currHour);
			bundle.putInt("minute", currMin);
			bundle.putInt("second", currSec);
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
	}
	
	private void updateTime() {
		// increment seconds
		currSec++;
		
		// update minutes
		if (clockRate == "1x" && currSec > 59) {
			currSec = 0;
			currMin++;
		} else if (clockRate == "1.25x" && currSec > 47) {
			currSec = 0; 
			currMin++;
		} else if (clockRate == "1.5x" && currSec > 39) {
			currSec = 0;
			currMin++;
		} else if (clockRate == "2x" && currSec > 29) {
			currSec = 0; 
			currMin++;
		}
			
		// update hours
		if (currMin > 59) {
			currMin = 0;
			currHour++;
			
			// if full day
			if (currHour > 24) {
				currHour = 0;
			}
		}
	}
	
	// methods to start and stop the clock
	public void startClock() {
		running = true;
	}
	public void stopClock() {
		running = false;
	}
	
	// resets (resyncs) the clock to the current time 
	// on the user's phone
	public void resetClock() {
		// create Time object
		// set to current time
		Time time = new Time();
		time.setToNow();
		
		// initialize and store current hour, minute and second
		currHour = time.hour;
		currMin = time.minute;
		currSec = time.second;
	}
	
	// sets the rate of the clock based on user setting
	public void setRate(String rate) {
		clockRate = rate;
	}
}
