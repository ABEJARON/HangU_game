package com.example.hangu;

import java.util.*;

public class WordBank {
    private static final Map<String, List<String>> categories = new HashMap<>();

    static {
        categories.put("Animals", Arrays.asList("tiger", "panda", "zebra", "shark", "eagle"));
        categories.put("Foods", Arrays.asList("rice", "cake", "milk", "beef", "corn"));
        categories.put("Countries", Arrays.asList("canada", "brazil", "japan", "france", "germany"));
        categories.put("Colors", Arrays.asList("red", "blue", "green", "yellow", "violet"));
        categories.put("Sports", Arrays.asList("soccer", "baseball", "tennis", "cricket", "hockey"));
    }

    public static Set<String> getCategories() {
        return categories.keySet();
    }

    public static String getRandomWord(String category) {
        List<String> words = categories.get(category);
        if (words == null || words.isEmpty())
            throw new IllegalArgumentException("Invalid or empty category: " + category);
        return words.get(new Random().nextInt(words.size()));
    }
}
