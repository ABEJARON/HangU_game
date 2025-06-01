package com.example.hangu;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Game game;
    private TextView wordTextView;
    private TextView attemptsTextView;
    private GridLayout lettersGrid;
    private Spinner categorySpinner;
    private LinearLayout balloonContainer;

    // Body part views
    private ImageView[] bodyParts;

    // Balloon views to pop on incorrect guesses
    private List<ImageView> balloonViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main1);

        // UI references
        wordTextView = findViewById(R.id.wordTextView);
        attemptsTextView = findViewById(R.id.attemptsTextView);
        lettersGrid = findViewById(R.id.letterButtonsGrid);
        categorySpinner = findViewById(R.id.categorySpinner);
        balloonContainer = findViewById(R.id.balloonContainer);

        // Map body parts
        bodyParts = new ImageView[]{
                findViewById(R.id.head),
                findViewById(R.id.body),
                findViewById(R.id.right_arm),
                findViewById(R.id.left_arm),
                findViewById(R.id.right_leg),
                findViewById(R.id.left_leg)
        };

        setupCategorySpinner();

        Button restartButton = findViewById(R.id.restartButton);
        restartButton.setOnClickListener(v -> {
            String category = (String) categorySpinner.getSelectedItem();
            startNewGame(category);
        });
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

        // Dynamically add balloons
        balloonContainer.removeAllViews();
        balloonViews.clear();

        String phrase = game.getFullWord();
        int balloonCount = phrase.replace(" ", "").length();

        for (int i = 0; i < balloonCount; i++) {
            ImageView balloon = new ImageView(this);
            balloon.setImageResource(R.drawable.balloon); // Replace with your balloon drawable
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60, 90);
            params.setMargins(4, 0, 4, 0);
            balloon.setLayoutParams(params);
            balloonContainer.addView(balloon);
            balloonViews.add(balloon);
        }
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

        if (!correct) {
            Toast.makeText(this, "Wrong guess!", Toast.LENGTH_SHORT).show();
        }

        updateUI();

        if (game.isGameWon()) {
            Toast.makeText(this, "Congratulations! You won!", Toast.LENGTH_LONG).show();
        } else if (game.isGameOver()) {
            Toast.makeText(this, "Game Over! The word was: " + game.getFullWord(), Toast.LENGTH_LONG).show();
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

        // Update hangman body part visibility
        int incorrectGuesses = 6 - remaining;
        for (int i = 0; i < bodyParts.length; i++) {
            bodyParts[i].setVisibility(i < incorrectGuesses ? View.VISIBLE : View.INVISIBLE);
        }

        // Pop balloons visually for wrong guesses
        for (int i = 0; i < incorrectGuesses && i < balloonViews.size(); i++) {
            balloonViews.get(i).setVisibility(View.INVISIBLE);
        }

        // Update letter buttons (disable guessed ones)
        for (int i = 0; i < lettersGrid.getChildCount(); i++) {
            View child = lettersGrid.getChildAt(i);
            if (child instanceof Button) {
                char btnChar = ((Button) child).getText().toString().toLowerCase().charAt(0);
                child.setEnabled(!game.getGuessedLetters().contains(btnChar));
            }
        }
    }
}
