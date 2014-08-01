package com.househelper.service;

import java.io.File;

import android.content.Context;
import android.net.Uri;

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
	
	
	FileUploadRequest(String ur, ICallBack cb, String fName, Context cont) {
		url = ur;
		cbl = cb;
		context = cont;
		fileName = fName;
		mClient.setBasicCredentials(usern, passw);
		handler = new FileUploadRequestHandler(cbl);
		mClient = OwnCloudClientFactory.createOwnCloudClient(serverUri, context, true);
	}
	//FileUploadRunnbale
	@Override
	public void run() {
		// TODO Auto-generated method stub
		File upFolder = new File(getCacheDir(), getString(R.string.upload_folder_path));
    	File fileToUpload = upFolder.listFiles()[0]; 
    	String remotePath = FileUtils.PATH_SEPARATOR + fileToUpload.getName(); 
    	String mimeType = getString(R.string.sample_file_mimetype);
    	UploadRemoteFileOperation uploadOperation = new UploadRemoteFileOperation(fileToUpload.getAbsolutePath(), remotePath, mimeType);
    	uploadOperation.addDatatransferProgressListener(this);
    	uploadOperation.execute(mClient, this, mHandler);
	}
	
}