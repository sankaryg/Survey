package com.example.survey_game;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.survey_game.WebService.ShowAlert;
import com.example.survey_game.WebService.UserFunction;
import com.example.survey_game_fuction.Brand;
import com.example.survey_game_fuction.CompFeature;
import com.example.survey_game_fuction.ContraFeature;
import com.example.survey_game_fuction.Service;
import com.example.survey_game_fuction.Service.Brands;
import com.example.survey_game_fuction.Service.Feature;
import com.example.survey_game_fuction.Service.compFeature;

public class CommonTask extends AsyncTask<String, Void, JSONObject>{

	IAsyncTask asyncTask;
	private String product_id;
	private Activity activity;
	private ProgressDialog pd;
	private JSONArray featureArrayDesc;
	private JSONArray featureArrayId;
	private JSONArray brandArrayDesc;
	private JSONArray brandArrayId;
	private JSONArray contarFeature;
	private JSONArray compFeature;
	private DBfunction datasource;
	private JSONArray brand_logo;
	private ShowAlert alert;
	public CommonTask(Activity activity,String product_id) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
	asyncTask = (IAsyncTask)activity;
	this.product_id = product_id;
	alert = new ShowAlert(activity);
	datasource = new DBfunction(activity);
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pd = new ProgressDialog(activity);
		pd.setMessage("Loading...");
		pd.setMax(100);
		pd.setCancelable(false);
		pd.setIndeterminate(false);
		pd.show();
	}
	@Override
	protected JSONObject doInBackground(String... params) {
		// TODO Auto-generated method stub
		UserFunction user = new UserFunction();
		JSONObject jboj = null;
		try{
			jboj = user.userProductById(product_id);
			
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
				//service.setUser_id(result.getString("user_id"));
				//service.setProduct_name(result.getString("product_name"));
				//service.setProduct_id(result.getString("product_id"));
				//service.setProduct_name((String)result.getJSONArray("product_name").get(0));
				//service.setProduct_name((String)result.getJSONArray("product_id").get(0));
				featureArrayDesc= result.getJSONArray("feature_name");
				featureArrayId= result.getJSONArray("feature_id");
				brandArrayDesc= result.getJSONArray("brand_name");
				brandArrayId= result.getJSONArray("brand_id");
				contarFeature = result.getJSONArray("contra_feature");
				compFeature = result.getJSONArray("comp_feature");
				brand_logo = result.getJSONArray("brand_logo");
				//Editor edit = preference.edit();
				//Log.d("abc_login", service.getUser_id()+"_"+service.getProduct_id());
				//edit.putString("pid", productid);
				//edit.putString("uid", service.getUser_id());
				//edit.commit();
				}
				catch(Exception e){
					System.out.println("Error="+e);
				}
				List<Feature> featureList = new ArrayList<Service.Feature>();
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
						datasource.insertFeaature(new com.example.survey_game_fuction.Feature(Integer.parseInt((String) featureArrayId.get(i)),(String) featureArrayDesc.get(i)),product_id);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					featureList.add(feature);
				}
				//service.setFeature(featureList);
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
					for(int feature=0;feature<featureList.size();feature++){
					try {
						if(contraObj.getString(featureList.get(feature).getFeature_id())!= null){
							comp.setFeature_id(featureList.get(feature).getFeature_id());
							JSONArray arr = contraObj.getJSONArray(featureList.get(feature).getFeature_id());
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
								for(int k=0;k<featureList.size();k++){
									if((arr.get(a)).equals(featureList.get(k).getFeature_id())){
										Feature feature1 = new Feature();
										feature1.setFeature_id(featureList.get(k).getFeature_id());
										feature1.setFeature_name(featureList.get(k).getFeature_name());
										featureList1.add(feature1);
										break;
									}
								}
								}
							}
							datasource.insertContra(localContra,product_id);
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
					
					for(int feature=0;feature<featureList.size();feature++){
					try {
						String id = null;
						try{
							id=contraObj1.getString(featureList.get(feature).getFeature_id());
						}catch (Exception e) {
							// TODO: handle exception
						}
						if(id!= null){
							comp.setFeature_id(featureList.get(feature).getFeature_id());
							JSONArray arr = contraObj1.getJSONArray(featureList.get(feature).getFeature_id());
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
								for(int k=0;k<featureList.size();k++){
									
									if((arr.get(a)).equals(featureList.get(k).getFeature_id())){
										Feature feature1 = new Feature();
										feature1.setFeature_id(featureList.get(k).getFeature_id());
										feature1.setFeature_name(featureList.get(k).getFeature_name());
										featureList2.add(feature1);
										break;
									}
									
								}
								}
								
								
							}
							comp.setCompFeatureList(featureList2);
							datasource.insertComp(localComp,product_id);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				}
				
				//service.setCompFeatures(comp);
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
						datasource.insertBrand(new Brand(Integer.parseInt((String)brandArrayId.get(j)),(String) brandArrayDesc.get(j),(String) "http://annanagaronline.com/survey/"+ brand_logo.get(j)),product_id);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
		
					brandList.add(brand);
				}
				asyncTask.loadCompleted();
				//service.setBrand(brandList);
				//global.setService(service);
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

				/*Intent i=new Intent(getBaseContext(), VideoViewDemo.class);
				
				startActivity(i);
				finish();
*/				//alert.showAlertDialog(loginnew.this, "Success", "Inserted Successfully", true);
			}else
			{
				alert.showAlertDialog(activity, "Error", "Failure", true);
				/*Editor edit = preference.edit();
				global.setLogin(null);
				Log.d("abc_login", service.getUser_id()+"_"+service.getProduct_id());
				edit.putBoolean("logout", true);
				edit.commit();*/
			}
		
	}
}
