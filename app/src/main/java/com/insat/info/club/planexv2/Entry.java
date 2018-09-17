package com.insat.info.club.planexv2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Contient les attributs qui caractérisent un cours
 * 
 * @author Proïd
 */
public class Entry implements Comparable<Entry> {
	/** Nom du cours **/
	private String mSummary = "";
	/** Nom de la salle **/
	private String mRoom = "";
	/** Nom du prof **/
	private String mProfessor = "";
	/** Horaire de début du cours en minutes depuis 8 heure du matin **/
	private int mStartTime;
	/** Horaire de fin du cours en minutes depuis 8 heure du matin **/
	private int mEndTime;
	/** Couleur associée **/
	private int mColor = 0xFFB6B6B6; // par défaut : gris
	/** Date du jour, utilisée dans la fenêtre pop-up. **/
	private int mDay, mDayOfMonth, mMonth, mYear;

	public Entry() {
	}
	
	/** Charge les données depuis un flux entrant 
	 * @throws IOException **/
	protected Entry(DataInputStream dis) throws IOException {
		byte buffer[] = new byte[512];
		int length;

		// ClassName
		length = dis.readInt();
		dis.read(buffer, 0, length);
		mSummary = new String(buffer, 0, length);
		// RoomName
		length = dis.readInt();
		dis.read(buffer, 0, length);
		mRoom = new String(buffer, 0, length);
		// ProfessorName
		length = dis.readInt();
		dis.read(buffer, 0, length);
		mProfessor = new String(buffer, 0, length);
		// Horaires
		mStartTime = dis.readInt();
		mEndTime = dis.readInt();
		// Couleur
		mColor = dis.readInt();
		// Date
		mDay = dis.readInt();
		mDayOfMonth = dis.readInt();
		mMonth = dis.readInt();
		mYear = dis.readInt();
	}
	/** Enregistre les données dans le flux sortant 
	 * @throws IOException **/
	protected void save(DataOutputStream dos) throws IOException {
		byte[] buffer;
		
		// ClassName
		buffer = mSummary.getBytes();
		dos.writeInt(buffer.length);
		dos.write(buffer);
		// RoomName
		buffer = mRoom.getBytes();
		dos.writeInt(buffer.length);
		dos.write(buffer);
		// ProfessorName
		buffer = mProfessor.getBytes();
		dos.writeInt(buffer.length);
		dos.write(buffer);
		// Horaires
		dos.writeInt(mStartTime);
		dos.writeInt(mEndTime);
		// Couleur
		dos.writeInt(mColor);
		// Date
		dos.writeInt(mDay);
		dos.writeInt(mDayOfMonth);
		dos.writeInt(mMonth);
		dos.writeInt(mYear);
	}
	
 	public String getSummary() {
		return mSummary;
	}
	public void setSummary(String className) {
		this.mSummary = className;
	}
	
	public boolean hasRoom() {
		return mRoom.length() > 0;
	}
	public String getRoom() {
		return mRoom;
	}
	public void setRoom(String roomName) {
		this.mRoom = roomName;
	}

	public boolean hasProfessor() {
		return mProfessor.length() > 0;
	}
	public String getProfessor() {
		return mProfessor;
	}
	public void setProfessor(String professorName) {
		this.mProfessor = professorName;
	}

	public int getStartTime() {
		return mStartTime;
	}
	public void setStartTime(int startTime) {
		mStartTime = startTime;
	}
	public int getEndTime() {
		return mEndTime;
	}
	public void setEndTime(int endTime) {
		mEndTime = endTime;
	}
	/** Retourne la durée du cours sous forme de String **/
	public String getStringDuration() {
		int hour = (mEndTime - mStartTime) / 60;
		int min = (mEndTime - mStartTime) % 60;
		
		return String.format(Locale.getDefault(), "%dH%02d", hour, min) + " (" + getStringTime() + ")";
	}
	/** Retourne les horaires du cours sous forme de String **/
	public String getStringTime() {
		int hourStart = (mStartTime + 8*60) / 60;
		int minStart = (mStartTime + 8*60) % 60;
		int hourEnd = (mEndTime + 8*60) / 60;
		int minEnd = (mEndTime + 8*60) % 60;
		
		return String.format(Locale.getDefault(), "%dH%02d - %dH%02d", hourStart, minStart, hourEnd, minEnd);
	}
	
	public int getColor() {
		return mColor;
	}
	public void setColor(int color) {
		this.mColor = color;
	}

	/** Compare deux cours pour vérifier s'ils sont strictement identiques **/
	@Override
	public boolean equals(Object o) {
		Entry e = (Entry) o;
		return (mSummary.equals(e.mSummary)
				&& mRoom.equals(e.mRoom)
				&& mProfessor.equals(e.mProfessor)
				&& mStartTime == e.mStartTime
				&& mEndTime == e.mEndTime
				&& mColor == e.mColor);
	}
	/** Compare deux cours. Ordre de priorité : début, -fin, nom, salle, prof, couleur **/
	public int compareTo(Entry o) {
		Entry e = (Entry) o;
		int a = ((Integer)mStartTime).compareTo(e.mStartTime);
		if(a != 0) return a;
		a = ((Integer)mEndTime).compareTo(e.mEndTime);
		if(a != 0) return a;
		a = mSummary.compareTo(e.mSummary);
		if(a != 0) return a;
		a = mRoom.compareTo(e.mRoom);
		if(a != 0) return a;
		a = mProfessor.compareTo(e.mProfessor);
		if(a != 0) return a;
		a = ((Integer)mColor).compareTo(e.mColor);
		return a;
	}
	/** Compare deux cours en fonction de leurs horaires. Ordre de priorité : début, fin ** /
	public int compareTimeTo(Entry o) {
		Entry e = (Entry) o;
		int a = ((Integer)mStartTime).compareTo(e.mStartTime);
		if(a != 0) return a;
		return ((Integer)mEndTime).compareTo(e.mEndTime);
	}*/
	
	/** Modifie la date du jour, utilisée dans la fenêtre pop-up **/
	/*public void setDate(int day, int dayOfMonth, int month, int year) {
		mDay = day;
		mDayOfMonth = dayOfMonth;
		mMonth = month;
		mYear = year;
	}*/
	private static final String[] sday = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
	private static final String[] smonth = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
	/** Retourne la date du jour au format 'Jour jj Mois aaaa' **/
	public String getStringDate() {
		return sday[mDay] + " " + mDayOfMonth + " " + smonth[mMonth] + " " + mYear;
	}

	public void setDate(GregorianCalendar gc) {
		mDay = gc.get(GregorianCalendar.DAY_OF_WEEK) - GregorianCalendar.MONDAY;
		mDayOfMonth = gc.get(GregorianCalendar.DAY_OF_MONTH);
		mMonth = gc.get(GregorianCalendar.MONTH);
		mYear = gc.get(GregorianCalendar.YEAR);
	}
	public GregorianCalendar getDate() {
		return DateUtils.get(mYear, mMonth, mDayOfMonth, 0, 0);
	}

	@Override
	public String toString() {
		return "Entry{" + "mSummary='" + mSummary + '\'' + ", mRoom='" + mRoom + '\'' + ", mProfessor='" + mProfessor + '\'' + ", mStartTime=" + mStartTime + ", mEndTime=" + mEndTime + ", mColor=" + mColor + ", mDay=" + mDay + ", mDayOfMonth=" + mDayOfMonth + ", mMonth=" + mMonth + ", mYear=" + mYear + '}';
	}
}
