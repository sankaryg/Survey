package com.example.survey_game.Game;

import com.example.survey_game.Util.Constants;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;


public class Enemy {


    
    private static final int BMP_ROWS = 1;

    private static int BMP_COLUMNS = 14;

    private int x = 0;

    private int y = 0;

    private int xSpeed = 5;

    private GameView gameView;

    private Bitmap bmp;

    public static int currentFrame = 0;

    private int width;

    private int height;

    private int ySpeed;

	private Rect src;

	private long framePeriod;

	private long frameTicker;

	private long frameTime = Constants.bonustime;

	private int limit = 25;
	private Paint paint;


    public Enemy(GameView gameView2, Bitmap add, float x2, float y2,
			 int i, int column) {
    	paint = new Paint();
    	BMP_COLUMNS = column;
    	
        this.width = add.getWidth() / BMP_COLUMNS;

        this.height = add.getHeight() / BMP_ROWS;

        this.gameView = gameView2;

        this.bmp = add;

        x = (int) x2;
        
        y = (int) y2;
        currentFrame = 1;
        framePeriod = 1000 / 5;
		frameTicker = 0l;

        src = new Rect(0,0,(int) width,height);
        paint.setAntiAlias(false);
        paint.setFilterBitmap(true);
        paint.setDither(true);
	}



	private void update() {

       
    	this.src.left = currentFrame * width;
		this.src.right = this.src.left+width;

    	if(frameTime == 0){
    
    		Constants.threeBalloons = false;
    		gameView.timerResume();
        	frameTime = Constants.bonustime;
        	GameView.ballcount = 0;
    	}else
    	{
    		frameTime = frameTime - 100;
    	}
    	
    	currentFrame = ++currentFrame % BMP_COLUMNS;
    	
    }

	


    public void onDraw(Canvas canvas) {
    	//int k;
         // update(System.currentTimeMillis()+5000);
    	  update();
         /* int srcX = currentFrame * width;
          if(currentFrame >0)
        	 k  =1;
          else
        	  k=0;
          int srcY = k*height;*/
          Rect dst = new Rect(x, y, x + width, y + height);
          canvas.drawBitmap(bmp, src, dst, paint);
          canvas.restore();

    }



}