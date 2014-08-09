package com.househelper.service;

import java.io.File;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.widget.TextView;

import com.househelper.common.HouseConstants;
import com.househelper.ui.R;
import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.OwnCloudClientFactory;
import com.owncloud.android.lib.common.accounts.AccountUtils;
import com.owncloud.android.lib.resources.files.FileUtils;
import com.owncloud.android.lib.resources.files.UploadRemoteFileOperation;


public class FileUploadRequest implements Request {
	String url = null;
	ICallBack cbl;
	String fileName = null;
	private OwnCloudClient mClient;
	Context context;
	Uri serverUri = Uri.parse(context.getString(R.string.server_base_url) + AccountUtils.WEBDAV_PATH_4_0);
	String usern = context.getString(R.string.username);
	String passw = context.getString(R.string.username);
	FileUploadRequestHandler handler;
	String folderName;
	Handler mHandler;
	int type = HouseConstants.FILE_UPLOAD;
	Handler serviceCallBackHandler;
	
	public int getType() {
		return type;
	}
	
	
	FileUploadRequest(String ur, 
			ICallBack cb,
			String folder, 
			String fName, Context cont , long requestId, Handler serviceCallBack) {
		url = ur;
		cbl = cb;
		context = cont;
		fileName = fName;
		folderName = folder;
		mClient.setBasicCredentials(usern, passw);
		handler = new FileUploadRequestHandler(cbl, fileName, requestId, serviceCallBack);
		mClient = OwnCloudClientFactory.createOwnCloudClient(serverUri, context, true);
		mHandler = new Handler();
		//serviceCallBackHandler = serviceCallBack;
	}
	//FileUploadRunnbale
	@Override
	public void run() {
		// TODO Auto-generated method stub
		File upFolder = new File(folderName);
    	File fileToUpload = new File(upFolder + folderName + "/" + fileName); 
    	String remotePath = FileUtils.PATH_SEPARATOR + folderName + fileToUpload.getName(); 
    	String mimeType = context.getString(R.string.sample_file_mimetype);
    	UploadRemoteFileOperation uploadOperation = new UploadRemoteFileOperation(fileToUpload.getAbsolutePath(), remotePath, mimeType);
    	uploadOperation.addDatatransferProgressListener(handler);
    	uploadOperation.execute(mClient, handler, mHandler);
	}
	
}