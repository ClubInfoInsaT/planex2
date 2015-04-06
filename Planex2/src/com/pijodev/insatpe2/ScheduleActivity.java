package com.pijodev.insatpe2;


import android.app.Activity;
import android.os.Bundle;

public class ScheduleActivity extends Activity {

	WeekView mScheduleView;
	ToolBarView mToolBarView;
	Schedule mActiveSchedule;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		
		initialize();
		
		loadViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
	}
	
	/** Initialise les classes statiques **/
	private void initialize() {
		Dimens.initialize(this);
		DetailPopUpView.initialize(this);
	}
	
	/** Initialise les vues composants l'interface **/
	private void loadViews() {
		mToolBarView = new ToolBarView(this);
		mScheduleView = new WeekView(this);
		mToolBarView.setPrevButtonState(false);
		mToolBarView.setCurrentButtonState(false);
		mToolBarView.setNextButtonState(true);
	}
}
