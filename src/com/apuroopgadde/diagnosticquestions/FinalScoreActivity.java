package com.apuroopgadde.diagnosticquestions;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

public class FinalScoreActivity extends Activity{
	final String TAG="dQuestions";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalscoreview);
		showScore();
	}

	private void showScore() {
		final SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
		TextView scoreDisplay = (TextView)findViewById(R.id.tV_scoreDisplay);
		Log.d(TAG,String.valueOf(wmbPreference.getInt("currentScore",0)));
		scoreDisplay.setText(String.valueOf(wmbPreference.getInt("currentScore",0)));
	}

}
