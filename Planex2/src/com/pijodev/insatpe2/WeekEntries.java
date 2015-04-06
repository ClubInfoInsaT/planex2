package com.pijodev.insatpe2;

import java.util.ArrayList;

/**
 * Contient une liste de cours (Entry) pour chaque
 * jour d'une semaine pour un groupe
 * @author Proïd
 *
 */
public class WeekEntries {
	private ArrayList<Entry> mEntries[] = new ArrayList[5];
	// Référence de la semaine
	private final int mWeekId;
	// Id du groupe
	private final int mGroupId;
	
	public WeekEntries(int weekId, int groupId) {
		mWeekId = weekId;
		mGroupId = groupId;
	}
	
	public void load(/* InputStream */) {
		for(int i = 0; i < 5; i++)
			mEntries[i] = new ArrayList<Entry>();
		/// TODO
	}
	
	/** Retourne la liste des cours d'une journée **/
	public ArrayList<Entry> getEntries(int day) {
		return mEntries[day];
	}
}
