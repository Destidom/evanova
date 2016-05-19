package com.tlabs.android.jeeves.views.routes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.routes.RouteDisplayGridAdapter.RouteHolder;

import org.devfleet.dotlan.DotlanJump;

class RouteDisplayGridAdapter extends AbstractDisplayRouteAdapter<RouteHolder> {

	static class RouteHolder extends AbstractDisplayRouteAdapter.RouteHolder {
		private final View view;
		
		public RouteHolder(final View parent) {
			super(parent);
			this.view = parent;
		}

		@Override
		public void bind(final DotlanJump entry) {
		    this.view.setBackgroundColor(EveFormat.getSecurityLevelColor(entry.getTo().getSecurityStatus()));
		}
	}

	@Override
	public RouteHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.jeeves_row_route_grid, viewGroup, false);
		return new RouteHolder(view);
	}
}