package com.pijodev.insatpe2.widget;

import java.util.ArrayList;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.util.SparseArray;

import com.pijodev.insatpe2.Cache;
import com.pijodev.insatpe2.DateUtils;
import com.pijodev.insatpe2.ScheduleRetriever;
import com.pijodev.insatpe2.UserSession;
import com.pijodev.insatpe2.WeekEntries;

public class FetchService extends Service implements Callback {
	/** Id du widget **/
	private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	
	private static final int FETCH_FINISHED = 42;
	Handler mHandler = new Handler(this);
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	/** Fonction appelée lorsqu'on reçoit une commande de chargement de planning deuis un widget **/
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
			// Récupération de l'id du widget
			appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			// Chargement de la liste des groupes à afficher 
			fetch(new UserSession(appWidgetId, this).getGroups());
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	/** Téléchargement asynchrone des emplois du temps **/
	private void fetch(final ArrayList<Integer> groups) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int week = DateUtils.getCurrentWeek();
				ScheduleRetriever sr = new ScheduleRetriever();
				
				// Téléchargement des données
				String data = sr.download(groups, week);
				
				if(data != null) {
					// Parsing du fichier obtenu
					SparseArray<WeekEntries> we = sr.parse(data, groups, week);
					
					if(we != null) {
						// Mise en cache
						for(int i = 0; i < we.size(); i++)
							Cache.putWeekEntries(we.valueAt(i), week, we.keyAt(i), FetchService.this);
					}
					// On enregistre le cache 
					Cache.save(FetchService.this);
				}
				
				// On indique que c'est terminé
				mHandler.sendEmptyMessage(FETCH_FINISHED);
			}
		}).start();
	}
	
	private void onFetchFinished() {
		// On fait appel au widget provider pour qu'il mette à jour la liste du widget avec les données téléchargées
		Intent widgetUpdateIntent = new Intent();
		widgetUpdateIntent.setAction(WidgetProvider.DATA_FETCHED);
		widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		sendBroadcast(widgetUpdateIntent);

		this.stopSelf();
	}

	@Override
	public boolean handleMessage(Message msg) {
		// Message reçu lorsque le téléchargement asynchrone est terminé
		if(msg.what == FETCH_FINISHED)  {
			onFetchFinished();
			return true;
		}
		return false;
	}
}
