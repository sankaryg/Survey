package com.example.survey_game;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.survey_game.entity.Product;
import com.example.survey_game_fuction.Brand;
import com.example.survey_game_fuction.CompFeature;
import com.example.survey_game_fuction.ContraFeature;
import com.example.survey_game_fuction.Feature;
import com.example.survey_game_fuction.Login;
import com.example.survey_game_fuction.Upload;

public class DBfunction {
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.USER_ID,
			MySQLiteHelper.NAME, MySQLiteHelper.AGE, MySQLiteHelper.GENDER };
	private long insertId;

	public DBfunction(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public boolean resetTables(String table_name) {
		// TODO Auto-generated method stub
		open();
		try{
		database.delete(table_name, null, null);
		return true;
		//db.close();
		}catch (Exception e) {
			// TODO: handle exception
			return false;
		}finally{
			close();
		}
	}

	public long insertLogin(Login login) {
		long insertId = 0;
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(MySQLiteHelper.NAME, login.getName());
			values.put(MySQLiteHelper.AGE, login.getAge());
			values.put(MySQLiteHelper.GENDER, login.getGender());
			values.put("status", login.isStatus());
			insertId = database.insert(MySQLiteHelper.TABLE_NAME, null, values);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			close();
		}
		
		return insertId;

	}
	
	public long insertProduct(Product product) {
		long insertId = 0;
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(MySQLiteHelper.product_name, product.getProduct_name());
			values.put(MySQLiteHelper.product_server_id, product.getProduct_id());
			
			insertId = database.insert(MySQLiteHelper.PRODUCT_TABLE, null, values);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			close();
		}
		
		return insertId;

	}
	public List<Product> retriveProducts() {
		open();
		List<Product> brands = new ArrayList<Product>();
		String query = "select *from " + MySQLiteHelper.PRODUCT_TABLE;
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
		try{
		while (!cursor.isAfterLast()) {
			Product brand = new Product();
			brand.setProduct_name(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.product_name)));
			brand.setProduct_id(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.product_server_id)));
			
			brands.add(brand);
			Log.d("check", brands.size()+"_");
			cursor.moveToNext();
		}
	}
	catch (Exception e) {
		// TODO: handle exception
	}finally{
		if(cursor!=null){
			cursor.close();
			close();
			}
	}
		return brands;
	}
	public long UploadData(Upload login) {
		long insertId = 0;
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(MySQLiteHelper.server_name, login.getName());
			values.put(MySQLiteHelper.server_age, login.getAge());
			values.put(MySQLiteHelper.server_gender, login.getGender());
			values.put(MySQLiteHelper.server_brand, login.getBrand());
			values.put(MySQLiteHelper.server_brandId, login.getBrandId());
			values.put(MySQLiteHelper.server_features, login.getFeature());
			insertId = database.insert(MySQLiteHelper.server_table, null, values);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			close();
		}
		return insertId;

	}

	public int updateUpload(Upload login) {
    	open();
		try{
			ContentValues values = new ContentValues();
			values.put(MySQLiteHelper.server_name, login.getName());
			values.put(MySQLiteHelper.server_age, login.getAge());
			values.put(MySQLiteHelper.server_gender, login.getGender());
			values.put(MySQLiteHelper.server_brand, login.getBrand());
			values.put(MySQLiteHelper.server_brandId, login.getBrandId());
			values.put(MySQLiteHelper.server_features, login.getFeature());
		return database.update(MySQLiteHelper.server_table, values,  MySQLiteHelper.server_brandId+" = ?",
                new String[] { String.valueOf(login.getBrandId()) });
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			close();
		}
		return 0;
    }
	public int deleteUpload(Upload login) {
    	open();
		try{
			
		return database.delete(MySQLiteHelper.server_table, MySQLiteHelper.server_brandId+" = ?",
                new String[] { String.valueOf(login.getBrandId()) });
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			close();
		}
		return 0;
    }
	public List<Upload> retriveUpload() {
		open();
		List<Upload> brands = new ArrayList<Upload>();
		String query = "select *from " + MySQLiteHelper.server_table;
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
		try{
		while (!cursor.isAfterLast()) {
			Upload brand = new Upload();
			brand.setAge(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.server_age)));
			brand.setBrand(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.server_brand)));
			brand.setBrandId(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.server_brandId)));
			brand.setFeature(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.server_features)));
			brand.setGender(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.server_gender)));
			brand.setName(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.server_name)));
			brands.add(brand);
			Log.d("check", brands.size()+"_");
			cursor.moveToNext();
		}
	}
	catch (Exception e) {
		// TODO: handle exception
	}finally{
		if(cursor!=null){
			cursor.close();
			close();
			}
	}
		return brands;
	}
	public long insertContra(ContraFeature contra) {
		long insertId = 0;
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(MySQLiteHelper.CONTRA_FEATURE_ID, contra.getFeature_id());
			values.put(MySQLiteHelper.CONTRA_TYPE1,
					contra.getContra_feature_1());
			values.put(MySQLiteHelper.CONTRA_TYPE2,
					contra.getContra_feature_2());
			values.put(MySQLiteHelper.CONTRA_TYPE3,
					contra.getContra_feature_3());
			values.put(MySQLiteHelper.CONTRA_TYPE4,
					contra.getContra_feature_4());
			values.put(MySQLiteHelper.CONTRA_TYPE5,
					contra.getContra_feature_5());
			insertId = database.insert(MySQLiteHelper.CONTRA_TYPE_TABLE, null,
					values);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			close();
		}
		/*
		 * Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,allColumns,
		 * MySQLiteHelper.USER_ID + " = " + insertId, null,null, null, null);
		 * cursor.moveToFirst(); Login newComment = cursorToLogin(cursor);
		 * cursor.close();
		 */
		return insertId;

	}

	public long insertComp(CompFeature contra) {
		long insertId = 0;
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(MySQLiteHelper.COMP_FEATURE_ID, contra.getFeature_id());
			values.put(MySQLiteHelper.COMP_TYPE1, contra.getContra_feature_1());
			values.put(MySQLiteHelper.COMP_TYPE2, contra.getContra_feature_2());
			values.put(MySQLiteHelper.COMP_TYPE3, contra.getContra_feature_3());
			values.put(MySQLiteHelper.COMP_TYPE4, contra.getContra_feature_4());
			values.put(MySQLiteHelper.COMP_TYPE5, contra.getContra_feature_5());
			insertId = database.insert(MySQLiteHelper.COMP_TYPE_TABLE, null,
					values);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			close();
		}
		
		return insertId;

	}

	public long insertBrand(Brand brand) {
		long insertId = 0;
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(MySQLiteHelper.BRAND_USER_ID, brand.getBrandId());
			values.put(MySQLiteHelper.BRNAD_NAME, brand.getBrandName());
			values.put(MySQLiteHelper.BRAND_PATH, brand.getBrand_image_path());
			values.put("status", "false");
			insertId = database
					.insert(MySQLiteHelper.BRAND_TABLE, null, values);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			close();
		}
		
		return insertId;

	}

	

	public long insertFeaature(Feature feature) {
		long insertId = 0;
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(MySQLiteHelper.BRAND_FEATURE_ID, feature.getId());
			values.put(MySQLiteHelper.FEATURE_NAME, feature.getFeature());
			insertId = database.insert(MySQLiteHelper.FEATURE_TABLE, null,
					values);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			close();
		}
		
		return insertId;

	}

	public int getNoOfRows() {
		String query = "select count(*) from " + MySQLiteHelper.BRAND_TABLE;
		Cursor id = database.rawQuery(query, null);
		id.moveToFirst();
		int rows = id.getInt(0);
		id.close();
		return rows;
	}

	public Login getFirstRecord() {
		open();
		Cursor cursor = null;
		Login login = null;
		try {
			String query = "SELECT * FROM " + MySQLiteHelper.TABLE_NAME
					+ "  ORDER BY " + MySQLiteHelper.USER_ID + " ASC LIMIT 1";
			cursor = database.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				login = new Login();
				login.setAge(cursor.getInt(cursor
						.getColumnIndex(MySQLiteHelper.AGE)));
				login.setGender(cursor.getString(cursor
						.getColumnIndex(MySQLiteHelper.GENDER)));
				login.setName(cursor.getString(cursor
						.getColumnIndex(MySQLiteHelper.NAME)));
				login.setStatus(Boolean.parseBoolean(cursor.getString(cursor
						.getColumnIndex("status"))));
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (cursor != null) {
				cursor.close();
				close();
			}
		}
		return login;
	}
	public int updateBrand(Brand brand,String status) {
    	open();
		try{
			ContentValues values = new ContentValues();
			values.put(MySQLiteHelper.BRAND_USER_ID, brand.getBrandId());
			values.put(MySQLiteHelper.BRNAD_NAME, brand.getBrandName());
			values.put(MySQLiteHelper.BRAND_PATH, brand.getBrand_image_path());
			values.put("status", status);
		return database.update(MySQLiteHelper.BRAND_TABLE, values,  MySQLiteHelper.BRAND_USER_ID+" = ?",
                new String[] { String.valueOf(brand.getBrandId()) });
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			close();
		}
		return 0;
    }
	public List<Brand> retriveBrand() {
		open();
		List<Brand> brands = new ArrayList<Brand>();
		String query = "select *from " + MySQLiteHelper.BRAND_TABLE;
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
		try{
		while (!cursor.isAfterLast()) {
			Brand brand = new Brand();
			brand.setBrandId(cursor.getInt(cursor
					.getColumnIndex(MySQLiteHelper.BRAND_USER_ID)));
			
			brand.setBrandName(cursor.getString(cursor
					.getColumnIndex(MySQLiteHelper.BRNAD_NAME)));
			brand.setBrand_image_path(cursor.getString(cursor
					.getColumnIndex(MySQLiteHelper.BRAND_PATH)));
			brand.setBrandStatus(cursor.getString(cursor
					.getColumnIndex("status")));
			brands.add(brand);
			cursor.moveToNext();
		}
	}
	catch (Exception e) {
		// TODO: handle exception
	}finally{
		if(cursor!=null){
			cursor.close();
			close();
			}
	}
		return brands;
	}

	public List<Feature> retriveFeature() {
		open();
		List<Feature> brands = new ArrayList<Feature>();
		String query = "select *from " + MySQLiteHelper.FEATURE_TABLE;
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
		try{
		while (!cursor.isAfterLast()) {
			Feature brand = new Feature();
			brand.setId(cursor.getInt(cursor
					.getColumnIndex(MySQLiteHelper.BRAND_FEATURE_ID)));
			
			brand.setFeature(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.FEATURE_NAME)));
			brands.add(brand);
			cursor.moveToNext();
		}
	}
	catch (Exception e) {
		// TODO: handle exception
	}finally{
		if(cursor!=null){
			cursor.close();
			close();
			}
	}
		return brands;
	}
	public List<ContraFeature> retriveContraFeature() {
		open();
		List<ContraFeature> brands = new ArrayList<ContraFeature>();
		String query = "select *from " + MySQLiteHelper.CONTRA_TYPE_TABLE;
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
		try{
		while (!cursor.isAfterLast()) {
			ContraFeature brand = new ContraFeature();
			brand.setFeature_id(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.CONTRA_FEATURE_ID)));
			brand.setContra_feature_1(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.CONTRA_TYPE1)));//, contra.getContra_feature_1());
			brand.setContra_feature_2(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.CONTRA_TYPE2)));//, contra.getContra_feature_2());
			brand.setContra_feature_3(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.CONTRA_TYPE3)));//, contra.getContra_feature_3());
			brand.setContra_feature_4(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.CONTRA_TYPE4)));//, contra.getContra_feature_4());
			brand.setContra_feature_5(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.CONTRA_TYPE5)));//, contra.getContra_feature_5());
			brands.add(brand);
			cursor.moveToNext();
		}
	}
	catch (Exception e) {
		// TODO: handle exception
	}finally{
		if(cursor!=null){
			cursor.close();
			close();
			}
	}
		
		return brands;
	}
	public List<CompFeature> retriveCompFeature() {
		open();
		List<CompFeature> brands = new ArrayList<CompFeature>();
		String query = "select *from " + MySQLiteHelper.COMP_TYPE_TABLE;
		Cursor cursor = database.rawQuery(query, null);
		try{
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CompFeature brand = new CompFeature();
			brand.setFeature_id(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COMP_FEATURE_ID)));
			brand.setContra_feature_1(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COMP_TYPE1)));//, contra.getContra_feature_1());
			brand.setContra_feature_2(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COMP_TYPE2)));//, contra.getContra_feature_2());
			brand.setContra_feature_3(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COMP_TYPE3)));//, contra.getContra_feature_3());
			brand.setContra_feature_4(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COMP_TYPE4)));//, contra.getContra_feature_4());
			brand.setContra_feature_5(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COMP_TYPE5)));//, contra.getContra_feature_5());
			brands.add(brand);
			cursor.moveToNext();
		}
		}
		catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(cursor!=null){
				cursor.close();
				close();
				}
		}
		
		return brands;
	}
	public void deleteLogin(Login comment) {
		long id = comment.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.USER_ID
				+ " = " + id, null);
	}

}
