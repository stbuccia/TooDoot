package com.example.todot;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.google.android.material.chip.Chip;

public class PriorityDialog extends ButtonsDialog{


    private Chip priority_chip = null;

    public PriorityDialog(final Context context, View view, int idButton){
        super(context, view, idButton);
        setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPriorityDialog();
            }
        });

    }

    public void showPriorityDialog() {


        d.setTitle("Select Priority");
        d.setContentView(R.layout.priority_dialog);

        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
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

        setupButtons(d, new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                prioritySet(np.getValue());
                d.dismiss();
            }
        });

        d.show();

    }



    private void prioritySet(int val){
        if (val == 1){
            priority_chip = view.findViewById(R.id.priority_chip);
            if (priority_chip != null)
                ((ViewGroup) priority_chip.getParent()).removeView(priority_chip);
        }
        else if (val > 1) {
            String txt;
            txt = (char)('A' + (val - 2)) + "";
            priority_chip = addChip(R.id.priority_chip, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPriorityDialog();
                }
            });
            priority_chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chipgroup.removeView(priority_chip);
                }
            });
            priority_chip.setText(txt);
        }
    }

}
