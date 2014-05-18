package com.example.com.location.tracker;

import com.example.com.location.common.Common;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;



public class Animation implements Runnable {
	
	Context contActivity;
	Handler mhandler;
	Handler mhandlerUICommands = new Handler() {
	    	public void handleMessage(Message msg) {
	    		
	    	}
	};
	Button bt;
	int state = Common.ANIMATION_STATE_NOT_RUNNING;
	
	Animation(Context con) {
		contActivity = (Activity) con;
		bt = (Button) ((Activity) contActivity).findViewById(R.id.onoff);
	}
	public int getState() {
		return state;
	}
	public synchronized void startAnimation() {
		bt = (Button) ((Activity) contActivity).findViewById(R.id.onoff);
		if (bt != null) {
			bt.setBackgroundResource(R.drawable.on1);
			bt.setTag("On");
		}
		state = Common.ANIMATION_STATE_RUNNIG;
		mhandlerUICommands.postDelayed(this, Common.ANIMATION_TIME);
	}
	
	public synchronized  void stopAnimation() {
		bt = (Button) ((Activity) contActivity).findViewById(R.id.onoff);
		if (bt != null) {
			bt.setBackgroundResource(R.drawable.off1);
		}
		state = Common.ANIMATION_STATE_NOT_RUNNING;
		mhandlerUICommands.removeCallbacks(this);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Button bt = (Button) ((Activity) contActivity).findViewById(R.id.onoff);
		if (bt != null) {
			String tag = (String) bt.getTag();
			if ( tag.compareTo("On") == 0) {
				bt.setBackgroundResource(R.drawable.plainbg);
				bt.setTag("Off");
				
			} else if( tag.compareTo("Off") == 0) {
				bt.setBackgroundResource(R.drawable.on1);
				bt.setTag("On");
			}
			
		}
		mhandlerUICommands.postDelayed(this, Common.ANIMATION_TIME);
	}
}