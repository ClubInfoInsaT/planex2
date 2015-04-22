package com.pijodev.insatpe2;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Contient les informations liées à la navigation actuelle,
 * aux préférences, à l'historiques, aux cartes bancaires et
 * à la vie privée de l'utilisateur. (It's a joke! lol.)
 * 
 * @author Proïd
 *
 */
public class UserSession {
	public static final String defaultSessionName = "user-default.data";
	
	/** Numéro des groupes actuellement visionnés **/
	private ArrayList<Integer> mGroups = new ArrayList<>();
	/** Numéro relatif de la semaine actuellement visionnée (par rapport à la semaine courante) **/
	private int mRelWeek; 
	/** Historique des groupes visités **/
	private ArrayList<Integer> mHistory = new ArrayList<>();
	/** Nom de la session (pour le fichier de sauvegarde) **/
	private String mName;
	/** Listener sur les changements de paramètres (groupes/semaine) **/
	private OnParamsChangedListener mParamsListener;
	
	/** Lance une nouvelle session à partir d'un nom de session
	 * à partir duquel on charge les fichiers de préférences.
	 * Nom de session différent pour les widgets par exemple **/
	UserSession(String sessionName, Context context) {
		mName = sessionName;
		mRelWeek = 0;
		load(context);
	}
	/** Lance la session utitlisateur par défaut **/
	UserSession(Context context) {
		mName = defaultSessionName;
		mRelWeek = 0;
		load(context);
	}
	
	/** Récupère les données de la session depuis le fichier de sauvegarde **/
	private void load(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(mName, 0);
		
		// Groupes visionnés
		mGroups.clear();
		int count = prefs.getInt("group-count", 0);
		for(int i = 0; i < count; i++) {
			int id = prefs.getInt("group"+i, -1);
			if(GroupList.isExisting(id))
				mGroups.add(id);
		}
		
		// Historique
		mHistory.clear();
		int size = prefs.getInt("hist-size", 0);
		for(int i = 0; i < size; i++) {
			int id = prefs.getInt("hist"+i, -1); 
			if(GroupList.isExisting(id))
				mHistory.add(id);
		}
	}
	
	/** Enregistre les données de la session **/
	public void save(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(mName, 0);
		Editor e = prefs.edit();
		e.clear();
		
		// Groupes visionnés
		e.putInt("group-count", mGroups.size());
		for(int i = 0; i < mGroups.size(); i++)
			e.putInt("group"+i, mGroups.get(i));
		// Historique
		e.putInt("hist-size", mHistory.size());
		for(int i = 0; i < mHistory.size(); i++)
			e.putInt("hist"+i, mHistory.get(i));
		
		e.commit();
	}
	
	/** Retourne l'historique des derniers groupes sélectionnés **/
	public ArrayList<Integer> getGroupHistory() {
		return mHistory;
	}
		
	/** Retourne la liste des groupes actuellement visionnés **/
	public ArrayList<Integer> getGroups() {
		return mGroups;
	}
	
	/** Retourne la semaine actuellement visionnée **/
	public int getRelWeek() {
		return mRelWeek;
	}
	/** Change le numéro de la semaine à visionner **/
	public void addRelWeek(int deltaweek) {
		mRelWeek += deltaweek;
		// en cas de dépassement, on change d'année
		int week = DateUtils.getCurrentWeek();
		if(mRelWeek > 0 && (week < 33 ? mRelWeek+week : (mRelWeek+week)%53) >= 33)
			mRelWeek -= 52;
		else if(mRelWeek < 0 && (week > 33 ? mRelWeek+week : (mRelWeek+week+52)%53) < 33)
			mRelWeek += 52;
		if(mParamsListener != null)
			mParamsListener.onParamsChanged(this, true, false);
	}
	/** Met à zéro (semaine courante) le numéro de la semaine à visionner **/
	public void resetRelWeek() {
		mRelWeek = 0;
		if(mParamsListener != null)
			mParamsListener.onParamsChanged(this, true, false);
	}

	/** Créer une requête avec les paramètres actuels de la session **/
	public ScheduleRequest createRequest() {
		return new ScheduleRequest(mGroups, mRelWeek);
	}
	
	/** Change le numéro des groupes à visionner **/
	public void setGroups(ArrayList<Integer> groups) {
		mGroups.clear();
		for(int i = groups.size(); --i >= 0;) {
			Integer g = groups.get(i);
			mHistory.remove(g);
			mHistory.add(0, g);
		}
		mGroups.addAll(groups);
		
		if(mParamsListener != null)
			mParamsListener.onParamsChanged(this, false, true);
	}
	/** Ajoute un groupe à visionner (en fin de liste) **/
	public void addGroups(int group) {
		mHistory.remove((Integer)group);
		mHistory.add(0, group);
		mGroups.add(group);
		if(mParamsListener != null)
			mParamsListener.onParamsChanged(this, false, true);
	}
	/** Retire un groupe de la liste **/
	public void removeGroup(int index) {
		mGroups.remove(index);
		if(mParamsListener != null)
			mParamsListener.onParamsChanged(this, false, true);
	}
	/** Modifie l'ID d'un groupe de la liste **/
	public void changeGroup(int groupId, int index) {
		mGroups.set(index, (Integer)groupId);
		if(mParamsListener != null)
			mParamsListener.onParamsChanged(this, false, true);
	}
	
	/** Assignation d'un listener pour les changements de paramètre **/
	public void setOnParamsChangedListener(OnParamsChangedListener listener) {
		mParamsListener = listener;
	}
	
	public interface OnParamsChangedListener {
		public void onParamsChanged(UserSession session, boolean weekChanged, boolean groupsChanged);
	}
}
