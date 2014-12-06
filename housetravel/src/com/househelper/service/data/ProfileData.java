package com.househelper.service.data;

public class ProfileData {
	String mAgentName;
	int mId;
	int mArea;
	int mPrice;
	String mNotes;
	
	public ProfileData() {
		
	}
	
	void setAgentName(String mName) {
		mAgentName = mName;
	}
	
	String getAgentName() {
		return mAgentName;
	}
	
	void setHouseArea(int area) {
		mArea = area;
	}
	
	void setPrice(int price) {
		mPrice = price;
	}
	
	void setNodes(String mn) {
		mNotes = mn;
	}
	
	int getArea() {
		return mArea;
	}
	
	int getPrice() {
		return mPrice;
	}
	
	String getNotes() {
		return mNotes;
	}
 
}