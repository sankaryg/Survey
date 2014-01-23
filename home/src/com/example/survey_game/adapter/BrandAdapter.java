package com.example.survey_game.adapter;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.example.survey_game.DBfunction;
import com.example.survey_game.R;
import com.example.survey_game.dbrand;
import com.example.survey_game.Game.GameActivity;
import com.example.survey_game.adapter.ProductAdapter.ViewHolder;
import com.example.survey_game.dbrand.Load;
import com.example.survey_game.entity.Product;
import com.example.survey_game_fuction.Brand;
import com.example.survey_game_fuction.Service.Brands;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BrandAdapter extends ArrayAdapter<Brands>{

	private Activity context;
	private int layout;
	private List<Brand> optionList;
	private HashMap<String, String> images;
	private int play;
	private String bid;
	private SharedPreferences preference;
	DBfunction db;
	private int uploadLimit;
	private int br;
	private List<Brand> brands;
	private int noOfButton;

	public BrandAdapter(Context context, int textViewResourceId, List<Brands> list) {
		super(context, textViewResourceId);
		// TODO Auto-generated constructor stub
		this.context = (Activity) context;
		layout = textViewResourceId;
		//this.optionList = list;

	}

	public BrandAdapter(Context context, int textViewResourceId, List<Brand> list,
			HashMap<String, String> storeImage, int play, String bid) {
		super(context, textViewResourceId);
		this.context = (Activity) context;
		layout = textViewResourceId;
		this.optionList = list;
		this.images = storeImage;
		
		this.play = play;
		this.bid = bid;
		db = new DBfunction(context);
		preference = context.getSharedPreferences("Survey", Context.MODE_PRIVATE);
		
		/*for(int i=0;i<list.size();i++){
			preference.edit().putString(list.get(i).getBrand_image_path(), (preference.getString(optionList.get(i).getBrand_image_path(),null))).commit();
			list.get(i).setBrand_image_path((preference.getString(optionList.get(i).getBrand_image_path(),null)));
			db.updateBrand(list.get(i), list.get(i).getBrandStatus());
			
			//Log.d("abc", list.get(i).getBrand_image_path()+"_"+preference.getString(optionList.get(i).getBrand_image_path(),null));
		}*/
		play = preference.getInt("playMode", 0);
		uploadLimit = preference.getInt("upload", 0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return optionList.size();
	}

	static class ViewHolder {

		TextView productName;
		ImageView productImage;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = context.getLayoutInflater().inflate(layout, null);

			holder.productName = (TextView) convertView
					.findViewById(R.id.thumbtextView);

			holder.productImage = (ImageView) convertView
					.findViewById(R.id.thumbimageView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		if(play== 2 && bid != null){
			Editor edit = preference.edit();
			edit.putInt("playMode", 0);
			edit.commit();
			Log.d("abc", bid+"+"+optionList.get(position).getBrandId()+"+"+bid.equals(String.valueOf(optionList.get(position).getBrandId())));
			if(bid.equals(String.valueOf(optionList.get(position).getBrandId()))){
				holder.productImage.setEnabled(false);
				holder.productImage.setClickable(false);
				holder.productName.setTextColor(Color.WHITE);
				holder.productName.setBackgroundColor(Color.BLACK);
				optionList.get(position).setBrandStatus("true");
				db.updateBrand(optionList.get(position),"true");
			}
		}
		if(optionList.get(position).getBrandStatus().equals("true")){
			holder.productImage.setEnabled(false);
			holder.productImage.setClickable(false);
			holder.productName.setTextColor(Color.WHITE);
			holder.productName.setBackgroundColor(Color.BLACK);
			
		}else{
			
			holder.productImage.setClickable(true);
			holder.productName.setTextColor(Color.BLACK);
			holder.productName.setBackgroundColor(Color.GRAY);
		}
		holder.productImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(optionList.get(position).getBrandStatus().equals("false")){
				Intent i=new Intent(context,GameActivity.class);
				i.putExtra("bname", optionList.get(position).getBrandName()+"_"+ optionList.get(position).getBrandId());
				i.putExtra("brandid", optionList.get(position).getBrandId());
				context.startActivity(i);
				context.finish();
				}
			}
		});
		holder.productName.setText(optionList.get(position).getBrandName());
		String path = android.os.Environment.getExternalStorageDirectory()+"/img_"+preference.getString("pid","1");//+optionList.get(position).getBrandName()+".png";//optionList.get(position).getBrand_image_path();//preference.getString(optionList.get(position).getBrand_image_path(),null);
		if(path!=null){
			File file = new File(android.os.Environment.getExternalStorageDirectory()+"/img_"+preference.getString("pid","1"),optionList.get(position).getBrandName()+".png");

//		File file = new File(path);
		if(file.exists())
		holder.productImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
		}

		br = db.getNoOfBrandPlayed(preference.getString("product_id", "1"));
		brands = db.retriveBrand(preference.getString("product_id", "1"));
		noOfButton = brands.size();//db.getNoOfRows();
		if(noOfButton == br){
			
				//preference.edit().putBoolean("abc", true).commit();
			//((dbrand)context).showAll();
			
		}
	//else{
		//alert.showAlertDialog(dbrand.this, "SurveyGame", "Check Your network "+br +"_"+noOfButton, true);
	
		return convertView;
	}

}
