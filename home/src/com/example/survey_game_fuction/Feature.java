package com.example.survey_game_fuction;



import java.io.Serializable;

public class Feature implements Serializable{

	private int branId;
	private int brand_id;
	private int noOfFeatures;
	private String feature;
	private boolean selected;
	public int getId() {
		return branId;
	}
	public Feature(int brandId, String brandName) {
		// TODO Auto-generated constructor stub
		this.branId = brandId;
		this.feature = brandName;
	}
	public Feature() {
		// TODO Auto-generated constructor stub
	}
	public void setId(int id) {
		this.branId = id;
	}
	public int getNoOfFeatures() {
		return noOfFeatures;
	}
	public void setNoOfFeatures(int noOfFeatures) {
		this.noOfFeatures = noOfFeatures;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public int getBrand_id() {
		return brand_id;
	}
	public void setBrand_id(int brand_id) {
		this.brand_id = brand_id;
	}
	
}
