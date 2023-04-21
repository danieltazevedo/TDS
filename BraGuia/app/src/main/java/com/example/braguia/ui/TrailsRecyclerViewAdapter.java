package com.example.braguia.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.braguia.model.Trail;
import com.example.braguia.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class TrailsRecyclerViewAdapter extends RecyclerView.Adapter<TrailsRecyclerViewAdapter.ViewHolder> {

    private final List<Trail> mValues;

    public TrailsRecyclerViewAdapter(List<Trail> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.name.setText(mValues.get(position).getTrail_name());
        holder.desc.setText(mValues.get(position).getTrail_desc());
        Picasso.get().load(mValues.get(position).getUrl().replace("http", "https")).into(holder.imageView);
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, Trail_info.class);
                Trail a = mValues.get(position);
                //intent.putExtra("pos", position);
                intent.putExtra("trail_info", a);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final Button info;
        public final View mView;
        public final TextView name;
        public final ImageView imageView;
        public final TextView desc;
        public Trail mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.trail_name);
            imageView = view.findViewById(R.id.cardimage);
            desc = view.findViewById(R.id.trail_desc);
            info = view.findViewById(R.id.info);
        }

        @Override
        public String toString() {
            return super.toString() + name;
        }
    }
}