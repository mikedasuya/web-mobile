package com.househelper.service.data;

public class NotificationObject {
	
	Long mId;
	String mFileName;
	int mProgress;
	int mOperation;
	int mRequestId;

	
	public Long getId() {
		// TODO Auto-generated method stub
		return mId;
	}
	
	public void setId(Long id) {
		mId = id;
	}

	public void setOperation(int operationFileUploadSuccess) {
		// TODO Auto-generated method stub
		mOperation = operationFileUploadSuccess;
	}

	

	public int getOperation() {
		// TODO Auto-generated method stub
		return mOperation;
	}

	public int getRequestId() {
		// TODO Auto-generated method stub
		return mRequestId;
	}

	public void setProgress(int arg2) {
		// TODO Auto-generated method stub
		mProgress = arg2;
	}

	public int getProgress() {
		// TODO Auto-generated method stub
		return mProgress;
	}
	
	
}