package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.enums.PieceColour;
import com.tomaslevesconte.javachess.enums.Square;
import com.tomaslevesconte.javachess.pieces.*;

import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;

public class PieceBuilder {

    private final Board board;

    private final UIComponents uiComponents;

    public PieceBuilder(Board board) {
        this.board = board;
        this.uiComponents = new UIComponents(board);
        initialiseWhitePieces();
        initialiseBlackPieces();
    }

    private void initialisePiece(Piece piece) {

        piece.setOnMouseEntered(mouseEvent -> {
            piece.setCursor(Cursor.DEFAULT);
            if (piece.getPieceColour().equals(PieceColour.WHITE)
                    && board.getGameState().isWhitesTurn()) {
                piece.setCursor(Cursor.OPEN_HAND);
            } else if (piece.getPieceColour().equals(PieceColour.BLACK)
                    && !board.getGameState().isWhitesTurn()) {
                piece.setCursor(Cursor.OPEN_HAND);
            }
        });

        piece.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)
                    && piece.getPieceColour().equals(PieceColour.WHITE)
                    && board.getGameState().isWhitesTurn()
                    || mouseEvent.getButton().equals(MouseButton.PRIMARY)
                    && piece.getPieceColour().equals(PieceColour.BLACK)
                    && !board.getGameState().isWhitesTurn()) {
                System.out.println("Is piece blocking check: " + board.getGameState().isPieceBlockingCheck(piece));

                uiComponents.removeSelectedPiece();
                uiComponents.displaySelectedPiece(piece);
                uiComponents.removeLegalMoves();
                uiComponents.displayLegalMoves(piece);

                piece.setCursor(Cursor.CLOSED_HAND);
                piece.toFront(); // Move piece in front of its siblings in terms of z-order
                piece.setLayoutX(mouseEvent.getSceneX() - (piece.getWidth() / 2)); // - half the size of the image to find the center
                piece.setLayoutY(mouseEvent.getSceneY() - (piece.getHeight() / 2));

            }
        });

        piece.setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)
                    && piece.getPieceColour().equals(PieceColour.WHITE)
                    && board.getGameState().isWhitesTurn()
                    || mouseEvent.getButton().equals(MouseButton.PRIMARY)
                    && piece.getPieceColour().equals(PieceColour.BLACK)
                    && !board.getGameState().isWhitesTurn()) {

                piece.setLayoutX(mouseEvent.getSceneX() - (piece.getWidth() / 2)); // - half the size of the image to find the center
                piece.setLayoutY(mouseEvent.getSceneY() - (piece.getHeight() / 2));

            }
        });

        piece.setOnMouseReleased(mouseEvent -> {
            if (piece.getPieceColour().equals(PieceColour.WHITE)
                    && board.getGameState().isWhitesTurn()) {
                piece.setCursor(Cursor.OPEN_HAND);
            } else if (piece.getPieceColour().equals(PieceColour.BLACK)
                    && !board.getGameState().isWhitesTurn()) {
                piece.setCursor(Cursor.OPEN_HAND);
            }

            Square newSquare = Square.find(
                    board.findClosestSquare(mouseEvent.getSceneX()),
                    board.findClosestSquare(mouseEvent.getSceneY()),
                    board.getSquareSize());
            Square lastSquare = Square.find(piece.getPosX(), piece.getPosY(), board.getSquareSize());
            int pieceIndex = board.getPieceIndex(lastSquare);

            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)
                    && newSquare != null && lastSquare != null
                    && piece.getPieceColour().equals(PieceColour.WHITE)
                    && board.getGameState().isWhitesTurn()
                    && board.getPieceList().get(pieceIndex).move(newSquare)
                    || mouseEvent.getButton().equals(MouseButton.PRIMARY)
                    && newSquare != null && lastSquare != null
                    && piece.getPieceColour().equals(PieceColour.BLACK)
                    && !board.getGameState().isWhitesTurn()
                    && board.getPieceList().get(pieceIndex).move(newSquare)) {

                uiComponents.removeSelectedPiece();
                uiComponents.removeLegalMoves();
                uiComponents.removeLastMovePath();
                uiComponents.displayLastMovePath(newSquare, lastSquare);

            } else {
                piece.setLayoutX(piece.getPosX());
                piece.setLayoutY(piece.getPosY());
            }
        });

        board.getPieceList().add(piece);
        board.getAnchorPane().getChildren().add(piece);
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
