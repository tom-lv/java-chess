package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class King extends Piece {
    public King(PieceColour pieceColour, double x, double y, Chessboard chessboard) {
        super(pieceColour, x, y, chessboard);
        pieceType = PieceType.KING;
    }

    @Override
    public Rectangle createPiece() {
        Rectangle king = new Rectangle(chessboard.getSquareWidth(), chessboard.getSquareHeight());
        king.setLayoutX(x);
        king.setLayoutY(y);
        if (pieceColour.equals(PieceColour.WHITE)) {
            king.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/pieces/wk.png")));
        } else if (pieceColour.equals(PieceColour.BLACK)) {
            king.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/pieces/bk.png")));
        }
        return king;
    }
}
