package de.benedikt_werner.WoodRiddleSolver;

import java.util.Arrays;

public class CompactMove {
    public int[] screws;
    public int amount;
    public boolean moveBlack;

    public CompactMove(int[] screws, boolean moveBlack, int amount) {
        this.screws = screws;
        this.moveBlack = moveBlack;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return Arrays.toString(screws) + " -> " + (moveBlack ? "Black" : "White") + (amount > 0 ? " right " : " left ") + Math.abs(amount); 
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CompactMove) {
            CompactMove otherMove = (CompactMove) other;
            return Arrays.equals(screws, otherMove.screws) && amount == otherMove.amount && moveBlack == otherMove.moveBlack;
        }
        return false;
    }
}
