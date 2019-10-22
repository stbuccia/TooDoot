package com.example.todot;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

abstract public class CheckTaskTouchHelper extends ItemTouchHelper.Callback {

    Context mContext;
    private Paint mClearPaint;
    private ColorDrawable mBackground;
    private int backgroundColor;
    private Drawable checkDrawable;
    private int intrinsicWidth;
    private int intrinsicHeight;


    public CheckTaskTouchHelper(Context context) {

        mContext = context;
        mBackground = new ColorDrawable();
        backgroundColor = context.getResources().getColor(R.color.design_default_color_secondary);
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        checkDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_check_24px);
        checkDrawable.setTint(context.getResources().getColor(R.color.design_default_color_background));
        intrinsicWidth = checkDrawable.getIntrinsicWidth();
        intrinsicHeight = checkDrawable.getIntrinsicHeight();


    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();

        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if (isCancelled) {
            clearCanvas(c, (float) itemView.getLeft(), (float) itemView.getTop(), (float) itemView.getLeft() + dX, (float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

        mBackground.setColor(backgroundColor);
        mBackground.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft()  + (int) dX, itemView.getBottom());
        mBackground.draw(c);

        int checkIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int checkIconMargin = (itemHeight - intrinsicHeight) / 2;
        int checkIconLeft = itemView.getLeft() + checkIconMargin;
        int checkIconRight = itemView.getLeft() + checkIconMargin + intrinsicWidth;
        int checkIconBottom = checkIconTop + intrinsicHeight;


        checkDrawable.setBounds(checkIconLeft, checkIconTop, checkIconRight, checkIconBottom);
        checkDrawable.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


    }

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        c.drawRect(left, top, right, bottom, mClearPaint);

    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.3f;
    }
}