package Game;

import Board.Board;
import GUI.*;

public class Game implements Observer {

    Singleton players = Singleton.getInstance();
    private Board board;
    private Player winner;
    private final int PLAYER1_INDEX = 0;
    private final int PLAYER2_INDEX = 1;
    private GUI gui;

    public void setUp(int dimension) {

        // create board
        this.board = new Board(dimension);

        // sort Names alphabetically
        players.sortList();

        gui = new GUI(board.getBoard());

        // determine player 1 player 2
        //players.getList(PLAYER1_INDEX).setPlayerNr(PlayerNr.PLAYER1);
        //players.get(PLAYER2_INDEX).setPlayerNr(PlayerNr.PLAYER2);
        for (Player player : players.getList()) {
            System.out.println(player.getName());
        }

        gui.registerObserver(this);
        gui.board.registerObserver(this);
        initialBoardConfig();
    }

    public void initialBoardConfig() {


        // Place first cells
        for (int i = 0; i < 4; i++) {
            board.setCell(0, 0, true, PlayerNr.PLAYER1);
        }

    }

    public void play() {

        while (winner == null) {

            for (Player player : players.getList()) {
                // Player takes turn
                turn(player);
                // Simulate Generation
                board.evolve();
                // Check if Cells == 0
                declareWinner();
            }
        }
    }

    public void turn(Player player) {



    }

    private void declareWinner() {
        int[] playerCells = board.getPlayerCells();

        if (playerCells[PLAYER1_INDEX] == 0) {
            winner = players.getPlayer(PLAYER2_INDEX); // declare Player2 as Winner

        }
        if (playerCells[PLAYER2_INDEX] == 0) {
            winner = players.getPlayer(PLAYER2_INDEX); // declare Player1 as Winner
        }
    }

    @Override
    public void updateGrid(short[][] grid) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {

                if (grid[row][col] == 2) {
                        board.setCell(row, col, true, PlayerNr.PLAYER1);
                }
                else if (grid[row][col] == 3) {
                    board.setCell(row, col, true, PlayerNr.PLAYER2);
                }
                else { board.setCell(row, col, false, PlayerNr.PLAYER1); } // don't matter which player
            }
        }
    }

    @Override
    public void skipGen() {
        board.evolve();
        gui.setBoard(board.getBoard());
    }
}
