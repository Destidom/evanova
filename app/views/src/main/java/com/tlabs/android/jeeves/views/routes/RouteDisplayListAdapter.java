package com.tlabs.android.jeeves.views.routes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.routes.RouteDisplayListAdapter.RouteHolder;

import org.apache.commons.lang.StringUtils;
import org.devfleet.dotlan.DotlanJump;
import org.devfleet.dotlan.DotlanSolarSystem;


class RouteDisplayListAdapter extends AbstractDisplayRouteAdapter<RouteHolder> {

	static class RouteHolder extends AbstractDisplayRouteAdapter.RouteHolder {
		
		private TextView regionView;
		private TextView solarSystemView;
		private TextView securityView;
		private TextView killsView;
		private TextView jumpsView;
		private TextView ownerView;
		
		public RouteHolder(final View parent) {
			super(parent);
			this.regionView = (TextView)parent.findViewById(R.id.j_rowJumpPlanRegionView);
			this.solarSystemView = (TextView)parent.findViewById(R.id.j_rowJumpPlanSolarSystemView);
			this.securityView = (TextView)parent.findViewById(R.id.j_rowJumpPlanSecurityView);
			this.killsView = (TextView)parent.findViewById(R.id.j_rowJumpPlanKillsView);
			this.jumpsView = (TextView)parent.findViewById(R.id.j_rowJumpPlanJumpsView);
			this.ownerView = (TextView)parent.findViewById(R.id.j_rowJumpPlanOwnerView);
		}

		@Override
		public void bind(final DotlanJump entry) {
			if (null == entry) {
				return;
			}

			renderDestination(entry.getTo());
			if (entry.getRequiredFuel() == 0) {
                renderRoute(entry.getTo());
            }
            else {
                renderJump(entry);
            }
		}

		private void renderDestination(final DotlanSolarSystem to) {
			this.regionView.setText(to.getRegionName());
			this.regionView.setVisibility(View.VISIBLE);
			this.solarSystemView.setText(to.getSolarSystemName());
			if (StringUtils.isBlank(to.getHolderName())) {
				this.ownerView.setText("");
			}
			else {
				this.ownerView.setText(to.getHolderName());
			}

			this.securityView.setText("" + to.getSecurityStatus());

			int color = EveFormat.getSecurityLevelColor(to.getSecurityStatus());
			this.solarSystemView.setTextColor(color);
			this.securityView.setTextColor(color);
		}

        //I18N
        private void renderRoute(final DotlanSolarSystem to) {
            if (to.getShipKills() == 0) {
                this.killsView.setText("");
            }
            else {
                this.killsView.setText(to.getShipKills() + " kills");
            }

            if (to.getShipJumps() == 0) {
                this.jumpsView.setText("");
            }
            else {
                this.jumpsView.setText(to.getShipJumps() + " ship jumps");
            }
        }

        private void renderJump(final DotlanJump entry) {
            this.killsView.setText(entry.getRequiredFuel() + " isotopes");
            this.jumpsView.setText(entry.getDistance() + " LY");
        }
	}

	@Override
	public RouteHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.jeeves_row_route_list, viewGroup, false);
		return new RouteHolder(view);
	}
}