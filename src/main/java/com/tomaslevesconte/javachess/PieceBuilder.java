package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.pieces.*;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class PieceBuilder {

    private final Chessboard chessboard;
    private int pI = 0; // pieceIndex

    public PieceBuilder(Chessboard chessboard) {
        this.chessboard = chessboard;
        addWhitePieces();
//        addBlackPieces();
    }

    private void addPiece(Piece piece) {
        int cPI = pI++; // currentPieceIndex
        piece.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                showLegalMoves(piece);
                piece.setCursor(Cursor.CLOSED_HAND);
                piece.toFront(); // Move piece in front of its siblings in terms of z-order
                piece.setLayoutX(mouseEvent.getSceneX() - (piece.getWidth() / 2)); // - half the size of the image to find the center
                piece.setLayoutY(mouseEvent.getSceneY() - (piece.getHeight() / 2));
            }
        });
        piece.setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                piece.setCursor(Cursor.CLOSED_HAND);
                piece.setLayoutX(mouseEvent.getSceneX() - (piece.getWidth() / 2)); // - half the size of the image to find the center
                piece.setLayoutY(mouseEvent.getSceneY() - (piece.getHeight() / 2));
            }
        });
        piece.setOnMouseReleased(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                piece.setCursor(Cursor.OPEN_HAND);
                double newX = chessboard.findClosestSquare(mouseEvent.getSceneX(), chessboard.getPossibleXAndYCoordinates());
                double newY = chessboard.findClosestSquare(mouseEvent.getSceneY(), chessboard.getPossibleXAndYCoordinates());
                if (chessboard.getPiecePositions().get(cPI).move(newX, newY)) {
                    piece.setLayoutX(newX); // Update pos on board
                    piece.setLayoutY(newY); // Update pos on board
                    hideLegalMoves(); // once placed, hide legal moves
                } else {
                    piece.setLayoutX(piece.getCurrentX());
                    piece.setLayoutY(piece.getCurrentY());
                }
            }
        });
        chessboard.getPiecePositions().add(piece);
        chessboard.getAnchorPane().getChildren().add(piece);
    }

    private void addWhitePieces() {
//        addPiece(new Rook(PieceColour.WHITE, Square.A1, chessboard));
//        addPiece(new Knight(PieceColour.WHITE, Square.B1, chessboard));
//        addPiece(new Bishop(PieceColour.WHITE, Square.C1, chessboard));
//        addPiece(new Queen(PieceColour.WHITE, Square.D1, chessboard));
//        addPiece(new King(PieceColour.WHITE, Square.E1, chessboard));
//        addPiece(new Bishop(PieceColour.WHITE, Square.F1, chessboard));
        addPiece(new Knight(PieceColour.WHITE, Square.G1, chessboard));
//        addPiece(new Rook(PieceColour.WHITE, Square.H1, chessboard));
//        addPiece(new Pawn(PieceColour.WHITE, Square.A2, chessboard));
//        addPiece(new Pawn(PieceColour.WHITE, Square.B2, chessboard));
//        addPiece(new Pawn(PieceColour.WHITE, Square.C2, chessboard));
//        addPiece(new Pawn(PieceColour.WHITE, Square.D2, chessboard));
//        addPiece(new Pawn(PieceColour.WHITE, Square.E2, chessboard));
//        addPiece(new Pawn(PieceColour.WHITE, Square.F2, chessboard));
//        addPiece(new Pawn(PieceColour.WHITE, Square.G2, chessboard));
//        addPiece(new Pawn(PieceColour.WHITE, Square.H2, chessboard));
    }

    private void addBlackPieces() {
        addPiece(new Rook(PieceColour.BLACK, Square.A8, chessboard));
        addPiece(new Knight(PieceColour.BLACK, Square.B8, chessboard));
        addPiece(new Bishop(PieceColour.BLACK, Square.C8, chessboard));
        addPiece(new Queen(PieceColour.BLACK, Square.D8, chessboard));
        addPiece(new King(PieceColour.BLACK, Square.E8, chessboard));
        addPiece(new Bishop(PieceColour.BLACK, Square.F8, chessboard));
        addPiece(new Knight(PieceColour.BLACK, Square.G8, chessboard));
        addPiece(new Rook(PieceColour.BLACK, Square.H8, chessboard));
        addPiece(new Pawn(PieceColour.BLACK, Square.A7, chessboard));
        addPiece(new Pawn(PieceColour.BLACK, Square.B7, chessboard));
        addPiece(new Pawn(PieceColour.BLACK, Square.C7, chessboard));
        addPiece(new Pawn(PieceColour.BLACK, Square.D7, chessboard));
        addPiece(new Pawn(PieceColour.BLACK, Square.E7, chessboard));
        addPiece(new Pawn(PieceColour.BLACK, Square.F7, chessboard));
        addPiece(new Pawn(PieceColour.BLACK, Square.G7, chessboard));
        addPiece(new Pawn(PieceColour.BLACK, Square.H7, chessboard));
    }

    private void showLegalMoves(Piece piece) {
        hideLegalMoves(); // hide last selected piece's moves
        piece.toFront();
        ArrayList<Square> legalMoves = piece.getLegalMoves();
        Rectangle currentSquare = new Rectangle(chessboard.getSquareSize(), chessboard.getSquareSize());
        currentSquare.setFill(Color.web("#FEF250", 0.6));
        currentSquare.setSmooth(false);
        currentSquare.setLayoutX(piece.getCurrentX());
        currentSquare.setLayoutY(piece.getCurrentY());
        Group possibleMoves = new Group();
        legalMoves.forEach(move -> {
            Rectangle legalMove = new Rectangle(chessboard.getSquareSize(), chessboard.getSquareSize());
            legalMove.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/hc.png")));
            legalMove.setSmooth(false);
            legalMove.setLayoutX(move.getX(chessboard.getSquareSize()));
            legalMove.setLayoutY(move.getY(chessboard.getSquareSize()));
            possibleMoves.getChildren().add(legalMove);
        });
        currentSquare.setId("currentSquare");
        possibleMoves.setId("possibleMoves");
        chessboard.getAnchorPane().getChildren().addAll(possibleMoves, currentSquare);
    }

    public void hideLegalMoves() {
        chessboard.getAnchorPane().getChildren().remove(chessboard.getAnchorPane().lookup("#currentSquare"));
        chessboard.getAnchorPane().getChildren().remove(chessboard.getAnchorPane().lookup("#possibleMoves"));
    }
}
