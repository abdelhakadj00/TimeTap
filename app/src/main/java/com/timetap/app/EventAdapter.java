package com.timetap.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    public interface OnDeleteListener {
        void onDelete(EventEntry event);
    }
    
    private List<EventEntry> events;
    private Context context;
    private OnDeleteListener listener;
    
    public EventAdapter(Context context, List<EventEntry> events, OnDeleteListener listener) {
        this.context = context;
        this.events = events;
        this.listener = listener;
    }
    
    public void updateEvents(List<EventEntry> newEvents) {
        this.events = newEvents;
        notifyDataSetChanged();
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EventEntry event = events.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        holder.tvNumber.setText("#" + event.getId());
        holder.tvTime.setText(sdf.format(new Date(event.getTimestamp())));
        
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) listener.onDelete(event);
            return true;
        });
    }
    
    @Override
    public int getItemCount() {
        return events.size();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber, tvTime;
        
        ViewHolder(View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
