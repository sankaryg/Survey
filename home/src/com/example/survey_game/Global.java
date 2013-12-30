package com.example.survey_game;

import android.app.Application;

import com.example.survey_game_fuction.Login;
import com.example.survey_game_fuction.Service;

public class Global extends Application{

	private Service service;
	private Login login;
	public Global() {
		// TODO Auto-generated constructor stub
	}
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	public Login getLogin() {
		return login;
	}
	public void setLogin(Login login) {
		this.login = login;
	}
	
}
