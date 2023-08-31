package com.example.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class home_admin extends Fragment {

    TextView driver, vehicle, tpo, fine, sumfine;
    OkHttpClient client;
    private static final int DELAY_DURATION = 1000; // Delay duration in milliseconds

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);
        if(AppConfig.IsLoggedIn){
            AppConfig.IsLoggedIn = false;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "Logged IN Successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }
        driver = view.findViewById(R.id.driver);
        vehicle = view.findViewById(R.id.vehicle);
        tpo = view.findViewById(R.id.tpo);
        fine = view.findViewById(R.id.totalfine);
        sumfine = view.findViewById(R.id.sumfine);
        client = new OkHttpClient();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(requireContext(), "Wait till the data is retrieved", Toast.LENGTH_LONG).show();
            }
        });
        fetchDataWithDelay();
        return view;
    }

    private void fetchDataWithDelay() {
        // Create a Handler instance with the main looper
        Handler handler = new Handler(Looper.getMainLooper());

        // Use postDelayed to introduce a delay before making the client call
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchData();
            }
        }, DELAY_DURATION);
    }

    private void fetchData() {
        Request request = new Request.Builder()
                .url("http://" + AppConfig.SERVER_IP + ":4000/adminhome")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                // Handle the failure scenario
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    final String responseData = response.body().string();
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(responseData);
                        updateTextViews(jsonObject);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // Handle the failure scenario
                }
            }
        });
    }

    private void updateTextViews(final JSONObject jsonObject) {
        try {
            final String driverValue = jsonObject.getString("driver");
            final String vehicleValue = jsonObject.getString("vehicle");
            final String tpoValue = jsonObject.getString("traffic_police");
            final String fineValue = jsonObject.getString("totalfine");
            final String sumFineValue = jsonObject.getString("fineamount");

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    driver.setText(String.format("Total Number of Drivers: %s", driverValue));
                    vehicle.setText(String.format("Total Number of Vehicles: %s", vehicleValue));
                    tpo.setText(String.format("Total Number of Traffic Police Officers: %s", tpoValue));
                    fine.setText(String.format("Total Number of Fines: %s", fineValue));
                    sumfine.setText(String.format("Total Fine Amount: %s", sumFineValue));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}