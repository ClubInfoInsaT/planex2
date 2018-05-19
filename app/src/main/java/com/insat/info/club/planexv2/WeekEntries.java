package com.insat.info.club.planexv2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;

/**
 * Contient une liste de cours (Entry) pour chaque
 * jour d'une semaine pour un groupe
 * @author Proïd
 *
 */
public class WeekEntries {
	/** Cours des 5 jours de la semaine **/
	@SuppressWarnings("unchecked")
	private ArrayList<Entry> mEntries[] = new ArrayList[5];
	/** Numéro de la semaine **/
	private final int mWeek;
	/** Identifiant du groupe **/
	private final int mGroupId;
	/** Date de mise en cache **/
	private final GregorianCalendar mCacheDate;
	
	public WeekEntries(int week, int groupId) {
		mWeek = week;
		mGroupId = groupId;
		for(int i = 0; i < 5; i++)
			mEntries[i] = new ArrayList<Entry>();
		mCacheDate = DateUtils.today();
	}
	
	/** Charge les données depuis un flux entrant 
	 * @throws IOException **/
	protected WeekEntries(DataInputStream dis) throws IOException {
		mWeek = dis.readInt();
		mGroupId = dis.readInt();
		// Date
		int year = dis.readInt();
		int month = dis.readInt();
		int day = dis.readInt();
		int hour = dis.readInt();
		int minute = dis.readInt();
		mCacheDate = DateUtils.get(year, month, day, hour, minute);
		
		// Cours des 5 jours
		for(int i = 0; i < 5; i++) {
			int count = dis.readInt();
			mEntries[i] = new ArrayList<Entry>(count);
			for(int j = 0; j < count; j++) {
				mEntries[i].add(new Entry(dis));
			}
		}
	}

	/** Enregistre les données avec un flux sortant 
	 * @throws IOException **/
	protected void save(DataOutputStream dos) throws IOException {
		dos.writeInt(mWeek);
		dos.writeInt(mGroupId);
		// Date
		dos.writeInt(mCacheDate.get(GregorianCalendar.YEAR));
		dos.writeInt(mCacheDate.get(GregorianCalendar.MONTH));
		dos.writeInt(mCacheDate.get(GregorianCalendar.DAY_OF_MONTH));
		dos.writeInt(mCacheDate.get(GregorianCalendar.HOUR_OF_DAY));
		dos.writeInt(mCacheDate.get(GregorianCalendar.MINUTE));

		// Cours des 5 jours
		for(int i = 0; i < 5; i++) {
			dos.writeInt(mEntries[i].size());
			for(Entry entry : mEntries[i]) {
				entry.save(dos);
			}
		}
	}
	
	/** Retourne la liste des cours d'une journée **/
	public ArrayList<Entry> getEntries(int day) {
		return mEntries[day];
	}

	public int getGroupId() {
		return mGroupId;
	}
	
	public int getWeek() {
		return mWeek;
	}
	
	/** Retourne la date de mise en cache **/
	public GregorianCalendar getCacheDate() {
		return mCacheDate;
	}
	/** Retourne le nombre de secondes écoulées depuis la dernière mise à jour du cache **/
	public long getCacheTime() {
		long diff = DateUtils.today().getTimeInMillis() - mCacheDate.getTimeInMillis();
		return diff / 1000;
	}

	/** Compare deux weekentries **/
	public boolean equals(Object o) {
		if(o == null)
			return false;
		WeekEntries w = (WeekEntries) o;
		for(int i = 0; i < 5; i++) {
			if(mEntries[i].size() != w.mEntries[i].size())
				return false;
			
			// on trie pour mieux comparer, mais c'est pas top de retrier à chaque fois pour les performances
			Collections.sort(mEntries[i]);
			Collections.sort(w.mEntries[i]);
			if(!mEntries[i].equals(w.mEntries[i]))
				return false;
		}
		return true;
	}
}
