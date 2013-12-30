package com.example.survey_game;






import com.example.survey_game.Game.GameView;
import com.example.survey_game_fuction.Brand;
import com.example.survey_game_fuction.Login;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class playscreen  extends Activity{
ImageView sound;
TextView name,brand;
Global global;
ToggleButton tb;
private int brand_name;
private GameView game;
	public void onCreate(Bundle b)
	{
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
		setContentView(R.layout.activity_playscreen);
		tb=(ToggleButton)findViewById(R.id.toggle);
		name=(TextView)findViewById(R.id.textView1);
		
		brand=(TextView)findViewById(R.id.textView2);
		Bundle bundle = getIntent().getExtras();
		if(bundle!= null){
			brand_name =  (Integer) bundle.get("bname");
		}
		global = (Global)getApplicationContext();
		String Player_name=(global.getLogin()!= null)?global.getLogin().getName():"";
		name.setText(Player_name);
		Log.d("check", brand_name+"_");
		String b1 =(global.getService()!= null)?global.getService().getBrand().get(brand_name-1).getBrand_name():"";
		brand.setText(b1);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Log.d(ACTIVITY_SERVICE, width+"_"+height);
       
       
	    tb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("onToggleClicked", "ToggleClick Event Started");
	            //an AudioManager object, to change the volume settings  
	            AudioManager amanager; 
	            amanager = (AudioManager)getSystemService(AUDIO_SERVICE);

		            // Is the toggle on?
	            boolean on = ((ToggleButton)v).isChecked();

	            if (on) {
	                Log.d("onToggleIsChecked", "ToggleClick Is On");
	               
	                amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
	                Log.d("STREAM_MUSIC", "Set to true");

	            } else {
	                Log.d("onToggleIsChecked", "ToggleClick Is Off");

	               
	                amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
	                Log.d("STREAM_MUSIC", "Set to False");
	            }
	            Log.d("onToggleClicked", "ToggleClick Event Ended");
			}
		});
	}
	
	public boolean onCreateOptionsMenu(Menu m)
	{
		super.onCreateOptionsMenu(m);
		CreateMenu(m);
		return true;
	}

	private void CreateMenu(Menu m) {
		// TODO Auto-generated method stub
		MenuItem m1=m.add(0,0,0,"back");
		{
		//m1.setIcon(R.drawable.about_buzz);	
		}
		MenuItem m2=m.add(0,1,1,"next");
	}

	public boolean onOptionsItemSelected(MenuItem mi){
		switch(mi.getItemId()){
		case 0:
			Intent i= new Intent(getBaseContext(),dbrand.class);
			startActivity(i);
			
			return true;
		case 1:
			//Toast.makeText(this,"Now you Pressing the setting menu...!", Toast.LENGTH_LONG).show();
		Intent in= new Intent(getBaseContext(),score.class);
		startActivity(in);
		
			return true;
		}
		return true;
		
	}

	private boolean MenuChoice(MenuItem mi)
	{
		return true;
	}
	
}