package com.tlabs.android.jeeves.views.routes;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.jeeves.views.routes.AbstractDisplayRouteAdapter.RouteHolder;

import org.devfleet.dotlan.DotlanJump;
import org.devfleet.dotlan.DotlanRoute;

import java.util.ArrayList;
import java.util.List;


abstract class AbstractDisplayRouteAdapter<T extends RouteHolder> extends Adapter<T> {

	public static class RouteHolder extends ViewHolder {
		public RouteHolder(View itemView) {
			super(itemView);
		}

		public void bind(final DotlanJump jump) {}
	}

	private final List<DotlanJump> plan;
	
	public AbstractDisplayRouteAdapter() {
		super();
		this.plan = new ArrayList<>();
	}

	@Override
	public abstract T onCreateViewHolder(ViewGroup viewGroup, int i);

	@Override
	public void onBindViewHolder(T t, int i) {
		t.bind(this.plan.get(i));
	}

	@Override
	public int getItemCount() {
		return this.plan.size();
	}

	public final void setRoute(final DotlanRoute plan) {
		this.plan.clear();
		if (null == plan) {
			return;
		}
		this.plan.addAll(plan.getRoute());
		notifyDataSetChanged();
	}
}