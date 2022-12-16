package Game;

import Board.Board;
import GUI.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Game implements Observer {

    private Board board;
    private Player winner;
    public List<Player> players = new ArrayList<>();
    private final int PLAYER1_INDEX = 0;
    private final int PLAYER2_INDEX = 1;
    private GUI gui;

    public Game(Menu menu) {
        menu.registerObserver(this);
    }

    public void setUp() {

        // sort Names alphabetically
       players.sort(Comparator.comparing(Player::getName));

        // determine player 1 player 2
        players.get(PLAYER1_INDEX).setPlayerNr(PlayerNr.PLAYER1);
        players.get(PLAYER2_INDEX).setPlayerNr(PlayerNr.PLAYER2);

        System.out.println(players.get(PLAYER1_INDEX).getName());
        System.out.println(players.get(PLAYER2_INDEX).getName());

        gui = new GUI();

    }

    public void initialBoardConfig(int dimension) {

        // Create Board
        this.board = new Board(dimension);

        // Place first cells
        for (int i = 0; i < 4; i++) {
            board.setCell(0, 0, true, PlayerNr.PLAYER1);
        }

    }

    public void play() {

        while (winner == null) {

            for (Player player : players) {
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
            winner = players.get(PLAYER2_INDEX); // declare Player2 as Winner

        }
        if (playerCells[PLAYER2_INDEX] == 0) {
            winner = players.get(PLAYER2_INDEX); // declare Player1 as Winner
        }
    }


    public void updatePlayers(List<Player> players) {
        this.players = players;

    }
}
