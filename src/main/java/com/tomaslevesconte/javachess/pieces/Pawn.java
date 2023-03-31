package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Pawn extends Piece {

    private boolean isFirstMove;

    public Pawn(PieceColour pieceColour, double x, double y, Chessboard chessboard) {
        super(pieceColour, x, y, chessboard);
        pieceType = PieceType.PAWN;
    }

    @Override
    public Rectangle createPiece() {
        Rectangle pawn = new Rectangle(chessboard.getSquareWidth(), chessboard.getSquareHeight());
        pawn.setLayoutX(x);
        pawn.setLayoutY(y);
        if (pieceColour.equals(PieceColour.WHITE)) {
            pawn.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/pieces/wp.png")));
        } else if (pieceColour.equals(PieceColour.BLACK)) {
            pawn.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/pieces/bp.png")));
        }
        return pawn;
    }
}
