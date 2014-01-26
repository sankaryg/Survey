package com.example.survey_game.Game;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.survey_game.DBfunction;
import com.example.survey_game.Global;
import com.example.survey_game.MySQLiteHelper;
import com.example.survey_game.R;
import com.example.survey_game.dbrand;
import com.example.survey_game.loginnew;
import com.example.survey_game.GIF.GifDecoderView;
import com.example.survey_game.Util.AnimationView;
import com.example.survey_game.Util.Constants;
import com.example.survey_game.WebService.ShowAlert;
import com.example.survey_game.WebService.UserFunction;
import com.example.survey_game.cup.MainGamePanel;
import com.example.survey_game.dbrand.Load;
import com.example.survey_game.entity.ExtensionFilter;
import com.example.survey_game_fuction.Brand;
import com.example.survey_game_fuction.ConnectionDetector;
import com.example.survey_game_fuction.Login;
import com.example.survey_game_fuction.Upload;

public class GameEnd extends Activity{
	private String screen;
	private ImageView playAgain;
	private ImageView quit;
	private Button logout;
	private ImageView brand;
	private String bname;
	DBfunction db;
	private SharedPreferences preference;
	private int count=1;
	private int score=0;
	private TextView scoreTet;
	private int uploadLimit;
	public static String bid;
	ShowAlert alert;
	ConnectionDetector connection;
	protected String type;
	private AlertDialog alertDialog;
	Global global;
	private List<Upload> uploadList;
	MainGamePanel coin_rotate;
	//ImageView coin_rotate;
	private AnimationDrawable anima;
	private LinearLayout layout;
	public ProgressDialog pd;
	public List<Brand> brands;
	private boolean promptAlert;
	private AlertDialog alertDialog1;
	private String activity;
	private List<Brand> brandsList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.gamewon);
						
	}
	 
	public void init(){
		layout = (LinearLayout) findViewById(R.id.bg);
		connection = new ConnectionDetector(this);
		global = (Global)getApplicationContext();
		scoreTet = (TextView)findViewById(R.id.textView1);
		
		//coin_rotate = (MainGamePanel) findViewById(R.id.imagecoin);
		//coin_rotate.setZOrderOnTop(true);    // necessary
		//SurfaceHolder sfhTrackHolder = coin_rotate.getHolder();
		//sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);
		//coin_rotate.setAsset("piggy.gif");
		//coin_rotate = (ImageView) findViewById(R.id.imagecoin);
		if(getIntent().getExtras()!=null){
	        bname = getIntent().getExtras().getString("bname");
	        bid = getIntent().getExtras().getString("bid");
	        score = getIntent().getExtras().getInt("score");
	        Log.d("check", score+"_"+getIntent().getExtras().get("score")+"_"+getIntent().getExtras().getInt("score"));
		}
		if(score < 0){
			score = 0;
			Constants.endScreen = "rookie";
			//coin_rotate.setImageResource(R.drawable.rookie);
		}
		else if(score <= (Constants.maxScore * 0.5)){
			Constants.endScreen = "rookie";
			//coin_rotate.setImageResource(R.drawable.rookie);
		}else if( score <= (Constants.maxScore * 0.8) && score > (Constants.maxScore * 0.5)){
			Constants.endScreen = "pro";
			//coin_rotate.setImageResource(R.drawable.pro);
		}else if(score > (Constants.maxScore * 0.8) ){//&& score <= Constants.maxScore
			Constants.endScreen = "expert";
			//coin_rotate.setImageResource(R.drawable.expert);
		}

		coin_rotate = (MainGamePanel) findViewById(R.id.imagecoin);
		coin_rotate.setZOrderOnTop(true);    // necessary
		SurfaceHolder sfhTrackHolder = coin_rotate.getHolder();
		sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);
		alert = new ShowAlert(this);
		brand = (ImageView) findViewById(R.id.btnBrand);
		scoreTet.setText(String.valueOf(score));
		Bundle b = getIntent().getExtras();
		db =new DBfunction(this);
		Log.d("date", java.text.DateFormat.getDateTimeInstance().format(new Date())+"_"+alert.getDeviceID(this));
		
		/*if(b!= null)
			screen = b.getString("screen");
		Log.d(ACTIVITY_SERVICE, screen);*/
		preference = getSharedPreferences("Survey", MODE_PRIVATE);
		count = preference.getInt("playMode", 1);
		uploadLimit = preference.getInt("upload", 1);
		//count = 1;
		brand.setVisibility(View.VISIBLE);
		playAgain = (ImageView)findViewById(R.id.button1);
		if(count == 2){
			
			//playAgain.setVisibility(View.GONE);
		}
		brand.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(coin_rotate!=null)
				coin_rotate.stop();
				Editor edit = preference.edit();
				edit.putInt("upload", 2);
				edit.putInt("playMode", 2);
				edit.commit();
				preference.edit().putBoolean("end_a", true).commit();
				Intent startGame = new Intent(getApplicationContext(), dbrand.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startGame.putExtra("bid", bid);
				startActivity(startGame);
				finish();
				
			}
		});
			//playAgain.setText("Brand");
		playAgain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(coin_rotate!=null)
				coin_rotate.stop();
				if(count != 2){
					Editor edit = preference.edit();
					edit.putInt("playMode", ++count);
					edit.commit();
				Intent startGame = new Intent(getApplicationContext(), GameActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startGame.putExtra("bname", bname+"_"+bid);
				startGame.putExtra("bid", bid);
				startActivity(startGame);
				finish();
				}else{
					showAlert();
				}
				Log.d("check", bid+"_"+bname+"+end");
			}
		});
		quit = (ImageView)findViewById(R.id.button2);
		quit.setOnClickListener(new OnClickListener() {
			private List<Brand> brands;
			int br = 1;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(coin_rotate!=null)
				coin_rotate.stop();
				brands = db.retriveBrand(preference.getString("product_id", "1"));
				type = "quit";
				for(Brand bra:brands){
					if(bra.getBrandStatus().equals("true")){
						++br;
					}
				}
				br = db.getNoOfBrandPlayed(preference.getString("product_id", "1"));
				if(br>=2){
					if(connection.isConnectingToInternet()){
						List<Login> Offlinelogin = db.retriveUsers();
						uploadList = db.retriveUpload(preference.getString("uid", null));
						//for(Upload up : uploadList){
						if(Offlinelogin.size()>0){
							new InsertData().execute(Offlinelogin);
						}else {
							try{
								new Load().execute(uploadList);
								//Thread.sleep(1000);
								}
								catch (Exception e) {
									// TODO: handle exception
								}
						//}
						/*db.logoutUser(GameEnd.this, MySQLiteHelper.TABLE_NAME);
						db.logoutUser(GameEnd.this, MySQLiteHelper.BRAND_TABLE);
						db.logoutUser(GameEnd.this, MySQLiteHelper.FEATURE_TABLE);
						db.logoutUser(GameEnd.this, MySQLiteHelper.CONTRA_TYPE_TABLE);
						db.logoutUser(GameEnd.this, MySQLiteHelper.COMP_TYPE_TABLE);*/
						Editor edit = preference.edit();
						edit.putBoolean("logout", true);
						edit.commit();
						db.deleteLogin(preference.getString("uid", null));
						db.deleteUpload(preference.getString("uid", null));
						//db.resetTables(MySQLiteHelper.TABLE_NAME);
						//db.resetTables(MySQLiteHelper.server_table);
						Toast.makeText(GameEnd.this, "success", Toast.LENGTH_LONG).show();
						global.setLogin(null);
						if(type.equals("quit")){
							//finish();
						}
						}
					}
					else{
						alertDialog = new AlertDialog.Builder(GameEnd.this).create();
						alertDialog.setMessage("Do you want to proceed with offline mode?");
						alertDialog.setTitle("SurveyGame");
				        // Setting alert dialog icon
				        //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
				 
				        // Setting OK Button
				        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								//db.logoutUser(GameEnd.this, MySQLiteHelper.TABLE_NAME);
								Editor edit = preference.edit();
								edit.putBoolean("logout", true);
								edit.commit();
								db.deleteLogin(preference.getString("uid", null));
								//db.resetTables(MySQLiteHelper.TABLE_NAME);
								uploadList = db.retriveUpload(preference.getString("uid", null));
								finish();
										
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
				}else{
					alert.showAlertDialog(GameEnd.this, "Survey Game", "Please play at least another brand before you quit", true);
				}
			}
		});
		/*logout = (Button) findViewById(R.id.button3);
		logout.setOnClickListener(new OnClickListener() {
			private List<Brand> brands;
			int br = 1;
			private List<Upload> uploadList;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				type ="logout";
				
				brands = db.retriveBrand();
				for(Brand bra:brands){
					if(bra.getBrandStatus().equals("true")){
						++br;
					}
				}
				if(br>=2){
					if(connection.isConnectingToInternet()){
						uploadList = db.retriveUpload();
						for(Upload up : uploadList){
							try{
							new Load().execute(up);
							//Thread.sleep(1000);
							}
							catch (Exception e) {
								// TODO: handle exception
							}
							}
					
						db.logoutUser(GameEnd.this, MySQLiteHelper.TABLE_NAME);
						db.logoutUser(GameEnd.this, MySQLiteHelper.BRAND_TABLE);
						db.logoutUser(GameEnd.this, MySQLiteHelper.FEATURE_TABLE);
						db.logoutUser(GameEnd.this, MySQLiteHelper.CONTRA_TYPE_TABLE);
						db.logoutUser(GameEnd.this, MySQLiteHelper.COMP_TYPE_TABLE);
						db.resetTables(MySQLiteHelper.TABLE_NAME);
						db.resetTables(MySQLiteHelper.server_table);
						Toast.makeText(GameEnd.this, "success", Toast.LENGTH_LONG).show();
						if(!type.equals("quit")){
							Intent startGame = new Intent(getApplicationContext(), loginnew.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(startGame);
							finish();
							}
					}
					else{
						alertDialog = new AlertDialog.Builder(GameEnd.this).create();
						alertDialog.setMessage("Do you want to proceed with offline mode?");
						alertDialog.setTitle("SurveyGame");
				        // Setting alert dialog icon
				        //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
				 
				        // Setting OK Button
				        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								//db.resetTables(MySQLiteHelper.TABLE_NAME);
								Editor edit = preference.edit();
								edit.putBoolean("logout", true);
								edit.commit();
								Intent startGame = new Intent(getApplicationContext(), loginnew.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(startGame);
								finish();
										
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
				}else{
					alert.showAlertDialog(GameEnd.this, "Warning", "You need to select two brand", true);
				}
			}
		});*/
		brandsList = db.retriveBrand(preference.getString("product_id", "1"));
		for(Brand brand:brandsList){
		if(bid.equals(String.valueOf(brand.getBrandId()))){
			brand.setBrandStatus("true");
			db.updateBrand(brand,"true");
		}
		}
	}
	@SuppressWarnings("unchecked")
	public void logout(){
		/*if(connection.isConnectingToInternet()){
			uploadList = db.retriveUpload();
				try{
					new Load().execute(uploadList);
					}
					catch (Exception e) {
						// TODO: handle exception
					}
		
		}else{
			alert.showAlertDialog(dbrand.this, "SurveyGame", "Check Your network", true);
		}*/
		
			/*brands = db.retriveBrand();
			for(Brand bra:brands){
				if(bra.getBrandStatus().equals("true")){
					++br;
				}
			}*/
		activity = preference.getString("activity", null);
			uploadList = db.retriveUpload(preference.getString("uid", null));
			List<Login> Offlinelogin = db.retriveUsers();
			//if(br>=2){
				if(connection.isConnectingToInternet()){
					if(Offlinelogin.size()>0){
						new InsertData().execute(Offlinelogin);
					}else 
					if(activity!=null){
					
						try{
							new Load().execute(uploadList);
							}
							catch (Exception e) {
								// TODO: handle exception
							}
				Editor edit = preference.edit();
				edit.putInt("upload", 2);
				edit.putInt("playMode", 2);
				preference.edit().putString("activity", null).commit();
				//edit.clear();
				edit.commit();
							//db.resetTables(MySQLiteHelper.TABLE_NAME);
						//db.resetTables(MySQLiteHelper.server_table);
						global.setLogin(null);
				Toast.makeText(GameEnd.this, "success", Toast.LENGTH_LONG).show();
				finish();
					}/*else{
						Constants.name = preference.getString("name", null);
						Constants.age = preference.getString("age", null);
						Constants.gender = preference.getString("gender", null);
						if(Constants.name!=null && Constants.age!=null && Constants.gender !=null)
							new InsertData().execute();
							else
							alert.showAlertDialog(dbrand.this, "Warning", "You are not logged in and no data to upload", true);

						

					}
*/					}
				else{
					Editor edit = preference.edit();
					edit.putInt("upload", 2);
					edit.putInt("playMode", 2);
					preference.edit().putString("activity", null).commit();
					//edit.clear();
					edit.commit();
					brands = db.retriveBrand(preference.getString("product_id", "1"));
					for(Brand brand:brands){
						db.updateBrand(brand, "false");
					}
								//db.resetTables(MySQLiteHelper.TABLE_NAME);
							//db.resetTables(MySQLiteHelper.server_table);
							global.setLogin(null);
					Toast.makeText(GameEnd.this, "success", Toast.LENGTH_LONG).show();
					finish();
					//alert.showAlertDialog(dbrand.this, "Warning", "Check your network connection", true);
				}
			//}
			//}else{
								
				
				//}
			
	}
	class InsertData extends AsyncTask<List<Login>, Void, String>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(GameEnd.this);
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
						
					JSONObject jobj = user.userUpload(up,android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()),alert.getDeviceID(GameEnd.this));
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
		Toast.makeText(GameEnd.this, "success", Toast.LENGTH_LONG).show();
		
			}
		else{
			alert.showAlertDialog(GameEnd.this, "Warning", "Check your network connection", true);
		}

		}
		finish();
		}else{
			alert.showAlertDialog(GameEnd.this, "Error", "Unable to process server", true);
		}
	}
	}	
	public void showAlert(){
		alertDialog = new AlertDialog.Builder(GameEnd.this).create();
		alertDialog.setMessage("Do you  want to play again?");
		alertDialog.setTitle("SurveyGame");
        // Setting alert dialog icon
        //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
 
        // Setting OK Button
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Intent startGame = new Intent(getApplicationContext(), GameActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startGame.putExtra("bname", bname);
				startGame.putExtra("bid", bid);
				startActivity(startGame);
				finish();
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
	public class Load extends AsyncTask<List<Upload>, Void, String>{

		private ProgressDialog pd;
		List<Upload> upload = null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(GameEnd.this);
			pd.setMessage("Loading...");
			pd.setMax(100);
			pd.setCancelable(false);
			pd.setIndeterminate(false);
			if(pd!= null && !pd.isShowing())
			pd.show();
			Toast.makeText(GameEnd.this, "please wait", Toast.LENGTH_LONG).show();
		}
		@Override
		protected String doInBackground(List<Upload>... arg0) {
			// TODO Auto-generated method stub
			UserFunction user = new UserFunction();
			String str = null;
			upload = arg0[0]; 
			for(Upload up:upload){
			try{
			JSONObject jobj = user.userUpload(up,android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()),alert.getDeviceID(GameEnd.this));
			str = jobj.getString("success");
			}catch (Exception e) {
				// TODO: handle exception
				str = null;
			}
			finally{
				/*if(up!=null)
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
				edit.putString("activity", null);
				edit.putInt("upload", 2);
				edit.putInt("playMode", 2);
				//edit.clear();
				edit.commit();
			finish();
			}else{
				try{
				alert.showAlertDialog(GameEnd.this, "Error", "Unable to process server", true);
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				}
		}
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//setContentView(layout);
		init();

	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(coin_rotate!=null){
			coin_rotate.stop();
			coin_rotate=null;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindDrawables(layout);
		System.gc();
		
	}
	private void unbindDrawables(View view) {
        System.gc();
        Runtime.getRuntime().gc();
        if (view.getBackground() != null) {
        view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
            unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
        ((ViewGroup) view).removeAllViews();
        }
    }
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(promptAlert){
			 if (alertDialog1 != null) {
					alertDialog1.cancel();
			 }
			 //else
			 {
					alertDialog1 = new AlertDialog.Builder(GameEnd.this).create();
					alertDialog1.setMessage("Are you "+db.getFirstRecord().getName()+"? Yes to proceed. No to Logout");
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

								}
							});
					alertDialog1.setButton(Dialog.BUTTON_NEGATIVE, "NO",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									alertDialog1.cancel();
									logout();
								}
							});
					// Showing Alert Message
					alertDialog1.show();
			//alert.showAlertDialog(dbrand.this, "SurveyGame", "Check Your network", true);
			Toast.makeText(GameEnd.this, "User Verification", Toast.LENGTH_SHORT).show();
			preference.edit().putBoolean("pause", false).commit();
			promptAlert = false;
		}
		
		}
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		preference.edit().putBoolean("pause", true).commit();
		promptAlert = preference.getBoolean("pause", true);
		super.onPause();
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		promptAlert = true;
		preference.edit().putBoolean("pause", true).commit();
		super.onRestart();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(coin_rotate!=null){
			coin_rotate.stop();
			coin_rotate = null;
		}
		finish();
		super.onBackPressed();
	}
	
}

