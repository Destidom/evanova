package com.tlabs.android.jeeves.views.skills;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.Strings;
import com.tlabs.android.jeeves.views.ui.animation.BlinkAnimation;


public class SkillLevelWidget extends LinearLayout {
	
	private int trainDrawable = R.drawable.s_blue;
	
	public SkillLevelWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SkillLevelWidget(Context context) {
		super(context);
	}
	
	public void setTrainDrawable(int trainDrawable) {
		this.trainDrawable = trainDrawable;
	}

	@Override
	public void onFinishInflate() {
		super.onFinishInflate();
		
		View.inflate(
				getContext().getApplicationContext(),
				R.layout.jeeves_view_skill_level,
				this);
		LinearLayout parent = (LinearLayout)getChildAt(0);
		parent.setVisibility(View.VISIBLE);
    }

	public void setSkillLevel(final int skillLevel) {
		initSkillLevelImpl(skillLevel);
		setSkillLevelImage(skillLevel);
	}

	public void setSkillLevel(final int skillLevel, final int trainLevel) {
		setSkillLevel(skillLevel, trainLevel, false);
	}
	
	public void setSkillLevel(final int skillLevel, final int trainLevel, boolean animate) {
		
		if (trainLevel <= skillLevel) {
			initSkillLevelImpl(skillLevel);
			setSkillLevelImage(skillLevel);
		}		
		else {
			//training > char skill level
			initSkillLevelImpl(trainLevel);
			setSkillLevelImpl(skillLevel, trainLevel, animate);
		}
	}
	
	public void setShowRequired(boolean hasRequirements) {
		cancelAnimations();
		final TextView skillLevelText = (TextView)findViewById(R.id.j_skillLevelText);
		skillLevelText.setVisibility(View.GONE);
		
		final LinearLayout levels = (LinearLayout)findViewById(R.id.skillLevelLayout);
		levels.setVisibility(View.GONE);		

		final ImageView requiredImage = (ImageView)findViewById(R.id.skillLevelAllowed);
		requiredImage.setVisibility(hasRequirements ? View.VISIBLE : View.GONE);
		
		final ImageView notRequiredImage = (ImageView)findViewById(R.id.skillLevelRequired);
		notRequiredImage.setVisibility(hasRequirements ? View.GONE : View.VISIBLE);
	}
	
	//all grey and completed
	private void setSkillLevelImage(final int level) {
		final LinearLayout parent = (LinearLayout)findViewById(R.id.skillLevelLayout);
		for (int i = 0; i < parent.getChildCount(); i++) {
			final ImageView iv = (ImageView)parent.getChildAt(i);
			if (level > i) {
				iv.setImageResource(R.drawable.s_grey);
			}
			else {				
				iv.setImageDrawable(null);
			}
		}		
	}
	
	private void setSkillLevelImpl(final int character, final int training, boolean animate) {
		final LinearLayout parent = (LinearLayout)findViewById(R.id.skillLevelLayout);
		for (int i = 0; i < parent.getChildCount(); i++) {
			final ImageView iv = (ImageView)parent.getChildAt(i);
			if (character > i) {
				iv.setImageResource(R.drawable.s_grey);
			}
			else if (training > i) {
				iv.setImageResource(this.trainDrawable);
				if (animate && i == character) {
					iv.setAnimation(new BlinkAnimation());
				}
			}
			else {
				iv.setImageDrawable(null);
			}					
		}
	}
	
	private void initSkillLevelImpl(final int level) {
		cancelAnimations();
		final ImageView requiredImage = (ImageView)findViewById(R.id.skillLevelRequired);
		requiredImage.setVisibility(View.GONE);
		
		final ImageView notRequiredImage = (ImageView)findViewById(R.id.skillLevelAllowed);
		notRequiredImage.setVisibility(View.GONE);
		
		final TextView skillLevelText = (TextView)findViewById(R.id.j_skillLevelText);
		Strings.r(skillLevelText, R.string.jeeeves_skilllevelview_level, level);

		skillLevelText.setTextColor(Color.WHITE);	
		skillLevelText.setVisibility(View.VISIBLE);
		
		final LinearLayout levels = (LinearLayout)findViewById(R.id.skillLevelLayout);
		levels.setVisibility(View.VISIBLE);		
	}
	
	private void cancelAnimations() {
		final LinearLayout parent = (LinearLayout)findViewById(R.id.skillLevelLayout);
		for (int i = 0; i < parent.getChildCount(); i++) {
			final ImageView iv = (ImageView)parent.getChildAt(i);
			final Animation a = iv.getAnimation();
			if (null != a) {
				//a.cancel(); API > 4
				iv.setAnimation(null);
			}
		}
	}
}
