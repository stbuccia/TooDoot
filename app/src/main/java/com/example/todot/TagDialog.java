package com.example.todot;

import android.content.Context;
import android.view.View;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;

public class TagDialog extends ButtonsDialog {

    private  Chip[] tag_chips;

    public TagDialog(final Context context, View view, int idButton){
        super(context, view, idButton);
        setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTagDialog(new ArrayList<String>());
            }
        });

    }
    private void showTagDialog(ArrayList<String> tags){
        d.setTitle("Add Tag");
        d.setContentView(R.layout.taglist_dialog);

        final EditTextCursorWatcher text = d.findViewById(R.id.taglistInputEditText);

        text.setUp(tags);
        setupButtons(d, new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                showTags(new ArrayList(Arrays.asList(text.getText().toString().split(" "))));
                d.dismiss();
            }
        });
        d.show();

    }

    private void showTags(final ArrayList<String> tags){
        tag_chips = new Chip[tags.size()];
        for(int i = 0; i < tags.size(); i++ ){
            final int finalI = i;
            tag_chips[i] = addChip(i, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTagDialog(tags);
                }
            });
            tag_chips[i].setText(tags.get(i));
            final int finalI1 = i;
            tag_chips[i].setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tags.remove(tag_chips[finalI1]);
                    chipgroup.removeView(view.findViewById(finalI));
                }
            });
        }
    }
}
