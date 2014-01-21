package com.example.com.location.tracker.service;

import com.example.com.location.common.Common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


public class WorkerThread extends Thread {
	Handler mhandler;
	Handler mhandleLocalContext ; //= new Handler();
	Context context = null;
	String email;
	ServerRunnable obj = null;
	
	WorkerThread(String ema, Context cont, Handler handle) {
		context = cont;
		email = ema;
		mhandler = handle;
		
	}
	public void errorReported(int error) {
		Message msg = new Message();
		msg.arg1 = error;
		mhandler.sendMessage(msg);
	}
	public void run() {
	    	//Code
		
		Looper.prepare();
		mhandleLocalContext = new Handler() {
			public void handleMessage(Message msg) {
				Message msg1 = new Message();
				if (msg.arg1 == Common.STOP_WORKER_THREAD) {
					mhandleLocalContext.removeCallbacks(obj);
					mhandleLocalContext.getLooper().quit();
				} else {
					msg1.arg1 = msg.arg1;
					msg1.obj = msg.obj;
					mhandler.sendMessage(msg1);
				}
	    		
			}
		};
		obj = new ServerRunnable(context, mhandleLocalContext ,email);
		mhandleLocalContext.postDelayed(obj, 0);
		Log.v("tracker", "Worker thread ---before loop");
		 Looper.loop();

	 }
	public void stopRunning() {
		// TODO Auto-generated method stub
		Log.v("tracker", "Worker thread ----stop -----------111");
		obj.stopRunning();
		//mhandleLocalContext.removeCallbacks(obj);
		//mhandleLocalContext.getLooper().quit();
		
	}
	
	
}