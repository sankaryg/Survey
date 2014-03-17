package com.example.survey_game;



import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.survey_game.Game.GameEnd;
import com.example.survey_game.Util.Constants;
import com.example.survey_game.WebService.ShowAlert;
import com.example.survey_game.WebService.UserFunction;
import com.example.survey_game.dbrand.InsertData;
import com.example.survey_game.dbrand.Load;
import com.example.survey_game_fuction.Brand;
import com.example.survey_game_fuction.ConnectionDetector;
import com.example.survey_game_fuction.Login;
import com.example.survey_game_fuction.Upload;

public class home extends Activity{

	ImageView login;
	private Global global;
	private ShowAlert alert;
	private DBfunction db;
	private List<Brand> brands;
	private SharedPreferences preference;
	private int play;
	private int uploadLimit;
	private ConnectionDetector connection;
	private List<Upload> uploadList;
	private ActionBar action;
	private AlertDialog alertDialog;
	public ProgressDialog pd;
	private String activity;
	private int br;
	private AlertDialog alertDialog1;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onCreate(Bundle b)
	{
		super.onCreate(b);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
		setContentView(R.layout.activity_home);
		alert = new ShowAlert(this);
		db = new DBfunction(this);
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String font = Constants.fonts[Integer.parseInt((sharedPrefs.getString("prefSyncFont", "NULL")=="NULL")?"0":sharedPrefs.getString("prefSyncFont", "NULL"))];
		Typeface tf = Typeface.createFromAsset(getAssets(),font);
		preference = getSharedPreferences("Survey", MODE_PRIVATE);
		play = preference.getInt("playMode", 0);
		uploadLimit = preference.getInt("upload", 0);
		activity = preference.getString("activity", null);
		connection = new ConnectionDetector(this);
		global=(Global)getApplicationContext();
		login=(ImageView)findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				preference.edit().putBoolean("end_a", false).commit();
				if(activity!=null){
					Intent i=new Intent(getBaseContext(),dbrand.class);
					preference.edit().putBoolean("log", true).commit();
					startActivity(i);
					finish();
				}
				else{
					/*Log.d("check", global.getLogin()+"_");
					if(activity == null && connection.isConnectingToInternet()){
					Intent i=new Intent(getBaseContext(),ProductSelectActivity.class);
				startActivity(i);
				finish();
					}else if((activity == null && !connection.isConnectingToInternet()) || activity.equals("dbrand")){
						Intent i=new Intent(getBaseContext(),loginnew.class);
						i.putExtra("pid", preference.getString("product_id", "1"));
						startActivity(i);
						finish();
					}*/
					List<Login> Offlinelogin = db.retriveUsers();
					uploadList = db.retriveUpload(preference.getString("uid", null));
					
					if(connection.isConnectingToInternet() && (Offlinelogin.size() > 0 && uploadList.size()>0) ){
						
					 if (alertDialog1 != null) {
							alertDialog1.cancel();
					 }else{
							alertDialog1 = new AlertDialog.Builder(home.this).create();
							alertDialog1.setMessage("You have some data to upload? Yes to upload. No to continue");
							alertDialog1.setTitle("EXIT");
							// Setting alert dialog icon
							// alertDialog.setIcon((status) ? R.drawable.success :
							// R.drawable.fail);

							// Setting OK Button
							alertDialog1.setButton(Dialog.BUTTON_POSITIVE, "YES",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											// TODO Auto-generated method stub
											alertDialog1.cancel();
											abc();
										}
									});
							alertDialog1.setButton(Dialog.BUTTON_NEGATIVE, "NO",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											alertDialog1.cancel();
											//logout();
											Intent i=new Intent(getBaseContext(),ProductSelectActivity.class);
											startActivity(i);
											finish();
							
										}
									});
							// Showing Alert Message
							alertDialog1.show();
					 }
					/*Intent i=new Intent(getBaseContext(),ProductSelectActivity.class);
					startActivity(i);
					finish();*/
				}else{
					/*if(connection.isConnectingToInternet()){
					if(Offlinelogin.size()>0 && uploadList.size() == 0){
						db.resetTables(MySQLiteHelper.TABLE_NAME);
					}
					}*/
					Intent i=new Intent(getBaseContext(),ProductSelectActivity.class);
					startActivity(i);
					finish();
				}
				}
				
			}
		});
		if(Build.VERSION.SDK_INT>=11){
			action = getActionBar();
			getWindow().
			  getDecorView().
			  setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION); 
			
		}
		showUserSettings();
		
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int br = 0; 

		switch (item.getItemId()) {

		case R.id.menu_settings:
			Intent i = new Intent(this, UserSettingActivity.class);
			startActivityForResult(i, RESULT_SETTINGS);
			break;
		case R.id.menu_upload:
	abc();
	break;

		}

		return true;
	}
	private void abc(){
		activity = preference.getString("activity", null);
		
		//if(activity!=null){
		br = db.getNoOfBrandPlayed((preference.getString("product_id", "1")));
		/*for(Brand bra:brands){
			if(bra.getBrandStatus().equals("true")){
				//++br;
				br = br+1;
			}
		}*/
		List<Login> Offlinelogin = db.retriveUsers();
		uploadList = db.retriveUpload(preference.getString("uid", null));
		//if(br>=2){
			if(connection.isConnectingToInternet()){
				if(Offlinelogin.size()>0){
					new InsertData().execute(Offlinelogin);
				}else {
					if(uploadList.size()>0){
					try{
						new Load().execute(uploadList);
						}
						catch (Exception e) {
							// TODO: handle exception
						}
			Editor edit = preference.edit();
			edit.putInt("upload", 2);
			edit.putInt("playMode", 2);
			edit.clear();
			edit.commit();
			db.deleteLogin(preference.getString("uid", null));
			db.deleteUpload(preference.getString("uid", null));
						//db.resetTables(MySQLiteHelper.TABLE_NAME);
					//db.resetTables(MySQLiteHelper.server_table);
					global.setLogin(null);
			Toast.makeText(home.this, "success", Toast.LENGTH_LONG).show();
			//finish();
				}
					else{
						alert.showAlertDialog(home.this, "Survey", "No data to upload", true,null);	
					}
				}
			}
			else{
				alert.showAlertDialog(home.this, "Warning", "Check your network connection", true,null);
			}
		/*}else{
			alert.showAlertDialog(home.this, "Warning", "You need to play atleast two brand", true);
		}*/
		/*//}else{
			{
				Constants.name = preference.getString("name", null);
				Constants.age = preference.getString("age", null);
				Constants.gender = preference.getString("gender", null);
				if(Constants.name!=null && Constants.age!=null && Constants.gender !=null)
				new InsertData().execute();
				else
				alert.showAlertDialog(home.this, "Warning", "You are not logged in and no data to upload", true);

			}
			
			
			}
*/	
	}
	/*class InsertData extends AsyncTask<Void, Void, String>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(home.this);
			pd.setMessage("Loading...");
			pd.setMax(100);
			pd.setCancelable(false);
			pd.setIndeterminate(false);
			pd.show();
		}
		public void execute(String name, String age, String gender) {
			// TODO Auto-generated method stub
			
		}
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			UserFunction user = new UserFunction();
			JSONObject jboj = null;
			String str = null;
			try{
				jboj = user.userRegistration(Constants.name, Constants.age, Constants.gender,Constants.productID);
				str = (jboj.getString("user_id"));//+"_"+jboj.getString("product_id");
				
			}catch(Exception e){
				str = null;
			}
			return str;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(pd!=null && pd.isShowing())
				pd.dismiss();
			if(result!= null){
				Constants.name="";Constants.age="";Constants.gender="";
			uploadList = db.retriveUpload(preference.getString("uid", null));
			
			for(Upload up:uploadList){
				up.setAge(result);
				up.setName(preference.getString("pid","1"));// setName(log.getName());//product id
				db.updateUpload(up);
			}
			db.deleteLogin(preference.getString("uid", null));
			//db.resetTables(MySQLiteHelper.TABLE_NAME);
			uploadList = db.retriveUpload(preference.getString("uid", null));
		if(uploadList!=null&&uploadList.size()>0){
			showAlert();
		}else
			alert.showAlertDialog(home.this, "Warning", "You are not logged in and no data to upload", true);
			}else{
				alert.showAlertDialog(home.this, "Error", "Unable to process server", true);
			}
		}
	}*/
	class InsertData extends AsyncTask<List<Login>, Void, String>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(home.this);
			pd.setMessage("Loading...");
			pd.setMax(100);
			pd.setCancelable(false);
			pd.setIndeterminate(false);
			pd.show();
		}
		
		@Override
		protected String doInBackground(List<Login>... params) {
			// TODO Auto-generated method stub
			UserFunction user = new UserFunction();
			JSONObject jboj = null;
			String str = null;
			List<Login> offline = params[0];
			for(Login log:offline){
			try{
				jboj = user.userRegistration(log.getName(), String.valueOf(log.getAge()), log.getGender(),log.getProduct_id());
				uploadList = db.retriveUpload(log.getDb_user_id());
				db.updateLogin(log, log.getProduct_id(), jboj.getString("user_id"));
				for(Upload up:uploadList){
					up.setAge(jboj.getString("user_id"));
					up.setName(log.getProduct_id());// setName(log.getName());//product id
					db.updateUpload(up);
				}
				for(Upload up:uploadList){
					try{
						
					JSONObject jobj = user.userUpload(up,android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()),alert.getDeviceID(home.this));
					str = jobj.getString("success");
					}catch (Exception e) {
						// TODO: handle exception
						str = null;
					}
				}
				uploadList = db.retriveUpload(jboj.getString("user_id"));
				for(Upload up:uploadList){
					db.deleteUpload(up.getAge());
					brands = db.retriveBrand(up.getName());
					for(Brand brand:brands){
						db.updateBrand(brand, "false");
					}
				}
				
				Editor edit = preference.edit();
				edit.putBoolean("logout", true);
				edit.putInt("upload", 2);
				edit.putInt("playMode", 2);
				if(connection.isConnectingToInternet())
				edit.putString("activity", null);
				//edit.clear();
				db.deleteLogin(jboj.getString("user_id"));
				//db.deleteUpload(preference.getString("uid", null));
				
				edit.commit();
				str = (jboj.getString("user_id"));//+"_"+jboj.getString("product_id");
				//db.u
			}catch(Exception e){
				str = null;
			}
			}
			return str;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(pd!=null && pd.isShowing())
				pd.dismiss();
			if(result!= null){
			/*	Constants.name="";Constants.age="";Constants.gender="";
			uploadList = db.retriveUpload(preference.getString("uid", null));
			
			for(Upload up:uploadList){
				up.setAge(result);
				up.setName(preference.getString("pid","1"));// setName(log.getName());//product id
				db.updateUpload(up);
			}
			db.deleteLogin(preference.getString("uid", null));*/
			//db.resetTables(MySQLiteHelper.TABLE_NAME);
				
			uploadList = db.retriveUpload(preference.getString("uid", null));
		if(uploadList!=null&&uploadList.size()>0){
			if(connection.isConnectingToInternet()){
				try{
					new Load().execute(uploadList);
					}
					catch (Exception e) {
						// TODO: handle exception
					}
		Editor edit = preference.edit();
		edit.putInt("upload", 2);
		edit.putInt("playMode", 2);
		//edit.clear();
		edit.commit();
		db.deleteUpload(preference.getString("uid", null));
		//db.resetTables(MySQLiteHelper.server_table);
		Toast.makeText(home.this, "success", Toast.LENGTH_LONG).show();
		
			}
		else{
			alert.showAlertDialog(home.this, "Warning", "Check your network connection", true,null);
		}

		}
		//finish();
		}else{
			alert.showAlertDialog(home.this, "Error", "Unable to process server", true,"finish");
		}
	}
	}	
	public void showAlert(){
		alertDialog = new AlertDialog.Builder(home.this).create();
		alertDialog.setMessage("Do you want to upload the old data?");
		alertDialog.setTitle("SurveyGame");
         alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				if(connection.isConnectingToInternet()){
						try{
							new Load().execute(uploadList);
							}
							catch (Exception e) {
								// TODO: handle exception
							}
				Editor edit = preference.edit();
				edit.putInt("upload", 2);
				edit.putInt("playMode", 2);
				//edit.clear();
				edit.commit();
				db.deleteUpload(preference.getString("uid", null));
				//db.resetTables(MySQLiteHelper.server_table);
				Toast.makeText(home.this, "success", Toast.LENGTH_LONG).show();
				}
				else{
					alert.showAlertDialog(home.this, "Warning", "Check your network connection", true,null);
				}
			}
		});
alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			
			}
		});
        // Showing Alert Message
       alertDialog.show();
	}
	private static final int RESULT_SETTINGS = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SETTINGS:
			showUserSettings();
			break;

		}

	}

	private void showUserSettings() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		StringBuilder builder = new StringBuilder();

		builder.append("\n Username: "
				+ sharedPrefs.getString("prefSyncThems", "NULL"));

		builder.append("\n Send report:"
				+ sharedPrefs.getString("prefSyncTimer", "NULL"));

		builder.append("\n Sync Frequency: "
				+ sharedPrefs.getString("prefSyncSpeed", "NULL"));
		builder.append("\n Sync Frequency: "
				+ sharedPrefs.getString("prefSyncFont", "NULL"));
		builder.append("\n Sync Frequency: "
				+ sharedPrefs.getInt("prefSyncFontSize",0));
	
	}
	public class Load extends AsyncTask<List<Upload>, Void, String>{

		private ProgressDialog pd;
		List<Upload> upload = null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(home.this);
			pd.setMessage("Loading...");
			pd.setMax(100);
			pd.setCancelable(false);
			pd.setIndeterminate(false);
			if(pd!= null && !pd.isShowing())
			pd.show();
			Toast.makeText(home.this, "please wait", Toast.LENGTH_LONG).show();
		}
		@Override
		protected String doInBackground(List<Upload>... arg0) {
			// TODO Auto-generated method stub
			UserFunction user = new UserFunction();
			String str = null;
			upload = arg0[0]; 
			for(Upload up:upload){
			try{
			JSONObject jobj = user.userUpload(up,android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()),alert.getDeviceID(home.this));
			str = jobj.getString("success");
			}catch (Exception e) {
				// TODO: handle exception
				str = null;
				e.printStackTrace();
				break;
			}
			finally{
				/*if(str!= null && up!=null)
				db.deleteUpload(up);*/
				
			}
			}
			return str;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(pd!=null && pd.isShowing())
				pd.dismiss();
			if(result != null && result.equals("1")){
				Log.d("check", result+"_");
				//if(upload!=null)
				uploadList = db.retriveUpload(preference.getString("uid", null));
				for(Upload up:uploadList){
					db.deleteUpload(up.getAge());
				}
				Editor edit = preference.edit();
				edit.putInt("upload", 2);
				edit.putInt("playMode", 2);
				edit.putString("activity", null);
				edit.clear();
				edit.commit();
			//finish();
			}else{
				try{
				alert.showAlertDialog(home.this, "Error", "Unable to process server", true,"finish");
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				}
		}
	}

}
