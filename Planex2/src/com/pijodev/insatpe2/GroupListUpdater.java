package com.pijodev.insatpe2;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.os.AsyncTask;
import android.widget.Toast;

import com.pijodev.insatpe2.GroupList.Group;

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
	private void startUpdating() {
		// Si un processus avait déjà été lancé auparavant, on s'assure qu'il est terminé
		if(mAsyncTask != null) {
			mAsyncTask.cancel(true);
		}
		
		// Création de la tâche asynchrone
		mAsyncTask = new AsyncTask<Void, Void, ArrayList<Group>>() {
			/** Télécharge les données depuis planning express et les met sous
			 * forme d'arraylist, le tout de manière asynchrone **/
			@Override
			protected ArrayList<Group> doInBackground(Void... params) {
				ArrayList<Group> list = new ArrayList<>();
				// TODO requête http
				try {
					Thread.sleep(1000*3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(isCancelled())
					return null;
				try {
					Thread.sleep(1000*3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// TODO parsing
				return null;
			}
			
			/** Fonction exécutée dans le thread de la GUI lorsque le chargement est terminé **/
			protected void onPostExecute(ArrayList<Group> result) {
				// Téléchargement réussi
				if(result != null) {
					GroupList.update(result);
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
			
		};
		
		// démarrage du téléchargement 
		mAsyncTask.execute();
	}

}
