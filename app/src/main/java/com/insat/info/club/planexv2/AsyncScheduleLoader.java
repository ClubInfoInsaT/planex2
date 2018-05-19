package com.insat.info.club.planexv2;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.SparseArray;

/**
 * Gère la chargement asynchrone et thread-safe des données depuis l'Internet
 * @author Proïd
 *
 */
public class AsyncScheduleLoader extends AsyncTask<ScheduleRequest, Void, Schedule> /*TODO change that*/ {
	private ScheduleActivity mActivity;
	
	public AsyncScheduleLoader(ScheduleActivity activity) {
		mActivity = activity;
	}
	
	@Override
	protected Schedule doInBackground(ScheduleRequest... params) {
		ScheduleRetriever sr = new ScheduleRetriever();
		ArrayList<Integer> groups = params[0].getGroups();
		int week = params[0].getRelWeek()+DateUtils.getCurrentWeek();
		String data = null;
		
		// Téléchargement des données si nécessaire
		if(!params[0].useOnlyCache())
			data = sr.download(groups, week);
		
		if(isCancelled())
			return null;
		
		boolean diff = false;
		if(data != null) {
			// Parsing du fichier obtenu
			SparseArray<WeekEntries> we = sr.parse(data, groups, week);
			
			if(we != null) {
				// Comparaison avec les données du cache et mise en cache
				diff = false;
				for(int i = 0; i < we.size(); i++) {
					if(params[0].showOnlyUpdate())
					if(!diff && !we.valueAt(i).equals(Cache.getWeekEntries(week, we.keyAt(i), mActivity)))
						diff = true;
					Cache.putWeekEntries(we.valueAt(i), week, we.keyAt(i), mActivity);
				}
			}
		}
		
		// Création de l'emploi du temps de la semaine
		Schedule schedule = new Schedule(params[0], mActivity, diff || !params[0].showOnlyUpdate());
		 
		return schedule;
	}
	
	/** Fonction appelée dans le thread de la GUI lorsque le chargement est terminé **/
	@Override
	protected void onPostExecute(Schedule result) {
		mActivity.updateSchedule(result);
	}
}
