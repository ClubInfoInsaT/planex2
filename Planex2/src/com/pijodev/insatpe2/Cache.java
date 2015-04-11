package com.pijodev.insatpe2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;

import android.content.Context;
import android.util.SparseArray;

/**
 * Contient toutes les données mise en cache
 * 
 * @author Proïd
 *
 */
public class Cache {
	private static final String cacheFileName = "weekentries-cache.data";
	/** Version du format du fichier de cache. A modifier si le format du cache évolue **/
	private static final int currentCacheVersion = 1;
	
	private static SparseArray<WeekEntries> mCache;
	
	
	/** Retourne les données du cache pour un ID (groupe+semaine)**/
	public static WeekEntries getWeekEntries(int week, int group, Context context) {
		if(mCache == null)
			load(context);
		return mCache.get(getCacheId(week, group));
	}
	
	/** Insère des données dans le cache. Remplace les données précédentes. **/
	public static void putWeekEntries(WeekEntries data, int week, int group, Context context) {
		if(mCache == null)
			load(context);
		mCache.put(getCacheId(week, group), data);
	}
	
	/** Charge le cache depuis le fichier de sauvegarde **/
	private static void load(Context context) {
		mCache = new SparseArray<>();
		
		try {
			FileInputStream fis = context.openFileInput(cacheFileName);
			DataInputStream dis = new DataInputStream(fis);
			
			// Vérification du format du cache
			int version = dis.readInt();
			if(version != currentCacheVersion)
				throw new DataFormatException();
			
			// Ecriture des données
			int nbWeekEntries = dis.readInt();
			for(int i = 0; i < nbWeekEntries; i++) {
				WeekEntries week = new WeekEntries(dis);
				mCache.put(getCacheId(week.getWeek(), week.getGroupId()), week);
			}
			
			dis.close();
		} catch (FileNotFoundException e) {
			// Le fichier de cache n'existe pas
		} catch (IOException e) {
		} catch (DataFormatException e) {
			// Le format du fichier de cache n'est pas à jour -> on supprime le cache
			new File(context.getFilesDir(), cacheFileName).delete();
		}
	}
	
	/** Enregistre le cache **/
	public static void save(Context context) {
		try {
			FileOutputStream fos = context.openFileOutput(cacheFileName, 0);
			DataOutputStream dos = new DataOutputStream(fos);
			
			// Version
			dos.writeInt(currentCacheVersion);
			
			// Lecture des données
			dos.writeInt(mCache.size());
			for(int i = 0; i < mCache.size(); i++) {
				mCache.valueAt(i).save(dos);
			}
			
			dos.close();
		} catch (IOException e) {
		}
	}
	
	/** Efface tout le cache **/
	public void clear() {
		mCache.clear();
	}
	
	/** Donne le l'ID du cache correspondant à une semaine et un groupe **/
	public static int getCacheId(int week, int group) {
		return (week << 24) | group;
	}
}
