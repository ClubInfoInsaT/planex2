package com.pijodev.insatpe2;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.SparseArray;
/**
 * Gestion du téléchargement et parsing du fichier ics exporté de planning express
 * @author Proïd
 *
 */
public class ScheduleRetriever {
	
	/** Télécharge le fichier ics, retourne null en cas d'erreur **/
	public String download(ArrayList<Integer> groups, int week) {
		URL url = getURL(groups, week);
		String data = null;
		try {
			InputStream stream = url.openStream();
			data = new String();
			byte[] buffer = new byte[512];
			int length;
			while((length = stream.read(buffer)) > 0)
				data += new String(buffer, 0, length);
			stream.close();
		} catch (IOException e) {
			
		}
		
		return data;
	}
	
	/** Construit un weekentries pour chaque groupe à partir du fichier ics de planning express **/
	public SparseArray<WeekEntries> parse(String data, ArrayList<Integer> groups, int week) {
		SparseArray<WeekEntries> we = new SparseArray<>(groups.size());
		for(Integer i : groups)
			we.put(i, new WeekEntries(week, i));
		
		String[] lines = data.split("\n");
		// On vérifie que l'en-tête du fichier est correct
		if(!lines[0].equals("BEGIN:VCALENDAR"))
			return null;
		
		Entry entry = null;
		int dayOfWeek = 0;
		int group = 0;
		
		// Lecture ligne par ligne
		for(String line : lines) {
			int limit = line.indexOf(':');
			if(limit == -1)
				continue;
			String fieldName = line.substring(0, limit);
			String fieldContent = line.substring(limit+1);
			
			switch(fieldName) {
			case "BEGIN":
				if(fieldContent.endsWith("VEVENT"))
					entry = new Entry(DateUtils.today());
				break;
			case "DTSTART": {
				// format du fichier : aaaammjjThhmmssZ
				int year = Integer.parseInt(fieldContent.substring(0, 4));
				int month = Integer.parseInt(fieldContent.substring(4, 6)) - 1; // les mois commencent à 0
				int day = Integer.parseInt(fieldContent.substring(6, 8));
				int hour = Integer.parseInt(fieldContent.substring(9, 11))+2;
				int minute = Integer.parseInt(fieldContent.substring(11, 13));
				GregorianCalendar gc = DateUtils.get(year, month, day, 0,0);
				// passage à l'heure d'été/hiver
				if(!DateUtils.inDaylightTime(gc))
					hour--;
				// on retient le jour de la semaine correspondant
				dayOfWeek = DateUtils.getDayOfWeek(gc);
				// on enregistre le nombre de minutes depuis 8h
				entry.setStartTime((hour-8) * 60 + minute);
				break;
			}
			case "DTEND": {
				// format : aaaammjjThhmmssZ
				int year = Integer.parseInt(fieldContent.substring(0, 4));
				int month = Integer.parseInt(fieldContent.substring(4, 6)) - 1; // les mois commencent à 0
				int day = Integer.parseInt(fieldContent.substring(6, 8));
				int hour = Integer.parseInt(fieldContent.substring(9, 11))+2;
				int minute = Integer.parseInt(fieldContent.substring(11, 13));
				GregorianCalendar gc = DateUtils.get(year, month, day, 0,0);
				// passage à l'heure d'été/hiver
				if(!DateUtils.inDaylightTime(gc))
					hour--;
				// on enregistre le nombre de minutes depuis 8h
				entry.setEndTime((hour-8) * 60 + minute);
				break;
			}
			case "SUMMARY":
				entry.setSummary(fieldContent);
				break;
			case "UID":
				// format planning-XXXX(-\d+)*
				group = Integer.parseInt(fieldContent.split("-")[1]);
				break;
			case "DESCRIPTION": {
				// format : Cours : ? - Enseignant : ? - Salle : ?
				int indexStart = fieldContent.indexOf(" - Enseignant : ") + (" - Enseignant : ").length();
				int indexEnd = fieldContent.indexOf(" - Salle : ");
				entry.setProfessor(fieldContent.substring(indexStart, indexEnd));
				break;
			}
			case "LOCATION":
				entry.setRoom(fieldContent);
				break;
			case "COLOR":
				int r = Integer.parseInt(fieldContent.substring(0, 3)) << 16;
				int g = Integer.parseInt(fieldContent.substring(3, 6)) << 8;
				int b = Integer.parseInt(fieldContent.substring(6, 9));
				
				entry.setColor(0xff000000 | r | g | b);
				break;
			case "END":
				if(fieldContent.equals("VEVENT"))
					we.get(group).getEntries(dayOfWeek).add(entry);
				break;
			}
		}
		
		
		return we;
	}
	
	/** Retourne l'URL qui permet d'accéder aux emplois du temps des groupes donnés pour la semaine donnée.
	 * Il doit y avoir au moins un groupe dans la liste, sinon... -Sinon quoi ? -Exactement !**/
	private URL getURL(ArrayList<Integer> groups, int week) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(groups.get(0));
		for(int i = 1; i < groups.size(); i++)
			sb.append('+').append(groups.get(i));
		
		try {
			return new URL("http://www.etud.insa-toulouse.fr/planning/index.php?gid="+sb.toString()+"&wid="+week+"&ics=1&planex=2");
		} catch (MalformedURLException e) {
			return null;
		}
	}
}
