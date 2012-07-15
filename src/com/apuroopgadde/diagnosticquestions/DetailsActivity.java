package com.apuroopgadde.diagnosticquestions;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class DetailsActivity extends Activity{
	DbHelper dbhelper;
	ArrayList<String> checked = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle answers = getIntent().getExtras();
        setContentView(R.layout.details);
		dbhelper = new DbHelper(this);
    	checked = answers.getStringArrayList("answers");
    	checkAnswer();
	}
	private void checkAnswer() {
		int score=0;
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		final SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
		String answersQuery = "select * from "+dbhelper.oTable+" where "+dbhelper.qid+" = "+
				wmbPreference.getInt("currQuestion", 1)+" AND "+dbhelper.correct+" = 'True';";
		Cursor answersIt = db.rawQuery(answersQuery, null);
		while(answersIt.moveToNext())
		{
			if(checked.contains(answersIt.getString(2)))
			{
				++score;
			}
		}
		TextView correctOrNot=(TextView)findViewById(R.id.tV_correctOrNot);
		TextView answerDetailsDisplay=(TextView)findViewById(R.id.tV_Explanation);
		if(answersIt.getCount()==checked.size()&&checked.size()==score)
		{
			correctOrNot.setText("You've got the correct answer");
		}
		else if(answersIt.getCount()!=checked.size()&&score>0)
		{
			correctOrNot.setText("Your answer is partially correct");
		}
		else
		{
			correctOrNot.setText("Sorry you've got the wrong answer");
		}
		answersIt.close();
		String explanationQuery="select * from "+dbhelper.aTable+" where "+dbhelper.qid+" = "+
				wmbPreference.getInt("currQuestion", 1)+";";
		Cursor explanationIt=db.rawQuery(explanationQuery, null);
		if(explanationIt.moveToNext())
			answerDetailsDisplay.setText("Correct answer and explanation is \n \n"+explanationIt.getString(2));
		int nextQuestion=wmbPreference.getInt("currQuestion",1)+1;
		
	}
	@Override
	public void onBackPressed() {
	}


}
