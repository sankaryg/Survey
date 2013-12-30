package com.example.ball_game;

import android.graphics.Canvas;
import android.util.Log;
public class GameThread extends Thread {

    static final long FPS = 10;

    private BallWorld view;

    private boolean running = false;

   

    public GameThread(BallWorld view) {

          this.view = view;

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

				/*long beginTimeMillis, timeTakenMillis, timeLeftMillis;
				beginTimeMillis = System.currentTimeMillis();

				Canvas c = null;

                startTime = System.currentTimeMillis();
				if (!BallWorld.paused) {
					// Execute one game step
					gameUpdate();
					// Refresh the display
					invalidate();
				}

				// Provide the necessary delay to meet the target rate
				timeTakenMillis = System.currentTimeMillis()
						- beginTimeMillis;
				timeLeftMillis = 1000L / UPDATE_RATE - timeTakenMillis;
				if (timeLeftMillis < 5)
					timeLeftMillis = 5; // Set a minimum

				// Delay and give other thread a chance
				try {
					Thread.sleep(timeLeftMillis);
				} catch (InterruptedException ex) {
				}
*/			
                 Canvas c = null;

                 startTime = System.currentTimeMillis();

				long beginTimeMillis, timeTakenMillis, timeLeftMillis;
				beginTimeMillis = System.currentTimeMillis();

                 try {

                        c = view.getHolder().lockCanvas();

                        synchronized (view.getHolder()) {
                        		view.gameUpdate();
                               view.onDraw(c);
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