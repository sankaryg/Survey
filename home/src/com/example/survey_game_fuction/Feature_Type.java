package com.example.survey_game_fuction;



import java.io.Serializable;

public class Feature_Type implements Serializable {
	private int branId;
	private int noOfFeatures;
	private String feature;
	private String type;
	public int getBranId() {
		return branId;
	}
	public void setBranId(int branId) {
		this.branId = branId;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
