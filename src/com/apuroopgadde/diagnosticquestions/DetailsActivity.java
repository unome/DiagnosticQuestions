package com.apuroopgadde.diagnosticquestions;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailsActivity extends Activity{
	final String TAG = "dQuestions";
	DbHelper dbhelper;
	int currQuestion=0;
	SharedPreferences wmbPreference=null;
	TextView correctOrNot=null;
	TextView answerDetailsDisplay=null;
	Button nextQues=null;
	ArrayList<String> checked = new ArrayList<String>();
	boolean alreadyAnswered=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		wmbPreference=PreferenceManager.getDefaultSharedPreferences(this);
		currQuestion=wmbPreference.getInt("currQuestion",1);
		super.onCreate(savedInstanceState);
		Bundle answers = getIntent().getExtras();
		setContentView(R.layout.details);
		if(currQuestion==7)
		{
			nextQues = (Button)findViewById(R.id.button_nextQues);
			nextQues.setText("View Score");
		}
		correctOrNot=(TextView)findViewById(R.id.tV_correctOrNot);
		answerDetailsDisplay=(TextView)findViewById(R.id.tV_Explanation);
		dbhelper = new DbHelper(this);
		checked = answers.getStringArrayList("answers");
		alreadyAnswered=answers.getBoolean("alreadyAnswered");
		checkAnswer();
	}
	private void checkAnswer() {
		int score=0;
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		if(!alreadyAnswered)
		{
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
			int updatedScore=wmbPreference.getInt("currentScore",0)+score;
			SharedPreferences.Editor editor = wmbPreference.edit();
			editor.putInt("currentScore", updatedScore);
			editor.commit();
			String explanationQuery="select * from "+dbhelper.aTable+" where "+dbhelper.qid+" = "+
					wmbPreference.getInt("currQuestion", 1)+";";
			Cursor explanationIt=db.rawQuery(explanationQuery, null);
			if(explanationIt.moveToNext())
				answerDetailsDisplay.setText("Correct answer and explanation is \n \n"+explanationIt.getString(2));
			String setQuesAnswered="update "+dbhelper.qTable+" set "+dbhelper.quesAnswered+"='True' where "+
					dbhelper.rowId+" = "+wmbPreference.getInt("currQuestion", 1)+";";
			Log.d(TAG,setQuesAnswered);
			db.execSQL(setQuesAnswered);
			db.close();
		}	
		else{
			correctOrNot.setText("");
			String explanationQuery="select * from "+dbhelper.aTable+" where "+dbhelper.qid+" = "+
					wmbPreference.getInt("currQuestion", 1)+";";
			Cursor explanationIt=db.rawQuery(explanationQuery, null);
			if(explanationIt.moveToNext())
				answerDetailsDisplay.setText("Correct answer and explanation is \n \n"+explanationIt.getString(2));
			db.close();
		}

	}
	@Override
	public void onBackPressed() {
	}

	public void onClickHandler(View v)
	{
		if(v.getId()==R.id.button_viewQues)
		{
			Intent showQues = new Intent(this,
					MainActivity.class);
			startActivityForResult(showQues, 0);
		}
		if(v.getId()==R.id.button_nextQues)
		{
			if(currQuestion!=7)
			{
				if(!alreadyAnswered)
				{
					int nextQuestion=wmbPreference.getInt("currQuestion",1)+1;
					SharedPreferences.Editor editor = wmbPreference.edit();
					editor.putInt("currQuestion", nextQuestion);
					editor.commit();
				}
				Intent nextQues = new Intent(this,MainActivity.class);
				startActivityForResult(nextQues, 0);
			}
			else
			{
				Intent scoreDisplay= new Intent(this,FinalScoreActivity.class);
				startActivityForResult(scoreDisplay,0);
			}
		}
	}

}
