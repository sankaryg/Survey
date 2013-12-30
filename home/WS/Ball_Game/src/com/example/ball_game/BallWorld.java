package com.example.ball_game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * The control logic and main display panel for game.
 * 
 * @author Hock-Chuan Chua
 * @version October 2010
 */
public class BallWorld extends SurfaceView {
	private static final int UPDATE_RATE = 30; // Frames per second (fps)
	private static final float EPSILON_TIME = 1e-2f; // Threshold for zero time

	// Balls
	private static final int MAX_BALLS = 25; // Max number allowed
	private int currentNumBalls; // Number currently active
	private Ball[] balls = new Ball[MAX_BALLS];

	private ContainerBox box; // The container rectangular box
	// private DrawCanvas canvas; // The Custom canvas for drawing the box/ball
	private int canvasWidth;
	private int canvasHeight;

	// private ControlPanel control; // The control panel of buttons and
	// sliders.
	private boolean paused = false; // Flag for pause/resume control
	private Paint mTextPen;
	private BallWorld gameview;
	private com.example.ball_game.GameThread gameLoopThread;
	private int width;
	private int height;
	private SurfaceHolder holder;
	private Bitmap bg;
	/**
	 * Constructor to create the UI components and init the game objects. Set
	 * the drawing canvas to fill the screen (given its width and height).
	 * 
	 * @param width
	 *            : screen width
	 * @param height
	 *            : screen height
	 */
	public BallWorld(Context context, int width, int height) {
		super(context);
		final int controlHeight = 30;
		canvasWidth = width;
		canvasHeight = height - controlHeight; // Leave space for the control
		 mTextPen = new Paint();
         mTextPen.setColor(Color.RED);
         mTextPen.setTextSize(14);
         gameLoopThread = new GameThread(this);
         gameview = this;
         this.width = width;
         
         this.height = height;
         bg = BitmapFactory.decodeResource(getResources(), R.drawable.heart);					// panel
		currentNumBalls = 11;
		balls[0] = new Ball(100, 210, 30,bg, 5, 34, Color.YELLOW);
		balls[1] = new Ball(80, 150, 30,bg, 2, -114, Color.YELLOW);
		balls[2] = new Ball(230, 200, 30,bg, 10, 14, Color.GREEN);
		balls[3] = new Ball(200, 200, 30,bg, 10, 14, Color.GREEN);
		balls[4] = new Ball(200, 50, 30,bg, 10, -47, Color.BLUE);
		balls[5] = new Ball(280, 320, 30,bg, 5, 47, Color.BLUE);
		balls[6] = new Ball(80, 150, 30, bg,5, -114, Color.CYAN);
		balls[7] = new Ball(100, 240, 30,bg, 5, 60, Color.CYAN);
		balls[8] = new Ball(250, 280, 30,bg, 5, -42, Color.BLUE);
		balls[9] = new Ball(200, 80, 30, bg,10, -84, Color.CYAN);
		balls[10] = new Ball(300, 170, 30, bg,20, -42, Color.MAGENTA);

		// The rest of the balls, that can be launched using the launch button
		for (int i = currentNumBalls; i < MAX_BALLS; i++) {
			// Allocate the balls, but position later before the launch
			balls[i] = new Ball(20, canvasHeight - 20, 15,bg, 5, 45, Color.RED);
		}

		// Init the Container Box to fill the screen
		/*box = new ContainerBox(0, 0, canvasWidth, canvasHeight, Color.BLACK,
				Color.WHITE);
*/
		// Init the custom drawing panel for drawing the game
		// canvas = new DrawCanvas();

		// Init the control panel
		// control = new ControlPanel();
//		box.set(0, 0, canvasWidth, canvasHeight);
		// Layout the drawing panel and control panel
		// Start the ball bouncing
		holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
                 @Override
                 public void surfaceDestroyed(SurfaceHolder holder) {
                	 stop();
                 }
                 @Override
                 public void surfaceCreated(SurfaceHolder holder) {
                	    gameLoopThread.setRunning(true);
                        gameLoopThread.start();
                        
                 }
                 @Override
                 public void surfaceChanged(SurfaceHolder holder, int format,

                               int width, int height) {

                 }

          });
		//gameStart();
	}
	public void stop(){
        boolean retry = true;
        
        gameLoopThread.setRunning(false);
        while (retry) {

               try {

                     gameLoopThread.join();

                     retry = false;

               } catch (InterruptedException e) {

               }

        }

    }
	/** Start the ball bouncing. */
	public void gameStart() {
		// Run the game logic in its own thread.
		Thread gameThread = new Thread() {
			public void run() {
				while (true) {
					long beginTimeMillis, timeTakenMillis, timeLeftMillis;
					beginTimeMillis = System.currentTimeMillis();

					if (!paused) {
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
				}
			}
		};
		gameThread.start(); // Invoke GaemThread.run()
	}

	/**
	 * One game time-step. Update the game objects, with proper collision
	 * detection and response.
	 */
	public void gameUpdate() {
		float timeLeft = 2.0f; // One time-step to begin with

		// Repeat until the one time-step is up
		do {
			// Find the earliest collision up to timeLeft among all objects
			float tMin = timeLeft;

			// Check collision between two balls
			for (int i = 0; i < currentNumBalls; i++) {
				for (int j = 0; j < currentNumBalls; j++) {
					if (i < j) {
						balls[i].intersect(balls[j], tMin);
						if (balls[i].earliestCollisionResponse.t < tMin) {
							tMin = balls[i].earliestCollisionResponse.t;
						}
					}
				}
			}
			// Check collision between the balls and the box
			for (int i = 0; i < currentNumBalls; i++) {
				balls[i].intersect(gameview, tMin);
				if (balls[i].earliestCollisionResponse.t < tMin) {
					tMin = balls[i].earliestCollisionResponse.t;
				}
			}

			// Update all the balls up to the detected earliest collision time
			// tMin,
			// or timeLeft if there is no collision.
			for (int i = 0; i < currentNumBalls; i++) {
				balls[i].update(tMin);
			}

			timeLeft -= tMin; // Subtract the time consumed and repeat
		} while (timeLeft > EPSILON_TIME); // Ignore remaining time less than
											// threshold
	}

	@Override
	protected void onDraw(Canvas g) {
		// TODO Auto-generated method stub
		super.onDraw(g);
		//box.draw(g);
		g.drawColor(Color.BLACK);
		for (int i = 0; i < currentNumBalls; i++) {
			balls[i].draw(g);
		}
		// Display balls' information
		//g.drawColor(Color.WHITE);
		// g.setFont(new Font("Courier New", Font.PLAIN, 12));
		/*for (int i = 0; i < currentNumBalls; i++) {
			g.drawText("Ball " + (i + 1) + " " + balls[i].toString(), 20,
					30 + i * 20, mTextPen);
		}*/
	}

	/*
   *//** The control panel (inner class). */
	/*
	 * class ControlPanel extends JPanel {
	 *//** Constructor to initialize UI components of the controls */
	/*
	 * public ControlPanel() { // A checkbox to toggle pause/resume movement
	 * JCheckBox pauseControl = new JCheckBox(); this.add(new JLabel("Pause"));
	 * this.add(pauseControl); pauseControl.addItemListener(new ItemListener() {
	 * 
	 * @Override public void itemStateChanged(ItemEvent e) { paused = !paused;
	 * // Toggle pause/resume flag transferFocusUpCycle(); // To handle key
	 * events } });
	 * 
	 * // A slider for adjusting the speed of all the balls by a factor final
	 * float[] ballSavedSpeedXs = new float[MAX_BALLS]; final float[]
	 * ballSavedSpeedYs = new float[MAX_BALLS]; for (int i = 0; i <
	 * currentNumBalls; i++) { ballSavedSpeedXs[i] = balls[i].speedX;
	 * ballSavedSpeedYs[i] = balls[i].speedY; } int minFactor = 5; // percent
	 * int maxFactor = 200; // percent JSlider speedControl = new
	 * JSlider(JSlider.HORIZONTAL, minFactor, maxFactor, 100); this.add(new
	 * JLabel("Speed")); this.add(speedControl);
	 * speedControl.addChangeListener(new ChangeListener() {
	 * 
	 * @Override public void stateChanged(ChangeEvent e) { JSlider source =
	 * (JSlider)e.getSource(); if (!source.getValueIsAdjusting()) { int
	 * percentage = (int)source.getValue(); for (int i = 0; i < currentNumBalls;
	 * i++) { balls[i].speedX = ballSavedSpeedXs[i] * percentage / 100.0f;
	 * balls[i].speedY = ballSavedSpeedYs[i] * percentage / 100.0f; } }
	 * transferFocusUpCycle(); // To handle key events } });
	 * 
	 * // A button for launching the remaining balls final JButton launchControl
	 * = new JButton("Launch New Ball"); this.add(launchControl);
	 * launchControl.addActionListener(new ActionListener() {
	 * 
	 * @Override public void actionPerformed(ActionEvent e) { if
	 * (currentNumBalls < MAX_BALLS) { balls[currentNumBalls].x = 20;
	 * balls[currentNumBalls].y = canvasHeight - 20; currentNumBalls++; if
	 * (currentNumBalls == MAX_BALLS) { // Disable the button, as there is no
	 * more ball launchControl.setEnabled(false); } } transferFocusUpCycle(); //
	 * To handle key events } }); } }
	 */
	}
