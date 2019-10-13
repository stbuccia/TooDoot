package com.example.todot;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class ButtonsDialog extends Dialog {
    protected Context context;
    protected MaterialButton button;
    protected View view;
    protected ChipGroup chipgroup;
    protected View.OnClickListener onClickListener;
    protected Chip chip = null;
    protected int chip_id = 0;

    public ButtonsDialog(Context context, View view, int idButton) {
        super(context);
        getWindow().setBackgroundDrawableResource(R.color.design_default_color_background);
        if (idButton != -1)
            button = view.findViewById(idButton);
        this.view = view;
        this.context = context;

        chipgroup = view.findViewById(R.id.chip_group);

    }

    public ButtonsDialog(Context context, View view, int idButton, ChipGroup chipgroup) {
        super(context);

        button = view.findViewById(idButton);
        this.view = view;
        this.context = context;

        this.chipgroup = chipgroup;
    }

    public void setListener(View.OnClickListener listener) {
        button.setOnClickListener(listener);


    }

    protected void setupButtons(View.OnClickListener listener) {

        Button set_btn = (Button) findViewById(R.id.set_button);
        Button cancel_btn = (Button) findViewById(R.id.cancel_button);
        set_btn.setOnClickListener(listener);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setChip(int id, int iconId, View.OnClickListener listener) {

        chip = view.findViewById(id);
        if (chip == null) {
            chip = new Chip(new ContextThemeWrapper(context, R.style.MyChipApperance), null, 0);


            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.design_default_color_primary)));
            chip.setTextColor(ContextCompat.getColor(context, R.color.design_default_color_on_primary));
            chip.setId(id);
            chip.setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT));
            chip.setCloseIconVisible(true);
            chip.setCloseIconResource(R.drawable.ic_clear_24px);
            chip.getCloseIcon().setTint(ContextCompat.getColor(context, R.color.design_default_color_on_primary));
            chip.setChipIconTint(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.design_default_color_on_primary)));
            if (iconId != -1) chip.setChipIconResource(iconId);
            if (listener != null) chip.setOnClickListener(listener);

        }
    }

    public void addChip() {
        chipgroup.addView(chip);
    }
}


