package com.example.minesweeper.data;

public enum GameDifficulty {
    NEWBIE, AMATEUR, PROFESSIONAL;

    public static GameDifficulty getDefaultDifficulty() {
        return NEWBIE;
    }
}
