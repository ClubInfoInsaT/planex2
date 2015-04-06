package com.pijodev.insatpe2;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EntryView extends RelativeLayout {
	
	TextView mGroupIdentifier;
	
	/** Construit la vue représentant de l'élément entry **/
	public EntryView(Context context, final Entry entry, int subdivideCount, int position) {
		super(context);
		
		// Positionnement
		setLayoutParams(getLayoutParams(entry, subdivideCount, position));
		
		// Couleur d'arrière plan
		setBackgroundColor(entry.getColor());
		
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
		tv_class.setText(entry.getClassName());
		tv_class.setLayoutParams(params);
		tv_class.setTextColor(0xFF000000);
		tv_class.setTextSize(Dimens.entryTextSize);
		tv_class.setGravity(Gravity.CENTER);
		tv_class.setTypeface(null, Typeface.BOLD);
		tv_class.setSingleLine(true);
		tv_class.setEllipsize(TruncateAt.END);
		ll.addView(tv_class);
	   
		// Salle
		if(entry.hasRoomName()) {
			TextView tv_room = new TextView(context);
			tv_room.setText(entry.getRoomName());
			tv_room.setLayoutParams(params);
			tv_room.setTextColor(0xff000000);
			tv_room.setTextSize(Dimens.entryTextSize);
			tv_room.setGravity(Gravity.CENTER);
			tv_room.setSingleLine(true);
			tv_room.setEllipsize(TruncateAt.END);
			ll.addView(tv_room);
		}
		
		// Heure
		TextView tv_hour = new TextView(context);
		tv_hour.setText(entry.getStringTime());
		tv_hour.setLayoutParams(params);
		tv_hour.setTextColor(0xff000000);
		tv_hour.setTextSize(Dimens.entryTextSize);
		tv_hour.setGravity(Gravity.CENTER);
		tv_hour.setSingleLine(true);
		tv_hour.setEllipsize(TruncateAt.END);
		ll.addView(tv_hour);
		
		// Logo : numero correpondant aux groupes secondaires
		mGroupIdentifier = new TextView(context);
		mGroupIdentifier.setVisibility(View.GONE);
		mGroupIdentifier.setTextColor(0xffffff);
		mGroupIdentifier.setTextSize(Dimens.entryTextSize*3/4);
		mGroupIdentifier.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
		mGroupIdentifier.setBackgroundColor(0xff666666);
		// positionné dans l'angle inférieur droit de la vue
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.alignWithParent = true;
		lp.rightMargin = 1;
		lp.bottomMargin = 1;
		mGroupIdentifier.setLayoutParams(lp);
		addView(mGroupIdentifier);
		
		
		// On affiche le pop-up lorsqu'on clique sur la vue
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DetailPopUpView.show(entry);
			}
		});
	}
	
	/** Layout params pour positionner l'entry correctement dans la colonne **/
	private LayoutParams getLayoutParams(Entry entry, int subdivideCount, int position) {
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		// Position et dimension en largeur 
		params.leftMargin = position * Dimens.columnWidth / subdivideCount;
		params.rightMargin = (position+1) * Dimens.columnWidth / subdivideCount;
		// hauteur
		params.topMargin = entry.getStartTime() * Dimens.hourHeight / 60;
    	params.height = (entry.getEndTime()-entry.getStartTime()) * Dimens.hourHeight / 60;
    	
		return params;
	}
}
