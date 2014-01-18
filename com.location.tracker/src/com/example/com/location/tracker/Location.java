package com.example.com.location.tracker;

import com.example.com.location.tracker.service.ExampleService;
import com.example.com.location.tracker.service.GPSTracker;

import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.EditText;

public class Location extends Activity {
	 final Handler handler=new Handler();
	 Animation an;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    GPSTracker mGPS = new GPSTracker(this);

    TextView text = (TextView) findViewById(R.id.texts);
    
    if(mGPS.canGetLocation ){
    	mGPS.getLocation();
   //text.setText("Lat"+mGPS.getLatitude()+"Lon"+mGPS.getLongitude());
    }else{
        text.setText("Unabletofind");
        System.out.println("Unable");
    }
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
            imageView.setImageResource(R.drawable.green);
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
        StartAnimation();
         
    } 
   }); 
   Log.i("MY", "FINISHED OnCreate");
   
}
@Override
public void onStart() {
	super.onStart();
	
}
private boolean isMyServiceRunning() {
    ActivityManager manager = (ActivityManager) getSystemService(this.ACTIVITY_SERVICE);
    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
        if (ExampleService.class.getName().equals(service.service.getClassName())) {
            return true;
        }
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

}

public void StartAnimation() {
	Animation an = new Animation();	
	handler.post(an);
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
    	
    	Intent in = new Intent(this, ExampleService.class);
    	in.putExtra("email", val);
    	this.startService(in);
    	
    } else {
        // Disable vibrate
    	Intent in = new Intent(this, ExampleService.class);
    	in.putExtra("email", val);
    	this.stopService(in);
    }
    }
}
@SuppressLint("NewApi")
@Override
public void onStart() {
	 ToggleButton tbutton = (ToggleButton) findViewById(R.id.togglebutton);
	if(isMyServiceRunning()) {
		tbutton.setChecked(true);
		tbutton.setText("Stop Location Tracking");
	} else {
		tbutton.setChecked(false);
		tbutton.setText("Start Location Tracking");
	}
	super.onStart();
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
}
}