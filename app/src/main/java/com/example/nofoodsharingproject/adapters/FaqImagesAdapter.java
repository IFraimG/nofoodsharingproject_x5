package com.example.nofoodsharingproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.nofoodsharingproject.R;

import java.util.List;

public class FaqImagesAdapter extends ArrayAdapter<Integer> {
    private Context context;
    private List<Integer> imageResourceIds;

    public FaqImagesAdapter(Context context, List<Integer> imageResourceIds) {
        super(context, 0, imageResourceIds);
        this.context = context;
        this.imageResourceIds = imageResourceIds;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_faq_image, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.faq_item_img);
        imageView.setImageResource(imageResourceIds.get(position));

        return convertView;
    }
}
