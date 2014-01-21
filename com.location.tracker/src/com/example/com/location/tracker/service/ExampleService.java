package com.example.com.location.tracker.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.example.com.location.common.Common;
import com.example.com.location.tracker.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
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

public class ExampleService extends Service {
    int mStartMode;       // indicates how to behave if the service is killed
    IBinder mBinder;      // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used
    private String PREFS_NAME = "prefs";
    IBinder bind = new ConnectionBinder();
    
    HashMap<ICallBack, ICallBack> cbList = new HashMap<ICallBack, ICallBack>();
    Handler mhandler = new Handler() {
    	public void handleMessage(Message msg) {
    		if (msg.arg1 == Common.MAXIMUM_COUNT) {
    			//sendNotification();
    			//stopSelf();
    		} if (msg.arg1 == Common.ERROR_SERVER_PROBLEM) {
    				st.onEvent(Common.EVENT.EVENT_STOP_TRACKING);
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
    			
    			destroy();
    			stopSelf();
    			sendNotification("Server Not responding Try again");
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
    			//destroy();
    			//stopSelf();
    			sendNotification("Geo Location service not found");
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
	    			if (work == null) {
	    				work = 
	    						new WorkerThread(email, ExampleService.this, mhandler);
	    				work.start();
	    				sendUI(Common.START_TRACKING);
	    				st.onEvent(Common.EVENT.EVENT_START_TRACKING);
	    			}
	    		} else if (msg.arg1 == Common.STOP_TRACKING) {
	    			
	    			if (work != null) {
	    				st.onEvent(Common.EVENT.EVENT_STOP_TRACKING);
	    				sendUI(Common.STOP_TRACKING);
	    				work.stopRunning();
	    				Log.v("tracker worker thread state -", " " +work.isAlive());
	    				work = null;
	    			} else {
	    				Log.v("tracker", "work = null");
	    			}
	    			
	    		}
	    	}

			
		};
		
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
		public boolean startTracking(String email) {
			boolean result = false;
			if (st.getCurrentState() ==  Common.STATE.STATE_TRACKING.getVal()) {
				result = false;
			} else {
				String emails = getEmailFromPersistent();
				if (emails == null) {
					setEmailPersistent(email);
				} else 	if (emails != null && emails.compareTo(email) != 0) {
					return false;
				}
				Message msg = new Message();
				msg.arg1 = Common.START_TRACKING;
				msg.obj = email;
				mhandlerUICommands.sendMessage(msg);
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
			if (st.getCurrentState() == Common.STATE.STATE_TRACKING.getVal()) {
				Log.v("tracker", "---syncui--tracking");
				sendUI(Common.START_TRACKING);
			} else if (st.getCurrentState() == Common.STATE.STATE_INITIAL.getVal()) {
				sendUI(Common.STOP_TRACKING);
				Log.v("tracker", "---syncui--initial");
			}
			return true;
		}
	
	};
	
	
    private void sendNotification(String msg) {
		// TODO Auto-generated method stub
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(this)
    	        .setSmallIcon(R.drawable.images)
    	        .setContentTitle("Location Server Notification")
    	        .setContentText(msg);
    	// Creates an explicit intent for an Activity in your app
    	
    	NotificationManager mNotificationManager =
    	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	// mId allows you to update the notification later on.
    	mNotificationManager.notify(0, mBuilder.build());
    	       
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
    			if (email == null) {
    				System.out.println(" tracker ---- intent null email cannt be null");
    				Message msg = new Message();
    				msg.arg1 = Common.START_TRACKING;
    				msg.obj = email;
    				mhandlerUICommands.sendMessage(msg);
    			}
    		}
    	}
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
    	System.out.println("tracker ------------------ ---  destroy");
    	
    }
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return bind;
	}
}