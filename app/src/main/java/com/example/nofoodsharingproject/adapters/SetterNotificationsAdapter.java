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
import com.example.nofoodsharingproject.activities.SetterAdvertAC;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class SetterNotificationsAdapter extends RecyclerView.Adapter<SetterNotificationsAdapter.ViewHolder> {
    Context ctx;
    private final List<Notification> notifications = new ArrayList<>();
    private final LayoutInflater inflater;

    public SetterNotificationsAdapter(Context context) {
        this.ctx = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public SetterNotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.setter_notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SetterNotificationsAdapter.ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.title.setText(notification.title);
        holder.desc.setText(notification.description);
//
//        holder.link.setOnClickListener(View -> {
//            Intent intent = new Intent(this.ctx, SetterAdvertAC.class);
//            intent.putExtra("advertID", notification.getNotificationID());
//            ctx.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void updateNotifications(List<Notification> advertisements) {
        try {
            this.notifications.clear();
            this.notifications.addAll(advertisements);
        } catch (NullPointerException err) {
                Log.e("msg", "null notifications");
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        TextView createdAt;
        Button link;

        public ViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.setter_notify_item_title);
            this.desc = (TextView) view.findViewById(R.id.setter_notify_item_desc);
            this.createdAt = (TextView) view.findViewById(R.id.setter_notify_item_date);
            this.link = (Button) view.findViewById(R.id.setter_notify_item_link);
        }
    }
}
