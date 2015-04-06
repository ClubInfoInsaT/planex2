package com.pijodev.insatpe2;

import android.util.SparseArray;

/**
 * Contient la liste des groupes (nom, id) enregistrés
 * -> liste chargée depuis planning express
 * -> groupes ajoutés manuellement
 * 
 * @author Proïd
 *
 */
public class GroupList {
	/** Ensemble des groupes identifiés par leur ID **/
	private static SparseArray<String> mDefaultGroups;
	/** Ensemble des groupes ajoutés par l'utilisateur **/
	private static SparseArray<String> mUserGroups;
	
	/** Retourne le nom du groupe correspondant à l'id.
	 *  Retourne null si l'id est inconnu **/
	public String getGroupName(int id) {
		String s = mDefaultGroups.get(id);
		
		return s != null ? s : mUserGroups.get(id);
	}
	
	/** Charge la liste officielle des groupes depuis planning express **/
	public void update() {
		
	}
	
	/** Ajoute une groupe à la liste utilisateur **/
	public void addGroup(int id, String name) {
		if(mUserGroups.get(id) == null) {
			mUserGroups.put(id, name);
		}
	}
}
