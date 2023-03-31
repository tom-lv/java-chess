package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Rook extends Piece {
    public Rook(PieceColour pieceColour, double x, double y, Chessboard chessboard) {
        super(pieceColour, x, y, chessboard);
        pieceType = PieceType.ROOK;
    }

    @Override
    public Rectangle createPiece() {
        Rectangle rook = new Rectangle(chessboard.getSquareWidth(), chessboard.getSquareHeight());
        rook.setLayoutX(x);
        rook.setLayoutY(y);
        if (pieceColour.equals(PieceColour.WHITE)) {
            rook.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/pieces/wr.png")));
        } else if (pieceColour.equals(PieceColour.BLACK)) {
            rook.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/pieces/br.png")));
        }
        return rook;
    }
}
