package com.househelper.common;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.househelper.service.ICallBack;
import com.househelper.service.IUploadRequest;
import com.househelper.service.UploadService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

public class ConnectionManager {
	Context context;
	private  BlockingQueue queue = new ArrayBlockingQueue(1024);
	public static ConnectionManager con; 
	boolean mConnected = false;
	IUploadRequest uploadInterface = null;

	boolean mRun = true;
	
	private class RequestHandler implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (mRun) {
				try {
					Request obj = (Request) queue.take();
					obj.run();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	Thread th = new Thread(new RequestHandler());
	
	private class Request implements Runnable {
		 String url = null;
		 String folderName = null;
		 String fileName = null;
		 ICallBack cb = null;
		 Request() {
			 
		 }
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				uploadInterface.uploadFilePath(url, folderName, fileName, cb);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private ConnectionManager(Context context) {
		this.context = context;
		Intent intent = new Intent(context, UploadService.class);
	    context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	
	public static synchronized ConnectionManager getInstance(Context context) {
		if (con == null) {
			con = new ConnectionManager(context);
		}
		return con;
	}
	
	boolean sendMessageToService(String url, String folderName, String file, ICallBack cb ) {
		Request req = new Request();
		req.url = url;
		req.fileName = file;
		req.folderName = folderName;
		req.cb = cb;
		queue.add(req);
		return true;
	}
	
	 /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
           // LocalBinder binder = (LocalBinder) service;
           // mService = binder.getService();
           // mBound = true;
        	uploadInterface = IUploadRequest.Stub.asInterface(service);
        	mConnected = true;
        	startServing();
        }

        private void startServing() {
			// TODO Auto-generated method stub
			th.start();
		}

		@Override
        public void onServiceDisconnected(ComponentName arg0) {
            //mBound = false;
        	mConnected = false;
        }
    };

    public void stopConnection() {
    	mRun = false;
    	context.unbindService(mConnection);
    }
	
}