package com.example.minesweeper;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.minesweeper.data.GameDifficulty;

public class App extends Application {
    static final String APP_PREFERENCES = "mSettings";
    private static final String DIFFICULTY_PREFERENCES_KEY = "difficulty";

    private static App instance;

    @NonNull
    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public void setDifficulty(GameDifficulty difficulty) {
        getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
                .edit()
                .putInt(DIFFICULTY_PREFERENCES_KEY, difficulty.ordinal())
                .apply();
    }

    public GameDifficulty getDifficulty() {
        int ordinal = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
                .getInt(DIFFICULTY_PREFERENCES_KEY, GameDifficulty.getDefaultDifficulty().ordinal());

        return GameDifficulty.values()[ordinal];
    }
}
