package com.example.survey_game.video;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.SoundEffectConstants;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.survey_game.R;
import com.example.survey_game.dbrand;
import com.example.survey_game.WebService.ShowAlert;

public class VideoViewDemo extends Activity {

    /**
     * TODO: Set the path variable to a streaming video URL or a local media
     * file path.
     */
    private String path = "";
    private VideoView mVideoView;
	private ShowAlert showAlert;
	private ImageView skip;
	private SharedPreferences preference;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_tutorial);
        mVideoView = (VideoView) findViewById(R.id.videoView1);
        showAlert = new ShowAlert(this);
        skip=(ImageView)findViewById(R.id.skip);
        preference = getSharedPreferences("Survey", MODE_PRIVATE);
		
		skip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getBaseContext(),dbrand.class);
				//preference.edit().putBoolean("log", true).commit();
				
				startActivity(i);
				finish();
			}
		});
       
        if (path == "") {
             mVideoView.setMediaController(new MediaController(this));
            
            mVideoView.getHolder().addCallback(new Callback() {
				
				@Override
				public void surfaceDestroyed(SurfaceHolder holder) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void surfaceCreated(SurfaceHolder holder) {
					// TODO Auto-generated method stub
					mVideoView.setVideoURI(Uri.parse("android.resource://com.example.survey_game/"+R.raw.trailer13gp_old));
		            mVideoView.playSoundEffect(SoundEffectConstants.CLICK)	;	
		            mVideoView.setOnCompletionListener(new OnCompletionListener() {
						
						@Override
						public void onCompletion(MediaPlayer mp) {
							// TODO Auto-generated method stub
							Intent i=new Intent(getBaseContext(),dbrand.class);
							//preference.edit().putBoolean("log", true).commit();
							
							startActivity(i);
							finish();
						}
					});
		            mVideoView.setOnPreparedListener(new OnPreparedListener() {
						
						@Override
						public void onPrepared(MediaPlayer arg0) {
							// TODO Auto-generated method stub
							arg0.setDisplay(mVideoView.getHolder());
							int videoWidth = arg0.getVideoWidth();
							int videoHeight = arg0.getVideoHeight();
							float videoProportion = (float)videoWidth/(float)videoHeight;
							int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
							int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
							float screenProportion = (float)screenWidth/(float)screenHeight;
							LayoutParams lp = mVideoView.getLayoutParams();
							if(videoProportion>screenProportion){
								lp.width = screenWidth;
								lp.height = (int) ((float)screenWidth/videoProportion);
							}else{
								lp.width = (int) (videoProportion * (float)screenHeight);
								lp.height = screenHeight;
							}
							mVideoView.setLayoutParams(lp);
							mVideoView.start();
							}
					});
				}
				
				@Override
				public void surfaceChanged(SurfaceHolder holder, int format, int width,
						int height) {
					// TODO Auto-generated method stub
					
				}
			});
            mVideoView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            mVideoView.requestFocus();
                  } else {

           
            mVideoView.setVideoURI(Uri.parse("android.resource://com.video/"+R.raw.trailer13gp_old));
            mVideoView.setMediaController(new MediaController(this));
           
            mVideoView.requestFocus();

        }
    }
}
