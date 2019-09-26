package com.example.todot;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import java.util.ArrayList;
import java.util.List;

import static model.Task.getAllTags;

public class TagDialog extends ButtonsDialog {

    protected Chip[] tag_chips = null;
    private ArrayList<String> tags = new ArrayList<String>();
    private TagListAdapter mAdapter;

    public TagDialog(final Context context, View view, int idButton){
        super(context, view, idButton);
        setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTagDialog();
            }
        });


    }

    public TagDialog(final Context context, View view, final int idButton, ChipGroup chipGroup){
        super(context, view, idButton, chipGroup);
        setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTagDialog();
            }
        });

    }
    private void showTagDialog(){
        setTitle("Add Tags");
        setContentView(R.layout.taglist_dialog);



        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getOwnerActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //mAdapter = new TagListAdapter(model.Task.getAllTags());


        mAdapter = new TagListAdapter(getAllTags());
        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        final NachoTextView text = findViewById(R.id.taglistInputEditText);

        String[] suggestions = (model.Task.getAllTags()).toArray(new String[model.Task.getAllTags().size()]);
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
        text.setText(tags);
        text.setInputType(InputType.TYPE_CLASS_TEXT);
        /*text.setAdapter(adapter);*/
        text.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        text.enableEditChipOnTouch(false, true);

        //text.setThreshold(0);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String s = charSequence.toString();

                s = s.substring(0, count);
                String lastWord = s.substring(s.lastIndexOf(" ") + 1);
                mAdapter.getFilter().filter(lastWord);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        setupButtons(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                tags = (ArrayList<String>) text.getChipValues();
                onTagSet();
                dismiss();
            }
        });
        show();

    }

    public void onTagSet(){
        tag_chips = new Chip[tags.size()];
        for(int i = 0; i < tags.size(); i++ ){
            final int finalI = i;
            setChip(i, -1, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTagDialog();
                }
            });
            //addChip();
            tag_chips[i] = chip;
            tag_chips[i].setText(tags.get(i));
            tag_chips[i].setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeTag(finalI);
                    chipgroup.removeView(view.findViewById(finalI));
                }
            });

        }
    }

    public Chip[] getChips(){
        return tag_chips;
    }


    public void removeTag(int i){
        tags.remove(tag_chips[i].getText());
    }

    public ArrayList<String> getTags(){
        return tags;
    }

    public void setTags(List<String> lists){
        this.tags = (ArrayList<String>)lists;
    }
}
