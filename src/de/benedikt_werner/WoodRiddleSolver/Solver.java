package de.benedikt_werner.WoodRiddleSolver;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Solver {
    private HashSet<Position> foundPositions;
    private LinkedList<Position> nextPositions;

    public static void main(String[] args) {
    }

    public List<Boolean> solve(Position startingPosition) {
        foundPositions = new HashSet<>();
        nextPositions = new LinkedList<>();
        nextPositions.add(startingPosition);

        while (!nextPositions.isEmpty()) {
            Position position = nextPositions.removeFirst();
            if (position.isPieceFalling()) // Check if offset of black OR white is big enoguh (so that all screws are out)
                return position.getTurns();

            foundPositions.add(position);
            Position turnedClockwise = position.turn(true);
            if (!foundPositions.contains(turnedClockwise)) {
                nextPositions.addLast(turnedClockwise);
            }
            Position turnedAntiClockwise = position.turn(false);
            if (!foundPositions.contains(turnedAntiClockwise)) {
                nextPositions.addLast(turnedAntiClockwise);
            }
        }
        return null;
    }
}
