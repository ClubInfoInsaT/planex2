package com.pijodev.insatpe2;


import android.content.Context;

/**
 * Permet d'accéder aux dimensions définies dans les fichiers xml
 * 
 * @author Proïd
 *
 */
public class Dimens {
	private static Context mContext;
	// Dimensions principales 
	public static int columnWidth, columnHeight;
	public static int hourHeight;
	public static int entryTextSize;
	
	public static void initialize(Context context) {
		mContext = context;
		columnWidth = getPixel(R.dimen.column_width);
		hourHeight = getPixel(R.dimen.hours_height);
		columnHeight = hourHeight * 15;
		entryTextSize = getPixel(R.dimen.entry_text_size);
	}
	
	public static int getPixel(int dimenId) {
		return mContext.getResources().getDimensionPixelSize(dimenId);
	}
	
	//public static int getColumnDay
}
