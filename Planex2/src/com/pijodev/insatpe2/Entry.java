package com.pijodev.insatpe2;

import java.util.Locale;

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
	/** Groupe(s) concerné(s) **/
	private int[] mGroups;
	/** Date de début du cours en minutes depuis 8 heure du matin **/
	private int mStartTime;
	/** Date de fin du cours en minutes depuis 8 heure du matin **/
	private int mEndTime;
	/** Couleur associée **/
	private int mColor = 0xFFB6B6B6; // par défaut : gris
	/** Date du jour **/
	private int mDay, mDayOfMonth, mMonth, mYear;
	
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
	public String getStringDuration() {
		int hour = (mEndTime - mStartTime) / 60;
		int min = (mEndTime - mStartTime) % 60;
		
		return String.format(Locale.getDefault(), "%dH%02d", hour, min) + " " + getStringTime();
	}
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
	
	public void setDate(int day, int dayOfMonth, int month, int year) {
		mDay = day;
		mDayOfMonth = dayOfMonth;
		mMonth = month;
		mYear = year;
	}
	private static final String[] sday = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
	private static final String[] smonth = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
	public String getStringDate() {
		return sday[mDay] + " " + mDayOfMonth + " " + smonth[mMonth] + " " + mYear;
	}
}
