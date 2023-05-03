package com.example.nofoodsharingproject.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.fragments.getter.GetterProductItem;
import com.google.firebase.annotations.concurrent.Background;

import java.util.List;

public class GetterProductListAdapter extends RecyclerView.Adapter<GetterProductListAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<GetterProductItem> products;

    public GetterProductListAdapter(Context context, List<GetterProductItem> products){
        this.products = products;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_getter_product_name, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int clickCounter = 0;
        GetterProductItem productItem = products.get(position);
        holder.name.setText(productItem.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.setBackgroundColor(Color.parseColor("#F44336"));
            }

        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.name_of_product);
        }
    }
}
