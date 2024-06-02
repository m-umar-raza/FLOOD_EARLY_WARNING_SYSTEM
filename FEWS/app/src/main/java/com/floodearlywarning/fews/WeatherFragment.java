package com.floodearlywarning.fews;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherFragment extends Fragment {

    private static final String CITY = "Islamabad,pak";
    private static final String API = "fc86b76718413f64a2b11a548d04271d"; // Use API key

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;

    public WeatherFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_weather, container, false);

        new WeatherTask().execute();

        // Inflate the layout for this fragment
        return rootView;
    }

    private class WeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing the ProgressBar, Making the main design GONE
            rootView.findViewById(R.id.loader).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.mainContainer).setVisibility(View.GONE);
            rootView.findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            String response = null;
            try {
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
                in.close();
                response = result.toString();
            } catch (Exception e) {
                response = null;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                // Extracting JSON returns from the API
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
                String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");

                long sunrise = sys.getLong("sunrise");
                long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");

                String address = jsonObj.getString("name") + ", " + sys.getString("country");

                // Populating extracted data into our views
                ((TextView) rootView.findViewById(R.id.address)).setText(address);
                ((TextView) rootView.findViewById(R.id.updated_at)).setText(updatedAtText);
                ((TextView) rootView.findViewById(R.id.status)).setText(weatherDescription.substring(0, 1).toUpperCase() + weatherDescription.substring(1));
                ((TextView) rootView.findViewById(R.id.temp)).setText(temp);
                ((TextView) rootView.findViewById(R.id.sunrise)).setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                ((TextView) rootView.findViewById(R.id.sunset)).setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                ((TextView) rootView.findViewById(R.id.wind)).setText(windSpeed);
                ((TextView) rootView.findViewById(R.id.pressure)).setText(pressure);
                ((TextView) rootView.findViewById(R.id.humidity)).setText(humidity);

                // Views populated, Hiding the loader, Showing the main design
                rootView.findViewById(R.id.loader).setVisibility(View.GONE);
                rootView.findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);

            } catch (Exception e) {
                rootView.findViewById(R.id.loader).setVisibility(View.GONE);
                rootView.findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }
        }
    }
}
