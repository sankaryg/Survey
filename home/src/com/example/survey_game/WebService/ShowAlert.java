package com.example.survey_game.WebService;

import com.example.survey_game.dbrand;
import com.example.survey_game.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class ShowAlert {

	private Context context;
	
	public ShowAlert(Context context){
		this.context = context;
	}
	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message, Boolean status, final String string) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
 
        // Setting Dialog Title
        alertDialog.setTitle(title);
 
        // Setting Dialog Message
        alertDialog.setMessage(message);
 
        // Setting alert dialog icon
        //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
 
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            if(string!=null && string.equals("finish")){
            	Intent intent = new Intent(ShowAlert.this.context, home.class);
    			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			((Activity)(ShowAlert.this.context)).startActivity(intent);
            	((Activity)(ShowAlert.this.context)).finish();
            }
            }
        });
 
        // Showing Alert Message
       alertDialog.show();
    }
	public String getDeviceID(Context context){
		TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String id = telephonyManager.getDeviceId();
		return id;
	}
}