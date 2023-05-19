package com.example.nofoodsharingproject.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.models.Faq;

import java.util.ArrayList;
import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {
    private final Context ctx;
    private final List<Faq> faq = new ArrayList<>();
    private final LayoutInflater inflater;

    public FaqAdapter(Context context) {
        this.ctx = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public FaqAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_faq, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FaqAdapter.ViewHolder holder, int position) {
        Faq faqItem = faq.get(position);
        holder.question.setText(faqItem.getQuestion());
        holder.answer.setText(faqItem.getAnswer());

        holder.showAnswer.setOnClickListener(View -> {
            faqItem.setShow(true);
            holder.answerLayout.setVisibility(android.view.View.VISIBLE);
            holder.showAnswer.setVisibility(android.view.View.GONE);
            holder.removeAnswer.setVisibility(android.view.View.VISIBLE);

            if (faqItem.getImages() != null && faqItem.getImages().length > 0) {
                List<Integer> items = new ArrayList<>();
                for (int item : faqItem.getImages()) items.add(item);

                ArrayAdapter<Integer> arrayAdapter = new FaqImagesAdapter(ctx, items);
                holder.imagesList.setAdapter(arrayAdapter);

                holder.imagesList.setVisibility(android.view.View.VISIBLE);
            }
        });

        holder.removeAnswer.setOnClickListener(View -> {
            faqItem.setShow(false);
            holder.answerLayout.setVisibility(android.view.View.GONE);
            holder.showAnswer.setVisibility(android.view.View.VISIBLE);
            holder.removeAnswer.setVisibility(android.view.View.GONE);
            holder.imagesList.setVisibility(android.view.View.GONE);
        });
    }

    public void loadFaq(List<Faq> faq) {
        try {
            this.faq.clear();
            this.faq.addAll(faq);
            notifyDataSetChanged();
        } catch (NullPointerException err) {
            Log.e("msg", ctx.getString(R.string.unvisinle_error));
        }
    }


    @Override
    public int getItemCount() {
        return faq.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView question;
        public final TextView answer;
        public final ImageView showAnswer;
        public final LinearLayout answerLayout;
        public final ImageView removeAnswer;
        public final ListView imagesList;

        public ViewHolder(View view) {
            super(view);
            this.question = (TextView) view.findViewById(R.id.faq_item_question);
            this.showAnswer = (ImageView) view.findViewById(R.id.faq_item_show);
            this.answer = (TextView) view.findViewById(R.id.faq_item_answer);
            this.answerLayout = (LinearLayout) view.findViewById(R.id.faq_item_show_answer);
            this.removeAnswer = (ImageView) view.findViewById(R.id.faq_item_remove);
            this.imagesList = (ListView) view.findViewById(R.id.faq_item_images);
        }
    }
}

