package com.example.survey_game.Game;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Score {
	 private float x;

	 public static boolean score = false;
     private float y;

     private Bitmap bmp;

     private int life = 15;

     private List<Score> temps;

	private GameView gameView;

	private int ySpeed;



     public  Score(List<Score> temps, GameView gameView, float x,

                  float y, Bitmap bmp) {

           this.x = Math.min(Math.max(x - bmp.getWidth() / 2, 0),

                         gameView.getWidth() - bmp.getWidth());

           score = false;
           this.y = Math.min(Math.max(y - bmp.getHeight() / 2, 0),

                         gameView.getHeight() - bmp.getHeight());
           this.gameView = gameView;

           this.bmp = bmp;

           ySpeed = -2;
           this.temps = temps;

     }



     public void onDraw(Canvas canvas) {

           update();

           canvas.drawBitmap(bmp, x, y, null);

     }



     private void update() {

           if (--life < 1) {

        	   score = true;
                  temps.remove(this);

           }else{
        	   if (y >= gameView.getHeight() - bmp.getHeight() - ySpeed || y + ySpeed <= 0) {

       			ySpeed = -ySpeed;
       		}

        	   score = false;
       		y = y + ySpeed;
           }

     }
}
