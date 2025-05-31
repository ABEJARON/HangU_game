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

    private void showExitConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Game")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> finishAffinity())
                .setNegativeButton("No", null)
                .show();
    }
}
