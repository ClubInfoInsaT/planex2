package com.insat.info.club.planexv2;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.insat.info.club.planexv2.R;
import com.insat.info.club.planexv2.Schedule.ScheduleEntry;


/**
 * Petite fenêtre pour afficher les détails d'un cours
 * 
 * @author Proïd
 *
 */
public class DetailPopUpView {
	static private AlertDialog mDialog;
	static private TextView mTextClassName;
	static private TextView mTextProfessor;
	static private TextView mTextHour;
	static private TextView mTextRoomName;
	static private TextView mTextDate;
	static private TextView mTextGroups;
	
	static private LinearLayout mLayout;
	
	@SuppressLint("InflateParams")
	static public void initialize(Context context) {
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		/// Chargement de la vue depuis le fichier xml
		mLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.popup, null);
		
		// Références des textviews
		mTextClassName = (TextView) mLayout.findViewById(R.id.tv_popup_class);
		mTextProfessor = (TextView) mLayout.findViewById(R.id.tv_popup_professor);
		mTextHour = (TextView) mLayout.findViewById(R.id.tv_popup_hour);
		mTextRoomName = (TextView) mLayout.findViewById(R.id.tv_popup_room);
		mTextDate = (TextView) mLayout.findViewById(R.id.tv_popup_date);
		mTextGroups = (TextView) mLayout.findViewById(R.id.tv_popup_groups);
		
		// Configuration
		adb.setView(mLayout);
		
		mDialog = adb.create();
		
		// On ferme le pop-up lorsqu'on clique dessus
		mLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
	}
	
	/** Affiche une boîte de dialogue contenant les informations de l'élément donné **/
	static public void show(ScheduleEntry sentry) {
		Entry entry = sentry.entry;
		
		mTextClassName.setText(entry.getSummary());
		
		mTextDate.setText("Date : " + entry.getStringDate());
		
		mTextHour.setText("Durée : " + entry.getStringDuration());
		
		mTextRoomName.setVisibility(entry.hasRoom() ? View.VISIBLE : View.GONE);
		mTextRoomName.setText("Salle : " + entry.getRoom());
		
		mTextProfessor.setVisibility(entry.hasProfessor() ? View.VISIBLE : View.GONE);
		mTextProfessor.setText("Enseignant : " + entry.getProfessor());
		
		// Construction de la liste des noms des groupes associés
		String s = GroupList.getGroupName(sentry.getGroupId(sentry.getGroupRef().get(0)));
		for(int i = 1; i < sentry.getGroupRef().size(); i++)
			s += ", "+GroupList.getGroupName(sentry.getGroupId(sentry.getGroupRef().get(i)));
		mTextGroups.setText("Groupe"+(sentry.getGroupRef().size() == 1 ? "" : "s")+" : "+s);
		
		EasterEggs.ee5(entry, mLayout);
		
		mLayout.setBackgroundColor(entry.getColor());
		
		mDialog.show();
	}
}
