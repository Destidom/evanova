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
import com.tlabs.eve.dogma.extra.format.AttributeFormat;
import com.tlabs.eve.dogma.model.Attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FittingAttributeListWidget extends AbstractListRecyclerView<Attribute> {

    private static class AttributeHolder extends ListRecyclerViewAdapter.ViewHolder<Attribute> {

        //   @BindView(R.id.rawAttributeCurrentValue)
        TextView currentValueView;

        //   @BindView(R.id.rawAttributeDefaultValue)
        TextView defaultValueView;

        //   @BindView(R.id.rawAttributeDescription)
        TextView descriptionView;

        //    @BindView(R.id.rawAttributeName)
        TextView nameView;

        public AttributeHolder(View itemView) {
            super(itemView);
        }

        public void render(final Attribute attribute) {
            this.nameView.setText("[" + attribute.getAttributeID() + "]: " + attribute.getAttributeName() + " " + AttributeFormat.format(attribute));
            this.descriptionView.setText(attribute.getDescription());
            this.currentValueView.setText("Current: " + attribute.getCurrentValue());
            this.defaultValueView.setText("Default: " + attribute.getDefaultValue());
        }
    }
    public FittingAttributeListWidget(Context context) {
        super(context);
    }

    public FittingAttributeListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FittingAttributeListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<Attribute> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AttributeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_fitting_attribute, parent, false));

    }

    @Override
    protected long getItemId(Attribute item) {
        return item.getAttributeID();
    }

    public void setFitting(Fitter fitter) {
        final List<Attribute> attributes = new ArrayList<>();
        for (Attribute a: fitter.getAttributes().values()) {
            attributes.add(a);
        }
        Collections.sort(attributes, (lhs, rhs) -> Integer.compare(lhs.getAttributeID(), rhs.getAttributeID()));
        setItems(attributes);
    }
}
