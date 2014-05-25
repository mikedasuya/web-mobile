package com.example.com.location.tracker.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.com.location.common.Common;
import com.example.com.location.tracker.NewUILocation;
import com.example.com.location.tracker.R;

import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

public class ExampleService extends Service {
    int mStartMode;       // indicates how to behave if the service is killed
    IBinder mBinder;      // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used
     
    IBinder bind = new ConnectionBinder();
    
    HashMap<ICallBack, ICallBack> cbList = new HashMap<ICallBack, ICallBack>();
    Handler mhandler = new Handler() {
    	public void handleMessage(Message msg) {
    		if (msg.arg1 == Common.MAXIMUM_COUNT) {
    			//sendNotification();
    			//stopSelf();
    		} if (msg.arg1 == Common.ERROR_SERVER_PROBLEM) {
    				Iterator it = cbList.entrySet().iterator();
    			    while (it.hasNext()) {
    			        Map.Entry pairs = (Map.Entry)it.next();
    			        ICallBack element = (ICallBack) pairs.getKey();
    			        try {
							element.result("Server Error", Common.ERROR_SERVER_PROBLEM);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    			     }
    			sendNotification(Common.NOTIFICATION_ID_NO_DATA_CONNECTION, Common.ERROR_NO_DATA_CONNECTION);
    		} else if (msg.arg1 == Common.GEO_NOT_FOUND) {
    			 Iterator it = cbList.entrySet().iterator();
 			    while (it.hasNext()) {
 			        Map.Entry pairs = (Map.Entry)it.next();
 			        ICallBack element = (ICallBack) pairs.getKey();
 			        try {
							element.result("Server Error", Common.GEO_NOT_FOUND);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
 			     }
 			    Message msg1 = new Message();
 				msg.arg1 = Common.STOP_TRACKING;
 				msg.arg2 = Common.ERROR_VALUE_NO_GEO;
 				msg.obj = getEmailFromPersistent();
 				mhandlerUICommands.sendMessage(msg);
 				//sendNotification(Common.NOTIFICATION_ID_NO_GEO, Common.ERROR_NO_GEO);
 			    
 			    stopSelf();
 			    destroy();
    			//destroy();
    			//stopSelf();
    			
    		}
    	}
	};
	StateMachine st =  StateMachine.getInstance(mhandler, this);
	WorkerThread work = null;
	 Handler mhandlerUICommands = new Handler() {
	    	public void handleMessage(Message msg) {
	    		if (msg.arg1 == Common.START_TRACKING) {
	    			//sendNotification();
	    			//stopSelf();
	    			String email = (String) msg.obj;
	    			int freq = msg.arg2;
	    			setFrequence(freq);
    				setEmailPersistent(email);
    				WorkerThread.getInstance(ExampleService.this, mhandler).start();
    				st.onEvent(Common.EVENT.EVENT_START_TRACKING);
    			    sendUI(Common.START_TRACKING);
    			    showRunningNotification(Common.Service_RUNNING, 0);
	    		} else if (msg.arg1 == Common.STOP_TRACKING) {
	    			removeNotification(Common.NOTIFICATION_ID_NO_DATA_CONNECTION);
	    			removeNotification(Common.NOTIFICATION_ID_NO_GEO);
	    			WorkerThread.getInstance(ExampleService.this, mhandler).stopRunning();
	    			st.onEvent(Common.EVENT.EVENT_STOP_TRACKING);
	    			sendUI(Common.STOP_TRACKING);
	    			showRunningNotification(Common.Service_NOT_RUNNING, msg.arg2);
	    		}
	    	}

			

			
		
		};
		private void removeNotification(int notificationIdNoDataConnection) {
			// TODO Auto-generated method stub
			NotificationManager mNotificationManager =
		    	    (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.cancel(notificationIdNoDataConnection);
		}
		
		private void showRunningNotification(int serviceRunning, int errorCode) {
			// TODO Auto-generated method stub
			/*NotificationManager mNotificationManager =
		    	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder mBuilder = null;
			 RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);
		        contentView.setImageViewResource(R.id.image11, R.drawable.main);
		        
		     if (serviceRunning == Common.Service_RUNNING) {   
		        contentView.setTextViewText(R.id.title, "Location Tracker Running");
		     } else if (serviceRunning == Common.Service_NOT_RUNNING ) {
		    	 contentView.setTextViewText(R.id.title, "Location Tracker Not Running");
		     } else if (serviceRunning == Common.Service_NOT_RUNNING && 
		    		  errorCode == Common.Service_NOT_RUNNING) {
		    	 contentView.setTextViewText(R.id.title, "Location Error Geo Not Found Restart");
		     }
		        
		        
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
		            new Intent(this, NewUILocation.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
	    	
	    	android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
	        notificationBuilder.setAutoCancel(false);
	        notificationBuilder.setSmallIcon(R.drawable.main);
	        notificationBuilder.setContentTitle(" Location Tracker");
	           Notification noti = notificationBuilder.build();
	        noti.contentIntent = contentIntent;
	        noti.contentView = contentView;
	        mNotificationManager.notify(Common.Service_RUNNING, noti);*/
	       String msg = null;
	       PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
		            new Intent(this, NewUILocation.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	       // Creates an explicit intent for an Activity in your app
	        if (serviceRunning == Common.Service_RUNNING) {   
		        msg =  "Location Tracker Running";
		     } else if (serviceRunning == Common.Service_NOT_RUNNING ) {
		    	msg =  "Location Tracker Not Running";
		     } else if (serviceRunning == Common.Service_NOT_RUNNING && 
		    		  errorCode == Common.ERROR_VALUE_NO_GEO) {
		    	 msg = "Location Error Geo Not Found Restart";
		     }
	        NotificationCompat.Builder mBuilder =
	    	        new NotificationCompat.Builder(this)
	    	        .setSmallIcon(R.drawable.main)
	    	        .setContentTitle("Location Server Notification")
	    	        .setContentIntent(contentIntent)
	    	        .setContentText(msg);
	        
	    	
	    	NotificationManager mNotificationManager =
	    	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	    	
	    	// mId allows you to update the notification later on.
	    	mNotificationManager.notify(Common.Service_RUNNING, mBuilder.build());
	    	
	    	
		}
		
		private boolean setFrequence(int freq) {
			// TODO Auto-generated method stub
			Log.v("tracker", "set frequencey" + freq);
	    	SharedPreferences settings = ExampleService.this.getSharedPreferences(Common.PREFS_NAME_FREQ, Activity.MODE_PRIVATE);
	    	SharedPreferences.Editor editor = settings.edit();
	    	editor.putInt(Common.FREQ,freq);
	    	editor.commit();
	    	return true;
		}
		
		public int getFrequenceInternal(int freq) {
			// TODO Auto-generated method stub
			SharedPreferences settings = this.getSharedPreferences(Common.PREFS_NAME_FREQ,
	                Activity.MODE_PRIVATE);
	         int uname = settings.getInt(Common.FREQ, 0);
	         return uname;
		}
		
		public void sendUI(int startTracking) {
			// TODO Auto-generated method stub
			 Iterator it = cbList.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry pairs = (Map.Entry)it.next();
			        ICallBack element = (ICallBack) pairs.getKey();
			        try {
						element.result("", startTracking);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			     }
		}
	
	class ConnectionBinder extends IServerConnection.Stub {

		@Override
		public boolean registerCallBack(ICallBack cb) throws RemoteException {
			// TODO Auto-generated method stub
			boolean result = false;
			if (cb == null) {
				result = false;
			} else {
				Log.v("tracker", "register callback --add ");
				cbList.put(cb, cb);
				result = true;
				
			}
			
			return result;
		}

		@Override
		public boolean unregister(ICallBack cb) throws RemoteException {
			// TODO Auto-generated method stub
			boolean result = false;
			cbList.clear();
			return result;
		}
		@Override
		public boolean startTracking(String email, int freq) {
			boolean result = false;
			if (st.getCurrentState() ==  Common.STATE.STATE_TRACKING.getVal()) {
				result = false;
			} else {
				if (st.getCurrentState() != Common.STATE.STATE_TRACKING.getVal()) {
					setEmailPersistent(email);
					Message msg = new Message();
					msg.arg1 = Common.START_TRACKING;
					msg.obj = email;
					msg.arg2 = freq;
					mhandlerUICommands.sendMessage(msg);
				}
				result = true;
			}
			return result;
			
		}
		@Override
		public boolean stopTracking(String email) {
			/*String emails = getEmailFromPersistent();
			if (emails == null) {
				setEmailPersistent(email);
			} else 	if (emails != null && emails.compareTo(email) != 0) {
				return false;
			}*/
			Message msg = new Message();
			msg.arg1 = Common.STOP_TRACKING;
			msg.obj = email;
			mhandlerUICommands.sendMessage(msg);
			return true;
			
		}

		@Override
		public boolean isTracking() throws RemoteException {
			// TODO Auto-generated method stub
			Log.v("tracker ", "is Tracking");
			Log.v("tracker", "state machine " + st.getCurrentState());
			if (st != null ) {
				if (st.getCurrentState() == Common.STATE.STATE_TRACKING.getVal()) {
					return true;
				}
			} else {
				Log.v("tracker ", "service state machine is null");
			}
			return false;
		}

		@Override
		public boolean syncUI() throws RemoteException {
			// TODO Auto-generated method stub
			Log.v("tracker", "---sync--tracking 1");
			if (st.getCurrentState() == Common.STATE.STATE_TRACKING.getVal()) {
				Log.v("tracker", "---sync--tracking");
				sendUI(Common.START_TRACKING);
			} else if (st.getCurrentState() == Common.STATE.STATE_INITIAL.getVal()) {
				sendUI(Common.STOP_TRACKING);
				Log.v("tracker", "---syncui--stop tracking");
			}
			return true;
		}
		
		@Override
		public String getEmail() {
			// TODO Auto-generated method stub
			SharedPreferences settings = ExampleService.this.getSharedPreferences(Common.PREFS_NAME_EMAIL,
	                Activity.MODE_PRIVATE);
			String uname = settings.getString(Common.Email, Common.NULL);
	         return uname;
		}
		@Override
		public boolean setFrequency(int freq) {
			// TODO Auto-generated method stub
			Log.v("tracker", " service -----  set frequencey" + freq);
	    	SharedPreferences settings = ExampleService.this.getSharedPreferences(Common.PREFS_NAME_FREQ, Activity.MODE_PRIVATE);
	    	SharedPreferences.Editor editor = settings.edit();
	    	editor.putInt(Common.FREQ,freq);
	    	editor.commit();
	    	return true;
		}

		@Override
		public boolean setEmail(String emailq) throws RemoteException {
			// TODO Auto-generated method stub
			setEmailPersistent(emailq);
			return false;
		}

		@Override
		public int getFrequency() throws RemoteException {
			// TODO Auto-generated method stub
			return getFrequenceInternal(0);
			
		}
	
	};
	
	
    private void sendNotification(int msgId, String msg) {
		// TODO Auto-generated method stub
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(this)
    	        .setSmallIcon(R.drawable.main)
    	        .setContentTitle("Location Server Notification")
    	        .setContentText(msg);
    	// Creates an explicit intent for an Activity in your app
    	
    	NotificationManager mNotificationManager =
    	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	// mId allows you to update the notification later on.
    	mNotificationManager.notify(msgId, mBuilder.build());
    	
    	    	    	       
	}
    
    @Override
    public void onCreate() {
        // The service is being created
    	Log.v("tracker", "onCreate Service");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
    	int state = st.getCurrentState();
    	if (intent == null) {
    		if (state == Common.STATE.STATE_TRACKING.getVal()) {
    			System.out.println("tracker ---- intent null state tracking");
    			String email = getEmailFromPersistent();
    			System.out.println(" tracker ---- intent null email cannt be null");
    			Message msg = new Message();
    			msg.arg1 = Common.START_TRACKING;
    			msg.obj = email;
    			mhandlerUICommands.sendMessage(msg);
    			
    		}
    	}
    	return START_STICKY;
        
    }
    
    private boolean setEmailPersistent(String email) {
    	SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME_EMAIL, 0);
    	SharedPreferences.Editor editor = settings.edit();
    	editor.putString(Common.Email,email);
    	editor.commit();
    	return true;
    }
    
    private String getEmailFromPersistent() {
    	SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME_EMAIL,
                Activity.MODE_PRIVATE);
         String uname = settings.getString(Common.Email, Common.NULL);
         return uname;
    }
    
   
	@Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return true;
    }
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }
    private void destroy() {
    	
    }
    
    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
    	if (st.getCurrentState() ==  Common.STATE.STATE_TRACKING.getVal())  {
    		WorkerThread.getInstance(ExampleService.this, mhandler).stopRunning();
			st.onEvent(Common.EVENT.EVENT_STOP_TRACKING);
			sendUI(Common.STOP_TRACKING);
							       
    	}
    	System.out.println("tracker ------------------ ---  destroy");
    	
    }
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return bind;
	}
}