package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.enums.Square;
import com.tomaslevesconte.javachess.pieces.Piece;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;

public class UIComponents {
    private final Board board;

    public UIComponents(Board board) {
        this.board = board;
    }

    public void displaySelectedPiece(Piece piece) {
        Rectangle selectedRec = createHighlightRectangle("#FEF250", 0.5f);
        selectedRec.setId("selectedPiece");
        selectedRec.setLayoutX(piece.getLayoutX());
        selectedRec.setLayoutY(piece.getLayoutY());

        board.getAnchorPane().getChildren().addAll(selectedRec);
        board.getPieceList().forEach(Node::toFront); // All pieces to front in terms of z-index
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
                moveRec.setFill(Color.web("#9A3048", 1.0f));
            } else {
                moveRec.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/hc.png")));
            }
            possibleMoves.getChildren().add(moveRec);
        });

        board.getAnchorPane().getChildren().add(possibleMoves);
        board.getPieceList().forEach(Node::toFront); // All pieces to front in terms of z-index
    }

    public void displayLastMovePath(Square newPos, Square lastPos) {
        double sqrSize = board.getSquareSize();

        Rectangle newRec = createHighlightRectangle("#FEF250", 0.5f);
        newRec.setId("newRec");
        newRec.setLayoutX(newPos.getX(sqrSize));
        newRec.setLayoutY(newPos.getY(sqrSize));

        Rectangle lastRec = createHighlightRectangle("#FEF250", 0.5f);
        lastRec.setId("lastRec");
        lastRec.setLayoutX(lastPos.getX(sqrSize));
        lastRec.setLayoutY(lastPos.getY(sqrSize));

        board.getAnchorPane().getChildren().addAll(newRec, lastRec);
        board.getPieceList().forEach(Node::toFront); // All pieces to front in terms of z-index
    }


    public void displayKingInCheck(Piece king) {
        Rectangle checkRec = createHighlightRectangle("#9A3048", 1.0f);
        checkRec.setId("checkRec");
        checkRec.setLayoutX(king.getLayoutX());
        checkRec.setLayoutY(king.getLayoutY());
        checkRec.setSmooth(false);

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.8f),
                new KeyValue(checkRec.fillProperty(), Color.web("#9A3048", 0.0f)));
        Timeline blink = new Timeline();
        blink.getKeyFrames().add(kf);
        blink.setCycleCount(Animation.INDEFINITE);
        blink.setAutoReverse(true);
        blink.play();

        board.getAnchorPane().getChildren().add(checkRec);
        board.getPieceList().forEach(Node::toFront); // All pieces to front in terms of z-index
    }

    public void removeSelectedPiece() {
        board.getAnchorPane().getChildren().remove(board.getAnchorPane().lookup("#selectedPiece"));
    }

    public void removeLegalMoves() {
        board.getAnchorPane().getChildren().remove(board.getAnchorPane().lookup("#legalMoves"));
    }

    public void removeLastMovePath() {
        board.getAnchorPane().getChildren().remove(board.getAnchorPane().lookup("#newRec"));
        board.getAnchorPane().getChildren().remove(board.getAnchorPane().lookup("#lastRec"));
    }

    public void removeKingInCheck() {
        board.getAnchorPane().getChildren().remove(board.getAnchorPane().lookup("#checkRec"));
    }

    private Rectangle createHighlightRectangle(String hexCode, double opacity) {
        Rectangle rec = new Rectangle(board.getSquareSize(), board.getSquareSize());
        rec.setSmooth(false);
        rec.setFill(Color.web(hexCode, opacity));
        return rec;
    }

    private Rectangle createMoveRectangle() {
        Rectangle rec = new Rectangle(board.getSquareSize(), board.getSquareSize());
        rec.setSmooth(false);
        return rec;
    }
}
