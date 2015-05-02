package com.pijodev.insatpe2.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViewsService;


/**
 * Service qui donne accès à un ListProvider
 * 
 * -> https://laaptu.wordpress.com/2013/07/19/android-app-widget-with-listview/
 * -> http://www.maraumax.fr/pages-3-creation-d-un-widget-parametrable-sur-android.html
 * 
 * @author Proïd
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB) // remote view service api lvl 11
public class WidgetService extends RemoteViewsService {
	
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		// On crée un nouveau ListProvider
		return (new ListProvider(this.getApplicationContext(), intent));
	}
 
}


