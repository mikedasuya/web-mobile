
package com.househelper.ui;

import com.dropbox.sync.android.DbxAccountManager;
import com.househelper.application.HouseHelperApplication;
import com.househelper.interfaces.ClickListenerInterface;


import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
 
public class MainActivity extends Activity implements ClickListenerInterface {
    // Declare Tab Variable
    ActionBar.Tab tab1, tab2;
    
   // Fragment fragmentTab3 = new FragmentTab3();
    private DbxAccountManager mDbxAcctMgr;
    HouseHelperApplication app ;
    Fragment fragmentsnapshot = new SnapshotFragment();
    HistoryFragment fragmentHistory = new HistoryFragment();; 
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Resources res = this.getResources();
		String appKey = String.format(res.getString(R.string.app_key));
		String appSecret = String.format(res.getString(R.string.app_secret));
		mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), appKey, appSecret);
		if (!mDbxAcctMgr.hasLinkedAccount()) {
			mDbxAcctMgr.startLink(this, 0);
		}
		app = (HouseHelperApplication) getApplication();
		
	    setContentView(R.layout.main);
		
		// Asking for the default ActionBar element that our platform supports.
		ActionBar actionBar = getActionBar();
		 
        // Screen handling while hiding ActionBar icon.
        actionBar.setDisplayShowHomeEnabled(false);
 
        // Screen handling while hiding Actionbar title.
        actionBar.setDisplayShowTitleEnabled(false);
 
        // Creating ActionBar tabs.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 
        // Setting custom tab icons.
        /*bmwTab = actionBar.newTab().setIcon(R.drawable.bmw_logo);
        toyotaTab = actionBar.newTab().setIcon(R.drawable.toyota_logo);
        fordTab = actionBar.newTab().setIcon(R.drawable.ford_logo);*/
        tab1 = actionBar.newTab();
        tab2 = actionBar.newTab();
        // Setting tab listeners.
        tab1.setTabListener(new TabListener(fragmentsnapshot));
        tab2.setTabListener(new TabListener(fragmentHistory));
        //fordTab.setTabListener(new TabListener(fordFragmentTab));
       
        // Adding tabs to the ActionBar.
        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        //initialise();
		
        //actionBar.addTab(Tab3);
    }
    
    
    
    void initialise() {
    	ProfileFragment newFragment = new ProfileFragment();
	 	FragmentTransaction transaction = MainActivity.this.getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        // Commit the transaction
        //transaction.commit();
    }

	@Override
	public void onClick(int id) {
		// TODO Auto-generated method stub
		if (id == R.id.bsnapshot) {
			 initialise();
		}
			
	} //onClick
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == 0) {
	        if (resultCode == Activity.RESULT_OK) {
	            // ... Start using Dropbox files.
	        	
	        	app.setDropBoxManager(mDbxAcctMgr);
	        } else {
	            // ... Link failed or was cancelled by the user.
	        }
	    } else {
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	}	
	
	/*@Override
	public void onDestroy() 
	 {
	    super.onDestroy(); 
	    Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));  
	    FragmentTransaction ft = MainActivity.this.getFragmentManager().beginTransaction();
	    ft.remove(fragment);
	    ft.commit();
	}*/
	
}