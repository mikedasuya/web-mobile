package com.househelper.ui;



import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.househelper.interfaces.ClickListenerInterface;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SnapshotFragment extends Fragment {
	private GoogleMap mMap;
	Button profileButton;
	ClickListenerInterface mCallBack = null;
	Activity act = null;
	
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
        act = activity;
    }

	
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View rootView = inflater.inflate(R.layout.snapshotview, container, false);
	        profileButton = (Button) rootView.findViewById(R.id.bsnapshot);
	        profileButton.setOnClickListener(buttonListener);
	        return rootView;
	    }
	 
	 OnClickListener buttonListener = new View.OnClickListener() {
	 @Override
	 public void onClick(View v) {
		 	mCallBack.onClick(v.getId());
		 }
	};
	
	@Override
	public void onDestroyView() {
	    super.onDestroyView();
	    Fragment fragment = act.getFragmentManager().findFragmentById(R.id.map);
	    if (fragment != null)
	        getFragmentManager().beginTransaction().remove(fragment).commit();
	}
	
	
	  
		
}
