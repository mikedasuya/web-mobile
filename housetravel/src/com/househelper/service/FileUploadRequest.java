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

import com.dropbox.sync.android.*;


public class FileUploadRequest implements Request {
	
	ICallBack mCallBackCaller;
	String mFileName = null;
	String mFolderName;
	int id;
	
		
	//private DropboxAPI<AndroidAuthSession> mDBApi;
	private DbxAccountManager mDbxAcctMgr;
	
	
	
	Handler mResultCallBackHandler = new Handler() {
		  public void 	handleMessage(Message msg) {
			  
		  }
    };
	
	FileUploadRequest(DbxAccountManager dropbox, 
			ICallBack cb,
			String folder, 
			String fName, long requestId) {
		
		mCallBackCaller = cb;
		mFileName = fName;
		mFolderName = folder;
		mDbxAcctMgr = dropbox;
		
		
	}
	//FileUploadRunnbale
	@Override
	public void run() {
		
		addToDropBox();
		// TODO Auto-generated method stub
		/*File upFolder = new File(mFolderName);
    	File fileToUpload = new File(upFolder + mFolderName + "/" + mFileName); 
    	String remotePath = FileUtils.PATH_SEPARATOR + mFolderName + fileToUpload.getName(); 
    	String mimeType = context.getString(R.string.sample_file_mimetype);
    	UploadRemoteFileOperation uploadOperation = new UploadRemoteFileOperation(fileToUpload.getAbsolutePath(), remotePath, mimeType);
    	uploadOperation.addDatatransferProgressListener(mfileUploadCallBack);
    	uploadOperation.execute(mClient, mfileUploadCallBack, mHandler);*/
	}


	private void addToDropBox() {
		// TODO Auto-generated method stub
		
	}
	
	
	


	@Override
	public String getFolderName() {
		// TODO Auto-generated method stub
		return mFolderName;
	}


	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
		return mFileName;
	}


		
	@Override
	public void setCallBackHandler(Handler mhandler) {
		// TODO Auto-generated method stub
		mResultCallBackHandler = mhandler;
	}
	
		
}