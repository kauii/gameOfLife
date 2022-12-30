package Game;

import Board.Board;
import GUI.Frames.GameOfLife;
import Board.PlayerNr;

import java.util.HashMap;
import java.util.Map;

import static Board.PlayerNr.*;

public class Game implements Observer {
    Singleton players = Singleton.getInstance();
    private Board board;
    private Player player1;
    private Player player2;
    private final int PLAYER1_INDEX = 0;
    private final int PLAYER2_INDEX = 1;
    private GameOfLife gui;
    private int player1Cells;
    private int player2Cells;

    public void setUp(int dimension) {

        // create board
        this.board = new Board(dimension);

        // sort Names alphabetically
        players.sortList();

        gui = new GameOfLife(board.getBoard());

        // determine player 1 player 2
        player1 = players.getPlayer(PLAYER1_INDEX);

        player2 = players.getPlayer(PLAYER2_INDEX);

        // register observer for board panel and gof frame
        gui.registerObserver(this);
    }

    private void updatePlayerCells() {
        player1Cells = board.getPlayerCells()[PLAYER1_INDEX];
        player2Cells = board.getPlayerCells()[PLAYER2_INDEX];
        player1.setLiveCells(player1Cells);
        player2.setLiveCells(player2Cells);
    }

    private void checkWinner() {
        if (player1Cells == 0 && player2Cells == 0) {
            gui.declareWinner(null);
        }
        else if (player1Cells == 0) {
            gui.declareWinner(player2);
        }
        else if (player2Cells == 0) {
            gui.declareWinner(player1);
        }
    }

    @Override
    public void updateGrid(PlayerNr[][] grid) {
        GridIterator iterator = new GridIterator(grid);
        while (iterator.hasNext()) {
            int row = iterator.getRow();
            int col = iterator.getCol();
            PlayerNr cell = iterator.next();
            board.setCell(row, col, cell);
        }
    }

    @Override
    public void skipGen() {

        // evolve cells
        board.evolve();
        gui.setBoard(board.getBoard());

        // update number of player cells
        updatePlayerCells();

        // check winner
        checkWinner();
    }


    @Override
    public void undo() {
        gui.setBoard(board.undo());
    }

}
