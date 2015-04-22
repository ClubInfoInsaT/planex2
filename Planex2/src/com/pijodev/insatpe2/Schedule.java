package com.pijodev.insatpe2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;

import android.content.Context;
import android.widget.Toast;

/**
 * 
 * @author Proïd
 *
 */

@SuppressWarnings("unchecked") // ArrayList[] !
public class Schedule {
	
	/** Listes de cours pour chaque jour **/
	private ArrayList<ScheduleEntry> mEntries[] = new ArrayList[5];
	/** Date du Lundi de la semaine **/
	private final GregorianCalendar mMondayDate;
	/** WeekEntries de chaque groupe **/
	private final ArrayList<WeekEntries> mWeekEntries = new ArrayList<>(); 
	/** Requête à l'origine de la création de ce schedule **/
	private final ScheduleRequest mRequest;
	/** Indique si cet emploi doit être affiché **/
	private final boolean mShow;
	
	
	/** Construit l'emploi de temps de la semaine à partir du cache **/
	public Schedule(ScheduleRequest request, Context context, boolean show) {
		mRequest = request;
		mShow = show;
		
		// Date
		mMondayDate = DateUtils.getMondayOfWeek(request.getRelWeek());
		
		// données pour accès au cache
		ArrayList<Integer> groups = request.getGroups();
		int week = DateUtils.getCurrentWeek()+request.getRelWeek();
		
		for(int day = 0; day < 5; day++) {
			mEntries[day] = new ArrayList<>();
			
			// Simplification des cours en commun et tri
			for(int g = groups.size()-1; g >= 0; g--) {
				WeekEntries we = Cache.getWeekEntries(week, groups.get(g), context);
				// l'objet donné par le cache peut être nul -> absence de données dans le cache
				mWeekEntries.add(we);
				
				if(we != null && mShow) {
					for(Entry e : we.getEntries(day)) {
						ScheduleEntry se = new ScheduleEntry(e, g+1, groups);
						int index = Collections.binarySearch(mEntries[day], se);
						// Cours commun : ajout du numéro du groupe
						if(index >= 0)
							mEntries[day].get(index).addGroupRef(g+1);
						// Insertion du cours dans la liste triée
						else
							mEntries[day].add(-1-index, se);
					}
				}
			}
			
			// Inutile de faire le reste si on n'affiche rien au final
			if(!mShow)
				continue;
			
			// Structure temporaire pour un algo qui trou l'cul
			ArrayList<ArrayList<ScheduleEntry>> set = new ArrayList<ArrayList<ScheduleEntry>>();

			// Calcul du nombre max de colonne requis
			for(ScheduleEntry se : mEntries[day]) {
				boolean done = false;
				ArrayList<ScheduleEntry> target = null;
				for(int i = 0; i < set.size() && !done; i++) {
					if(set.get(i).get(set.get(i).size()-1).entry.getEndTime() <= se.entry.getStartTime()) {
						target = set.get(i);
						done = true;
					}
				}
				if(!done)
					set.add(target = new ArrayList<ScheduleEntry>());
				
				target.add(se);
			}
			
			// Calcul de la largeur minimale des entries
			for(int i = set.size()-1; --i >= 0; ) {
				for(ScheduleEntry se : set.get(i)) {
					for(ScheduleEntry se2 : set.get(i+1)) {
						if(se2.entry.getStartTime() >= se.entry.getEndTime())
							break;
						else if(!(se2.entry.getEndTime() <= se.entry.getStartTime()))
							se.minColumnCount = Math.max(se.minColumnCount, 1 + se2.minColumnCount);
					}
					se.width = 1.0f / se.minColumnCount;
				}
			}
			// Calcul de la position et optimisation de la largeur des entries
			for(int i = 1; i < set.size(); i++) {
				for(ScheduleEntry se : set.get(i)) {
					for(ScheduleEntry se2 : set.get(i-1)) {
						if(se2.entry.getStartTime() >= se.entry.getEndTime())
							break;
						else if(!(se2.entry.getEndTime() <= se.entry.getStartTime()))
							se.position = Math.max(se.position, se2.position + se2.width);
					}
					se.width = (1.0f-se.position) / se.minColumnCount;
				}
			}
		}
	}
	
	/** Indique si cet emploi doit être affiché **/
	public boolean mustBeShown() {
		return mShow;
	}
	
	/** Indique si l'emploi du temps de la semaine est complètement vide **/
	public boolean isEmpty() {
		for(int i = 0; i < 5; i++)
			if(!mEntries[i].isEmpty())
				return false;

		return true;
	}
	
	/** Retourne la liste des cours d'une journée **/
	public ArrayList<ScheduleEntry> getEntries(int day) {
		return mEntries[day];
	}
	
	/** Retourne la date d'un jour de la semaine au format int[] {jour, mois [0;11], année} **/
	public int[] getDate(int day) {
		GregorianCalendar copy = (GregorianCalendar) mMondayDate.clone();
		copy.add(GregorianCalendar.DAY_OF_MONTH, day);
		return new int[] {
				copy.get(GregorianCalendar.DAY_OF_MONTH),
				copy.get(GregorianCalendar.MONTH),
				copy.get(GregorianCalendar.YEAR)
		};
	}
	
	/** Donne la requête à l'origine de ce schedule **/
	public ScheduleRequest getRequest() {
		return mRequest;
	}
	
	/** Affiche un message sous forme toast à propos du cache (manquant, ancient, ...) si nécessaire **/
	public void showCacheWarning(Context context) {
		// On affiche pas de message si aucun groupe n'est affiché
		if(mRequest.getGroups().size() == 0)
			return;
		
		int missingCount = 0;
		long timeMax = 0;
		for(WeekEntries we : mWeekEntries) {
			if(we == null) {
				missingCount++;
			} else {
				timeMax = Math.max(timeMax, we.getCacheTime());
			}
		}
		boolean isIncomplete = missingCount > 0;
		boolean isEmpty = missingCount == mWeekEntries.size();

		String msg = "";
		if(isEmpty) {
			if(!mRequest.useOnlyCache())
				msg = "Pas d'accès Internet";
		}
		else  {
			String cache;
			if(timeMax < 60)
				cache = "";
			else if((timeMax /= 60) < 60)
				cache = "Âge du cache : "+timeMax+" minute" + (timeMax==1?"":"s");
			else if((timeMax /= 60) < 24)
				cache = "Âge du cache : "+timeMax+" heure" + (timeMax==1?"":"s");
			else if((timeMax /= 24) < 7)
				cache = "Âge du cache : "+timeMax+" jour" + (timeMax==1?"":"s");
			else
				cache = "Âge du cache : "+(timeMax/7)+" semaine" + (timeMax==1?"":"s");
			
			if(isIncomplete && !mRequest.useOnlyCache())
				msg = "Affichage incomplet, besoin d'accès Internet" + (cache.length() > 0 ? "" : "\n");
			msg += cache;
		}
			
		if(msg.length() > 0)
			MyToast.show(context, msg, Toast.LENGTH_SHORT);
	}
	
	public static class ScheduleEntry implements Comparable<ScheduleEntry> {
		/** Cours **/
		public Entry entry;
		/** Numéros des groupes associés (triés par ordre croissant) **/
		private ArrayList<Integer> groupRef;
		/** Liste des id des groupes potentiellement concernés **/
		private ArrayList<Integer> groupId;
		/** Nombre max de groupe associé */
		private int groupMax;
		/** Nombre de sous-colonnes minimum requis pour ce cours **/
		private int minColumnCount = 1;
		/** Position de la case dans une colonne selon l'axe horizontal **/
		private float position = 0.0f; // max col[i-1].(position+width)
		/** Largeur de la case dans une colonne. Largeur d'une colonne = 1.0f **/
		private float width = 1.0f;// (widthMax - position) / minColumnCount
		
		public ScheduleEntry(Entry e, int g, ArrayList<Integer> groupId) {
			entry = e;
			groupRef = new ArrayList<>();
			groupRef.add(g);
			groupMax = groupId.size();
			this.groupId = groupId;
		}
		/** Ajoute un numéro de groupe dans la liste triée **/
		public void addGroupRef(int g) {
			groupRef.add(-1-Collections.binarySearch(groupRef, (Integer)g), g);
		}
		/** Met la liste des numéros de groupe sous forme de String **/
		public String groupRefToString() {
			// tous les groupes sont concernés, on n'affiche rien
			if(groupRef.size() == groupMax)
				return null;
			
			String s = "";
			for(Integer i : groupRef)
				s += i;
			return s.length() == 0 ? null : s;
		}
		/** Retourne la liste des groupes **/
		public ArrayList<Integer> getGroupRef() {
			return groupRef;
		}
		/** Retourne l'id du groupe à partir d'un numéro **/
		public int getGroupId(int number) {
			return groupId.get(number-1);
		}
		/** Retourne la largeur de la case dans une colonne. Largeur d'une colonne = 1.0f **/
		public float getMeasuredWidth() {
			return width;
		}
		/** Retourne la position de la case dans une colonne selon l'axe horizontal. Largeur d'une colonne = 1.0f **/
		public float getMeasuredPosition() {
			return position;
		}
		
		@Override
		public int compareTo(ScheduleEntry another) {
			/*int diff = this.entry.compareTimeTo(another.entry);
			if(diff != 0)
				return diff;
			diff = ((Integer)groupRef.get(0)).compareTo(another.groupRef.get(0));
			if(diff != 0)
				return diff;*/
			return this.entry.compareTo(another.entry);
		}
	}
}
