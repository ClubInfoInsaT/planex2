package com.pijodev.insatpe.widget;

import java.util.ArrayList;
import java.util.Collections;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.pijodev.insatpe.Cache;
import com.pijodev.insatpe.DateUtils;
import com.pijodev.insatpe.Entry;
import com.pijodev.insatpe.R;
import com.pijodev.insatpe.UserSession;
import com.pijodev.insatpe.WeekEntries;

/**
 * Met en forme le contenu de la liste depuis les données du cache.
 * 
 * -> https://laaptu.wordpress.com/2013/07/19/android-app-widget-with-listview/
 * -> http://www.maraumax.fr/pages-3-creation-d-un-widget-parametrable-sur-android.html
 * 
 * @author Proïd
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ListProvider implements RemoteViewsFactory {
	/** Données à afficher dans la liste : {Nom, Horaires+Salle, groupes, couleur} **/
	private ArrayList<String[]> mItemList = new ArrayList<String[]>();
	/** Référence vers le context de l'application **/
	private Context mContext = null;
	/** Id du widget **/
	private int mAppWidgetId;
	/** Listes des groupes à afficher **/
	private ArrayList<Integer> groupsId;

	public ListProvider(Context context, Intent intent) {
		this.mContext = context;
		// Récupération de l'id du widget
		mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
	}

	@Override
	public int getCount() {
		return mItemList.size();
	}

	/** Crée la vue associée à un cours **/
	@Override
	public RemoteViews getViewAt(int position) {
		final RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.item_widget);
		String[] data = mItemList.get(position);
		
		// Nom du cours
		remoteView.setTextViewText(R.id.item_widget_summary, data[0]);
		
		// Salle et horaires
		remoteView.setTextViewText(R.id.item_widget_details, data[1]);
		
		// Références des groupes concernés
		if(data[2].length() < groupsId.size())
			remoteView.setTextViewText(R.id.item_widget_gnum, data[2]);
		else
			remoteView.setViewVisibility(R.id.item_widget_gnum, View.GONE);
		
		// Couleur
		remoteView.setInt(R.id.item_widget_ll, "setBackgroundColor", Integer.parseInt(data[3]));
		
		return remoteView;
	}


	/** Met à jour la liste en rechargeant les données depuis le cache **/
	@Override
	public void onDataSetChanged() {
		// Chargement de la liste des groupes
		groupsId = new UserSession(mAppWidgetId, mContext).getGroups();
		
		//Log.i("###", "onDataSetChanged gid:"+groupsId +" widgetId:"+mAppWidgetId);
		ArrayList<Entry> entries = new ArrayList<>();
		mItemList.clear();
		
		// Numéro de semaine et jour de la semaine
		int week = DateUtils.getCurrentWeek();
		int day = DateUtils.getDayOfWeek();
		int gnum = 0;
		
		// Rassemblement et fusion de tous les cours
		for(Integer gid : groupsId) {
			WeekEntries we = Cache.getWeekEntries(week, gid, mContext);
			if(we != null)
			for(Entry e : we.getEntries(day)) {
				int index = Collections.binarySearch(entries, e);
				if(index < 0) {
					entries.add(-index-1, e);
					mItemList.add(-index-1, new String[]{e.getSummary(), e.getStringTime()+" "+e.getRoom(), ""+(gnum+1), ""+e.getColor()});
				} else {
					mItemList.get(index)[2] += (gnum + 1);
				}
			}
			gnum++;
		}
	}

	/** osef **/
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/** osef **/
	@Override
	public void onCreate() {
	}
	
	/** osef **/
	@Override
	public void onDestroy() {
	}
	
	/** osef **/
	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	/** osef **/
	@Override
	public int getViewTypeCount() {
		return 1;
	}

	/** osef **/
	@Override
	public boolean hasStableIds() {
		return false;
	}
}
