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

public class nav_tpo_vehicle extends Fragment {

    EditText license;
    Button submit;
    TextView license_plate_number, registration_number, vehicle_type, owner_name, owner_contact, vehicle_make, vehicle_model, vehicle_year, insurance_policy_number, status;

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
        View view = inflater.inflate(R.layout.fragment_nav_tpo_vehicle, container, false);

        license = view.findViewById(R.id.editTextText);

        // Initialize Button
        submit = view.findViewById(R.id.submit);

        license_plate_number = view.findViewById(R.id.license_plate_number);
        registration_number = view.findViewById(R.id.registration_number);
        vehicle_type = view.findViewById(R.id.vehicle_type);
        owner_name = view.findViewById(R.id.owner_name);
        owner_contact = view.findViewById(R.id.owner_contact);
        vehicle_make = view.findViewById(R.id.vehicle_make);
        vehicle_model = view.findViewById(R.id.vehicle_model);
        vehicle_year = view.findViewById(R.id.vehicle_year);
        insurance_policy_number = view.findViewById(R.id.insurance_policy_number);
        status = view.findViewById(R.id.status);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new OkHttpClient();
                String url = "http://" + AppConfig.SERVER_IP + ":4000/vehicledetails";
                String licenseText = license.getText().toString();

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("id", licenseText);
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
                            // Request successful
                            assert response.body() != null;
                            final String responseData = response.body().string();
                            JSONObject jsonObject;

                            try {
                                jsonObject = new JSONObject(responseData);
                                String temp = jsonObject.getString("message");
                                if (temp.equals("license not found")) {
                                    // Display Toast message for license not found
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "License not found", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    return; // Exit the method to prevent further processing
                                }
                                temp = jsonObject.getString("license_plate_number");
                                final String finalTemp = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        license_plate_number.setText(String.format("License Plate Number : %s", finalTemp));
                                    }
                                });
                                temp = jsonObject.getString("registration_number");
                                final String finalTemp2 = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        registration_number.setText(String.format("Registration Number : %s", finalTemp2));
                                    }
                                });
                                temp = jsonObject.getString("vehicle_type");
                                final String finalTemp3 = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        vehicle_type.setText(String.format("Vehicle Type : %s", finalTemp3));
                                    }
                                });
                                temp = jsonObject.getString("owner_name");
                                final String finalTemp4 = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        owner_name.setText(String.format("Owner Name : %s", finalTemp4));
                                    }
                                });
                                temp = jsonObject.getString("owner_phone");
                                final String finalTemp5 = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        owner_contact.setText(String.format("Owner Contact : %s", finalTemp5));
                                    }
                                });
                                temp = jsonObject.getString("vehicle_make");
                                final String finalTemp6 = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        vehicle_make.setText(String.format("Vehicle Make : %s", finalTemp6));
                                    }
                                });
                                temp = jsonObject.getString("vehicle_model");
                                final String finalTemp7 = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        vehicle_model.setText(String.format("Vehicle Model : %s", finalTemp7));
                                    }
                                });
                                temp = jsonObject.getString("vehicle_year");
                                final String finalTemp8 = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        vehicle_year.setText(String.format("Vehicle Year : %s", finalTemp8));
                                    }
                                });
                                temp = jsonObject.getString("insurance_policy_number");
                                final String finalTemp9 = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        insurance_policy_number.setText(String.format("Insurance Policy Number : %s", finalTemp9));
                                    }
                                });
                                temp = jsonObject.getString("vehicle_status");
                                final String finalTemp10 = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        status.setText(String.format("Status : %s", finalTemp10));
                                    }
                                });

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            //Toast.makeText(UserLogin.this, responseBody, Toast.LENGTH_LONG).show();
                            // Process the response if needed
                        } else {
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