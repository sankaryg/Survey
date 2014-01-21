/**
 * 
 */
package com.example.survey_game.cup;

import java.lang.ref.WeakReference;

import com.example.survey_game.R;
import com.example.survey_game.Util.Constants;
import com.example.survey_game.cup.model.ElaineAnimated;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author impaler This is the main surface that handles the ontouch events and
 *         draws the image to the screen.
 */
public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();

	private MainThread thread;
	private ElaineAnimated elaine;

	private boolean active = false;
	// the fps to be displayed
	private String avgFps;
	Bitmap bg;

	private int id;

	public void setAvgFps(String avgFps) {
		this.avgFps = avgFps;
	}

	public MainGamePanel(Context context, AttributeSet attr) {
		// TODO Auto-generated constructor stub
		super(context, attr);
		init();
	}

	public MainGamePanel(Context context, AttributeSet attr, int arg2) {
		// TODO Auto-generated constructor stub
		super(context, attr, arg2);
		init();
	}

	public MainGamePanel(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		init();
	}

	private Bitmap imageView;

	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		private final WeakReference<Bitmap> imageViewReference;
		private int data = 0;

		public BitmapWorkerTask() {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imageViewReference = new WeakReference<Bitmap>(imageView);
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(Integer... params) {
			data = params[0];
			return decodeSampledBitmapFromResource(getResources(), data, 200,
					200);
		}

		// Once complete, see if ImageView is still around and set bitmap.
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (imageViewReference != null && bitmap != null) {
				Bitmap imageView1 = imageViewReference.get();
				if (imageView1 != null) {
					imageView = imageView1;
					elaine = new ElaineAnimated(imageView1, 0, 0 // initial
																	// position
							, 391, 400 // width and height of sprite
							, 5, 8);
					// imageView.setImageBitmap(bitmap);
				}
			}
		}

	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public void init() {
		getHolder().addCallback(this);

		// getHolder().setFormat(PixelFormat.TRANSPARENT);
		// create Elaine and load bitmap
		// if(!Constants.threeBalloons)
		if (Constants.endScreen.equals("rookie")) {
			id = R.drawable.rookie;
		} else if (Constants.endScreen.equals("pro")) {
			id = R.drawable.pro;
		} else if (Constants.endScreen.equals("expert"))
			id = R.drawable.expert;
		elaine = new ElaineAnimated(BitmapFactory.decodeResource(
				getResources(), id), 0, 0 // initial
				// position
				, 391, 400 // width and height of sprite
				, 5, 8);
		// new BitmapWorkerTask().execute(id);
		// FPS and number of frames in the animation
		/*
		 * else elaine = new ElaineAnimated(BitmapFactory.decodeResource(
		 * getResources(), R.drawable.bonus_seq), 0, 0 // initial // position ,
		 * 375, 300 // width and height of sprite , 5, 14); // FPS and number of
		 * frames in the animation
		 */// create the game loop thread
		thread = new MainThread(getHolder(), this);
		bg = BitmapFactory.decodeResource(getResources(),
				R.drawable.transparent);
		// make the GamePanel focusable so it can handle events
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop
		active = true;
		thread.setRunning(active);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// handle touch
		}
		return true;
	}

	public void render(Canvas canvas) {
		if (!Constants.finishbonus) {
			if (active) {
				if (canvas != null) {
					synchronized (canvas) {

						if (canvas != null) {
							// canvas.drawBitmap(bg, 0, 0, null);
							canvas.drawColor(Color.TRANSPARENT,
									PorterDuff.Mode.CLEAR);
							elaine.draw(canvas);
							// display fps
							// displayFps(canvas, avgFps);
						}
					}
				}
			}
		} else
			stop();
	}

	public void stop() {
		active = false;
		thread.setRunning(active);
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
	}

	/**
	 * This is the game update method. It iterates through all the objects and
	 * calls their update method if they have one or calls specific engine's
	 * update method.
	 */
	public void update() {
		elaine.update(System.currentTimeMillis());
	}

	private void displayFps(Canvas canvas, String fps) {
		if (canvas != null && fps != null) {
			Paint paint = new Paint();
			paint.setARGB(255, 255, 255, 255);
			canvas.drawText(fps, this.getWidth() - 50, 20, paint);
		}
	}

}
