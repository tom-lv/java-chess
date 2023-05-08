package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;

import com.tomaslevesconte.javachess.Square;
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
    private double posX;
    private double posY;
    private final int SQUARES_IT_CAN_MOVE;
    private final Board board;
    private boolean hasMoved;

    public Piece(PieceType pieceType, PieceColour pieceColour, Square square, int SQUARES_IT_CAN_MOVE, Board board) {
        this.pieceType = pieceType;
        this.pieceColour = pieceColour;
        this.posX = square.getX(board.getSquareSize());
        this.posY = square.getY(board.getSquareSize());
        this.SQUARES_IT_CAN_MOVE = SQUARES_IT_CAN_MOVE;
        this.board = board;
        this.hasMoved = false;
    }

    public void createPiece() {
        setWidth(getBoard().getSquareSize());
        setHeight(getBoard().getSquareSize());
        setLayoutX(posX);
        setLayoutY(posY);
        setSmooth(false);
        char pieceInitial = getPieceType().equals(PieceType.KNIGHT)
                ? getPieceType().toString().toLowerCase().charAt(1)
                : getPieceType().toString().toLowerCase().charAt(0);
        char colourInitial = getPieceColour().toString().toLowerCase().charAt(0);
        setFill(new ImagePattern(new Image(IMAGE_PATH + colourInitial + pieceInitial + IMAGE_TYPE)));
    }

    public boolean move(Square newSquare) {
        double sqrSize = getBoard().getSquareSize();
        for (Square legalSquare : getLegalMoves(true)) {
            if (newSquare.equals(legalSquare)) {
                setPosX(newSquare.getX(sqrSize));
                setPosY(newSquare.getY(sqrSize));
                setHasMoved(true);
                board.getGameState().update();
                return true;
            }
        }
        return false;
    }

    public void setCaptured() {
        // King cannot be captured
        if (!getPieceType().equals(PieceType.KING)) {
            getBoard().getPieceList().remove(this);
        }
    }

    public abstract ArrayList<Square> getLegalMoves(boolean filterCoveredSquares);

    public ArrayList<Square> getVerticalAttackPattern(boolean filterCoveredSquares) {
        ArrayList<Square> attackPattern = new ArrayList<>();
        ArrayList<Square> upPattern = new ArrayList<>(); // Up y
        ArrayList<Square> downPattern = new ArrayList<>(); // Down y

        for (int i = 1; i <= SQUARES_IT_CAN_MOVE; i++) {
            double sqrSize = getBoard().getSquareSize();
            upPattern.add(Square.find(getPosX(), getPosY() + (sqrSize * i), sqrSize));
            downPattern.add(Square.find(getPosX(), getPosY() + (-sqrSize * i), sqrSize));
        }
        upPattern.removeIf(Objects::isNull);
        downPattern.removeIf(Objects::isNull);

        if (filterCoveredSquares) {
            attackPattern.addAll(filterAll(upPattern));
            attackPattern.addAll(filterAll(downPattern));
        } else {
            attackPattern.addAll(filterBlockedSquares(upPattern));
            attackPattern.addAll(filterBlockedSquares(downPattern));
        }

        return attackPattern;
    }

    public ArrayList<Square> getHorizontalAttackPattern(boolean filterCoveredSquares) {
        ArrayList<Square> attackPattern = new ArrayList<>();
        ArrayList<Square> upPattern = new ArrayList<>(); // Up x
        ArrayList<Square> downPattern = new ArrayList<>(); // Down x

        for (int i = 1; i <= SQUARES_IT_CAN_MOVE; i++) {
            double sqrSize = getBoard().getSquareSize();
            upPattern.add(Square.find(getPosX() + (sqrSize * i), getPosY(), sqrSize));
            downPattern.add(Square.find(getPosX() + (-sqrSize * i), getPosY(), sqrSize));
        }

        upPattern.removeIf(Objects::isNull);
        downPattern.removeIf(Objects::isNull);

        if (filterCoveredSquares) {
            attackPattern.addAll(filterAll(upPattern));
            attackPattern.addAll(filterAll(downPattern));
        } else {
            attackPattern.addAll(filterBlockedSquares(upPattern));
            attackPattern.addAll(filterBlockedSquares(downPattern));
        }

        return attackPattern;
    }

    public ArrayList<Square> getDiagonalAttackPattern(boolean filterCoveredSquares) {
        ArrayList<Square> attackPattern = new ArrayList<>();
        ArrayList<Square> patternOne = new ArrayList<>(); // Down x, down y
        ArrayList<Square> patternTwo = new ArrayList<>(); // Up x, down y
        ArrayList<Square> patternThree = new ArrayList<>(); // Down x, up y
        ArrayList<Square> patternFour = new ArrayList<>(); // Up x, up y

        for (int i = 1; i <= SQUARES_IT_CAN_MOVE; i++) {
            double sqrSize = getBoard().getSquareSize();
            patternOne.add(Square.find(getPosX() + (-sqrSize * i), getPosY() + (-sqrSize * i), sqrSize));
            patternTwo.add(Square.find(getPosX() + (sqrSize * i), getPosY() + (-sqrSize * i), sqrSize));
            patternThree.add(Square.find(getPosX() + (-sqrSize * i), getPosY() + (sqrSize * i), sqrSize));
            patternFour.add(Square.find(getPosX() + (sqrSize * i), getPosY() + (sqrSize * i), sqrSize));
        }

        patternOne.removeIf(Objects::isNull);
        patternTwo.removeIf(Objects::isNull);
        patternThree.removeIf(Objects::isNull);
        patternFour.removeIf(Objects::isNull);

        if (filterCoveredSquares) {
            attackPattern.addAll(filterAll(patternOne));
            attackPattern.addAll(filterAll(patternTwo));
            attackPattern.addAll(filterAll(patternThree));
            attackPattern.addAll(filterAll(patternFour));
        } else {
            attackPattern.addAll(filterBlockedSquares(patternOne));
            attackPattern.addAll(filterBlockedSquares(patternTwo));
            attackPattern.addAll(filterBlockedSquares(patternThree));
            attackPattern.addAll(filterBlockedSquares(patternFour));
        }

        return attackPattern;
    }

    private ArrayList<Square> filterAll(ArrayList<Square> sList) {
        for (int i = 0; i < sList.size(); i++) {
            if (getBoard().isSquareOccupied(sList.get(i))
                    && getBoard().getPiece(sList.get(i)).getPieceColour() != getPieceColour()) {
                sList.removeAll(sList.subList(i + 1, sList.size()));
            } else if (getBoard().isSquareOccupied(sList.get(i))
                    && getBoard().getPiece(sList.get(i)).getPieceColour().equals(getPieceColour())) {
                sList.removeAll(sList.subList(i, sList.size()));
            }
        }
        return sList;
    }

    private ArrayList<Square> filterBlockedSquares(ArrayList<Square> sList) {
        for (int i = 0; i < sList.size(); i++) {
            if (getBoard().isSquareOccupied(sList.get(i))) {
                sList.removeAll(sList.subList(i + 1, sList.size()));
            }
        }
        return sList;
    }

    public Square getSquare() {
        return Square.find(getPosX(), getPosY(), getBoard().getSquareSize());
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public PieceColour getPieceColour() {
        return pieceColour;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public Board getBoard() {
        return board;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}
