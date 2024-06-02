package com.floodearlywarning.fews;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;


public class ThingSpeakClient {

    private final OkHttpClient client = new OkHttpClient();
    private String latestWaterLevel;
    private String latestReadingTime;
    TimeZone timeZone = TimeZone.getTimeZone("Asia/Karachi");
    public String fetchData() {

        String channelId = "851627";
        String readApiKey = "IXWJ2J83MXA46MJI";

        String url = "https://api.thingspeak.com/channels/" + channelId + "/feeds.json?api_key=" + readApiKey;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void parseData(String jsonResponse) {

        // To be safe from null pointer exceptions
        if (jsonResponse == null) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray feeds = jsonObject.getJSONArray("feeds");
            JSONObject latestReading = feeds.getJSONObject(feeds.length() - 1);

            latestWaterLevel = latestReading.getString("Water Level");
            latestReadingTime = latestReading.getString("created_at");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isEsp8266Online() {
        String jsonResponse = fetchData();
        if (jsonResponse == null) {
            return false;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray feeds = jsonObject.getJSONArray("feeds");
            JSONObject latestReading = feeds.getJSONObject(feeds.length() - 1);

            String statusUpdateTime = latestReading.getString("Status");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US); // Adjusted the date format to match the ThingSpeak date format
            sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // ThingSpeak dates are in GMT
            Date statusUpdateDate = sdf.parse(statusUpdateTime); // Parse the statusUpdateTime into a Date object

            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Karachi")); // Set the time zone to Asia/Karachi
            String currentTimeInPakistan = sdf.format(new Date());

            // Check if the status was updated in the last 20 seconds
            long diffInMillies = Math.abs(new Date().getTime() - statusUpdateDate.getTime());
            long diffInSeconds = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            return diffInSeconds <= 20;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getLatestWaterLevel() {
        return latestWaterLevel;
    }

    public String getLatestReadingTime() {
        return latestReadingTime;
    }
}