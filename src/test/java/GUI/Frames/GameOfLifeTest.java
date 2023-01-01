package GUI.Frames;

import Board.Cell;
import Game.Singleton;
import Observer.Buttons.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;


class GameOfLifeTest {

    private GameOfLife game;
    private MockObserver observer;

    @BeforeEach
    void setUp() {

        Cell[][] grid = new Cell[][] {
                { Cell.DEAD, Cell.DEAD, Cell.DEAD },
                { Cell.DEAD, Cell.DEAD, Cell.DEAD },
                { Cell.DEAD, Cell.DEAD, Cell.DEAD }
        };
        game = new GameOfLife(grid);

        observer = new MockObserver();
        game.registerObserver(observer);

    }

    private static class MockObserver implements Observer {
        int skipGenCount = 0;
        int undoCount = 0;
        int clearStackCount = 0;
        int resetCount = 0;

        @Override
        public void skipGen() {
            skipGenCount++;
        }

        @Override
        public void undo() {
            undoCount++;
        }

        @Override
        public void clearStack() {
            clearStackCount++;
        }

        @Override
        public void reset() {
            resetCount++;
        }
    }

    @Test
    void setBoard() {

        Cell[][] grid = new Cell[][] {
                { Cell.DEAD, Cell.PLAYER1, Cell.DEAD },
                { Cell.DEAD, Cell.PLAYER2, Cell.DEAD },
                { Cell.DEAD, Cell.DEAD, Cell.DEAD }
        };

        game.setBoard(grid);

        assertArrayEquals(grid, game.getBoard());
    }

    @Test
    void notifyObserver() {

        game.notifyObserver(new ActionEvent(this, 0, "Evolve"));

        assertEquals(1, observer.skipGenCount);
        assertEquals(0, observer.clearStackCount);
        assertEquals(0, observer.resetCount);
        assertEquals(0,observer.undoCount);

        game.notifyObserver(new ActionEvent(this, 12, "Start"));

        assertEquals(1, observer.skipGenCount);
        assertEquals(1, observer.clearStackCount);
        assertEquals(0, observer.resetCount);
        assertEquals(0,observer.undoCount);

        game.notifyObserver(new ActionEvent(this, 0, "Reset"));

        assertEquals(1, observer.skipGenCount);
        assertEquals(1, observer.clearStackCount);
        assertEquals(1, observer.resetCount);
        assertEquals(0,observer.undoCount);

        game.notifyObserver(new ActionEvent(this, 2, "Undo"));

        assertEquals(1, observer.skipGenCount);
        assertEquals(1, observer.clearStackCount);
        assertEquals(1, observer.resetCount);
        assertEquals(1,observer.undoCount);
    }
}