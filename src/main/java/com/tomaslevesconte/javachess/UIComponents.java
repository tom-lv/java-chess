package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.enums.Square;
import com.tomaslevesconte.javachess.pieces.Piece;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class UIComponents {
    private final Board board;

    public UIComponents(Board board) {
        this.board = board;
    }

    public void displaySelectedPiece(Piece piece) {
        Rectangle selectedRec = createHighlightRectangle();
        selectedRec.setId("selectedPiece");
        selectedRec.setLayoutX(piece.getLayoutX());
        selectedRec.setLayoutY(piece.getLayoutY());

        board.getAnchorPane().getChildren().addAll(selectedRec);
        board.getPieceList().forEach(Node::toFront); // All pieces to front in terms of z-index
    }

    public void removeSelectedPiece() {
        board.getAnchorPane().getChildren().remove(board.getAnchorPane().lookup("#selectedPiece"));
    }

    public void displayLegalMoves(Piece piece) {
        ArrayList<Square> legalMoves = piece.getLegalMoves();

        Group possibleMoves = new Group();
        possibleMoves.setId("legalMoves");

        legalMoves.forEach(lm -> {
            Rectangle moveRec = createMoveRectangle();
            double sqrSize = board.getSquareSize();
            moveRec.setLayoutX(lm.getX(sqrSize));
            moveRec.setLayoutY(lm.getY(sqrSize));
            if (board.isSquareOccupied(Square.find(
                    lm.getX(sqrSize),
                    lm.getY(sqrSize),
                    sqrSize))) {
                moveRec.setFill(Color.web("#9A3048", 1));
            } else {
                moveRec.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/hc.png")));
            }
            possibleMoves.getChildren().add(moveRec);
        });

        board.getAnchorPane().getChildren().add(possibleMoves);
        board.getPieceList().forEach(Node::toFront); // All pieces to front in terms of z-index
    }

    public void removeLegalMoves() {
        board.getAnchorPane().getChildren().remove(board.getAnchorPane().lookup("#legalMoves"));
    }

    public void displayLastMovePath(Square newPos, Square lastPos) {
        double sqrSize = board.getSquareSize();

        Rectangle newSquare = createHighlightRectangle();
        newSquare.setId("newSquare");
        newSquare.setLayoutX(newPos.getX(sqrSize));
        newSquare.setLayoutY(newPos.getY(sqrSize));

        Rectangle lastSquare = createHighlightRectangle();
        lastSquare.setId("lastSquare");
        lastSquare.setLayoutX(lastPos.getX(sqrSize));
        lastSquare.setLayoutY(lastPos.getY(sqrSize));

        board.getAnchorPane().getChildren().addAll(newSquare, lastSquare);
        board.getPieceList().forEach(Node::toFront); // All pieces to front in terms of z-index
    }

    public void removeLastMovePath() {
        board.getAnchorPane().getChildren().remove(board.getAnchorPane().lookup("#lastSquare"));
        board.getAnchorPane().getChildren().remove(board.getAnchorPane().lookup("#newSquare"));
    }

    private Rectangle createHighlightRectangle() {
        Rectangle rec = new Rectangle(board.getSquareSize(), board.getSquareSize());
        rec.setSmooth(false);
        rec.setFill(Color.web("#FEF250", 0.5));
        return rec;
    }

    private Rectangle createMoveRectangle() {
        Rectangle rec = new Rectangle(board.getSquareSize(), board.getSquareSize());
        rec.setSmooth(false);
        return rec;
    }
}
