package com.househelper.service;

import java.io.File;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.househelper.common.HouseConstants;
import com.househelper.ui.R;
import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.OwnCloudClientFactory;
import com.owncloud.android.lib.common.accounts.AccountUtils;
import com.owncloud.android.lib.resources.files.FileUtils;
import com.owncloud.android.lib.resources.files.UploadRemoteFileOperation;


public class FileUploadRequest implements Request {
	String mUrl = null;
	ICallBack mCallBackCaller;
	String mFileName = null;
	private OwnCloudClient mClient;
	Context context;
	Uri serverUri = Uri.parse(context.getString(R.string.server_base_url) + AccountUtils.WEBDAV_PATH_4_0);
	String usern = context.getString(R.string.username);
	String passw = context.getString(R.string.username);
	FileUploadRequestHandler mfileUploadCallBack;
	String mFolderName;
	Handler mhandlerOperationListener;
	int type = HouseConstants.FILE_UPLOAD;
	Handler mHandler;  ///todo use unknown check it
	Handler mCallBackHandler = null;
	
	public int getType() {
		return type;
	}
	
	Handler mHandlerFromHandlerOperationListener = new Handler() {
		  public void 	handleMessage(Message msg) {
			  mCallBackHandler.sendMessage(msg);
		  }
    };
	
	FileUploadRequest(String ur, 
			ICallBack cb,
			String folder, 
			String fName, Context cont , long requestId) {
		mUrl = ur;
		mCallBackCaller = cb;
		context = cont;
		mFileName = fName;
		mFolderName = folder;
		mClient.setBasicCredentials(usern, passw);
		mfileUploadCallBack = new FileUploadRequestHandler(requestId, mHandlerFromHandlerOperationListener);
		mClient = OwnCloudClientFactory.createOwnCloudClient(serverUri, context, true);
		mHandler = new Handler();
		
	}
	//FileUploadRunnbale
	@Override
	public void run() {
		// TODO Auto-generated method stub
		File upFolder = new File(mFolderName);
    	File fileToUpload = new File(upFolder + mFolderName + "/" + mFileName); 
    	String remotePath = FileUtils.PATH_SEPARATOR + mFolderName + fileToUpload.getName(); 
    	String mimeType = context.getString(R.string.sample_file_mimetype);
    	UploadRemoteFileOperation uploadOperation = new UploadRemoteFileOperation(fileToUpload.getAbsolutePath(), remotePath, mimeType);
    	uploadOperation.addDatatransferProgressListener(mfileUploadCallBack);
    	uploadOperation.execute(mClient, mfileUploadCallBack, mHandler);
	}


	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getFolderName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ICallBack getCallBack() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setCallBackHandler(Handler mhandler) {
		// TODO Auto-generated method stub
		mCallBackHandler = mHandler;
	}
	
}