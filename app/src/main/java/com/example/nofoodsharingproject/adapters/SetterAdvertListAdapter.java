package com.example.nofoodsharingproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.activities.SetterAdvert_Activity;
import com.example.nofoodsharingproject.models.Advertisement;

import java.util.ArrayList;
import java.util.List;

public class SetterAdvertListAdapter extends RecyclerView.Adapter<SetterAdvertListAdapter.ViewHolder> {
    final Context ctx;
    private final List<Advertisement> advertisements = new ArrayList<>();
    private final LayoutInflater inflater;
    private final Intent intent;

    public SetterAdvertListAdapter(Context context) {
        this.ctx = context;
        this.inflater = LayoutInflater.from(context);
        this.intent = new Intent(this.ctx, SetterAdvert_Activity.class);
    }

    @Override
    public SetterAdvertListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.setter_advert_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SetterAdvertListAdapter.ViewHolder holder, int position) {
        Advertisement advertisement = advertisements.get(position);
        holder.title.setText(advertisement.title);
        holder.desc.setText(advertisement.fieldDescription);
        holder.authorName.setText(advertisement.authorName);

        holder.link.setOnClickListener(View -> {
            intent.putExtra("advertID", advertisement.advertsID);
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return advertisements.size();
    }

    public void updateAdverts(List<Advertisement> advertisements) {
        if (this.advertisements != null) {
            try {
                this.advertisements.clear();
                this.advertisements.addAll(advertisements);
            } catch (NullPointerException err) {
                Log.e("msg", "null adverts");
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView desc;
        final TextView authorName;
        final Button link;

        public ViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.setter_advert_item_title);
            this.desc = (TextView) view.findViewById(R.id.setter_advert_item_desc);
            this.authorName = (TextView) view.findViewById(R.id.setter_advert_item_name);
            this.link = (Button) view.findViewById(R.id.setter_advert_item_link);
        }
    }
}
