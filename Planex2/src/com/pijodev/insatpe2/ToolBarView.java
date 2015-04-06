package com.pijodev.insatpe2;


import android.widget.ImageButton;
import android.widget.LinearLayout;

public class ToolBarView {
	/** ViewGroup principal de la barre d'outils **/
	private LinearLayout mLayout;
	
	private ImageButton mPrevButton;
	private ImageButton mNextButton;
	private ImageButton mCurrentButton;
	
	public ToolBarView(ScheduleActivity activity) {
		mLayout = (LinearLayout) activity.findViewById(R.id.ll_settings);
		
		mPrevButton = (ImageButton) mLayout.findViewById(R.id.ib_prev);
		mNextButton = (ImageButton) mLayout.findViewById(R.id.ib_next);
		mCurrentButton = (ImageButton) mLayout.findViewById(R.id.ib_current);
		
		// TODO groupButton(s)
		
		// TODO setOnClickListener
	}
	
	/** Change l'état du bouton 'semaine précédante' (enabled/disabled) **/
	public void setPrevButtonState(boolean enabled) {
		mPrevButton.setEnabled(enabled);
	}
	
	/** Change l'état du bouton 'semaine précédante' (enabled/disabled) **/
	public void setNextButtonState(boolean enabled) {
		mNextButton.setEnabled(enabled);
	}
	
	/** Change l'état du bouton 'semaine précédante' (enabled/disabled) **/
	public void setCurrentButtonState(boolean enabled) {
		mCurrentButton.setEnabled(enabled);
	}
}
