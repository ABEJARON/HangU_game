package com.example.hangu;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class MainActivity extends AppCompatActivity {

    private Game game;
    private TextView wordTextView;
    private TextView attemptsTextView;
    private GridLayout lettersGrid;
    private Spinner categorySpinner;
    private MediaPlayer mediaPlayer; // 🎵 Added MediaPlayer for background music


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);

        // 🎵 Start background music
        mediaPlayer = MediaPlayer.create(this, R.raw.cute);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        // UI references
        wordTextView = findViewById(R.id.wordTextView);
        attemptsTextView = findViewById(R.id.attemptsTextView);
        lettersGrid = findViewById(R.id.letterButtonsGrid);
        categorySpinner = findViewById(R.id.categorySpinner);

        setupCategorySpinner();

        // 🎯 Check if a new category was passed from WinActivity
        String categoryFromIntent = getIntent().getStringExtra("CATEGORY");
        if (categoryFromIntent != null) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) categorySpinner.getAdapter();
            int position = adapter.getPosition(categoryFromIntent);
            if (position >= 0) {
                categorySpinner.setSelection(position);
            }
        }
    }


    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                WordBank.getCategories().toArray(new String[0])
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = (String) parent.getItemAtPosition(position);
                startNewGame(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    private void startNewGame(String category) {
        game = GameSession.startNewGame(category);
        updateUI();
        setupLetterButtons();
    }

    private void setupLetterButtons() {
        lettersGrid.removeAllViews();

        for (char c = 'A'; c <= 'Z'; c++) {
            Button btn = new Button(this);
            btn.setText(String.valueOf(c));
            btn.setTextSize(18);
            btn.setAllCaps(true);
            btn.setTypeface(ResourcesCompat.getFont(this, R.font.joti_one));
            btn.setPadding(8, 8, 8, 8);
            btn.setOnClickListener(v -> onLetterClicked(((Button) v).getText().toString().charAt(0)));

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            btn.setLayoutParams(params);

            lettersGrid.addView(btn);
        }
    }



    private void onLetterClicked(char letter) {
        if (game.isGameOver()) {
            Toast.makeText(this, "Game is over. Please select a new category.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean correct = game.guess(letter);
        updateUI();

        if (game.isGameWon()) {
            // 🎉 Launch Win Activity
            Intent intent = new Intent(this, WinActivity.class);
            startActivity(intent);
        } else if (game.isGameOver()) {
            // 💀 Launch Failed Activity
            Intent intent = new Intent(this, FailedActivity.class);
            intent.putExtra("ANSWER", game.getFullWord()); // optionally pass the correct word
            startActivity(intent);
        }
    }


    private void updateUI() {
        // Update word with spaces
        StringBuilder spaced = new StringBuilder();
        for (char c : game.getMaskedWord().toCharArray()) {
            spaced.append(c).append(" ");
        }

        wordTextView.setText(spaced.toString().trim());
        wordTextView.setTypeface(ResourcesCompat.getFont(this, R.font.joti_one));

        // Update attempts counter
        int remaining = game.getRemainingAttempts();
        attemptsTextView.setText("Attempts left : " + remaining);

        // Update letter buttons (disable guessed ones)
        for (int i = 0; i < lettersGrid.getChildCount(); i++) {
            View child = lettersGrid.getChildAt(i);
            if (child instanceof Button) {
                char btnChar = ((Button) child).getText().toString().toLowerCase().charAt(0);
                child.setEnabled(!game.getGuessedLetters().contains(btnChar));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 🎵 Stop and release music resources
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
