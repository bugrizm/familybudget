package com.bugra.familybudget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

public class PaymentIconView extends View {

    private ShapeDrawable drawable;
    private boolean isTransparent = false;
    private String colorHex;

    public PaymentIconView(Context context) {
        super(context);

        initDrawable();
    }

    public PaymentIconView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initDrawable();
    }

    public PaymentIconView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initDrawable();
    }

    private void initDrawable() {
        drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(0xffffffff);

        float scale = getContext().getResources().getDisplayMetrics().density;

        int left = (int)(5 * scale + 0.5f);
        int top = (int)(5 * scale + 0.5f);
        int right = (int)(45 * scale + 0.5f);
        int bottom = (int)(45 * scale + 0.5f);

        drawable.setBounds(left, top, right, bottom);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawable.draw(canvas);
    }

    public void setColor(String colorHex) {
        this.colorHex = colorHex;

        String transparency = "ff";
        if(isTransparent) {
            transparency = "44";
        }
        drawable.getPaint().setColor((int)Long.parseLong(transparency + colorHex, 16));
        invalidate();
    }

    public String getColorHex() {
        return colorHex;
    }

    public int getColor() {
        return drawable.getPaint().getColor();
    }

    public void setTransparent(boolean isTransparent) {
        this.isTransparent = isTransparent;
        setColor(colorHex);
    }

    public boolean getTransparent() {
        return isTransparent;
    }

}
