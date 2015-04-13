package com.pijodev.insatpe2;

import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awprog.view.IXYScrollView;
import com.awprog.view.IXYScrollView.OnScrollChangedListener;

/**
 * 
 * @author Proïd
 *
 */
public class WeekView {
	// Scroll view principale
	private IXYScrollView mScrollView;
	// Barre supérieure des jours
	private ViewGroup mDayBar;
	// Colonnes des jours
	private RelativeLayout[] mColumnDay = new RelativeLayout[5];
	// Intitulé des colonnes : jour de la semaine + date
	private TextView[] mTitleDay = new TextView[5];
	
	/** Initialise les views **/
	public WeekView(ScheduleActivity activity) {
		mScrollView = (IXYScrollView) activity.findViewById(R.id.sv_week);
		mDayBar = (ViewGroup) activity.findViewById(R.id.ll_day);

		enableDayBarAutoScroll();
		
		mColumnDay[0] = (RelativeLayout) mScrollView.findViewById(R.id.rl_column_monday);
		mColumnDay[1] = (RelativeLayout) mScrollView.findViewById(R.id.rl_column_tuesday);
		mColumnDay[2] = (RelativeLayout) mScrollView.findViewById(R.id.rl_column_wednesday);
		mColumnDay[3] = (RelativeLayout) mScrollView.findViewById(R.id.rl_column_thursday);
		mColumnDay[4] = (RelativeLayout) mScrollView.findViewById(R.id.rl_column_friday);
		
		mTitleDay[0] = (TextView) mDayBar.findViewById(R.id.ll_monday).findViewById(R.id.tv_date);
		mTitleDay[1] = (TextView) mDayBar.findViewById(R.id.ll_tuesday).findViewById(R.id.tv_date);
		mTitleDay[2] = (TextView) mDayBar.findViewById(R.id.ll_wednesday).findViewById(R.id.tv_date);
		mTitleDay[3] = (TextView) mDayBar.findViewById(R.id.ll_thursday).findViewById(R.id.tv_date);
		mTitleDay[4] = (TextView) mDayBar.findViewById(R.id.ll_friday).findViewById(R.id.tv_date);
		
		// TODO remove
		setDates();
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
		// TODO
	}
	
	/** Affiche des icones de chargement dans les colonnes **/
	public void displayLoadingViews() {
		// TODO
	}
	
	/** Supprime le contenu des colonnes **/
	private void clearColumns() {
		// TODO
	}
	
	/** Ajoute une liste de cours dans les colonnes **/
	private void fillColumns(boolean animation) {
		// TODO
	}
	
	/** Met à jour l'intitulé des colonnes avec la bonne date **/
	private void setDates() {
		// TODO
		String day[] = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
		for(int i = 0; i < 5; i++) {
			mTitleDay[i].setText(day[i] + "\t01.01.1970");
		}
	}
	
	/** Animation d'apparition d'un élément **/
	Animation mItemArrivalAnimation;
	private Animation getItemArrivalAnimation() {
		mItemArrivalAnimation = new AlphaAnimation(0, 1);
		mItemArrivalAnimation.setDuration(400);
		mItemArrivalAnimation.setFillBefore(true);
    	
    	return mItemArrivalAnimation;
	}
	
	/** Animation d'apparition d'un ensemble d'éléments dans une colonne **/
	private LayoutAnimationController getColumnFillingAnimation() {
		LayoutAnimationController layoutAnim = new LayoutAnimationController(getItemArrivalAnimation());
    	layoutAnim.setOrder(LayoutAnimationController.ORDER_RANDOM);
    	layoutAnim.setDelay(0.25f);
    	
    	return layoutAnim;
	}
}
