


package com.househelper.application;

import com.househelper.connection.ConnectionManager;
import com.househelper.service.IUploadRequest;
import com.househelper.service.Request;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

public class HouseHelperApplication extends Application {
	LocalContext context;
	int count = 0;
	ConnectionManager con = new ConnectionManager(this);
	
	@SuppressLint("NewApi")
	ActivityLifecycleCallbacks cb = new ActivityLifecycleCallbacks() {

		@Override
		public void onActivityCreated(Activity activity,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			count++;
			if (count == 1) {
				con.connect();
			}
			
		}

		@Override
		public void onActivityStarted(Activity activity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityResumed(Activity activity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityPaused(Activity activity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityStopped(Activity activity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivitySaveInstanceState(Activity activity,
				Bundle outState) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityDestroyed(Activity activity) {
			// TODO Auto-generated method stub
			count--;
			if (count == 0) {
				con.disConnect();
			}
		}
		
	};
	
	@SuppressLint("NewApi")
	@Override
	  public void onCreate()
	  {
	    super.onCreate();
	     
	    // Initialize the singletons so their instances
	    // are bound to the application process.
	    context = new LocalContext();
	    registerActivityLifecycleCallbacks(cb);
	        
	    
	  }
	
	public LocalContext getLocalContext() {
		return context;
	}
	
	
	/**
	 * This function must be called once activity is in front 
	 * which means connection must have been established
	 * @param obj
	 * @return
	 */
	
	boolean addRequest(Request obj) {
		con.addRequest(obj);
		return true;
		
	}
	
	 
}