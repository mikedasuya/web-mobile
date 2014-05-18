package com.example.com.location.tracker;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class SetFrequency extends Activity {
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
   Spinner spinner1;
    
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.freq);
        Button newPicButton = (Button)findViewById(R.id.SaveFreq);
        newPicButton.setOnClickListener(btnListener);
       spinner1 = (Spinner) findViewById(R.id.spinner1);
       String [] fiilliste= getResources().getStringArray(R.array.frequency);
       ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
    			android.R.layout.simple_spinner_item, fiilliste);
    		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinner1.setAdapter(dataAdapter);
            
    }
    
    

	private OnClickListener btnListener = new OnClickListener()
    {

        @SuppressLint("NewApi")
		public void onClick(View v)
        {   
        	if(v.getId() == R.id.SaveFreq){
        		String freq =	String.valueOf(spinner1.getSelectedItem());
        		Intent returnIntent = new Intent();
   			 	returnIntent.putExtra("result",freq);
   			 	setResult(RESULT_OK,returnIntent);     
   			 	finish();
            }        
        }
	

    }; 
    
    private void showText(String val) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Email Field cannot be empty", Toast.LENGTH_LONG).show();
	} 
    

   
}