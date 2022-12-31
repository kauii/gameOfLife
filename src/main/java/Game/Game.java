package Game;

import Board.Board;
import GUI.Frames.GameOfLife;
import Board.Cell;
import Observer.Buttons.Observer;
import Observer.Board.CellObserver;

import static Board.Cell.DEAD;

public class Game implements Observer, CellObserver {
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
        gui.registerCellObserver(this);
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
    public void skipGen() {

        // evolve cells
        board.evolve();
        gui.setBoard(board.getBoard());

        // update number of player cells
        updatePlayerCells();

        checkWinner();
    }

    @Override
    public void undo() {
        Cell[][] last = board.undo();
        gui.setBoard(last);
    }

    @Override
    public void clearStack() {
        board.clearStack();
    }

    @Override
    public void reset() {
        GridIterator iterator = new GridIterator(board.getBoard());
        while (iterator.hasNext()) {
            int row = iterator.getRow();
            int col = iterator.getCol();
            board.setCell(row, col, DEAD);
            iterator.next();
        }
        gui.setBoard(board.getBoard());
    }

    @Override
    public void updateCell(int row, int col, Cell cell) {
        board.setCell(row, col, cell);
        updatePlayerCells();
    }
}
