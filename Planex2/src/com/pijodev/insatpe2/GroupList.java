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
	private static final int currentCacheVersion = 2;
	
	/** Ensemble des groupes identifiés par leur ID **/
	private static SparseArray<Group> mDefaultGroups = new SparseArray<>();
	/** Ensemble des groupes ajoutés par l'utilisateur **/
	private static SparseArray<Group> mUserGroups = new SparseArray<>();
	/** Liste complète des groupes **/
	private static ArrayList<Group> mListGroups = new ArrayList<>();
	
	private static boolean mIsLoaded = false, mHasChanged = false;
	
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
	/** Retourne vrai si le groupe associé à cette ID existe dans les listes */
	public static boolean isExisting(int id) {
		return getGroup(id) != null;
	}
	
	/** Indique si le groupe a été ajouté dans la liste par l'utilisateur */
	public static boolean isUserGroup(int id) {
		return mUserGroups.get(id) != null;
	}
	/** Ajoute un groupe dans la liste utilisateur 
	 * @throws Exception ID/nom de groupe déjà existant **/
	public static void addUserGroup(String name, int id) throws Exception {
		if(getGroup(id) != null)
			throw new Exception("Cet identifiant de groupe existe déjà !");
		for(Group g : mListGroups)
			if(g.name.equals(name))
				throw new Exception("Ce nom de groupe existe déjà !");
		Group grp = new Group(name, id);
		mUserGroups.put(id, grp);
		mListGroups.add(grp);
		mHasChanged = true;
	}
	/** Supprime un groupe de la liste utilisateur.
	 * Retourne vrai si la suppression a été effectuée avec succès **/
	public static boolean removeUserGroup(int id) {
		if(isUserGroup(id)) {
			mListGroups.remove(mUserGroups.get(id));
			mUserGroups.remove(id);
			mHasChanged = true;
			return true;
		}
		else
			return false;
	}
	
	/** Retourne la liste complète des groupes **/
	public static ArrayList<Group> getList() {
		return mListGroups;
	}
	
	/** Charge la liste des groupes mise en cache **/
	public static void load(Context context) {
		if(mIsLoaded)
			return;
		
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
			
			// Vérification de la version du xml
			int versionXml = dis.readInt();
			boolean xmlHasBeenUpdated = (versionXml != context.getResources().getInteger(R.integer.groups_xml_version));
			
			// Groupes par défaut
			int defCount = dis.readInt();
			for(int i = 0; i < defCount; i++) {
				// Nom
				bufferLength = dis.readInt();
				dis.read(buffer, 0, bufferLength);
				String name = new String(buffer, 0, bufferLength); 
				// Id
				int id = dis.readInt();
				
				// si l'xml a été mis à jour, on ne tient pas compte du contenu de ce fichier
				if(!xmlHasBeenUpdated) {
					// Création de l'objet et ajout dans les listes
					Group g = new Group(name, id);
					mDefaultGroups.put(id, g);
					mListGroups.add(g);
				}
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
		
		// Si la liste de groupe par défaut est vide (jamais enregistré,ou mise à jour de l'xml), on charge la liste xml
		if(mDefaultGroups.size() == 0)
			loadFromXML(context);
		mIsLoaded = true;
		mHasChanged = false;
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
		if(!mIsLoaded || !mHasChanged)
			return;
		
		byte[] buffer;
		try {
			DataOutputStream dos = new DataOutputStream(context.openFileOutput(groupsFileName, 0));
			// Version du format
			dos.writeInt(currentCacheVersion);
			// Version de l'XML
			dos.writeInt(context.getResources().getInteger(R.integer.groups_xml_version));
			
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
			mHasChanged = false;
		} catch (IOException e) {
		}
	}
	
	/** Met à jour la liste de groupe par défaut **/
	public static void update(ArrayList<Group> defaultGroups, Context context) {
		// On vide la liste
		mListGroups.clear();
		
		// On met à jour les groupes par défaut
		mDefaultGroups.clear();
		mListGroups.addAll(defaultGroups);
		for(Group g : defaultGroups) {
			// on évite d'avoir le même groupe dans les deux listes
			if(mUserGroups.get(g.id) != null)
				mUserGroups.remove(g.id);
			mDefaultGroups.put(g.id, g);
		}
		
		// On réinsère les groupes utilisateur
		for(int i = 0; i < mUserGroups.size(); i++)
			mListGroups.add(mUserGroups.valueAt(i));
		
		mHasChanged = true;
		
		// on enregistre tout de suite
		save(context);
	}
	
	/** Ajoute une groupe à la liste utilisateur **/
	public static void addGroup(int id, String name) {
		if(mUserGroups.get(id) == null) {
			mUserGroups.put(id, new Group(name, id));
			mHasChanged = true;
		}
	}
	
	
	static class Group {
		public String name;
		public int id;

		public Group(String name, int id) {
			this.name = name;
			this.id = id;
		}
	}
}
