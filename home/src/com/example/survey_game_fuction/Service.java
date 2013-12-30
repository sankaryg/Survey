package com.example.survey_game_fuction;

import java.util.List;

public class Service {

	private String status;
	private String user_id;
	private String product_name;
	private String product_id;
	private List<Feature> feature;
	private List<Brands> brand;
	private compFeature compFeatures;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public List<Feature> getFeature() {
		return feature;
	}
	public void setFeature(List<Feature> feature) {
		this.feature = feature;
	}
	public List<Brands> getBrand() {
		return brand;
	}
	public void setBrand(List<Brands> brand) {
		this.brand = brand;
	}
	public compFeature getCompFeatures() {
		return compFeatures;
	}
	public void setCompFeatures(compFeature compFeatures) {
		this.compFeatures = compFeatures;
	}
	public static class Feature{
		private String feature_id;
		private String feature_name;
		public String getFeature_id() {
			return feature_id;
		}
		public void setFeature_id(String feature_id) {
			this.feature_id = feature_id;
		}
		public String getFeature_name() {
			return feature_name;
		}
		public void setFeature_name(String feature_name) {
			this.feature_name = feature_name;
		}
	}
	public static class Brands{
		private String brand_name;
		private String brand_id;
		private String brand_image_path;
		public String getBrand_name() {
			return brand_name;
		}
		public void setBrand_name(String brand_name) {
			this.brand_name = brand_name;
		}
		public String getBrand_id() {
			return brand_id;
		}
		public void setBrand_id(String brand_id) {
			this.brand_id = brand_id;
		}
		public String getBrand_image_path() {
			return brand_image_path;
		}
		public void setBrand_image_path(String brand_image_path) {
			this.brand_image_path = brand_image_path;
		}
	}
	public static class compFeature{
		private String feature_id;
		private List<Feature> compFeatureList;
		private List<Feature> contraFeatureList;
		public String getFeature_id() {
			return feature_id;
		}
		public void setFeature_id(String string) {
			this.feature_id = string;
		}
		public List<Feature> getCompFeatureList() {
			return compFeatureList;
		}
		public void setCompFeatureList(List<Feature> compFeatureList) {
			this.compFeatureList = compFeatureList;
		}
		public List<Feature> getContraFeatureList() {
			return contraFeatureList;
		}
		public void setContraFeatureList(List<Feature> contraFeatureList) {
			this.contraFeatureList = contraFeatureList;
		}
		
	}
	
}
