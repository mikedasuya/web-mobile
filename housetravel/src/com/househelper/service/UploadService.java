package com.househelper.service;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFileSystem;
import com.househelper.application.HouseHelperApplication;
import com.househelper.common.HouseConstants;
import com.househelper.service.data.NotificationObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

public class UploadService extends Service {

//Time Out use case:
//In both
	
  
  protected static final String TAG = "UploadService";
HashMap<Long, Request> mapRequestId = new HashMap<Long, Request>();
  NotificationHandler mNotifyHandler = null;
  private  BlockingQueue queue = new ArrayBlockingQueue(1024);
  NotificationHandler notificationManager = null;
  Thread listenerThread = null;
  HouseHelperApplication mAppObj = null;
  DbxAccountManager mDropBoxObj = null;
  
  Handler mHandler = new Handler() {
	  public void 	handleMessage(Message msg) {
		  if (msg.arg1 == HouseConstants.OPERATION_FILE_UPLOAD_SUCCESS ) {
			  Bundle data = msg.getData();
			  NotificationObject obj = new NotificationObject();
			  obj.setId((Long)data.get(HouseConstants.REQUEST_ID_STRIND));
			  obj.setOperation(HouseConstants.OPERATION_FILE_UPLOAD_SUCCESS);
			  notificationManager.handleObject(obj);
			 			 
		  } else if (msg.arg1 == HouseConstants.OPERATION_FILE_UPLOAD_INPROGRESS) {
			  Bundle data = msg.getData();
			  NotificationObject obj = new NotificationObject();
			  obj.setId((Long) data.get(HouseConstants.REQUEST_ID_STRIND));
			  obj.setOperation(HouseConstants.OPERATION_FILE_UPLOAD_SUCCESS);
			  obj.setProgress(msg.arg2);
			  notificationManager.handleObject(obj);
			  
		  } else if (msg.arg1 == HouseConstants.OPERATION_FILE_UPLOAD_FAILURE) {
			  Bundle data = msg.getData();
			  NotificationObject obj = new NotificationObject();
			  obj.setId((Long) data.get(HouseConstants.REQUEST_ID_STRIND));
			  obj.setOperation(HouseConstants.OPERATION_FILE_UPLOAD_SUCCESS);
			  notificationManager.handleObject(obj);
			  			  
		  } else if (msg.arg1 == HouseConstants.OPERATION_FILE_UPLOAD_START) {
			  Bundle data = msg.getData();
			  NotificationObject obj = new NotificationObject();
			  obj.setId((Long) data.get(HouseConstants.REQUEST_ID_STRIND));
			  obj.setOperation(HouseConstants.OPERATION_FILE_UPLOAD_SUCCESS);
			  notificationManager.handleObject(obj);
			  
			  
		  } else if (msg.arg1 == HouseConstants.OPERATION_FILE_UPLOAD_DATA_CONNECTIVITY_ERROR) {
			  Bundle data = msg.getData();
			  NotificationObject obj = new NotificationObject();
			  Long id =  (Long) data.get(HouseConstants.REQUEST_ID_STRIND);
			  obj.setId(id);
			  obj.setOperation(HouseConstants.OPERATION_FILE_UPLOAD_DATA_CONNECTIVITY_ERROR);
			  notificationManager.handleObject(obj);
			  
		  }
		  
	  }

	
  };
  ConsumerThreadService consumer = new ConsumerThreadService(queue, mHandler);
  
  

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    //TODO do something useful
	  mAppObj = (HouseHelperApplication) this.getApplication();
	  
    return Service.START_NOT_STICKY;
  }

  

@Override
public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
	synchronized (this) {
		if (listenerThread == null) {
			listenerThread = new Thread(consumer);
			listenerThread.start();
		}
	}
	return mBinder;
}

@Override
public boolean onUnbind(Intent intent)  {
	
	//TODO:Request req = new Request();
	
	return false;
	
}

public long randInt(int min, int max) {

    // NOTE: Usually this should be a field rather than a method
    // variable so that it is not re-seeded every call.
	java.util.Date date = new java.util.Date();
	long randomNum = date.getTime();
    //Random rand = new Random();

    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
    //int randomNum = rand.nextInt((max - min) + 1) + min;

    return randomNum;
}



private boolean checkDataConnection() {
	// TODO Auto-generated method stub

	NetworkInfo info = (NetworkInfo) ((ConnectivityManager) this
            .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

    if (info == null || !info.isConnected()) {
        return false;
    }
    if (info.isRoaming()) {
        // here is the roaming option you can change it if you want to
        // disable internet while roaming, just return false
        return false;
    }
    return true;

}

private void sendStartUploadFileMessage(long id) {
	// TODO Auto-generated method stub
	Message msg = new Message();
	msg.arg1 = HouseConstants.OPERATION_FILE_UPLOAD_START;
	Bundle bund = new Bundle();
	bund.putLong(HouseConstants.REQUEST_ID_STRIND, id);
	msg.setData(bund);
	mHandler.sendMessage(msg);
	
}

private void sendDataConnectivityErrorNotification(long id) {
	// TODO Auto-generated method stub
	Message msg = new Message();
	msg.arg1 = HouseConstants.OPERATION_FILE_UPLOAD_DATA_CONNECTIVITY_ERROR;
	Bundle bund = new Bundle();
	bund.putLong(HouseConstants.REQUEST_ID_STRIND, id);
	msg.setData(bund);
	mHandler.sendMessage(msg);
}

private final IUploadRequest.Stub mBinder = new IUploadRequest.Stub() {
    
    
	@Override
	public int uploadFilePath(String fileName, String folderName, ICallBack cb)
			throws RemoteException {
		// TODO Auto-generated method stub
		if (notificationManager == null) {
			notificationManager = new NotificationHandler(getApplicationContext());
		}
		Log.d(TAG, "--servce---filename + foldername ---" + fileName + folderName);
		int result = 0;
		long Id = 0;
		Request req = new FileUploadRequest(mDropBoxObj,
														cb,
														folderName,
														fileName,
														Id
														);
			//check for duplicate entry
				mapRequestId.put(Id, req);
			//	sendStartUploadFileMessage(Id);
			
				try {
					queue.put(req);
					
				} catch (InterruptedException e) {
			// TODO Auto-generated catch block
					e.printStackTrace();
					result = HouseConstants.OPERATION_FILE_UPLOAD_INTERNAL_ERROR;
				}
			/*}  else {
			//check data connection
				result = HouseConstants.OPERATION_FILE_UPLOAD_DATA_CONNECTIVITY_ERROR;
				sendDataConnectivityErrorNotification(Id);
			
			}*/
		  // if some thing is null
		return result;
	}
	
	

	



	@Override
	public int uploadEntry(String url, int id, ICallBack cb)
			throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int uploadDbEntry(String url, int Map, ICallBack cb)
			throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
};

} 