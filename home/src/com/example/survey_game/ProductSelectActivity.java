package com.example.survey_game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.example.survey_game.WebService.ShowAlert;
import com.example.survey_game.WebService.UserFunction;
import com.example.survey_game.adapter.ProductAdapter;
import com.example.survey_game.entity.Product;
import com.example.survey_game_fuction.Brand;
import com.example.survey_game_fuction.CompFeature;
import com.example.survey_game_fuction.ConnectionDetector;
import com.example.survey_game_fuction.ContraFeature;

public class ProductSelectActivity extends Activity implements
		OnItemClickListener, IAsyncTask {

	private GridView grid;
	public ProgressDialog pd;
	ProductAdapter adapter;
	DBfunction db;
	List<Product> products;
	private SharedPreferences preference;
	private ShowAlert alert;
	private ConnectionDetector connection;
	private String productID;
	private int currentPro_id;
	private int product_id;
	private List<ContraFeature> featureList;
	private List<Brand> brandList;
	private List<CompFeature> contraList;
	private List<CompFeature> compList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_selct);
		grid = (GridView) findViewById(R.id.gridView1);
		grid.setOnItemClickListener(this);
		db = new DBfunction(this);
		alert = new ShowAlert(this);
		connection = new ConnectionDetector(this);
		preference = getSharedPreferences("Survey", MODE_PRIVATE);
		preference.edit().clear().commit();
		productID = preference.getString("product_id", null);

		products = db.retriveProducts();
		currentPro_id = products.size();
		/**/
		if (connection.isConnectingToInternet())
			new CheckUpdate().execute();
		// else{
		// alert.showAlertDialog(ProductSelectActivity.this, "Survey Game",
		// "No network", true);
		else if (products.size() <= 0)
			if (!connection.isConnectingToInternet()) {
				alert.showAlertDialog(ProductSelectActivity.this,
						"Survey Game", "No network", true,"finish");

			} else
				new InsertData().execute();
		else
			callAdapter();
		// }
	}

	class CheckUpdate extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(ProductSelectActivity.this);
			pd.setMessage("Loading...");
			pd.setMax(100);
			pd.setCancelable(false);
			pd.setIndeterminate(false);
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			// TODO Auto-generated method stub
			UserFunction user = new UserFunction();
			JSONObject jboj = null;
			String str = null;
			try {
				jboj = user.checkUpdate();

			} catch (Exception e) {
				str = null;
			}
			return jboj;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pd != null && pd.isShowing())
				pd.dismiss();
			if (result != null) {
				try {
					String str = result.getString("message");
					if (str.equals("updates found")) {
						db.resetTables(MySQLiteHelper.PRODUCT_TABLE);
						db.resetTables(MySQLiteHelper.BRAND_TABLE);
						db.resetTables(MySQLiteHelper.FEATURE_TABLE);
						db.resetTables(MySQLiteHelper.CONTRA_TYPE_TABLE);
						db.resetTables(MySQLiteHelper.COMP_TYPE_TABLE);
						File folder = Environment.getExternalStorageDirectory();
						String fileName = folder.getPath() + "img_"
								+ preference.getString("pid", "1");
						File myFile = new File(fileName);
						recursiveDelete(myFile);

						if (connection.isConnectingToInternet())
							new InsertData().execute();
						else
							alert.showAlertDialog(ProductSelectActivity.this,
									"Survey Game", "No network", true,"finish");

					} else

					if (products.size() <= 0)
						if (!connection.isConnectingToInternet()) {
							alert.showAlertDialog(ProductSelectActivity.this,
									"Survey Game", "No network", true,"finish");

						} else
							new InsertData().execute();
					else
						callAdapter();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private void callAdapter() {
		// TODO Auto-generated method stub

		adapter = new ProductAdapter(ProductSelectActivity.this,
				R.layout.grid_item, products);
		grid.setAdapter(adapter);
		//
		product_id = 0;
		if (products.size() > 0) {
			for (Product pro : products) {
				featureList = db.retriveContraFeature(products.get(product_id)
						.getProduct_id());
				brandList = db.retriveBrand(products.get(product_id)
						.getProduct_id());
				contraList = db.retriveCompFeature(products.get(product_id)
						.getProduct_id());
				compList = db.retriveCompFeature(products.get(product_id)
						.getProduct_id());
				if (!(featureList.size() > 0 || brandList.size() > 0
						|| contraList.size() > 0 || compList.size() > 0)) {
					CommonTask task = new CommonTask(
							ProductSelectActivity.this, products
									.get(product_id).getProduct_id());
					task.execute();
					break;
				} else {
					if (product_id < products.size() - 1)
						++product_id;
				}
			}
		}

		// }
	}

	class InsertData extends AsyncTask<Void, Void, JSONObject> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(ProductSelectActivity.this);
			pd.setMessage("Loading...");
			pd.setMax(100);
			pd.setCancelable(false);
			pd.setIndeterminate(false);
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			// TODO Auto-generated method stub
			UserFunction user = new UserFunction();
			JSONObject jboj = null;
			String str = null;
			try {
				jboj = user.userProduct("product");

			} catch (Exception e) {
				str = null;
			}
			return jboj;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pd != null && pd.isShowing())
				pd.dismiss();
			if (result != null) {
				try {
					db.resetTables(MySQLiteHelper.PRODUCT_TABLE);
					JSONArray product_name = result
							.getJSONArray("product_name");
					JSONArray product_id = result.getJSONArray("product_id");
					products = new ArrayList<Product>();
					for (int i = 0; i < product_name.length(); i++) {
						Product pro = new Product();
						pro.setProduct_id((String) product_id.get(i));
						pro.setProduct_name((String) product_name.get(i));
						if (i == 0)
							pro.setProduct_image(R.drawable.mobile);
						else if (i == 1)
							pro.setProduct_image(R.drawable.laptop);
						else

							pro.setProduct_image(R.drawable.ic_launcher);

						products.add(pro);
						db.insertProduct(pro);
					}
					if (products.size() > 0) {
						callAdapter();
						currentPro_id = products.size();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else
				alert.showAlertDialog(ProductSelectActivity.this,
						"Survey Game", "Unable to connect server", true,"finish");
			
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		// db.resetTables(MySQLiteHelper.TABLE_NAME);
		/*
		 * db.resetTables(MySQLiteHelper.BRAND_TABLE);
		 * db.resetTables(MySQLiteHelper.FEATURE_TABLE);
		 * db.resetTables(MySQLiteHelper.CONTRA_TYPE_TABLE);
		 * db.resetTables(MySQLiteHelper.COMP_TYPE_TABLE);
		 * db.resetTables(MySQLiteHelper.server_table);
		 */
		Editor edit = preference.edit();
		edit.clear().commit();
		// edit.putString("activity", "dbrand");
		if (productID == null) {
			edit.putString("product_id", products.get(arg2).getProduct_id());
			edit.putInt("download", 0);
		} else if (!productID.equals(products.get(arg2).getProduct_id())) {
			edit.putString("product_id", products.get(arg2).getProduct_id());
			edit.putInt("download", 1);
		}
		edit.commit();
		/* will going to use check update screen */
		/*
		 * File folder = Environment.getExternalStorageDirectory(); String
		 * fileName = folder.getPath()+"img_"+preference.getString("pid","1");
		 * File myFile = new File(fileName); recursiveDelete(myFile);
		 */
		/*
		 * if(myFile.exists()) myFile.delete();
		 */
		Intent i = new Intent(getBaseContext(), loginnew.class);
		com.example.survey_game.Util.Constants.productID = products.get(arg2)
				.getProduct_id();
		i.putExtra("pid", products.get(arg2).getProduct_id());
		startActivity(i);
		finish();
	}

	private void recursiveDelete(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				recursiveDelete(child);

		fileOrDirectory.delete();
	}

	@Override
	public void loadCompleted() {
		// TODO Auto-generated method stub
		if (product_id < products.size() - 1) {
			++product_id;
			CommonTask task = new CommonTask(ProductSelectActivity.this,
					products.get(product_id).getProduct_id());
			task.execute();
		} else {

		}
		// Toast.makeText(ProductSelectActivity.this, "product load finished",
		// Toast.LENGTH_SHORT).show();
	}
}
