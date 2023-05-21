package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.enums.PieceColour;
import com.tomaslevesconte.javachess.enums.PieceType;
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

    public void disablePieceEventHandler(PieceColour pieceColour) {
        for (Node node : board.getAnchorPane().getChildren()) {
            for (Piece piece : board.getPieceList()) {
                if (node.equals(piece)
                        && piece.getPieceColour().equals(pieceColour)) {
                    node.setCursor(Cursor.DEFAULT);
                    node.setOnMousePressed(null);
                    node.setOnMouseDragged(null);
                    node.setOnMouseReleased(null);
                }
            }
        }
    }

    public void enablePieceEventHandler(PieceColour pieceColour) {
        for (Node node : board.getAnchorPane().getChildren()) {
            for (Piece piece : board.getPieceList()) {
                if (node.equals(piece)
                        && piece.getPieceColour().equals(pieceColour)) {
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
                System.out.println("Is piece blocking check: " + board.getGameState().isPieceBlockingCheck(piece));

                uiComponents.removeSelectedPiece();
                if (board.getGameState().isKingInCheck()
                        && piece.getPieceType().equals(PieceType.KING)) {
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
        initialisePiece(new Rook(PieceColour.WHITE, Square.A1, board));
        initialisePiece(new Knight(PieceColour.WHITE, Square.B1, board));
        initialisePiece(new Bishop(PieceColour.WHITE, Square.C1, board));
        initialisePiece(new Queen(PieceColour.WHITE, Square.D1, board));
        initialisePiece(new King(PieceColour.WHITE, Square.E1, board));
        initialisePiece(new Bishop(PieceColour.WHITE, Square.F1, board));
        initialisePiece(new Knight(PieceColour.WHITE, Square.G1, board));
        initialisePiece(new Rook(PieceColour.WHITE, Square.H1, board));
        initialisePiece(new Pawn(PieceColour.WHITE, Square.A2, board));
        initialisePiece(new Pawn(PieceColour.WHITE, Square.B2, board));
        initialisePiece(new Pawn(PieceColour.WHITE, Square.C2, board));
        initialisePiece(new Pawn(PieceColour.WHITE, Square.D2, board));
        initialisePiece(new Pawn(PieceColour.WHITE, Square.E2, board));
        initialisePiece(new Pawn(PieceColour.WHITE, Square.F2, board));
        initialisePiece(new Pawn(PieceColour.WHITE, Square.G2, board));
        initialisePiece(new Pawn(PieceColour.WHITE, Square.H2, board));
    }

    private void initialiseBlackPieces() {
        initialisePiece(new Rook(PieceColour.BLACK, Square.A8, board));
        initialisePiece(new Knight(PieceColour.BLACK, Square.B8, board));
        initialisePiece(new Bishop(PieceColour.BLACK, Square.C8, board));
        initialisePiece(new Queen(PieceColour.BLACK, Square.D8, board));
        initialisePiece(new King(PieceColour.BLACK, Square.E8, board));
        initialisePiece(new Bishop(PieceColour.BLACK, Square.F8, board));
        initialisePiece(new Knight(PieceColour.BLACK, Square.G8, board));
        initialisePiece(new Rook(PieceColour.BLACK, Square.H8, board));
        initialisePiece(new Pawn(PieceColour.BLACK, Square.A7, board));
        initialisePiece(new Pawn(PieceColour.BLACK, Square.B7, board));
        initialisePiece(new Pawn(PieceColour.BLACK, Square.C7, board));
        initialisePiece(new Pawn(PieceColour.BLACK, Square.D7, board));
        initialisePiece(new Pawn(PieceColour.BLACK, Square.E7, board));
        initialisePiece(new Pawn(PieceColour.BLACK, Square.F7, board));
        initialisePiece(new Pawn(PieceColour.BLACK, Square.G7, board));
        initialisePiece(new Pawn(PieceColour.BLACK, Square.H7, board));
    }
}
