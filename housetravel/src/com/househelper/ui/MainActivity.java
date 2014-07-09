package com.househelper.ui;



import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
         
        TabHost tabHost = getTabHost();
         
        // Tab for Photos
        TabSpec photospec = tabHost.newTabSpec("SnapShot");
        // setting Title and Icon for the Tab
        photospec.setIndicator("SnapShot", getResources().getDrawable(R.drawable.ic_launcher));
        Intent photosIntent = new Intent(this, SnapshotActivity.class);
        photospec.setContent(photosIntent);
         
        // Tab for Songs
        TabSpec songspec = tabHost.newTabSpec("History");       
        songspec.setIndicator("History", getResources().getDrawable(R.drawable.ic_launcher));
        Intent songsIntent = new Intent(this, HistoryActivity.class);
        songspec.setContent(songsIntent);
         
         
        // Adding all TabSpec to TabHost
        tabHost.addTab(photospec); // Adding photos tab
        tabHost.addTab(songspec); // Adding songs tab
        
	}

    /*protected void onStart() {
    	
    }
    
    protected void onRestart(){
    	
    }

    protected void onResume(){
    	
    }

    protected void onPause(){
    	
    }

    protected void onStop() {
    	
    }
*/
    protected void onDestroy() {
    	super.onDestroy();
    }

}
