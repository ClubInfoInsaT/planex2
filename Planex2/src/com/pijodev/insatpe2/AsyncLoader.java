package com.pijodev.insatpe2;

import android.os.AsyncTask;

/**
 * Gère la chargement asynchrone et thread-safe des données depuis l'Internet
 * @author Proïd
 *
 */
public class AsyncLoader extends AsyncTask<UserSession, Void, Schedule> /*TODO change that*/ {
	private ScheduleActivity mActivity;
	
	public AsyncLoader(ScheduleActivity activity) {
		mActivity = activity;
	}
	
	@Override
	protected Schedule doInBackground(UserSession... params) {
		// TODO téléchargement des données
		// TODO parsing et mise en cache
		// TODO En cas d'erreur, chargement du cache
		// TODO création d'un Schedule
		return null;
	}
	
	@Override
	protected void onPostExecute(Schedule result) {
		// TODO Save in the cache
		// TODO display on GUI
	}
	
	/// TODO this function is not called on API < 11
	@Override
	protected void onCancelled(Schedule result) {
		// TODO Auto-generated method stub
		// save in cache if not null
		// do nothing on GUI
	}
	
	@Override
	protected void onCancelled() {
		onCancelled(null);
	}
}
