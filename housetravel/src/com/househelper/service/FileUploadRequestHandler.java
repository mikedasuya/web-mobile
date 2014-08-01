package com.househelper.service;

import com.owncloud.android.lib.common.network.OnDatatransferProgressListener;
import com.owncloud.android.lib.common.operations.OnRemoteOperationListener;
import com.owncloud.android.lib.common.operations.RemoteOperation;
import com.owncloud.android.lib.common.operations.RemoteOperationResult;

public class FileUploadRequestHandler implements OnRemoteOperationListener, OnDatatransferProgressListener {
	
	ICallBack callBack;
	public FileUploadRequestHandler(ICallBack cbl) {
		// TODO Auto-generated constructor stub
		callBack = cbl;
	}

	@Override
	public void onTransferProgress(long progressRate,
			long totalTransferredSoFar, long totalToTransfer,
			String fileAbsoluteName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRemoteOperationFinish(RemoteOperation caller,
			RemoteOperationResult result) {
		// TODO Auto-generated method stub
		
	}
	
}