package com.pijodev.insatpe2;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


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
	
	static private LinearLayout mLayout;
	
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
	static public void show(Entry entry) {
		mTextClassName.setText(entry.getClassName());
		
		mTextDate.setText("Date : " + entry.getStringDate());
		
		mTextHour.setText("Durée : " + entry.getStringDuration());
		
		mTextRoomName.setVisibility(entry.hasRoomName() ? View.VISIBLE : View.GONE);
		mTextRoomName.setText("Salle : " + entry.getRoomName());
		
		mTextProfessor.setVisibility(entry.hasProfessorName() ? View.VISIBLE : View.GONE);
		mTextProfessor.setText("Enseignant : " + entry.getProfessorName());
		
		mLayout.setBackgroundColor(entry.getColor());
		
		mDialog.show();
	}
}
