package com.pijodev.insatpe2;

import java.util.GregorianCalendar;

import android.graphics.Matrix;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.pijodev.insatpe2.Schedule.ScheduleEntry;
import com.pijodev.insatpe2.XYScrollView.OnPullListener;
import com.pijodev.insatpe2.XYScrollView.OnScrollChangedListener;

/**
 * Gestion de l'interface liée au tableau de la semaine.
 * Animations, pull-to-change
 * 
 * @author Proïd
 *
 */
public class WeekView {
	/** Scroll view principale **/
	private XYScrollView mScrollView;
	/** Barre supérieure des jours **/
	private LinearLayout mDayBar;
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
	/** Icones de changement de semaine **/
	private ImageView mArrowLeft, mArrowRight;
	
	/** Initialise les views **/
	public WeekView(ScheduleActivity activity) {
		mScrollView = (XYScrollView) activity.findViewById(R.id.sv_week);
		mDayBar = (LinearLayout) activity.findViewById(R.id.ll_day);
		
		mLayoutAnimation = createColumnFillingAnimation();

		enableDayBarAutoScroll();
		enablePullToChangeWeek(activity);
		
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

		mArrowLeft = (ImageView) activity.findViewById(R.id.iv_prev);
		mArrowRight = (ImageView) activity.findViewById(R.id.iv_next);
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
				mDayBar.scrollTo(x, 0);
			}
		});
	}
	
	/** Active la fonctionnalité pul to change week (~pull to refresh)  **/
	private void enablePullToChangeWeek(ScheduleActivity activity) {
		final UserSession session = activity.getSession();
		final float pullDistance = Dimens.columnWidth*0.7f, startDistance = pullDistance * 0.1f;
		
		mScrollView.setOnPullListener(new OnPullListener() {
			/** Fonction appelée lorsque le début d'un nouveau pull est détecté **/
			@SuppressWarnings("deprecation") // setAlpha(int)
			@Override
			public void onPullStarted(boolean right) {
				// annulation de l'animation précédante
				if(right) {
					mArrowRight.clearAnimation();
					RelativeLayout.LayoutParams lp = (LayoutParams) mArrowRight.getLayoutParams();
					lp.leftMargin = 0;
					mArrowRight.setLayoutParams(lp);
					mArrowRight.setImageMatrix(new Matrix());
					mArrowRight.setAlpha(255);
				}
				else {
					mArrowLeft.clearAnimation();
					RelativeLayout.LayoutParams lp = (LayoutParams) mArrowLeft.getLayoutParams();
					lp.rightMargin = 0;
					mArrowLeft.setLayoutParams(lp);
					mArrowLeft.setImageMatrix(new Matrix());
					mArrowLeft.setAlpha(255);
				}
			}
			/** Fonction appelée lorsqu'on relève le doigt, retourne vrai si le pull est valide **/
			@SuppressWarnings("deprecation") // setAlpha(int)
			@Override
			public boolean onPullReleased(float dist, boolean right, boolean cancelled) {
				boolean pullDone = false;
				AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
				if(!cancelled && dist > pullDistance) {
					pullDone = true;
					// Pull valide : on change de semaine
					session.addRelWeek(right ? +1 : -1);
					mScrollView.fullScroll(right ? View.FOCUS_LEFT : View.FOCUS_RIGHT);
					// animation de disparition plus longue
					anim.setDuration(350);
					anim.setStartOffset(250);
				} else {
					// Animation de disparition rapide
					anim.setDuration(150);
				}
				
				if(right) {
					// Lorsque l'animation est terminé, on réinitialise l'état de la flèche
					anim.setAnimationListener(new AnimationListener() {
						@Override public void onAnimationStart(Animation animation) {}
						@Override public void onAnimationRepeat(Animation animation) {}
						@Override public void onAnimationEnd(Animation animation) {
							RelativeLayout.LayoutParams lp = (LayoutParams) mArrowRight.getLayoutParams();
							lp.leftMargin = 0;
							mArrowRight.setLayoutParams(lp);
							mArrowRight.setImageMatrix(new Matrix());
							mArrowRight.setAlpha(255);
						}
					});
					// On lance l'animation
					mArrowRight.startAnimation(anim);
				} else {
					// Lorsque l'animation est terminé, on réinitialise l'état de la flèche
					anim.setAnimationListener(new AnimationListener() {
						@Override public void onAnimationStart(Animation animation) {}
						@Override public void onAnimationRepeat(Animation animation) {}
						@Override public void onAnimationEnd(Animation animation) {
							RelativeLayout.LayoutParams lp = (LayoutParams) mArrowLeft.getLayoutParams();
							lp.rightMargin = 0;
							mArrowLeft.setLayoutParams(lp);
							mArrowLeft.setImageMatrix(new Matrix());
							mArrowLeft.setAlpha(255);
						}
					});
					// On lance l'animation
					mArrowLeft.startAnimation(anim);
				}
				return pullDone;
			}
			/** Fonction appelér lorsqu'on bouge le doigt **/
			@SuppressWarnings("deprecation") // setAlpha(int) 
			@Override
			public void onPullDistanceChanged(float dist, boolean right) {
				// Données de transformation
				Matrix matrix = new Matrix();
				float k = Math.max(dist - startDistance, 0.0f) / (pullDistance - startDistance);
				
				final float exp = 1.0f / 3.0f;
				// Translation
				float translate = (float) (k < 3 ? Math.pow(k, exp) : Math.pow(3, exp)+(k-3)*Math.pow(3, exp-1)/3);
				// angle de rotation
				//float foggle = 180 * (1.0f - Math.min(Math.max(k-0.2f, 0.0f)/0.8f, 1.0f));
				// transparence
				int alpha = (int)((k < 1 ? k*0.5f : 1.0f) * 255);
				
				if(right) {
					// Translation
					RelativeLayout.LayoutParams lp = (LayoutParams) mArrowRight.getLayoutParams();
					lp.leftMargin = (int) (-pullDistance / 3 * translate);
					mArrowRight.setLayoutParams(lp);
					// Rotation
					//matrix.postRotate(-foggle, imgWidth*0.5f, imgHeight*0.5f);
					// recadrage
					mArrowRight.setImageMatrix(matrix);
					// Transparence
					mArrowRight.setAlpha(alpha);
				} else {
					// Translation
					RelativeLayout.LayoutParams lp = (LayoutParams) mArrowLeft.getLayoutParams();
					lp.rightMargin = (int) (-pullDistance / 3 * translate);
					mArrowLeft.setLayoutParams(lp);
					// Rotation
					//matrix.postRotate(foggle, imgWidth*0.5f, imgHeight*0.5f);
					// recadrage
					int imgWidth = mArrowRight.getDrawable().getBounds().width();
					matrix.postTranslate(mArrowLeft.getWidth()-imgWidth, 0);
					mArrowLeft.setImageMatrix(matrix);
					// Transparence
					mArrowLeft.setAlpha(alpha);
				}
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
				mLoadingView[i].setIndeterminateDrawable(mLoadingView[i].getContext().getResources().getDrawable(R.drawable.loader_anim2));
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

	
	/** Orientation actuelle **/
	private boolean mIsInLandscapeMode = false;
	/** Change les dimensions des vues en fonction de l'orientation de l'écran **/
	public void changeOrientation(boolean landscapeMode) {
		if(mIsInLandscapeMode == landscapeMode)
			return;
		EasterEggs.ee4(mColumnDay);
		/*
		final int columnID[] = {R.id.rl_column_monday, R.id.rl_column_tuesday, R.id.rl_column_wednesday, R.id.rl_column_thursday, R.id.rl_column_friday};
		Log.i("###", ""+mScrollView.getWidth()+" "+mScrollView.getHeight());
		// Mode paysage : redimensionnement des colonnes afin qu'elles
		// soient toutes visibles, et modification de la hauteur pour voir
		// tout l'intervalle 8h-18h15
		if(landscapeMode) {
			for(int i = 0; i < 5; i++) {
				// Barre des jours
				View v = mDayBar.getChildAt(i);
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v.getLayoutParams();
				lp.width = 45;
				v.setLayoutParams(lp);
				// Colonnes des jours
				v = mScrollView.findViewById(columnID[i]);
				lp = (LinearLayout.LayoutParams) v.getLayoutParams();
				lp.width = 45;
				v.setLayoutParams(lp);
			}
		}
		// Mode portrait : vue par défaut
		else {
			for(int i = 0; i < 5; i++) {
				// Barre des jours
				View v = mDayBar.getChildAt(i);
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v.getLayoutParams();
				lp.width = Dimens.columnWidth;
				v.setLayoutParams(lp);
				// Colonnes des jours
				v = mScrollView.findViewById(columnID[i]);
				lp = (LinearLayout.LayoutParams) v.getLayoutParams();
				lp.width = Dimens.columnWidth;
				v.setLayoutParams(lp);
			}
		}*/
			
		mIsInLandscapeMode = landscapeMode;
	}

}
