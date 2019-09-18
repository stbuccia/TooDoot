package com.example.todot;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class ButtonsDialog {
    protected Context context;
    private MaterialButton button;
    protected View view;
    protected ChipGroup chipgroup;
    protected View.OnClickListener onClickListener;
    protected Dialog d;

    public ButtonsDialog(Context context, View view, int idButton){
        button = view.findViewById(idButton);
        this.view = view;
        this.context = context;

        chipgroup = view.findViewById(R.id.chip_group);
        d = new Dialog(context);

    }
    public void setListener(View.OnClickListener listener){
        button.setOnClickListener(listener);


    }
    protected void setupButtons(final Dialog d, View.OnClickListener listener) {

        Button set_btn = (Button) d.findViewById(R.id.set_button);
        Button cancel_btn = (Button) d.findViewById(R.id.cancel_button);
        set_btn.setOnClickListener(listener);
        cancel_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public Chip addChip(int id, View.OnClickListener listener){

        Chip chip;
        chip = view.findViewById(id);
        if (chip == null){
            chip = new Chip(context);

            chip.setBackgroundColor(ContextCompat.getColor(context, R.color.design_default_color_primary));
            chip.setTextColor(R.color.design_default_color_on_primary);
            chip.setId(id);
            chip.setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT));
            chipgroup.addView(chip);
            chip.setCloseIconVisible(true);
            chip.setOnClickListener(listener);

        }
        return chip;
    }

}
