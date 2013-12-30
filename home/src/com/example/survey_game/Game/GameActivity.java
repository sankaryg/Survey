package com.example.survey_game.Game;

import java.util.HashMap;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.survey_game.R;
import com.example.survey_game.GIF.GifDecoderView;
import com.example.survey_game.Util.Constants;
import com.example.survey_game.cup.MainGamePanel;

public class GameActivity extends Activity {

	private static final int SOUND_EFFECT_POP = 1;
	private GameView game;
	private HashMap<Integer, Integer> soundsMap;
	private SoundPool quizzlySoundPool;
	protected SoundManager mSoundManager;
	private String bname="";
	private boolean paused  = false;
	private int width;
	private int height;
	public static String bid;
	private TelephonyManager tmgr;
	Handler handler = null;//new Handler();

	private PhoneStateListener listener = new PhoneStateListener() {
		public void onCallStateChanged(int state, String incomingNumber) {
			if (state == TelephonyManager.CALL_STATE_RINGING) {
	            //Incoming call: Pause music
				onPause();
	        } else if(state == TelephonyManager.CALL_STATE_IDLE) {
	            //Not in call: Play music
	        	
	        } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
	            //A call is dialing, active or on hold
	        }
	        super.onCallStateChanged(state, incomingNumber);
		};
	};
	protected void initializeMedia() {
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mSoundManager = new SoundManager(this);
		mSoundManager.addSound(SOUND_EFFECT_POP, R.raw.balloonpop_3);
    }
	AlertDialog.Builder alert;
	private AlertDialog alertDialog;
	private SharedPreferences preference;
	private String uid;
	private String pid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        if(getIntent().getExtras()!=null){
        bname = getIntent().getExtras().getString("bname").split("_")[0];
        bid = getIntent().getExtras().getString("bname").split("_")[1];//getIntent().getExtras().getString("brandid");
        }
        preference = getSharedPreferences("Survey", Context.MODE_PRIVATE);
		uid = preference.getString("uid", null);
		pid = preference.getString("pid", null);
        Log.d("check", bid+"_"+bname+"+game");
		
initializeMedia();
         Log.d(ACTIVITY_SERVICE, width+"_"+height);
            
	}

public void gameEnd(){
		
		Intent inte = new Intent(getApplicationContext(), GameEnd.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		inte.putExtra("bid", bid);
		startActivity(inte);
		finish();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.d(ACTIVITY_SERVICE, "on start called");
		super.onStart();
		Log.d("abc_game", uid+"_"+pid);
		//
		
		game = new GameView(bid,bname,GameActivity.this,this,width,height,uid,pid);
        //setContentView(R.layout.activity_main_xml);
		setContentView(game);
        /*game.init(bid,bname,GameActivity.this,this,width,height,uid,pid);*/
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d("abc", "On Resume called");
		//bgmusic.resumeMusic();
		/*if(!paused){
			game.resume();
		}
 */
		super.onResume();
		if(alertDialog!=null){
			alertDialog.cancel();
			alertDialog = new AlertDialog.Builder(GameActivity.this).create();
			alertDialog.setMessage("Do u really want to close?");
			alertDialog.setTitle("EXIT");
	        // Setting alert dialog icon
	        //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
	 
	        // Setting OK Button
	        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					game.timerpause = false;
					//game.timer.setPaused(false);
					//game.timer.onFinish();
					//game.timer.cancel();
					game.stop();
					finish();
							
				}
			});
alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					//Toast.makeText(GameActivity.this, "cancel clicked", Toast.LENGTH_SHORT).show();	
					Log.d("Update", "cancel called");
					game.resume();
					paused = false;
				
				}
			});
	        // Showing Alert Message
	       alertDialog.show();
			
		}
	/*	game.resume();
		paused = false;
*/    tmgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
    if(tmgr!=null)
    	tmgr.listen(listener , PhoneStateListener.LISTEN_CALL_STATE);
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		/*if(tmgr != null) {
		    tmgr.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		}
			if(!paused)
		game.resume();*/
	}
@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		paused = false;
		game.pause();
		// game.timer.setPaused(false);
		//if (!Constants.threeBalloons) {
			if (!GameView.end) {
				alertDialog = new AlertDialog.Builder(GameActivity.this)
						.create();
				alertDialog.setMessage("Do u really want to close?");
				alertDialog.setTitle("EXIT");
				// Setting alert dialog icon
				// alertDialog.setIcon((status) ? R.drawable.success :
				// R.drawable.fail);

				// Setting OK Button
				alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								game.timerpause = false;
								// game.timer.setPaused(false);
								// game.timer.onFinish();
								// game.timer.cancel();
								game.stop();
								finish();

							}
						});
				alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								// Toast.makeText(GameActivity.this,
								// "cancel clicked", Toast.LENGTH_SHORT).show();
								Log.d("Update", "cancel called");
								game.resume();
								paused = false;

							}
						});
				// Showing Alert Message
				try {
					alertDialog.show();
				} catch (Exception e) {

				}

			}
			
		//}
/*else {
			//openDialog();
		}*/
		if(GameView.end){
			if(alertDialog!=null)
			alertDialog.cancel();
			game.stop();
		}
		Log.d("abc", "OnPause called"+GameView.end);
		super.onPause();
	}
	
@Override
public void onConfigurationChanged(Configuration newConfig) {
	// TODO Auto-generated method stub
	super.onConfigurationChanged(newConfig);
	paused = false;
	game.pause();
	if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
		if(game!= null){
			game.resume();
			paused = false;
		
		}
	}
	if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
		if(game!= null){
			game.resume();
			paused = false;
		
		}
	}
}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
     // TODO Auto-generated method stub
     //return super.onKeyDown(keyCode, event);
     if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME){
    	 /*paused = true;
    	game.pause();
    	AlertDialog.Builder alert = new AlertDialog.Builder(GameActivity.this);
		alert.setCancelable(false);
		alert.setMessage("Do u really want to close?");
		alert.setTitle("EXIT");
		
   	 	
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
		public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
				game.timerpause = false;
				//game.timer.setPaused(false);
				//game.timer.onFinish();
				//game.timer.cancel();
				game.stop();
				finish();
				
		}
		});
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(GameActivity.this, "cancel clicked", Toast.LENGTH_SHORT).show();	
					Log.d("Update", "cancel called");
					game.resume();
					paused = false;
				}
			});
     alert.create();
	 alert.show();*/
    onPause();
     return true; 
     }
     return super.onKeyDown(keyCode, event);
     }

	public static GifDecoderView coin_rotate;
	private Dialog dialog;
	private CountDownTimer timer;
	public void openDialog() {
		paused = false;
		game.timerPause();
		//dialog = null;
		//coin_rotate = null;
		dialog = new Dialog(GameActivity.this);
		// dialog.setTitle("Animation Dialog");
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialoglayout);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		coin_rotate = (GifDecoderView) findViewById(R.id.imageload);
		// coin_rotate.setAsset("bonus_points.gif");
		/*Button btnDismiss = (Button) dialog.getWindow().findViewById(
				R.id.button1);
		
		btnDismiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				game.resume();
				paused = false;
				Constants.threeBalloons = false;
				coin_rotate = null;
			}
		});
*/
		dialog.show();
		/*Constants.finishbonus = false;
		timer = new CountDownTimer(60000,1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				//Constants.finishbonus = true;
				timer.cancel();
				if(coin_rotate!=null){
				coin_rotate.stop();
				coin_rotate = null;
				}
				//handler.sendEmptyMessage(1);
				if (handler != null)
					handler.removeCallbacks(pushTask);
				game.resume();
				paused = false;
				Constants.threeBalloons = false;
				if (dialog != null)
					dialog.dismiss();
				dialog.hide();
			}
		};
		timer.start();*/
		if(handler == null)
			handler = new Handler();
		 if(handler!=null && pushTask!=null){
		 handler.removeCallbacks(pushTask); } 
		handler.postDelayed(pushTask, 5000);
		GameThread.paused = true; 
	}

	public void dismissdialog(){
		if (dialog != null)
			dialog.dismiss();
		game.timerResume();
		paused = false;
		Constants.threeBalloons = false;
		dialog = null;
	}
	private Runnable pushTask = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (dialog != null)
				dialog.dismiss();
			Constants.finishbonus = true;
			/*if(coin_rotate!=null)
			coin_rotate.cleanup();
			*/
			//if(coin_rotate!=null){
			
			//coin_rotate = null;
			//}
			//handler.sendEmptyMessage(1);
			if (handler != null)
				handler.removeCallbacks(pushTask);
			game.resume();
			paused = false;
			Constants.threeBalloons = false;
			dialog.cancel(); 
			GameThread.paused = true;
			//coin_rotate = null;
			//coin_rotate = null;
		}
	};

	public void cleanup(){
		 handler = null;
		 game = null;
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.d("abc", "on Stop called");
		super.onStop();
		if(tmgr != null) {
		    tmgr.listen(listener, PhoneStateListener.LISTEN_NONE);
		}
		if(game!=null)
			game.stop();
		
		 if(handler!=null && pushTask!=null){
		  handler.removeCallbacks(pushTask); }
		 

		// game.stop();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d("Update", "On Destroy called");
		
		super.onDestroy();
		if(game!=null)
			game.stop();
		mSoundManager.cleanup();
		unbindDrawables(game);
		cleanup();
		//System.gc();
	}
	
	private void unbindDrawables(View view) {
        System.gc();
        Runtime.getRuntime().gc();
        if (view.getBackground() != null) {
        view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
            unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
        ((ViewGroup) view).removeAllViews();
        }
    }
	protected void playPop() {

		mSoundManager.playSound(SOUND_EFFECT_POP);

	}

}
