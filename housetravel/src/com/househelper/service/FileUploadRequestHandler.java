package com.househelper.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.househelper.common.HouseConstants;
import com.owncloud.android.lib.common.network.OnDatatransferProgressListener;
import com.owncloud.android.lib.common.operations.OnRemoteOperationListener;
import com.owncloud.android.lib.common.operations.RemoteOperation;
import com.owncloud.android.lib.common.operations.RemoteOperationResult;


public class FileUploadRequestHandler implements OnRemoteOperationListener, OnDatatransferProgressListener {
	
	private static final String LOG_TAG = "FileUploadRequestHandler";
	ICallBack callBack;
	String fileName;
	long requestId;
	Handler mServiceCallBack;
	public FileUploadRequestHandler(ICallBack cbl, String file, long request, Handler serviceCallBack) {
		// TODO Auto-generated constructor stub
		callBack = cbl;
		fileName = file;
		requestId = request;
		mServiceCallBack = serviceCallBack;
	}

	@Override
	public void onTransferProgress(long progressRate,
			long totalTransferredSoFar, long totalToTransfer,
			String fileAbsoluteName) {
		// TODO Auto-generated method stub
		final long percentage = (totalToTransfer > 0 ? totalTransferredSoFar * 100 / totalToTransfer : 0);
		//final boolean upload = fileName.contains(getString(R.string.upload_folder_path));
		Log.d(LOG_TAG, "progressRate " + percentage);
		Message msg = new Message();
		msg.arg1 = HouseConstants.OPERATION_FILE_UPLOAD_INPROGRESS;
		msg.arg2 = (int) percentage;
		Bundle bundle = new Bundle();
		bundle.putLong(HouseConstants.REQUEST_ID_STRIND, requestId);
		msg.setData(bundle);
		mServiceCallBack.sendMessage(msg);
		
	}

	@Override
	public void onRemoteOperationFinish(RemoteOperation caller,
			RemoteOperationResult result) {
		Message msg = new Message();
		
		// TODO Auto-generated method stub
		if (result.isSuccess()) {
			msg.arg1 = HouseConstants.OPERATION_FILE_UPLOAD_SUCCESS;
			Bundle bundle = new Bundle();
			bundle.putLong(HouseConstants.REQUEST_ID_STRIND, requestId);
			msg.setData(bundle);
			mServiceCallBack.sendMessage(msg);
			try {
				callBack.uploadRequest(requestId, fileName, HouseConstants.OPERATION_FILE_UPLOAD_SUCCESS);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			msg.arg1 = HouseConstants.OPERATION_FILE_UPLOAD_FAILURE;
			Bundle bundle = new Bundle();
			bundle.putLong(HouseConstants.REQUEST_ID_STRIND, requestId);
			msg.setData(bundle);
			mServiceCallBack.sendMessage(msg);
			try {
				callBack.uploadRequest(requestId, fileName, HouseConstants.OPERATION_FILE_UPLOAD_FAILURE);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}