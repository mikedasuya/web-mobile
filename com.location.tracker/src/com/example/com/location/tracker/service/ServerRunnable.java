package com.example.com.location.tracker.service;

import com.example.com.location.common.Common;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class ServerRunnable implements Runnable {
	Context context;
	ServerConnection connect;
	Handler mhandler;
	String email;
	ServerRunnable(Context con, Handler mhandle, String emal) {
		 connect = new ServerConnection();
		 mhandler = mhandle;
		 email = emal;
		 context = con;
	}
	public void errorReported(int error) {
		Message msg = new Message();
		msg.arg1 = error;
		mhandler.removeCallbacks(this);
		mhandler.sendMessage(msg);
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
		    		 mhandler.postDelayed(this, Common.time);
		    	 }
		     } catch (Exception e) {
		    	 errorReported(Common.ERROR_SERVER_PROBLEM);
		     }
		 } else {
			 	errorReported(Common.GEO_NOT_FOUND);
		 }
	}
	public void stopRunning() {
		// TODO Auto-generated method stub
		
	}
	
}