package com.example.todot;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import com.google.android.material.chip.Chip;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import java.util.ArrayList;

public class ListDialog extends ButtonsDialog{

    private Chip[] list_chips;
    private ArrayList<String> lists = new ArrayList<>();

    public ListDialog(final Context context, View view, int idButton){
        super(context, view, idButton);
        setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListDialog();
            }
        });

    }
    private void showListDialog(){
        d.setTitle("Add Lists");
        d.setContentView(R.layout.taglist_dialog);

        final NachoTextView text = d.findViewById(R.id.taglistInputEditText);

        String[] suggestions = (model.Task.getAllLists()).toArray(new String[model.Task.getAllLists().size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, suggestions);

        //focus on editext
        text.requestFocus();
        text.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(text, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 150);
        text.setText(lists);
        text.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        text.setAdapter(adapter);
        text.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        text.enableEditChipOnTouch(false, true);

        text.setThreshold(0);


        setupButtons(d, new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                lists = (ArrayList<String>) text.getChipValues();
                showLists();
                d.dismiss();
            }
        });
        d.show();

    }

    private void showLists(){
        list_chips = new Chip[lists.size()];
        for(int i = 0; i < lists.size(); i++ ){
            final int id = 50 + i;
            list_chips[i] = addChip(id, R.drawable.ic_list_18px, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showListDialog();
                }
            });
            list_chips[i].setText(lists.get(i));
            final int finalI = i;
            list_chips[i].setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lists.remove(list_chips[finalI].getText());
                    chipgroup.removeView(view.findViewById(id));
                }
            });
        }
    }
    public Chip[] getChips(){
        return list_chips;
    }

    public ArrayList<String> getLists(){
        return lists;
    }

}
