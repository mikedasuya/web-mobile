package com.example.com.location.tracker;

import com.example.com.location.common.Common;
import com.example.com.location.tracker.service.ExampleService;
import com.example.com.location.tracker.service.GPSTracker;
import com.example.com.location.tracker.service.ICallBack;
import com.example.com.location.tracker.service.IServerConnection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;


public class NewUILocation extends Activity {
	
	 final Handler handler=new Handler();
	 Animation an;
	 IServerConnection serverCon = null;
	 CallBack cb;
	 String emailForService = null;
	 int freqf = 5000;
	 GPSTracker mGPS = new GPSTracker(this);
	 Animation am =  null;
	 
@Override
protected void onCreate(Bundle savedInstanceState) {
	
	Log.v("tracker", "onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity_1);
    //TextView text = (TextView) findViewById(R.id.texts);
    
    if(mGPS.canGetLocation ){
    	mGPS.getLocation();
   //text.setText("Lat"+mGPS.getLatitude()+"Lon"+mGPS.getLongitude());
    } else {
      //  text.setText("Unabletofind");
        System.out.println("Unable");
    }
    Intent in = new Intent(this, ExampleService.class);
	addListeners();
	cb = new CallBack();
	//startService(in);
	bindService(in, connectionService, Context.BIND_AUTO_CREATE);
	Log.i("MY", "FINISHED OnCreate");
	
	
}

private void addListeners() {
	// TODO Auto-generated method stub
	Button bt1 = (Button) findViewById(R.id.togglebuttonStart);
	bt1.setOnClickListener( buttonListener);
	Button bt2 = (Button) findViewById(R.id.togglebuttongps);
	bt2.setOnClickListener( buttonListener);
	Button bt3 = (Button) findViewById(R.id.togglebuttonFrequency);
	bt3.setOnClickListener( buttonListener);
	Button bt4 = (Button) findViewById(R.id.togglebuttonEmail);
	bt4.setOnClickListener( buttonListener);
	
	
}

OnClickListener buttonListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
    	switch (v.getId()) {
        case R.id.togglebuttonStart:
        	handleStartStopButton();
           // Toast.makeText(getApplicationContext(), "Plus is clicked" + "+", Toast.LENGTH_SHORT).show(); 
            break;
        case R.id.togglebuttongps:
        	handleGPsButton();
            //Toast.makeText(getApplicationContext(),"Minus is clicked" + "-", Toast.LENGTH_SHORT).show();
            break;
        case R.id.togglebuttonFrequency:
        	handleButtonClickFrequency();
        	break;
        case R.id.togglebuttonEmail:
        	handleButtonClickEmail();
        	break;
        default:
            break;
        }
           
    }
    
   


	private void handleGPsButton() {
		// TODO Auto-generated method stub
		final ComponentName toLaunch = new ComponentName("com.android.settings","com.android.settings.SecuritySettings");
        final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
	}




	void handleButtonClickFrequency() {
    	Intent i = new Intent(NewUILocation.this, SetFrequency.class);
    	startActivityForResult(i, 2);
     	
    }
    
    void handleButtonClickEmail() {
    	Intent i = new Intent(NewUILocation.this, AddEmail.class);
    	String email = null;
    	 if (serverCon != null) {
     		email = getEmail();
     		i.putExtra(Common.Email, email);
        	startActivityForResult(i, 1);
     	} else {
     		showUI("Connection not set Try again");
     	}
    	
    }
  
};

@SuppressLint("NewApi")
private void handleStartStopButton() {
	// TODO Auto-generated method stub
	Button tbutton = (Button) findViewById(R.id.togglebuttonStart);
	String email = getEmail();
	int fre = getFreq();
	if (email == null || email.isEmpty() || (email.compareTo(Common.NULL) == 0)) {
		showUI("Email Tracking cannot be Empty");
	} else {
		String s = new String((String) tbutton.getText());
		if (s.compareTo("Start") == 0 ) {
			if (!isMyServiceRunning()) {
				startTracking();
			}
			tbutton.setText("Stop");
		} else if (s.compareTo("Stop") == 0) {
			//stopTracking();
			tbutton.setText("Start");
			if (serverCon != null) {
				try {
					serverCon.stopTracking(getEmail());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
			
	}
	
	
}

private void showUI(String string) {
	// TODO Auto-generated method stub
	Toast.makeText(NewUILocation.this, string, Toast.LENGTH_LONG).show();
}



private int getFrequencyFromString(String result) {
	// TODO Auto-generated method stub
	int resultVal = 5000;
	if (result.contains("5 seconds")) {
		resultVal = 5000;
	} else if (result.contains("10 seconds")) {
		resultVal = 10000;
	} else if (result.contains("30 seconds")) {
		resultVal = 30000;
	} else if (result.contains("60 seconds")) {
		resultVal = 60000;
	} else if (result.contains("5 minutes")) {
		resultVal = 300000;
	} else if (result.contains("10 minutes")) {
		resultVal = 600000;
	} else if (result.contains("30 minutes")) {
		resultVal = 1800000;
	} else if (result.contains("60 minutes")) {
		resultVal = 3600000;
	}
	return resultVal;
}

private String getStringFromFrequency(int result) {
	// TODO Auto-generated method stub
	String resultVal = "5 seconds";
	if (result == 5) {
		//resultVal = 5000;
	} else if (result == 10000) {
		resultVal = "10 sec";
	} else if (result == 30000) {
		resultVal = "30 sec";
	} else if (result == 60000) {
		resultVal = "60 sec";
	} else if (result == 300000) {
		resultVal = "5 min";
	} else if (result == 600000) {
		resultVal = "10 min";
	} else if (result == 1800000) {
		resultVal = "30 min";
	} else if (result == 3600000) {
		resultVal = "10 min";
	}
	return resultVal;
}


private String getEmail() {
	// TODO Auto-generated method stub
	String email = null;
	 if (serverCon != null) {
  		try {
  			email = serverCon.getEmail();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }
	 return email;
}

@SuppressLint("NewApi")
private void startTracking() {
	// TODO Auto-generated method stub
	
	if (serverCon != null) {
		try {
			String email = getEmail();
			int fre = getFreq();
			if (email == null || email.isEmpty() || (email.compareTo(Common.NULL) == 0)) {
				showUI("Email Tracking cannot be Empty");
			} else {
				serverCon.startTracking(email, freqf);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

private int getFreq() {
	// TODO Auto-generated method stub
	return freqf;
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	  if (requestCode == 1) {

	     if(resultCode == RESULT_OK){      
	         String result=data.getStringExtra("result");
	         emailForService = result;
	         try {
	        	if (serverCon != null) {
	        		serverCon.setEmail(emailForService);
	        	} else {
	        		showUI("Retry , Serv Conn problem");
	        	}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     }
	     if (resultCode == RESULT_CANCELED) {    
	         //Write your code if there's no result
	     }
	  } else if (requestCode == 2) {
		  if(resultCode == RESULT_OK){      
 	         String result = data.getStringExtra("result");
 	         int freq = getFrequencyFromString(result);
 	         freqf = freq;
 	         Log.v("tracker", " UI freq send to service" + freq);
 	         if (serverCon != null) {
 	        	 try {
					serverCon.setFrequency(freqf);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 	         }
		  }
	  }
}

private boolean isMyServiceRunning() {
	Log.v("tracker", "is My service Running");
	try {
		if (serverCon != null) {
				if (serverCon.isTracking()) {
					return true;
				}
		}
	} catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return false;
}

@Override
public void onStart() {
	Log.v("tracker", "onStart");
	Button tbutton = (Button) findViewById(R.id.togglebuttonStart);
	if(isMyServiceRunning()) {
		tbutton.setText("Stop");
	} else {
		tbutton.setText("Start");
	}
	super.onStart();
	//setText();
}

@Override
protected void onResume() {
	super.onResume();
	
	setFreqText();
	setStatusText();
	setNetworkText();
	if (serverCon != null) {
		try {
			serverCon.syncUI();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	setEmailText();

}

@Override
protected void onDestroy() {
	super.onDestroy();
	unbindService(connectionService);

}

void setNetworkText() {
	mGPS.getLocation();
	TextView rr = (TextView)findViewById(R.id.textNetwork1);
	if (rr != null) {
		 if(mGPS.canGetLocation){
		    	rr.setText("On");	 	
		 } else {
			 rr.setText("Off");	
		     
		 }
	}
	
}
void setStatusText() {
	TextView rr = (TextView)findViewById(R.id.textStatus1);
	if (rr != null) {
		if (isMyServiceRunning()) {
		    rr.setText("On");
		} else {
			 rr.setText("Off");
		}
	}
}

void setFreqText() {
	TextView rr1 = (TextView)findViewById(R.id.textFrequency1);
	if (rr1 != null) {
		if (serverCon != null) {
			int freq = 5000;
			try {
				freq = serverCon.getFrequency();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String val = getStringFromFrequency(freq);
			rr1.setText(val);
			
		}
	}
	
}

void setEmailText() {
	TextView rr1 = (TextView)findViewById(R.id.textStatus1Email);
	if (rr1 != null) {
		String email = getEmail();
		rr1.setText(email);
	}
}

private void startAnimation() {
	// TODO Auto-generated method stub
	if (am == null) {
		am = new Animation(this);
	}
	if (isMyServiceRunning()) {
		if (am.getState() == Common.ANIMATION_STATE_NOT_RUNNING) {
			am.startAnimation();
		}
	}
	
}

private void stopAnimation() {
	// TODO Auto-generated method stub
	if (am != null) {
		am.stopAnimation();
	}	
	
}

class CallBack extends ICallBack.Stub {

	@Override
	public void result(String value, int returnCode) throws RemoteException {
		// TODO Auto-generated method stub
		if (returnCode == Common.ERROR_SERVER_PROBLEM) {
			//showDialog();
			//finish();
		} else if (returnCode == Common.GEO_NOT_FOUND) {
			runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	Toast.makeText(getApplication(), "Geo Not found try again", Toast.LENGTH_LONG).show();
                    
                }
            });
			finish();
			
		} else if (returnCode == Common.START_TRACKING) {
			Log.e("tracker", "-------------Location callb start tracking  ui");
			
			runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   	Button tbutton = (Button) findViewById(R.id.togglebuttonStart);
                   	if (tbutton != null) {
                   		tbutton.setText("Stop");
                   	}
                   	setFreqText();
                	setStatusText();
                	setNetworkText();
                   	startAnimation();
                   	setEmailText();
                }
            });
			
			  
		} else if (returnCode == Common.STOP_TRACKING) {
			
			runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	Log.e("tracker", "-------------Location call bcack stop tracking ui");
               		Button tbutton = (Button)findViewById(R.id.togglebuttonStart);
               		if (tbutton != null) {
               			tbutton.setText("Start");
               		}
               		setFreqText();
                	setStatusText();
                	setNetworkText();
        			stopAnimation();
        			setEmailText();
                }
            });
			
		} else if (returnCode == Common.SYNC_UI) {
			runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                	Log.e("tracker", "-------------Location call bcack stop tracking ui");
	                	setFreqText();
	                	setStatusText();
	                	setNetworkText();
	                	startAnimation();
	                	setEmailText();
	                }

					
	            });
				
			}
		
	}
	@Override
	 public boolean equals(Object obj) {
			return true;
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
			updateState();
			serverCon.syncUI();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateState() {
		// TODO Auto-generated method stub
		emailForService = getEmail();
		
		
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
		
	}
	
};




}