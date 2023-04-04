package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.pieces.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChessController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    private Chessboard chessboard;
    private final ArrayList<Piece> pieceList = new ArrayList<>();
    private int pieceIndex = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chessboard = new Chessboard(ChessApplication.BOARD_SIZE, anchorPane);
        addWhitePieces(chessboard.getSquareSize(), chessboard.getSquareSize());
        addBlackPieces(chessboard.getSquareSize(), chessboard.getSquareSize());
    }

    private void addPiece(Piece piece) {
        int currentPieceIndex = pieceIndex++;
        pieceList.add(piece);

        piece.setCursor(Cursor.OPEN_HAND); // default open hand on hover


//        rec.setOnMouseClicked(mouseEvent -> {
//            if (isPieceSelected()) {
//                System.out.println("deselecting piece " + getSelectedPiece());
//                pieceList.get(getSelectedPiece()).hideLegalMoves();
//                pieceList.get(getSelectedPiece()).setSelected(false);
//            }
//            pieceList.get(currentPieceIndex).setSelected(true);
//            System.out.println(getSelectedPiece() + " is now selected.");
//            if (pieceList.get(currentPieceIndex).isSelected()) {
//                pieceList.get(currentPieceIndex).showLegalMoves();
//            }
//        });


        piece.setOnMousePressed(mouseEvent -> {
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
            piece.setCursor(Cursor.OPEN_HAND);
            piece.setLayoutX(chessboard.findClosestSquare(mouseEvent.getSceneX(), chessboard.getPossibleXAndYCoordinates())); // Update pos on board
            piece.setLayoutY(chessboard.findClosestSquare(mouseEvent.getSceneY(), chessboard.getPossibleXAndYCoordinates())); // Update pos on board
            pieceList.get(currentPieceIndex).setPositionX(piece.getLayoutX()); // Update pos in arraylist
            pieceList.get(currentPieceIndex).setPositionY(piece.getLayoutY()); // Update pos in arraylist
            System.out.println(piece.getPieceColour() + " " + piece.getPieceType() + " " + Square.findSquare(piece.getLayoutX(), piece.getLayoutY(), chessboard.getSquareSize()) +
                    " (x=" + piece.getLayoutX() + ", y=" + piece.getLayoutY() + ")");
        });
        anchorPane.getChildren().add(piece);
    }

    private void addWhitePieces(double w, double h) {
        addPiece(new Rook(PieceColour.WHITE, Square.A1.getX(w), Square.A1.getY(h), chessboard));
        addPiece(new Knight(PieceColour.WHITE, Square.B1.getX(w), Square.B1.getY(h), chessboard));
        addPiece(new Bishop(PieceColour.WHITE, Square.C1.getX(w), Square.C1.getY(h), chessboard));
        addPiece(new Queen(PieceColour.WHITE, Square.D1.getX(w), Square.D1.getY(h), chessboard));
        addPiece(new King(PieceColour.WHITE, Square.E1.getX(w), Square.E1.getY(h), chessboard));
        addPiece(new Bishop(PieceColour.WHITE, Square.F1.getX(w), Square.F1.getY(h), chessboard));
        addPiece(new Knight(PieceColour.WHITE, Square.G1.getX(w), Square.G1.getY(h), chessboard));
        addPiece(new Rook(PieceColour.WHITE, Square.H1.getX(w), Square.H1.getY(h), chessboard));
        addPiece(new Pawn(PieceColour.WHITE, Square.A2.getX(w), Square.A2.getY(h), chessboard));
        addPiece(new Pawn(PieceColour.WHITE, Square.B2.getX(w), Square.B2.getY(h), chessboard));
        addPiece(new Pawn(PieceColour.WHITE, Square.C2.getX(w), Square.C2.getY(h), chessboard));
        addPiece(new Pawn(PieceColour.WHITE, Square.D2.getX(w), Square.D2.getY(h), chessboard));
        addPiece(new Pawn(PieceColour.WHITE, Square.E2.getX(w), Square.E2.getY(h), chessboard));
        addPiece(new Pawn(PieceColour.WHITE, Square.F2.getX(w), Square.F2.getY(h), chessboard));
        addPiece(new Pawn(PieceColour.WHITE, Square.G2.getX(w), Square.G2.getY(h), chessboard));
        addPiece(new Pawn(PieceColour.WHITE, Square.H2.getX(w), Square.H2.getY(h), chessboard));
    }

    private void addBlackPieces(double w, double h) {
        addPiece(new Rook(PieceColour.BLACK, Square.A8.getX(w), Square.A8.getY(h), chessboard));
        addPiece(new Knight(PieceColour.BLACK, Square.B8.getX(w), Square.B8.getY(h), chessboard));
        addPiece(new Bishop(PieceColour.BLACK, Square.C8.getX(w), Square.C8.getY(h), chessboard));
        addPiece(new Queen(PieceColour.BLACK, Square.D8.getX(w), Square.D8.getY(h), chessboard));
        addPiece(new King(PieceColour.BLACK, Square.E8.getX(w), Square.E8.getY(h), chessboard));
        addPiece(new Bishop(PieceColour.BLACK, Square.F8.getX(w), Square.F8.getY(h), chessboard));
        addPiece(new Knight(PieceColour.BLACK, Square.G8.getX(w), Square.G8.getY(h), chessboard));
        addPiece(new Rook(PieceColour.BLACK, Square.H8.getX(w), Square.H8.getY(h), chessboard));
        addPiece(new Pawn(PieceColour.BLACK, Square.A7.getX(w), Square.A7.getY(h), chessboard));
        addPiece(new Pawn(PieceColour.BLACK, Square.B7.getX(w), Square.B7.getY(h), chessboard));
        addPiece(new Pawn(PieceColour.BLACK, Square.C7.getX(w), Square.C7.getY(h), chessboard));
        addPiece(new Pawn(PieceColour.BLACK, Square.D7.getX(w), Square.D7.getY(h), chessboard));
        addPiece(new Pawn(PieceColour.BLACK, Square.E7.getX(w), Square.E7.getY(h), chessboard));
        addPiece(new Pawn(PieceColour.BLACK, Square.F7.getX(w), Square.F7.getY(h), chessboard));
        addPiece(new Pawn(PieceColour.BLACK, Square.G7.getX(w), Square.G7.getY(h), chessboard));
        addPiece(new Pawn(PieceColour.BLACK, Square.H7.getX(w), Square.H7.getY(h), chessboard));
    }

    private boolean isPieceSelected() {
        boolean bool = false;
        for (Piece piece : pieceList) {
            if (piece.isSelected()) {
                bool = true;
                break;
            }
        }
        return bool;
    }

    private int getSelectedPiece() {
        int pieceIndex = 0;
        for (int i = 0; i < pieceList.size(); i++) {
            if (pieceList.get(i).isSelected()) {
                pieceIndex = i;
                break;
            }
        }
        return pieceIndex;
    }
}