package com.example.survey_game_fuction;

public class CompFeature {

	private String feature_id;
	private String comp_feature_1;
	private String comp_feature_2;
	private String comp_feature_3;
	private String comp_feature_4;
	private String comp_feature_5;
	public CompFeature(String fid,String cid1,String cid2,String cid3,String cid4,String cid5) {
		// TODO Auto-generated constructor stub
		this.setFeature_id(fid);
		this.setContra_feature_1(cid1);
		this.setContra_feature_2(cid2);
		this.setContra_feature_3(cid3);
		this.setContra_feature_4(cid4);
		this.setContra_feature_5(cid5);
	}
	public CompFeature() {
		// TODO Auto-generated constructor stub
	}
	public void setContra_feature_5(String contra_feature_5) {
		this.comp_feature_5 = contra_feature_5;
	}
	public String getContra_feature_5() {
		return comp_feature_5;
	}
	public void setFeature_id(String feature_id) {
		this.feature_id = feature_id;
	}
	public String getFeature_id() {
		return feature_id;
	}
	public void setContra_feature_1(String contra_feature_1) {
		this.comp_feature_1 = contra_feature_1;
	}
	public String getContra_feature_1() {
		return comp_feature_1;
	}
	public void setContra_feature_2(String contra_feature_2) {
		this.comp_feature_2 = contra_feature_2;
	}
	public String getContra_feature_2() {
		return comp_feature_2;
	}
	public void setContra_feature_3(String contra_feature_3) {
		this.comp_feature_3 = contra_feature_3;
	}
	public String getContra_feature_3() {
		return comp_feature_3;
	}
	public void setContra_feature_4(String contra_feature_4) {
		this.comp_feature_4 = contra_feature_4;
	}
	public String getContra_feature_4() {
		return comp_feature_4;
	}
}
