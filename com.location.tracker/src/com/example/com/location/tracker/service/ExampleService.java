package com.example.com.location.tracker.service;

import com.example.com.location.common.Common;
import com.example.com.location.tracker.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class ExampleService extends Service {
    int mStartMode;       // indicates how to behave if the service is killed
    IBinder mBinder;      // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used
    private String PREFS_NAME = "prefs";
    Handler mhandler = new Handler() {
    	public void handleMessage(Message msg) {
    		if (msg.arg1 == Common.MAXIMUM_COUNT) {
    			//sendNotification();
    			//stopSelf();
    		}
    	}
	};
    private void sendNotification() {
		// TODO Auto-generated method stub
    	       
	}
    GeoLocation loc = new GeoLocation(mhandler, this);
    @Override
    public void onCreate() {
        // The service is being created
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
    	System.out.println("tracker on Start Command");
    	//TODO:Check the flag because thi aint working
    	String email = null;
    	if (intent == null) {
    		System.out.println("tracker intent is null");
    		email = getEmailFromPersistent();
    	} else {
    		email = intent.getStringExtra("email");
    	   	setEmailPersistent(email);
    	}
    	loc.setEmail(email);
	   	mStartMode = 1;
	   	startPostingData();
        return START_STICKY;
        
    }
    
    private boolean setEmailPersistent(String email) {
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	SharedPreferences.Editor editor = settings.edit();
    	editor.putString("email",email);
    	editor.commit();
    	return true;
    }
    
    private String getEmailFromPersistent() {
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Activity.MODE_PRIVATE);
         String uname = settings.getString("email", null);
         return uname;
    }
    
    private void startPostingData() {
		// TODO Auto-generated method stub
    	loc.setStop(1);
    	mhandler.postDelayed (loc, 0);
	}
	@Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return mAllowRebind;
    }
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }
    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
    	System.out.println("destroy");
    	mhandler.removeCallbacks(loc);
    	loc.setStop(-1);
    	mStartMode = -1;
    }
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}