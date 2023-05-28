package com.buyhelp.nofoodsharingproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class GetterNotificationsAdapter extends RecyclerView.Adapter<GetterNotificationsAdapter.ViewHolder> {
    private final Context ctx;
    private final List<Notification> notifications = new ArrayList<>();
    private final LayoutInflater inflater;

    public GetterNotificationsAdapter(Context context) {
        this.ctx = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public GetterNotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_getter_notifications, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GetterNotificationsAdapter.ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.title.setText(notification.getTitle());
        holder.desc.setText(notification.getDescription());
        holder.createdAt.setText(notification.getCreatedAt());

        if (notification.getAdvertID() != null && notification.getAdvertID().length() > 0) {
            holder.link.setVisibility(View.VISIBLE);
            holder.link.setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putString("advertID", notification.getAdvertID());
                Navigation.findNavController(v).navigate(R.id.action_getterNotifyF_to_setterAdvertFragment, args);
            });
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void updateNotifications(List<Notification> notifications) {
        try {
            this.notifications.clear();
            this.notifications.addAll(notifications);
            notifyDataSetChanged();
        } catch (NullPointerException err) {
            err.printStackTrace();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView desc;
        public final TextView createdAt;
        public final Button link;

        public ViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.notify_title);
            this.desc = (TextView) view.findViewById(R.id.notify_body);
            this.createdAt = (TextView) view.findViewById(R.id.notify_date);
            this.link = (Button) view.findViewById(R.id.notify_btn);
        }
    }
}
