package com.example.com.location.tracker;

import com.example.com.location.common.Common;
import com.example.com.location.tracker.service.ExampleService;
import com.example.com.location.tracker.service.GPSTracker;
import com.example.com.location.tracker.service.ICallBack;
import com.example.com.location.tracker.service.IServerConnection;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.EditText;

public class Location extends Activity {
	 final Handler handler=new Handler();
	 Animation an;
	 IServerConnection serverCon = null;
	 CallBack cb;
	 
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    GPSTracker mGPS = new GPSTracker(this);
    Intent in = new Intent(this, ExampleService.class);
	bindService(in, connectionService, Context.BIND_AUTO_CREATE);
   
    //TextView text = (TextView) findViewById(R.id.texts);
    
    if(mGPS.canGetLocation ){
    	mGPS.getLocation();
   //text.setText("Lat"+mGPS.getLatitude()+"Lon"+mGPS.getLongitude());
    }else{
      //  text.setText("Unabletofind");
        System.out.println("Unable");
    }
    
    final String val = "white";
    final LinearLayout root = (LinearLayout) findViewById(R.id.layout);
    root.post(new Runnable() { 
    public void run() { 
       
        // By now we got the height of titleBar & statusBar
        // Now lets get the screen size
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);   
        int screenHeight = metrics.heightPixels;
        int screenWidth = metrics.widthPixels;
        Log.i("MY", "Actual Screen Height = " + screenHeight + " Width = " + screenWidth);   
        int n = screenWidth/30;
        Log.i("MY", "n = " + n); 
        for (int i = 0; i < n;i++) {
        	final ImageView imageView = new ImageView(Location.this);
            //setting image resource
        	if (val.compareTo("white") == 0) {
        			imageView.setImageResource(R.drawable.white);
            } else if (val.compareTo("green") == 0) {
        		imageView.setImageResource(R.drawable.green);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30, 1);
            imageView.setLayoutParams(params);
            Log.i("MY", "adding" + i); 
        	root.addView(imageView);
        }
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
       
         
    } 
   }); 
   cb = new CallBack();
    
      Log.i("MY", "FINISHED OnCreate");
   
}

@Override
protected void onSaveInstanceState(final Bundle outState) {
	outState.putBoolean("isAnimation", true);
			
}



private void setAnimationIcons(final String val) {
	final LinearLayout root = (LinearLayout) findViewById(R.id.layout);
    root.post(new Runnable() { 
    public void run() { 
    	int count  = root.getChildCount();
    	for (int i = 0; i < count; i++) {
    		ImageView v = (ImageView)root.getChildAt(i);
    		if (val.compareTo("white") == 0) {
    			v.setImageResource(R.drawable.white);
    		} else if (val.compareTo("green") == 0) {
    			v.setImageResource(R.drawable.green);
    		}
    		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30, 1);
    		v.setLayoutParams(params);
    		root.removeViewAt(i);
    		root.addView(v, i);
    	}
	
    }
    });
 }
private boolean isMyServiceRunning() {
	Log.v("tracker", "is My service Running");
	try {
		if (serverCon != null) 
				if (serverCon.isTracking()) {
					return true;
		}
	} catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return false;
}
class Animation implements Runnable {
	
	LinearLayout root;
	int childcount;
	int i = 0;
	Animation() {
		root = (LinearLayout) findViewById(R.id.layout);
		childcount = root.getChildCount();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (i == childcount) {
			i = 0;
		}
	    //for (int i =0;i< childcount;i++) {
		   
	    	Log.i("MY Ui Thread", "adding 1 ---" + i);
			ImageView v = (ImageView)root.getChildAt(i);
			v.setImageResource(R.drawable.white);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30, 1);
			v.setLayoutParams(params);
			root.removeViewAt(i);
			root.addView(v, i);
			if (i > 0)
			{
				int j = i -1;
				//setting image resource
				ImageView v1 = (ImageView)root.getChildAt(j);
	            v1.setImageResource(R.drawable.green);
	            v1.setLayoutParams(params);
	            Log.i("MY", "adding" + i);
	            root.removeViewAt(j); 
	        	root.addView(v1, j);
			}
		 //}
			i++;
	     handler.postDelayed (this, 1000);
	}
	
}

@Override
public void onDestroy() {
	super.onDestroy();
	handler.removeCallbacks(an);
	try {
		serverCon.unregister(cb);
	} catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	unbindService(connectionService);

}

public void StartAnimation() {
	if (an == null) {
		an = new Animation();
	}
	handler.post(an);
}

public void stopAnimation() {
	handler.removeCallbacks(an);
	an = null;
	setAnimationIcons("white");
}
public void onToggleClicked(View view) {
    // Is the toggle on?
	
    System.out.println(" tracker ---toggled");
    boolean on = ((ToggleButton) view).isChecked();
    EditText text = (EditText)findViewById(R.id.edittext);
    ToggleButton tbutton = (ToggleButton) findViewById(R.id.togglebutton);
    String val = text.getText().toString();
    if (val.length() == 0) {
    	tbutton.setChecked(false);
    } else {
    if (on) {
        // Enable vibrate
    //	StartAnimation();
    	if (serverCon != null) {
    		try {
				serverCon.startTracking(val);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    } else {
        // Disable vibrate
    		//stopAnimation();
    		try {
    			if (serverCon != null) {
    				serverCon.stopTracking(val);
    			}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}
@SuppressLint("NewApi")
@Override
public void onStart() {
	ToggleButton tbutton = (ToggleButton) findViewById(R.id.togglebutton);
	if(isMyServiceRunning()) {
		tbutton.setChecked(true);
		StartAnimation();
		tbutton.setText("Stop Location Tracking");
	} else {
		tbutton.setChecked(false);
		stopAnimation();
		tbutton.setText("Start Location Tracking");
	}
	super.onStart();
}

class CallBack extends ICallBack.Stub {

	@Override
	public void result(String value, int returnCode) throws RemoteException {
		// TODO Auto-generated method stub
		if (returnCode == Common.ERROR_SERVER_PROBLEM) {
			//showDialog();
			finish();
			stopAnimation();
		} else if (returnCode == Common.GEO_NOT_FOUND) {
			runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	Toast.makeText(getApplication(), "Geo Not found try again", Toast.LENGTH_LONG).show();
                    
                }
            });
			
		} else if (returnCode == Common.START_TRACKING) {
			StartAnimation();
			ToggleButton tbutton = (ToggleButton) findViewById(R.id.togglebutton);
			tbutton.setChecked(true);
			  
		} else if (returnCode == Common.STOP_TRACKING) {
			stopAnimation();
			ToggleButton tbutton = (ToggleButton) findViewById(R.id.togglebutton);
			tbutton.setChecked(false);
			
		}
	}

	
};

ServiceConnection connectionService = new ServiceConnection() {

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// TODO Auto-generated method stub
		serverCon = IServerConnection.Stub.asInterface(service);
		try {
			Log.v("tracker", "Service Connected");
			serverCon.registerCallBack(cb);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
		
	}
	
};

}