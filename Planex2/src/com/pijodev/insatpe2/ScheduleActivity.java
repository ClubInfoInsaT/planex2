package com.pijodev.insatpe2;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.pijodev.insatpe2.UserSession.OnParamsChangedListener;
/**
 * 
 * @author Proïd
 *
 */
public class ScheduleActivity extends Activity implements OnParamsChangedListener {

	private WeekView mScheduleView;
	private ToolBarView mToolBarView;
	
	private AsyncLoader mLoader;
	
	private Schedule mActiveSchedule;
	private UserSession mSession;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		
		initialize();

		mSession = new UserSession(this);
		// TODO better please!
		mSession.setOnParamsChangedListener(this);
		
		loadViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// On enregistre les données du cache dès que l'on quitte l'appli
		Cache.save(this);
		mSession.save(this);
		GroupList.save(this);
	}
	
	/** Initialise les classes statiques **/
	private void initialize() {
		Dimens.initialize(this);
		DetailPopUpView.initialize(this);
		GroupList.load(this);
	}
	
	/** Initialise les vues composants l'interface **/
	private void loadViews() {
		mToolBarView = new ToolBarView(this);
		mScheduleView = new WeekView(this);
	}

	
	/** Retourne la session utilisateur **/
	public UserSession getSession() {
		return mSession;
	}

	/** Fonction appelée lorsque les paramètres changent (semaine ou groupes) **/
	@Override
	public void onParamsChanged(UserSession session, boolean weekChanged, boolean groupsChanged) {
		Log.i("###", "onParamsChanged(w:"+weekChanged+", g:"+groupsChanged+") "+session.getRelWeek()+" "+session.getGroups().toString());
	}
}
