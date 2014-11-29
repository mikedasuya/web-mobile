package com.househelper.connection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.househelper.service.ICallBack;
import com.househelper.service.IUploadRequest;
import com.househelper.service.Request;
import com.househelper.service.UploadService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;



public class ConnectionManager {
	private  BlockingQueue queue = new ArrayBlockingQueue(1024);
	IUploadRequest mUploadInterface = null;
	int WAIT_TIME = 2000;
	Context mContext = null;
	Thread th ;
	boolean mDisConnect = false;
	
	public ConnectionManager(Context cont ) {
		mContext = cont;
	}
	
	public void connect() {
		if (mContext != null) {
			Intent in = new Intent();
			Intent callService = new Intent(mContext, UploadService.class);
			mContext.bindService(callService, mConnection, Context.BIND_AUTO_CREATE);
			Consumer cons = new Consumer();
			th = new Thread(cons);
			th.start();
			mDisConnect = false;
		}
		
	}
	
	class DummyRequest implements Request {

		

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}

		

		@Override
		public String getFolderName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Uri getFileName() {
			// TODO Auto-generated method stub
			return null;
		}

		
		@Override
		public void setCallBackHandler(Handler mhandler) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public void disConnect() {
		if (mContext != null) {
			mContext.unbindService(mConnection);
			mUploadInterface = null;
			mDisConnect = true;
			Request obj = new DummyRequest();
			queue.add(obj);
			
		}
	}
	
	class Consumer implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (mDisConnect == false) {
				try {
					Request obj = (Request) queue.take();
					handleConsumerRequest(obj);
				} catch (InterruptedException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		private void consumeRequest(Request obj) {
			try {
				if (obj != null) {
					if (mUploadInterface != null) {
						Uri uri = obj.getFileName();
						try {
							if (uri != null && obj != null) {
								mUploadInterface.uploadFilePath(uri.toString(), obj.getFolderName(), null);
							}
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
											
					} else {
						Thread.sleep(WAIT_TIME);
						queue.add(obj);
					}
				}
							// TODO Auto-generated catch block
								} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void handleConsumerRequest(Request obj) {
			// TODO Auto-generated method stub
			consumeRequest(obj);
			
		}
		
	}
	
	public void addRequest(Request obj) {
		queue.add(obj);
	}
	
	private ServiceConnection mConnection = new ServiceConnection() {
	       

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mUploadInterface = IUploadRequest.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
    };

}