package com.buyhelp.nofoodsharingproject.presentation.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.models.Advertisement;

import java.util.ArrayList;
import java.util.List;

public class GiverAdvertListAdapter extends RecyclerView.Adapter<GiverAdvertListAdapter.ViewHolder> {
    private final Context ctx;
    private final List<Advertisement> advertisements = new ArrayList<>();
    private final LayoutInflater inflater;

    public GiverAdvertListAdapter(Context context) {
        this.ctx = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public GiverAdvertListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.giver_advert_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GiverAdvertListAdapter.ViewHolder holder, int position) {
        Advertisement advertisement = advertisements.get(position);
        holder.title.setText(advertisement.getTitle());
        holder.authorName.setText(advertisement.getAuthorName());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.ctx, R.layout.item_needy_product_name, advertisement.getListProducts());
        holder.productList.setAdapter(arrayAdapter);


        holder.link.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("advertID", advertisement.getAdvertsID());
            Navigation.findNavController(v).navigate(R.id.action_giverAdvrsF_to_giverAdvertFragment, args);
        });
    }

    @Override
    public int getItemCount() {
        return advertisements.size();
    }

    public void updateAdverts(List<Advertisement> advertisements) {
        try {
            this.advertisements.clear();
            this.advertisements.addAll(advertisements);

            notifyDataSetChanged();
        } catch (NullPointerException err) {
            err.printStackTrace();
        }}

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView authorName;
        private final Button link;
        private final ListView productList;

        public ViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.giver_advert_item_title);

            this.authorName = (TextView) view.findViewById(R.id.giver_advert_item_name);
            this.link = (Button) view.findViewById(R.id.giver_advert_item_link);
            this.productList = (ListView) view.findViewById(R.id.giver_advert_product_list);
        }
    }
}
