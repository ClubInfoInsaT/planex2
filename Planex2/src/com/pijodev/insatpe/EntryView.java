package com.pijodev.insatpe;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pijodev.insatpe.Schedule.ScheduleEntry;


/**
 * Représentation graphique d'un cours
 * @author Proïd
 *
 */
public class EntryView extends RelativeLayout {
	/** Référence vers le ScehduleEntry associé à cette vue **/
	private ScheduleEntry mSEntry;
	private TextView[] mTextView = new TextView[4];
	
	/** Construit la vue représentant de l'élément entry **/
	public EntryView(Context context, final ScheduleEntry sentry) {
		super(context);
		mSEntry = sentry;
		
		// Positionnement
		setLayoutParams(getLayoutParams(sentry));
		
		// Couleur d'arrière plan
		setBackgroundColor(sentry.entry.getColor() == 0xffffffff ? 0xfff8f8f8 : sentry.entry.getColor());
		
		// Contenu
		LinearLayout ll = new LinearLayout(context);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		ll.setGravity(Gravity.CENTER);
		ll.setOrientation(LinearLayout.VERTICAL);
		addView(ll);

		// Création des textview
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    	
    	// Nom du cours
	    TextView tv_class = new TextView(context);
		tv_class.setText(sentry.entry.getSummary());
		tv_class.setLayoutParams(params);
		tv_class.setTextColor(0xFF000000);
		tv_class.setTextSize(TypedValue.COMPLEX_UNIT_PX, Dimens.entryTextSize);
		tv_class.setGravity(Gravity.CENTER);
		tv_class.setTypeface(null, Typeface.BOLD);
		tv_class.setSingleLine(true);
		tv_class.setEllipsize(TruncateAt.END);
		ll.addView(tv_class);
		mTextView[0] = tv_class;
	   
		// Salle
		if(sentry.entry.hasRoom()) {
			TextView tv_room = new TextView(context);
			tv_room.setText(sentry.entry.getRoom());
			tv_room.setLayoutParams(params);
			tv_room.setTextColor(0xff000000);
			tv_room.setTextSize(TypedValue.COMPLEX_UNIT_PX, Dimens.entryTextSize);
			tv_room.setGravity(Gravity.CENTER);
			tv_room.setSingleLine(true);
			tv_room.setEllipsize(TruncateAt.END);
			ll.addView(tv_room);
			mTextView[1] = tv_room;
		}
		
		// Heure
		TextView tv_hour = new TextView(context);
		tv_hour.setText(sentry.entry.getStringTime());
		tv_hour.setLayoutParams(params);
		tv_hour.setTextColor(0xff000000);
		tv_hour.setTextSize(TypedValue.COMPLEX_UNIT_PX, Dimens.entryTextSize);
		tv_hour.setGravity(Gravity.CENTER);
		tv_hour.setSingleLine(true);
		tv_hour.setEllipsize(TruncateAt.END);
		ll.addView(tv_hour);
		mTextView[2] = tv_hour;
		
		// Logo : numéro correpondant aux groupes secondaires
		String gidTxt = sentry.groupRefToString();
		if(gidTxt != null) {
			TextView tv_groupId = new TextView(context);
			tv_groupId.setBackgroundColor(0xAA444444);
			tv_groupId.setTextColor(0xAAffffff);
			tv_groupId.setTextSize(TypedValue.COMPLEX_UNIT_PX, Dimens.entryTextSize*3/4);
			tv_groupId.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
			tv_groupId.setText(gidTxt);
			tv_groupId.setPadding(1, 0, 1, 0);
			// positionné dans l'angle inférieur droit de la vue
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			tv_groupId.setLayoutParams(lp);
			addView(tv_groupId);
			mTextView[3] = tv_groupId;
		}
		
		
		// On affiche le pop-up lorsqu'on clique sur la vue
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DetailPopUpView.show(sentry);
			}
		});
	}
	
	public void updateDimens() {
		setLayoutParams(getLayoutParams(mSEntry));
		for(int i = 0; i < 3; i++)
			if(mTextView[i] != null)
				mTextView[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, Dimens.entryTextSize);
		if(mTextView[3] != null)
			mTextView[3].setTextSize(TypedValue.COMPLEX_UNIT_PX, Dimens.entryTextSize*3/4);
	}
	
	/** Layout params pour positionner l'entry correctement dans la colonne **/
	private LayoutParams getLayoutParams(ScheduleEntry sentry) {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		// Position et dimension en largeur
		params.leftMargin = (int) (sentry.getMeasuredPosition() * Dimens.columnWidth);
		params.rightMargin = (int) ((1-(sentry.getMeasuredPosition()+sentry.getMeasuredWidth())) * Dimens.columnWidth);
		// hauteur
		params.topMargin = (int) (sentry.entry.getStartTime() * Dimens.hourHeight / 60);
    	params.height = (int) ((sentry.entry.getEndTime()-sentry.entry.getStartTime()) * Dimens.hourHeight / 60);
    	
		return params;
	}
}
