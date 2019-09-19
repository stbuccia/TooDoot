package com.example.todot;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.material.chip.Chip;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import java.util.ArrayList;

public class TagDialog extends ButtonsDialog {

    private  Chip[] tag_chips;
    private ArrayList<String> tags = new ArrayList<String>();

    public TagDialog(final Context context, View view, int idButton){
        super(context, view, idButton);
        setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTagDialog();
            }
        });

    }
    private void showTagDialog(){
        d.setTitle("Add Tags");
        d.setContentView(R.layout.taglist_dialog);

        final NachoTextView text = d.findViewById(R.id.taglistInputEditText);


        String[] suggestions = (model.Task.getAllTags()).toArray(new String[model.Task.getAllTags().size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, suggestions);

        //text.setUp(tags);
        text.setAdapter(adapter);
        text.setInputType(InputType.TYPE_CLASS_TEXT);
        text.setText(tags);
        text.setThreshold(0);
        text.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        text.enableEditChipOnTouch(true, true);
        setupButtons(d, new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                tags = (ArrayList<String>) text.getChipValues();
                //remove empty tags
                showTags();
                d.dismiss();
            }
        });
        d.show();

    }

    private void showTags(){
        tag_chips = new Chip[tags.size()];
        for(int i = 0; i < tags.size(); i++ ){
            final int finalI = i;
            tag_chips[i] = addChip(i, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTagDialog();
                }
            });
            tag_chips[i].setText(tags.get(i));
            tag_chips[i].setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tags.remove(tag_chips[finalI].getText());
                    chipgroup.removeView(view.findViewById(finalI));
                }
            });

        }
    }

    public Chip[] getChips(){
        return tag_chips;
    }

    public ArrayList<String> getTags(){
        return tags;
    }
}
