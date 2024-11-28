package ro.pub.cs.systems.eim.practicaltest01var05;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import androidx.activity.result.ActivityResultLauncher;

public class PracticalTest01Var05MainActivity extends AppCompatActivity {

    Button navigateToSecondaryActivity, topLeft, topRight, center, bottomLeft, bottomRight;
    TextView input1;
    int buttonPressCount = 0;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var05_main);

        navigateToSecondaryActivity = findViewById(R.id.navigate_to_secondary_activity_button);
        topLeft = findViewById(R.id.top_left_button);
        topRight = findViewById(R.id.top_right_button);
        center = findViewById(R.id.center_button);
        bottomLeft = findViewById(R.id.bottom_left_button);
        bottomRight = findViewById(R.id.bottom_right_button);
        input1 = findViewById(R.id.input1);
        input1.setText("");

        if(savedInstanceState != null) {
            buttonPressCount = savedInstanceState.getInt(Constants.BUTTON_PRESS_COUNT_KEY, 0);
            Toast.makeText(this, "Button press count: " + buttonPressCount, Toast.LENGTH_SHORT).show();
        }

        View.OnClickListener listener = v -> {
            String text = input1.getText().toString();
            if (!text.isEmpty()) {
                text += ", ";
            }
            if (v.getId() == R.id.top_left_button) {
                text += "Top Left";
            } else if (v.getId() == R.id.top_right_button) {
                text += "Top Right";
            } else if (v.getId() == R.id.center_button) {
                text += "Center";
            } else if (v.getId() == R.id.bottom_left_button) {
                text += "Bottom Left";
            } else if (v.getId() == R.id.bottom_right_button) {
                text += "Bottom Right";
            }
            input1.setText(text);
            buttonPressCount++;
        };

        topLeft.setOnClickListener(listener);
        topRight.setOnClickListener(listener);
        center.setOnClickListener(listener);
        bottomLeft.setOnClickListener(listener);
        bottomRight.setOnClickListener(listener);


        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Toast.makeText(this, "Verify button pressed", Toast.LENGTH_SHORT).show();
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        Toast.makeText(this, "Cancel button pressed", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        navigateToSecondaryActivity.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PracticalTest01Var05SecondaryActivity.class);
            intent.putExtra(Constants.INPUT1, input1.getText().toString());
            activityResultLauncher.launch(intent);
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.BUTTON_PRESS_COUNT_KEY, buttonPressCount);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        buttonPressCount = savedInstanceState.getInt(Constants.BUTTON_PRESS_COUNT_KEY, 0);
    }
}