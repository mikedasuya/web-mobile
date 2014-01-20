package com.example.com.location.tracker.service;

import java.util.concurrent.locks.Lock;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.example.com.location.common.Common;
import com.example.com.location.common.Common.EVENT;

public class StateMachine {
	private static final String PREFS_NAME = "state";
	private int currentState;
	Handler mhandler;
	Context  context;
	private StateMachine(Handler handle, Context cont) {
		currentState = 1;
		mhandler = handle;
		context = cont;
		
	}
	public static StateMachine getInstance(Handler mHandler, Context cont) {
		if (val == null) {
			val = new StateMachine(mHandler, cont);
		}
		return val;
	}
	public boolean onEvent(EVENT ev) {
		currentState = Common.ar[currentState][ev.getVal()];
		Message msg = new Message();
		if (currentState == Common.STATE.STATE_INITIAL.getVal()) {
			msg.arg1 = currentState;
			msg.obj = Common.STATE.STATE_INITIAL;
		} else if (currentState == Common.STATE.STATE_TRACKING.getVal()) {
			msg.obj = Common.STATE.STATE_TRACKING;
			msg.arg1 = currentState;
		} else if (currentState == Common.STATE.STATE_ERROR.getVal()) {
			msg.obj = Common.STATE.STATE_ERROR;
			msg.arg1 = currentState;
		}
		setState(currentState);
		mhandler.sendMessage(msg);
		return true;
	}
	public int getCurrentState() {
		currentState = getState();
		return currentState;
	}
	 public boolean setState(int state) {
	    	SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
	    	SharedPreferences.Editor editor = settings.edit();
	    	editor.putInt("state",state);
	    	editor.commit();
	    	return true;
	    }
	    
	    public int getState() {
	    	SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
	                Activity.MODE_PRIVATE);
	         int uname = (int) settings.getInt("state", (Integer) null);
	         return uname;
	    }
	private static StateMachine val = null;
}