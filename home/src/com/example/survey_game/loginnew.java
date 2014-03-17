package com.example.survey_game;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

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
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.example.survey_game.Util.Constants;
import com.example.survey_game.WebService.ShowAlert;
import com.example.survey_game.WebService.UserFunction;
import com.example.survey_game.video.VideoViewDemo;
import com.example.survey_game_fuction.Brand;
import com.example.survey_game_fuction.ConnectionDetector;
import com.example.survey_game_fuction.Login;
import com.example.survey_game_fuction.Service;
public class loginnew extends Activity implements OnClickListener{
	  private DBfunction datasource;
	EditText name_ed,age_ed;
    String n = "male";
	String sname,sage,m,f;
	RadioGroup genderradio;
	int rid;
	private ShowAlert alert;
	Global global;
	ConnectionDetector connection;
	ImageView submit;
	public Service service;
	public ProgressDialog pd;
	private SharedPreferences preference;
	private String uid;
	private String pid;
	private AlertDialog alertDialog;
	protected List<Brand> brands;
	ImageView male,female;
	private boolean checkLogin;
	private List<Brand> brands1;
	private String productid;
	public JSONArray brand_logo;
	private int download;
	private String activity;
	private int UserID;
	
		public void onCreate(Bundle b)
		{
			
			super.onCreate(b);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	       
			setContentView(R.layout.activity_login);
			alert = new ShowAlert(this);
			connection = new ConnectionDetector(this);
			datasource = new DBfunction(this);
			if(getIntent().getExtras()!=null){
				productid = getIntent().getExtras().getString("pid");
			}
			SharedPreferences sharedPrefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			preference = getSharedPreferences("Survey", MODE_PRIVATE);
			uid = preference.getString("uid", null);
			pid = preference.getString("pid", null);
			male = (ImageView) findViewById(R.id.imageView4);
			female = (ImageView) findViewById(R.id.imageView5);
			male.setOnClickListener(this);
			female.setOnClickListener(this);
			checkLogin = preference.getBoolean("logout", false);
			download = preference.getInt("download", 0);
			String font = Constants.fonts[Integer.parseInt((sharedPrefs.getString("prefSyncFont", "NULL")=="NULL")?"0":sharedPrefs.getString("prefSyncFont", "NULL"))];
			Typeface tf = Typeface.createFromAsset(getAssets(),font);

			Login log = datasource.getFirstRecord();//Get the last record of a table
		    //datasource.open();
			int br1 = 1;
			UserID = preference.getInt("offline_userId", 0);
			brands1= datasource.retriveBrand(preference.getString("product_id", "1"));
			if(brands1!=null && brands1.size()>0){
			for(Brand bra:brands1){
				if(bra.getBrandStatus().equals("true")|| bra.getBrandStatus().equals("false")){
					++br1;
				}
			}
			}
		    global = ((Global)getApplicationContext());
		    activity = preference.getString("activity", null);
			if(br1>1 && activity!=null && !checkLogin){
				global.setLogin(log);
				preference.edit().putBoolean("log", true).commit();
				
				Intent i=new Intent(getBaseContext(),dbrand.class);
				startActivity(i);
				finish();
			}else
			service = new Service();
			name_ed=(EditText)findViewById(R.id.name);
			age_ed=(EditText)findViewById(R.id.age);
			genderradio=(RadioGroup)findViewById(R.id.radioGroup1);
			rid=genderradio.getCheckedRadioButtonId();
			
			genderradio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub        
		               switch (checkedId) {
		               case R.id.male:
		                       n = "1";
		                       break;
		               case R.id.female:
		                        n = "2";
		                       break;		        
				}
		  	}
			});
			
			submit=(ImageView)findViewById(R.id.submit);
			submit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				//	 mp.start();
					
			
				genderradio=(RadioGroup)findViewById(R.id.radioGroup1);
				rid=genderradio.getCheckedRadioButtonId();
					
			
				if((name_ed.getText().toString()).length()==0)
				{
					Toast.makeText(getBaseContext(), "pls enter name", Toast.LENGTH_SHORT).show();
				}
				else if((age_ed.getText().toString()).length()==0)
				{
					Toast.makeText(getBaseContext(), "pls enter age", Toast.LENGTH_SHORT).show();
				}
				else if(rid==-1)
				{
					Toast.makeText(getBaseContext(), "pls select gender", Toast.LENGTH_SHORT).show();
				}
				else
				{
					if((Integer.parseInt(age_ed.getText().toString()))>=100)
					{
						Toast.makeText(getBaseContext(), "pls enter valid age", Toast.LENGTH_SHORT).show();
						age_ed.setText("");
					}
					else
					{
						if(connection.isConnectingToInternet()){
							
						Login login = new Login();
						login.setName(name_ed.getText().toString());
						login.setAge(Integer.parseInt(age_ed.getText().toString().trim()));
						login.setGender(n);
						login.setStatus(true);
						//datasource.resetTables(MySQLiteHelper.TABLE_NAME);
						if(datasource.insertLogin(login,productid)>0 && download ==0)
						{
							global.setLogin(login);
							preference.edit().putString("activity", "loggedin").commit();
							preference.edit().putString("name", null).commit();
							preference.edit().putString("age", null).commit();
							preference.edit().putString("gender", null).commit();
						/*	if(datasource.insertBrand(new Brand(1,"barand1"))>0)
							{
								datasource.insertBrand(new Brand(2,"brand2"));
								datasource.insertBrand(new Brand(3,"brand3"));
								name_ed.setText("");
								age_ed.setText("");
								Intent i=new Intent(getBaseContext(), tutorial.class);
								startActivity(i);
							}				
							else
							{
								Toast.makeText(loginnew.this, "brand insertion failure", Toast.LENGTH_SHORT).show();
							}*/
							new LoadData().execute(login);
							
						}
						}else{
							int br = 1;
							brands1= datasource.retriveBrand(preference.getString("product_id", "1"));
							if(brands1!=null && brands1.size()>0){
							for(Brand bra:brands1){
								if(bra.getBrandStatus().equals("true")|| bra.getBrandStatus().equals("false")){
									++br;
								}
							}
							}
							if(br>1 && download == 0)
							showAlert();
							else if(download ==1)
								alert.showAlertDialog(loginnew.this, "Survey Game"	, "Product not yet downloaded ", true,null);

							else 
							alert.showAlertDialog(loginnew.this, "Survey Game"	, "Please initialize by playing at least one game online ", true,null);
						}
					}
				
				}
				
			}
				
				
			});
			
				
		}
		public void showAlert(){
			alertDialog = new AlertDialog.Builder(loginnew.this).create();
			alertDialog.setMessage("You dont have network connection , proceed with offline mode");
			alertDialog.setTitle("SurveyGame");
	        // Setting alert dialog icon
	        //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
	 
	        // Setting OK Button
	        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Login login = new Login();
					Editor edit = preference.edit();
					
					login.setName(name_ed.getText().toString());
					login.setAge(Integer.parseInt(age_ed.getText().toString().trim()));
					login.setGender(n);
					login.setStatus(true);
					preference.edit().putBoolean("log", true).commit();
					
					preference.edit().putString("name", name_ed.getText().toString()).commit();
					preference.edit().putString("age", age_ed.getText().toString().trim()).commit();
					preference.edit().putString("gender", n).commit();
					/*Constants.name = name_ed.getText().toString();
					Constants.age = age_ed.getText().toString().trim();
					Constants.gender = n;*/
					
					if(datasource.insertLogin(login,preference.getString("product_id", "1"))>0){
						edit.putBoolean("logout", false);
						edit.putInt("offline_userId", datasource.getFirstRecord().getId()).commit();
						Log.d("abc_login", service.getUser_id()+"_"+service.getProduct_id());
						edit.putString("pid", preference.getString("product_id", "1"));
						edit.putString("uid", "offline_"+preference.getInt("offline_userId", 1));
						edit.putString("activity", "loggedin");
						edit.commit();
						datasource.updateLogin(datasource.getFirstRecord(), preference.getString("product_id", "1"), preference.getString("uid", null));
						brands = datasource.retriveBrand(preference.getString("product_id", "1"));
						for(Brand brand:brands){
							datasource.updateBrand(brand, "false");
						}
						global.setLogin(login);
						
						Intent i=new Intent(getBaseContext(),dbrand.class);
						startActivity(i);
						finish();
						
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
		
		class LoadData extends AsyncTask<Login, Void, JSONObject>{

			private JSONArray featureArrayDesc;
			private JSONArray featureArrayId;
			private JSONArray brandArrayDesc;
			private JSONArray brandArrayId;
			private JSONArray contarFeature;
			private JSONArray compFeature;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pd = new ProgressDialog(loginnew.this);
				pd.setMessage("Loading...");
				pd.setMax(100);
				pd.setCancelable(false);
				pd.setIndeterminate(false);
				pd.show();
			}
			
			@Override
			protected JSONObject doInBackground(Login... params) {
				// TODO Auto-generated method stub
				UserFunction user = new UserFunction();
				Login login = params[0];
				JSONObject jboj = null;
				try{
					jboj = user.userRegistration(login.getName(), String.valueOf(login.getAge()), login.getGender(),productid);
					global.setLogin(login);
				}catch(Exception e){
					
				}
				return jboj;
			}
			
			@Override
			protected void onPostExecute(JSONObject result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(pd.isShowing() && pd!= null)
					pd.dismiss();
				
					if(result != null){
						/*datasource.resetTables(MySQLiteHelper.BRAND_TABLE);
						datasource.resetTables(MySQLiteHelper.FEATURE_TABLE);
						datasource.resetTables(MySQLiteHelper.CONTRA_TYPE_TABLE);
						datasource.resetTables(MySQLiteHelper.COMP_TYPE_TABLE);*/
						try{
						Log.d("check", result+"_");
						//service.setStatus(result.getString("success"));
						service.setUser_id(result.getString("user_id"));
						Login log = global.getLogin();
						datasource.updateLogin(datasource.getFirstRecord(), productid, service.getUser_id());
						//service.setProduct_name(result.getString("product_name"));
						//service.setProduct_id(result.getString("product_id"));
						//service.setProduct_name((String)result.getJSONArray("product_name").get(0));
						//service.setProduct_name((String)result.getJSONArray("product_id").get(0));
						/*featureArrayDesc= result.getJSONArray("feature_name");
						featureArrayId= result.getJSONArray("feature_id");
						brandArrayDesc= result.getJSONArray("brand_name");
						brandArrayId= result.getJSONArray("brand_id");
						contarFeature = result.getJSONArray("contra_feature");
						compFeature = result.getJSONArray("comp_feature");
						brand_logo = result.getJSONArray("brand_logo");*/
						Editor edit = preference.edit();
						//Log.d("abc_login", service.getUser_id()+"_"+service.getProduct_id());
						edit.putString("pid", productid);
						edit.putString("uid", service.getUser_id());
						edit.commit();
						}
						catch(Exception e){
							System.out.println("Error="+e);
						}
						/*List<Feature> featureList = new ArrayList<Service.Feature>();
						for(int i=0; i<featureArrayDesc.length();i++){
							Feature feature = new Feature();
							try {
								feature.setFeature_id((String) featureArrayId.get(i));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								feature.setFeature_name((String) featureArrayDesc.get(i));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								datasource.insertFeaature(new com.example.survey_game_fuction.Feature(Integer.parseInt((String) featureArrayId.get(i)),(String) featureArrayDesc.get(i)));
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							featureList.add(feature);
						}
						service.setFeature(featureList);
						com.example.survey_game_fuction.Service.compFeature comp = new compFeature();
						for(int contra=0;contra<contarFeature.length();contra++){
							JSONObject contraObj = null;
							try {
								contraObj = contarFeature.getJSONObject(contra);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							List<Feature> featureList1 = new ArrayList<Service.Feature>();
							for(int feature=0;feature<service.getFeature().size();feature++){
							try {
								if(contraObj.getString(service.getFeature().get(feature).getFeature_id())!= null){
									comp.setFeature_id(service.getFeature().get(feature).getFeature_id());
									JSONArray arr = contraObj.getJSONArray(service.getFeature().get(feature).getFeature_id());
									ContraFeature localContra = new ContraFeature();
									
									localContra.setFeature_id(comp.getFeature_id());
									for(int a=0;a<arr.length();a++){
										switch (a) {
										case 0:
											localContra.setContra_feature_1((String)arr.get(a)==null?"0":(String)arr.get(a));
											break;
										case 1:
											localContra.setContra_feature_2((String)arr.get(a)==null?"0":(String)arr.get(a));
											break;
										case 2:
											localContra.setContra_feature_3((String)arr.get(a)==null?"0":(String)arr.get(a));
											break;
										case 3:
											localContra.setContra_feature_4((String)arr.get(a)==null?"0":(String)arr.get(a));
											break;
										case 4:
											localContra.setContra_feature_5((String)arr.get(a)==null?"0":(String)arr.get(a));
											break;
										default:
											break;
										}
										
										if(!arr.get(a).equals("0")){
										for(int k=0;k<service.getFeature().size();k++){
											if((arr.get(a)).equals(service.getFeature().get(k).getFeature_id())){
												Feature feature1 = new Feature();
												feature1.setFeature_id(service.getFeature().get(k).getFeature_id());
												feature1.setFeature_name(service.getFeature().get(k).getFeature_name());
												featureList1.add(feature1);
												break;
											}
										}
										}
									}
									datasource.insertContra(localContra);
									comp.setContraFeatureList(featureList1);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							}
						}
						
						for(int contra1=0;contra1<compFeature.length();contra1++){
							JSONObject contraObj1 = null;
							
							try {
								contraObj1 = compFeature.getJSONObject(contra1);
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							List<Feature> featureList2 = new ArrayList<Service.Feature>();
							
							for(int feature=0;feature<service.getFeature().size();feature++){
							try {
								String id = null;
								try{
									id=contraObj1.getString(service.getFeature().get(feature).getFeature_id());
								}catch (Exception e) {
									// TODO: handle exception
								}
								if(id!= null){
									comp.setFeature_id(service.getFeature().get(feature).getFeature_id());
									JSONArray arr = contraObj1.getJSONArray(service.getFeature().get(feature).getFeature_id());
									CompFeature localComp = new CompFeature();
									localComp.setFeature_id(comp.getFeature_id());
									for(int a=0;a<arr.length();a++){
										switch (a) {
										case 0:
											localComp.setContra_feature_1((String)arr.get(a)==null?"0":(String)arr.get(a));
											break;
										case 1:
											localComp.setContra_feature_2((String)arr.get(a)==null?"0":(String)arr.get(a));
											break;
										case 2:
											localComp.setContra_feature_3((String)arr.get(a)==null?"0":(String)arr.get(a));
											break;
										case 3:
											localComp.setContra_feature_4((String)arr.get(a)==null?"0":(String)arr.get(a));
											break;
										case 4:
											localComp.setContra_feature_5((String)arr.get(a)==null?"0":(String)arr.get(a));
											break;
										default:
											break;
										}
										
										if(!arr.get(a).equals("0")){
										for(int k=0;k<service.getFeature().size();k++){
											
											if((arr.get(a)).equals(service.getFeature().get(k).getFeature_id())){
												Feature feature1 = new Feature();
												feature1.setFeature_id(service.getFeature().get(k).getFeature_id());
												feature1.setFeature_name(service.getFeature().get(k).getFeature_name());
												featureList2.add(feature1);
												break;
											}
											
										}
										}
										
										
									}
									comp.setCompFeatureList(featureList2);
									datasource.insertComp(localComp);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							}
						}
						
						service.setCompFeatures(comp);
						//Log.d("check", service.getCompFeatures()+"_"+service.getCompFeatures().getCompFeatureList().size()+"_"+service.getCompFeatures().getContraFeatureList().size());
						List<Brands> brandList = new ArrayList<Service.Brands>();
						for(int j=0; j<brandArrayDesc.length();j++){
							Brands brand = new Brands();
							try {
								brand.setBrand_id((String) brandArrayId.get(j));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								brand.setBrand_name((String) brandArrayDesc.get(j));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								brand.setBrand_image_path((String) "http://annanagaronline.com/survey/"+brand_logo.get(j));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								datasource.insertBrand(new Brand(Integer.parseInt((String)brandArrayId.get(j)),(String) brandArrayDesc.get(j),(String) "http://annanagaronline.com/survey/"+ brand_logo.get(j)));
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
				
							brandList.add(brand);
						}
						service.setBrand(brandList);
						global.setService(service);*/
						/*if(datasource.insertLogin(login)>0)
						{
							if(datasource.insertBrand(new Brand(1,"barand1"))>0)
							{
								datasource.insertBrand(new Brand(2,"brand2"));
								datasource.insertBrand(new Brand(3,"brand3"));
								name_ed.setText("");
								age_ed.setText("");
								Intent i=new Intent(getBaseContext(), tutorial.class);
								startActivity(i);
							}				
							else
							{
								Toast.makeText(loginnew.this, "brand insertion failure", Toast.LENGTH_SHORT).show();
							}
						}
						
*/													

						Intent i=new Intent(getBaseContext(), VideoViewDemo.class);
						
						startActivity(i);
						finish();
						//alert.showAlertDialog(loginnew.this, "Success", "Inserted Successfully", true);
					}else
					{
						alert.showAlertDialog(loginnew.this, "Error", "Failure", true,"finish");
						Editor edit = preference.edit();
						global.setLogin(null);
						Log.d("abc_login", service.getUser_id()+"_"+service.getProduct_id());
						edit.putBoolean("logout", true);
						edit.commit();
					}
				}
				
				
			//}
		}

		@Override
		protected void onSaveInstanceState(Bundle outState) {
			// TODO Auto-generated method stub
			super.onSaveInstanceState(outState);
			outState.putString("name",name_ed.getText().toString());
			outState.putString("age", sage);
			
		}
		public boolean onCreateOptionsMenu(Menu menu) {
		    menu.add(0, 0, 0, "Change Timer");
		    menu.add(0, 1, 1, "Set themes");
		    menu.add(0, 2, 2, "Set balloon speed");
		    return true;
		}
		public boolean onOptionsItemSelected(MenuItem item) {
		    switch (item.getItemId()) {
		    case 0:
		       Toast.makeText(getBaseContext(), "change timer", Toast.LENGTH_SHORT).show();
		        return true;
		    case 1:
		    	Toast.makeText(getBaseContext(), "Set themes", Toast.LENGTH_SHORT).show();
		        return true;
		    case 2:
		    	Toast.makeText(getBaseContext(), "Change balloon speed", Toast.LENGTH_SHORT).show();
		        return true;
		    }
		    return false;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
            case R.id.imageView4:
            	((RadioButton)(genderradio.getChildAt(0))).setChecked(true);
            	((RadioButton)(genderradio.getChildAt(1))).setChecked(false);
                    n = "1";
                    break;
            case R.id.imageView5:
            	((RadioButton)(genderradio.getChildAt(0))).setChecked(false);
            	((RadioButton)(genderradio.getChildAt(1))).setChecked(true);
                     n = "2";
                    break;
		}
		}
	
}
