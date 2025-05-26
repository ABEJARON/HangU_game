package com.example.hangu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private ImageView playImageView, optionsImageView, exitImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        playImageView = findViewById(R.id.playImageView);
        optionsImageView = findViewById(R.id.optionsImageView);
        exitImageView = findViewById(R.id.exitImageView);

        playImageView.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(intent);
        });

        optionsImageView.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, OptionsActivity.class);
            startActivity(intent);
        });

        exitImageView.setOnClickListener(v -> showExitConfirmation());
    }

    private void showExitConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Game")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> finishAffinity()) // Closes the app
                .setNegativeButton("No", null)
                .show();
    }
}
