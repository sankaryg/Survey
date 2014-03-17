package com.example.survey_game;

import com.example.survey_game.Game.GameActivity;
import com.example.survey_game.Game.GameEnd;
import com.example.survey_game.Util.Constants;
import com.example.survey_game.WebService.ShowAlert;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver{

	private SharedPreferences preference;

	@Override
	public void onReceive(Context context, Intent arg1) {
		boolean flag = Intent.ACTION_BOOT_COMPLETED.equals(arg1.getAction());
		preference = context.getSharedPreferences("Survey", Context.MODE_PRIVATE);
		if(flag==true){
			//Toast.makeText(context, "Restarted", Toast.LENGTH_SHORT).show();
			preference.edit().putBoolean("pause", true).commit();
			preference.edit().putBoolean("boot", true).commit();
			if(Constants.context!=null){
			((Activity)(Constants.context)).finish();
			
			}
		}
		
	}
	
	

}
