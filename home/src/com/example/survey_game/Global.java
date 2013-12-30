package com.example.survey_game;

import java.util.List;

import android.app.Application;

import com.example.survey_game_fuction.Feature;
import com.example.survey_game_fuction.Login;
import com.example.survey_game_fuction.ServerData;
import com.example.survey_game_fuction.Service;

public class Global extends Application{

	private Service service;
	private Login login;
	private int count,scorenumber,ballcount;
	private long millisremaining;
	private List<Feature> featureList;
	private List<ServerData> list;
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getScorenumber() {
		return scorenumber;
	}
	public void setScorenumber(int scorenumber) {
		this.scorenumber = scorenumber;
	}
	public int getBallcount() {
		return ballcount;
	}
	public void setBallcount(int ballcount) {
		this.ballcount = ballcount;
	}
	public long getMillisremaining() {
		return millisremaining;
	}
	public void setMillisremaining(long millisremaining) {
		this.millisremaining = millisremaining;
	}
	public List<Feature> getFeatureList() {
		return featureList;
	}
	public void setFeatureList(List<Feature> featureList) {
		this.featureList = featureList;
	}
	public List<ServerData> getList() {
		return list;
	}
	public void setList(List<ServerData> list) {
		this.list = list;
	}
	
}
