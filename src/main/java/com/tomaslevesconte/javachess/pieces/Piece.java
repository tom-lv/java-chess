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
import java.util.Objects;

public abstract class Piece extends Rectangle {

    private static final String IMAGE_PATH = "com/tomaslevesconte/javachess/pieces/";
    private static final String IMAGE_TYPE = ".png";

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

    public ArrayList<Square> getVerticalAttackPattern() {
        ArrayList<Square> attackPattern = new ArrayList<>();
        ArrayList<Square> upPattern = new ArrayList<>(); // Up y
        ArrayList<Square> downPattern = new ArrayList<>(); // Down y

        for (int i = 1; i <= getSquaresItCanMove(); i++) {
            double squareSize = getChessboard().getSquareSize();
            upPattern.add(Square.find(getCurrentX(), getCurrentY() + (squareSize * i), squareSize));
            downPattern.add(Square.find(getCurrentX(), getCurrentY() + (-squareSize * i), squareSize));
        }
        upPattern.removeIf(Objects::isNull);
        downPattern.removeIf(Objects::isNull);

        attackPattern.addAll(removeBlockedSquares(upPattern));
        attackPattern.addAll(removeBlockedSquares(downPattern));

        return attackPattern;
    }

    public ArrayList<Square> getHorizontalAttackPattern() {
        ArrayList<Square> attackPattern = new ArrayList<>();
        ArrayList<Square> upPattern = new ArrayList<>(); // Up x
        ArrayList<Square> downPattern = new ArrayList<>(); // Down x

        for (int i = 1; i <= getSquaresItCanMove(); i++) {
            double squareSize = getChessboard().getSquareSize();
            upPattern.add(Square.find(getCurrentX() + (squareSize * i), getCurrentY(), squareSize));
            downPattern.add(Square.find(getCurrentX() + (-squareSize * i), getCurrentY(), squareSize));
        }

        upPattern.removeIf(Objects::isNull);
        downPattern.removeIf(Objects::isNull);

        attackPattern.addAll(removeBlockedSquares(upPattern));
        attackPattern.addAll(removeBlockedSquares(downPattern));

        return attackPattern;
    }

    public ArrayList<Square> getDiagonalAttackPattern() {
        ArrayList<Square> attackPattern = new ArrayList<>();
        ArrayList<Square> patternOne = new ArrayList<>(); // Down x, down y
        ArrayList<Square> patternTwo = new ArrayList<>(); // Up x, down y
        ArrayList<Square> patternThree = new ArrayList<>(); // Down x, up y
        ArrayList<Square> patternFour = new ArrayList<>(); // Up x, up y

        for (int i = 1; i < getSquaresItCanMove(); i++) {
            double squareSize = getChessboard().getSquareSize();
            patternOne.add(Square.find(getCurrentX() + (-squareSize * i), getCurrentY() + (-squareSize * i), squareSize));
            patternTwo.add(Square.find(getCurrentX() + (squareSize * i), getCurrentY() + (-squareSize * i), squareSize));
            patternThree.add(Square.find(getCurrentX() + (-squareSize * i), getCurrentY() + (squareSize * i), squareSize));
            patternFour.add(Square.find(getCurrentX() + (squareSize * i), getCurrentY() + (squareSize * i), squareSize));
        }

        patternOne.removeIf(Objects::isNull);
        patternTwo.removeIf(Objects::isNull);
        patternThree.removeIf(Objects::isNull);
        patternFour.removeIf(Objects::isNull);

        attackPattern.addAll(removeBlockedSquares(patternOne));
        attackPattern.addAll(removeBlockedSquares(patternTwo));
        attackPattern.addAll(removeBlockedSquares(patternThree));
        attackPattern.addAll(removeBlockedSquares(patternFour));

        return attackPattern;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
