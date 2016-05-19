package com.tlabs.android.jeeves.views.ui.list;

import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

//An ExpandableListView.OnGroupExpandListener which closes previously selected groups
public class SingleListGroupDisplayAdapter implements OnGroupExpandListener {
	
	private int expanded = -1;
	private final ExpandableListView listView;
	
	public SingleListGroupDisplayAdapter(ExpandableListView listView) {
		super();
		this.listView = listView;		
	}
	
	@Override
	public void onGroupExpand(int groupPosition) {
		if (this.expanded == -1) {
			this.expanded = groupPosition;
			return;
		}
		if (this.expanded == groupPosition) {
			return;
		}
		listView.collapseGroup(this.expanded);
		this.expanded = groupPosition;
	}   
}
