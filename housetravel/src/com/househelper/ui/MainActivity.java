
package com.househelper.ui;

import com.househelper.interfaces.ClickListenerInterface;
import com.househelper.profile.ProfileFragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
 
public class MainActivity extends Activity implements ClickListenerInterface {
    // Declare Tab Variable
    ActionBar.Tab Tab1, Tab2;
    Fragment fragmentsnapshot = new SnapshotFragment();
    Fragment fragmentHistory = new HistoryFragment();
   // Fragment fragmentTab3 = new FragmentTab3();
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTabs();
        //actionBar.addTab(Tab3);
    }
    
    void setTabs() {
    	setContentView(R.layout.main);
    	 
        ActionBar actionBar = getActionBar();
 
        // Hide Actionbar Icon
        actionBar.setDisplayShowHomeEnabled(false);
 
        // Hide Actionbar Title
        actionBar.setDisplayShowTitleEnabled(false);
 
        // Create Actionbar Tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 
        // Set Tab Icon and Titles
        Tab1 = actionBar.newTab().setText("Snapshot");
        Tab2 = actionBar.newTab().setText("History");
       // Tab3 = actionBar.newTab().setText("Tab3");
 
        // Set Tab Listeners
        Tab1.setTabListener(new TabListener(fragmentsnapshot));
        Tab2.setTabListener(new TabListener(fragmentHistory));
   
        // Add tabs to actionbar
        actionBar.addTab(Tab1);
        actionBar.addTab(Tab2);
    }

	@Override
	public void onClick(int id) {
		// TODO Auto-generated method stub
		if (id == R.id.bsnapshot) {
			 ProfileFragment newFragment = new ProfileFragment();
			 	FragmentTransaction transaction = MainActivity.this.getFragmentManager().beginTransaction();
	            // Replace whatever is in the fragment_container view with this fragment,
	            // and add the transaction to the back stack so the user can navigate back
	            transaction.replace(R.id.fragment_container, newFragment);
	            transaction.addToBackStack(null);
	            // Commit the transaction
	            transaction.commit();

		}
			
	} //onClick
	
		
	
}