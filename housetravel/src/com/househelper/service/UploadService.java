package com.househelper.service;

import java.util.concurrent.BlockingQueue;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class UploadService extends Service {

  private  BlockingQueue queue;

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

private final IUploadRequest.Stub mBinder = new IUploadRequest.Stub() {
    
    
	@Override
	public int uploadFilePath(String url, String file, ICallBack cb)
			throws RemoteException {
		// TODO Auto-generated method stub
		FileUploadRequest req = new FileUploadRequest(url, cb, file, getApplicationContext());
		try {
			queue.put(req);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
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