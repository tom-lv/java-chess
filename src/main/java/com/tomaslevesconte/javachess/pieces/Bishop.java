package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Bishop extends Piece {
    public Bishop(PieceColour pieceColour, double x, double y, Chessboard chessboard) {
        super(pieceColour, x, y, chessboard);
        pieceType = PieceType.BISHOP;
    }

    @Override
    public Rectangle createPiece() {
        Rectangle bishop = new Rectangle(chessboard.getSquareWidth(), chessboard.getSquareHeight());
        bishop.setLayoutX(x);
        bishop.setLayoutY(y);
        if (pieceColour.equals(PieceColour.WHITE)) {
            bishop.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/pieces/wb.png")));
        } else if (pieceColour.equals(PieceColour.BLACK)) {
            bishop.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/pieces/bb.png")));
        }
        return bishop;
    }
}
