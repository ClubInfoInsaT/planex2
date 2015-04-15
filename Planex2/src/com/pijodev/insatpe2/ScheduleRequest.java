package com.pijodev.insatpe2;
import java.util.ArrayList;

/**
 * Requête d'emploi du temps : groupes et semaine
 * @author Proïd
 *
 */
public class ScheduleRequest {
	/** Semaine relative à la semaine courante **/
	private final int mRelWeek;
	/** Indetifiants des groupes **/
	private final ArrayList<Integer> mGroups;
	
	/** ne pas télécharger de données, utiliser seulement le cache **/
	private boolean mUseOnlyCache = false; // pas de téléchargement
	/** affichage seulement si les données téléchargées sont différentes du cache **/
	private boolean mShowOnlyUpdate = false;
	
	public ScheduleRequest(ArrayList<Integer> groupId, int week) {
		mRelWeek = week;
		mGroups = new ArrayList<>(groupId);
	}
	
	public boolean useOnlyCache() {
		return mUseOnlyCache;
	}
	public boolean showOnlyUpdate() {
		return mShowOnlyUpdate;
	}
	
	/** Demande poliment de n'utiliser que le cache **/
	public void pleaseUseOnlyCache() {
		mUseOnlyCache = true;
	}
	/** Demande poliment de mettre à jour l'afichage que si il y a des différences avec le cache **/
	public void pleaseShowOnlyUpdate() {
		mShowOnlyUpdate = true;
	}
	
	public int getRelWeek() {
		return mRelWeek;
	}
	public ArrayList<Integer> getGroups() {
		return mGroups;
	}
}
