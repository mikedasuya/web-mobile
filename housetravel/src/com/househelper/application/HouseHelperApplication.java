


package com.househelper.application;

import android.app.Application;

public class HouseHelperApplication extends Application {
	LocalContext context;
	@Override
	  public void onCreate()
	  {
	    super.onCreate();
	     
	    // Initialize the singletons so their instances
	    // are bound to the application process.
	    context = new LocalContext();
	  }
	public LocalContext getLocalContext() {
		return context;
	}
	 
}