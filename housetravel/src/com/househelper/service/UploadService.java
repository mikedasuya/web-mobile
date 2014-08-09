package com.househelper.service;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.househelper.common.HouseConstants;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

public class UploadService extends Service {

//Time Out use case:
//In both
	
  private  BlockingQueue queue = new ArrayBlockingQueue(1024);
  HashMap<Long, Request> mapRequestId = new HashMap<Long, Request>();
  NotificationHandler mNotifyHandler = null;
  
  Handler mHandler = new Handler() {
	  public void 	handleMessage(Message msg) {
		  if (msg.arg1 == HouseConstants.OPERATION_FILE_UPLOAD_SUCCESS ) {
			  Bundle data = msg.getData();
			  handleFileUploadResult(data, HouseConstants.OPERATION_FILE_UPLOAD_SUCCESS);
			 
		  } else if (msg.arg1 == HouseConstants.OPERATION_FILE_UPLOAD_INPROGRESS) {
			  handleFileUploadResult(msg.arg2);
		  } else if (msg.arg1 == HouseConstants.OPERATION_FILE_UPLOAD_FAILURE) {
			  Bundle data = msg.getData();
			  handleFileUploadResult(data, HouseConstants.OPERATION_FILE_UPLOAD_FAILURE);
		  } else if (msg.arg1 == HouseConstants.OPERATION_FILE_UPLOAD_START) {
			  handleFileUploadResult(msg.arg1);
			  
		  }
		  
	  }

	
  };
  
  public void handleFileUploadResult(Bundle data, int code) {
	  long id = data.getLong(HouseConstants.REQUEST_ID_STRIND);
	  mapRequestId.remove(id);
	  if (mNotifyHandler != null && code == HouseConstants.OPERATION_FILE_UPLOAD_FAILURE) {
		  mNotifyHandler.updateNotificationMessage(HouseConstants.OPERATION_FILE_UPLOAD_ERROR_STRING);
	  } else if (mNotifyHandler != null && code == HouseConstants.OPERATION_FILE_UPLOAD_SUCCESS) {
		  mNotifyHandler.updateNotificationMessage(HouseConstants.OPERATION_FILE_UPLOAD_SUCCESS_STRING);
	  }
  }
  
  private void handleFileUploadResult(int arg2) {
		// TODO Auto-generated method stub
	   if (arg2 == HouseConstants.OPERATION_FILE_UPLOAD_START) {
		   mNotifyHandler =  new NotificationHandler(getApplicationContext(), 1);
	   } else if (arg2 == HouseConstants.OPERATION_FILE_UPLOAD_INPROGRESS) {
		   if (mNotifyHandler != null) {
				mNotifyHandler.updateProgress(arg2);
			}   
	   }
		
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    //TODO do something useful
    return Service.START_NOT_STICKY;
  }

  

@Override
public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
	return mBinder;
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

private void sendStartUploadFile() {
	// TODO Auto-generated method stub
	Message msg = new Message();
	msg.arg1 = HouseConstants.OPERATION_FILE_UPLOAD_START;
	mHandler.sendMessage(msg);
	
}

private boolean checkDataConnection() {
	// TODO Auto-generated method stub
	return false;
}

private final IUploadRequest.Stub mBinder = new IUploadRequest.Stub() {
    
    
	@Override
	public int uploadFilePath(String url, String folderName, String file, ICallBack cb)
			throws RemoteException {
		// TODO Auto-generated method stub
		int result = 0;
		if (checkDataConnection()) {
			long Id = randInt(0, 100); 
			FileUploadRequest req = new FileUploadRequest(url,
				                                       cb,
				                                       folderName, 
				                                       file,
				                                       getApplicationContext(),
				                                       Id,
				                                       mHandler);
			//check for duplicate entry
			mapRequestId.put(Id, req);
			
			try {
				queue.put(req);
				result = HouseConstants.OPERATION_FILE_UPLOAD_INPROGRESS;
				sendStartUploadFile();
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
				result = HouseConstants.OPERATION_FILE_UPLOAD_INTERNAL_ERROR;
			}
		} else {
			//check data connection
			result = HouseConstants.OPERATION_FILE_UPLOAD_DATA_CONNECTIVITY_ERROR;
		}
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