package com.example.hangu;

import java.util.HashSet;
import java.util.Set;

public class Game {
    private final String word;
    private final Set<Character> guessedLetters = new HashSet<>();
    private int maxAttempts = 6;
    private int wrongAttempts = 0;

    public Game(String word) {
        this.word = word.toLowerCase();
    }

    public boolean guess(char letter) {
        letter = Character.toLowerCase(letter);
        guessedLetters.add(letter);
        if (!word.contains(String.valueOf(letter))) {
            wrongAttempts++;
            return false;
        }
        return true;
    }

    public boolean isGameWon() {
        for (char c : word.toCharArray()) {
            if (!guessedLetters.contains(c)) return false;
        }
        return true;
    }

    public boolean isGameOver() {
        return wrongAttempts >= maxAttempts || isGameWon();
    }

    public String getMaskedWord() {
        StringBuilder masked = new StringBuilder();
        for (char c : word.toCharArray()) {
            masked.append(guessedLetters.contains(c) ? c : "_");
        }
        return masked.toString();
    }

    public int getRemainingAttempts() {
        return maxAttempts - wrongAttempts;
    }

    public Set<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public String getFullWord() {
        return word;
    }
}
