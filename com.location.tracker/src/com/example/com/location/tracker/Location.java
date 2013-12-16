package com.example.com.location.tracker;

import com.example.com.location.tracker.service.ExampleService;
import com.example.com.location.tracker.service.GPSTracker;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.EditText;

public class Location extends Activity {

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