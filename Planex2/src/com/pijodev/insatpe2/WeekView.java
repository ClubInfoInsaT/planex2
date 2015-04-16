package com.pijodev.insatpe2;

import java.util.GregorianCalendar;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.awprog.view.IXYScrollView;
import com.awprog.view.IXYScrollView.OnScrollChangedListener;
import com.pijodev.insatpe2.Schedule.ScheduleEntry;

/**
 * 
 * @author Proïd
 *
 */
public class WeekView {
	/** Scroll view principale **/
	private IXYScrollView mScrollView;
	/** Barre supérieure des jours **/
	private ViewGroup mDayBar;
	/** Colonnes des jours **/
	private RelativeLayout[] mColumnDay = new RelativeLayout[5];
	/** Icone animée de chargement **/
	private ProgressBar[] mLoadingView = new ProgressBar[5];
	/** Intitulé des colonnes : jour de la semaine + date **/
	private TextView[] mTitleDay = new TextView[5];
	/** Animation d'apparition d'un ensemble d'éléments dans une colonne **/
	private LayoutAnimationController mLayoutAnimation;
	/** Visibilité des icones de chargement **/
	private boolean mLoadingViewEnabled = false;
	
	/** Initialise les views **/
	public WeekView(ScheduleActivity activity) {
		mScrollView = (IXYScrollView) activity.findViewById(R.id.sv_week);
		mDayBar = (ViewGroup) activity.findViewById(R.id.ll_day);
		
		mLayoutAnimation = createColumnFillingAnimation();

		enableDayBarAutoScroll();
		
		mColumnDay[0] = (RelativeLayout) mScrollView.findViewById(R.id.rl_column_monday);
		mColumnDay[1] = (RelativeLayout) mScrollView.findViewById(R.id.rl_column_tuesday);
		mColumnDay[2] = (RelativeLayout) mScrollView.findViewById(R.id.rl_column_wednesday);
		mColumnDay[3] = (RelativeLayout) mScrollView.findViewById(R.id.rl_column_thursday);
		mColumnDay[4] = (RelativeLayout) mScrollView.findViewById(R.id.rl_column_friday);
		for(int i = 0; i < 5; i++)
			mColumnDay[i].setLayoutAnimation(mLayoutAnimation);
		
		mTitleDay[0] = (TextView) mDayBar.findViewById(R.id.ll_monday).findViewById(R.id.tv_date);
		mTitleDay[1] = (TextView) mDayBar.findViewById(R.id.ll_tuesday).findViewById(R.id.tv_date);
		mTitleDay[2] = (TextView) mDayBar.findViewById(R.id.ll_wednesday).findViewById(R.id.tv_date);
		mTitleDay[3] = (TextView) mDayBar.findViewById(R.id.ll_thursday).findViewById(R.id.tv_date);
		mTitleDay[4] = (TextView) mDayBar.findViewById(R.id.ll_friday).findViewById(R.id.tv_date);
	}
	
	/** Centre la vue sur le jour donné **/
	public void centerOnDay(int day) {
		int colWidth = mColumnDay[0].getWidth();
		int screenWidth = mScrollView.getWidth();
		int position = day * colWidth + (day-1) * Dimens.columnRightMargin - (screenWidth - colWidth) / 2;
		int scroll = Math.min(Math.max(0, position),  colWidth*5+Dimens.columnRightMargin*4-screenWidth);
		mScrollView.scrollTo(scroll, 0);
	}
	
	/** Permet à la barre des jours de s'aligner automatiquement avec le scrollView **/
	private void enableDayBarAutoScroll() {
		mScrollView.setOnScrollChangedListener(new OnScrollChangedListener() {
			@Override
			public void onScrollChanged(int x, int y, int oldX, int oldY) {
				//if(oldX != x)
					mDayBar.scrollTo(x, 0);
			}
		});
	}
	
	/** Affiche le contenu d'un emploi du temps dans la vue **/
	public void showSchedule(Schedule schedule, boolean anim) {
		clearColumns();
		// Remplissage des colonnes
		fillColumns(schedule, anim);
		// mise à jour des dates de la barre supérieure
		setDates(schedule.getRequest().getRelWeek());
	}
	
	/** Affiche des icones de chargement dans les colonnes **/
	public void displayLoadingViews(ScheduleRequest request) {
		// mise à jour des dates de la barre supérieure
		setDates(request.getRelWeek());
		
		// Vues déjà affichées
		if(mLoadingViewEnabled)
			return;
		
		clearColumns();
		for(int i = 0; i < 5; i++) {
			if(mLoadingView[i] == null) {
				mLoadingView[i] = new ProgressBar(mColumnDay[i].getContext());
				RelativeLayout.LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				lparams.topMargin = Dimens.loadingViewVerticalPosition;
				mLoadingView[i].setLayoutParams(lparams);
				mLoadingView[i].setIndeterminateDrawable(mLoadingView[i].getContext().getResources().getDrawable(R.drawable.loader_anim));
			}
			mColumnDay[i].addView(mLoadingView[i]);
			mColumnDay[i].startLayoutAnimation();
		}
		
		mLoadingViewEnabled = true;
	}
	
	/** Supprime le contenu des colonnes **/
	private void clearColumns() {
		for(int i = 0; i < 5; i++) {
			RelativeLayout rl = mColumnDay[i];
			// Annulation des animations en cours
			if(mLoadingView[i] != null)
				mLoadingView[i].clearAnimation();
			rl.setLayoutAnimation(null);
			rl.setLayoutAnimation(mLayoutAnimation);
			rl.removeAllViews();
		}
		mLoadingViewEnabled = false;
	}
	
	/** Ajoute une liste de cours dans les colonnes **/
	private void fillColumns(Schedule schedule, boolean animation) {
		for(int i = 0; i < 5; i++) {
			for(ScheduleEntry se : schedule.getEntries(i)) {
				View view = new EntryView(mColumnDay[i].getContext(), se);
				mColumnDay[i].addView(view);
			}
			if(animation)
				mColumnDay[i].startLayoutAnimation();
		}
	}
	
	/** Met à jour l'intitulé des colonnes avec la bonne date **/
	private void setDates(int relWeek) {
		String day[] = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
		
		for(int i = 0; i < 5; i++) {
			GregorianCalendar gc = DateUtils.getDayOfWeek(relWeek, i);
			int d = gc.get(GregorianCalendar.DAY_OF_MONTH);
			int m = gc.get(GregorianCalendar.MONTH);
			int y = gc.get(GregorianCalendar.YEAR);
			
			mTitleDay[i].setText(day[i] + "\t"+String.format("%02d.%02d.%d", d, m+1, y));
		}
	}
	
	/** Animation d'apparition d'un élément **/
	private Animation createItemArrivalAnimation() {
		// Transparence
		Animation alphaAnim = new AlphaAnimation(0.0f, 1);
		alphaAnim.setDuration(400);
		alphaAnim.setFillBefore(true);
		// + Zoom
		Animation scaleAnim = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnim.setDuration(200);
		scaleAnim.setFillBefore(true);
		// = Awesome animation
		AnimationSet awesomeAnim = new AnimationSet(false/*shareinterpolator*/);
		awesomeAnim.addAnimation(scaleAnim);
		awesomeAnim.addAnimation(alphaAnim);
		awesomeAnim.setFillAfter(false);
		
    	return awesomeAnim;
	}
	
	/** Animation d'apparition d'un ensemble d'éléments dans une colonne **/
	private LayoutAnimationController createColumnFillingAnimation() {
		LayoutAnimationController layoutAnim = new LayoutAnimationController(createItemArrivalAnimation());
    	layoutAnim.setOrder(LayoutAnimationController.ORDER_RANDOM);
    	layoutAnim.setDelay(0.25f);
    	
    	return layoutAnim;
	}
}
