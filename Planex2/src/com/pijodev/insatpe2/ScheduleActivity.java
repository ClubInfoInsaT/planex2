package com.pijodev.insatpe2;


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import com.pijodev.insatpe2.UserSession.OnParamsChangedListener;
/**
 * 
 * @author Proïd
 *
 */
public class ScheduleActivity extends Activity implements OnParamsChangedListener {

	private WeekView mScheduleView;
	private ToolBarView mToolBarView;
	
	private AsyncScheduleLoader mLoader;
	
	private UserSession mSession;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		
		initialize();

		mSession = new UserSession(this);
		mSession.setOnParamsChangedListener(this);
		
		loadViews();
		
		initSchedule();
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
	
	
	private boolean mIsScrollInitialized = false;
	/** Contient les initialisations à faire seulement une fois que les vues sont visibles **/
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// On centre la vue sur le jour actuel, seulement au lancement de l'appli
		if(hasFocus && !mIsScrollInitialized) {
			mScheduleView.centerOnDay(DateUtils.getDayOfWeek());
			mIsScrollInitialized = true;
		}
	}

	/** Fonctione appelée lorsque l'orientation de l'écran change **/
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		mScheduleView.changeOrientation(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE);
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

	/** Fonction appelée lorsque les paramètres de l'emploi du temps changent (semaine ou groupes) **/
	@Override
	public void onParamsChanged(UserSession session, boolean weekChanged, boolean groupsChanged) {
		if(mLoader != null)
			mLoader.cancel(true);

		mToolBarView.updateCurrentButtonState();
		
		ScheduleRequest request = mSession.createRequest();
		mLoader = new AsyncScheduleLoader(this);
		mLoader.execute(request);
		mScheduleView.displayLoadingViews(request);
		
	}
	
	/** Affiche l'emploi du temps **/
	public void updateSchedule(Schedule schedule) {
		if(schedule.mustBeShown())
			mScheduleView.showSchedule(schedule, true);
		if(!schedule.getRequest().concealCautionMessage() || schedule.getRequest().forceCautionMessage())
			schedule.showCacheWarning(this);
	}
	
	/** Affiche l'emploi du temps de la semaine actuelle avec une requête du cache puis une requête internet **/
	public void initSchedule() {
		// Avec le cache (rapide)
		ScheduleRequest request = mSession.createRequest();
		request.pleaseUseOnlyCache();
		request.pleaseConcealCautionMessage();
		mLoader = new AsyncScheduleLoader(this);
		mLoader.execute(request);
		
		if(mSession.getGroups().size() == 0)
			return;
		
		// Avec internet, on met à jour l'affichage que si il y a des mises à jour
		request = mSession.createRequest();
		request.pleaseShowOnlyUpdate();
		request.pleaseForceCautionMessage();
		mLoader = new AsyncScheduleLoader(this);
		mLoader.execute(request);
	}
}
