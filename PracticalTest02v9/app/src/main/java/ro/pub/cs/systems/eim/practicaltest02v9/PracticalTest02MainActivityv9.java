package ro.pub.cs.systems.eim.practicaltest02v9;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PracticalTest02MainActivityv9 extends AppCompatActivity {


    private EditText cuvantText;
    private EditText lengthText;
    private Button initButton;

    private Button goMapButton;
    private Button goBackButton;
    private TextView resultTextView;
    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "http://www.anagramica.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02v9_main);

        cuvantText = findViewById(R.id.cuvant_text);
        lengthText = findViewById(R.id.length_text);
        initButton = findViewById(R.id.init_button);
        resultTextView = findViewById(R.id.result_text);
        goMapButton = findViewById(R.id.go_map_button);
//        goBackButton = findViewById(R.id.back_button);

        goMapButton.setOnClickListener(v -> {
            Intent intent = new Intent(PracticalTest02MainActivityv9.this, MapsActivity.class);
            startActivity(intent);
        });
        //goBackButton.setOnClickListener(v -> switchToBaseLayout());

        initButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = cuvantText.getText().toString();
                int minLength = Integer.parseInt(lengthText.getText().toString());
                fetchAnagrams(word, minLength);
            }
        });
    }

    private void fetchAnagrams(String word, int minLength) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AnagramApiService apiService = retrofit.create(AnagramApiService.class);
        Call<AnagramResponse> call = apiService.getAnagrams(word);

        call.enqueue(new Callback<AnagramResponse>() {
            @Override
            public void onResponse(Call<AnagramResponse> call, Response<AnagramResponse> response) {
                Log.d(TAG, "Response received: " + response.body());
                if (response.isSuccessful()) {
                    AnagramResponse anagramResponse = response.body();
                    if (anagramResponse != null) {
                        List<String> words = anagramResponse.getAll();
                        Log.d(TAG, "Parsed anagrams: " + words);
                        StringBuilder filteredWords = new StringBuilder();

                        for (String w : words) {
                            if (w.length() >= minLength) {
                                if (filteredWords.length() > 0) {
                                    filteredWords.append(", ");
                                }
                                filteredWords.append(w);
                            }
                        }

                        resultTextView.setText(filteredWords.toString());

                        // Send broadcast with the filtered words
                        Intent intent = new Intent("com.example.ANAGRAMS_RESULT");
                        intent.putExtra("anagrams", filteredWords.toString());
                        sendBroadcast(intent);
                    }
                } else {
                    Log.e(TAG, "Request failed");
                }
            }

            @Override
            public void onFailure(Call<AnagramResponse> call, Throwable t) {
                Log.e(TAG, "Network request failed", t);
            }
        });
    }

    private final BroadcastReceiver anagramReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.example.ANAGRAMS_RESULT".equals(intent.getAction())) {
                String anagrams = intent.getStringExtra("anagrams");
                resultTextView.setText(anagrams);  // Update the UI with the anagrams
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // Register the receiver to listen for the broadcast
        IntentFilter filter = new IntentFilter("com.example.ANAGRAMS_RESULT");
        registerReceiver(anagramReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unregister the receiver when the activity is stopped to avoid memory leaks
        unregisterReceiver(anagramReceiver);
    }
}