package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class UserLogin extends AppCompatActivity {

    Button login,signup;
    EditText email,password;
    OkHttpClient client;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        login = findViewById(R.id.login);

        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLogin.this, UserSignup.class);

                startActivity(intent);
            }
        });

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Call your function here
                putData();
            }
        });
    }

    private void putData() {
        client = new OkHttpClient();
        String url = "http://" + AppConfig.SERVER_IP + ":4000/userlogin";  // Replace with your Node.js server URL and endpoint
        String email_data = email.getText().toString();
        String password_data = password.getText().toString();

        // Create a JSON object with email and password
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email_data);
            jsonBody.put("password", password_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a JSON request body
        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);

        // Create a POST request with the JSON data
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // Execute the request asynchronously
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
                        Intent i = new Intent(UserLogin.this, UserDashboard.class);
                        startActivity(i);
                    }
                    else {
                        // Inside an Activity or any other context
                        Context context = UserLogin.this;

                        String message = "Invalid email/password";

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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
}