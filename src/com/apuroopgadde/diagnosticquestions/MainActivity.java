package com.apuroopgadde.diagnosticquestions;


import java.io.IOException;
import java.util.ArrayList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;
import android.support.v4.app.NavUtils;

@SuppressLint("ParserError")
public class MainActivity extends Activity {
	DbHelper dbhelper;
	int questionId=0;
	static String TAG="dQuestions";
	ArrayList<String> checked = new ArrayList<String>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_dquestions);
		final SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
		if (isFirstRun)
		{
			SharedPreferences.Editor editor = wmbPreference.edit();
			editor.putBoolean("FIRSTRUN", false);
			editor.putInt("currQuestion",1);
			editor.putInt("currentScore", 0);
			editor.commit();
		}
		dbhelper = new DbHelper(this);
		try {
			dbhelper.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//dbhelper.openDataBase();
		showQuestion();

	}


	private void showQuestion() {
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		TableLayout tl = (TableLayout) findViewById(R.id.tL_optionsDisplay);
		tl.removeAllViews();
		Button doneButton=(Button)findViewById(R.id.button_done);
		Button prevQuesButton=(Button)findViewById(R.id.button_prevQuestion);
		Button skipQuesButton=(Button)findViewById(R.id.button_skipQuestion);
		final SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
		Log.d(TAG,"currentQuestion"+wmbPreference.getInt("currQuestion", 1));
		String question = "select * from "+dbhelper.qTable + " where "+dbhelper.rowId +" = "
				+wmbPreference.getInt("currQuestion", 1)+";";
		Cursor sqlIt = db.rawQuery(question, null);
		String answered = "false";
		if(sqlIt.moveToNext())
		{
			TextView quesText = (TextView)findViewById(R.id.tV_question);
			quesText.setText(sqlIt.getString(1));
			questionId = sqlIt.getInt(0);
			answered=sqlIt.getString(2);
		}
		if(questionId==1)
			prevQuesButton.setVisibility(View.INVISIBLE);
		if(questionId==7)
			skipQuesButton.setText("View Score");
		if(answered.equals("True"))
			doneButton.setVisibility(View.INVISIBLE);
		sqlIt.close();
		String optionsQuery = "select * from "+dbhelper.oTable + " where "+dbhelper.qid+" = "
				+questionId;
		Cursor optionsIt = db.rawQuery(optionsQuery,null);
		while(optionsIt.moveToNext())
		{
			TableRow tbrow = new TableRow(this);
			tbrow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			CheckBox cBox = new CheckBox(this);
			cBox.setTag("cB" + optionsIt.getString(2));
			if(answered.equals("True"))
				cBox.setClickable(false);
			cBox.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					TableRow currRow = (TableRow) v.getParent(); // getParent returns the parent
					ArrayList<View> allViews = new ArrayList<View>();
					allViews = currRow.getTouchables();
					TextView optionName = (TextView) (allViews.get(1));
					Log.d(TAG,"option is"+optionName.getText());
					if(((CheckBox)v).isChecked())
						checked.add((String) optionName.getText());
					else
						checked.remove(checked.indexOf((String)optionName.getText()));
				}
			});
			tbrow.addView(cBox);
			TextView kName = new TextView(this);
			kName.setText(optionsIt.getString(2));
			kName.setTextSize(15);
			kName.setClickable(true);
			tbrow.addView(kName);
			tl.addView(tbrow);
		}
		db.close();
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
			final SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
			int prevQuestionValue = wmbPreference.getInt("currQuestion",1)-1;
			SharedPreferences.Editor editor = wmbPreference.edit();
			editor.putInt("currQuestion",prevQuestionValue);
			editor.commit();
			onCreate(null);
		}
		if(v.getId()==R.id.button_skipQuestion)
		{
			if(questionId==7)
			{
				return;
			}
			final SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
			int nextQuestionValue = wmbPreference.getInt("currQuestion",1)+1;
			SharedPreferences.Editor editor = wmbPreference.edit();
			editor.putInt("currQuestion",nextQuestionValue);
			editor.commit();
			onCreate(null);

		}
		if(v.getId()==R.id.button_done)
		{
			Log.d(TAG,"checked size is:"+checked.size());
			Intent showAnswer = new Intent(MainActivity.this,
					DetailsActivity.class);
			showAnswer.putStringArrayListExtra("answers",checked);
			startActivityForResult(showAnswer, 0);
			//Check for correct answers
		}
	}


}
