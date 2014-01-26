package com.example.survey_game;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.survey_game.Game.GameActivity;
import com.example.survey_game.Util.Constants;
import com.example.survey_game.WebService.ShowAlert;
import com.example.survey_game.WebService.UserFunction;
import com.example.survey_game.adapter.BrandAdapter;
import com.example.survey_game_fuction.Brand;
import com.example.survey_game_fuction.ConnectionDetector;
import com.example.survey_game_fuction.Login;
import com.example.survey_game_fuction.Upload;

public class dbrand extends Activity{

	Button b1,b2,b3,b4;
	LinearLayout linarView;
	DBfunction db;
	int noOfButton;
	Button btn;
	Global global;
	List<Brand> brands;
	private TextView text;
	Typeface tf;
	SharedPreferences preference;
	private static int play;
	private int uploadLimit;
	private String bid;
	private List<Upload> uploadList;
	ConnectionDetector connection;
	private ShowAlert alert;
	private AlertDialog alertDialog,alertDialog1;
	private RelativeLayout layout;
	private ImageView quit;
	private TextView logout;
	private String path;
	private Bitmap bmImg;
	GridView grid;
	public static int br = 1;
	BrandAdapter adapter;
	private File file;
	private boolean promptAlert;
	private String activity;
	
	void downloadFile(Brand up, String fileUrl, String fileName) throws URISyntaxException {
	    // here you can add folder in which you want the image to be stored
		String extStorageDirectory = null;
	    URL myFileUrl = null;
	    URI uri = null;
	    
	        if (android.os.Environment.getExternalStorageState().equals(
        	        android.os.Environment.MEDIA_MOUNTED)){
	        	//path = Constants.imagePath+"/"+preference.getString("pid","1");
	        	    file=new File(android.os.Environment.getExternalStorageDirectory(),"img_"+preference.getString("pid","1"));
	                extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	          }else{
	                file=dbrand.this.getCacheDir();
	         }
	        	//try{
	        	//File mFile = new File(path);
	        	if(!file.exists()){
	        		file.mkdir();
	        	}
	        	//extStorageDirectory += "/img_"+preference.getString("pid","1")+up.getBrandName()+".png";
	        	File f = new File(android.os.Environment.getExternalStorageDirectory()+"/img_"+preference.getString("pid","1"),up.getBrandName()+".png");
	        	bmImg = BitmapFactory.decodeFile(f.getAbsolutePath());
	        	if(bmImg!=null){
	        		up.setBrand_image_path(f.getAbsolutePath());
	        		db.updateBrand(up, up.getBrandStatus());
	        		preference.edit().putString(up.getBrandName(), f.getAbsolutePath()).commit();
	        		//preference.edit().putString(fileUrl, f.getAbsolutePath()).commit();
	        	}else{
	        		extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	        	    BitmapFactory.Options bmOptions;
	        	    bmOptions = new BitmapFactory.Options();
	        	    bmOptions.inSampleSize = 1;
	        	    
	        	    try {
	        	    	if(fileUrl.equals("http://annanagaronline.com/survey/logo/hp 2.jpg"))
	        	    		fileUrl = fileUrl.replace(" ", "");
	        	    	//String str = URLEncoder.encode(fileUrl,HTTP.UTF_8);
						myFileUrl = new URL(fileUrl);
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    			HttpURLConnection conn = null;
					try {
						conn = (HttpURLConnection) myFileUrl
								.openConnection();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    			conn.setDoInput(true);
	    			try {
						conn.connect();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    			InputStream is = null;
					try {
						is = conn.getInputStream();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}


	    	        bmImg = BitmapFactory.decodeStream(is);

	        	    OutputStream outStream = null;
	        	    //file=new File(android.os.Environment.getExternalStorageDirectory(),"img_"+preference.getString("pid","1")+"_"+up.getBrandName());
	                //file=new File(extStorageDirectory, "img_"+preference.getString("pid","1")+fileName+".png");
	        	    File file = new File(android.os.Environment.getExternalStorageDirectory()+"/img_"+preference.getString("pid","1"),up.getBrandName()+".png");
	        	     try {
	        	        outStream = new FileOutputStream(file);
	        	        bmImg.compress(Bitmap.CompressFormat.PNG, 100, outStream);
	        	        outStream.flush();
	        	        outStream.close();
	        	        up.setBrand_image_path(file.getAbsolutePath());
		        		db.updateBrand(up, up.getBrandStatus());
	        	        preference.edit().putString(up.getBrandName(), file.getAbsolutePath()).commit();
	        	      } catch (FileNotFoundException e) {
	        	         // TODO Auto-generated catch block
	        	       e.printStackTrace();

	        	      } catch (IOException e) {
	        	         // TODO Auto-generated catch block
	        	        e.printStackTrace();

	        	      }
	        	}
	        	
	      /*  }catch(Exception e){
	        	e.printStackTrace();
	        }
	        }
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	        Toast.makeText(this, "Url contains malicious character", Toast.LENGTH_LONG).show();

	    } catch (IOException e) {
	        e.printStackTrace();
	        Toast.makeText(this, "You must have network to download image on first time", Toast.LENGTH_LONG).show();
	    } */
	}
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle b)
	{
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
		setContentView(R.layout.activity_brand);
		layout = (RelativeLayout) findViewById(R.id.bg);
		text =(TextView) findViewById(R.id.imageView1);
		logout = (TextView)findViewById(R.id.textView1);
		logout.setVisibility(View.GONE);
		grid = (GridView) findViewById(R.id.scrollView1);
		//tf = Typeface.createFromAsset(getAssets(),"Oxida Regular.ttf");
		//quit = (ImageView)findViewById(R.id.exit);
		logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				br = db.getNoOfBrandPlayed(preference.getString("product_id", "1"));
				if(br>=2)
				logout();
				else
					alert.showAlertDialog(dbrand.this, "Survey Game", "Please play at least two brand before log out", true);
			}
		});
		
		preference = getSharedPreferences("Survey", MODE_PRIVATE);
		play = preference.getInt("playMode", 0);
		uploadLimit = preference.getInt("upload", 0);
		linarView = (LinearLayout) findViewById(R.id.linearLayout1);
		db = new DBfunction(this);
		Login log = db.getFirstRecord();
		SpannableString content = new SpannableString(getResources().getString(R.string.logout) +" "+log.getName());
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		logout.setText(content);
		
		connection = new ConnectionDetector(this);
		if(getIntent().getExtras()!=null){
	        
	        bid = getIntent().getExtras().getString("bid");
		}
		global = (Global)getApplicationContext();
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String font = Constants.fonts[Integer.parseInt((sharedPrefs.getString("prefSyncFont", "NULL")=="NULL")?"0":sharedPrefs.getString("prefSyncFont", "NULL"))];
		tf = Typeface.createFromAsset(getAssets(),font);
		text.setTypeface(tf);
		text.setText(text.getText()+"");
		brands = db.retriveBrand(preference.getString("product_id", "1"));
		alert = new ShowAlert(this);
		
		
		
		Log.d("check", brands+"_");
		Collections.shuffle(brands);
		Log.d("check before", brands+"_");
		//if(Constants.storeImage ==null || Constants.storeImage.size() <= 0 )
		if(connection.isConnectingToInternet())
			new ImageLoad().execute(brands);
		
		else
			callAdapter(brands);
		//generateView(brands);
		Log.d("check", bid+"_"+"+brand");
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				/*if(!brands.get(arg2).getBrandStatus().equals("true")){
					btn.setEnabled(false);
					btn.setClickable(false);
					btn.setTextColor(Color.BLACK);
					Intent i=new Intent(getBaseContext(),GameActivity.class);
					i.putExtra("bname", brands.get(arg2).getBrandName()+"_"+ brands.get(arg2).getBrandId());
					i.putExtra("brandid", brands.get(arg2).getBrandId());
					startActivity(i);
					finish();
				}*/
				
			}
		});
		//if(br>=1){
			//quit.setVisibility(View.VISIBLE);
			logout.setVisibility(View.VISIBLE);
		//}
	}
	private void callAdapter(List<Brand> brands2) {
		
		adapter = new BrandAdapter(dbrand.this, R.layout.grid_item, brands,Constants.storeImage,play,bid);
		grid.setAdapter(adapter);
		
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
				Toast.makeText(dbrand.this, "success", Toast.LENGTH_LONG).show();
				/*Intent intent = new Intent(dbrand.this, home.class);
				startActivity(intent);
				
				finish();*/
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
					Toast.makeText(dbrand.this, "success", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(dbrand.this, home.class);
					startActivity(intent);
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
			pd = new ProgressDialog(dbrand.this);
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
						
					JSONObject jobj = user.userUpload(up,android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()),alert.getDeviceID(dbrand.this));
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
		Toast.makeText(dbrand.this, "success", Toast.LENGTH_LONG).show();
		
			}
		else{
			alert.showAlertDialog(dbrand.this, "Warning", "Check your network connection", true);
		}

		}
		Intent intent = new Intent(dbrand.this, home.class);
		startActivity(intent);
		finish();
		}else{
			alert.showAlertDialog(dbrand.this, "Error", "Unable to process server", true);
		}
	}
	}	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putInt("br", br);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		preference.edit().putBoolean("pause", true).commit();
		promptAlert = preference.getBoolean("pause", true);
		super.onPause();
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
					alertDialog1 = new AlertDialog.Builder(dbrand.this).create();
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
			Toast.makeText(dbrand.this, "User Verification", Toast.LENGTH_SHORT).show();
			preference.edit().putBoolean("pause", false).commit();
			promptAlert = false;
		}
		
		}
		{
			showAll();
		}
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		promptAlert = true;
		preference.edit().putBoolean("pause", true).commit();
		super.onRestart();
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
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//unbindDrawables(layout);
		System.gc();
		if(pd!=null){
			pd.dismiss();
			pd.cancel();
		}
		
	}
	private ProgressDialog pd;
	public class Load extends AsyncTask<List<Upload>, Void, String>{

		
		List<Upload> upload = null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(dbrand.this);
			pd.setMessage("Loading...");
			pd.setMax(100);
			pd.setCancelable(false);
			pd.setIndeterminate(false);
			if(pd!= null && !pd.isShowing())
			pd.show();
			Toast.makeText(dbrand.this, "please wait", Toast.LENGTH_LONG).show();
		}
		@Override
		protected String doInBackground(List<Upload>... arg0) {
			// TODO Auto-generated method stub
			UserFunction user = new UserFunction();
			String str = null;
			upload = arg0[0]; 
			for(Upload up:upload){
			try{
				
			JSONObject jobj = user.userUpload(up,android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()),alert.getDeviceID(dbrand.this));
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
				/*for(Brand brand:brands){
					File file = new File(brand.getBrand_image_path());
					file.delete();
				}*/
				uploadList = db.retriveUpload(preference.getString("uid", null));
				for(Upload up:uploadList){
					db.deleteUpload(up.getAge());
				}
				Editor edit = preference.edit();
				edit.putBoolean("logout", true);
				edit.putInt("upload", 2);
				edit.putInt("playMode", 2);
				if(connection.isConnectingToInternet())
				edit.putString("activity", null);
				//edit.clear();
				
				edit.commit();
				brands = db.retriveBrand(preference.getString("product_id", "1"));
				for(Brand brand:brands){
					db.updateBrand(brand, "false");
				}
				db.deleteLogin(preference.getString("uid", null));
				db.deleteUpload(preference.getString("uid", null));
				//db.resetTables(MySQLiteHelper.TABLE_NAME);
				//db.resetTables(MySQLiteHelper.server_table);
				Toast.makeText(dbrand.this, "success", Toast.LENGTH_LONG).show();
			global.setLogin(null);
			Intent intent = new Intent(dbrand.this, home.class);
			startActivity(intent);
			finish();
				
			}else{
				try{
				alert.showAlertDialog(dbrand.this, "Error", "Unable to process server", true);
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				}
		}
	}
	
	
	int image = 1;
	public class ImageLoad extends AsyncTask<List<Brand>, Void, String>{

		
		List<Upload> upload = null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(dbrand.this);
			pd.setMessage("Loading...");
			pd.setMax(100);
			pd.setCancelable(false);
			pd.setIndeterminate(false);
			if(pd!= null && !pd.isShowing())
			pd.show();
			Toast.makeText(dbrand.this, "please wait", Toast.LENGTH_LONG).show();
		}
		@Override
		protected String doInBackground(List<Brand>... arg0) {
			// TODO Auto-generated method stub
			UserFunction user = new UserFunction();
			String str = null;
			List<Brand> upload = arg0[0]; 
			for(Brand up:upload){
			try{
			if(preference.getString(up.getBrand_image_path(), null) == null)
			downloadFile(up,up.getBrand_image_path(), "survey_"+image);
			str = "success";
			image++;
			}catch (Exception e) {
				// TODO: handle exception
				str = "failure";
			}
			finally{
				
				
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
			if(result != null){// && result.equals("success")){
				adapter = new BrandAdapter(dbrand.this, R.layout.grid_item, brands,Constants.storeImage,play,bid);
				grid.setAdapter(adapter);
			}
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	private void generateView(List<Brand> brands2) {
		// TODO Auto-generated method stub
		
		for(Brand brand:brands2){
		btn = new Button(this);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		btn.setLayoutParams(lp);
		btn.setId((brand.getBrandId()));
		btn.setBackgroundResource(R.drawable.button);
		btn.setGravity(Gravity.CENTER);
		btn.setText(brand.getBrandName());
		btn.setTextColor(Color.WHITE);
		
		if(tf!= null)
			btn.setTypeface(tf);
		btn.setTextSize(24*getResources().getDisplayMetrics().density);
		btn.setTag(brand.getBrandName()+"_"+brand.getBrandId());
		if(play== 2 && bid != null){
			Editor edit = preference.edit();
			edit.putInt("playMode", 0);
			edit.commit();
			Log.d("abc", bid+"+"+brand.getBrandId()+"+"+bid.equals(String.valueOf(brand.getBrandId())));
			if(bid.equals(String.valueOf(brand.getBrandId()))){
				btn.setEnabled(false);
				btn.setClickable(false);
				btn.setTextColor(Color.BLACK);
				db.updateBrand(brand,"true");
			}
		}else{
			
		}
		if(brand.getBrandStatus().equals("true")){
			btn.setEnabled(false);
			btn.setClickable(false);
			btn.setTextColor(Color.BLACK);
			
		}
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getBaseContext(),GameActivity.class);
				i.putExtra("bname", v.getTag().toString().split("_")[0]);
				i.putExtra("bid", v.getTag().toString().split("_")[1]);
				startActivity(i);
				finish();
				}
		});
		linarView.addView(btn);
		
		}
		
		
	}
	public void showAll() {
		// TODO Auto-generated method stub
		//if(preference.getBoolean("abc", false)){
			//preference.edit().putBoolean("abc", true).commit();
		br = db.getNoOfBrandPlayed(preference.getString("product_id", "1"));
		
		/*if(preference.getBoolean("end_a", true))
		br = br+1;*/
			/*for(Brand brand:brands){
			if(brand.getBrandStatus().equals("true")){
				//br++;
				br = br+1;
			}
		}*/
		
		noOfButton = brands.size();//db.getNoOfRows();
		if(noOfButton == br){
			if(alertDialog1!=null)
				alertDialog1.cancel();
			alertDialog = new AlertDialog.Builder(dbrand.this).create();
			alertDialog.setMessage("Thank you for playing all the brands!");
			alertDialog.setTitle("SurveyGame");
	        
	        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					/*if(connection.isConnectingToInternet()){
						uploadList = db.retriveUpload(preference.getString("uid", null));
							try{
								new Load().execute(uploadList);
								}
								catch (Exception e) {
									// TODO: handle exception
								}
					
					}else{
						alert.showAlertDialog(dbrand.this, "SurveyGame", "Check Your network", true);
					}*/
					logout();
							
				}
			});
	
	       alertDialog.show();
	}
	//else{
		//alert.showAlertDialog(dbrand.this, "SurveyGame", "Check Your network "+br +"_"+noOfButton, true);
	
	}
	//}
}
