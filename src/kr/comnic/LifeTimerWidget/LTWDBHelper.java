package kr.comnic.LifeTimerWidget;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LTWDBHelper extends SQLiteOpenHelper {
	private static final String TAG = "LifeTimerWidget";
	private static final int DATABASE_VERSION = 1;
	private static final String DB_FILE_NAME = "LTW.db";

	public LTWDBHelper(Context context) {
		super(context, DB_FILE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE info(_id INTEGER PRIMARY KEY AUTOINCREMENT, birth_year TEXT, birth_month TEXT, birth_day TEXT, exp_age NUMBER, chk1 BOOLEAN, chk2 BOOLEAN, text1 TEXT, text2 TEXT, kind NUMBER, color TEXT);");
		db.execSQL("INSERT INTO info VALUES (null, '0000', '00', '00', 85, 1, 1, '지난시간', '남은시간', 0, '#AAAAAA');");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	
	/**
	 * * * @return the row ID of the newly inserted row, or -1 if an error
	 * occurred
	 */
/*	
	public long insert(String value) {
		long id = -1;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues row = new ContentValues();
		row.put("f01", value);
		id = db.insert("info", null, row);
		Log.i(TAG, "insert");
		return id;
	}
*/
	public int update(String field, String value, String whereClause) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues row = new ContentValues();
		row.put(field, value);
		Log.i(TAG, "update");
		int r = db.update("info", row, whereClause, null);
		db.close();
		return r;
	}
/*
	public int delete(String whereClause) {
		SQLiteDatabase db = getWritableDatabase();
		return db.delete("info", whereClause, null);
	}
*/
	public LTWInfomation select() {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor;
		LTWInfomation info = null;
		
		cursor = db.rawQuery("SELECT * from info", null);
		Log.i(TAG, "select");
		
		if (cursor.moveToNext()) {
			info = new LTWInfomation(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5) == 1?true:false, cursor.getInt(6) == 1?true:false, cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getInt(10));
		}
		db.close();
		return info;
	}
}
