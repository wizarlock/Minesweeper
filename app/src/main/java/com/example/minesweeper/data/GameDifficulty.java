package com.example.minesweeper.data;

public enum GameDifficulty {
    NEWBIE, AMATEUR, PROFESSIONAL, SPECIAL;

    public static GameDifficulty getDefaultDifficulty() {
        return NEWBIE;
    }

}
