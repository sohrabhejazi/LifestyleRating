package com.example.lifestylerating;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class RegisterHelper extends SQLiteOpenHelper {
	public String tableName;
	/*private final String CREATE_table =
			"CREATE TABLE "+ tableName + "( datum INTEGER PRIMARY KEY,"
			+ "Zaehler INTEGER);";*/

	private static final String DB_NAME = "sohrabDB";
	private static final int DB_VERSION = 1;
	private static final String TAG = RegisterHelper.class.getSimpleName();
	//private SQLiteDatabase db;
	//this.onCreate(DB_NAME);
	public RegisterHelper(Context context, String name)
	{
		super(context, DB_NAME, null, DB_VERSION);
		tableName=name;
	}
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		StringBuilder CREATE_table = new StringBuilder();
		CREATE_table.append("CREATE TABLE  ");
		CREATE_table.append(tableName);
		CREATE_table.append("(datum INTEGER PRIMARY KEY, Zaehler INTEGER);");


		try{
		db.execSQL(CREATE_table.toString());
		Log.d("TAG", "DB erzeugt in: \"" + db.getPath()
		+ "\"");
		}
		catch(SQLException sqle) {
			// android.database.SQLException importieren!
			Log.e(TAG, "onCreate: " + sqle.toString());
			}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	public void upDate(SQLiteDatabase db, int JD, int newValue) {
		// TODO Auto-generated method stub
		String strSQL = "UPDATE " + tableName +" SET "+ " Zaehler = "+ newValue  +" WHERE datum = " + JD;
		try {
			db.execSQL(strSQL);
		}
		catch(SQLException sqle) {
			// android.database.SQLException importieren!
			Log.e(TAG, "onUpDate: " + sqle.toString());
		}

	}

	public int maxDate(SQLiteDatabase db) {
		String maxSQL = "SELECT MAX(datum) FROM "+ tableName +" ;";
		try {
			 Cursor cursor =db.rawQuery(maxSQL, null);
			if(cursor.getCount() <1 )
				return 0;
			else
			{
				cursor.moveToLast();
				return cursor.getInt(0);
			}

		} catch (Exception e) {
			Log.e(TAG, "onMaxDate: " +e.toString());
			return -1;
		}
	}

	public boolean insert(SQLiteDatabase db,int date, int count) {
		String insertSQL = "INSERT INTO  "+ tableName +  " VALUES("
				+ " '" + date+ "', '" + count + "');";
		try {
			db.execSQL(insertSQL);
			return true;
		} catch (Exception e) {
			Log.e("TAG", e.toString());
			return false;
		}
	}

}
