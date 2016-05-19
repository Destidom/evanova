package com.tlabs.android.jeeves.views.ui.list;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.R;

public abstract class AbstractListRowHolder<T> extends ListRecyclerViewAdapter.ViewHolder<T>{

    protected final TextView text1;

    protected final TextView text2;

    protected final TextView text3;

    protected final TextView text4;

    protected final TextView text5;

    protected final ImageView portraitImage;

    protected final ImageView crestImage;

    protected final ProgressBar progress;

    public AbstractListRowHolder(View itemView) {
        super(itemView);

        this.text1 = (TextView)itemView.findViewById(R.id.j_rowCharacterText1);
        this.text2 = (TextView)itemView.findViewById(R.id.j_rowCharacterText2);
        this.text3 = (TextView)itemView.findViewById(R.id.j_rowCharacterText3);
        this.text4 = (TextView)itemView.findViewById(R.id.j_rowCharacterText4);
        this.text5 = (TextView)itemView.findViewById(R.id.j_rowCharacterTrainingText5);

        this.portraitImage = (ImageView)itemView.findViewById(R.id.j_rowCharacterPortrait);
        this.crestImage = (ImageView)itemView.findViewById(R.id.j_rowCharacterCrest);
        this.progress = (ProgressBar) itemView.findViewById(R.id.j_rowCharacterProgress);
    }

    public abstract void render(final T t);
}
