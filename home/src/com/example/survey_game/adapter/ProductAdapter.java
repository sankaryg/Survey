package com.example.survey_game.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.survey_game.R;
import com.example.survey_game.entity.Product;

public class ProductAdapter extends ArrayAdapter<Product> {

	private Activity context;
	private int layout;
	private List<Product> optionList;

	public ProductAdapter(Context context, int textViewResourceId, List<Product> list) {
		super(context, textViewResourceId);
		// TODO Auto-generated constructor stub
		this.context = (Activity) context;
		layout = textViewResourceId;
		this.optionList = list;

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
	public View getView(int position, View convertView, ViewGroup parent) {

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
		holder.productName.setTextColor(Color.BLACK);
		holder.productName.setBackgroundColor(Color.GRAY);
		holder.productName.setText(optionList.get(position).getProduct_name());
		if(optionList.get(position).getProduct_name().equals("mobile"))
		holder.productImage.setImageResource(R.drawable.mobile);
		else if(optionList.get(position).getProduct_name().equals("Laptop"))
			holder.productImage.setImageResource(R.drawable.laptop);
				return convertView;
	}

	
}