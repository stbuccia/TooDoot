package com.example.toodoot;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toodoot.R;
import com.hootsuite.nachos.NachoTextView;

import java.util.ArrayList;
import java.util.List;


public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.ViewHolder> implements Filterable {

    private ArrayList<String> taglists;
    private ArrayList<String> taglistsFiltered;
    private NachoTextView text;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkButton;
        public TextView titleView;

        public ViewHolder(View v) {
            super(v);
            titleView = v.findViewById(R.id.taglistname);
            titleView.setGravity(Gravity.CENTER_VERTICAL);

            checkButton = v.findViewById(R.id.checkBox);


        }
    }

    public TagListAdapter(ArrayList<String> data, NachoTextView edittext) {

        taglists = data;
        taglistsFiltered = taglists;
        text = edittext;

    }

    @NonNull
    @Override
    public TagListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.taglist, parent, false);
        TagListAdapter.ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final String tl = taglistsFiltered.get(position);
        holder.titleView.setText(taglistsFiltered.get(position));

        holder.checkButton.setChecked(isInEditText(text, tl));

        holder.checkButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        addToEditText(text, tl);
                    } else {
                        removeToEditText(text, tl);
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return taglistsFiltered.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty())
                    taglistsFiltered = taglists;
                else {
                    ArrayList<String> filteredList = new ArrayList<>();
                    for (String row : taglists){
                        if (row.toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(row);
                        }
                    }
                    taglistsFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = taglistsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                taglistsFiltered = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private boolean isInEditText(NachoTextView text, String search){
        List<String> chipstxt = text.getChipValues();
        return chipstxt.contains(search);
    }

    private void addToEditText (NachoTextView text, String s){
        StringBuilder updatetxt = new StringBuilder();
        for (String chiptxt : text.getChipValues())
            updatetxt.append(chiptxt).append(" ");
        text.setText(new StringBuilder().append(updatetxt).append(" ").append(s).append(" ").toString());
        text.setSelection(text.getText().length());

    }

    private void removeToEditText(NachoTextView text, String s){
        List<String> chipstxt = text.getChipValues();
        chipstxt.remove(s);
        text.setText(chipstxt);
    }

}
