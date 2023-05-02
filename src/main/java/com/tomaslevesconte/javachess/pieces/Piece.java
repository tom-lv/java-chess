package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;

import com.tomaslevesconte.javachess.Square;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public abstract class Piece extends Rectangle {

    // Constants
    private static final String IMAGE_PATH = "com/tomaslevesconte/javachess/pieces/";
    private static final String IMAGE_TYPE = ".png";

    // Instance variables
    private final PieceType pieceType;
    private final PieceColour pieceColour;
    private double currentX;
    private double currentY;
    private final int squaresItCanMove;
    private final Chessboard chessboard;
    private boolean hasMoved;
    private boolean isSelected;

    public Piece(PieceType pieceType, PieceColour pieceColour, Square square, int squaresItCanMove, Chessboard chessboard) {
        this.pieceType = pieceType;
        this.pieceColour = pieceColour;
        this.currentX = square.getX(chessboard.getSquareSize());
        this.currentY = square.getY(chessboard.getSquareSize());
        this.squaresItCanMove = squaresItCanMove;
        this.chessboard = chessboard;
        this.hasMoved = false;
        this.isSelected = false;
    }

    public void createPiece() {
        setCursor(Cursor.OPEN_HAND);
        setWidth(getChessboard().getSquareSize());
        setHeight(getChessboard().getSquareSize());
        setLayoutX(currentX);
        setLayoutY(currentY);
        setSmooth(false);
        char pieceInitial = getPieceType().equals(PieceType.KNIGHT)
                ? getPieceType().toString().toLowerCase().charAt(1)
                : getPieceType().toString().toLowerCase().charAt(0);
        char colourInitial = getPieceColour().toString().toLowerCase().charAt(0);
        setFill(new ImagePattern(new Image(IMAGE_PATH + colourInitial + pieceInitial + IMAGE_TYPE)));
    }

    public boolean move(double newX, double newY) {
        double squareSize = getChessboard().getSquareSize();
        for (Square legalMove : getLegalMoves()) {
            double lmX = Math.round(legalMove.getX(squareSize));
            double lmY = Math.round(legalMove.getY(squareSize));
            if (Math.round(newX) == lmX && Math.round(newY) == lmY) {
                setHasMoved(true);
                setCurrentX(newX);
                setCurrentY(newY);
                return true;
            }
        }
        return false;
    }

    // Change
    public void setCaptured() {
        // King cannot be captured
        if (!getPieceType().equals(PieceType.KING)) {
            setCurrentX(-1); // No longer on the board
            setCurrentY(-1); // No longer on the board
        }
    }

    public abstract ArrayList<Square> getLegalMoves();

    private ArrayList<Square> removeBlockedSquares(ArrayList<Square> sList) {
        for (int i = 0; i < sList.size(); i++) {
            if (getChessboard().isSquareOccupied(sList.get(i))
                    && getChessboard().getPiece(sList.get(i)).getPieceColour() != getPieceColour()) {
                sList.removeAll(sList.subList(i + 1, sList.size()));
            } else if (getChessboard().isSquareOccupied(sList.get(i))
                    && getChessboard().getPiece(sList.get(i)).getPieceColour().equals(getPieceColour())) {
                sList.removeAll(sList.subList(i, sList.size()));
            }
        }
        return sList;
    }

    public ArrayList<Square> getVerticalAttackPatterns() {
        ArrayList<Square> attackPatterns = new ArrayList<>();

        double squareSize = getChessboard().getSquareSize();

        // Up attack pattern (in terms of y)
        ArrayList<Square> upAttackPatterns = new ArrayList<>();
        for (int i = 1; i <= getSquaresItCanMove(); i++) {
            upAttackPatterns.add(Square.find(getCurrentX(), (getCurrentY() + (squareSize * i)), squareSize));
        }
        upAttackPatterns.removeIf(Objects::isNull); // Remove if square !exist

        // Down attack pattern (in terms of y)
        ArrayList<Square> downAttackPatterns = new ArrayList<>();
        for (int i = 1; i <= getSquaresItCanMove(); i++) {
            downAttackPatterns.add(Square.find(getCurrentX(), (getCurrentY() + (-squareSize * i)), squareSize));
        }
        downAttackPatterns.removeIf(Objects::isNull); // Remove if square !exist

        attackPatterns.addAll(removeBlockedSquares(upAttackPatterns));
        attackPatterns.addAll(removeBlockedSquares(downAttackPatterns));

        return attackPatterns;
    }

    public ArrayList<Square> getHorizontalAttackPatterns() {
        ArrayList<Square> attackPatterns = new ArrayList<>();

        double squareSize = getChessboard().getSquareSize();

        // Up attack pattern (in terms of x)
        ArrayList<Square> upAttackPatterns = new ArrayList<>();
        for (int i = 1; i <= getSquaresItCanMove(); i++) {
            upAttackPatterns.add(Square.find((getCurrentX() + (squareSize * i)), getCurrentY(), squareSize));
        }
        upAttackPatterns.removeIf(Objects::isNull); // Remove if square !exist

        // Down attack pattern (in terms of x)
        ArrayList<Square> downAttackPatterns = new ArrayList<>();
        for (int i = 1; i <= getSquaresItCanMove(); i++) {
            downAttackPatterns.add(Square.find((getCurrentX() + (-squareSize * i)), getCurrentY(), squareSize));
        }
        downAttackPatterns.removeIf(Objects::isNull); // Remove if square !exist

        attackPatterns.addAll(removeBlockedSquares(upAttackPatterns));
        attackPatterns.addAll(removeBlockedSquares(downAttackPatterns));

        return attackPatterns;
    }

    public ArrayList<Square> evaluateDiagonalSquares() {
        ArrayList<Square> diagonalSquares = new ArrayList<>();
        double squareSize = getChessboard().getSquareSize();

        // Evaluate diagonal up/left squares
        double[] nextDiagonal = getChessboard().findNextDiagonal(true, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < getSquaresItCanMove(); i++) {
            if (getCurrentX() == 0
                    || getCurrentY() == 0
                    || nextDiagonal[0] < 0
                    || nextDiagonal[1] < 0) {
                break;
            } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])
                    && getPieceColour() != getChessboard().getPiece(nextDiagonal[0], nextDiagonal[1]).getPieceColour()) {
                diagonalSquares.add(Square.find(nextDiagonal[0], nextDiagonal[1], squareSize));
                break;
            } else if (!getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                diagonalSquares.add(Square.find(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else {
                break;
            }
            nextDiagonal[0] -= squareSize;
            nextDiagonal[1] -= squareSize;
        }

        // Evaluate diagonal up/right squares
        nextDiagonal = getChessboard().findNextDiagonal(true, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < getSquaresItCanMove(); i++) {
            if (getCurrentX() == (squareSize * 7)
                    || getCurrentY() == 0
                    || nextDiagonal[0] > (squareSize * 7)
                    || nextDiagonal[1] < 0) {
                break;
            } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])
                    && getPieceColour() != getChessboard().getPiece(nextDiagonal[0], nextDiagonal[1]).getPieceColour()) {
                diagonalSquares.add(Square.find(nextDiagonal[0], nextDiagonal[1], squareSize));
                break;
            } else if (!getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                diagonalSquares.add(Square.find(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else {
                break;
            }
            nextDiagonal[0] += squareSize;
            nextDiagonal[1] -= squareSize;
        }

        // Evaluate diagonal down/left squares
        nextDiagonal = getChessboard().findNextDiagonal(false, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < getSquaresItCanMove(); i++) {
            if (getCurrentY() == (squareSize * 7)
                    || getCurrentX() == 0
                    || nextDiagonal[0] < 0
                    || nextDiagonal[1] > (squareSize * 7)) {
                break;
            } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])
                    && getPieceColour() != getChessboard().getPiece(nextDiagonal[0], nextDiagonal[1]).getPieceColour()) {
                diagonalSquares.add(Square.find(nextDiagonal[0], nextDiagonal[1], squareSize));
                break;
            } else if (!getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                diagonalSquares.add(Square.find(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                break;
            }
            nextDiagonal[0] -= squareSize;
            nextDiagonal[1] += squareSize;
        }

        // Evaluate diagonal down/right squares
        nextDiagonal = getChessboard().findNextDiagonal(false, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < getSquaresItCanMove(); i++) {
            if (getCurrentY() == (squareSize * 7)
                    || getCurrentX() == (squareSize * 7)
                    || nextDiagonal[0] > (squareSize * 7)
                    || nextDiagonal[1] > (squareSize * 7)) {
                break;
            } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])
                    && getPieceColour() != getChessboard().getPiece(nextDiagonal[0], nextDiagonal[1]).getPieceColour()) {
                diagonalSquares.add(Square.find(nextDiagonal[0], nextDiagonal[1], squareSize));
                break;
            } else if (!getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                diagonalSquares.add(Square.find(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                break;
            }
            nextDiagonal[0] += squareSize;
            nextDiagonal[1] += squareSize;
        }
        return diagonalSquares;
    }

    public Square getSquare() {
        return Square.find(getCurrentX(), getCurrentY(), getChessboard().getSquareSize());
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public PieceColour getPieceColour() {
        return pieceColour;
    }

    public double getCurrentX() {
        return currentX;
    }

    public void setCurrentX(double currentX) {
        this.currentX = currentX;
    }

    public double getCurrentY() {
        return currentY;
    }

    public void setCurrentY(double currentY) {
        this.currentY = currentY;
    }

    public int getSquaresItCanMove() {
        return squaresItCanMove;
    }

    public Chessboard getChessboard() {
        return chessboard;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
