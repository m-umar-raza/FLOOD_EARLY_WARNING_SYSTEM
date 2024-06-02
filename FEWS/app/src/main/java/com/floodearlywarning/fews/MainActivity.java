package com.floodearlywarning.fews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.floodearlywarning.fews.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private static final int THRESHOLD = 10; // Replace 10 with your actual threshold value

    private class WaterLevelUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            double waterLevel = intent.getIntExtra("waterLevel", 0);
            TextView floodRiskValueTextView = findViewById(R.id.flood_risk_value);
            CardView warningCardView = findViewById(R.id.flood_alert);
            Button okButton = findViewById(R.id.ok);
            okButton.setOnClickListener(v -> warningCardView.setVisibility(View.GONE));

            if (waterLevel < THRESHOLD) {
                floodRiskValueTextView.setText("Low");
                floodRiskValueTextView.setTextColor(Color.GREEN);
                warningCardView.setVisibility(View.GONE);
            } else if (waterLevel == THRESHOLD) {
                floodRiskValueTextView.setText("Medium");
                floodRiskValueTextView.setTextColor(Color.YELLOW);
                warningCardView.setVisibility(View.GONE);
            } else if (waterLevel > THRESHOLD && waterLevel < THRESHOLD * 2) {
                floodRiskValueTextView.setText("High");
                floodRiskValueTextView.setTextColor(Color.RED);
                warningCardView.setVisibility(View.GONE);
            } else {
                floodRiskValueTextView.setText("Very High");
                floodRiskValueTextView.setTextColor(Color.RED);
                warningCardView.setVisibility(View.VISIBLE);
            }
        }
    }

    private WaterLevelUpdateReceiver waterLevelUpdateReceiver = new WaterLevelUpdateReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = new Intent(this, WaterLevelService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

        // Start the WaterLevelService
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

        registerReceiver(waterLevelUpdateReceiver, new IntentFilter("com.floodearlywarning.fews.WATER_LEVEL_UPDATE"),Context.RECEIVER_NOT_EXPORTED);
        Button okButton = findViewById(R.id.ok);
        okButton.setOnClickListener(v -> {
            CardView warningCardView = findViewById(R.id.flood_alert);
            warningCardView.setVisibility(View.GONE);
        });

        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.weather) {
                replaceFragment(new WeatherFragment());
            } else if (itemId == R.id.safety) {
                replaceFragment(new SafetyFragment());
            } else if (itemId == R.id.emergency) {
                replaceFragment(new EmergencyFragment());
            }

            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(waterLevelUpdateReceiver);
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(waterLevelUpdateReceiver, new IntentFilter("com.floodearlywarning.fews.WATER_LEVEL_UPDATE"),Context.RECEIVER_NOT_EXPORTED);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(waterLevelUpdateReceiver);
    }
}