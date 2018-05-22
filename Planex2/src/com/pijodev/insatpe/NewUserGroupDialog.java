package com.pijodev.insatpe;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.pijodev.insatpe.R;

/**
 * Boîte de dialogue permettant de créer un nouveau groupe utilisateur
 * à ajouter dans la liste de groupe
 * @author Proïd
 *
 */
public class NewUserGroupDialog implements OnClickListener {
	private AlertDialog mDialog;
	private EditText mName;
	private EditText mID;
	private OnNewUserGroupCreatedListener mListener;
	private Context mContext;
	
	public NewUserGroupDialog(Context context, OnNewUserGroupCreatedListener listener) {
		mContext = context;
		mDialog = build(context);
		mListener = listener;
	}
	
	/** Affiche la fenêtre **/
	public void show() {
		mName.setText("");
		mID.setText("");
		mDialog.show();
	}
	
	/** Crée la fenêtre et son contenu **/
	@SuppressLint("InflateParams") 
	private AlertDialog build(Context context) {
		Builder builder = new Builder(context);
		ViewGroup view = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.new_user_group, null);
		mName = (EditText) view.getChildAt(0);
		mID = (EditText) view.getChildAt(1);
		builder.setView(view);
		
		builder.setTitle("Nouveau groupe");
		builder.setPositiveButton("Ajouter", this);
		builder.setNegativeButton("Annuler", null);
		
		return builder.create();
	}
	
	public interface OnNewUserGroupCreatedListener {
		/** Cette fonction est appelée après l'ajout avec succès
		 * d'un nouveau groupe utilisateur dans GroupList **/
		void onNewUserGroupCreated(String name, int id);
		/** Cette fonction est appelée après l'échec de la création d'un groupe utilisateur. **/
		void onNewUserGroupFailed(String name, Integer id);
	}

	/** Transforme un String en entier, retourne -1 si non valide **/
	private static int getIDFromString(String s) {
		try {
			return Integer.parseInt(s);
		} catch(NumberFormatException e) {
			return -1;
		}
	}
	/** Normalise le String (enlève des espaces), retourne null si non valide **/
	private static String getNameFromString(String s) {
		if(s.length() == 0)
			return null;

		// TODO avec des regexp, c'est plus propre, non ?
		// on enlève les espaces de début et fin
		while(s.startsWith(" "))
			s = s.substring(1);
		while(s.endsWith(" "))
			s = s.substring(0, s.length()-1);
		// et les espaces multiples
		while((s = s.replace("  ", " ")).contains("  "));
		
		return (s.length() == 0) ? null : s;
	}

	/** Fonction appelée lorsqu'on clique sur le bouton 'Ajouter' **/
	@Override
	public void onClick(DialogInterface dialog, int which) {
		int id = getIDFromString(mID.getText().toString());
		String name = getNameFromString(mName.getText().toString());
		
		if(id == -1) {
			MyToast.show(mContext, "Numéro invalide", Toast.LENGTH_SHORT);
			if(mListener != null)
				mListener.onNewUserGroupFailed(null, null);
			return;
		}
		
		if(name == null) {
			Toast.makeText(mContext, "Nom invalide", Toast.LENGTH_SHORT).show();
			if(mListener != null)
				mListener.onNewUserGroupFailed(null, null);
			return;
		}
		
		try {
			GroupList.addUserGroup(name, id);
		} catch (Exception e) {
			Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
			if(mListener != null)
				mListener.onNewUserGroupFailed(name, id);
			return;
		}
		
		if(mListener != null)
			mListener.onNewUserGroupCreated(name, id);
	}
}
