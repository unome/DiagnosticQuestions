package com.apuroopgadde.diagnosticquestions;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
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
		final SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
		if (isFirstRun)
		{
			SharedPreferences.Editor editor = wmbPreference.edit();
			editor.putBoolean("FIRSTRUN", false);
			editor.putInt("currQuestion",1);
			editor.putInt("currentScore", -1);
			editor.commit();
		}
		showQuestion();
        
    }
    

    private void showQuestion() {
		
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
    	if(v.getId()==R.id.button_done)
    	{
    		//Check for correct answers
    	}
    }

    
}
