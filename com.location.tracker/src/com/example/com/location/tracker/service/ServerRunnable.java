package com.example.com.location.tracker.service;

import java.io.IOException;

import com.example.com.location.common.Common;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ServerRunnable implements Runnable {
	Context context;
	ServerConnection connect;
	Handler mhandler;
	boolean flag = false;
	GPSTracker mGPS;
	
	ServerRunnable(Context con, Handler mhandle) {
		 connect = new ServerConnection();
		 mhandler = mhandle;
		 context = con;
		 flag = false;
		 mGPS = new GPSTracker(context);
	}
	
	public void errorReported(int error) {
			Message msg = new Message();
			msg.arg1 = error;
			mhandler.sendMessage(msg);
		
	}
	
	public int getFrequence(int freq) {
		// TODO Auto-generated method stub
		SharedPreferences settings = context.getSharedPreferences(Common.PREFS_NAME_FREQ,
                Activity.MODE_PRIVATE);
         int uname = settings.getInt(Common.FREQ, 5);
         return uname;
	}
	
	public String getEmail() {
		// TODO Auto-generated method stub
		SharedPreferences settings = context.getSharedPreferences(Common.PREFS_NAME_EMAIL,
                Activity.MODE_PRIVATE);
		String uname = settings.getString(Common.Email, Common.NULL);
         return uname;
	}
	
	private void handleSuccessSensingToServer() {
		// TODO Auto-generated method stub
		int freq = getFrequence(0);
		Log.v("tracker", "ServerRunnable + get frequency" + freq);
		if (flag == false) {
		 		mhandler.postDelayed(this, freq);
		} else if (flag == true) {
		 	stopRunning();
		 	errorReported(Common.STOP_WORKER_THREAD);
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		 mGPS.getLocation();	    
		 if(mGPS.canGetLocation ) {
			
		     System.out.println("tracker Lat"+mGPS.getLatitude()+"Lon  "+mGPS.getLongitude());
		     String lat = Double.toString(mGPS.getLatitude());
		     String longi = Double.toString(mGPS.getLongitude());
		     String email = getEmail();
			try {
		    	 if (connect.sendEmailLatLong(email, lat, longi)) {
		    		 NotificationManager mNotificationManager =
		    		    	    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		    		 mNotificationManager.cancel(Common.NOTIFICATION_ID_NO_DATA_CONNECTION);
		    	 }
		    } catch (IOException e) {
		    	e.printStackTrace();
		    	errorReported(Common.ERROR_SERVER_PROBLEM);
		     }
			handleSuccessSensingToServer();
		     
		 } else {
			 	stopRunning();
			 	errorReported(Common.GEO_NOT_FOUND);
			 	errorReported(Common.STOP_WORKER_THREAD);
		 }
	}
	
	
	public void stopRunning() {
		// TODO Auto-generated method stub
		flag = true;
		mhandler.removeCallbacks(this);
		
	}
	
}