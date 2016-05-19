package com.tlabs.android.jeeves.views.fittings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.dogma.Fitter;
import com.tlabs.eve.dogma.model.Effect;

public class FittingEffectListWidget extends AbstractListRecyclerView<Effect> implements FittingWidgetInterface {

    private static class EffectHolder extends ListRecyclerViewAdapter.ViewHolder<Effect> {
        //  @BindView(R.id.rawEffectName)
        TextView nameView;

        //  @BindView(R.id.rawEffectDescription)
        TextView descriptionView;

        // @BindView(R.id.rawEffectExpression)
        TextView expressionView;

        public EffectHolder(View itemView) {
            super(itemView);
        }

        public void render(final Effect effect) {
            this.nameView.setText(effect.getEffectName());
            this.descriptionView.setText(effect.getDescription());
            this.expressionView.setText(effect.getPostExpression().getDescription());
        }
    }

    public FittingEffectListWidget(Context context) {
        super(context);
    }

    public FittingEffectListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FittingEffectListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<Effect> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EffectHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_item_effect, parent, false));
    }

    @Override
    protected long getItemId(Effect item) {
        return item.getEffectID();
    }

    @Override
    public void setFitting(Fitter fitter) {
        setItems(fitter.getItem().getEffects());
    }
}
