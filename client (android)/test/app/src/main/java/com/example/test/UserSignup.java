package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class UserSignup extends AppCompatActivity {

    EditText name, email, password;
    Button signup;
    OkHttpClient client;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        name = findViewById(R.id.editTextTextPersonName2);
        email = findViewById(R.id.id);
        password = findViewById(R.id.editTextTextPassword);

        signup = findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Call your function here
                putData();
            }
        });
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
                        Intent i = new Intent(UserSignup.this, UserDashboard.class);
                        startActivity(i);
                    }
                    else{
                        Context context = UserSignup.this;

                        String message = "Enter Valid Email";

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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