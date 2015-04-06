package com.pijodev.insatpe2;
/**
 * Contient les informations liées à la navigation actuelle,
 * aux préférences, à l'historiques, aux cartes bancaires et
 * à la vie privée de l'utilisateur. (It's a joke! lol.)
 * 
 * @author Proïd
 *
 */
public class UserSession {
	public static final String defaultSessionName = "default";  
	
	/** Lance une nouvelle session à partir d'un nom de session
	 * à partir duquel on charge les fichiers de préférences.
	 * Nom de session différent pour les widgets par exemple **/
	UserSession(String sessionName) {
		// TODO load pref file
	}
	
	/** Retourne l'historique des derniers groupes sélectionnés **/
	public void getGroupHistory() {
		// TODO
	}
	
	/** Retourne la liste des groupes actuellement visionnés **/
	public void getCurrentGroup() {
		// TODO
	}
	
	/** Retourne (le numero de ?) la semaine actuellement visionnée **/
	public void getCurrentWeek() {
		// TODO
	}
}
