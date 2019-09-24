package com.example.todot;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class PriorityDialog extends ButtonsDialog{


    protected char priority = '0';

    public PriorityDialog(final Context context, View view, int idButton){
        super(context, view, idButton);
        setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPriorityDialog();
            }
        });

    }

    public PriorityDialog(final Context context, View view, final int idButton, ChipGroup chipGroup){
        super(context, view, idButton, chipGroup);
        setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPriorityDialog();
            }
        });

    }

    public void showPriorityDialog() {


        setTitle("Select Priority");
        setContentView(R.layout.priority_dialog);

        final NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker);
        String[] alphabet = new String[27];

        alphabet[0] = "Nessuna";

        for(char i=1; i <= 26 ; i++){
            alphabet[i] = "" + (char)((i-1) + 'A');
        }

        np.setDisplayedValues(null);
        np.setMinValue(1);
        np.setDisplayedValues(alphabet);

        np.setMaxValue(alphabet.length);
        np.setWrapSelectorWheel(true);

        setupButtons(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                onPrioritySet(np.getValue());
                dismiss();
            }
        });

        show();

    }



    public void onPrioritySet(int val){
        if (val == 1){
            chip = view.findViewById(R.id.priority_chip);
            if (chip != null) {
                ((ViewGroup) chip.getParent()).removeView(chip);
            }
            priority = '0';
        }
        else if (val > 1) {
            priority = (char)('A' + (val - 2));
            String txt;
            txt = priority + "";
            setChip(R.id.priority_chip, -1, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPriorityDialog();
                }
            });/*
            addChip();*/
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chipgroup.removeView(chip);
                    priority = '0';
                }
            });
            chip.setText(txt);
        }
    }

    public Chip getChip(){
        return chip;
    }

    public char getPriority(){
        return priority;
    }

}
