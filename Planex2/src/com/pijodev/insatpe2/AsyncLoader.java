package com.pijodev.insatpe2;

import android.os.AsyncTask;

public class AsyncLoader extends AsyncTask<UserSession, Void, Schedule> /*TODO change that*/ {
	private ScheduleActivity mActivity;
	
	public AsyncLoader(ScheduleActivity activity) {
		mActivity = activity;
	}
	
	@Override
	protected Schedule doInBackground(UserSession... params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void onPostExecute(Schedule result) {
		// TODO Auto-generated method stub
		// Save in the cache
		// display on GUI
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		// ?
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
		// TODO Auto-generated method stub
		onCancelled(null);
	}
}
