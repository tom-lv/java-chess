package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.enums.PieceColour;
import com.tomaslevesconte.javachess.enums.PieceType;

import com.tomaslevesconte.javachess.enums.Square;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Piece extends Rectangle {

    private static final String IMAGE_PATH = "com/tomaslevesconte/javachess/pieces/";
    private static final String IMAGE_TYPE = ".png";
    private static final String CASTLE_SOUND_PATH = "/com/tomaslevesconte/javachess/sounds/castle.mp3";
    private static final String MOVE_SOUND_PATH = "/com/tomaslevesconte/javachess/sounds/move-self.mp3";
    private static final String CAPTURE_SOUND_PATH = "/com/tomaslevesconte/javachess/sounds/capture.mp3";
    private static final String CHECK_SOUND_PATH = "/com/tomaslevesconte/javachess/sounds/move-check.mp3";

    private final PieceType pieceType;
    private final PieceColour pieceColour;
    private double posX;
    private double posY;
    private Square lastPos;
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
        lastPos = Square.find(getPosX(), getPosY(), getBoard().getSquareSize());
        for (Square legalSquare : getLegalMoves()) {
            if (newSquare.equals(legalSquare)) {
                boolean didCapture = false;
                if (getBoard().isSquareOccupied(newSquare)
                        && getBoard().getPiece(newSquare).getPieceColour() != pieceColour) {
                    Piece target = getBoard().getPiece(newSquare);
                    getBoard().getAnchorPane().getChildren().remove(target);
                    target.capture();
                    didCapture = true;
                }
                updatePositionOnBoardAndList(newSquare);
                setHasMoved(true);
                attemptCastle();
                getBoard().getGameState().update(); // Update game state
                if (getBoard().getGameState().isKingInCheck() && didCapture) {
                    checkSound().play();
                } else if (didCapture) {
                    captureSound().play();
                } else {
                    moveSound().play();
                }
                return true;
            }

        }
        return false;
    }

    public abstract ArrayList<Square> getLegalMoves();
    public abstract ArrayList<Square> getLegalMoves(boolean ignoreCoveredSquares);

    public ArrayList<Square> getVerticalAttackPattern(boolean ignoreCoveredSquares) {
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

        if (ignoreCoveredSquares) {
            attackPattern.addAll(removeBlockedSquares(upPattern));
            attackPattern.addAll(removeBlockedSquares(downPattern));
        } else {
            attackPattern.addAll(addCoveredSquares(upPattern));
            attackPattern.addAll(addCoveredSquares(downPattern));
        }

        return attackPattern;
    }

    public ArrayList<Square> getHorizontalAttackPattern(boolean ignoreCoveredSquares) {
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

        if (ignoreCoveredSquares) {
            attackPattern.addAll(removeBlockedSquares(upPattern));
            attackPattern.addAll(removeBlockedSquares(downPattern));
        } else {
            attackPattern.addAll(addCoveredSquares(upPattern));
            attackPattern.addAll(addCoveredSquares(downPattern));
        }

        return attackPattern;
    }

    public ArrayList<Square> getDiagonalAttackPattern(boolean ignoreCoveredSquares) {
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

        if (ignoreCoveredSquares) {
            attackPattern.addAll(removeBlockedSquares(patternOne));
            attackPattern.addAll(removeBlockedSquares(patternTwo));
            attackPattern.addAll(removeBlockedSquares(patternThree));
            attackPattern.addAll(removeBlockedSquares(patternFour));
        } else {
            attackPattern.addAll(addCoveredSquares(patternOne));
            attackPattern.addAll(addCoveredSquares(patternTwo));
            attackPattern.addAll(addCoveredSquares(patternThree));
            attackPattern.addAll(addCoveredSquares(patternFour));
        }

        return attackPattern;
    }

    private void capture() {
        // King cannot be captured
        if (!getPieceType().equals(PieceType.KING)) {
            getBoard().getPieceList().remove(this);
        }
    }

    private void attemptCastle() {
        if (getPieceType().equals(PieceType.KING)) {
            Square kSquare = getPieceColour().equals(PieceColour.WHITE)
                    ? Square.E1
                    : Square.E8;
            Square[] kPos = getPieceColour().equals(PieceColour.WHITE)
                    ? new Square[]{Square.C1, Square.G1}
                    : new Square[]{Square.C8, Square.G8};
            Square[] rPos = getPieceColour().equals(PieceColour.WHITE)
                    ? new Square[]{Square.D1, Square.F1}
                    : new Square[]{Square.D8, Square.F8};

            if (Objects.equals(lastPos, kSquare)
                    && getSquare().equals(kPos[0])) {
                castleSound().play();
                Piece qSR = board.getQueenSideRook(getPieceColour());
                updatePositionOnBoardAndList(qSR, rPos[0]);

            } else if (Objects.equals(lastPos, kSquare)
                    && getSquare().equals(kPos[1])) {
                castleSound().play();
                Piece kSR = board.getKingSideRook(getPieceColour());
                updatePositionOnBoardAndList(kSR, rPos[1]);

            } else {
                moveSound().play();
            }
        }
    }

    private void updatePositionOnBoardAndList(Square newSquare) {
        if (newSquare != null) {
            // Update visual pos on board
            setLayoutX(newSquare.getX(getBoard().getSquareSize()));
            setLayoutY(newSquare.getY(getBoard().getSquareSize()));
            // Update pos in list
            setPosX(newSquare.getX(getBoard().getSquareSize()));
            setPosY(newSquare.getY(getBoard().getSquareSize()));
        }
    }

    private void updatePositionOnBoardAndList(Piece rook, Square newSquare) {
        if (newSquare != null) {
            // Update visual pos on board
            rook.setLayoutX(newSquare.getX(getBoard().getSquareSize()));
            rook.setLayoutY(newSquare.getY(getBoard().getSquareSize()));
            // Update pos in list
            rook.setPosX(newSquare.getX(getBoard().getSquareSize()));
            rook.setPosY(newSquare.getY(getBoard().getSquareSize()));
        }
    }

    private ArrayList<Square> removeBlockedSquares(ArrayList<Square> sList) {
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

    private ArrayList<Square> addCoveredSquares(ArrayList<Square> sList) {
        for (int i = 0; i < sList.size(); i++) {
            if (getBoard().isSquareOccupied(sList.get(i))) {
                sList.removeAll(sList.subList(i + 1, sList.size()));
            }
        }
        return sList;
    }

    private AudioClip moveSound() {
        return new AudioClip(Objects.requireNonNull(getClass().getResource(MOVE_SOUND_PATH)).toString());
    }

    private AudioClip captureSound() {
        return new AudioClip(Objects.requireNonNull(getClass().getResource(CAPTURE_SOUND_PATH)).toString());
    }

    private AudioClip checkSound() {
        return new AudioClip(Objects.requireNonNull(getClass().getResource(CHECK_SOUND_PATH)).toString());
    }

    private AudioClip castleSound() {
        return new AudioClip(Objects.requireNonNull(getClass().getResource(CASTLE_SOUND_PATH)).toString());
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
