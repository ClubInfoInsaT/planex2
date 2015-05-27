package com.pijodev.insatpe;

import java.util.GregorianCalendar;

/**
 * Quelques fonctions statiques utiles pour manipuler les dates
 * @author Proïd
 *
 */
public class DateUtils {
	/** Retourne la date du lundi de la semaine actuelle **/
	private static GregorianCalendar getMondayOfCurrentWeek() {
		GregorianCalendar calendar = today();

		// si on on est samedi, on se place au lundi suivant
		if(calendar.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SATURDAY)
			calendar.add(GregorianCalendar.DAY_OF_MONTH, 2);
		// si on on est dimanche, on se place au lundi suivant
		else if(calendar.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SUNDAY)
			calendar.add(GregorianCalendar.DAY_OF_MONTH, 1);
		// Sinon, on remonte jusqu'au premier jour de la semaine
		else
			while(calendar.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.MONDAY)
				calendar.add(GregorianCalendar.DAY_OF_MONTH, -1);
		
		return calendar;
	}
	/** retourne le numéro de la semaine actuelle **/
	public static int getCurrentWeek() {
		return getMondayOfCurrentWeek().get(GregorianCalendar.WEEK_OF_YEAR);
	}

	/** Retourne la date d'un jour (0~4) d'une semaine relative à la semaine courante **/
	public static GregorianCalendar getDayOfWeek(int relativeWeek, int day) {
		GregorianCalendar calendar = getMondayOfCurrentWeek();
		
		calendar.add(GregorianCalendar.DAY_OF_MONTH, 7*relativeWeek + day);

		return calendar;
	}
	/** Retourne la date du lundi d'une semaine relative à la semaine courante **/
	public static GregorianCalendar getMondayOfWeek(int relativeWeek) {
		GregorianCalendar calendar = getMondayOfCurrentWeek();
		
		calendar.add(GregorianCalendar.DAY_OF_MONTH, 7*relativeWeek);
		
		return calendar;
	}
	/** Retourne le jour de la semaine de la date actuelle **/
	public static int getDayOfWeek() {
		GregorianCalendar calendar = today();
		
		// si on on est samedi, on se place au lundi suivant
		if(calendar.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SATURDAY)
			calendar.add(GregorianCalendar.DAY_OF_MONTH, 2);
		// si on on est dimanche, on se place au lundi suivant
		else if(calendar.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SUNDAY)
			calendar.add(GregorianCalendar.DAY_OF_MONTH, 1);
		
		return calendar.get(GregorianCalendar.DAY_OF_WEEK) - GregorianCalendar.MONDAY; 
	}
	
	/** Retourne un nouvel objet GregorianCalendar à la date courante. Met le premier jour de la semaine au lundi **/
	public static GregorianCalendar today() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setFirstDayOfWeek(GregorianCalendar.MONDAY);
		return gc;
	}
	/** Retourne un nouvel objet GregorianCalendar à la date donnée. Met le premier jour de la semaine au lundi **/
	public static GregorianCalendar get(int year, int month, int day, int hour, int minute) {
		GregorianCalendar gc = new GregorianCalendar(year, month, day, hour, minute);
		gc.setFirstDayOfWeek(GregorianCalendar.MONDAY);
		return gc;
	}
	/** Retourne un nouvel objet GregorianCalendar à la date donnée **/
	public static boolean inDaylightTime(GregorianCalendar c) {
		return c.getTimeZone().inDaylightTime(c.getTime());
	}
	/** Retourne le jour de la semaine de cette date **/
	public static int getDayOfWeek(GregorianCalendar c) {
		return c.get(GregorianCalendar.DAY_OF_WEEK) - GregorianCalendar.MONDAY;
	}
}