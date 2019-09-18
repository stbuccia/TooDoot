package com.example.todot;

import android.content.Context;
import android.view.View;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;

public class ListDialog extends ButtonsDialog{

    private Chip[] list_chips;

    public ListDialog(final Context context, View view, int idButton){
        super(context, view, idButton);
        setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTagDialog(new ArrayList<String>());
            }
        });

    }
    private void showTagDialog(ArrayList<String> tags){
        d.setTitle("Add Lists");
        d.setContentView(R.layout.taglist_dialog);

        final EditTextCursorWatcher text = d.findViewById(R.id.taglistInputEditText);

        text.setUp(tags);
        setupButtons(d, new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                showLists(new ArrayList(Arrays.asList(text.getText().toString().split(" "))));
                d.dismiss();
            }
        });
        d.show();

    }

    private void showLists(final ArrayList<String> lists){
        list_chips = new Chip[lists.size()];
        for(int i = 0; i < lists.size(); i++ ){
            final int finalI = i;

            if (lists.get(i) != "") {
                list_chips[i] = addChip(i, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showTagDialog(lists);
                    }
                });
                list_chips[i].setText(lists.get(i));
                list_chips[i].setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lists.remove(list_chips[finalI]);
                        chipgroup.removeView(view.findViewById(finalI));
                    }
                });
            }
        }
    }

}
