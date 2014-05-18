package com.example.com.location.tracker;

import java.util.ArrayList;

import com.example.com.location.common.Common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class AddEmail extends Activity {
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    EditText mEdit;
    String value;
    
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        Button newPicButton = (Button)findViewById(R.id.SaveEmail1);
        newPicButton.setOnClickListener(btnListener);
        mEdit   = (EditText)findViewById(R.id.emailtxt);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString(Common.Email);
        }
        if (value != null && !value.isEmpty() && !(value.compareTo(Common.NULL) == 0)) {
        	mEdit.setText(value);
        }
        
    }
    
    

	private OnClickListener btnListener = new OnClickListener()
    {

        @SuppressLint("NewApi")
		public void onClick(View v)
        {   
        	if(v.getId() == R.id.SaveEmail1){
        		String value =  mEdit.getText().toString();
        		if (value == null || value.isEmpty()) {
        			showText("");
        		} else {
        			Intent returnIntent = new Intent();
        			 returnIntent.putExtra("result",value);
        			 setResult(RESULT_OK,returnIntent);     
        			 finish();
        		}
                //handle the click here
            }        
        }

		

		

    }; 
    
    private void showText(String val) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Email Field cannot be empty", Toast.LENGTH_LONG).show();
	} 
    

   
}