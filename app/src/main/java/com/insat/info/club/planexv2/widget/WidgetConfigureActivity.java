package com.insat.info.club.planexv2.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.insat.info.club.planexv2.GroupSelectorView.OnGroupSelectedListener;
import com.insat.info.club.planexv2.EasterEggs;
import com.insat.info.club.planexv2.GroupList;
import com.insat.info.club.planexv2.MyToast;
import com.insat.info.club.planexv2.R;
import com.insat.info.club.planexv2.TreeGroupSelectorView;
import com.insat.info.club.planexv2.UserSession;

/**
 * Activité de configuration initiale du widget.
 * -> https://laaptu.wordpress.com/2013/07/19/android-app-widget-with-listview/
 * -> http://www.maraumax.fr/pages-3-creation-d-un-widget-parametrable-sur-android.html
 * 
 * @author Proïd
 *
 */
public class WidgetConfigureActivity extends Activity implements OnGroupSelectedListener {
	/** Identifiant du widget créé **/
	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	
	private LinearLayout mListGroup;
	private EditText mTitleEdit;
	
	private TreeGroupSelectorView mGroupSelector;
	private UserSession mSession;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_widgetconf);
		
		setResult(RESULT_CANCELED);
		
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			Toast.makeText(this, "Nécessite Android 3.2 minimum :(", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		// On récurpère l'id du widget à créer
		Bundle extras = getIntent().getExtras();
		if(extras != null)
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		// On abandonne la configuration si on a pas d'ID de widget valide
		if(mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
			finish();
		else {
			GroupList.load(this);
			mGroupSelector = new TreeGroupSelectorView(this);
			mGroupSelector.setOnGroupSelectedListener(this);
			initViews();
			mSession = new UserSession(mAppWidgetId, this);
			// On efface les données si il en reste d'un précédant widget
			mSession.setGroups(new ArrayList<Integer>());
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		GroupList.save(this);
	}
	
	/** Initialise les vues de l'interface **/
	private void initViews() {
		// Liste des groupes
		mListGroup = (LinearLayout) findViewById(R.id.ll_widget_group_list);
		// Saisi du titre du widget
		mTitleEdit = (EditText) findViewById(R.id.et_title_widget);
		
		// Bouton d'ajout de groupe
		((Button) findViewById(R.id.b_add_group_widget)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mSession.getGroups().size() >= 9)
					EasterEggs.ee2(WidgetConfigureActivity.this);
				else
					mGroupSelector.show(-1, -1);
			}
		});
		
		// Validation
		((Button) findViewById(R.id.b_create_widget)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mSession.getGroups().size() == 0) {
					MyToast.show(WidgetConfigureActivity.this, "Choississez au moins un groupe.", Toast.LENGTH_SHORT);
				}
				else {
					String title = mTitleEdit.getText().toString();
					if(title.length() == 0) {
						MyToast.show(WidgetConfigureActivity.this, "Entrez un titre.", Toast.LENGTH_SHORT);
					} else {
						mSession.setTitle(title);
						confirmAndExit();
					}
				}
			}
		});
		
		EasterEggs.ee6(findViewById(R.id.iv_ee6));
	}

	/** Mise à jour du widget **/
    private void confirmAndExit()
    {
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);
		finish();

		mSession.save(this);
        new WidgetProvider()
        .onUpdate(this,
                  AppWidgetManager.getInstance(this),
                  new int[] { mAppWidgetId }
         );
        this.finish();
    }
    
    /** Met à jour le titre du widget avec les groupes actuellement sélectionnés **/
    private void updateTitle() {
    	String s = "";
    	if(mSession.getGroups().size() > 0) {
	    	for(Integer id : mSession.getGroups())
	    		s += GroupList.getGroupName(id) + " & ";
	    	// on supprime le dernier " & " inutile
	    	s = s.substring(0, s.length()-3);
    	}
    	mTitleEdit.setText(s);
    }
    
	/** Créer une vue pour un groupe **/
    private View createGroupView(int groupId, int viewId) {
    	Button button = new Button(this);
    	button.setBackgroundResource(R.drawable.button_text9);
    	button.setId(viewId);
    	button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    	button.setText(GroupList.getGroupName(groupId));
    	button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mGroupSelector.show(v.getId(), mSession.getGroups().get(v.getId()));
			}
		});
    	return button;
    }
    
    /** Fonction appelée lorsque un groupe a été sélectionné pour un des éléments de la liste
	 * @param groupId ID du groupe sélectionné, -1 si demande de suppression
	 * @param target convention : numéro de groupe dans la liste, -1 pour nouveau groupe **/
	@Override
	public void onGroupSelected(int groupId, int target) {
		if(target == -1) {
			if(groupId != -1) {
				addGroup(groupId);
			}
		} else {
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
	
	/** Ajoute un nouveau groupe dans la liste **/
	private void addGroup(int groupId) {
		int index = getNbGroup();
		mListGroup.addView(createGroupView(groupId, index), index);
		
		// Ajout du groupe dans les paramètres de session
		mSession.addGroups(groupId);
		// On met à jour le titre du widget
		updateTitle();
	}
	/** Supprime un groupe de la liste **/
	private void deleteGroup(int index) {
		// suppression de la vue
		mListGroup.removeView(mListGroup.findViewById(index));
		// mise à jour des id des autres vues
		for(int i = 0; i < getNbGroup(); i++) {
			mListGroup.getChildAt(i).setId(i);
		}
		
		// Suppression du groupe dans les paramètres de session
		mSession.removeGroup(index);
		// On met à jour le titre du widget
		updateTitle();
	}
	/** Modifie un groupe de la liste **/
	private void changeGroup(int groupId, int index) {
		Button b = (Button) mListGroup.findViewById(index);
		b.setText(GroupList.getGroupName(groupId));

		// Mise à jour du groupe dans les paramètres de session
		mSession.changeGroup(groupId, index);
		// On met à jour le titre du widget
		updateTitle();
	}
	/** Retourne le nombre de groupes présents dans le liste **/
	private int getNbGroup() {
		return mListGroup.getChildCount();
	}
}