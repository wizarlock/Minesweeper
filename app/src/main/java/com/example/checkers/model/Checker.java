package com.example.checkers.model;

public class Checker {
    private final Colors color;
    private boolean condition;

    public Checker(Colors color, boolean condition) {
        this.color = color;
        this.condition = condition;
    }

    public Colors getColor() {
        return color;
    }

    public void setCondition(Boolean condition) {
        this.condition = condition;
    }

    public boolean getCondition() {
        return condition;
    }

    public enum Colors {
        WHITE, BLACK
    }
}
