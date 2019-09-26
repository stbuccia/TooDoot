package com.example.todot;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hootsuite.nachos.NachoTextView;

import java.util.ArrayList;


public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.ViewHolder> implements Filterable {

    private ArrayList<String> taglists;
    private ArrayList<String> taglistsFiltered;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;

        public ViewHolder(View v) {
            super(v);
            titleView = v.findViewById(R.id.taglistname);
            titleView.setGravity(Gravity.CENTER_VERTICAL);

        }
    }

    public TagListAdapter(ArrayList<String> data) {

        taglists = data;
        taglistsFiltered = taglists;

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

        holder.titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NachoTextView text = view.getRootView().findViewById(R.id.taglistInputEditText);
                String updatetxt = "";
                for (String chiptxt : text.getChipValues())
                    updatetxt += chiptxt + " ";
                text.setText(updatetxt + " " + tl + " ");
                text.setSelection(text.getText().length());
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


}
