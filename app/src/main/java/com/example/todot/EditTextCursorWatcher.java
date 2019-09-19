package com.example.todot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import com.google.android.material.chip.ChipDrawable;

import java.util.ArrayList;

@SuppressLint("AppCompatCustomView")
public class EditTextCursorWatcher extends AutoCompleteTextView {
    private ImageSpan[] spans = new ImageSpan[50];
    private int [] spannedLengths = new int[50];
    private int lastSpanPos = 0;
    private int length = 0;
    private int selOld = 0;

    public EditTextCursorWatcher(Context context) {
        super(context);
    }

    public EditTextCursorWatcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextCursorWatcher(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUp(ArrayList<String> words){
        spannedLengths[0] = 0;

        String initial = "";
        for(int i = 0; i < words.size(); i++ ){
            initial += words.get(i) + " ";
        }
        setText(initial);
        setChips();

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                if (count >= 1){
                    if(charSequence.charAt(start) == ' '){
                        setChips();
                    }
                    else if (start < lastSpanPos){ //I'm not writing in the end
                        for (int i = 0; i < length; i++ ) {
                            if (start  >= spannedLengths[i] && start < spannedLengths[i + 1] ) {
                                getEditableText().removeSpan(spans[i]);
                            }
                        }
                        setSpanLength();
                    }

                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });


    }


    private int getSelectedSpan(int pos){
        pos++;
        for (int i = 0; i < length; i++ ) {
            if (pos  >= spannedLengths[i] && pos <= spannedLengths[i + 1] ) {
                return i;
            }
        }
        return  -1;
    }

    private void setSpanLength(){
        String chips[] = getText().toString().split(" ");
        spannedLengths[0] = 0;
        for(int i = 0; i < chips.length; i++ ){
            spannedLengths[i + 1] = spannedLengths[i] + chips[i].length() + 1;
        }

    }
    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {


        if (getSelectedSpan(selOld) != getSelectedSpan(selEnd)) {
            setChips();
        }
        selOld = selEnd;
    }

    public void setChips(){
        for (int j = 0; j < spans.length; j++) {
            getEditableText().removeSpan(spans[j]);
        }
        String chips[] = getText().toString().split(" ");
        for(int i = 0; i < chips.length; i++ ){
            ChipDrawable chipDrawable = ChipDrawable.createFromResource(getContext(), R.xml.chip);
            chipDrawable.setText((CharSequence)chips[i]);
            chipDrawable.setBounds(0, 0, chipDrawable.getIntrinsicWidth(), chipDrawable.getIntrinsicHeight());
            spans[i] = new ImageSpan(chipDrawable);
            getEditableText().setSpan(spans[i], spannedLengths[i], spannedLengths[i] + chips[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannedLengths[i + 1] = spannedLengths[i] + chips[i].length() + 1;

        }
        lastSpanPos = spannedLengths[chips.length] - 1;
        length = chips.length;
    }


    public void removeChip(int i){
        getEditableText().delete(spannedLengths[i], spannedLengths[i + 1]);
    }


}