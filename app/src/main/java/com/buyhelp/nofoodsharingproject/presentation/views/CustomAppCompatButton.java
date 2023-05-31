package com.buyhelp.nofoodsharingproject.presentation.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.buyhelp.nofoodsharingproject.R;

public class CustomAppCompatButton extends AppCompatButton {

    public CustomAppCompatButton(Context context) {
        super(context);
    }

    public CustomAppCompatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomAppCompatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
        final Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            this.startAnimation(scaleDown);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            this.startAnimation(scaleUp);
            performClick();
        }
        return true;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);

    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
