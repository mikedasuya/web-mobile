package com.househelper.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.househelper.common.HouseConstants;
import com.househelper.ui.R;
import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.OwnCloudClientFactory;
import com.owncloud.android.lib.common.accounts.AccountUtils;
import com.owncloud.android.lib.resources.files.FileUtils;
import com.owncloud.android.lib.resources.files.UploadRemoteFileOperation;

import com.dropbox.sync.android.*;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxPath.InvalidPathException;


public class FileUploadRequest implements Request {
	
	private static final String TAG = "FileUploadRequest";
	ICallBack mCallBackCaller;
	String mFileName = null;
	String mFolderName;
	int id;
	Handler mResultCallBackHandler = null;
		
	//private DropboxAPI<AndroidAuthSession> mDBApi;
	private DbxAccountManager mDbxAcctMgr;
	
	
	
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
		
		try {
			addToDropBox(mFileName, mFolderName);
		} catch (InvalidPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	private void addToDropBox(String fileName, String folderName) throws InvalidPathException, IOException {
		// TODO Auto-generated method stub
		
		Log.d (TAG, "---------------file name + folder name" + fileName + folderName);
		DbxFile testFile = null;
		BufferedReader in = null;
		try {
			
			DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
			File inputFile = new File(fileName.toString());
			
			FileInputStream fis = new FileInputStream(inputFile);
			in = new BufferedReader(new InputStreamReader(fis));
	 
			/*FileWriter fstream = new FileWriter(dest, true);
			BufferedWriter out = new BufferedWriter(fstream);
	 */
			testFile= dbxFs.create(new DbxPath(fileName.toString()));
			String aLine = null;
			while ((aLine = in.readLine()) != null) {
				//Process each line and add output to Dest.txt file
				//testFile.writeFromExistingFile(file, shouldSteal)
				testFile.writeString(aLine);
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			testFile.close();
			in.close();
		}
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