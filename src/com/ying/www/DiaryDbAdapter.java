package com.ying.www;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DiaryDbAdapter {
	
	private static final String DATABASE_NAME = "database";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = "create table diary (_id integer primary key autoincrement, "
			+ "title text not null, body text not null, created text not null,"
			+ "type text not null,w_type text not null);";
	private final Context mCtx;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	public static final String KEY_W_TYPE = "w_type";
	public static final String KEY_TITLE = "title";
	public static final String KEY_BODY = "body";
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TYPE = "type";
	public static final String KEY_CREATED = "created";
	private static final String DATABASE_TABLE = "diary";
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
			DatabaseHelper (Context context){
			super(context,DATABASE_NAME,null,DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS diary");
			onCreate(db);
			
		}
	}
	
	public DiaryDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}
	
	public DiaryDbAdapter open()throws SQLException{
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		mDbHelper.close();
	}
	
	public long createDiary(String title, String body,String type,String w_type) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_BODY, body);
		initialValues.put(KEY_TYPE, type);
		initialValues.put(KEY_W_TYPE, w_type);
		Calendar calendar = Calendar.getInstance();
		String created = calendar.get(Calendar.YEAR) + "."
				+  Integer.toString( Integer.valueOf(calendar.get(Calendar.MONTH)).intValue()+1) + "."
				+ calendar.get(Calendar.DAY_OF_MONTH) + "  "
				+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE) + " ";
		initialValues.put(KEY_CREATED, created);
		return mDb.insert(DATABASE_TABLE, null, initialValues);
		//mDb.insert(DATABASE_TABLE, null, initialValues);
	}
	
	public boolean deleteDiary(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public Cursor getAllNotes() {
		
		
		 
		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
			KEY_BODY, KEY_CREATED,KEY_TYPE,KEY_W_TYPE },null, null, null, null, null);
	}
	public Cursor getDiaryByType(String ss) throws SQLException {
		return mDb.rawQuery("SELECT  _id,title,body,created,type,w_type FROM diary where type = " + ss, null);
	}
	
	public Cursor getDiary(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
				KEY_BODY, KEY_CREATED,KEY_TYPE ,KEY_W_TYPE}, KEY_ROWID + "=" + rowId,null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public boolean updateDiary(long rowId, String title, String body,String type,
			String created,String w_type) {
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE, title);
		args.put(KEY_BODY, body);
		args.put(KEY_TYPE, type);
		args.put(KEY_W_TYPE, w_type);
		//Calendar calendar = Calendar.getInstance();
		/*String created = calendar.get(Calendar.YEAR) + "."
				+ calendar.get(Calendar.MONTH) + "."
				+ calendar.get(Calendar.DAY_OF_MONTH) + "  "
				+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE) + " ";*/
		args.put(KEY_CREATED, created);

		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	/*public boolean updateDiary(long rowId, String title, String body) {
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE, title);
		args.put(KEY_BODY, body);
		Calendar calendar = Calendar.getInstance();
		String created = calendar.get(Calendar.YEAR) + "��"
				+ calendar.get(Calendar.MONTH) + "��"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "��"
				+ calendar.get(Calendar.HOUR_OF_DAY) + "Сʱ"
				+ calendar.get(Calendar.MINUTE) + "����";
		args.put(KEY_CREATED, created);

		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}*/
}
