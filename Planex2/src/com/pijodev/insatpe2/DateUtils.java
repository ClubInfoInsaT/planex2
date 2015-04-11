package com.pijodev.insatpe2;

import java.util.GregorianCalendar;

/**
 * Quelques fonctions statiques utiles pour manipuler les dates
 * @author Proïd
 *
 */
public class DateUtils {
	/** Retourne la date du lundi de la semaine actuelle **/
	private static GregorianCalendar getMondayOfCurrentWeek() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setFirstDayOfWeek(GregorianCalendar.MONDAY);

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
	public static GregorianCalendar getDayOfWeek(int relativeWeek) {
		GregorianCalendar calendar = getMondayOfCurrentWeek();
		
		calendar.add(GregorianCalendar.DAY_OF_MONTH, 7*relativeWeek);
		
		return calendar;
	}
}
