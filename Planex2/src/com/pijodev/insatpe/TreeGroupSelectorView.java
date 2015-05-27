package com.pijodev.insatpe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pijodev.insatpe.R;
import com.pijodev.insatpe.GroupList.Group;
import com.pijodev.insatpe.GroupListUpdater.OnGroupListUpdatedListener;
import com.pijodev.insatpe.NewUserGroupDialog.OnNewUserGroupCreatedListener;

/**
 * TODO inteface propre, bouton charger liste,
 * historique, supprimer, ajouter group perso,
 * positionnement init auto
 * @author Proïd
 *
 */
public class TreeGroupSelectorView extends GroupSelectorView
		implements OnItemClickListener, OnNewUserGroupCreatedListener, OnItemLongClickListener, OnGroupListUpdatedListener {
	private AlertDialog mDialog;
	private LinearLayout mPathView;
	private View mDeleteTarget;
	private Adapter mListAdapter;
	private LinkedList<GroupNameTree> mPath = new LinkedList<>();
	private GroupNameTree mRoot;
	private Context mContext;
	
	@SuppressLint("InflateParams")
	public TreeGroupSelectorView(Context c) {
		mContext = c;
		Builder builder = new Builder(mContext);
		
		mRoot = GroupNameTree.buildTree();
		mPath.addLast(mRoot);
		
		ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.group_tree_selector, null);
		
		// Titre
		mPathView = (LinearLayout) view.findViewById(R.id.ll_group_path);
		
		// Bouton téléchargement 
		View update = view.findViewById(R.id.b_group_list_update);
		final GroupListUpdater listUpdaterDialog = new GroupListUpdater(mContext, this);
		update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listUpdaterDialog.show();
			}
		});
		// Bouton nouveau groupe
		View add = view.findViewById(R.id.b_group_list_add);
		final NewUserGroupDialog newGroupDialog = new NewUserGroupDialog(mContext, this);
		add.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				newGroupDialog.show();
			}
		});
		// Bouton supprimer la cible
		mDeleteTarget = view.findViewById(R.id.ib_group_delete);
		mDeleteTarget.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				if(mGroupSelectedListener != null)
					mGroupSelectedListener.onGroupSelected(-1, mTarget);
				mDialog.dismiss();
			}
		});
		
		
		// Liste
		ListView list = (ListView) view.findViewById(R.id.lv_groups_list);
		mListAdapter = new Adapter();
		list.setOnItemClickListener(this);
		list.setOnItemLongClickListener(this);
		list.setAdapter(mListAdapter);
		
		mDialog = builder.setView(view).create();
		applyPath();
	}
	
	/** Fonction appelée lorsqu'on clique sur un élément de la liste **/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(position < mPath.getLast().branches.size()) {
			GroupNameTree g = mPath.getLast().branches.get(position);
			if(g.branches == null) {
				if(mGroupSelectedListener != null)
					mGroupSelectedListener.onGroupSelected(g.id, mTarget);
				mDialog.dismiss();
			} else {
				mPath.addLast(g);
				applyPath();
			}
		}
		else {
			if(mGroupSelectedListener != null)
				mGroupSelectedListener.onGroupSelected(mPath.getLast().id, mTarget);
			mDialog.dismiss();
		}
	}
	/** Fonction appelée lorsqu'on fait un clic long sur un élément de la liste **/
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long viewId) {
		GroupNameTree grp = mPath.getLast();
		final int id = position >= grp.branches.size() ? grp.id : (grp.branches.get(position).branches == null ? grp.branches.get(position).id : -1);
		if(GroupList.isUserGroup(id)) {
			new Builder(mContext)
				.setTitle("Supprimer")
				.setMessage("Supprimer "+GroupList.getGroupName(id)+" ?")
				.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
					@Override public void onClick(DialogInterface dialog, int which) {
						removeUserGroup(id);
					}
				})
				.setNegativeButton("Annuler", null)
				.setCancelable(true)
				.show();
			return true;
		}
		
		return false;
	}
	
	/** Fonction appelée lorsqu'un nouveau groupe utilisateur a été ajouté à GroupList **/
	@Override
	public void onNewUserGroupCreated(String name, int id) {
		mPath.clear();
		mRoot = GroupNameTree.buildTree();
		mRoot.getPath(id, mPath);
		applyPath();
	}
	/** Fonction appelée lorsque la création d'un groupe a échoué **/
	@Override
	public void onNewUserGroupFailed(String name, Integer id) {
		// Si l'id est valide, l'erreur vient du fait que l'id demandé existe déjà
		if(id != null && GroupList.isExisting(id)) {
			// On se place alors à l'endroit de l'id (~fonction recherche)
			mPath.clear();
			mRoot.getPath(id, mPath);
			applyPath();
		}
	}
	
	/** Fonction appelée lorsque la mise à jour de la liste a été téléchargé **/
	@Override
	public void onGroupListUpdated() {
		mRoot = GroupNameTree.buildTree();
		mPath.clear();
		mPath.addLast(mRoot);
		applyPath();
	}
	
	/** Si l'ID est différent de -1, positionne le chemin afin d'avoir accès à l'ID un clic
	 * Affiche la fenêtre. **/
	public void show(int target, int defaultId) {
		mTarget = target;
		
		if(defaultId != -1) {
			// le bouton 'supprimer la cible' est disponible
			mDeleteTarget.setEnabled(true);
			// On se positionne correctement
			mPath.clear();
			if(!mRoot.getPath(defaultId, mPath))
				mPath.addLast(mRoot);
			applyPath();
		}
		else {
			// On cherche à ajouter un nouveau groupe, le bouton 'supprimer la cible' n'est pas utile
			mDeleteTarget.setEnabled(false);
		}
		
		mDialog.show();
	}
	
	/** Supprime un groupe ajouté par l'utilisateur.
	 * Met à jour l'arbre, la liste statique et le path **/
	private void removeUserGroup(int id) {
		// suppression dans la liste statique, abandon si échec
		if(!GroupList.removeUserGroup(id))
			return;
		
		// teste posssibilité de supprimer le groupe sans modifier la structure de l'arbre
		if(!mPath.getLast().remove(id)) {
			// Reconstruction de l'arbre avec la nouvelle liste (ouais, je sais, ça se fait pas de tout reconstruire juste pour ça)
			mRoot = GroupNameTree.buildTree();
			
			// Mise à jour du chemin
			mPath.clear();
			mPath.addLast(mRoot);
		}
		
		applyPath();
	}
	
	/** Se positionne dans l'arbre en fonction du chemin. Met également à jour le titre **/
	private void applyPath() {
		// Reconstitution du titre
		mPathView.removeAllViews();
		for(GroupNameTree grp : mPath) {
			mPathView.addView(createTitlePart(grp));
		}
		
		// Mise à jour le contenu de la liste 
		mListAdapter.notifyDataSetChanged();
	}
	
	/** Crée un vue réprésentant un partie du chemin **/
	private View createTitlePart(final GroupNameTree grp) {
		TextView tv = new TextView(mContext);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
		
		// texte associé
		tv.setText((grp.name == null) ? " ~ \u232A " : grp.name+" \u232A ");
		
		// Changement du path lorsqu'on clique dessus
		tv.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				// on remonte jusqu'au niveau correspondant
				while(mPath.size() > 0 && mPath.getLast() != grp)
					mPath.removeLast();
				applyPath();
			}
		});
		
		return tv;
	}
	
	/** Classe permettant de gérer le contenu de la liste en fonction du path **/
	private class Adapter implements ListAdapter {
		// Pour permettre de modifier le contenu de la liste
		private ArrayList<DataSetObserver> observers = new ArrayList<DataSetObserver>();
		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
		    observers.add(observer);
		}
		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			observers.remove(observer);
		}
		/** Permet de mettre à jour la liste lorsque les données ont changé **/
		public void notifyDataSetChanged(){
		    for (DataSetObserver observer: observers) {
		        observer.onChanged();
		    }
		}
		
		@Override
		public boolean isEmpty() {
			return getCount() == 0;
		}
		@Override
		public boolean hasStableIds() {
			return true;
		}
		@Override
		public int getViewTypeCount() {
			return 1;
		}
		
		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewGroup view;
			if(convertView != null)
				view = (ViewGroup) convertView;
			else
				view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.item_group_list, null);

			// Text
			((TextView)view.getChildAt(0)).setText((String) getItem(position));
			
			// info supprimer
			GroupNameTree grp = mPath.getLast();
			int id = position >= grp.branches.size() ? grp.id : (grp.branches.get(position).branches == null ? grp.branches.get(position).id : -1);
			TextView tv = (TextView)view.getChildAt(1);
			if(isEnabled(position) && GroupList.isUserGroup(id)) {
				tv.setVisibility(View.VISIBLE);
				tv.setTextColor((tv.getTextColors().getDefaultColor()&0x00ffffff) | 0x88000000);
			}
			else {
				tv.setVisibility(View.INVISIBLE);
			}
			
			return view;
		}
		
		@Override
		public int getItemViewType(int position) {
			return 0;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public Object getItem(int position) {
			if(!isEnabled(position))
				return "";
			
			// ID du chemin actuel
			if(position >= mPath.getLast().branches.size())
				return ". ["+mPath.getLast().id+"]";
			
			GroupNameTree grp = mPath.getLast().branches.get(position);
			if(grp.id != -1 && grp.branches == null)
				return grp.name+" ["+grp.id+"]";
			else
				return grp.name;
		}
		@Override
		public int getCount() {
			return getRealCount(); //mRoot.maxBranchSize; // décommenter ceci pour éviter les redimensionnements fréquents de la fenêtre
		}
		// Retourne le nombre d'item actif
		public int getRealCount() {
			return mPath.getLast().branches.size() + (mPath.getLast().id != -1 ? 1 : 0);
		}
		@Override
		public boolean isEnabled(int position) {
			return position < getRealCount();
		}
		@Override
		public boolean areAllItemsEnabled() {
			return getCount() == getRealCount();
		}
	}
	
	private static class GroupNameTree {
		int id = -1;
		String name;
		ArrayList<GroupNameTree> branches;
		int maxBranchSize = 0;
		
		static GroupNameTree buildTree() {
			GroupNameTree root = new GroupNameTree();
			
			for(Group grp : GroupList.getList())
				add(grp.name, grp.id, root);
			
			root.sortAndCount();

			return root;
		}
		
		/** Trouve le chemin complet pour accéder à l'ID donné
		 * Retourne vrai si l'ID a été trouvé **/
		private boolean getPath(int id, LinkedList<GroupNameTree> path) {
			if(this.id == id)
				return true;
			
			path.addLast(this);
			
			if(branches != null) {
				for(GroupNameTree g : branches)
					if(g.getPath(id, path))
						return true;
			}
			
			path.removeLast();
			
			return false;
		}
		
		/** Affiche l'arbre dans le logcat pour le debug ** /
		private void print(String tab) {
			Log.i("###", tab+name+" "+id);
			if(branches != null)
				for(GroupNameTree g : branches)
					g.print(tab+"+---");
		}*/
		
		/** Trie chaque branche par ordre alphabétique et calcule la largeur de la branche la plus grande **/
		private void sortAndCount() {
			if(branches != null) {
				// Tri
				Collections.sort(branches, new Comparator<GroupNameTree>() {
					@Override public int compare(GroupNameTree lhs, GroupNameTree rhs) {
						return lhs.name.compareTo(rhs.name);
					}
				});
				// Parcours récursif et calcul de largeur max
				maxBranchSize = branches.size() + (id != -1 ? 1 : 0);
				for(GroupNameTree g : branches) {
					g.sortAndCount();
					maxBranchSize = Math.max(maxBranchSize, g.maxBranchSize);
				}
			}
		}
		
		/** Ajoute un groupe dans l'arbre. L'arbre peut changer de forme. **/
		static void add(String name, int id, GroupNameTree tree) {
			/// /!\ conflit pour nom déjà existant dans l'arbre
			if(tree.branches != null) {
				for(GroupNameTree b : tree.branches) {
					int common = commonWord(name, b.name);
					if(common == b.name.length() && common == name.length()) {
						b.id = id; // nom déjà existant, on garde le dernier id
						return;
					}
					else if(common == b.name.length()) {
						add(name.substring(common+1), id, b);
						return;
					}
					else if (common == name.length()) {
						GroupNameTree t = new GroupNameTree();
						t.id = b.id;
						t.name = b.name.substring(common+1);
						t.branches = b.branches;
						b.id = id;
						b.name = name;
						b.branches = new ArrayList<>();
						b.branches.add(t);
						return;
					}
					else if(common > 0){
						GroupNameTree t = new GroupNameTree();
						t.id = b.id;
						t.name = b.name.substring(common+1);
						t.branches = b.branches;
						b.branches = new ArrayList<>();
						b.name = name.substring(0, common);
						b.id = -1;
						b.branches.add(t);
						t = new GroupNameTree();
						t.id = id;
						t.name = name.substring(common+1);
						b.branches.add(t);
						return;
					}
					else {
						// common == 0, on poursuit le parcours de l'arraylist
					}
				}
			}
			else {
				tree.branches = new ArrayList<>();
			}

			GroupNameTree t = new GroupNameTree();
			t.id = id;
			t.name = name;
			tree.branches.add(t);
		}
		
		/** Retire un groupe de l'arbre si cela est possible sans modifier la structure de l'arbre
		 * Retourne vrai si l'opération a réussi **/
		public boolean remove(int id) {
			// Si l'id correspond à cette branche
			if(this.id == id) {
				if(this.branches != null && branches.size() > 1) {
					this.id = -1;
					return true;
				} else {
					return false;
				}
			}
				
			// recherche de l'id dans les branches directes
			for(int i = 0; i < branches.size(); i++)
				if(branches.get(i).id == id) {
					if(branches.get(i).branches != null) {
						// la structure de l'arbre aurait besoin de changer (fusion de 2 branches alignées)
						if(branches.get(i).branches.size() == 1)
							return false;
						// on peut supprimer l'id sans modifier la structure
						else {
							branches.get(i).id = -1;
							return true;
						}
					}
					else {
						// la structure de l'arbre aurait besoin de changer (une seule branche qui serait à fusionner)
						if(branches.size() <= 2)
							return false;
						// on supprime une feuille
						else {
							branches.remove(i);
							return true;
						}
					}
				}
			
			// id introuvé dans l'arbre
			return false;
		}
		
		/** Retourne la position du dernier espace délimitant les parties communes aux deux string **/
		private static int commonWord(String s, String t) {
			int lastCommonSpace = 0;
			s += ' ';
			t += ' ';
			for(int i = 0; i < Math.min(s.length(), t.length()); i++) {
				if(s.charAt(i) != t.charAt(i))
					break;
				if(s.charAt(i) == ' ')
					lastCommonSpace = i;
			}
			
			return lastCommonSpace;
		}
	}


}
