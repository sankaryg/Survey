package com.example.survey_game.Game;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.survey_game.DBfunction;
import com.example.survey_game.Global;
import com.example.survey_game.R;
import com.example.survey_game.Util.Constants;
import com.example.survey_game.WebService.ShowAlert;
import com.example.survey_game_fuction.CompFeature;
import com.example.survey_game_fuction.ContraFeature;
import com.example.survey_game_fuction.Feature;
import com.example.survey_game_fuction.Login;
import com.example.survey_game_fuction.ServerData;
import com.example.survey_game_fuction.Service;
import com.example.survey_game_fuction.Upload;

public class GameView extends SurfaceView {
	private static final int UPDATE_RATE = 30; // Frames per second (fps)
	private static final float EPSILON_TIME = 1e-2f; // Threshold for zero time

	private SurfaceHolder holder;

	private GameThread gameLoopThread;

	private static List<Ballons> ballons = new ArrayList<Ballons>();
	long timeleft1 = 2 * 60000;// 60 sec
	public int clicked = 0;
	static double starttime;// time when we started game
	public static GameView gameview;
	private int width;

	private int height;
	private Paint mTextPen;

	public int min;

	protected int sec;

	DBfunction db;
	float startY = 0, startX = 0;

	int dadBmp = 0, momBmp = 0;
	float momX, momY, dadX, dadY;
	Bitmap background;
	private boolean paused;

	private List<com.example.survey_game_fuction.Feature> featureList;

	class Point {
		float x, y;
	}

	int i = 0;
	Bundle bundle;
	private Bitmap scaled;

	public static boolean remove = false;
	private List<Score> score;
	public static boolean end = false;
	private int[] diffscreen = { R.drawable.balloon_green_sheet,
			R.drawable.balloon_orange_sheet, R.drawable.balloon_pink_sheet,
			R.drawable.balloon_red_sheet, R.drawable.balloon_violet_sheet,
			R.drawable.balloon_yellow_sheet, R.drawable.balloon_green_sheet };
	static int count = 0;
	ShowAlert alert;
	Global global;

	private List<ServerData> list = new ArrayList<ServerData>();
	private long lastClick;
	private List<Feature> features;
	public CountDownTimer timer;

	SharedPreferences preference;
	private static GameActivity activity;

	private List<CompFeature> compList;
	private static long millisRemaining = 1 * 30000;

	private List<ContraFeature> contraList;

	private String speed;

	private long time;

	private String bid;

	private Login log;

	private List<Upload> uploadList;

	private String str;

	private String font;

	private String uid;

	private String pid;

	private int fontsize;
	private Bitmap bmp;
	private static String brand;
	private List<Enemy> enemies;
	/*public Bitmap screenshot() {
		View v = getRootView();
		v.setDrawingCacheEnabled(true);
		v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		// v.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());

		v.buildDrawingCache(true);
		Bitmap bitmap = v.getDrawingCache();

		File file = new File(Environment.getExternalStorageDirectory()
				+ "/game_screen.png");
		try {
			file.createNewFile();
			FileOutputStream output = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 100, output);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		float heightScale = ((float) bitmap.getHeight()) / (float) height;

		float widthScale = (float) bitmap.getWidth() / (float) width;

		int newWidth = Math.round((float) bitmap.getWidth() / widthScale);
		int newHeight = Math.round((float) bitmap.getHeight() / heightScale);
		return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

	}*/
	private SharedPreferences preference1;
	private SharedPreferences sharedPrefs;

	public void cleanUp() {

		if (ballons != null && ballons.size() > 0) {
			for (int i = 0; i < ballons.size(); i++) {
				ballons.get(i).cleanup();
			}
			ballons = null;

		}
		db = null;
		if (background != null)
			background.recycle();
		if (scaled != null)
			scaled.recycle();
		if (score != null && score.size() > 0) {
			score.clear();
			score = null;
		}
		if (list != null && list.size() > 0) {
			list.clear();
			list = null;
		}
		if (featureList != null && featureList.size() > 0) {
			featureList.clear();
			featureList = null;
		}
		if (features != null && features.size() > 0) {
			features.clear();
			features = null;
		}
		if (compList != null && compList.size() > 0) {
			compList.clear();
			compList = null;
		}
		if (contraList != null && contraList.size() > 0) {
			contraList.clear();
			contraList = null;
		}
		if (uploadList != null && uploadList.size() > 0) {
			uploadList.clear();
			uploadList = null;
		}
		
	}

	public void countDownTimer(final long millisRemaining1) {
		final long millis = millisRemaining1;
		timer = new CountDownTimer(millis, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				if (!timerpause) {
					min = (int) (millisUntilFinished / 1000);
					sec = (int) ((millisUntilFinished) % 1000);
					millisRemaining = millisUntilFinished;
				}
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				if (!Score.score) {
					timer.cancel();
					if (ballons != null)
						ballons.clear();
					time = Constants.time[Integer.parseInt((sharedPrefs.getString(
							"prefSyncTimer", "NULL") == "NULL") ? "0" : sharedPrefs
							.getString("prefSyncTimer", "NULL"))];
					millisRemaining = time;
					if(timer != null)
						timer.cancel();
					countDownTimer(millisRemaining);
					//addDiffBallons(++count, Constants.noOfBallons);
					if (featureList.size() > Constants.noOfBallons){
						addDiffBallons(
								++count,
								Constants.noOfBallons);
					}
					else{
						//count = count-featureList.size();
						count = count/2;
						addDiffBallons(count,
								featureList.size());
					}
					//countDownTimer(millisRemaining);
					timer.start();
				}
				if (featureList.size() == 0) {
					stop();
					//timer.cancel();
					// startGame.gameEnd();
					millisRemaining = 0;
					GameView.end = true;
					GameEndCall();
				} 
				/*if (count >= diffscreen.length) {
					stop();
					// startGame.gameEnd();
					millisRemaining = 0;
					GameView.end = true;
					GameEndCall();

				}*/
			}
		};

	}

	public GameView(String bname, String bname2, Context context,
			GameActivity gameActivity, final int width, final int height,
			String uid, String pid) {

		super(context);
		end = false;
		gameview = this;
		activity = (GameActivity) context;
		sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(activity);
		time = 1 * 30000;
		int bg = Constants.themes[Integer.parseInt((sharedPrefs.getString(
				"prefSyncThems", "NULL") == "NULL") ? "0" : sharedPrefs
				.getString("prefSyncThems", "NULL"))];
		time = Constants.time[Integer.parseInt((sharedPrefs.getString(
				"prefSyncTimer", "NULL") == "NULL") ? "0" : sharedPrefs
				.getString("prefSyncTimer", "NULL"))];
		speed = Constants.speed[Integer.parseInt((sharedPrefs.getString(
				"prefSyncSpeed", "NULL") == "NULL") ? "0" : sharedPrefs
				.getString("prefSyncSpeed", "NULL"))];
		font = Constants.fonts[Integer.parseInt((sharedPrefs.getString(
				"prefSyncFont", "NULL") == "NULL") ? "0" : sharedPrefs
				.getString("prefSyncFont", "NULL"))];
		fontsize = sharedPrefs.getInt("prefSyncFontSize", 0);
		preference1 = activity.getSharedPreferences("Survey", Context.MODE_PRIVATE);
		db = new DBfunction(context);
		global = (Global) getContext().getApplicationContext();
		if(preference1.getBoolean("restart", false)){
			
			count = global.getCount();
			
			millisRemaining = global.getMillisremaining();
			
			if(millisRemaining == 0)
				millisRemaining = time;
			scorenumber = global.getScorenumber();
			
			ballcount = global.getBallcount();
			featureList = global.getFeatureList();
			 if(featureList == null)
				 featureList = db.retriveFeature(preference1.getString("product_id", "1"));
			list = global.getList();
			
			}else{
			count = 0;	
			millisRemaining = time;
			scorenumber = 0;
			ballcount = 0;
			featureList = db.retriveFeature(preference1.getString("product_id", "1"));
			
		}
		
		Constants.noneUsed = false;
		Constants.checkFinish = false;
		bundle = new Bundle();
		brand = bname2;
		bid = bname;
		this.uid = uid;
		this.pid = pid;
		
		compList = db.retriveCompFeature(preference1.getString("product_id", "1"));
		contraList = db.retriveContraFeature(preference1.getString("product_id", "1"));
		score = new ArrayList<Score>();
		enemies = new ArrayList<Enemy>();

		gameLoopThread = new GameThread(this);
		
		mTextPen = new Paint();
		mTextPen.setAntiAlias(true);
		mTextPen.setStyle(Style.STROKE);
		mTextPen.setColor(Color.WHITE);
		mTextPen.setTextSize(14 * (activity.getResources().getDisplayMetrics().density));
		features = new ArrayList<Feature>();

		
		log = db.getFirstRecord();
		uploadList = db.retriveUpload(preference1.getString("uid", null));
		if (uploadList.size() > 0) {
			str = "update";
			boolean newdata = true;
			for (Upload up : uploadList) {
				if (up.getBrandId().equals(bid)) {
					newdata = false;
					str = "update";
					break;
				} else {
					newdata = true;
					str = "new";
				}
			}
		} else
			str = "new";

		if(timer != null)
			timer.cancel();
		countDownTimer(millisRemaining);
		timer.start();
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(), font);
		mTextPen.setTypeface(tf);
		if (bg == R.drawable.notheme) {
			mTextPen.setAntiAlias(true);
			mTextPen.setStyle(Style.STROKE);
			mTextPen.setColor(Color.BLACK);
			mTextPen.setTextSize(Constants.gameTopFontSize
					* (activity.getResources().getDisplayMetrics().density));
		} else {
			mTextPen.setAntiAlias(true);
			mTextPen.setStyle(Style.STROKE);
			mTextPen.setColor(Color.WHITE);
			mTextPen.setTextSize(Constants.gameTopFontSize
					* (activity.getResources().getDisplayMetrics().density));
		}
		
		
		alert = new ShowAlert(context);
		bundle.putInt("count", count);
		if (ballons.size() > 0) {
			for (int i = 0; i < ballons.size(); i++)
				bundle.putSerializable("ballons_" + i, ballons.get(i));
		}
		bundle.putLong("time", millisRemaining);

		

		if (global.getService() != null)
			global.getService();
		this.width = width;

		this.height = height;
		// resume();
		holder = getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				stop();
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				addDiffBallons(diffscreen[count], Constants.noOfBallons);
				if (!gameLoopThread.isAlive()) {
					if (gameLoopThread.getState() == Thread.State.TERMINATED) {
						gameLoopThread = new GameThread(gameview);
						gameLoopThread.setRunning(true);
						gameLoopThread.start();

					} else {
						gameLoopThread.setRunning(true);
						gameLoopThread.start();
					}
					if(timer != null)
						timer.cancel();
					countDownTimer(millisRemaining);
					timer.start();
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,

			int width, int height) {

			}

		});

		background = BitmapFactory.decodeResource(getResources(), bg);
		float heightScale = ((float) background.getHeight()) / (float) height;

		float widthScale = (float) background.getWidth() / (float) width;

		int newWidth = Math.round((float) background.getWidth() / widthScale);
		int newHeight = Math
				.round((float) background.getHeight() / heightScale);
		scaled = Bitmap.createScaledBitmap(background, newWidth, newHeight,
				true);
		
	}

	Set<Feature> generated = new LinkedHashSet<Feature>();

	List<Integer> avoidrepeat = new ArrayList<Integer>();

	public boolean timerpause;

	private Set<Feature> dummylist = new LinkedHashSet<Feature>();

	private ServerData server;;

	private static int scorenumber;

	public void addDiffBallons(int id, int noofBallon) {
		// Collections.shuffle(featureList);
		if (count < diffscreen.length) {

			Random rng = new Random(); // Ideally just create one instance
										// globally
			// Note: use LinkedHashSet to maintain insertion order
			dummylist.clear();
			dummylist = generated;
			generated.clear();
			while (generated.size() < noofBallon) {
				boolean repeat = false;
				Integer next = rng.nextInt(featureList.size());
				if (dummylist.size() > 0) {
					Iterator<Feature> it = dummylist.iterator();
					while (it.hasNext()) {
						System.out.println(it.next());
						try {
							if (it.next().getId() == featureList.get(next)
									.getId()) {
								repeat = true;
								break;
							}
						} catch (NoSuchElementException e) {
							// TODO: handle exception
						}
					}
				}
				if (!repeat)
					generated.add(featureList.get(next));
			}
			Log.d("checxk", generated.size()+"_"+count);
			for (Feature f1 : generated) {
				addBallons(f1, id);
			}
			addBallons(null, id);
			/*
			 * int random; //for (int i = count; i < noofBallon+count; i++) //
			 * addBallons(featureList.get(new //
			 * Random().nextInt(featureList.size())).getFeature(), id); //random
			 * =new Random().nextInt(featureList.size()-count);
			 * //addBallons(featureList.get(i), id); int j=0; features.clear();
			 * for(int i=0;i>=0;i++){ boolean repeat = false; random =new
			 * Random().nextInt(featureList.size()); Log.d("abc",
			 * random+"_"+featureList.get(random).getId()); if(features!=null &&
			 * features.size()>0){ for(int k=0;k<features.size();k++){
			 * Log.d("abc", random+"_"+features.get(k).getId()); if(random ==
			 * features.get(k).getId()){ repeat = true; break; } } }
			 * if(!repeat){ if(j<noofBallon){
			 * features.add(featureList.get(random));
			 * addBallons(features.get(j), id); j++; }else{ break; } } }
			 */
			// }
		} else {
			// stop();
			// alert.showAlertDialog(getContext(), "End ", "Game Over", false);
			//
		}
	}

	int spe = -1;

	private int layout;

	private void addBallons(com.example.survey_game_fuction.Feature feature,
			int id) {
		Bitmap bmp = null;
		String str1="nil";
		if (feature == null)
			bmp = createBitmap(R.drawable.balloon_gray_sheet);
		else{
			/*for(ContraFeature cnt:contraList){
			if(cnt.getFeature_id().contains(String.valueOf(feature.getId()))){
				str1 = "contra";
				break;
			}
			}for(CompFeature cnt:compList){
				if(cnt.getFeature_id().contains(String.valueOf(feature.getId()))){
					str1 = "comp";
					break;
				}
				}*/
		/*	if(count>diffscreen.length-1){
			layout = count%diffscreen.length;
			}else
				layout = diffscreen[count];*/
			bmp = createBitmap(diffscreen[count]);
		}
			if (bmp != null) {
			double radius = Math.random() * bmp.getWidth() / 2;
			double rotation = Math.floor(Math.random() * 360);
			/*
			 * if(ballons.size()>0){ for(Ballons ballon:ballons){
			 * if(ballon.getFeature().getId()==feature.getId()){ feature =
			 * featureList.get(new Random().nextInt(featureList.size())); break;
			 * } } }
			 */
			ballons.add(new Ballons(this, bmp, bmp.getWidth()/16, 30,
					feature, Float.parseFloat(speed) , font,
					fontsize));

		}
	}
	Bitmap add = null;
	private Bitmap createBitmap(int bmp) {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inSampleSize = 1;
		o.inDither = false;
		o.inPurgeable = true;
		//o.inTempStorage=new byte[16 * 1024];
		/*if(add!=null)
			add.recycle();*/
		add = BitmapFactory.decodeResource(getResources(), bmp, o);
		
		return add;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(Constants.threeBalloons)
			return true;
		if(remove)
			return true;
		if(list== null)
			list = new ArrayList<ServerData>();
		if (System.currentTimeMillis() - lastClick > 500) {

			lastClick = System.currentTimeMillis();
			synchronized (getHolder()) {

				for (int i = ballons.size() - 1; i >= 0; i--) {
					if (!(ballons.get(i).getFeature() == null && ballons.get(i)
							.isCollition(event.getX(), event.getY()))) {
						if (ballons.get(i).isCollition(event.getX(),
								event.getY())) {
							remove = true;
							score.clear();
							ballons.get(i).setRemove(remove);
							server = new ServerData();
							if (featureList.size() > 0) {
								for (int c = 0; c < featureList.size(); c++) {
									if (ballons.get(i).getFeature().getId() == featureList
											.get(c).getId()) {
										featureList.remove(featureList.get(c));
										break;
									}
								}
							}
							if (ballons.get(i).getFeature() != null) {
								server.setFeature_Id(ballons.get(i)
										.getFeature().getId());
								for (int j = 0; j < contraList.size(); j++) {
									if (ballons.get(i).getFeature().getId() == Integer
											.parseInt(contraList.get(j)
													.getFeature_id())) {
										server.setContra_Feature(contraList
												.get(j).getContra_feature_1()
												+ ","
												+ contraList.get(j)
														.getContra_feature_2()
												+ ","
												+ contraList.get(j)
														.getContra_feature_3()
												+ ","
												+ contraList.get(j)
														.getContra_feature_4()
												+ ","
												+ contraList.get(j)
														.getContra_feature_5());
										Log.d("abc", server.getContra_Feature()
												+ "_contra");
										break;
									}
								}
								for (int k = 0; k < compList.size(); k++) {
									if (ballons.get(i).getFeature().getId() == Integer
											.parseInt(compList.get(k)
													.getFeature_id())) {
										server.setComp_Feature(compList.get(k)
												.getContra_feature_1()
												+ ","
												+ compList.get(k)
														.getContra_feature_2()
												+ ","
												+ compList.get(k)
														.getContra_feature_3()
												+ ","
												+ compList.get(k)
														.getContra_feature_4()
												+ ","
												+ compList.get(k)
														.getContra_feature_5());
										Log.d("abc", server.getComp_Feature()
												+ "_comp");
										break;
									}
								}
								if (list!=null && list.size() == 0) {
									score.add(new Score(score, this, ballons
											.get(i).getX()
											+ ballons.get(i).getBmp()
													.getWidth() / 8 / 2,
											ballons.get(i).getY()
													+ ballons.get(i).getBmp()
															.getHeight() / 4,
											createBitmap(R.drawable.five)));
									scorenumber += 50;
								}
								list.add(server);
								if (list.size() > 1) {
									boolean def = false;
									for (int k = 0, m = list.size() - 1; k < list
											.size(); k++) {
										/*
										 * if(list.size()==(k+1)) m = k-1; else
										 * m = k+1;
										 */
										
										if (!def
												&& list.get(m)
														.getContra_Feature() != null && list.get(k).getContra_Feature()!=null) {
											for (String str : list.get(k)
													.getContra_Feature()
													.split(",")) {
												// if(list.get(m).getContra_Feature()!=null
												// && !str.equals("0")){
												// for(String
												// comp:list.get(m).getContra_Feature().split(",")){
												if (String
														.valueOf(
																list.get(m)
																		.getFeature_Id())
														.equals(str)) {
													score.add(new Score(
															score,
															this,
															ballons.get(i)
																	.getX()
																	+ ballons
																			.get(i)
																			.getBmp()
																			.getWidth()
																	/ 8 / 2,
															ballons.get(i)
																	.getY()
																	+ ballons
																			.get(i)
																			.getBmp()
																			.getHeight()
																	/ 4,
															createBitmap(R.drawable.ten_negative)));
													scorenumber -= 100;
													ballcount = 0;
													def = true;
													break;
												}

											}
											if (!def
													&& list.get(k)
															.getContra_Feature() != null && list.get(m).getContra_Feature()!=null) {
												for (String str : list.get(m)
														.getContra_Feature()
														.split(",")) {
													// if(list.get(m).getContra_Feature()!=null
													// && !str.equals("0")){
													// for(String
													// comp:list.get(m).getContra_Feature().split(",")){
													if (String
															.valueOf(
																	list.get(k)
																			.getFeature_Id())
															.equals(str)) {
														score.add(new Score(
																score,
																this,
																ballons.get(i)
																		.getX()
																		+ ballons
																				.get(i)
																				.getBmp()
																				.getWidth()
																		/ 8 / 2,
																ballons.get(i)
																		.getY()
																		+ ballons
																				.get(i)
																				.getBmp()
																				.getHeight()
																		/ 4,
																createBitmap(R.drawable.ten_negative)));
														scorenumber -= 100;
														ballcount = 0;
														def = true;
														break;
													}
												}
											}
										}
										if (!def
												&& list.get(m)
														.getComp_Feature() != null && list.get(k)
																.getComp_Feature()!=null) {
											for (String str : list.get(k)
													.getComp_Feature()
													.split(",")) {
												// if(list.get(m).getComp_Feature()!=null
												// && !str.equals("0")){
												// for(String
												// comp:list.get(m).getComp_Feature().split(",")){
												if (String
														.valueOf(
																list.get(m)
																		.getFeature_Id())
														.equals(str)) {
													score.add(new Score(
															score,
															this,
															ballons.get(i)
																	.getX()
																	+ ballons
																			.get(i)
																			.getBmp()
																			.getWidth()
																	/ 8 / 2,
															ballons.get(i)
																	.getY()
																	+ ballons
																			.get(i)
																			.getBmp()
																			.getHeight()
																	/ 4,
															createBitmap(R.drawable.ten)));
													scorenumber += 100;
													ballcount++;
													def = true;
													break;
												}

											}
											if (!def
													&& list.get(k)
															.getComp_Feature() != null && list.get(m).getComp_Feature()!=null) {
												for (String str : list.get(m)
														.getComp_Feature()
														.split(",")) {
													// if(list.get(m).getComp_Feature()!=null
													// && !str.equals("0")){
													// for(String
													// comp:list.get(m).getComp_Feature().split(",")){
													if (String
															.valueOf(
																	list.get(k)
																			.getFeature_Id())
															.equals(str)) {
														score.add(new Score(
																score,
																this,
																ballons.get(i)
																		.getX()
																		+ ballons
																				.get(i)
																				.getBmp()
																				.getWidth()
																		/ 8 / 2,
																ballons.get(i)
																		.getY()
																		+ ballons
																				.get(i)
																				.getBmp()
																				.getHeight()
																		/ 4,
																createBitmap(R.drawable.ten)));
														scorenumber += 100;
														ballcount++;
														def = true;
														break;
													}
												}
											}
										}
										if (!def
												&& list.get(m)
														.getComp_Feature() == null
												&& list.get(m)
														.getContra_Feature() == null) {
											score.add(new Score(
													score,
													this,
													ballons.get(i).getX()
															+ ballons.get(i)
																	.getBmp()
																	.getWidth()
															/ 8 / 2,
													ballons.get(i).getY()
															+ ballons
																	.get(i)
																	.getBmp()
																	.getHeight()
															/ 4,
													createBitmap(R.drawable.ten)));
											scorenumber += 100;
											ballcount++;// = 0;
											def = true;
											break;
										}
										if (def)
											break;
									}
									
									  if (!def) {
											score.add(new Score(
													score,
													this,
													ballons.get(i).getX()
															+ ballons.get(i)
																	.getBmp()
																	.getWidth()
															/ 8 / 2,
													ballons.get(i).getY()
															+ ballons
																	.get(i)
																	.getBmp()
																	.getHeight()
															/ 4,
													createBitmap(R.drawable.ten)));
											scorenumber += 100;
											ballcount++;
											def = true;
										}
										if (ballcount == 3) {
											// bmp = screenshot();
											enemies.clear();
											Bitmap bmp = createBitmap(R.drawable.bonus_seq);
											enemies.add(new Enemy(gameview, bmp,( width -bmp.getWidth()/20 )/2,
													(height  - bmp.getHeight())/2, bmp.getWidth(), 20));
											ballcount = 0;
											scorenumber += 100;
											Constants.threeBalloons = true;
											timerPause();
										}
								}
								Log.d("check", list + "_");
							}
							//if (Constants.threeBalloons) {
								// pause();
								/*if (handler == null) {
									handler = new Handler();
								}
								handler.postDelayed(runnable, 3000);*/
								//

								// activity.openDialog();
								// activity.onPause();
								//timerPause();
							//} else
							if ((activity != null)) {
								activity.playPop();
							}
							++count;
							timer.cancel();
							break;
						}
					
					
					} else {
						//
						if (!Constants.noneUsed) {
							for (int j = ballons.size() - 1; j >= 0; j--) {
								// if(ballons.get(i).getFeature()!= null){
								remove = true;
								score.clear();
								ballons.get(j).setRemove(remove);
								// Constants.noneUsed = true;
								if (featureList.size() > 0) {
									for (int c = 0; c < featureList.size(); c++) {
										if (ballons.get(j).getFeature() != null) {
											if (ballons.get(j).getFeature()
													.getId() == featureList
													.get(c).getId()) {
												featureList.remove(featureList
														.get(c));
												break;
											}
										}
									}
									// }
								}
							}
							break;
						}
					}
				}

			}

		}
		time = Constants.time[Integer.parseInt((sharedPrefs.getString(
				"prefSyncTimer", "NULL") == "NULL") ? "0" : sharedPrefs
				.getString("prefSyncTimer", "NULL"))];
		millisRemaining = time;
		if(timer != null)
			timer.cancel();
		countDownTimer(millisRemaining);
		return false;
	}

	public static int ballcount = 0;

	public void GameEndCall() {
		if (list != null && list.size() > 0) {
			String feature = "";
			for (ServerData ser : list) {
				feature += ser.getFeature_Id() + ",";
			}
			Upload upload = new Upload();
			Log.d("check", uid + "_" + pid);
			if (uid == null)
				uid = "10";
			if (pid == null)
				pid = "1";
			upload.setAge(uid);// setAge(String.valueOf(log.getAge()));//player
								// id
			upload.setName(pid);// setName(log.getName());//product id
			upload.setGender(log.getGender());
			upload.setBrand(brand);
			upload.setBrandId(bid);
			upload.setFeature(feature.substring(0, feature.length() - 1));

			if (str.equals("new")) {
				db.UploadData(upload);
			} else {
				db.updateUpload(upload);
			}
		}
		Constants.checkFinish = true;
		Constants.threeBalloons = false;
		ballcount = 0;
		/*if (handler != null) {
			handler.removeCallbacks(runnable);
		}*/
		gameLoopThread.setRunning(false);
		// activity.gameEnd();
		// handler = new Handler();
		// handler.postDelayed(runnable, 500);
		movenext();
	}

	public void movenext() {

		if(scorenumber < 0){
			scorenumber = 0;
			Constants.endScreen = "rookie";
			//coin_rotate.setImageResource(R.drawable.rookie);
		}
		else if(scorenumber <= (Constants.maxScore * 0.5)){
			Constants.endScreen = "rookie";
			//coin_rotate.setImageResource(R.drawable.rookie);
		}else if( scorenumber <= (Constants.maxScore * 0.8) && scorenumber > (Constants.maxScore * 0.5)){
			Constants.endScreen = "pro";
			//coin_rotate.setImageResource(R.drawable.pro);
		}else if(scorenumber > (Constants.maxScore * 0.8) ){//&& score <= Constants.maxScore
			Constants.endScreen = "expert";
			//coin_rotate.setImageResource(R.drawable.expert);
		}

		Intent inte = new Intent(activity, GameEnd.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		inte.putExtra("bname", brand);
		inte.putExtra("bid", bid);
		inte.putExtra("score", scorenumber);
		activity.startActivity(inte);
		activity.finish();
		//stop();
	}

	public void pause() {
		// gameLoopThread.setRunning(false);

		/*
		 * try { gameLoopThread.wait(); } catch (InterruptedException e) { //*
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */
		bundle.putInt("count", count);
		global.setCount(count);
		/*
		 * if(ballons.size()>0){ for(int i=0;i<ballons.size();i++)
		 * bundle.putSerializable("ballons_"+i, ballons.get(i)); }
		 */
		bundle.putLong("time", millisRemaining);
		global.setMillisremaining(millisRemaining);
		bundle.putInt("score", scorenumber);
		global.setScorenumber(scorenumber);
		bundle.putInt("bonuscount", ballcount);
		global.setBallcount(ballcount);
		saveFeatureList(bundle);
		GameThread.paused = true;
		// timer.pause();
		if(preference1.getBoolean("restart", false))
			gameLoopThread.setRunning(false);
		else
		stop();
		timerpause = true;
		paused = true;
	}
	public void saveFeatureList(Bundle b)
	{
		
	    
	    b.putInt("Feature_size",featureList.size()); /* sKey is an array */ 

	    global.setFeatureList(featureList);
	    /*for(int i=0;i<featureList.size();i++)  
	    {
	        b.putSerializable("feature_"+i, featureList.get(i));
	    }*/
	    //b.putInt("Server_size", list.size());
	    global.setList(list);
	    /*for(int i=0;i<list.size();i++)  
	    {
	        b.putSerializable("list_"+i, list.get(i));
	    }*/

	}
	public void resume() {
		// gameLoopThread.notifyAll();
		//ballcount = 0;
		count = global.getCount();//bundle.getInt("count", count);
		/*
		 * if(ballons.size()>0){ ballons.clear(); for(int i=0;i<5;i++){
		 * ballons.add((Ballons)bundle.getSerializable("ballons_"+i)); } }
		 */
		millisRemaining = bundle.getLong("time");;//global.getMillisremaining();//bundle.getLong("time");
		paused = false;
		scorenumber = global.getScorenumber();//bundle.getInt("score");
		ballcount = global.getBallcount();//bundle.getInt("bonuscount");
		/*int featureSize = bundle.getInt("Feature_size"); 
		if(featureList!= null && featureList.size()>0){
			featureList.clear();
		
		}else {
			featureList = new ArrayList<Feature>();
		}*/
		featureList = global.getFeatureList();
	    /*for(int i=0;i<featureSize;i++)  
	    {
	        featureList.add((Feature) bundle.getSerializable("feature_"+i));
	    }*/
	    /*int serverSize = bundle.getInt("Server_size"); 
		if(list!= null && list.size()>0){
			list.clear();
		}else {
			list = new ArrayList<ServerData>();
		}
		*/
		list = global.getList();
	    /*for(int i=0;i<serverSize;i++)  
	    {
	        list.add((ServerData) bundle.getSerializable("list_"+i));
	    }*/
		
		timerpause = false;
		if(!preference1.getBoolean("restart", false)){
			if(timer != null)
				timer.cancel();
			countDownTimer(millisRemaining);
		timer.start();
		}
		gameLoopThread.paused = false;
		if (!gameLoopThread.isAlive()) {

			gameLoopThread = new GameThread(gameview);
			gameLoopThread.setRunning(true);
			gameLoopThread.start();
		}
	}

	public void timerPause() {
		bundle.putInt("count", count);
		bundle.putLong("time", millisRemaining);
		timerpause = true;
		// paused = true;
	}

	public void timerResume() {
		//ballcount = 0;
		count = bundle.getInt("count", count);
		millisRemaining = bundle.getLong("time");
		// paused = false;
		timerpause = false;
		if(!preference1.getBoolean("restart", false)){
			if(timer != null)
				timer.cancel();
		//countDownTimer(millisRemaining);
		timer.start();
		}
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	public void stop() {
		boolean retry = true;

		gameLoopThread.setRunning(false);
		timer.cancel();
		while (retry) {

			try {

				gameLoopThread.join();

				retry = false;

			} catch (InterruptedException e) {

			}

		}

	}

	public double getRadians(double delta_x, double delta_y) {
		double r = Math.atan2(delta_y, delta_x);

		if (delta_y < 0) {
			r += (2 * Math.PI);
		}
		return r;
	}

	/**
	 * Get degrees
	 * 
	 * @param radians
	 * @return
	 */
	public double getDegrees(double radians) {
		return Math.floor(radians / (Math.PI / 180));
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (!Constants.checkFinish) {
			if (!paused) {
				canvas.drawBitmap(scaled, 0, 0, null);
				synchronized (canvas) {
					try {
						if (score != null && score.size() > 0) {
							for (int i = score.size() - 1; i >= 0; i--) {

								Score.score = true;
								score.get(i).onDraw(canvas);

							}
						} else {
							Score.score = false;
							{
						/*if (count >= diffscreen.length) {
									// stop();
									// startGame.gameEnd();
									GameView.end = true;
									GameEndCall();

								}*/
							}
						}
						if (ballons.size() > 0) {
							for (int i = 0; i < ballons.size(); i++) {
							/*	for (int j = i + 1; j < ballons.size(); j++) {

									if (Ballons
											.checkcirclecollide(ballons.get(i)
													.getX(), ballons.get(i)
													.getY(), ballons.get(i)
													.getBmp().getWidth() / 2,
													ballons.get(j).getX(),
													ballons.get(j).getY(),
													ballons.get(j).getBmp()
															.getWidth() / 2)) {
										Ballons.intersect(ballons.get(i),
												ballons.get(j));

										Log.d("check", "collided" + "_" + i
												+ " collided with " + j);

									}

								}*/
								if (!Score.score) {
									if (ballons.size() > 0) {
										/*if (Constants.threeBalloons) {
											ballons.get(i);
											Ballons.setCurrentFrame(1);
										}*/
										ballons.get(i).onDraw(canvas);
										if (!Constants.threeBalloons) {
										try {
											if (ballons.get(i).isRemove()
													&& ballons.get(i)
															.getCurrentFrame() == Ballons.BMP_COLUMNS - 1) {
												remove = false;
												ballons.get(i)
														.setRemove(remove);
												// ballons.remove(i);
												millisRemaining = time;
												ballons.clear();
												if(timer != null)
													timer.cancel();
												//countDownTimer(millisRemaining);
												timer.start();
												if(count>=diffscreen.length)
													count = count/2;
												if (featureList.size() > Constants.noOfBallons){
												
													addDiffBallons(
															count,
															Constants.noOfBallons);
												}
												else if (featureList.size() == 0) {
													// stop();
													timer.cancel();
													// startGame.gameEnd();
													millisRemaining = 0;
													GameView.end = true;
													GameEndCall();
												} else{
													//count = count-featureList.size();
													count = count/2;
													addDiffBallons(count,
															featureList.size());
												}
											}
										}

										catch (Exception e) {
											// TODO: handle exception
										}
										}
									}
								}
								canvas.drawText(
										String.format("%02d", min / 60)
												+ ":"
												+ String.format("%02d",
														min % 60),
										width / 2 - 40, 60, mTextPen);
								canvas.drawText("Score : " + scorenumber, width
										- width / 4 - 30, 30, mTextPen);
								canvas.drawText("Brand : " + brand, 20, 30,
										mTextPen);
							}
						}
						if (Constants.threeBalloons) {
							synchronized (canvas) {
								if (enemies.size() > 0) {
									for (Enemy enemy : enemies) {
										enemy.onDraw(canvas);
									}
								}
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
						Log.d("update", e.getMessage()+"_"+e.toString());
						// GameEndCall();
					}
				}
			}
			
		} else {
			// cleanUp();
		}
		if(preference1.getBoolean("restart", false)){
			pause();
			preference1.edit().putBoolean("restart", false).commit();
		}
	}

	public boolean isPaused() {
		// TODO Auto-generated method stub
		return paused;
	}
		public void gameUpdate() {
		float timeLeft = 2.0f; // One time-step to begin with

		// Repeat until the one time-step is up
		do {
			// Find the earliest collision up to timeLeft among all objects
			float tMin = timeLeft;

			// Check collision between two balls
			for (int i = 0; i < ballons.size(); i++) {
				for (int j = 0; j < ballons.size(); j++) {
					if (i < j) {
						try{
						ballons.get(i).intersect(ballons.get(j), tMin);
						if (ballons.get(i).earliestCollisionResponse.t < tMin) {
							tMin = ballons.get(i).earliestCollisionResponse.t;
						}
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				}
			}
			// Check collision between the balls and the box
			for (int i = 0; i < ballons.size(); i++) {
				ballons.get(i).intersect(gameview, tMin);
				if (ballons.get(i).earliestCollisionResponse.t < tMin) {
					tMin = ballons.get(i).earliestCollisionResponse.t;
				}
			}

			// Update all the balls up to the detected earliest collision time
			// tMin,
			// or timeLeft if there is no collision.
			for (int i = 0; i < ballons.size(); i++) {
				ballons.get(i).update(tMin);
			}

			timeLeft -= tMin; // Subtract the time consumed and repeat
		} while (timeLeft > EPSILON_TIME); // Ignore remaining time less than
											// threshold
	}


}