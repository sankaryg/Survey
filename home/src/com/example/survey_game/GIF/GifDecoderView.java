package com.example.survey_game.GIF;

import java.io.InputStream;

import com.example.survey_game.Util.Constants;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

public class GifDecoderView extends ImageView {

    private boolean mIsPlayingGif = false;

    private GifDecoder mGifDecoder;

    private Bitmap mTmpBitmap;

    final Handler mHandler = new Handler();

    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            if (mTmpBitmap != null && !mTmpBitmap.isRecycled()) {
                GifDecoderView.this.setImageBitmap(mTmpBitmap);
            }
        }
    };

	private String str;

    public void setAsset(String str){
    	this.str = str;
    }
    
    public GifDecoderView(Context context,AttributeSet attr) {
		// TODO Auto-generated constructor stub
    	super(context,attr);
    	InputStream stream = null;
        try {
        	if(Constants.threeBalloons)
        		str = "bonus_points.gif";
        	else
        		str = "piggy.gif";
            stream = context.getAssets().open(str);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        playGif(stream);
	}
    public GifDecoderView(Context context) {
        super(context);
        InputStream stream = null;
        try {
        	if(Constants.threeBalloons)
        		str = "bonus_points.gif";
        	else
        		str = "piggy.gif";
            stream = context.getAssets().open(str);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        playGif(stream);
    }

    private void playGif(InputStream stream) {
    	
        mGifDecoder = new GifDecoder();
        mGifDecoder.read(stream);

        mIsPlayingGif = true;

        new Thread(new Runnable() {
            public void run() {
                final int n = mGifDecoder.getFrameCount();
                final int ntimes = mGifDecoder.getLoopCount();
                int repetitionCounter = 0;
                do {
                    for (int i = 0; i < n; i++) {
                        mTmpBitmap = mGifDecoder.getFrame(i);
                        int t = mGifDecoder.getDelay(i);
                        mHandler.post(mUpdateResults);
                        try {
                            Thread.sleep(t);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(ntimes != 0) {
                        repetitionCounter ++;
                    }
                } while (mIsPlayingGif && (repetitionCounter <= ntimes));
            }
        }).start();
    }
    
    public void stopRendering() {
        mIsPlayingGif = true;
    }
}