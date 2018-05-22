package com.insat.info.club.planexv2.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import com.insat.info.club.planexv2.GroupList;

import com.insat.info.club.planexv2.ScheduleActivity;
import com.insat.info.club.planexv2.UserSession;
import com.insat.info.club.planexv2.R;
/**
 *
 * -> https://laaptu.wordpress.com/2013/07/19/android-app-widget-with-listview/
 * -> http://www.maraumax.fr/pages-3-creation-d-un-widget-parametrable-sur-android.html
 *
 * @author Proïd
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WidgetProvider extends AppWidgetProvider {

    public static final String DATA_FETCHED = "com.pijodej.insatpe2.DATA_FETCHED";

    /** Permet de restaurer un widget supprimé par l'utilisateur **/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRestored(Context context, int[] oldWidgetIds,
                           int[] newWidgetIds) {
        //Log.i("###", "onRestored");
        for(int i = 0;i<newWidgetIds.length;i++) {
            //Log.i("###", "onRestored >"+oldWidgetIds[i]+":"+newWidgetIds[i]);
            UserSession newSession = new UserSession(newWidgetIds[i], context);
            UserSession oldSession = new UserSession(oldWidgetIds[i], context);
            newSession.setGroups(oldSession.getGroups());
        }
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context,
                                          AppWidgetManager appWidgetManager, int appWidgetId,
                                          Bundle newOptions) {
        //Log.i("###", "onAppWidgetOptionsChanged > "+appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
                newOptions);
    }

    /*
     * this method is called every 6 hours as specified on widgetinfo.xml this
     * method is also called on every phone reboot from this method nothing is
     * updated right now but instead RetmoteFetchService class is called this
     * service will fetch data,and send broadcast to WidgetProvider this
     * broadcast will be received by WidgetProvider onReceive which in turn
     * updates the widget
     */

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Log.i("###", "onUpdate");
        // Chargement de la liste des groupes si ce n'est pas déjà fait
        GroupList.load(context);

        for (int appWidgetId : appWidgetIds) {
            boolean manualUpdate = false;
            if(appWidgetId < 0) {
                appWidgetId = -appWidgetId;
                manualUpdate = true;
            }
            // Si le widget a déjà été configuré (titre initialisé), on met à jour la vue
            if(new UserSession(appWidgetId, context).getTitle().length() > 0) {
                if(!manualUpdate) {
                    //Log.i("###", "onUpdate button > "+appWidgetId);
                    // Mise à jour de la vue avec liste
                    RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
                    appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
                    // On met à jour le contenu de la liste
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_widget_list);

                }
                else {
                    //Log.i("###", "onUpdate auto > "+appWidgetId);
                    // Création du service de mise à jour
                    Intent serviceIntent = new Intent(context, FetchService.class);
                    // on transmet l'id du widget
                    serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                    // Lancement du service
                    context.startService(serviceIntent);
                    // Affichage du widget avec icone de chargement
                    appWidgetManager.updateAppWidget(appWidgetId, updateWidgetLoadingView(context, appWidgetId));
                }
            }
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    /** Fonction appelée lorsqu'un widget est supprimé **/
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        //Log.i("###", "onDeleted");

        // On supprime le fichier de session associé
        for(int id : appWidgetIds)
            new UserSession(id, context).remove(context);
    }

    /** Construit la vue du widget avec une icone de chargement au centre **/
    private RemoteViews updateWidgetLoadingView(Context context, int appWidgetId) {
        //Log.i("###", "updateWidgetLoadingView");
        // which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        UserSession session = new UserSession(appWidgetId, context);

        // Bouton 'actualiser'
        Intent intentRefresh = new Intent(context, WidgetProvider.class);
        intentRefresh.setData(Uri.withAppendedPath(Uri.parse("imgwidget://widget/id/"), String.valueOf(appWidgetId)));
        intentRefresh.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intentRefresh.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { -appWidgetId });

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentRefresh, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ib_widget_refresh, pendingIntent);

        // Titre de widget
        remoteViews.setTextViewText(R.id.tv_widget_title, session.getTitle());
        Intent configIntent = new Intent(context, ScheduleActivity.class);
        configIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_widget_title, configPendingIntent);

        // Icone de chargement
        remoteViews.setViewVisibility(R.id.pb_widget_loading, View.VISIBLE);

        // Liste (-> cachée)
        remoteViews.setViewVisibility(R.id.lv_widget_list, View.GONE);

        return remoteViews;
    }

    /** Construit la vue du widget avec la liste **/
    @SuppressWarnings("deprecation") // setRemoteAdapter(int,int,Intent)
    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {
        //Log.i("###", "updateWidgetListView");
        // which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        UserSession session = new UserSession(appWidgetId, context);

        // Bouton 'actualiser'
        Intent intentRefresh = new Intent(context, WidgetProvider.class);
        intentRefresh.setData(Uri.withAppendedPath(Uri.parse("imgwidget://widget/id/"), String.valueOf(appWidgetId)));
        intentRefresh.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intentRefresh.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { -appWidgetId });

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentRefresh, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ib_widget_refresh, pendingIntent);

        // Titre de widget
        remoteViews.setTextViewText(R.id.tv_widget_title, session.getTitle());
        Intent configIntent = new Intent(context, ScheduleActivity.class);
        //TODO: A régler
        configIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent configPendingIntent = PendingIntent.getActivity(context, appWidgetId, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_widget_title, configPendingIntent);

        // Icone de chargement (-> cachée)
        remoteViews.setViewVisibility(R.id.pb_widget_loading, View.GONE);

        // Liste
        remoteViews.setViewVisibility(R.id.lv_widget_list, View.VISIBLE);
        // RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, WidgetService.class);
        // passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // setting a unique Uri to the intent
        // don't know its purpose to me right now
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        // setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(appWidgetId, R.id.lv_widget_list, svcIntent);
        // setting an empty view in case of no data
        // remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);

        return remoteViews;
    }

    /*
     * It receives the broadcast as per the action set on intent filters on
     * Manifest.xml once data is fetched from RemotePostService,it sends
     * broadcast and WidgetProvider notifies to change the data the data change
     * right now happens on ListProvider as it takes RemoteFetchService
     * listItemList as data
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // Action envoyé par le service de téléchargement lorsqu'il est terminé
        if (intent.getAction().equals(DATA_FETCHED)) {
            //Log.i("###", "onReceived fetch");
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            // Mise à jour de la vue avec liste
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteViews);
            // On met à jour le contenu de la liste
            AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_widget_list);
        }

    }
}
