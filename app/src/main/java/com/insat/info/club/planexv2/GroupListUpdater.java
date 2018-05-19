package com.insat.info.club.planexv2;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.insat.info.club.planexv2.GroupList.Group;

/**
 * Chargement de la liste de groupe par défaut depuis planning express +
 * Affichage d'une fenêtre de chargement.
 * @author Proïd
 *
 */
public class GroupListUpdater {
	private ProgressDialog mDialog;
	private Context mContext;
	private OnGroupListUpdatedListener mListener;
	private AsyncTask<Void, Void, ArrayList<Group>> mAsyncTask;

	public GroupListUpdater(Context c, OnGroupListUpdatedListener listener) {
		mContext = c;
		mListener = listener;
		buildDialog();
	}
	
	/** Crée la fenêtre de chargement **/
	private void buildDialog() {
		mDialog = new ProgressDialog(mContext);
		mDialog.setTitle("Mise à jour de la liste des groupes");
		mDialog.setIndeterminate(true);
		mDialog.setMessage("Téléchargement en cours");
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(true);
		mDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Annuler", (OnClickListener)null);
		mDialog.setOnDismissListener(new OnDismissListener() {
			@Override public void onDismiss(DialogInterface dialog) {
				mAsyncTask.cancel(false);
			}
		});
		mDialog.setOnShowListener(new OnShowListener() {
			@Override public void onShow(DialogInterface dialog) {
				startUpdating();
			}
		});
	}
	
	/** Affiche la fenêtre de chargement **/
	public void show() {
		mDialog.show();
	}
	
	public interface OnGroupListUpdatedListener {
		public void onGroupListUpdated();
	}

	/** Lance le téléchargement asynchrone **/
	//TODO : staticfieldLeak. To erase if necessary
	@SuppressLint("StaticFieldLeak")
	private void startUpdating() {
		// Si un processus avait déjà été lancé auparavant, on s'assure qu'il est terminé
		if(mAsyncTask != null) {
			mAsyncTask.cancel(true);
		}
		
		// Création de la tâche asynchrone
		mAsyncTask = new AsyncTask<Void, Void, ArrayList<Group>>() {
			/** Télécharge les données depuis etud et les met sous
			 * forme d'arraylist, le tout de manière asynchrone **/
			@Override
			protected ArrayList<Group> doInBackground(Void... params) {
				// Chargement de la liste depuis etud
				String file = download("https://www.etud.insa-toulouse.fr/planex/api.php?PIPIKK=Pr0ut!");

				// On abandonne si la tâche a déjà été annulée 
				if(isCancelled() || file == null)
					return null;
				
				// Parse le fichier
				String[] lines = file.split("\n");
				ArrayList<Group> groups = new ArrayList<>();
				// Parcours du fichier ligne par ligne
				for(String line : lines) {
					line = line.trim();
					
					// Suppression du dièse si présent
					if(line.charAt(0) == '#')
						line = line.substring(1);
					
					// On vérifie que le format de la ligne est valide
					if(line.matches("[^:]+:\\s*\\d+(\\s+\\d+)*")) {
						String fields[] = line.split(":");
						
						// Nom du groupe
						String name = fields[0].trim().replaceAll("\\s+", " ");
						
						// Id(s) associé(s)
						String num[] = fields[1].split("\\s");
						ArrayList<Integer> ids = new ArrayList<>();
						for(String s : num) {
							try {
								Integer id = Integer.valueOf(s);
								ids.add(id);
							} catch (NumberFormatException e) {
								
							}
						}
						
						// Ajout du groupe dans la liste
						if(ids.size() > 0) {
							if(ids.size() == 1)
								groups.add(new Group(name, ids.get(0)));
							else {
								// Ajout d'un numéro au nom des groupes pour les différenciés
								for(int i = 0; i < ids.size(); i++)
									groups.add(new Group(name+" ("+(i+1)+")", ids.get(i)));
							}
							Log.i("###", groups.get(groups.size()-1).name+" "+groups.get(groups.size()-1).id);
						}
					}
					
				}
				
				return groups;
			}
			
			/** Fonction exécutée dans le thread de la GUI lorsque le chargement est terminé **/
			protected void onPostExecute(ArrayList<Group> result) {
				// Téléchargement réussi
				if(result != null) {
					MyToast.show(mContext, "Mise à jour réussie", Toast.LENGTH_SHORT);
					// Mise à jour de la liste static
					GroupList.update(result, mContext);
					// Notification de mise à jour dans le tree group selector
					if(mListener != null)
						mListener.onGroupListUpdated();
				}
				// Echec de téléchagement
				else {
					MyToast.show(mContext, "Echec ! :(", Toast.LENGTH_SHORT);
				}
				// On ferme la boîte de chargement
				if(mDialog.isShowing())
					mDialog.dismiss();
			}
			
			/** Downloads a file with the given url. Returns the file as a string or null if an error occurs **/
			public String download(String url_string) {
				// Create the url
				URL url;
				try {
					url = new URL(url_string);
				} catch (MalformedURLException e1) {
					return null;
				}
				
				String data = null;
				try {
					// Connect and open an input stream
					InputStream stream = url.openStream();
					// Download the data
					data = new String();
					byte[] buffer = new byte[1024];
					int length;
					while((length = stream.read(buffer)) > 0)
						data += new String(buffer, 0, length);

					stream.close();
				} catch (IOException e) {
					
				}
				
				return data;
			}
		};
		
		// démarrage du téléchargement 
		mAsyncTask.execute();
	}

}
