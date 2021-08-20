package com.chaohaodu.sudoku.userinterface;

import com.chaohaodu.sudoku.constants.GameState;
import com.chaohaodu.sudoku.problemdomain.SudokuGame;
import com.chaohaodu.sudoku.problemdomain.Coordinates;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;


public class BadUserInterfaceImpl implements IUserInterfaceContract.View, EventHandler<KeyEvent> {
    private final Stage stage;
    private final Group root;

    private HashMap<Coordinates, SudokuTextField> textFieldCoordinates;
    private IUserInterfaceContract.EventListener listener;


    public BadUserInterfaceImpl(Stage stage) {
        this.stage = stage;
        this.root = new Group();
        this.textFieldCoordinates = new HashMap<>();

        Scene scene = new Scene(root, 668, 668);

        Text t = new Text(235, 690, "Sudoku");
        t.setFill(Color.WHITE);
        Font tf = new Font(43);
        t.setFont(tf);
        root.getChildren().add(t);
        Color c = Color.rgb(0, 150, 136);
        scene.setFill(c);
        stage.setScene(scene);

        Rectangle bg = new Rectangle();
        bg.setX(50);
        bg.setY(50);
        bg.setWidth(576);
        bg.setHeight(576);
        bg.setFill(Color.rgb(224, 242, 241));
        root.getChildren().add(bg);

        final int X = 50;
        final int Y = 50;

        final int X_AND_Y_DELTA = 64;

        for (int xIndex = 0; xIndex < 9; xIndex++) {
            for (int yIndex = 0; yIndex < 9; yIndex++) {
                int currX = X + xIndex * X_AND_Y_DELTA;
                int currY = Y + yIndex * X_AND_Y_DELTA;

                SudokuTextField stf = new SudokuTextField(xIndex, yIndex);
                Font numberFont = new Font(32);
                stf.setFont(numberFont);
                stf.setAlignment(Pos.CENTER);
                stf.setLayoutX(currX);
                stf.setLayoutY(currY);
                stf.setPrefHeight(64);
                stf.setPrefWidth(64);
                stf.setBackground(Background.EMPTY);
                stf.setOnKeyPressed(this);
                textFieldCoordinates.put(new Coordinates(xIndex, yIndex), stf);
                root.getChildren().add(stf);
            }
        }

        int xAndY = 114;
        int index = 0;

        for (int i = 0; i < 8; i++) {
            Rectangle vl = new Rectangle();

            vl.setX(xAndY + 64 * i);
            vl.setY(50);
            vl.setHeight(576);
            if (i == 2 || i == 5) {
                vl.setWidth(3);
            } else {
                vl.setWidth(2);
            }

            vl.setFill(Color.BLACK);

            Rectangle hl = new Rectangle();
            hl.setY(xAndY + 64 * i);
            hl.setX(50);
            hl.setWidth(576);
            if (i == 2 || i == 5) {
                hl.setHeight(3);
            } else {
                hl.setHeight(2);
            }
            hl.setFill(Color.BLACK);

            root.getChildren().addAll(vl, hl);

        }

        stage.show();
    }


    @Override
    public void setListener(IUserInterfaceContract.EventListener listener) {
        this.listener = listener;
    }


    @Override
    public void updateSquare(int x, int y, int input) {
        SudokuTextField tile = textFieldCoordinates.get(new Coordinates(x, y));
        String value = Integer.toString(input);

        if (value.equals("0")) {
            value = "";
        }

        tile.textProperty().setValue(value);
    }

    @Override
    public void updateBoard(SudokuGame game) {
        for (int xIndex = 0; xIndex < 9; xIndex++) {
            for (int yIndex = 0; yIndex < 9; yIndex++) {
                TextField tile = textFieldCoordinates.get(new Coordinates(xIndex, yIndex));

                String value = Integer.toString(game.getCopyOfGridState()[xIndex][yIndex]);

                if (value.equals("0")) {
                    value = "";
                }
                tile.setText(value);

                if (game.getGameState() == GameState.NEW) {
                    if (value.equals("")) {
                        tile.setStyle("-fx-opacity: 1;");
                        tile.setDisable(false);
                    } else {
                        tile.setStyle("-fx-opacity: 0.8;");
                        tile.setDisable(true);
                    }
                }
            }
        }
    }

    @Override
    public void showDialog(String message) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        dialog.showAndWait();

        if (dialog.getResult() == ButtonType.OK) {
            listener.onDialogClick();
        }
    }

    @Override
    public void showError(String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        dialog.showAndWait();
    }


    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            String text = event.getText();
            if (text.matches("[0-9]+")) {
                int value = Integer.parseInt(event.getText());
                listener.onSudokuInput(((SudokuTextField) event.getSource()).getX(),
                   ((SudokuTextField) event.getSource()).getY(), value);

            } else if (event.getCode() == KeyCode.BACK_SPACE) {
                listener.onSudokuInput(((SudokuTextField) event.getSource()).getX(),
                   ((SudokuTextField) event.getSource()).getY(), 0);

            } else {
                ((TextField) event.getSource()).setText("");
            }
        }

        event.consume();
    }
}
