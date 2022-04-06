package com.example.minesweeper;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.minesweeper.data.GameDifficulty;

public class App extends Application {
    static final String APP_PREFERENCES = "mSettings";
    private static final String DIFFICULTY_PREFERENCES_KEY = "difficulty";

    private static App instance;
    private int customFieldLength = 100;
    private int customFieldHeight = 100;
    private int customNumOfMines = 100;

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

    public static Integer getFieldLength() {
        switch (App.getInstance().getDifficulty()) {
            case NEWBIE:
                return 8;
            case AMATEUR:
                return 16;
            case PROFESSIONAL:
                return 30;
            default:
                return getInstance().customFieldLength;
        }
    }

    public static Integer getFieldHeight() {
        switch (App.getInstance().getDifficulty()) {
            case NEWBIE:
                return 8;
            case AMATEUR:
            case PROFESSIONAL:
                return 16;
            default:
                return getInstance().customFieldHeight;
        }
    }

    public static Integer getNumOfMines() {
        switch (App.getInstance().getDifficulty()) {
            case NEWBIE:
                return 10;
            case AMATEUR:
                return 40;
            case PROFESSIONAL:
                return 99;
            default:
                return getInstance().customNumOfMines;
        }
    }

    public static void setCustomFieldLength(int length) {
        getInstance().customFieldLength = length;
    }

    public static void setCustomFieldHeight(int height) {
        getInstance().customFieldHeight = height;
    }

    public static void setCustomNumOfMines(int mines) {
        getInstance().customNumOfMines = mines;
    }
}
