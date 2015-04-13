package com.pijodev.insatpe2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.sax.StartElementListener;

/**
 * Contient les attributs qui caractérisent un cours
 * 
 * @author Proïd
 */
public class Entry {
	/** Nom du cours **/
	private String mClassName = "";
	/** Nom de la salle **/
	private String mRoomName = "";
	/** Nom du prof **/
	private String mProfessorName = "";
	/** Horaire de début du cours en minutes depuis 8 heure du matin **/
	private int mStartTime;
	/** Horaire de fin du cours en minutes depuis 8 heure du matin **/
	private int mEndTime;
	/** Couleur associée **/
	private int mColor = 0xFFB6B6B6; // par défaut : gris
	/** Date du jour, utilisée dans la fenêtre pop-up. **/
	private int mDay, mDayOfMonth, mMonth, mYear;
	
	public Entry(GregorianCalendar date) {
		mDay = date.get(GregorianCalendar.DAY_OF_WEEK) - GregorianCalendar.MONDAY;
		mDayOfMonth = date.get(GregorianCalendar.DAY_OF_MONTH);
		mMonth = date.get(GregorianCalendar.MONTH);
		mYear = date.get(GregorianCalendar.YEAR);
	}
	
	/** Charge les données depuis un flux entrant 
	 * @throws IOException **/
	protected Entry(DataInputStream dis) throws IOException {
		byte buffer[] = new byte[512];
		int length;

		// ClassName
		length = dis.readInt();
		dis.read(buffer, 0, length);
		mClassName = new String(buffer, 0, length);
		// RoomName
		length = dis.readInt();
		dis.read(buffer, 0, length);
		mRoomName = new String(buffer, 0, length);
		// ProfessorName
		length = dis.readInt();
		dis.read(buffer, 0, length);
		mProfessorName = new String(buffer, 0, length);
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
		buffer = mClassName.getBytes();
		dos.write(buffer);
		// RoomName
		buffer = mRoomName.getBytes();
		dos.write(buffer);
		// ProfessorName
		buffer = mProfessorName.getBytes();
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
	
 	public String getClassName() {
		return mClassName;
	}
	public void setClassName(String className) {
		this.mClassName = className;
	}
	
	public boolean hasRoomName() {
		return mRoomName.length() > 0;
	}
	public String getRoomName() {
		return mRoomName;
	}
	public void setRoomName(String roomName) {
		this.mRoomName = roomName;
	}

	public boolean hasProfessorName() {
		return mProfessorName.length() > 0;
	}
	public String getProfessorName() {
		return mProfessorName;
	}
	public void setProfessorName(String professorName) {
		this.mProfessorName = professorName;
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
		
		return String.format(Locale.getDefault(), "%dH%02d", hour, min) + " " + getStringTime();
	}
	/** Retourne les horaires du cours sous forme de String **/
	public String getStringTime() {
		int hourStart = mStartTime / 60 + 8;
		int minStart = mStartTime % 60;
		int hourEnd = mEndTime / 60 + 8;
		int minEnd = mEndTime % 60;
		
		return String.format(Locale.getDefault(), "(%dH%02d - %dH%02d)", hourStart, minStart, hourEnd, minEnd);
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
		return (mClassName.equals(e.mClassName)
				&& mRoomName.equals(e.mRoomName)
				&& mProfessorName.equals(e.mProfessorName)
				&& mStartTime == e.mStartTime
				&& mEndTime == e.mEndTime
				&& mColor == e.mColor);
	}
	/** Compare deux cours **/
	public int compareTo(Entry o) {
		Entry e = (Entry) o;
		int a = ((Integer)mStartTime).compareTo(e.mStartTime);
		if(a != 0) return a;
		a = ((Integer)mEndTime).compareTo(e.mEndTime);
		if(a != 0) return a;
		a = mClassName.compareTo(e.mClassName);
		if(a != 0) return a;
		a = mRoomName.compareTo(e.mRoomName);
		if(a != 0) return a;
		a = mProfessorName.compareTo(e.mProfessorName);
		if(a != 0) return a;
		a = ((Integer)mColor).compareTo(e.mColor);
		return a;
	}
	
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
}
