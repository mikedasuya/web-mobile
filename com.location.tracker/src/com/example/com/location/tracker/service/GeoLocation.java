package com.example.com.location.tracker.service;

import com.example.com.location.common.Common;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

class ThreadConnection implements Runnable{
	String email;
	String lat;
	String longi;
	ServerConnection con;
	
	ThreadConnection(String email,
			String lat,
			String longi,
			ServerConnection con) {
		this.email = email;
		this.lat = lat;
		this.longi = longi;
		this.con = con;
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		con.sendEmailLatLong("email@gmail.com",lat,longi);
		
	}
	
};

public class GeoLocation implements Runnable {
	Handler handle;
	int stop;
	Context context;
	int counter = 0;
	ServerConnection con;
	GeoLocation(Handler handler, Context cont) {
		handle = handler;
		context = cont;
		counter = 0;
		con = new ServerConnection();
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("tested ---runnable");
		GPSTracker mGPS = new GPSTracker(context);
		if(mGPS.canGetLocation ){
	    	mGPS.getLocation();
	    	System.out.println("tracker Lat"+mGPS.getLatitude()+"Lon  "+mGPS.getLongitude());
	    	ThreadConnection task = new ThreadConnection("email@gmail.com",Double.toString(mGPS.getLatitude()),
	    			Double.toString(mGPS.getLongitude()),con);
	    	Thread t1 = new Thread(task);
	    	t1.start();
	    	
	    } else {
	     //   text.setText("Unabletofind");
	        System.out.println("Unable");
	        counter++;
	        
	        
	        if (counter == Common.MAXIMUM_COUNT) {
	        	
	        	Message msg = new Message();
	        	msg.arg1 = Common.MAXIMUM_COUNT;
	        	handle.sendMessage(msg);
	        	stop = -1;
	        }
	        
	    }
		if (stop != -1) {
			handle.postDelayed (this, Common.time);
		}
	}
	//1 for not stop;
	//-1 for stop;
	void setStop(int val) {
		stop = val;
		
	}
}