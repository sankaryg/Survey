package com.example.survey_game.WebService;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.example.survey_game_fuction.Upload;

public class UserFunction {

	private static String Home_Url = "http://annanagaronline.com/survey/webservice.php";//http://eyeopentechnologies.com/android/webservice.php";
	private JSONParser jsonParser;
	
	public UserFunction() {
		// TODO Auto-generated constructor stub
		jsonParser = new JSONParser();
	}
	public JSONObject checkUpdate(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("task", "checkupdate"));
		return jsonParser.getJSONFromUrl(Home_Url, params);
	}
	public JSONObject userRegistration(String uname,String age,String gender, String productid){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("task", "register"));
		params.add(new BasicNameValuePair("uname", uname));
		params.add(new BasicNameValuePair("age", age));
		params.add(new BasicNameValuePair("gender", gender));
		params.add(new BasicNameValuePair("productid", productid));
		return jsonParser.getJSONFromUrl(Home_Url, params);
	}
	public JSONObject userUpload(Upload upload, CharSequence charSequence, String icode){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("task", "upload"));
		params.add(new BasicNameValuePair("product", upload.getName()));
		params.add(new BasicNameValuePair("player", upload.getAge()));
		/*params.add(new BasicNameValuePair("gender", upload.getGender()));
		*/
		params.add(new BasicNameValuePair("brand", upload.getBrandId()));
		params.add(new BasicNameValuePair("feature", upload.getFeature()));
		params.add(new BasicNameValuePair("time", charSequence.toString()));
		params.add(new BasicNameValuePair("icode", icode));
		return jsonParser.getJSONFromUrl(Home_Url, params);
	}

	public JSONObject userProduct(String string) {
		// TODO Auto-generated method stub
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("task", string));
		return jsonParser.getJSONFromUrl(Home_Url, params);
	}
	public JSONObject userProductById(String product_id) {
		// TODO Auto-generated method stub
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("task", "productlist"));
		params.add(new BasicNameValuePair("productid", product_id));
		return jsonParser.getJSONFromUrl(Home_Url, params);
	}
}
