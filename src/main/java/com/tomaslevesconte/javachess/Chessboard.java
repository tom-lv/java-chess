package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.pieces.*;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Chessboard {

    private final static byte TOTAL_NUM_OF_SQUARES = 64;
    private static final Color LIGHT_SQUARE_COLOUR = Color.web("#F2D8B5");
    private static final Color DARK_SQUARE_COLOUR = Color.web("#B78B64");

    private final double squareSize;
    private final AnchorPane anchorPane;
    private final ArrayList<Piece> piecePos = new ArrayList<>();
    private int pieceIndex = 0;


    public Chessboard(double boardSize, AnchorPane anchorPane) {
        this.squareSize = boardSize / Math.sqrt(TOTAL_NUM_OF_SQUARES);
        this.anchorPane = anchorPane;
        createBoard();
        addWhitePieces();
        addBlackPieces();
    }

    public void createBoard() {
        double x = 0;
        double y = 0;
        for (int i = 0; i < Math.sqrt(TOTAL_NUM_OF_SQUARES); i++) {
            for (int j = 0; j < Math.sqrt(TOTAL_NUM_OF_SQUARES); j++) {
                Rectangle rectangle = new Rectangle(x, y, squareSize, squareSize);
                rectangle.setSmooth(false); // Remove antialiasing
                if ((i+j) % 2 == 0) {
                    rectangle.setFill(Chessboard.LIGHT_SQUARE_COLOUR);
                } else {
                    rectangle.setFill(Chessboard.DARK_SQUARE_COLOUR);
                }
                getAnchorPane().getChildren().add(rectangle);
                x += rectangle.getWidth();
            }
            x = 0;
            y += squareSize;
        }
    }

    public double findClosestSquare(double input, double[] possibleCoordinates) {
        double result = 0.0;
        for (int i = 0; i < possibleCoordinates.length; i++) {
            if (input >= possibleCoordinates[i] & input <= possibleCoordinates[i+1] | input < 0) {
                result = possibleCoordinates[i];
                break;
            } else if (input > possibleCoordinates[possibleCoordinates.length - 1]) {
                result = possibleCoordinates[possibleCoordinates.length - 1];
                break;
            }
        }
        return result;
    }
    
    public double[] getPossibleXAndYCoordinates() {
        double[] possibleXAndYCoordinates = new double[(int) (Math.sqrt(Chessboard.TOTAL_NUM_OF_SQUARES))];
        for (int i = 0; i < possibleXAndYCoordinates.length; i++) {
            possibleXAndYCoordinates[i] = squareSize * i;
        }
        return possibleXAndYCoordinates;
    }

    private void addPiece(Piece piece) {
        int currentPieceIndex = pieceIndex++;
        piecePos.add(piece);
        piece.setOnMousePressed(mouseEvent -> {
            if (isPieceSelected()) {
                piecePos.get(getSelectedPiece()).setSelected(false);
                hideLegalMoves();
            }
            piecePos.get(currentPieceIndex).setSelected(true);
            if (piecePos.get(currentPieceIndex).isSelected()) {
                showLegalMoves(piece);
            }
            piece.setCursor(Cursor.CLOSED_HAND);
            piece.toFront(); // Move rec in front of its siblings in terms of z-order
            piece.setLayoutX(mouseEvent.getSceneX() - (piece.getWidth() / 2)); // - half the size of the image to find the center
            piece.setLayoutY(mouseEvent.getSceneY() - (piece.getHeight() / 2));
        });
        piece.setOnMouseDragged(mouseEvent -> {
            piece.setCursor(Cursor.CLOSED_HAND);
            piece.setLayoutX(mouseEvent.getSceneX() - (piece.getWidth() / 2)); // - half the size of the image to find the center
            piece.setLayoutY(mouseEvent.getSceneY() - (piece.getHeight() / 2));
        });
        piece.setOnMouseReleased(mouseEvent -> {
            double newX = findClosestSquare(mouseEvent.getSceneX(), getPossibleXAndYCoordinates());
            double newY = findClosestSquare(mouseEvent.getSceneY(), getPossibleXAndYCoordinates());
            piece.setCursor(Cursor.OPEN_HAND);
            if (piecePos.get(currentPieceIndex).move(newX, newY)) {
                piece.setLayoutX(newX); // Update pos on board
                piece.setLayoutY(newY); // Update pos on board
                hideLegalMoves();
            } else {
                double currentX = piece.getCurrentX();
                double currentY = piece.getCurrentY();
                piece.setLayoutX(currentX);
                piece.setLayoutY(currentY);
            }
        });
        getAnchorPane().getChildren().add(piece);
    }

    private void addWhitePieces() {
        addPiece(new Rook(PieceColour.WHITE, Square.A1, this));
        addPiece(new Knight(PieceColour.WHITE, Square.B1, this));
        addPiece(new Bishop(PieceColour.WHITE, Square.C1, this));
        addPiece(new Queen(PieceColour.WHITE, Square.D1, this));
        addPiece(new King(PieceColour.WHITE, Square.E1, this));
        addPiece(new Bishop(PieceColour.WHITE, Square.F1, this));
        addPiece(new Knight(PieceColour.WHITE, Square.G1, this));
        addPiece(new Rook(PieceColour.WHITE, Square.H1, this));
        addPiece(new Pawn(PieceColour.WHITE, Square.A2, this));
        addPiece(new Pawn(PieceColour.WHITE, Square.B2, this));
        addPiece(new Pawn(PieceColour.WHITE, Square.C2, this));
        addPiece(new Pawn(PieceColour.WHITE, Square.D2, this));
        addPiece(new Pawn(PieceColour.WHITE, Square.E2, this));
        addPiece(new Pawn(PieceColour.WHITE, Square.F2, this));
        addPiece(new Pawn(PieceColour.WHITE, Square.G2, this));
        addPiece(new Pawn(PieceColour.WHITE, Square.H2, this));
    }

    private void addBlackPieces() {
        addPiece(new Rook(PieceColour.BLACK, Square.A8, this));
        addPiece(new Knight(PieceColour.BLACK, Square.B8, this));
        addPiece(new Bishop(PieceColour.BLACK, Square.C8, this));
        addPiece(new Queen(PieceColour.BLACK, Square.D8, this));
        addPiece(new King(PieceColour.BLACK, Square.E8, this));
        addPiece(new Bishop(PieceColour.BLACK, Square.F8, this));
        addPiece(new Knight(PieceColour.BLACK, Square.G8, this));
        addPiece(new Rook(PieceColour.BLACK, Square.H8, this));
        addPiece(new Pawn(PieceColour.BLACK, Square.A7, this));
        addPiece(new Pawn(PieceColour.BLACK, Square.B7, this));
        addPiece(new Pawn(PieceColour.BLACK, Square.C7, this));
        addPiece(new Pawn(PieceColour.BLACK, Square.D7, this));
        addPiece(new Pawn(PieceColour.BLACK, Square.E7, this));
        addPiece(new Pawn(PieceColour.BLACK, Square.F7, this));
        addPiece(new Pawn(PieceColour.BLACK, Square.G7, this));
        addPiece(new Pawn(PieceColour.BLACK, Square.H7, this));
    }

    private void showLegalMoves(Piece piece) {
        ArrayList<Square> legalMoves = piece.getLegalMoves();
        Rectangle currentSquare = new Rectangle(getSquareSize(), getSquareSize());
        currentSquare.setFill(Color.web("#FEF250", 0.6));
        currentSquare.setSmooth(false);
        currentSquare.setLayoutX(piece.getLayoutX());
        currentSquare.setLayoutY(piece.getLayoutY());
        Group possibleMoves = new Group();
        legalMoves.forEach(move -> {
            Rectangle legalMove = new Rectangle(getSquareSize(), getSquareSize());
            legalMove.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/hc.png")));
            legalMove.setSmooth(false);
            legalMove.setLayoutX(move.getX(getSquareSize()));
            legalMove.setLayoutY(move.getY(getSquareSize()));
            possibleMoves.getChildren().add(legalMove);
        });
        currentSquare.setId("currentSquare");
        possibleMoves.setId("possibleMoves");
        getAnchorPane().getChildren().addAll(possibleMoves, currentSquare);
        piece.toFront();
    }

    public void hideLegalMoves() {
        getAnchorPane().getChildren().remove(getAnchorPane().lookup("#currentSquare"));
        getAnchorPane().getChildren().remove(getAnchorPane().lookup("#possibleMoves"));
    }

    private boolean isPieceSelected() {
        boolean bool = false;
        for (Piece piece : getPiecePos()) {
            if (piece.isSelected()) {
                bool = true;
                break;
            }
        }
        return bool;
    }

    private int getSelectedPiece() {
        int pieceIndex = 0;
        for (int i = 0; i < getPiecePos().size(); i++) {
            if (getPiecePos().get(i).isSelected()) {
                pieceIndex = i;
                break;
            }
        }
        return pieceIndex;
    }

    public double getSquareSize() {
        return squareSize;
    }

    public ArrayList<Piece> getPiecePos() {
        return piecePos;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }
}
