package com.example.test;

import android.os.Bundle;

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

public class nav_tpo_home extends Fragment {

    EditText license_plate_number,fine_type,fine_date,fine_location,fine_amount,tpo_id;

    Button submit;

    OkHttpClient client;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_tpo_home, container, false);

        if(AppConfig.IsLoggedIn){
            AppConfig.IsLoggedIn = false;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "Logged IN Successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }

        license_plate_number = view.findViewById(R.id.editTextText8);
        fine_type = view.findViewById(R.id.editTextText10);
        fine_date = view.findViewById(R.id.editTextText3);
        fine_location = view.findViewById(R.id.editTextText12);
        fine_amount = view.findViewById(R.id.editTextText9);
        tpo_id = view.findViewById(R.id.editTextText11);

        submit = view.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new OkHttpClient();
                String url = "http://" + AppConfig.SERVER_IP + ":4000/addfine";
                String licenseText = license_plate_number.getText().toString();
                String typeText = fine_type.getText().toString();
                String dateText = fine_date.getText().toString();
                String locationText = fine_location.getText().toString();
                String amountText = fine_amount.getText().toString();
                String idText = tpo_id.getText().toString();

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("license_plate_number", licenseText);
                    jsonBody.put("fine_type", typeText);
                    jsonBody.put("fine_date", dateText);
                    jsonBody.put("fine_location", locationText);
                    jsonBody.put("fine_amount", amountText);
                    jsonBody.put("traffic_police_officer_id", idText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);

                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        // Handle request failure
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        // Handle request success
                        if (response.isSuccessful()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(requireContext(), "Fine Added Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(requireContext(), "Error in details", Toast.LENGTH_SHORT).show();
                                }
                            });
                        // Request not successful
                        // Handle the error
                        }
                        response.close();
                    }
            });
        }
    });

        return view;
    }
}