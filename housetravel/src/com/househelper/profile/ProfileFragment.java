package com.househelper.profile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.google.android.gms.maps.MapFragment;
import com.househelper.common.HouseConstants;
import com.househelper.contentprovider.Auth;
import com.househelper.contentprovier.test.testDb;
import com.househelper.getLocation.LocationTracker;
import com.househelper.service.ICallBack;
import com.househelper.service.UploadService;
import com.househelper.ui.R;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.househelper.application.HouseHelperApplication;
import com.househelper.application.LocalContext;

public class ProfileFragment extends Fragment {
	private static final String TAG = "ProfileFragment";
	Button buttonSave = null;
	Button addPic = null;
	Button addVideo = null;
	Context context = null;
	public static final Uri LC_CONTENT_URI = 
	         Uri.withAppendedPath(
	               Auth.CONTENT_URI,
	               "items");
	EditText agentNameView = null;
	EditText houseArea = null;
	EditText houseprice = null;
	EditText notes = null;
	String agentName;
	int houArea = 0;
	int price = 0;
	String hnotes;
	HouseHelperApplication app;
	LocalContext lcontext;
	String folderName;
	Float lat;
	Float longi;
	LocationTracker tracker = null;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	Uri finalMediaUri = null;
	
	
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    context = activity;
	    app = (HouseHelperApplication) getActivity().getApplication();
	    lcontext = app.getLocalContext();
	    tracker = new LocationTracker(context);
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile, container, false);
        buttonSave = (Button) rootView.findViewById(R.id.save);
        buttonSave.setOnClickListener(buttonListener);
        agentNameView = (EditText) rootView.findViewById(R.id.agentname);
        houseArea = (EditText) rootView.findViewById(R.id.harea);
        houseprice = (EditText) rootView.findViewById(R.id.price);
        notes = (EditText) rootView.findViewById(R.id.Notes);
        addPic = (Button) rootView.findViewById(R.id.AddPic);
        addPic.setOnClickListener(buttonListener);
        addVideo = (Button) rootView.findViewById(R.id.AddVideo);
        addVideo.setOnClickListener(buttonListener);
        addPic.setClickable(false);
        addVideo.setClickable(false);
       
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
	    	
	    		if (v.getId() == R.id.save) {
	    			handleSave();
	    			/*testDb testdb1 = new testDb(context);
	    			testdb1.insert();
	    			testdb1.getData();
	    			testdb1.updateData();
*/	    		} else if (v.getId() == R.id.AddPic) {
					finalMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
					Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					camera.putExtra(MediaStore.EXTRA_OUTPUT, finalMediaUri);
					startActivityForResult(camera, HouseConstants.PIC_INTENT);
				} else if (v.getId() == R.id.AddVideo) {
					finalMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
					 Intent camera = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
					camera.putExtra(MediaStore.EXTRA_OUTPUT, finalMediaUri);
					startActivityForResult(camera, HouseConstants.VIDEO_INTENT);
				}
	    	
	    	//context.getContentResolver().insert(url, values);
	    }
	    
	    
	    
	    
	    
	    /**
	     * Creating file uri to store image/video
	     */
	    public Uri getOutputMediaFileUri(int type) {
	        return Uri.fromFile(getOutputMediaFile(type));
	    }
	     
	    /*
	     * returning image / video
	     */
	    private  File getOutputMediaFile(int type) {
	     
	        // External sdcard location
	        File mediaStorageDir = new File(
	                Environment
	                        .getExternalStorageDirectory(), folderName);
	     
	        // Create the storage directory if it does not exist
	        if (!mediaStorageDir.exists()) {
	            if (!mediaStorageDir.mkdirs()) {
	                
	                return null;
	            }
	        }
	     
	        // Create a media file name
	        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
	                Locale.getDefault()).format(new Date());
	        File mediaFile;
	        if (type == MEDIA_TYPE_IMAGE) {
	            mediaFile = new File(mediaStorageDir.getPath() + File.separator
	                    + folderName+"/"+"IMG_" + timeStamp + ".jpg");
	        } else if (type == MEDIA_TYPE_VIDEO) {
	            mediaFile = new File(mediaStorageDir.getPath() + File.separator
	                    +folderName+ "/"+ "VID_" + timeStamp + ".mp4");
	        } else {
	            return null;
	        }
	     
	        return mediaFile;
	    }

		private void handleSave() {
			// TODO Auto-generated method stub
			checkValues();
			ContentValues newValues = new ContentValues();

			// Sets the values of each column and inserts the word.
			newValues.put(Auth.ItemsColumn.COL_AGENT_NAME, agentName);
			newValues.put(Auth.ItemsColumn.COL_HOUSE_AREA, houArea);
			newValues.put(Auth.ItemsColumn.COL_NOTES, hnotes);
			newValues.put(Auth.ItemsColumn.COL_PRICE, price);
			newValues.put(Auth.ItemsColumn.COL_SYNCED, HouseConstants.NOT_SYNC);
			newValues.put(Auth.ItemsColumn.COL_USERNAME, HouseConstants.TEMP_USERNAME);
			folderName = getNewFolderName(0);
			newValues.put(Auth.ItemsColumn.COL_FOLDERNAME, folderName);
			newValues.put(Auth.ItemsColumn.COL_LAT, tracker.getLatitude());
			newValues.put(Auth.ItemsColumn.COL_LAT, tracker.getLongitude());
			
			setContext();
			try {
					context.getContentResolver().insert(
							LC_CONTENT_URI,   // the user dictionary content URI
							newValues);
			} catch (Exception e) {
				e.printStackTrace();
			}
			addPic.setClickable(true);
	        addVideo.setClickable(true);
			sendSyncRequest();
			
		}

		

		private void setContext() {
			// TODO Auto-generated method stub
			lcontext.AgentName = agentName;
			lcontext.HouseArea = houArea;
			lcontext.notes = hnotes;
			lcontext.price = price;
			lcontext.folderName = folderName;
			lcontext.lat = tracker.getLatitude();
			lcontext.longi = tracker.getLongitude();
		}

		private boolean checkValues() {
			// TODO Auto-generated method stub
		
			agentName = agentNameView.getText().toString();
			houArea = Integer.parseInt(houseArea.getText().toString());
			price = Integer.parseInt(houseprice.getText().toString());
			hnotes = notes.getText().toString();
			if (agentName == null || agentName.length() == 0) {
				agentName = new String("temp");
			}
			if (houArea  == 0) {
				houArea = 0;
			}
			if (price == 0) {
				price = 0;
			}
			if (hnotes == null || hnotes.length() == 0) {
				hnotes = new String("temp");
			}
			return true;
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // if the result is capturing Image
	    if (requestCode == HouseConstants.PIC_INTENT) {
	    	if (resultCode == Activity.RESULT_OK) {
	    		sendSyncRequest();
	    	}
	    } else if (requestCode == HouseConstants.VIDEO_INTENT) {
	    	if (resultCode == Activity.RESULT_OK) {
	    		sendSyncRequest();
	    	}
	    } else {
	    	
	    }
	}
	
	private void sendSyncRequest() {
		// TODO Auto-generated method stub
		//  int uploadFilePath(in String url, in String folderName, in String file, in ICallBack cb );
	}
	
	private class CallBack implements ICallBack {

		
		@Override
		public int uploadRequest(long requestId, String file, int progress)
				throws RemoteException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public IBinder asBinder() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	
	
}
