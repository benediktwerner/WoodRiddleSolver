package de.benedikt_werner.WoodRiddleSolver;

public class Move {
    public int whiteMove = 0, blackMove = 0;
    public int screwIndex = -1, screwMove = 0;

    public Move(int amount, boolean moveBlack) {
        if (moveBlack)
            blackMove = amount;
        else
            whiteMove = amount;
    }

    public Move(int moveScrew, int screw) {
        screwMove = moveScrew;
        screwIndex = screw;
    }

    private String intToVertical(int i) {
        if (i > 0) return "up";
        else if (i < 0) return "down";
        else return "not";
    }

    private String intToHorizontal(int i) {
        if (i > 0) return "right";
        else if (i < 0) return "left";
        else return "not";
    }

    @Override
    public String toString() {
        if (whiteMove != 0)
            return "White " + intToHorizontal(whiteMove);
        else if (blackMove != 0)
            return "Black " + intToHorizontal(blackMove);
        else
            return "Screw " + screwIndex + " " + intToVertical(screwMove);
    }
}
