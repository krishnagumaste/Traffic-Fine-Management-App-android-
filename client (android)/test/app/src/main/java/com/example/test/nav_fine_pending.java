package com.example.test;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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

public class nav_fine_pending extends Fragment {

    EditText text;
    Button check,btnPayNow;

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
        View view = inflater.inflate(R.layout.fragment_nav_fine_pending, container, false);

        text = view.findViewById(R.id.editTextText2);
        check = view.findViewById(R.id.check);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new OkHttpClient();
                String url = "http://" + AppConfig.SERVER_IP + ":4000/finedetails";
                String id = text.getText().toString();

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("id", id);
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

                            try {
                                JSONArray jsonArray = new JSONArray(responseData);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject js = jsonArray.getJSONObject(i);
                                    final JSONObject cardData = js; // Store the card data as final variable

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                createCard(cardData); // Call createCard method on the UI thread
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                                }
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

    private void createCard(JSONObject jsonObject) throws JSONException {
        // Create a new CardView for each card
        CardView cardView = new CardView(requireContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int margin = getResources().getDimensionPixelSize(R.dimen.card_margin);
        layoutParams.setMargins(margin, margin, margin, margin);
        cardView.setLayoutParams(layoutParams);

        // Set card background and elevation
        cardView.setCardBackgroundColor(getResources().getColor(R.color.card_background));
        cardView.setCardElevation(getResources().getDimension(R.dimen.card_elevation));

        // Create a LinearLayout as the container for card content
        LinearLayout cardContentLayout = new LinearLayout(requireContext());
        cardContentLayout.setOrientation(LinearLayout.VERTICAL);
        cardContentLayout.setPadding(margin, margin, margin, margin);
        cardView.addView(cardContentLayout);

        // Create TextViews for displaying fine details
        //TextView txtFineTitle = new TextView(requireContext());
        TextView license_plate_number = new TextView(requireContext());
        TextView fine_id = new TextView(requireContext());
        TextView fine_amount = new TextView(requireContext());
        TextView fine_type = new TextView(requireContext());
        TextView fine_date = new TextView(requireContext());
        TextView fine_location = new TextView(requireContext());

        // Set the data from the JSON object to the TextViews
        //String fineTitle = jsonObject.getString("title");
        String plate_number = jsonObject.getString("license_plate_number");
        String id = jsonObject.getString("fine_id");
        String amount = jsonObject.getString("fine_amount");
        String type = jsonObject.getString("fine_type");
        String date = jsonObject.getString("fine_date");
        String location = jsonObject.getString("fine_location");


        //txtFineTitle.setText("Title: " + fineTitle);
        license_plate_number.setText(String.format("License Plate Number : %s", plate_number));
        fine_id.setText(String.format("Fine ID : %s", id));
        fine_amount.setText(String.format("Fine Amount : ₹%s", amount));
        fine_type.setText(String.format("Fine Type : %s", type));
        fine_date.setText(String.format("Fine Date : %s", date));
        fine_location.setText(String.format("Fine Location : %s", location));

        // Create a Button for "Pay Now"
        Button btnPayNow = new Button(requireContext());
        btnPayNow.setText("Pay Now");

        // Set click listener for the "Pay Now" button
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
                // Perform payment or any other action
                Toast.makeText(requireContext(), "Pay Now clicked for fine: " + id, Toast.LENGTH_SHORT).show();
            }
        });

        // Add TextViews and Button to the card content layout
        //cardContentLayout.addView(txtFineTitle);
        cardContentLayout.addView(license_plate_number);
        cardContentLayout.addView(fine_id);
        cardContentLayout.addView(fine_amount);
        cardContentLayout.addView(fine_type);
        cardContentLayout.addView(fine_date);
        cardContentLayout.addView(fine_location);

        cardContentLayout.addView(btnPayNow);

        // Get the LinearLayout container from the XML layout
        LinearLayout llCardsContainer = requireView().findViewById(R.id.llCardsContainer);

        // Add the card view to the container
        llCardsContainer.addView(cardView);
    }


}