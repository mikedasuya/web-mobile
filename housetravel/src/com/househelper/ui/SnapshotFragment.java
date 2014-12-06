package com.househelper.ui;



import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.househelper.getLocation.LocationTracker;
import com.househelper.interfaces.ClickListenerInterface;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SnapshotFragment extends Fragment {
	private static final String TAG = "househelper";
	private GoogleMap mMap;
	Button profileButton;
	ClickListenerInterface mCallBack = null;
	MapView mapView = null;
	public LocationTracker mLocationTracker;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	   // setRetainInstance(true); 
	    super.onCreate(savedInstanceState);   
	    mLocationTracker = new LocationTracker(getActivity());
	    
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallBack = (ClickListenerInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
       
    }

	
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		        
	        View v = inflater.inflate(R.layout.snapshotview, container, false);

	        // Gets the MapView from the XML layout and creates it
	       mapView = (MapView) v.findViewById(R.id.mapview);
	        mapView.onCreate(savedInstanceState);

	        // Gets to GoogleMap from the MapView and does initialization stuff
	        mMap = mapView.getMap();
	        if (mMap == null) {
	        	Log.d(TAG, " google mpa null ------------------------------------");
	        } else {
	        	mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	        	mMap.getUiSettings().setMyLocationButtonEnabled(true);
	        }
	       
	        profileButton = (Button) v.findViewById(R.id.bsnapshot);
	    	profileButton.setOnClickListener(buttonListener);
	        return v;
	    }
	 
	 OnClickListener buttonListener = new View.OnClickListener() {
	 @Override
	 public void onClick(View v) {
		 	mCallBack.onClick(v.getId());
		 }
	};
	
	void setmarker() {
		 Location loc = mLocationTracker.getLocation();
		 if (loc != null) {
			 mMap.setMyLocationEnabled(true);
			 mMap.addMarker(new MarkerOptions()
			 .position(new LatLng(loc.getLatitude(), loc.getLongitude())));
			 MapsInitializer.initialize(this.getActivity());
	        // Updates the location and zoom of the MapView
			 CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 10);
			 mMap.animateCamera(cameraUpdate);
		 }
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onDestroyView() {
	    super.onDestroyView();
	   /* Fragment fragment = act.getFragmentManager().findFragmentById(R.id.map);
	    if (fragment != null)
	        getFragmentManager().beginTransaction().remove(fragment).commit();*/
	   /* FragmentManager fm = getActivity().getFragmentManager();
	    Fragment fragment = (fm.findFragmentById(R.id.map));
	    FragmentTransaction ft = fm.beginTransaction();
	    ft.remove(fragment);
	    ft.commit();*/
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (mapView != null) {
			mapView.onResume();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (mapView != null) {
			mapView.onPause();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mapView != null) {
			mapView.onDestroy();
		}
		
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		if (mapView != null) {
			mapView.onLowMemory();
		}
	}
	
	
	  
		
}
