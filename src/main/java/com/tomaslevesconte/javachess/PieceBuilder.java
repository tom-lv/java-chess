package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.pieces.*;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Objects;

public class PieceBuilder {

    private final Board board;

    public PieceBuilder(Board board) {
        this.board = board;
        initialiseWhitePieces();
        initialiseBlackPieces();
    }

    private void initialisePiece(Piece piece) {

        piece.setOnMouseEntered(mouseEvent -> {

            piece.setCursor(Cursor.DEFAULT);
            if (piece.getPieceColour().equals(PieceColour.WHITE) && board.getGameState().isWhitesTurn()) {
                piece.setCursor(Cursor.OPEN_HAND);
            } else if (piece.getPieceColour().equals(PieceColour.BLACK) && !board.getGameState().isWhitesTurn()) {
                piece.setCursor(Cursor.OPEN_HAND);
            }

        });

        piece.setOnMousePressed(mouseEvent -> {

            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)
                    && piece.getPieceColour().equals(PieceColour.WHITE) && board.getGameState().isWhitesTurn()
                    || mouseEvent.getButton().equals(MouseButton.PRIMARY)
                    && piece.getPieceColour().equals(PieceColour.BLACK) && !board.getGameState().isWhitesTurn()) {

                showLegalMoves(piece);
                piece.setCursor(Cursor.CLOSED_HAND);
                piece.toFront(); // Move piece in front of its siblings in terms of z-order
                piece.setLayoutX(mouseEvent.getSceneX() - (piece.getWidth() / 2)); // - half the size of the image to find the center
                piece.setLayoutY(mouseEvent.getSceneY() - (piece.getHeight() / 2));

            }

        });

        piece.setOnMouseDragged(mouseEvent -> {

            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)
                    && piece.getPieceColour().equals(PieceColour.WHITE) && board.getGameState().isWhitesTurn()
                    || mouseEvent.getButton().equals(MouseButton.PRIMARY)
                    && piece.getPieceColour().equals(PieceColour.BLACK) && !board.getGameState().isWhitesTurn()) {

                piece.setLayoutX(mouseEvent.getSceneX() - (piece.getWidth() / 2)); // - half the size of the image to find the center
                piece.setLayoutY(mouseEvent.getSceneY() - (piece.getHeight() / 2));
            }

        });

        piece.setOnMouseReleased(mouseEvent -> {


            if (piece.getPieceColour().equals(PieceColour.WHITE) && board.getGameState().isWhitesTurn()) {
                piece.setCursor(Cursor.OPEN_HAND);
            } else if (piece.getPieceColour().equals(PieceColour.BLACK) && !board.getGameState().isWhitesTurn()) {
                piece.setCursor(Cursor.OPEN_HAND);
            }

            Square newSquare = Square.find(
                    board.findClosestSquare(mouseEvent.getSceneX(), board.getPossibleXAndYCoordinates()),
                    board.findClosestSquare(mouseEvent.getSceneY(), board.getPossibleXAndYCoordinates()),
                    board.getSquareSize());

            Square lastSquare = Square.find(piece.getPosX(), piece.getPosY(), board.getSquareSize());

            int pieceIndex = board.getPieceIndex(lastSquare);
            Piece enemyPiece = board.getPiece(newSquare);

            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)
                    && newSquare != null
                    && piece.getPieceColour().equals(PieceColour.WHITE) && board.getGameState().isWhitesTurn()
                    && board.getPieceList().get(pieceIndex).move(newSquare)
                    || mouseEvent.getButton().equals(MouseButton.PRIMARY)
                    && newSquare != null
                    && piece.getPieceColour().equals(PieceColour.BLACK) && !board.getGameState().isWhitesTurn()
                    && board.getPieceList().get(pieceIndex).move(newSquare)) {

                hideLegalMoves();
                attemptCapture(enemyPiece);
                attemptCastle(piece, lastSquare);
                updatePositionOnBoard(piece, newSquare);

            } else {

                piece.setLayoutX(piece.getPosX());
                piece.setLayoutY(piece.getPosY());

            }
        });

        board.getPieceList().add(piece);
        board.getAnchorPane().getChildren().add(piece);
    }

    private void updatePositionOnBoard(Piece piece, Square square) {
        if (piece != null && square != null) {
            piece.setLayoutX(square.getX(board.getSquareSize()));
            piece.setLayoutY(square.getY(board.getSquareSize()));
        }
    }

    private void updatePositionOnBoardAndList(Piece piece, Square square) {
        if (piece != null && square != null) {
            piece.setPosX(square.getX(board.getSquareSize()));
            piece.setPosY(square.getY(board.getSquareSize()));
            piece.setLayoutX(square.getX(board.getSquareSize()));
            piece.setLayoutY(square.getY(board.getSquareSize()));
        }
    }

    private void attemptCapture(Piece capturedPiece) {
        if (capturedPiece != null && !capturedPiece.getPieceType().equals(PieceType.KING)) {
            board.getAnchorPane().getChildren().remove(capturedPiece); // Remove piece from the board
            capturedPiece.setCaptured();
        }
    }

    private void attemptCastle(Piece king, Square square) {
        if (king.getPieceType().equals(PieceType.KING)) {
            Square kSquare = king.getPieceColour().equals(PieceColour.WHITE)
                    ? Square.E1
                    : Square.E8;
            Square[] kPos = king.getPieceColour().equals(PieceColour.WHITE)
                    ? new Square[]{Square.C1, Square.G1}
                    : new Square[]{Square.C8, Square.G8};
            Square[] rPos = king.getPieceColour().equals(PieceColour.WHITE)
                    ? new Square[]{Square.D1, Square.F1}
                    : new Square[]{Square.D8, Square.F8};

            if (Objects.equals(square, kSquare)
                    && king.getSquare().equals(kPos[0])) {
                Piece qSR = board.getQueenSideRook(king.getPieceColour());
                updatePositionOnBoardAndList(qSR, rPos[0]);

            } else if (Objects.equals(square, kSquare)
                    && king.getSquare().equals(kPos[1])) {
                Piece kSR = board.getKingSideRook(king.getPieceColour());
                updatePositionOnBoardAndList(kSR, rPos[1]);
            }
        }
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

    private void showLegalMoves(Piece piece) {
        hideLegalMoves(); // Hide last selected piece's moves
        ArrayList<Square> legalMoves = piece.getLegalMoves(true);
        double squareSize = board.getSquareSize();
        Rectangle currentSquare = new Rectangle(squareSize, squareSize);
        currentSquare.setFill(Color.web("#FEF250", 0.5));
        currentSquare.setSmooth(false);
        currentSquare.setLayoutX(piece.getPosX());
        currentSquare.setLayoutY(piece.getPosY());
        Group possibleMoves = new Group();
        legalMoves.forEach(move -> {
            Rectangle legalMove = new Rectangle(squareSize, squareSize);
            legalMove.setSmooth(false);
            legalMove.setLayoutX(move.getX(board.getSquareSize()));
            legalMove.setLayoutY(move.getY(board.getSquareSize()));
            if (board.isSquareOccupied(Square.find(move.getX(squareSize), move.getY(squareSize), board.getSquareSize()))) {
                legalMove.setFill(Color.web("#9A3048", 1));
            } else {
                legalMove.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/hc.png")));
            }
            possibleMoves.getChildren().add(legalMove);
        });
        currentSquare.setId("currentSquare");
        possibleMoves.setId("possibleMoves");
        board.getAnchorPane().getChildren().addAll(possibleMoves, currentSquare);
        board.getPieceList().forEach(Node::toFront); // All pieces to front in terms of z-index
    }

    public void hideLegalMoves() {
        board.getAnchorPane().getChildren().remove(board.getAnchorPane().lookup("#currentSquare"));
        board.getAnchorPane().getChildren().remove(board.getAnchorPane().lookup("#possibleMoves"));
    }
}
