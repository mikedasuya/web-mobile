package com.househelper.ui;



import android.app.Activity;
import android.os.Bundle;

public class SnapshotActivity extends Activity {
	private GoogleMap mMap;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.snapshotview);
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

	}
}
