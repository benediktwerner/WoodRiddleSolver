package de.benedikt_werner.WoodRiddleSolver;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Position {
    public int whiteOffset, blackOffset;
    public int[] screws;
    private Move move;
    private Position from;
    
    public Position() {
        this(0, 0, new int[5]);
    }
    
    public Position(int whiteOffset, int blackOffset, int[] screws) {
        this.whiteOffset = whiteOffset;
        this.blackOffset = blackOffset;
        this.screws = screws;
        move = null;
        from = null;
    }
    
    private Position(int whiteOffset, int blackOffset, int[] screws, Move move, Position from) {
        this.whiteOffset = whiteOffset;
        this.blackOffset = blackOffset;
        this.screws = screws;
        this.move = move;
        this.from = from;
    }
    
    public Position applyMove(Move move) {
        int[] newScrews = Arrays.copyOf(screws, screws.length);
        if (move.screwIndex != -1) newScrews[move.screwIndex] += move.screwMove;
        return new Position(whiteOffset + move.whiteMove, blackOffset + move.blackMove, newScrews, move, this);
    }
    
    @Override
    public int hashCode() {
        int hashCode = whiteOffset * 3 + blackOffset * 11;
        for (int i = 0; i < screws.length; i++)
            hashCode += screws[i] * i;
        return Integer.hashCode(hashCode);
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof Position) {
            Position otherPos = (Position) other;
            if (whiteOffset == otherPos.whiteOffset && blackOffset == otherPos.blackOffset)
                return Arrays.equals(screws, otherPos.screws);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "W " + whiteOffset + ", B " + blackOffset + ", " + Arrays.toString(screws);
    }
    
    public void printPath() {
        Position pos = this;
        while (pos.from != null) {
            System.out.println(pos + " - " + pos.move);
            pos = pos.from;
        }
        System.out.println(pos + " - start");
    }
    
    public LinkedList<CompactMove> getPathCompact() {
        LinkedList<CompactMove> list = new LinkedList<>();
        Position pos = this;
        while (pos.from != null) {
            if (pos.move.screwIndex == -1) {
                CompactMove m = new CompactMove(pos.screws, pos.move.whiteMove == 0, pos.move.whiteMove + pos.move.blackMove);
                if (!list.isEmpty() && m.equals(list.getFirst()))
                    list.getFirst().amount += m.amount;
                else
                    list.addFirst(m);
            }
            pos = pos.from;
        }
        return list;
    }
    
    public List<Move> getMoves() {
        LinkedList<Move> moves = new LinkedList<>();
        Position pos = this;
        while (pos.from != null) {
            moves.addFirst(pos.move);
            pos = pos.from;
        }
        return moves;
    }

    public List<Move> possibleMoves(boolean[][] black, boolean[][] white) {
        List<Move> possibleMoves = new LinkedList<>();
        if (canMoveBlack(black, -1))
            possibleMoves.add(new Move(-1, true));
        if (canMoveBlack(black, +1))
            possibleMoves.add(new Move(+1, true));
        if (canMoveWhite(white, -1))
            possibleMoves.add(new Move(-1, false));
        if (canMoveWhite(white, +1))
            possibleMoves.add(new Move(+1, false));
        
        for (int screw = 0; screw < screws.length; screw++) {
            if (canMoveScrew(screw, black, white, -1))
                possibleMoves.add(new Move(-1, screw));
            if (canMoveScrew(screw, black, white, +1))
                possibleMoves.add(new Move(+1, screw));
        }
        return possibleMoves;
    }
    
    private boolean canMoveScrew(int screw, boolean[][] black, boolean[][] white, int move) {
        int newY = screws[screw] + move;
        if (newY >= black.length || newY < 0)
            return false;
        int wPos = screw * 6 - whiteOffset;
        int bPos = screw * 6 - blackOffset;
        return (wPos >= white[newY].length || wPos < 0 || !white[newY][wPos])
                && (bPos >= black[newY].length || bPos < 0 || !black[newY][bPos]);
    }
    
    private boolean canMoveWhite(boolean[][] white, int move) {
        if (move > 0 && whiteOffset == 0 || whiteOffset <= -30)
            return false;
        for (int i = 0; i < 5; i++)
            if (i * 6 - move - whiteOffset >= white[0].length)
                continue;
            else if (white[screws[i]][i * 6 - move - whiteOffset])
                return false;
        return true;
    }
    
    private boolean canMoveBlack(boolean[][] black, int move) {
        if (move < 0 && blackOffset == -5 || blackOffset >= 25)
            return false;
        for (int i = 0; i < 5; i++)
            if (i * 6 - move - blackOffset < 0)
                continue;
            else if (black[screws[i]][i * 6 - move - blackOffset])
                return false;
        return true;
    }
}
