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

public class nav_tpo_driver extends Fragment {

    EditText license;
    Button submit;
    TextView name, license_id, phone, email, address, issue_date, class_of_vehicle, registered_at, registered_office_code, status;

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
        View view = inflater.inflate(R.layout.fragment_nav_tpo_driver, container, false);

        // Initialize EditText
        license = view.findViewById(R.id.editTextText);

        // Initialize Button
        submit = view.findViewById(R.id.submit);

        // Initialize TextViews
        name = view.findViewById(R.id.name);
        license_id = view.findViewById(R.id.license_id);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        address = view.findViewById(R.id.address);
        issue_date = view.findViewById(R.id.issue_date);
        class_of_vehicle = view.findViewById(R.id.class_of_vehicle);
        registered_at = view.findViewById(R.id.registered_at);
        registered_office_code = view.findViewById(R.id.registered_office_code);
        status = view.findViewById(R.id.status);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new OkHttpClient();
                String url = "http://" + AppConfig.SERVER_IP + ":4000/driverdetails";
                String licenseText = license.getText().toString();

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("license_id", licenseText);
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
                                temp = jsonObject.getString("name");
                                final String finalTemp = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        name.setText(String.format("Name : %s", finalTemp));
                                    }
                                });
                                temp = jsonObject.getString("license_id");
                                final String finalTemp2 = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        license_id.setText(String.format("License ID : %s", finalTemp2));
                                    }
                                });
                                temp = jsonObject.getString("phone");
                                final String finalTemp3 = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        phone.setText(String.format("Phone : %s", finalTemp3));
                                    }
                                });
                                temp = jsonObject.getString("email");
                                final String finalTemp4 = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        email.setText(String.format("Email : %s", finalTemp4));
                                    }
                                });
                                temp = jsonObject.getString("address");
                                final String finalTemp5 = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        address.setText(String.format("Address : %s", finalTemp5));
                                    }
                                });
                                temp = jsonObject.getString("issue_date");
                                final String finalTemp6 = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        issue_date.setText(String.format("Issue Date : %s", finalTemp6));
                                    }
                                });
                                temp = jsonObject.getString("class_of_vehicle");
                                final String finalTemp7 = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        class_of_vehicle.setText(String.format("Class of Vehicle : %s", finalTemp7));
                                    }
                                });
                                temp = jsonObject.getString("registered_at");
                                final String finalTemp8 = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        registered_at.setText(String.format("Registered At : %s", finalTemp8));
                                    }
                                });
                                temp = jsonObject.getString("registered_office_code");
                                final String finalTemp9 = temp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        registered_office_code.setText(String.format("Registered Office Code : %s", finalTemp9));
                                    }
                                });
                                temp = jsonObject.getString("status");
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