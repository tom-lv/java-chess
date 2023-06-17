package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.enums.Event;
import com.tomaslevesconte.javachess.enums.Colour;
import com.tomaslevesconte.javachess.enums.Type;
import com.tomaslevesconte.javachess.enums.Square;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Piece extends Rectangle {

    private static final String IMAGE_PATH = "com/tomaslevesconte/javachess/pieces/";
    private static final String FILE_EXTENSION = ".png";

    private final Type type;
    private final Colour colour;
    private Square position;
    private Square previousPosition;
    private final int MAX_SQUARE_ADVANCE;
    private final Board board;
    private boolean hasMoved;

    public Piece(Type type, Colour colour, Square position, int MAX_SQUARE_ADVANCE, Board board) {
        this.type = type;
        this.colour = colour;
        this.position = position;

        this.MAX_SQUARE_ADVANCE = MAX_SQUARE_ADVANCE;

        this.board = board;
        this.hasMoved = false;
    }

    public void createPiece() {
        char colourInitial = getColour().toString().toLowerCase().charAt(0);
        char typeInitial = getType().equals(Type.KNIGHT)
                ? getType().toString().toLowerCase().charAt(1)
                : getType().toString().toLowerCase().charAt(0);
        setFill(new ImagePattern(new Image(IMAGE_PATH + colourInitial + typeInitial + FILE_EXTENSION)));
        setSmooth(false);

        setWidth(getBoard().getSquareSize());
        setHeight(getBoard().getSquareSize());

        setLayoutX(getPosX());
        setLayoutY(getPosY());
    }

    public boolean move(Square newSquare) {
        setPreviousPosition(getPosition());

        for (Square legalMove : getBoard().getGame().getPossibleMoves(this)) {
            if (newSquare == legalMove) {
                Event event = Event.MOVE;
                if (getBoard().isSquareOccupied(newSquare)
                        && getBoard().getPiece(newSquare).getColour() != getColour()) {
                    event = Event.CAPTURE;
                    Piece target = getBoard().getPiece(newSquare);
                    target.capture(target);
                }
                setPosition(newSquare);
                setHasMoved(true);
                getBoard().getGame().update(event);
                return true;
            }
        }

        return false;
    }

    public abstract ArrayList<Square> getLegalMoves();
    public abstract ArrayList<Square> getLegalMoves(boolean applyKingFilter);

    public ArrayList<Square> getVerticalAttackPattern(boolean applyKingFilter) {
        ArrayList<Square> attackPattern = new ArrayList<>();

        ArrayList<Square> upPattern = new ArrayList<>(); // Up y
        ArrayList<Square> downPattern = new ArrayList<>(); // Down y
        for (int i = 1; i <= MAX_SQUARE_ADVANCE; i++) {
            double sqrSize = getBoard().getSquareSize();
            upPattern.add(Square.find(getPosX(), getPosY() + (sqrSize * i), sqrSize));
            downPattern.add(Square.find(getPosX(), getPosY() + (-sqrSize * i), sqrSize));
        }

        upPattern.removeIf(Objects::isNull);
        downPattern.removeIf(Objects::isNull);

        if (applyKingFilter) {
            attackPattern.addAll(kingFilter(upPattern));
            attackPattern.addAll(kingFilter(downPattern));
        } else {
            attackPattern.addAll(regularFilter(upPattern));
            attackPattern.addAll(regularFilter(downPattern));
        }

        return attackPattern;
    }

    public ArrayList<Square> getHorizontalAttackPattern(boolean applyKingFilter) {
        ArrayList<Square> attackPattern = new ArrayList<>();

        ArrayList<Square> upPattern = new ArrayList<>(); // Up x
        ArrayList<Square> downPattern = new ArrayList<>(); // Down x
        for (int i = 1; i <= MAX_SQUARE_ADVANCE; i++) {
            double sqrSize = getBoard().getSquareSize();
            upPattern.add(Square.find(getPosX() + (sqrSize * i), getPosY(), sqrSize));
            downPattern.add(Square.find(getPosX() + (-sqrSize * i), getPosY(), sqrSize));
        }

        upPattern.removeIf(Objects::isNull);
        downPattern.removeIf(Objects::isNull);

        if (applyKingFilter) {
            attackPattern.addAll(kingFilter(upPattern));
            attackPattern.addAll(kingFilter(downPattern));
        } else {
            attackPattern.addAll(regularFilter(upPattern));
            attackPattern.addAll(regularFilter(downPattern));
        }

        return attackPattern;
    }

    public ArrayList<Square> getDiagonalAttackPattern(boolean applyKingFilter) {
        ArrayList<Square> attackPattern = new ArrayList<>();

        ArrayList<Square> patternOne = new ArrayList<>(); // Down x, down y
        ArrayList<Square> patternTwo = new ArrayList<>(); // Up x, down y
        ArrayList<Square> patternThree = new ArrayList<>(); // Down x, up y
        ArrayList<Square> patternFour = new ArrayList<>(); // Up x, up y

        for (int i = 1; i <= MAX_SQUARE_ADVANCE; i++) {
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

        if (applyKingFilter) {
            attackPattern.addAll(kingFilter(patternOne));
            attackPattern.addAll(kingFilter(patternTwo));
            attackPattern.addAll(kingFilter(patternThree));
            attackPattern.addAll(kingFilter(patternFour));
        } else {
            attackPattern.addAll(regularFilter(patternOne));
            attackPattern.addAll(regularFilter(patternTwo));
            attackPattern.addAll(regularFilter(patternThree));
            attackPattern.addAll(regularFilter(patternFour));
        }

        return attackPattern;
    }

    public void capture(Piece piece) {
        // King cannot be captured
        if (!getType().equals(Type.KING)) {
            getBoard().getAnchorPane().getChildren().remove(piece);
            getBoard().getPieceList().remove(this);
        }
    }

//    public void updatePositionOnBoardAndList(Square newSquare) {
//        if (newSquare != null) {
//            // Update visual pos on board
//            setLayoutX(newSquare.getX(getBoard().getSquareSize()));
//            setLayoutY(newSquare.getY(getBoard().getSquareSize()));
//            // Update pos in list
//            setPosition(newSquare);
//        }
//    }

    private ArrayList<Square> regularFilter(ArrayList<Square> sList) {
        for (int i = 0; i < sList.size(); i++) {
            Piece cPiece = getBoard().getPiece(sList.get(i));
            if (getBoard().isSquareOccupied(sList.get(i))
                    && cPiece.getColour() != getColour()) {
                sList.removeAll(sList.subList(i + 1, sList.size()));
            } else if (getBoard().isSquareOccupied(sList.get(i))) {
                sList.removeAll(sList.subList(i, sList.size()));
            }
        }
        return sList;
    }

    private ArrayList<Square> kingFilter(ArrayList<Square> sList) {
        for (int i = 0; i < sList.size(); i++) {
            Piece cPiece = getBoard().getPiece(sList.get(i));
            if (getBoard().isSquareOccupied(sList.get(i))
                    && cPiece.getColour() != getColour()
                    && cPiece.getType().equals(Type.KING)) {
                // Remove nothing
            } else if (getBoard().isSquareOccupied(sList.get(i))) {
                sList.removeAll(sList.subList(i + 1, sList.size()));
            }
        }
        return sList;
    }

    public Type getType() {
        return type;
    }

    public Colour getColour() {
        return colour;
    }

    public Square getPosition() {
        return position;
    }

    public void setPosition(Square position) {
        setLayoutX(position.getX(getBoard().getSquareSize()));
        setLayoutY(position.getY(getBoard().getSquareSize()));

        this.position = position;
    }

    public Square getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(Square previousPosition) {
        this.previousPosition = previousPosition;
    }

    public double getPosX() {
        return position.getX(board.getSquareSize());
    }

    public double getPosY() {
        return position.getY(board.getSquareSize());
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
