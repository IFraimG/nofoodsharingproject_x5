package com.example.nofoodsharingproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nofoodsharingproject.R;

import java.util.ArrayList;

public class SetterAdvertListAdapter extends RecyclerView.Adapter<SetterAdvertListAdapter.ViewHolder> {
    Context ctx;
    private final LayoutInflater inflater;

    public SetterAdvertListAdapter(Context context) {
        this.ctx = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public SetterAdvertListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.setter_advert_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SetterAdvertListAdapter.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);

        }
    }
}
