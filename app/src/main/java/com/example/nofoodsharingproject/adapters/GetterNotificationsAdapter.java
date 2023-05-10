package com.example.nofoodsharingproject.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.models.Notification;

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
        View view = inflater.inflate(R.layout.setter_notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GetterNotificationsAdapter.ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.title.setText(notification.getTitle());
        holder.desc.setText(notification.getDescription());
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
        final TextView title;
        final TextView desc;
        final TextView createdAt;
        final Button link;

        public ViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.setter_notify_item_title);
            this.desc = (TextView) view.findViewById(R.id.setter_notify_item_desc);
            this.createdAt = (TextView) view.findViewById(R.id.setter_notify_item_date);
            this.link = (Button) view.findViewById(R.id.setter_notify_item_link);
        }
    }
}
