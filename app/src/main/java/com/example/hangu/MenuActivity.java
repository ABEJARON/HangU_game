package com.example.hangu;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button playButton, optionsButton, exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        playButton = findViewById(R.id.playButton);
        optionsButton = findViewById(R.id.optionsButton);
        exitButton = findViewById(R.id.exitButton);

        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(intent);
        });

        optionsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, OptionsActivity.class);
            startActivity(intent);
        });

        exitButton.setOnClickListener(v -> showExitConfirmation());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Only start music if user enabled it in settings
        if (MusicManager.isMusicEnabled(this)) {
            MusicManager.startMusic(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // ⚠️ Do not stop or pause music — it's managed globally now
    }

    private void showExitConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Game")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    MusicManager.stopMusic(); // ✅ Proper global stop
                    finishAffinity();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // No need to stop music here; let MusicManager handle it
    }
}
