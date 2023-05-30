package com.buyhelp.nofoodsharingproject.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.models.Message;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private final Context ctx;
    private final List<Message> messages = new ArrayList<>();
    private final LayoutInflater inflater;
    private final String userID;

    public MessagesAdapter(Context context, String userID) {
        this.ctx = context;
        this.userID = userID;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_message, parent, false);
        return new MessagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.ViewHolder holder, int position) {
        Message message = messages.get(position);

        holder.body.setText(message.getBody());
        holder.dateCreated.setText(message.getDateCreated());
        if (message.getAuthorID().equals(userID)) {
            holder.msgLayout.setBackground(AppCompatResources.getDrawable(ctx, R.drawable.style7));
            holder.body.setTextColor(ctx.getResources().getColor(R.color.white));
            holder.textYou.setTextColor(ctx.getResources().getColor(R.color.white));
            holder.dateCreated.setTextColor(ctx.getResources().getColor(R.color.white));
            holder.textYou.setVisibility(View.VISIBLE);
        } else {
            holder.msgLayout.setBackground(AppCompatResources.getDrawable(ctx, R.drawable.custom_round_border));
            holder.body.setTextColor(ctx.getResources().getColor(R.color.black));
            holder.textYou.setTextColor(ctx.getResources().getColor(R.color.black));
            holder.dateCreated.setTextColor(ctx.getResources().getColor(R.color.black));
            holder.textYou.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void updateMessages(List<Message> messages) {
        try {
            this.messages.addAll(messages);
            notifyDataSetChanged();
        } catch (NullPointerException err) {
            err.printStackTrace();
        }
    }

    public void updateMessage(Message message) {
        try {
            this.messages.add(message);
            notifyDataSetChanged();
        } catch (NullPointerException err) {
           err.printStackTrace();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView body;
        public final TextView dateCreated;
        public final LinearLayout msgLayout;
        public final TextView textYou;

        public ViewHolder(View view) {
            super(view);
            this.body = (TextView) view.findViewById(R.id.message_body);
            this.dateCreated = (TextView) view.findViewById(R.id.message_date);
            this.msgLayout = (LinearLayout) view.findViewById(R.id.message_layout);
            this.textYou = (TextView) view.findViewById(R.id.message_you);
        }
    }
}
