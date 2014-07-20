package com.househelper.profile;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.maps.MapFragment;
import com.househelper.contentprovier.test.testDb;
import com.househelper.ui.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ProfileFragment extends Fragment {
	private static final String TAG = "ProfileFragment";
	Button buttonSave = null;
	Context context = null;
	
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    context = activity;
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile, container, false);
        buttonSave = (Button) rootView.findViewById(R.id.save);
        buttonSave.setOnClickListener(buttonListener);
        return rootView;
	}
    
	@Override
    public void onActivityCreated(Bundle saved) { 
        super.onActivityCreated(saved);
        Log.i(TAG, "onActivityCreated");
        buttonSave.setOnClickListener(buttonListener);
	}
	
	OnClickListener buttonListener = new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
	    	if (checkValues()) {
	    		testDb testdb1 = new testDb(context);
	    		testdb1.insert();
	    		testdb1.getData();
	    		testdb1.updateData();
	    	}
	    	//context.getContentResolver().insert(url, values);
	    }

		private boolean checkValues() {
			// TODO Auto-generated method stub
			return false;
		}
	        
	};
	
	public String getNewFolderName(int id) {
		String folderName = null;
		if (id == 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String currentDateandTime = sdf.format(new Date());
			folderName = "folder" + currentDateandTime;
		}
		return folderName;
		
	}
		
	
	
}
