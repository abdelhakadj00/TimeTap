package com.timetap.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView tvTimer, tvCount;
    private Button btnRecord;
    private RecyclerView recyclerView;
    private EventRepository repository;
    private EventAdapter adapter;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable tickRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tvTimer = findViewById(R.id.tv_timer);
        tvCount = findViewById(R.id.tv_count);
        btnRecord = findViewById(R.id.btn_record);
        recyclerView = findViewById(R.id.recycler);
        
        repository = EventRepository.getInstance(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventAdapter(this, repository.getAllEvents(), event -> {
            repository.deleteEvent(event.getId());
            updateUI();
        });
        recyclerView.setAdapter(adapter);
        
        btnRecord.setOnClickListener(v -> {
            repository.addEvent(System.currentTimeMillis());
            updateUI();
            startTimer();
        });
        
        startTimer();
        updateUI();
    }
    
    private void startTimer() {
        if (tickRunnable != null) handler.removeCallbacks(tickRunnable);
        tickRunnable = new Runnable() {
            @Override
            public void run() {
                EventEntry last = repository.getLastEvent();
                if (last != null) {
                    long elapsed = System.currentTimeMillis() - last.getTimestamp();
                    tvTimer.setText(TimeUtils.formatDuration(elapsed));
                } else {
                    tvTimer.setText("00:00:00");
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(tickRunnable);
    }
    
    private void updateUI() {
        List<EventEntry> events = repository.getAllEvents();
        tvCount.setText("عدد الضغطات: " + events.size());
        adapter.updateEvents(events);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (tickRunnable != null) handler.removeCallbacks(tickRunnable);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
        updateUI();
    }
}
