package ro.pub.cs.systems.eim.practicaltest01var05;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

public class PracticalTest01Var05SecondaryActivity extends AppCompatActivity {

    TextView textViewSecondary = findViewById(R.id.textViewSecondary);
    Button verifyButton = findViewById(R.id.verify_button);
    Button cancelButton = findViewById(R.id.cancel_button);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var05_secondary);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.INPUT1)) {
            String text = intent.getStringExtra(Constants.INPUT1);
            textViewSecondary.setText(text);
        }

        verifyButton.setOnClickListener(view -> {
            setResult(RESULT_OK, new Intent());
            finish();
        });

        cancelButton.setOnClickListener(view -> {
            setResult(RESULT_CANCELED, new Intent());
            finish();
        });
    }
}