package com.floodearlywarning.fews;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class WaterLevelService extends Service {
    private boolean running = true;
    private MediaPlayer mediaPlayer;
    private final IBinder binder = new LocalBinder();
    private ThingSpeakClient thingSpeakClient = new ThingSpeakClient();
    private final int THRESHOLD = 220; // Replace with your threshold value

    public class LocalBinder extends Binder {
        WaterLevelService getService() {
            return WaterLevelService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("WaterLevelServiceChannel", "Water Level Service", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.alert_sound);
        mediaPlayer.setLooping(true);

        Notification notification = new NotificationCompat.Builder(this, "WaterLevelServiceChannel")
                .setContentTitle("Water Level Service")
                .setContentText("The service is running...")
                .setSmallIcon(R.drawable.baseline_warning_amber_24)
                .build();

        startForeground(1, notification);

        new Thread(() -> {
            while (running) {
                try {
                    thingSpeakClient.parseData(thingSpeakClient.fetchData());
                }catch (Exception e){
                    Log.e("WaterLevelService", "Error fetching or parsing data", e);
                }
                String waterLevelString = thingSpeakClient.getLatestWaterLevel();
                if (waterLevelString != null) {
                    double waterLevel = (Double.parseDouble(waterLevelString)) / 100; //to convert to meter
                    updateFloodRiskValue(waterLevel);
                    if (waterLevel > THRESHOLD) {
                        sendNotification();
                    }
                }

                try {
                    Thread.sleep(1000); // Wait for second before fetching the next reading
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return START_STICKY; // Service will be restarted if it gets terminated
    }

    private void updateFloodRiskValue(double waterLevel) {
        Intent intent = new Intent("com.floodearlywarning.fews.WATER_LEVEL_UPDATE");
        intent.putExtra("waterLevel", waterLevel);
        sendBroadcast(intent);
    }

    private void sendNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }

        Notification notification = new NotificationCompat.Builder(this, "WaterLevelServiceChannel")
                .setContentTitle("Flood Warning")
                .setContentText("The water level is above the threshold. Immediate action is required.")
                .setSmallIcon(R.drawable.baseline_warning_amber_24)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat.from(this).notify(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}