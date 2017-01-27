package de.benedikt_werner.WoodRiddleSolver;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Solver {
    private HashSet<Position> foundPositions;
    private LinkedList<Position> nextPositions;
    private boolean[][] black, white;

    public static void main(String[] args) {
        String[] white = {
                "     X     X   X       X   X  ",
                " XXX X X XXX XXX XXX XXX XXX X",
                "   X X X       X   X     X   X",
                " X X X XXX XXX XXX X XXX X X X",
                " X X     X   X     X X     X X"
        };
        String[] black = {
                "   X  X     X   X   X X   X   ",
                "XX XX X XXX X XXXXX X X X X XX",
                "X   X X X X X   X   X   X X   ",
                "XXX   X X X X X X X X XXX XXX ",
                "X   X   X     X   X     X     "
        };
        Solver s = new Solver(black, white);
        Scanner in = new Scanner(System.in);
        System.out.println("Solve from start? (0: start, 1: custom)");
        int i = in.nextInt();
        Position startPos;
        boolean backwards = false;
        if (i == 0)
            startPos = new Position(0, -5, new int[]{4, 4, 2, 4, 0});
        else {
            System.out.println("WhiteOffset BlackOffset Screws1-5 :");
            startPos = new Position(in.nextInt(), in.nextInt(), new int[]{in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt()});
            in.nextLine();
            System.out.println("Direction: (0: solve, 1: build) ");
            int dir = in.nextInt();
            if (dir == 1)
                backwards = true;
        }
        System.out.println("Starting...");
        Position pos = s.solve(startPos, backwards);
        
        if (pos == null)
            System.out.println("Impossible!");
        else {
            LinkedList<CompactMove> list = pos.getPathCompact();
            System.out.println("Found solution! Press enter for step:");
            while (!list.isEmpty()) {
                in.nextLine();
                System.out.println("Next: " + list.removeFirst());
            }
        }
        in.close();
    }
    
    public Solver(String[] black, String[] white) {
        this.black = new boolean[black.length][black[0].length()];
        this.white = new boolean[black.length][black[0].length()];
        
        for (int i = 0; i < white.length; i++) {
            for (int c = 0; c < white[4-i].length(); c++) {
                if (white[4-i].charAt(c) == 'X')
                    this.white[i][c] = true;
                else
                    this.white[i][c] = false;
                if (black[4-i].charAt(c) == 'X')
                    this.black[i][c] = true;
                else
                    this.black[i][c] = false;
            }
        }
    }

    public Position solve(Position startPos, boolean backwards) {
        int closestW = startPos.whiteOffset, closestB = startPos.blackOffset;
        int goalW = backwards ?  0 : -30;
        int goalB = backwards ? -5 :  25;
        foundPositions = new HashSet<>();
        nextPositions = new LinkedList<>();
        nextPositions.add(startPos);
        foundPositions.add(startPos);

        while (!nextPositions.isEmpty()) {
            Position position = nextPositions.removeFirst();
            
            if (!backwards) {
                if (position.whiteOffset < closestW) {
                    closestW = position.whiteOffset;
                    System.out.println(position);
                }
                else if (position.blackOffset > closestB) {
                    closestB = position.blackOffset;
                    System.out.println(position);
                }
                if (closestW == goalW || closestB == goalB)
                    return position;
            }
            else if (position.whiteOffset == goalW && position.blackOffset == goalB) {
                return position;
            }
            
            
            List<Move> possibleMoves = position.possibleMoves(black, white);
            for (Move move : possibleMoves) {
                Position newPosition = position.applyMove(move);
                if (!foundPositions.contains(newPosition)) {
                    nextPositions.addLast(newPosition);
                    foundPositions.add(newPosition);
                }
            }
        }
        return null;
    }
    
    public void printBool2D(boolean[][] array) {
        for (int i = 0; i < array.length; i++) {
            String line = "";
            for (int j = 0; j < array[i].length; j++)
                line += array[i][j] ? "X" : " ";
            System.out.println(line);
        }
    }
}
