package com.example.todot;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import java.util.ArrayList;
import java.util.List;

public class ListDialog extends ButtonsDialog {

    protected Chip[] list_chips = null;
    private ArrayList<String> lists = new ArrayList<String>();
    private TagListAdapter mAdapter;
    private ArrayList<String> allLists = new ArrayList<String>();
    private String title;
    private int startId;
    private int colorRes;

    public ListDialog(final Context context, View view, int idButton, ArrayList<String> all, String name, int id, int res){
        super(context, view, idButton);
        setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListDialog();
            }
        });
        allLists = all;
        title = name;
        startId = id;
        colorRes = res;


    }

    private void showListDialog(){
        setTitle(title);
        setContentView(R.layout.taglist_dialog);

        getWindow().setLayout((int)(context.getResources().getDisplayMetrics().widthPixels*0.90), WindowManager.LayoutParams.WRAP_CONTENT);

        final NachoTextView text = findViewById(R.id.taglistInputEditText);



        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getOwnerActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //mAdapter = new ListListAdapter(model.Task.getAlllists());


        mAdapter = new TagListAdapter(allLists, text);
        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation()));



        //focus on editext
        text.setText(lists);
        text.setInputType(InputType.TYPE_CLASS_TEXT);
        text.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        text.setChipBackgroundResource(colorRes);
        text.enableEditChipOnTouch(false, true);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String s = charSequence.toString();

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

                lists = (ArrayList<String>) text.getChipValues();
                onListSet();
                dismiss();
            }
        });
        show();

    }

    public void onListSet(){
        list_chips = new Chip[lists.size()];
        for(int i = 0; i < lists.size(); i++ ){
            final int id = startId + i;
            final int finalI = i;

            setChip(id, -1, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showListDialog();
                }
            });
            //addChip();
            list_chips[i] = chip;
            list_chips[i].setText(lists.get(i));
            list_chips[i].setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeList(finalI);
                    chipgroup.removeView(view.findViewById(id));
                }
            });

        }
    }

    public Chip[] getChips(){
        return list_chips;
    }


    public void removeList(int i){
        lists.remove(list_chips[i].getText());
    }

    public ArrayList<String> getLists(){
        return lists;
    }

    public void setLists(List<String> lists){
        this.lists = (ArrayList<String>)lists;
    }
}

