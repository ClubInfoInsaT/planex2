package com.pijodev.insatpe2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
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
	private static SparseArray<Group> mDefaultGroups = new SparseArray<>();
	/** Ensemble des groupes ajoutés par l'utilisateur **/
	private static SparseArray<Group> mUserGroups = new SparseArray<>();
	/** Liste complète des groupes **/
	private static ArrayList<Group> mListGroups = new ArrayList<>();
	
	/** Retourne le nom du groupe correspondant à l'id.
	 *  Retourne null si l'id est inconnu **/
	public static String getGroupName(int id) {
		Group g = mDefaultGroups.get(id);
		if(g != null)
			return g.name;
		
		g = mUserGroups.get(id);
		if(g != null)
			return g.name;
		
		return null;
	}
	
	/** Charge la liste des groupes mise en cache **/
	public static void load(Context context) {
		Resources r = context.getResources();
		String names[][] = {
				r.getStringArray(R.array.group_name_1),
				r.getStringArray(R.array.group_name_2),
				r.getStringArray(R.array.group_name_3),
				r.getStringArray(R.array.group_name_4),
				r.getStringArray(R.array.group_name_5),
		};
		int ids[][] = {
				r.getIntArray(R.array.group_id_1),
				r.getIntArray(R.array.group_id_2),
				r.getIntArray(R.array.group_id_3),
				r.getIntArray(R.array.group_id_4),
				r.getIntArray(R.array.group_id_5),
		};
		for(int i = 0; i < 5; i++)
			for(int j = 0; j < names.length; j++) {
				Group g = new Group(names[i][j], ids[i][j]);
				mDefaultGroups.put(ids[i][j], g);
				mListGroups.add(g);
			}
	}
	
	/** Charge la liste officielle des groupes depuis planning express **/
	public static void update() {
		
	}
	
	/** Ajoute une groupe à la liste utilisateur **/
	public static void addGroup(int id, String name) {
		if(mUserGroups.get(id) == null) {
			mUserGroups.put(id, new Group(name, id));
		}
	}
	
	/** Retourne la liste des groupes dont le nom du groupe contient la chaîne de caractère donnée **/
	public LinkedList<Group> filter(String keyword) {
		LinkedList<Group> list = new LinkedList<>();
		keyword = keyword.toLowerCase(Locale.getDefault()).replace(" ", "");
		for(Group g : mListGroups) {
			if(g.name.toLowerCase(Locale.getDefault()).replace(" ", "").contains(keyword))
				list.add(g);
		}
		
		return list;
	}
	
	static class Group {
		public String name;
		public int id;

		public Group(String name, int id) {
			this.name = name;
			this.id = id;
		}
	}
}
