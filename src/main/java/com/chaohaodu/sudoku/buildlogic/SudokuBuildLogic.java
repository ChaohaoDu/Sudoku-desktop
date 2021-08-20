package com.chaohaodu.sudoku.buildlogic;

import com.chaohaodu.sudoku.persistence.LocalStorageImpl;
import com.chaohaodu.sudoku.problemdomain.IStorage;
import com.chaohaodu.sudoku.problemdomain.SudokuGame;
import com.chaohaodu.sudoku.computationlogic.GameLogic;
import com.chaohaodu.sudoku.userinterface.IUserInterfaceContract;
import com.chaohaodu.sudoku.userinterface.logic.ControlLogic;

import java.io.IOException;

public class SudokuBuildLogic {

    public static void build(IUserInterfaceContract.View userInterface) throws IOException {
        SudokuGame initialState;
        IStorage storage = new LocalStorageImpl();

        try {
            initialState = storage.getGameData();

        } catch (IOException e) {
            initialState = GameLogic.getNewGame();
            storage.updateGameData(initialState);
        }

        IUserInterfaceContract.EventListener uiLogic = new ControlLogic(storage, userInterface);
        userInterface.setListener(uiLogic);
        userInterface.updateBoard(initialState);
    }
}
