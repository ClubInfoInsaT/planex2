package com.pijodev.insatpe2;


/**
 * 
 * @author Proïd
 *
 */
abstract public class GroupSelectorView {
	protected OnGroupSelectedListener mGroupSelectedListener;
	protected int mTarget; // id de la cible
	
	abstract void show(int target, int defaultId);
	
	public void setOnGroupSelectedListener(OnGroupSelectedListener listener) {
		mGroupSelectedListener = listener;
	}
	
	public interface OnGroupSelectedListener {
		/**
		 * Listener appelé lors de la sélection d'un groupe
		 * @param groupId ID du groupe sélectionné, -1 si demande de suppression
		 * @param target convention : numéro de groupe dans la liste, -1 pour nouveau groupe
		 */
		void onGroupSelected(int groupId, int target);
	}
}
