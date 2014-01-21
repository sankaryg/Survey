package com.example.survey_game_fuction;



import java.io.Serializable;

public class Login implements Serializable{

	private  int id;
	private  String name;
	private  int age;
	private  String gender;
	private boolean status;
	private String product_id;
	private String db_user_id;
	
	public Login() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isStatus() {
		return status;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getDb_user_id() {
		return db_user_id;
	}

	public void setDb_user_id(String db_user_id) {
		this.db_user_id = db_user_id;
	}
	
	
}
