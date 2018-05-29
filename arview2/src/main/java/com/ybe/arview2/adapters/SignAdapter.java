package com.ybe.arview2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybe.arview2.R;
import com.ybe.arview2.interfaces.RecyclerViewClickListener;
import com.ybe.arview2.models.Sign;

import java.util.List;


public class SignAdapter extends RecyclerView.Adapter<SignAdapter.ViewHolder> {

    private final RecyclerViewClickListener clickListener;
    private List<Sign> signs;
    private ImageView previousSelected;
    private boolean first = true;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView signName;
        ImageView signImage;

        public ViewHolder(View itemView) {
            super(itemView);
            signImage = itemView.findViewById(R.id.img_ar_itemImage);
            signName = itemView.findViewById(R.id.txt_ar_itemName);
        }
    }

    public SignAdapter(List<Sign> signs, RecyclerViewClickListener clickListener) {
        this.signs = signs;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ar_list_item, parent, false);

        ImageView image = itemView.findViewById(R.id.img_ar_itemImage);

        if (first) {
            image.setBackgroundResource(R.drawable.border_round_selected);
            first = false;
            previousSelected = image;
        }

        itemView.setOnClickListener(view -> {
            previousSelected.setBackgroundResource(R.drawable.border_round);
            image.setBackgroundResource(R.drawable.border_round_selected);

            clickListener.recyclerViewListClicked(itemView);
            previousSelected = image;
        });

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sign sign = signs.get(position);

        Context context = holder.signImage.getContext();
        int resID = context.getResources().getIdentifier(sign.getImagePath(), "drawable", context.getPackageName());

        holder.signImage.setImageResource(resID);
        holder.signName.setText(sign.getName());
    }

    @Override
    public int getItemCount() {
        return signs.size();
    }
}
