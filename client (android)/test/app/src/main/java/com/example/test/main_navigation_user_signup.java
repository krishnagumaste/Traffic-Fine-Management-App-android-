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

public class main_navigation_user_signup extends Fragment {

    EditText name, email, password;
    Button signup;
    OkHttpClient client;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_navigation_user_signup, container, false);

        name = view.findViewById(R.id.editTextTextPersonName2);
        email = view.findViewById(R.id.id);
        password = view.findViewById(R.id.editTextTextPassword);

        signup = view.findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Call your function here
                putData();
            }
        });

        return view;
    }

    private void putData() {
        client = new OkHttpClient();
        String url = "http://" + AppConfig.SERVER_IP + ":4000/usersignup";

        String name_data = name.getText().toString();
        String email_data = email.getText().toString();
        String password_data = password.getText().toString();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name_data);
            jsonBody.put("email", email_data);
            jsonBody.put("password", password_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);

        // Create a POST request with the JSON data
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

                    if(item.equals("valid")){
                        AppConfig.IsLoggedIn = true;
                        Intent i = new Intent(requireContext(), nav_draw.class);
                        startActivity(i);
                    }
                    else{
                        String message = "Enter Valid Email";

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } else {
                    // Request not successful
                    // Handle the error
                }
                response.close();
            }
        });
    }
}