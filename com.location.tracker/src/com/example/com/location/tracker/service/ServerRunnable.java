package com.example.com.location.tracker.service;

import java.io.IOException;

import com.example.com.location.common.Common;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ServerRunnable implements Runnable {
	Context context;
	ServerConnection connect;
	Handler mhandler;
	String email;
	boolean flag = false;
	ServerRunnable(Context con, Handler mhandle, String emal) {
		 connect = new ServerConnection();
		 mhandler = mhandle;
		 email = emal;
		 context = con;
		 flag = false;
	}
	public void errorReported(int error) {
		if (flag == false) {
			Message msg = new Message();
			msg.arg1 = error;
			mhandler.removeCallbacks(this);
			mhandler.sendMessage(msg);
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		GPSTracker mGPS = new GPSTracker(context);
		 if(mGPS.canGetLocation ) {
			 mGPS.getLocation();
		     System.out.println("tracker Lat"+mGPS.getLatitude()+"Lon  "+mGPS.getLongitude());
		     String lat = Double.toString(mGPS.getLatitude());
		     String longi = Double.toString(mGPS.getLongitude());
		     
		     try {
		    	 
		    	 if (connect.sendEmailLatLong(email, lat, longi)) {
		    		 	if (flag == false) {
		    		 		mhandler.postDelayed(this, Common.time);
		    		 	} else if (flag == true) {
		    		 		Message msg = new Message();
		    		 		msg.arg1 = Common.STOP_WORKER_THREAD;
		    		 		mhandler.sendMessage(msg);
		    		 	}
		    	 }
		    	
		     } catch (IOException e) {
		    	 e.printStackTrace();
		    	 errorReported(Common.ERROR_SERVER_PROBLEM);
		     }
		 } else {
			 	
			 	errorReported(Common.GEO_NOT_FOUND);
		 }
	}
	public void stopRunning() {
		// TODO Auto-generated method stub
		flag = true;
		mhandler.removeCallbacks(this);
	}
	
}