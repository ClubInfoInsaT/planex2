package com.pijodev.insatpe2;


import java.util.ArrayList;

import android.content.res.Configuration;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pijodev.insatpe2.GroupSelectorView.OnGroupSelectedListener;

/**
 * 
 * @author Proïd
 *
 */
public class ToolBarView implements OnGroupSelectedListener {
	/** ViewGroup principal de la barre d'outils **/
	private LinearLayout mLayout;
	
	/** Bouton semaine précédente **/
	private ImageButton mPrevButton;
	/** Bouton semaine suivante **/
	private ImageButton mNextButton;
	/** Bouton semaine courante **/
	private ImageButton mCurrentButton;
	
	/** Vue contenant les listes des vues associées aux groupes séléctionnés **/
	private LinearLayout mGroupsLayout;
	/** Outils de sélection de groupe **/
	private GroupSelectorView mGroupSelector;
	/** Référence vers la session utilisateur **/
	private UserSession mUserSession;
	
	public ToolBarView(ScheduleActivity activity) {
		mLayout = (LinearLayout) activity.findViewById(R.id.ll_settings);
		// On cache cette barre (passage en mode plein écran) si on est en mode paysage
		setVisible(activity.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE);
		
		initWeekButtons(activity);
		initGroupViews(activity);

		mUserSession = activity.getSession();
		initGroups(mUserSession.getGroups());
		
		updateCurrentButtonState();
	}
	
	/** Change la visibilité de la barre (VISIBLE/GONE) pour le mode plein écran **/
	public void setVisible(boolean visible) {
		mLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
	}
	
	/**** Gestion de la sélection de groupes ****/
	
	/** Initialise les vues associées à la sélection de groupes **/
	private void initGroupViews(final ScheduleActivity activity) {
		mGroupSelector = new TreeGroupSelectorView(activity);
		mGroupSelector.setOnGroupSelectedListener(this);
		
		mGroupsLayout = (LinearLayout) mLayout.findViewById(R.id.ll_groups);
		mGroupsLayout.getChildAt(0).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mUserSession.getGroups().size() >= 9)
					EasterEggs.ee2(activity);
				else
					mGroupSelector.show(-1, -1);
			}
		});
	}
	
	/** Retourne le nombre de groupes présents dans le liste **/
	private int getNbGroup() {
		return mGroupsLayout.getChildCount() - 1;
	}
	
	/** Fonction appelée lorsque un groupe a été sélectionné pour un des éléments de la liste
	 * @param groupId ID du groupe sélectionné, -1 si demande de suppression
	 * @param target convention : numéro de groupe dans la liste, -1 pour nouveau groupe **/
	@Override
	public void onGroupSelected(int groupId, int target) {
		if(target == -1) {
			// ajout d'un groupe
			if(groupId != -1)
				addGroup(groupId);
		}
		else {
			// Suppression du groupe
			if(groupId == -1) {
				deleteGroup(target);
			}
			// Modification du groupe
			else {
				changeGroup(groupId, target);
			}
		}
	}

	/** Lors de la phase d'initialisation, ajoute un ensemble de groupes **/
	private void initGroups(ArrayList<Integer> groupsId) {
		for(int index = 0; index < groupsId.size(); index++)
			mGroupsLayout.addView(createView(groupsId.get(index), index), index);
	}
	
	/** Ajoute un nouveau groupe dans la liste **/
	private void addGroup(int groupId) {
		int index = getNbGroup();
		mGroupsLayout.addView(createView(groupId, index), index);
		
		// Ajout du groupe dans les paramètres de session
		mUserSession.addGroups(groupId);
	}
	/** Supprime un groupe de la liste **/
	private void deleteGroup(int index) {
		// suppression de la vue
		mGroupsLayout.removeView(mGroupsLayout.findViewById(index));
		// mise à jour des id des autres vues
		for(int i = 0; i < getNbGroup(); i++) {
			mGroupsLayout.getChildAt(i).setId(i);
		}
		
		// Suppression du groupe dans les paramètres de session
		mUserSession.removeGroup(index);
	}
	/** Modifie un groupe de la liste **/
	private void changeGroup(int groupId, int index) {
		Button b = (Button) mGroupsLayout.findViewById(index);
		b.setText(GroupList.getGroupName(groupId));

		// Mise à jour du groupe dans les paramètres de session
		mUserSession.changeGroup(groupId, index);
	}
	
	/** Créer une vue pour un groupe **/
	private View createView(int groupId, int viewId) {
		// Création d'un boutton
		Button b = new Button(mGroupsLayout.getContext());
		b.setText(GroupList.getGroupName(groupId));
		b.setBackgroundResource(R.drawable.button_text);
		b.setId(viewId);
		b.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		
		// Au clic, on affiche le fenêtre de sélection de groupe
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mGroupSelector.show(v.getId(), mUserSession.getGroups().get(v.getId()));
			}
		});
		
		return b;
	}
	
	
	/**** Boutons de changement de semaine ****/
	
	/** Initialise les boutons associés au changement de semaine **/
	private void initWeekButtons(final ScheduleActivity activity) {
		mPrevButton = (ImageButton) mLayout.findViewById(R.id.ib_prev);
		mNextButton = (ImageButton) mLayout.findViewById(R.id.ib_next);
		mCurrentButton = (ImageButton) mLayout.findViewById(R.id.ib_current);
		
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v == mPrevButton) {
					if(EasterEggs.ee3_locked())
						EasterEggs.ee3((RelativeLayout)activity.findViewById(R.id.rl_main_view), 0);
					else {
						boolean bound = mUserSession.addRelWeek(-1);
						EasterEggs.ee3_poke(bound);
					}
				}
				else if(v == mNextButton) {
					if(EasterEggs.ee3_locked())
						EasterEggs.ee3((RelativeLayout)activity.findViewById(R.id.rl_main_view), 2);
					else {
						boolean bound = mUserSession.addRelWeek(+1);
						EasterEggs.ee3_poke(bound);
					}
				}
				else {
					if(EasterEggs.ee3_locked())
						EasterEggs.ee3((RelativeLayout)activity.findViewById(R.id.rl_main_view), 1);
					else {
						mUserSession.resetRelWeek();
						EasterEggs.ee3_poke(false);
					}
				}
			}
		};
		mPrevButton.setOnClickListener(listener);
		mNextButton.setOnClickListener(listener);
		mCurrentButton.setOnClickListener(listener);
	}
	
	/** Change l'état du bouton 'semaine actuelle' (enabled/disabled) **/
	public void updateCurrentButtonState() {
		// Le bouton 'semaine courante' est désactivé si on est positionné à la semaine courante
		mCurrentButton.setEnabled(mUserSession.getRelWeek() != 0);
	}

	
}
