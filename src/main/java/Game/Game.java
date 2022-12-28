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
        player1.setPlayerNr(PLAYER1);

        player2 = players.getPlayer(PLAYER2_INDEX);
        player2.setPlayerNr(PLAYER2);

        // register observer for board panel and gof frame
        gui.registerObserver(this);
        gui.registerBoardObserver(this);
    }


    public void supdateGrid(PlayerNr[][] grid) {
        Map<PlayerNr, PlayerNr> playerMap = new HashMap<>();
        playerMap.put(PLAYER1, PLAYER1);
        playerMap.put(PLAYER2, PLAYER2);
        playerMap.put(null, DEAD);

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                PlayerNr value = playerMap.get(grid[row][col]);
                PlayerNr[][] raw = board.setCell(row, col, value);
            }
        }
    }

    @Override
    public void updateGrid(PlayerNr[][] grid) {
        GridIterator iterator = new GridIterator(grid);
        while (iterator.hasNext()) {
            PlayerNr cell = iterator.next();
            int row = iterator.getRow();
            int col = iterator.getCol();
            board.setCell(row, col, cell);
        }
    }

    @Override
    public void skipGen() {

        // evolve cells
        board.evolve();
        gui.setBoard(board.getBoard());

        // update number of player cells
        player1Cells = board.getPlayerCells()[PLAYER1_INDEX];
        player2Cells = board.getPlayerCells()[PLAYER2_INDEX];

        player1.setLiveCells(player1Cells);
        player2.setLiveCells(player2Cells);

        // check winner
        if (player1Cells == 0 || player2Cells == 0) {
            if (player1Cells == 0 && player2Cells == 0) {
                gui.declareWinner(null);
            }
            else if (player1Cells == 0) {
                gui.declareWinner(player2);
            }
            else {
                gui.declareWinner(player1);
            }
        }
    }

    @Override
    public void enableStart(boolean enable) {

        player1.setLiveCells(board.getPlayerCells()[PLAYER1_INDEX]);
        player2.setLiveCells(board.getPlayerCells()[PLAYER2_INDEX]);

        gui.enableStartButton(enable);
    }

    @Override
    public void turnOver() {
        gui.enableEvolveButton(true);
    }

}
