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

        gui = new GUI(board.getBoard());

        // sort Names alphabetically
       players.sortList();

        // determine player 1 player 2
        //players.getList(PLAYER1_INDEX).setPlayerNr(PlayerNr.PLAYER1);
        //players.get(PLAYER2_INDEX).setPlayerNr(PlayerNr.PLAYER2);
        for (Player player : players.getList()) {
            System.out.println(player.getName());
        }


        initialBoardConfig();

        gui.registerObserver(this);

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

}
