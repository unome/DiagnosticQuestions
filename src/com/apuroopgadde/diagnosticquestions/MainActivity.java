package com.apuroopgadde.diagnosticquestions;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
        
    }*/
    
    public void onClickHandler(View v)
    {
    	if(v.getId()==R.id.button_prevQuestion)
    	{
    		//Navigate to previous question
    	}
    	if(v.getId()==R.id.button_skipQuestion)
    	{
    		//skip to next question 
    		
    	}
    }

    
}
