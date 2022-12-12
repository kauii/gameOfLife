package Game;

import Board.Board;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Game {

    private Board board;
    private boolean gameOver;
    private final List<Player> players = new ArrayList<>();


    public Game() {
    }

    public void setUp() {

        initialBoardConfig();

        // Player 1
        players.add(new Player(//TODO: ask User about Name))
                "Bob"));

        // Player 2
        players.add(new Player("Joe"));

        // sort Names alphabetically
        players.sort(Comparator.comparing(Player::getName));

        // determine player 1 player 2
        players.get(0).setPlayerNr(PlayerNr.PLAYER1);
        players.get(1).setPlayerNr(PlayerNr.PLAYER2);

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

        while (!gameOver) {

        }

    }

}
