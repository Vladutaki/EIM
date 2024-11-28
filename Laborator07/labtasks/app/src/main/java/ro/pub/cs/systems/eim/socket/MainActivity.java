package ro.pub.cs.systems.eim.socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private TextView outputSocket;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputSocket = findViewById(R.id.outputSocket);
        Button buttonSocket = findViewById(R.id.buttonSocket);
        buttonSocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        communicateWithServer();
                    }
                }).start();
            }
        });
    }

    private void communicateWithServer() {
        try (Socket socket = new Socket("10.0.2.2", 5000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("Hello Server");

            final String response = in.readLine();

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    outputSocket.setText(response);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}