package de.benedikt_werner.WoodRiddleSolver;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Position {
    private int whiteOffset, blackOffset;
    private int[] screws;
    private LinkedList<Move> moves;
    
    public Position() {
        this(0, 0, new int[5]);
    }
    
    public Position(int whiteOffset, int blackOffset, int[] screws) {
        this.whiteOffset = whiteOffset;
        this.blackOffset = blackOffset;
        this.screws = screws;
        moves = new LinkedList<>();
    }
    
    public Position applyMove(Move move) {
        int[] newScrews = Arrays.copyOf(screws, screws.length);
        if (move.screwIndex != -1) newScrews[move.screwIndex] += move.screwMove;
        return new Position(whiteOffset + move.whiteMove, blackOffset + move.blackMove, newScrews);
    }
    
    @Override
    public int hashCode() {
        int hashCode = whiteOffset * 17 + blackOffset * 9;
        for (int i = 0; i < screws.length; i++)
            hashCode += screws[i] * i;
        return Integer.hashCode(hashCode);
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof Position) {
            Position otherPos = (Position) other;
            if (this.whiteOffset == otherPos.whiteOffset && this.blackOffset == otherPos.blackOffset)
                return Arrays.equals(this.screws, otherPos.screws);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "W " + whiteOffset + ", B " + blackOffset + ", " + Arrays.toString(screws);
    }
    
    public List<Move> getMoves() {
        return moves;
    }
}
