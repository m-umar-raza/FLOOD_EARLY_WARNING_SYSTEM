package com.floodearlywarning.fews;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


//public class HomeFragment extends Fragment implements OnMapReadyCallback{
public class HomeFragment extends Fragment {
    private ThingSpeakClient thingSpeakClient = new ThingSpeakClient();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // FOR MAPVIEW
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
//    private FusedLocationProviderClient fusedLocationClient;
//    private GoogleMap mMap;
//    private MapView mapView;
//    private LatLng sensorLocation = new LatLng(-34, 151); // Replace with your sensor's coordinates
    // MAPVIEW END

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public void updateSensorStatus(View view) {
        TextView sensorStatusValueTextView = view.findViewById(R.id.sensor_status_value); // Replace with the actual id of your TextView
        if (thingSpeakClient.isEsp8266Online()) {
            sensorStatusValueTextView.setText("Online");
            sensorStatusValueTextView.setTextColor(Color.GREEN);
        } else {
            sensorStatusValueTextView.setText("Offline");
            sensorStatusValueTextView.setTextColor(Color.BLACK);
        }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        // FOR INTERNET STATUS
        updateInternetStatus(view);
        // INTERNET STATUS END

        // FOR LINE CHART SHOWING WATER LEVEL VS TIME
        WebView webView = view.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://thingspeak.com/channels/851627/charts/1");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // The page loaded successfully, the chart should be showing
                Log.d("WebView", "The chart has loaded successfully");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                // There was an error while loading the page, the chart might not be showing
                Log.d("WebView", "There was an error while loading the chart: " + error.getDescription());
                Toast.makeText(getActivity(), "Failed to load chart", Toast.LENGTH_SHORT).show();
            }
        });
        // LINE CHART END

        // FOR MAPVIEW
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
//
//        mapView = view.findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(this);
//
//        Button enableLocationButton = view.findViewById(R.id.enable_live_location);
//        enableLocationButton.setOnClickListener(v -> {
//            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        LOCATION_PERMISSION_REQUEST_CODE);
//            } else {
//                fusedLocationClient.getLastLocation()
//                        .addOnSuccessListener(getActivity(), location -> {
//                            if (location != null) {
//                                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
//                                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
//                                updateLocationValueTextView(view, "Available");
//                            }
//                        });
//            }
//        });
//
//        Button addLocationButton = view.findViewById(R.id.add_location);
//        addLocationButton.setOnClickListener(v -> {
//            // Replace with your code to get the user's manually entered location
//            LatLng userLocation = new LatLng(-34, 151);
//            mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
//            updateLocationValueTextView(view, "Available");
//        });
        // MAPVIEW END

        return view;
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.addMarker(new MarkerOptions().position(sensorLocation).title("Sensor Location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sensorLocation));
//    }
//
//    private void updateLocationValueTextView(View view, String text) {
//        TextView locationValueTextView = view.findViewById(R.id.location_value);
//        locationValueTextView.setText(text);
//        locationValueTextView.setTextColor(Color.GREEN);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }

    public void updateInternetStatus(View view) {
        TextView internetValueTextView = view.findViewById(R.id.internet_value);
        if (isNetworkConnected()) {
            internetValueTextView.setText("Connected");
            internetValueTextView.setTextColor(Color.GREEN);
        } else {
            internetValueTextView.setText("Please connect to a network");
            internetValueTextView.setTextColor(Color.BLACK);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}