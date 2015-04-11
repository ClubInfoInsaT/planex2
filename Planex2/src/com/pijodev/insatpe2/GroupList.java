package com.pijodev.insatpe2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

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
	private static final String groupsFileName = "groups.data";
	/** Version du format du fichier. A modifier si le format évolue **/
	private static final int currentCacheVersion = 1;
	
	/** Ensemble des groupes identifiés par leur ID **/
	private static SparseArray<Group> mDefaultGroups = new SparseArray<>();
	/** Ensemble des groupes ajoutés par l'utilisateur **/
	private static SparseArray<Group> mUserGroups = new SparseArray<>();
	/** Liste complète des groupes **/
	private static ArrayList<Group> mListGroups = new ArrayList<>();
	
	/** Retourne le nom du groupe correspondant à l'id.
	 *  Retourne null si l'id est inconnu **/
	public static String getGroupName(int id) {
		Group g = getGroup(id);
		if(g != null)
			return g.name;
		
		return null;
	}
	/** Retourne le groupe associé à cet ID
	 *  Retourne null si l'id est inconnu **/
	private static Group getGroup(int id) {
		Group g = mDefaultGroups.get(id);
		if(g != null)
			return g;
		
		g = mUserGroups.get(id);
		if(g != null)
			return g;
		
		return null;
	}
	
	/** Retourne la liste complète des groupes **/
	public static ArrayList<Group> getList() {
		return mListGroups;
	}
	
	/** Charge la liste des groupes mise en cache **/
	public static void load(Context context) {
		mDefaultGroups.clear();
		mUserGroups.clear();
		mListGroups.clear();
		
		int bufferLength;
		byte[] buffer = new byte[512];
		try {
			DataInputStream dis = new DataInputStream(context.openFileInput(groupsFileName));
			// Vérification de la version du format
			int version = dis.readInt();
			if(version != currentCacheVersion)
				throw new DataFormatException();
			
			// Groupes par défaut
			int defCount = dis.readInt();
			for(int i = 0; i < defCount; i++) {
				// Nom
				bufferLength = dis.readInt();
				dis.read(buffer, 0, bufferLength);
				String name = new String(buffer, 0, bufferLength); 
				// Id
				int id = dis.readInt();
				// Création de l'objet et ajout dans les listes
				Group g = new Group(name, id);
				mDefaultGroups.put(id, g);
				mListGroups.add(g);
			}
			
			// Groupes utilisateur
			int userCount = dis.readInt();
			for(int i = 0; i < userCount; i++) {
				// Nom
				bufferLength = dis.readInt();
				dis.read(buffer, 0, bufferLength);
				String name = new String(buffer, 0, bufferLength); 
				// Id
				int id = dis.readInt();
				// Création de l'objet et ajout dans les listes
				Group g = new Group(name, id);
				mUserGroups.put(id, g);
				mListGroups.add(g);
			}
			dis.close();
		} catch (IOException e) {
		} catch (DataFormatException e) {
			// Le format du fichier n'est pas à jour -> on supprime le fichier
			new File(context.getFilesDir(), groupsFileName).delete();
		}
		
		// Si la liste de groupe par défaut est vide, on charge la liste 
		if(mDefaultGroups.size() == 0)
			loadFromXML(context);
	}
	/** Charge la liste des groupes par défaut depuis les fichiers XML */
	private static void loadFromXML(Context context) {
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
			for(int j = 0; j < names[i].length; j++) {
				Group g = new Group(""+(i+1)+" "+names[i][j], ids[i][j]);
				mDefaultGroups.put(ids[i][j], g);
				mListGroups.add(g);
			}
	}
	
	/** Enregistre la liste des groupes **/
	public static void save(Context context) {
		byte[] buffer;
		try {
			DataOutputStream dos = new DataOutputStream(context.openFileOutput(groupsFileName, 0));
			// Version du format
			dos.writeInt(currentCacheVersion);
			
			// Groupes par défaut
			dos.writeInt(mDefaultGroups.size());
			for(int i = 0; i < mDefaultGroups.size(); i++) {
				// Nom
				buffer = mDefaultGroups.valueAt(i).name.getBytes();
				dos.writeInt(buffer.length);
				dos.write(buffer);
				// Id 
				dos.writeInt(mDefaultGroups.valueAt(i).id);
			}
			
			// Groupes utilisateur
			dos.writeInt(mUserGroups.size());
			for(int i = 0; i < mUserGroups.size(); i++) {
				// Nom
				buffer = mUserGroups.valueAt(i).name.getBytes();
				dos.writeInt(buffer.length);
				dos.write(buffer);
				// Id 
				dos.writeInt(mUserGroups.valueAt(i).id);
			}
			dos.close();
		} catch (IOException e) {
		}
	}
	
	/** Charge la liste officielle des groupes depuis planning express **/
	public static void update() {
		// TODO
	}
	
	/** Ajoute une groupe à la liste utilisateur **/
	public static void addGroup(int id, String name) {
		if(mUserGroups.get(id) == null) {
			mUserGroups.put(id, new Group(name, id));
		}
	}
	
	/** TODO compléter et utiliser ceci
	 * Retourne la liste des groupes dont le nom du groupe contient la chaîne de caractère donnée ** /
	public LinkedList<Group> filter(String keyword) {
		LinkedList<Group> list = new LinkedList<>();
		keyword = keyword.toLowerCase(Locale.getDefault()).replace(" ", "");
		for(Group g : mListGroups) {
			if(g.name.toLowerCase(Locale.getDefault()).replace(" ", "").contains(keyword))
				list.add(g);
		}
		
		return list;
	}*/
	
	static class Group {
		public String name;
		public int id;

		public Group(String name, int id) {
			this.name = name;
			this.id = id;
		}
	}
}
