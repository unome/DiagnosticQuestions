package com.apuroopgadde.diagnosticquestions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper{
	public static final String DB_NAME = "DQuestions.db";
	public static final int DB_VERSION = 1;
	public static final String tName = "QuestionsTable";
	public static final String questDetails="Question";
	public static final String tableId=BaseColumns._ID;
	public static final String option1 ="Option1";
	public static final String option2 ="Option2";
	public static final String option3 ="Option3";
	public static final String option4 ="Option4";
	public static final String option5 ="Option5";
	public static final String corrOption ="CorrectOption";
	public static final String TAG = "DQuestions";




	Context context;
	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context; 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + tName + " (" + tableId + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
				+ questDetails + " TEXT NOT NULL, " + option1 + " TEXT NOT NULL, " +  option2 + " TEXT NOT NULL, "+option3 
				+ " TEXT NOT NULL, "+option4 + " TEXT NOT NULL, "+option5+ " TEXT NOT NULL, "+corrOption + "INTEGER NOT NULL)"; 	// Database creation sql statement
		Log.d(TAG,sql);
		db.execSQL(sql); //Actual sql command to create the table in sqlite

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
