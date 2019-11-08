package com.example.todot;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.NumberPicker;

import com.google.android.material.chip.Chip;

import model.Priority;
import model.Utils;

public class PriorityDialog extends ButtonsDialog {


    protected char priority = '0';

    public PriorityDialog(final Context context, View view, int idButton){
        super(context, view, idButton);
        setListener(view1 -> showPriorityDialog());

    }

    public PriorityDialog(final Context context) {

        super(context);
    }

    public void showPriorityDialog() {

        setTitle("Select Priority");
        setContentView(R.layout.priority_dialog);

        getWindow().setLayout((int)(context.getResources().getDisplayMetrics().widthPixels*0.90), WindowManager.LayoutParams.WRAP_CONTENT);
        //getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        final NumberPicker np = findViewById(R.id.numberPicker);
        String[] alphabet = new String[27];

        alphabet[0] = "Unknown";

        for(char i=1; i <= 26 ; i++){
            alphabet[i] = "" + (char)((i-1) + 'A');
        }

        np.setDisplayedValues(null);
        np.setMinValue(1);
        np.setDisplayedValues(alphabet);

        np.setMaxValue(alphabet.length);
        np.setWrapSelectorWheel(true);


        setupButtons(v -> {
            onPrioritySet(np.getValue());
            dismiss();
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
            chip_id = R.id.priority_chip;
            setChip(chip_id, -1, view -> showPriorityDialog());
            chip.setOnCloseIconClickListener(view -> {
                chipgroup.removeView(chip);
                priority = '0';
            });
            chip.setText(txt);
        }
        button.setIconTintResource(Utils.getPriorityResColor(Priority.fromCharToInt(priority)));
    }

    public Chip getChip(){
        return chip;
    }

    public char getPriority(){
        return priority;
    }

}
