package com.example.survey_game.Game;

import android.graphics.Canvas;
import android.util.Log;
public class GameThread extends Thread {

    static final long FPS = 10;

    private GameView view;

    private boolean running = false;

	public static boolean paused;

   

    public GameThread(GameView view) {

          this.view = view;
          paused = false;

    }



    public void setRunning(boolean run) {

          running = run;
          Log.d("Update", "running thread "+running);

    }



    @Override

    public void run() {

          long ticksPS = 1000 / FPS;
          Log.d("Update", "run method"+isAlive());
          long startTime;

          long sleepTime;

          while (running) {

                /* Canvas c = null;

                 startTime = System.currentTimeMillis();

				long beginTimeMillis, timeTakenMillis, timeLeftMillis;
				beginTimeMillis = System.currentTimeMillis();

                 try {
                	 	if(view.getHolder().getSurface().isValid()){
                        c = view.getHolder().lockCanvas();

                        synchronized (view.getHolder()) {
                        		if(!paused){
									view.gameUpdate();
                               view.onDraw(c);
							   }
                               Log.d("Update", "running thread on draw called");

                        }
                	 	}
                 } finally {

                        if (c != null) {

                               view.getHolder().unlockCanvasAndPost(c);

                        }

                 }
              // Provide the necessary delay to meet the target rate
 				timeTakenMillis = System.currentTimeMillis()
 						- beginTimeMillis;
 				timeLeftMillis = 1000L / 30 - timeTakenMillis;
 				if (timeLeftMillis < 5)
 					timeLeftMillis = 5; // Set a minimum

                 //sleepTime = ticksPS-(System.currentTimeMillis() - startTime);

                 try {

                        if (timeLeftMillis > 0)

                               sleep(timeLeftMillis);

                        else

                               sleep(10);

                 } catch (Exception e) {}

          }

*/
        	  Canvas c = null;

              startTime = System.currentTimeMillis();

				long beginTimeMillis, timeTakenMillis, timeLeftMillis;
				beginTimeMillis = System.currentTimeMillis();

              try {

                     c = view.getHolder().lockCanvas();

                     synchronized (view.getHolder()) {
                    		if(!paused){
								view.gameUpdate();
								view.onDraw(c);
                           }	
                    		  
     						 
                            Log.d("Update", "running thread on draw called");

                     }

              } finally {

                     if (c != null) {

                            view.getHolder().unlockCanvasAndPost(c);

                     }

              }
           // Provide the necessary delay to meet the target rate
				timeTakenMillis = System.currentTimeMillis()
						- beginTimeMillis;
				timeLeftMillis = 1000L / 30 - timeTakenMillis;
				if (timeLeftMillis < 5)
					timeLeftMillis = 5; // Set a minimum

              //sleepTime = ticksPS-(System.currentTimeMillis() - startTime);

              try {

                     if (timeLeftMillis > 0)

                            sleep(timeLeftMillis);

                     else

                            sleep(10);

              } catch (Exception e) {}

       }
  
          }

}