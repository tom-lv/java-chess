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

public abstract class Piece extends Rectangle {

    private static final String IMAGE_PATH = "com/tomaslevesconte/javachess/pieces/";
    private static final String IMAGE_TYPE = ".png";
    
    private PieceType pieceType;
    private final PieceColour pieceColour;
    private double currentX;
    private double currentY;
    private final Chessboard chessboard;

    public Piece(PieceColour pieceColour, Square square, Chessboard chessboard) {
        this.pieceColour = pieceColour;
        this.currentX = square.getX(chessboard.getSquareSize());
        this.currentY = square.getY(chessboard.getSquareSize());
        this.chessboard = chessboard;
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
                setCurrentX(newX);
                setCurrentY(newY);
                return true;
            }
        }
        return false;
    }

    public void setCaptured() {
        // King cannot be captured
        if (!getPieceType().equals(PieceType.KING)) {
            setCurrentX(-1); // No longer on the board
            setCurrentY(-1); // No longer on the board
        }
    }

    public abstract ArrayList<Square> getLegalMoves();

    public ArrayList<Square> evaluateVerticalSquares(int numOfSquares) {
        ArrayList<Square> verticalSquares = new ArrayList<>();
        double squareSize = getChessboard().getSquareSize();

        // Evaluate up squares
        double nextY = getChessboard().findNextVerticalSquare(true, getCurrentY());
        for (int i = 0; i < numOfSquares; i++) {
            if (getCurrentY() == 0 || nextY < 0) {
                break;
            } else if (getChessboard().isSquareOccupied(getCurrentX(), nextY)
                    && !getPieceColour().equals(getChessboard().findPieceColour(getCurrentX(), nextY))) {
                verticalSquares.add(Square.findSquare(getCurrentX(), nextY, squareSize));
                break;
            } else if (!getChessboard().isSquareOccupied(getCurrentX(), nextY)) {
                verticalSquares.add(Square.findSquare(getCurrentX(), nextY, squareSize));
            } else {
                break;
            }
            nextY -= squareSize;
        }

        // Evaluate down squares
        nextY = getChessboard().findNextVerticalSquare(false, getCurrentY());
        for (int i = 0; i < numOfSquares; i++) {
            if (getCurrentY() == (squareSize * 7) || nextY > (squareSize * 7)) {
                break;
            } else if (getChessboard().isSquareOccupied(getCurrentX(), nextY)
                    && !getPieceColour().equals(getChessboard().findPieceColour(getCurrentX(), nextY))) {
                verticalSquares.add(Square.findSquare(getCurrentX(), nextY, squareSize));
                break;
            } else if (!getChessboard().isSquareOccupied(getCurrentX(), nextY)) {
                verticalSquares.add(Square.findSquare(getCurrentX(), nextY, squareSize));
            } else {
                break;
            }
            nextY += squareSize;
        }

        return verticalSquares;
    }

    public ArrayList<Square> evaluateHorizontalSquares(int numOfSquares) {
        ArrayList<Square> horizontalSquares = new ArrayList<>();
        double squareSize = getChessboard().getSquareSize();

        // Evaluate left squares
        double nextX = getChessboard().findNextHorizontalSquare(true, getCurrentX());
        for (int i = 0; i < numOfSquares; i++) {
            if (getCurrentX() == 0 || nextX < 0) {
                break;
            } else if (getChessboard().isSquareOccupied(nextX, getCurrentY())
                    && !getPieceColour().equals(getChessboard().findPieceColour(nextX, getCurrentY()))) {
                horizontalSquares.add(Square.findSquare(nextX, getCurrentY(), squareSize));
                break;
            }  else if (!getChessboard().isSquareOccupied(nextX, getCurrentY())) {
                horizontalSquares.add(Square.findSquare(nextX, getCurrentY(), squareSize));
            } else {
                break;
            }
            nextX -= squareSize;
        }

        // Evaluate right squares
        nextX = getChessboard().findNextHorizontalSquare(false, getCurrentX());
        for (int i = 0; i < numOfSquares; i++) {
            if (getCurrentX() == (squareSize * 7) || nextX > (squareSize * 7)) {
                break;
            } else if (getChessboard().isSquareOccupied(nextX, getCurrentY())
                    && !getPieceColour().equals(getChessboard().findPieceColour(nextX, getCurrentY()))) {
                horizontalSquares.add(Square.findSquare(nextX, getCurrentY(), squareSize));
                break;
            } else if (!getChessboard().isSquareOccupied(nextX, getCurrentY())) {
                horizontalSquares.add(Square.findSquare(nextX, getCurrentY(), squareSize));
            } else {
                break;
            }
            nextX += squareSize;
        }

        return horizontalSquares;
    }

    public ArrayList<Square> evaluateDiagonalSquares(int numOfSquares) {
        ArrayList<Square> diagonalSquares = new ArrayList<>();
        double squareSize = getChessboard().getSquareSize();

        // Evaluate diagonal up/left squares
        double[] nextDiagonal = getChessboard().findNextDiagonal(true, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < numOfSquares; i++) {
            if (getCurrentX() == 0
                    || getCurrentY() == 0
                    || nextDiagonal[0] < 0
                    || nextDiagonal[1] < 0) {
                break;
            } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])
                    && !getPieceColour().equals(getChessboard().findPieceColour(nextDiagonal[0], nextDiagonal[1]))) {
                diagonalSquares.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
                break;
            } else if (!getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                diagonalSquares.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else {
                break;
            }
            nextDiagonal[0] -= squareSize;
            nextDiagonal[1] -= squareSize;
        }

        // Evaluate diagonal up/right squares
        nextDiagonal = getChessboard().findNextDiagonal(true, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < numOfSquares; i++) {
            if (getCurrentX() == (squareSize * 7)
                    || getCurrentY() == 0
                    || nextDiagonal[0] > (squareSize * 7)
                    || nextDiagonal[1] < 0) {
                break;
            } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])
                    && !getPieceColour().equals(getChessboard().findPieceColour(nextDiagonal[0], nextDiagonal[1]))) {
                diagonalSquares.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
                break;
            } else if (!getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                diagonalSquares.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else {
                break;
            }
            nextDiagonal[0] += squareSize;
            nextDiagonal[1] -= squareSize;
        }

        // Evaluate diagonal down/left squares
        nextDiagonal = getChessboard().findNextDiagonal(false, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < numOfSquares; i++) {
            if (getCurrentY() == (squareSize * 7)
                    || getCurrentX() == 0
                    || nextDiagonal[0] < 0
                    || nextDiagonal[1] > (squareSize * 7)) {
                break;
            } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])
                    && !getPieceColour().equals(getChessboard().findPieceColour(nextDiagonal[0], nextDiagonal[1]))) {
                diagonalSquares.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
                break;
            } else if (!getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                diagonalSquares.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                break;
            }
            nextDiagonal[0] -= squareSize;
            nextDiagonal[1] += squareSize;
        }

        // Evaluate diagonal down/right squares
        nextDiagonal = getChessboard().findNextDiagonal(false, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < numOfSquares; i++) {
            if (getCurrentY() == (squareSize * 7)
                    || getCurrentX() == (squareSize * 7)
                    || nextDiagonal[0] > (squareSize * 7)
                    || nextDiagonal[1] > (squareSize * 7)) {
                break;
            } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])
                    && !getPieceColour().equals(getChessboard().findPieceColour(nextDiagonal[0], nextDiagonal[1]))) {
                diagonalSquares.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
                break;
            } else if (!getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                diagonalSquares.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                break;
            }
            nextDiagonal[0] += squareSize;
            nextDiagonal[1] += squareSize;
        }

        return diagonalSquares;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
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

    public Chessboard getChessboard() {
        return chessboard;
    }
}
