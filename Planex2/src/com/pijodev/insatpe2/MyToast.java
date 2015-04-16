package com.pijodev.insatpe2;

import android.content.Context;
import android.widget.Toast;

public class MyToast {
	private static Toast mToast;
	
	public static void show(Context context, String msg, int duration) {
		if(mToast != null)
			mToast.cancel();
		
		mToast = Toast.makeText(context, msg, duration);
		
		mToast.show();
	}
}
