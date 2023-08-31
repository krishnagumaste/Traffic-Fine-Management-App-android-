package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class main_navigation_tpo extends Fragment {

    EditText id,password;
    Button sign_in;
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
        View view = inflater.inflate(R.layout.fragment_main_navigation_tpo, container, false);

        id = view.findViewById(R.id.id);
        password = view.findViewById(R.id.adminpassword);
        sign_in = view.findViewById(R.id.tpologin);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new OkHttpClient();
                String url = "http://" + AppConfig.SERVER_IP + ":4000/tpologin";
                String pid_data = id.getText().toString();
                String password_data = password.getText().toString();

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("pid", pid_data);
                    jsonBody.put("password", password_data);
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
                    public void onFailure(@NonNull Call call, IOException e) {
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
                            String item;
                            try {
                                jsonObject = new JSONObject(responseData);
                                item = jsonObject.getString("message");
                                System.out.println(item);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            if(item.equals("Authorized")){
                                AppConfig.IsLoggedIn = true;
                                Intent i = new Intent(requireContext(), NavTpoActivity.class);
                                startActivity(i);
                            }
                            else {
                                // Inside an Activity or any other context
                                String message = "Invalid pid/password";

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                    }
                                });
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