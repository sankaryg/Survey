package com.example.survey_game_fuction;



import java.io.Serializable;
import java.util.List;


public class Brand implements Serializable{
	private int id;
	private int brandId;
	private String brandName;
	private String brand_image_path;
	private List<Feature> brandFeature;
	public Brand(int brandId, String brandName, String string) {
		// TODO Auto-generated constructor stub
		this.brandId = brandId;
		this.brandName = brandName;
		this.brand_image_path = string;
	}
	public Brand() {
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBrandId() {
		return brandId;
	}
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public List<Feature> getBrandFeature() {
		return brandFeature;
	}
	public void setBrandFeature(List<Feature> brandFeature) {
		this.brandFeature = brandFeature;
	}
	private String brandStatus;
	public void setBrandStatus(String string) {
		// TODO Auto-generated method stub
		this.brandStatus = string;
	}
	public String getBrandStatus() {
		return brandStatus;
	}
	public String getBrand_image_path() {
		return brand_image_path;
	}
	public void setBrand_image_path(String brand_image_path) {
		this.brand_image_path = brand_image_path;
	}
}
