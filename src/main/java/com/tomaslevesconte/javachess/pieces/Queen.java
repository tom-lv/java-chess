package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Queen extends Piece {
    public Queen(PieceColour pieceColour, double x, double y, Chessboard chessboard) {
        super(pieceColour, x, y, chessboard);
        pieceType = PieceType.QUEEN;
    }

    @Override
    public Rectangle createPiece() {
        Rectangle queen = new Rectangle(chessboard.getSquareWidth(), chessboard.getSquareHeight());
        queen.setLayoutX(x);
        queen.setLayoutY(y);
        if (pieceColour.equals(PieceColour.WHITE)) {
            queen.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/pieces/wq.png")));
        } else if (pieceColour.equals(PieceColour.BLACK)) {
            queen.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/pieces/bq.png")));
        }
        return queen;
    }
}
