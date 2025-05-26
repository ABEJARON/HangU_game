package com.example.hangu;

public class GameSession {
    private static Game currentGame;

    public static Game startNewGame(String category) {
        String word = WordBank.getRandomWord(category);
        currentGame = new Game(word);
        return currentGame;
    }

    public static Game getCurrentGame() {
        return currentGame;
    }
}
