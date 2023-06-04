package com.buyhelp.nofoodsharingproject.presentation.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.buyhelp.nofoodsharingproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomBottomNavigationMenu extends BottomNavigationView {

    private Context ctx;

    public CustomBottomNavigationMenu(@NonNull Context context) {
        super(context);
        this.ctx = context;
    }

    public CustomBottomNavigationMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
    }

    public CustomBottomNavigationMenu(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.ctx = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        MenuItem specialItemSetter = getMenu().findItem(R.id.setterAdvrsF);
        changeColor(specialItemSetter);

        MenuItem specialItemGetter = getMenu().findItem(R.id.getterAdvrsF);
        changeColor(specialItemGetter);
    }

    private void changeColor(MenuItem menuItem) {
        if (menuItem != null && menuItem.getIcon() != null) {
            if (menuItem.isChecked()) {
                menuItem.getIcon().setColorFilter(ContextCompat.getColor(ctx, R.color.custom_grey), PorterDuff.Mode.SRC_IN);
            } else {
                menuItem.getIcon().setColorFilter(ContextCompat.getColor(ctx, R.color.grey_500), PorterDuff.Mode.SRC_IN);
            }
        }
    }
}