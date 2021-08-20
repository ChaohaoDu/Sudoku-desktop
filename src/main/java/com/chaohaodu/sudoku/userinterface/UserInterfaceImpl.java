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

public class UserInterfaceImpl implements IUserInterfaceContract.View, EventHandler<KeyEvent> {
    private final Stage stage;
    private final Group group;

    // <coordinates, value>
    private HashMap<Coordinates, SudokuTextField> textFieldCoordinates;

    private IUserInterfaceContract.EventListener listener;

    //Size of the window
    private static final double WINDOW_Y = 732;
    private static final double WINDOW_X = 668;
    //distance between window and board
    private static final double BOARD_PADDING = 50;

    private static final double BOARD_X_AND_Y = 576;
    private static final Color WINDOW_BACKGROUND_COLOR = Color.rgb(0, 150, 136);
    private static final Color BOARD_BACKGROUND_COLOR = Color.rgb(224, 242, 241);
    private static final String SUDOKU = "Sudoku";

    public UserInterfaceImpl(Stage stage) {
        this.stage = stage;
        this.group = new Group();
        this.textFieldCoordinates = new HashMap<>();
        initializeUserInterface();
    }


    @Override
    public void setListener(IUserInterfaceContract.EventListener listener) {
        this.listener = listener;
    }

    public void initializeUserInterface() {
        drawBackground(group);
        drawTitle(group);
        drawSudokuBoard(group);
        drawTextFields(group);
        drawGridLines(group);
        stage.show();
    }

    private void drawTextFields(Group group) {
        final int X = 50;
        final int Y = 50;
        final int X_AND_Y_DELTA = 64;

        for (int xIndex = 0; xIndex < 9; xIndex++) {
            for (int yIndex = 0; yIndex < 9; yIndex++) {
                int currX = X + xIndex * X_AND_Y_DELTA;
                int currY = Y + yIndex * X_AND_Y_DELTA;

                SudokuTextField tile = new SudokuTextField(xIndex, yIndex);
                styleSudokuTile(tile, currX, currY);

                tile.setOnKeyPressed(this);

                textFieldCoordinates.put(new Coordinates(xIndex, yIndex), tile);

                group.getChildren().add(tile);
            }
        }
    }


    private void styleSudokuTile(SudokuTextField tile, double x, double y) {
        Font numberFont = new Font(32);
        tile.setFont(numberFont);
        tile.setAlignment(Pos.CENTER);

        tile.setLayoutX(x);
        tile.setLayoutY(y);
        tile.setPrefHeight(64);
        tile.setPrefWidth(64);

        tile.setBackground(Background.EMPTY);
    }


    private void drawGridLines(Group group) {
        int xAndY = 114;

        for (int i = 0; i < 8; i++) {
            int thickness = (i == 2 || i == 5) ? 3 : 2;
            Rectangle verticalLine = getLine(xAndY + 64 * i, BOARD_PADDING, BOARD_X_AND_Y,
               thickness);

            Rectangle horizontalLine = getLine(BOARD_PADDING, xAndY + 64 * i, thickness,
               BOARD_X_AND_Y);

            group.getChildren().addAll(verticalLine, horizontalLine);

        }
    }


    public Rectangle getLine(double x, double y, double height, double width) {
        Rectangle line = new Rectangle();

        line.setX(x);
        line.setY(y);

        line.setHeight(height);
        line.setWidth(width);

        line.setFill(Color.BLACK);
        return line;

    }


    private void drawBackground(Group group) {
        Scene scene = new Scene(group, WINDOW_X, WINDOW_Y);
        scene.setFill(WINDOW_BACKGROUND_COLOR);
        stage.setScene(scene);
    }


    private void drawSudokuBoard(Group group) {
        Rectangle boardBackground = new Rectangle();
        boardBackground.setX(BOARD_PADDING);
        boardBackground.setY(BOARD_PADDING);
        boardBackground.setWidth(BOARD_X_AND_Y);
        boardBackground.setHeight(BOARD_X_AND_Y);
        boardBackground.setFill(BOARD_BACKGROUND_COLOR);
        group.getChildren().add(boardBackground);
    }

    private void drawTitle(Group group) {
        Text title = new Text(235, 690, SUDOKU);
        title.setFill(Color.WHITE);
        Font titleFont = new Font(43);
        title.setFont(titleFont);
        group.getChildren().add(title);
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

            if (text.matches("[1-9]+")) {
                int value = Integer.parseInt(text);
                handleInput(value, event.getSource());

            } else if (event.getCode() == KeyCode.BACK_SPACE) {
                handleInput(0, event.getSource());

            } else {
                ((TextField) event.getSource()).setText("");
            }
        }

        event.consume();
    }


    private void handleInput(int value, Object source) {
        listener.onSudokuInput(((SudokuTextField) source).getX(), ((SudokuTextField) source).getY(),
           value);
    }
}
