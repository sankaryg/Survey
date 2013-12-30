package com.example.survey_game;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_NAME = "login";
	public static final String USER_ID = "_id";
	public static final String NAME = "username";
	public static final String AGE = "age";
	public static final String GENDER = "gender";

	private static final String DATABASE_NAME = "Survey_Game.db";
	private static final int DATABASE_VERSION = 2;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + TABLE_NAME
			+ "(" + USER_ID + " integer primary key autoincrement, " + NAME
			+ " text not null, " + AGE + " integer not null," + GENDER
			+ " text not null, status text not null );";

	/* table for list of brands */

	public static final String BRAND_TABLE = "brand";
	public static final String BRAND_ID = "id";
	public static final String BRAND_USER_ID = "brand_id";
	public static final String BRNAD_NAME = "brand_name";
	public static final String BRAND_PATH = "brand_path";

	private static final String BRAND_TABLE_QUERY = "create table "
			+ BRAND_TABLE + "(" + BRAND_ID
			+ " integer primary key autoincrement, " + BRAND_USER_ID
			+ " integer not null, " + BRNAD_NAME + " text not null, "+ BRAND_PATH + " text not null, status tet not null);";
	/* table for list of feature */
	public static final String FEATURE_TABLE = "feature";
	public static final String FEATURE_ID = "id";
	public static final String BRAND_FEATURE_ID = "brand_id";
	public static final String FEATURE_NAME = "feature_name";

	private static final String FEATURE_TABLE_QUERY = "create table "
			+ FEATURE_TABLE + "(" + FEATURE_ID
			+ " integer primary key autoincrement, " + BRAND_FEATURE_ID
			+ " integer not null, " + FEATURE_NAME + " text not null);";

	public static final String CONTRA_TYPE_TABLE = "contra";

	public static final String CONTRA_ID = "id";
	public static final String CONTRA_FEATURE_ID = "feature_id";
	public static final String CONTRA_TYPE1 = "contra1";
	public static final String CONTRA_TYPE2 = "contra2";
	public static final String CONTRA_TYPE3 = "contra3";
	public static final String CONTRA_TYPE4 = "contra4";
	public static final String CONTRA_TYPE5 = "contra5";

	private static final String CONTRA_TYPE_TABLE_QUERY = "create table "
			+ CONTRA_TYPE_TABLE + "(" + CONTRA_ID
			+ " integer primary key autoincrement, " + CONTRA_FEATURE_ID
			+ " text not null, " + CONTRA_TYPE1 + " text not null, "+ CONTRA_TYPE2
			+ " text not null, " + CONTRA_TYPE3	+ " text not null, "+ CONTRA_TYPE4
			+ " text not null, " + CONTRA_TYPE5 + " text not null );";

	public static final String COMP_TYPE_TABLE = "comp";

	public static final String COMP_ID = "id";
	public static final String COMP_FEATURE_ID = "feature_id";
	public static final String COMP_TYPE1 = "comp1";
	public static final String COMP_TYPE2 = "comp2";
	public static final String COMP_TYPE3 = "comp3";
	public static final String COMP_TYPE4 = "comp4";
	public static final String COMP_TYPE5 = "comp5";

	private static final String COMP_TYPE_TABLE_QUERY = "create table "
			+ COMP_TYPE_TABLE + "(" + COMP_ID
			+ " integer primary key autoincrement, " + COMP_FEATURE_ID
			+ " text not null, " + COMP_TYPE1 + " text not null, " + COMP_TYPE2
			+ " text not null, " + COMP_TYPE3 + " text not null, " + COMP_TYPE4
			+ " text not null, " + COMP_TYPE5 + " text not null );";

	public static final String server_table = "server";
	public static final String server_id = "_id";
	public static final String server_name = "name";
	public static final String server_age = "age";
	public static final String server_gender = "gender";
	public static final String server_brand = "brand";
	public static final String server_features = "feature";
	public static final String server_brandId = "bid";
	public static final String SERVER_TABLE_QUERY = "create table "
			+ server_table + "(" + server_id
			+ " integer primary key autoincrement, " + server_name
			+ " text not null, " + server_age + " text not null, " + server_gender
			+ " text not null, " + server_brand + " text not null, " + server_features
			+ " text not null, " + server_brandId + " text not null );";
	public static final String PRODUCT_TABLE = "product";
	public static final String product_id = "_id";
	public static final String product_name = "product_name";
	public static final String product_server_id = "product_id";
	public static final String PRODUCT_TABLE_QUERY = "create table "
			+ PRODUCT_TABLE + "(" + product_id
			+ " integer primary key autoincrement, " + product_name
			+ " text not null, " + product_server_id + " text not null);";
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		database.execSQL(DATABASE_CREATE);
		database.execSQL(PRODUCT_TABLE_QUERY);
		database.execSQL(BRAND_TABLE_QUERY);
		database.execSQL(FEATURE_TABLE_QUERY);
		database.execSQL(CONTRA_TYPE_TABLE_QUERY);
		database.execSQL(COMP_TYPE_TABLE_QUERY);
		database.execSQL(SERVER_TABLE_QUERY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL("Drop table if exists " + BRAND_TABLE);
		db.execSQL("Drop table if exists " + FEATURE_TABLE);
		db.execSQL("Drop table if exists " + CONTRA_TYPE_TABLE);
		db.execSQL("Drop table if exists " + COMP_TYPE_TABLE);
		db.execSQL("Drop table if exists " + server_table);
		db.execSQL("Drop table if exists " + PRODUCT_TABLE);
		onCreate(db);
	}

	/*public void resetTables(String table_name) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(table_name, null, null);
		//db.close();

	}*/
}
