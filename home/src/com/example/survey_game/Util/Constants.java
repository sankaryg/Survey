package com.example.survey_game.Util;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Environment;

import com.example.survey_game.R;
import com.example.survey_game.entity.ExtensionFilter;

public class Constants {

	public static int[] themes = { R.drawable.bgone, R.drawable.bgtwo,
			R.drawable.bgthree, R.drawable.bgfour, R.drawable.notheme };
	public static String[] speed = { "0.25", "0.5", "0.75" };
	public static long[] time = { 30 * 1000, 40 * 1000, 60 * 1000 };
	public static String[] fonts = { "Oxida Alternate.ttf",
			"Oxida Regular.ttf", "predator_0.ttf", "Punk kid.ttf", "romeo.ttf" };
	public static float gameTopFontSize = 24;
	public static int noOfBallons = 5;
	public static String name = "";
	public static String age = "";
	public static String gender = "";
	public static boolean noneUsed = false;
	public static boolean threeBalloons = false;
	public static boolean checkFinish = false;;
	public static boolean finishbonus = false;
	public static long bonustime = 5000;
	public static int maxScore = 1200;
	public static String productID = "";
	public static String endScreen = "rookie";
	public static HashMap<String, String> storeImage;// = new HashMap<String,
														// Bitmap>();
	public static String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "images";

	public static List<File> listFiles() {

		File[] files = Environment.getExternalStoragePublicDirectory("survey")
				.listFiles(new ExtensionFilter("jpg"));

		return Arrays.asList(files);
	}

}
