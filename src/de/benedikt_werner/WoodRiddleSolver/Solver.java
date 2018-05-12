package de.benedikt_werner.WoodRiddleSolver;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Solver {
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
        
        // Get input
        Scanner in = new Scanner(System.in);
        System.out.println("Solve from start? (0: start, 1: custom)");
        int i = in.nextInt();

        Position startPos;
        boolean backwards = false;
        if (i == 0)
            startPos = new Position(0, -5, new int[]{4, 4, 2, 4, 0});
        else {
            System.out.println();
            System.out.println("Turn it so that you can read the \"Bonbon\" text.");
            System.out.println("White should be up with the hole on the left.");
            System.out.println("Black should be down with the hole on the right.");
            System.out.println();
            System.out.println("To solve black must be moved to the right (positive direction)");
            System.out.println("and white to the left (negative direction)");
            System.out.println();
            System.out.println("Offsets are given relative to the leftmost hole.");
            System.out.println("If both pieces are pushed in completely white is at 0 and black at -5");
            System.out.println("Screw positions are given from the bottom and start at 0");
            System.out.println();
            System.out.println("WhiteOffset BlackOffset Screws (start is: 0 -5 X X 2 4 0):");
            startPos = new Position(in.nextInt(), in.nextInt(), new int[]{in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt()});
            in.nextLine();
            if (startPos.whiteOffset != 0 || startPos.blackOffset != -5) {
                System.out.println("Direction: (0: solve, 1: build) ");
                int dir = in.nextInt();
                if (dir == 1)
                    backwards = true;
            }
        }
        
        // Solve
        System.out.println("Starting...");
        Position pos = s.solve(startPos, backwards);

        // Print result
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
        LinkedList<Position> nextPositions;
        HashSet<Position> foundPositions;

        int goalW = backwards ?  0 : -30;
        int goalB = backwards ? -5 :  25;

        foundPositions = new HashSet<>();
        nextPositions = new LinkedList<>();
        nextPositions.add(startPos);
        foundPositions.add(startPos);

        while (!nextPositions.isEmpty()) {
            Position position = nextPositions.removeFirst();

            if (!backwards && (position.whiteOffset == goalW || position.blackOffset == goalB)
                    || (backwards && position.whiteOffset == goalW && position.blackOffset == goalB))
                return position;

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
