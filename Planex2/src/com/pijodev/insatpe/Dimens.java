package com.pijodev.insatpe;


import android.content.Context;
import com.pijodev.insatpe.R;

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

	private static int defaultColumnWidth;
	private static float defaultEntryTextSize;
	
	public static void initialize(Context context) {
		mContext = context;
		defaultColumnWidth = columnWidth = getPixel(R.dimen.column_width);
		hourHeight = getPixelFloat(R.dimen.hours_height);
		columnHeight = (int) (hourHeight * 15);
		columnRightMargin = getPixel(R.dimen.separator_width);
		defaultEntryTextSize = entryTextSize = getPixelFloat(R.dimen.entry_text_size);
		loadingViewVerticalPosition = getPixel(R.dimen.loading_view_ypos);
	}
	
	public static void update(int weekViewWidth, int weekViewHeight, boolean landscapeMode) {
		if(landscapeMode)
			columnWidth = (int) Math.ceil((double)(weekViewWidth-4*Dimens.columnRightMargin) * 0.2);
		else
			columnWidth = defaultColumnWidth;
		
		columnHeight = weekViewHeight * 60 / 41;//1500 / 1025; // nombre d'heures d'une journée : 15, nombre d'heures normales : 8h - 18h15 -> 10.25 heures
		hourHeight = (float)weekViewHeight / 10.25f;
		
		
		entryTextSize = Math.min(Math.max(10.0f, 0.75f*hourHeight/3), defaultEntryTextSize);
	}
	
	public static int getPixel(int dimenId) {
		return mContext.getResources().getDimensionPixelSize(dimenId);
	}
	public static int getPixelFloat(int dimenId) {
		return mContext.getResources().getDimensionPixelSize(dimenId);
	}
	
	//public static int getColumnDay
}
