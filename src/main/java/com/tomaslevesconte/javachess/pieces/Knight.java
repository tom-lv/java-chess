package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Knight extends Piece {

    public Knight(PieceColour pieceColour, double x, double y, Chessboard chessboard) {
        super(pieceColour, x, y, chessboard);
        pieceType = PieceType.KNIGHT;
    }

    @Override
    public Rectangle createPiece() {
        Rectangle knight = new Rectangle(chessboard.getSquareWidth(), chessboard.getSquareHeight());
        knight.setLayoutX(x);
        knight.setLayoutY(y);
        if (pieceColour.equals(PieceColour.WHITE)) {
            knight.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/pieces/wn.png")));
        } else if (pieceColour.equals(PieceColour.BLACK)) {
            knight.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/pieces/bn.png")));
        }
        return knight;
    }
}
