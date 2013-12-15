package com.example.com.location.tracker.service;

import com.example.com.location.common.Common;
import com.example.com.location.tracker.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class ExampleService extends Service {
    int mStartMode;       // indicates how to behave if the service is killed
    IBinder mBinder;      // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used
    Handler mhandler = new Handler() {
    	public void handleMessage(Message msg) {
    		if (msg.arg1 == Common.MAXIMUM_COUNT) {
    			sendNotification();
    			stopSelf();
    		}
    	}
	};
    private void sendNotification() {
		// TODO Auto-generated method stub
    	NotificationManager notificationManager = (NotificationManager) 
    			  getSystemService(NOTIFICATION_SERVICE);
    	Notification n  = new Notification.Builder(this)
        .setContentTitle(Common.text)
        .setSmallIcon(R.drawable.ic_launcher).build();
        
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
    	mStartMode = 1;
    	startPostingData();
        return mStartMode;
        
    }
    
    private void startPostingData() {
		// TODO Auto-generated method stub
    	loc.setStop(1);
    	mhandler.postDelayed (loc, Common.time);
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
    	loc.setStop(-1);
    	mStartMode = -1;
    }
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}