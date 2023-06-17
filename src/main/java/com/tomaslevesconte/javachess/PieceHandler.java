package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.enums.Colour;
import com.tomaslevesconte.javachess.enums.Type;
import com.tomaslevesconte.javachess.enums.Square;
import com.tomaslevesconte.javachess.pieces.*;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class PieceHandler {

    private final Board board;
    private final UIComponents uiComponents;

    public PieceHandler(Board board) {
        this.board = board;
        this.uiComponents = new UIComponents(board);
        initialiseWhitePieces();
        initialiseBlackPieces();
    }

    public void disablePieceEventHandler(Colour colour) {
        for (Node node : board.getAnchorPane().getChildren()) {
            for (Piece piece : board.getPieceList()) {
                if (node.equals(piece)
                        && piece.getColour().equals(colour)) {
                    node.setCursor(Cursor.DEFAULT);
                    node.setOnMousePressed(null);
                    node.setOnMouseDragged(null);
                    node.setOnMouseReleased(null);
                }
            }
        }
    }

    public void enablePieceEventHandler(Colour colour) {
        for (Node node : board.getAnchorPane().getChildren()) {
            for (Piece piece : board.getPieceList()) {
                if (node.equals(piece)
                        && piece.getColour().equals(colour)) {
                    node.setCursor(Cursor.OPEN_HAND);
                    node.setOnMousePressed(select(piece));
                    node.setOnMouseDragged(drag(piece));
                    node.setOnMouseReleased(release(piece));
                }
            }
        }
    }

    private void initialisePiece(Piece piece) {
        board.getPieceList().add(piece);
        board.getAnchorPane().getChildren().add(piece);
    }

    private EventHandler<MouseEvent> select(Piece piece) {
        return mEvent -> {
            if (mEvent.getButton().equals(MouseButton.PRIMARY)) {
                System.out.println("Is piece blocking check: " + board.getGame().isPieceBlockingCheck(piece));

                uiComponents.removeSelectedPiece();
                if (board.getGame().isKingInCheck()
                        && piece.getType().equals(Type.KING)) {
                    // Do nothing
                } else {
                    uiComponents.displaySelectedPiece(piece);
                }
                uiComponents.removeLegalMoves();
                uiComponents.displayLegalMoves(piece);

                piece.setCursor(Cursor.CLOSED_HAND);
                piece.toFront(); // Move piece in front of its siblings in terms of z-order
                piece.setLayoutX(mEvent.getSceneX() - (piece.getWidth() / 2)); // - half the size of the image to find the center
                piece.setLayoutY(mEvent.getSceneY() - (piece.getHeight() / 2));
            }
        };
    }

    private EventHandler<MouseEvent> drag(Piece piece) {
        return mEvent -> {
            if (mEvent.getButton().equals(MouseButton.PRIMARY)) {
                piece.setLayoutX(mEvent.getSceneX() - (piece.getWidth() / 2)); // - half the size of the image to find the center
                piece.setLayoutY(mEvent.getSceneY() - (piece.getHeight() / 2));
            }
        };
    }

    private EventHandler<MouseEvent> release(Piece piece) {
        return mEvent -> {
            piece.setCursor(Cursor.OPEN_HAND);
            Square nSquare = Square.find(
                    board.findClosestSquare(mEvent.getSceneX()),
                    board.findClosestSquare(mEvent.getSceneY()),
                    board.getSquareSize());
            Square lSquare = Square.find(piece.getPosX(), piece.getPosY(), board.getSquareSize());

            int pIndex = board.getPieceIndex(lSquare);

            if (mEvent.getButton().equals(MouseButton.PRIMARY)
                    && nSquare != null && lSquare != null
                    && board.getPieceList().get(pIndex).move(nSquare)) {
                uiComponents.removeSelectedPiece();
                uiComponents.removeLegalMoves();
                uiComponents.removeLastMovePath();
                uiComponents.displayLastMovePath(nSquare, lSquare);
            } else {
                piece.setLayoutX(piece.getPosX());
                piece.setLayoutY(piece.getPosY());
            }
        };
    }

    private void initialiseWhitePieces() {
        initialisePiece(new Rook(Colour.WHITE, Square.A1, board));
        initialisePiece(new Knight(Colour.WHITE, Square.B1, board));
        initialisePiece(new Bishop(Colour.WHITE, Square.C1, board));
        initialisePiece(new Queen(Colour.WHITE, Square.D1, board));
        initialisePiece(new King(Colour.WHITE, Square.E1, board));
        initialisePiece(new Bishop(Colour.WHITE, Square.F1, board));
        initialisePiece(new Knight(Colour.WHITE, Square.G1, board));
        initialisePiece(new Rook(Colour.WHITE, Square.H1, board));
        initialisePiece(new Pawn(Colour.WHITE, Square.A2, board));
        initialisePiece(new Pawn(Colour.WHITE, Square.B2, board));
        initialisePiece(new Pawn(Colour.WHITE, Square.C2, board));
        initialisePiece(new Pawn(Colour.WHITE, Square.D2, board));
        initialisePiece(new Pawn(Colour.WHITE, Square.E2, board));
        initialisePiece(new Pawn(Colour.WHITE, Square.F2, board));
        initialisePiece(new Pawn(Colour.WHITE, Square.G2, board));
        initialisePiece(new Pawn(Colour.WHITE, Square.H2, board));
    }

    private void initialiseBlackPieces() {
        initialisePiece(new Rook(Colour.BLACK, Square.A8, board));
        initialisePiece(new Knight(Colour.BLACK, Square.B8, board));
        initialisePiece(new Bishop(Colour.BLACK, Square.C8, board));
        initialisePiece(new Queen(Colour.BLACK, Square.D8, board));
        initialisePiece(new King(Colour.BLACK, Square.E8, board));
        initialisePiece(new Bishop(Colour.BLACK, Square.F8, board));
        initialisePiece(new Knight(Colour.BLACK, Square.G8, board));
        initialisePiece(new Rook(Colour.BLACK, Square.H8, board));
        initialisePiece(new Pawn(Colour.BLACK, Square.A7, board));
        initialisePiece(new Pawn(Colour.BLACK, Square.B7, board));
        initialisePiece(new Pawn(Colour.BLACK, Square.C7, board));
        initialisePiece(new Pawn(Colour.BLACK, Square.D7, board));
        initialisePiece(new Pawn(Colour.BLACK, Square.E7, board));
        initialisePiece(new Pawn(Colour.BLACK, Square.F7, board));
        initialisePiece(new Pawn(Colour.BLACK, Square.G7, board));
        initialisePiece(new Pawn(Colour.BLACK, Square.H7, board));
    }
}
