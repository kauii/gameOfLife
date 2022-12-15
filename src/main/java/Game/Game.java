package Game;

import Board.Board;
import GUI.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Game {

    private Board board;
    private Player winner;
    private final List<Player> players = new ArrayList<>();
    private final int PLAYER1_INDEX = 0;
    private final int PLAYER2_INDEX = 1;

    public Game() {
        GUI gui = new GUI();
    }

    public void setUp() {

        initialBoardConfig();

        // Player 1
        players.add(new Player(//TODO: ask User about Name))
                "Bob", Color.BLUE));

        // Player 2
        players.add(new Player("Joe",Color.RED));

        // sort Names alphabetically
        players.sort(Comparator.comparing(Player::getName));

        // determine player 1 player 2
        players.get(PLAYER1_INDEX).setPlayerNr(PlayerNr.PLAYER1);
        players.get(PLAYER2_INDEX).setPlayerNr(PlayerNr.PLAYER2);

        // Ask which color for each Player should be used

        //
    }

    private void initialBoardConfig() {

        // Create Board
        this.board = new Board(//TODO: ask User for dimension through GUI
                1000);

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



}
