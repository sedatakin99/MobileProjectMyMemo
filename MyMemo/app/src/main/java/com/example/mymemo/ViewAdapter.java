package com.example.mymemo;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;

    private Context context;
    private ArrayList title_id, desc_id, loc_id, uri_id, emoji_id;

    public ViewAdapter(Context context, ArrayList title_id, ArrayList desc_id, ArrayList loc_id, ArrayList uri_id, ArrayList emoji_id, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.title_id = title_id;
        this.desc_id = desc_id;
        this.loc_id = loc_id;
        this.uri_id = uri_id;
        this.emoji_id = emoji_id;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.memory_entry, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title_id.setText(String.valueOf(title_id.get(position)));
        holder.desc_id.setText(String.valueOf(desc_id.get(position)));
        holder.loc_id.setText(String.valueOf(loc_id.get(position)));
    }

    @Override
    public int getItemCount() {
        return title_id.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_id, desc_id, loc_id, emoji_id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_id = itemView.findViewById(R.id.titleEntry);
            desc_id = itemView.findViewById(R.id.descriptionEntry);
            loc_id = itemView.findViewById(R.id.locationEntry);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
