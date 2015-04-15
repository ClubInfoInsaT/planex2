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
	// Dimensions principales en pixels
	public static int columnWidth, columnHeight;
	public static int columnRightMargin;
	public static float hourHeight;
	public static float entryTextSize;
	public static int loadingViewVerticalPosition;
	
	public static void initialize(Context context) {
		mContext = context;
		columnWidth = getPixel(R.dimen.column_width);
		hourHeight = getPixelFloat(R.dimen.hours_height);
		columnHeight = (int) (hourHeight * 15);
		columnRightMargin = getPixel(R.dimen.separator_width);
		entryTextSize = getPixelFloat(R.dimen.entry_text_size);
		loadingViewVerticalPosition = getPixel(R.dimen.loading_view_ypos);
	}

	public static int getPixel(int dimenId) {
		return mContext.getResources().getDimensionPixelSize(dimenId);
	}
	public static int getPixelFloat(int dimenId) {
		return mContext.getResources().getDimensionPixelSize(dimenId);
	}
	
	//public static int getColumnDay
}
