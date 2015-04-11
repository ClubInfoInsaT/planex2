package com.pijodev.insatpe2;

import java.util.ArrayList;
import java.util.Stack;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pijodev.insatpe2.GroupList.Group;

/**
 * 
 * @author Proïd
 *
 */
public class TreeGroupSelectorView extends GroupSelectorView {
	private AlertDialog mDialog;
	private TextView title;
	private BaseAdapter adpater;
	private Stack<GroupNameTree> state = new Stack<>();
	private GroupNameTree root;
	
	public TreeGroupSelectorView(Context context) {
		Builder builder = new Builder(context);
		
		root = GroupNameTree.buildTree();
		state.push(root);
		
		ListView list = new ListView(context);
		adpater = new BaseAdapter() {
			ArrayList<TextView> lines = new ArrayList<>();

			@Override public View getView(int position, View convertView, ViewGroup parent) {
				if(position >= lines.size())
					lines.add(new TextView(parent.getContext()));
				if(state.peek().branch.size() > position)
					lines.get(position).setText((String)getItem(position)+(state.peek().branch.get(position).id != -1 ? " "+state.peek().branch.get(position).id : ""));
				else if(position == getCount()-1 && mTarget != -1)
					lines.get(position).setText((String)getItem(position));
				else
					lines.get(position).setText("");
				return lines.get(position);
			}
			@Override
			public long getItemId(int position) {
				return position;
			}
			@Override public Object getItem(int position) {
				if(position == getCount()-1)
					return "Supprimer";
				if(state.peek().branch.size() <= position)
					return null;
				return state.peek().branch.get(position).name;
			}
			@Override public int getCount() {
				return root.maxBranchSize+1;
			}
		};
		title = new TextView(context);
		title.setText("");
		list.addHeaderView(title);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 0) {
					if(state.size() > 1)
						state.pop();
				} else if(position == parent.getCount()-1) {
					if(mGroupSelectedListener != null)
						mGroupSelectedListener.onGroupSelected(-1, mTarget);
					mDialog.dismiss();
				} else if(position <= state.peek().branch.size()) {
					position--;
					state.push(state.peek().branch.get(position));
					if(state.peek().id != -1) {
						if(mGroupSelectedListener != null)
							mGroupSelectedListener.onGroupSelected(state.peek().id, mTarget);
						mDialog.dismiss();
					} else {
						title.setText(title.getText()+" "+state.peek().name);
						adpater.notifyDataSetChanged();
					}
				}
			}
		});
		
		list.setAdapter(adpater);
		builder.setView(list);
		mDialog = builder.create();
	}
	
	void show(int target) {
		mTarget = target;
		state.clear();
		state.push(root);
		title.setText("");
		adpater.notifyDataSetChanged();
		mDialog.show();
	}
	
	private static class GroupNameTree {
		int id = -1;
		String name;
		ArrayList<GroupNameTree> branch;
		int maxBranchSize = 0;
		static GroupNameTree buildTree() {
			GroupNameTree root = new GroupNameTree();
			
			for(Group grp : GroupList.getList()) {
				add(grp.name, grp.id, root);
			}
			root.print("");
			return root;
		}
		
		private void print(String tab) {
			Log.i("###", tab+name+" "+id);
			if(branch != null) {
				maxBranchSize = Math.max(maxBranchSize, branch.size());
				for(GroupNameTree g : branch) {
					g.print(tab+"+---");
					maxBranchSize = Math.max(maxBranchSize, g.maxBranchSize);
				}
			}
		}
		
		static void add(String name, int id, GroupNameTree tree) {
			/// /!\ conflit pour nom déjà existant dans l'arbre
			if(tree.branch != null) {
				for(GroupNameTree b : tree.branch) {
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
						t.branch = b.branch;
						b.id = id;
						b.name = name;
						b.branch = new ArrayList<>();
						b.branch.add(t);
						return;
					}
					else if(common > 0){
						GroupNameTree t = new GroupNameTree();
						t.id = b.id;
						t.name = b.name.substring(common+1);
						t.branch = b.branch;
						b.branch = new ArrayList<>();
						b.name = name.substring(0, common);
						b.id = -1;
						b.branch.add(t);
						t = new GroupNameTree();
						t.id = id;
						t.name = name.substring(common+1);
						b.branch.add(t);
						return;
					}
					else {
						// common == 0, on poursuit le parcours de l'arraylist
					}
				}
			}
			else {
				tree.branch = new ArrayList<>();
			}

			GroupNameTree t = new GroupNameTree();
			t.id = id;
			t.name = name;
			tree.branch.add(t);
		}
		
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
