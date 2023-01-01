package GUI.Panels;

import Board.Cell;
import Game.GridIterator;
import Game.Player;
import Game.Singleton;
import Observer.Board.CellObserver;
import Observer.Board.JObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.MouseEvent;

class BoardPanelTest {

    private final Singleton players = Singleton.getInstance();
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final int CELL_SIZE = 10;
    private static final Cell[][] grid = new Cell[ROWS][COLS];
    private static final Player player1 = new Player("Player 1", Color.RED);
    private static final Player player2 = new Player("Player 2", Color.BLACK);

    private BoardPanel panel;
    private MockJObserver observer;
    private MockCellObserver cellObserver;

    @BeforeEach
    public void setUp() {
        GridIterator iterator = new GridIterator(grid);
        while (iterator.hasNext()) {
            grid[iterator.getRow()][iterator.getCol()] = Cell.DEAD;
            iterator.next();
        }
        players.addToList(player1);
        players.addToList(player2);
        panel = new BoardPanel(grid);

        observer = new MockJObserver();
        panel.registerJObserver(observer);

        cellObserver = new MockCellObserver();
        panel.registerCellObserver(cellObserver);
    }


    @Test
    void initialPlacement() {
        MouseEvent cell1 = simulateMouseEvent(0,0);
        panel.dispatchEvent(cell1);

        // test symmetry
        assert(panel.getGrid()[0][0] == Cell.PLAYER1);
        assert(panel.getGrid()[9][9] == Cell.PLAYER2);

        // place all initial cells
        MouseEvent cell2 = simulateMouseEvent(0,1);
        panel.dispatchEvent(cell2);
        MouseEvent cell3 = simulateMouseEvent(0,2);
        panel.dispatchEvent(cell3);
        MouseEvent cell4 = simulateMouseEvent(0,3);
        panel.dispatchEvent(cell4);
        MouseEvent cell5 = simulateMouseEvent(0,4);
        panel.dispatchEvent(cell5);
        MouseEvent cell6 = simulateMouseEvent(0,5);
        panel.dispatchEvent(cell6);

        // simulate a 7th cell that should not be placed in the board
        MouseEvent cell7 = simulateMouseEvent(0,6);
        panel.dispatchEvent(cell7);

        assert(panel.getGrid()[6][0] == Cell.DEAD);


    }


    @Test
    void play() {
        // fulfill requirements to invoke play method

        // 1st: place all 6 cells
        MouseEvent cell1 = simulateMouseEvent(0,0);
        panel.dispatchEvent(cell1);
        MouseEvent cell2 = simulateMouseEvent(2,4);
        panel.dispatchEvent(cell2);
        MouseEvent cell3 = simulateMouseEvent(1,3);
        panel.dispatchEvent(cell3);
        MouseEvent cell4 = simulateMouseEvent(3,3);
        panel.dispatchEvent(cell4);
        MouseEvent cell5 = simulateMouseEvent(3,4);
        panel.dispatchEvent(cell5);
        MouseEvent cell6 = simulateMouseEvent(4,4);
        panel.dispatchEvent(cell6);

        // 2nd: start button pressed
        panel.startGame();

        // check if cell is killed
        assert(panel.getGrid()[9][9] == Cell.PLAYER2);
        MouseEvent killCell1 = simulateMouseEvent(9,9);
        panel.dispatchEvent(killCell1);
        assert(panel.getGrid()[9][9] == Cell.DEAD);

        // check if cell is placed
        MouseEvent placeCell1 = simulateMouseEvent(9,9);
        panel.dispatchEvent(placeCell1);
        assert(panel.getGrid()[9][9] == Cell.PLAYER1);

        // check if other mouse event will be ignored
        assert(panel.getGrid()[6][5] == Cell.DEAD);
        MouseEvent dummyCell1 = simulateMouseEvent(5,6);
        panel.dispatchEvent(dummyCell1);
        assert(panel.getGrid()[6][5] == Cell.DEAD);

        panel.changeActivePlayer();

        // check if cell is killed
        assert(panel.getGrid()[0][0] == Cell.PLAYER1);
        MouseEvent killCell2 = simulateMouseEvent(0,0);
        panel.dispatchEvent(killCell2);
        assert(panel.getGrid()[0][0] == Cell.DEAD);

        // check if cell is placed
        MouseEvent placeCell2 = simulateMouseEvent(0,0);
        panel.dispatchEvent(placeCell2);
        assert(panel.getGrid()[0][0] == Cell.PLAYER2);

        // check if other mouse event will be ignored
        assert(panel.getGrid()[1][0] == Cell.DEAD);
        MouseEvent dummyCell2 = simulateMouseEvent(0,1);
        panel.dispatchEvent(dummyCell2);
        assert(panel.getGrid()[1][0] == Cell.DEAD);

    }

    @Test
    void inBoard() {
        // enable start button
        observer.startIsEnabled = true;

        // if in board -> notifyJObserver -> start button will be disabled
        MouseEvent cell0 = simulateMouseEvent(20,40);
        panel.dispatchEvent(cell0);
        assert(observer.startIsEnabled);
    }

    @Test
    void startGame() {

        MouseEvent cell1 = simulateMouseEvent(2,3);
        panel.dispatchEvent(cell1);
        MouseEvent cell2 = simulateMouseEvent(2,4);
        panel.dispatchEvent(cell2);
        MouseEvent cell3 = simulateMouseEvent(1,3);
        panel.dispatchEvent(cell3);
        MouseEvent cell4 = simulateMouseEvent(3,3);
        panel.dispatchEvent(cell4);
        MouseEvent cell5 = simulateMouseEvent(3,4);
        panel.dispatchEvent(cell5);
        MouseEvent cell6 = simulateMouseEvent(4,4);
        panel.dispatchEvent(cell6);

        // check if start button is disabled after pressing start button
        panel.startGame();
        panel.notifyJObserver();
        assert(!observer.startIsEnabled);
    }

    @Test
    void undoLastAction() {
        panel.startGame();

        // player 1 places cell = Cells[5][5]: 'PLACED' added in Actions[]
        MouseEvent cell1 = simulateMouseEvent(5,5);
        panel.dispatchEvent(cell1);
        assert(panel.getGrid()[5][5] == Cell.PLAYER1);

        // player 1 cannot place cell = Cells[6][6] because Actions[] registered 'PLACED'
        MouseEvent cell2 = simulateMouseEvent(6,6);
        panel.dispatchEvent(cell2);
        assert(panel.getGrid()[6][6] == Cell.DEAD);

        // player 1 places cell = Cells[6][6] by removing 'PLACED' in Actions[]
        panel.undoLastAction();
        panel.dispatchEvent(cell2);
        assert(panel.getGrid()[6][6] == Cell.PLAYER1);

        // player 2 kills cell = Cells[5][5]
        panel.changeActivePlayer();
        panel.dispatchEvent(cell1);
        assert(panel.getGrid()[5][5] == Cell.DEAD);

        // player 2 cannot kill cell = Cells[6][6] because Actions[] registered 'KILLED'
        panel.dispatchEvent(cell2);
        assert(panel.getGrid()[6][6] == Cell.PLAYER1);

        // undoLastAction removes registered enum Action 'KILLED'
        panel.undoLastAction();
        panel.dispatchEvent(cell2);
        assert(panel.getGrid()[6][6] == Cell.DEAD);

        // player 2 places cell
        panel.dispatchEvent(cell2);

        // player 2 places 2nd cell = Cells[7][6] by removing Action 'PLACED'
        panel.undoLastAction();
        MouseEvent cell3 = simulateMouseEvent(6,7);
        panel.dispatchEvent(cell3);
        assert(panel.getGrid()[7][6] == Cell.PLAYER2);

        // player 1 kills cell = Cells[6][6]
        panel.changeActivePlayer();
        panel.dispatchEvent(cell2);
        assert(panel.getGrid()[6][6] == Cell.DEAD);

        // player 1 cannot kill cell = Cells[7][6]
        panel.dispatchEvent(cell3);
        assert(panel.getGrid()[7][6] == Cell.PLAYER2);

        // player 1 kills cell = Cells[7][6]
        panel.undoLastAction();
        panel.dispatchEvent(cell3);
        assert(panel.getGrid()[7][6] == Cell.DEAD);
    }

    @Test
    void mousePressed() {
        // simulate mouse pressed at (6,9)
        MouseEvent cell1 = simulateMouseEvent(6,9);
        panel.dispatchEvent(cell1);

        assert(panel.getGrid()[9][6] == Cell.PLAYER1);

        panel.dispatchEvent(cell1);
        assert(panel.getGrid()[9][6] == Cell.DEAD);
    }

    private MouseEvent simulateMouseEvent(int x, int y) {
        return new MouseEvent(panel, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0,
                x * CELL_SIZE, y * CELL_SIZE, 1, false);
    }

    @Test
    void clear() {

        MouseEvent cell1 = simulateMouseEvent(2,3);
        panel.dispatchEvent(cell1);
        MouseEvent cell2 = simulateMouseEvent(2,4);
        panel.dispatchEvent(cell2);
        MouseEvent cell3 = simulateMouseEvent(1,3);
        panel.dispatchEvent(cell3);
        MouseEvent cell4 = simulateMouseEvent(3,3);
        panel.dispatchEvent(cell4);
        MouseEvent cell5 = simulateMouseEvent(3,4);
        panel.dispatchEvent(cell5);
        MouseEvent cell6 = simulateMouseEvent(4,4);
        panel.dispatchEvent(cell6);

        assert(observer.startIsEnabled);

        panel.clear();

        assert(!observer.startIsEnabled);

        GridIterator iterator = new GridIterator(panel.getGrid());
        while (iterator.hasNext()) {
            assert(panel.getGrid()[iterator.getRow()][iterator.getCol()] == Cell.DEAD);
            iterator.next();
        }

    }

    @Test
    void notifyJObserver() {

        // place first cell
        MouseEvent cell1 = simulateMouseEvent(2,3);
        panel.dispatchEvent(cell1);

        // check if start button is disabled
        assert(!observer.startIsEnabled);

        // place all 6 cells
        MouseEvent cell2 = simulateMouseEvent(2,4);
        panel.dispatchEvent(cell2);
        MouseEvent cell3 = simulateMouseEvent(1,3);
        panel.dispatchEvent(cell3);
        MouseEvent cell4 = simulateMouseEvent(3,3);
        panel.dispatchEvent(cell4);
        MouseEvent cell5 = simulateMouseEvent(3,4);
        panel.dispatchEvent(cell5);
        MouseEvent cell6 = simulateMouseEvent(4,4);
        panel.dispatchEvent(cell6);

        // check if start button is enabled after 6 cells
        assert (observer.startIsEnabled);
    }

    @Test
    void notifyCellObserver() {
        panel.startGame();
        MouseEvent cell1 = simulateMouseEvent(1,1);
        panel.dispatchEvent(cell1);
        assert(cellObserver.cell == Cell.PLAYER1);
        assert(cellObserver.row == 1);
        assert(cellObserver.col == 1);
    }

    private static class MockJObserver implements JObserver {
        boolean startIsEnabled = false;
        boolean evolveIsEnabled = false;
        boolean undoIsEnabled = false;
        Color placed;
        Color killed;
        @Override
        public void enableStart(boolean enable) {
            startIsEnabled = enable;
        }

        @Override
        public void enableEvolve(boolean enable) {
            evolveIsEnabled = enable;
        }

        @Override
        public void enableUndo(boolean enable) {
            undoIsEnabled = enable;
        }

        @Override
        public void colorPlaced(Color color) {
            placed = color;
        }

        @Override
        public void colorKilled(Color color) {
            killed = color;
        }
    }

    private static class MockCellObserver implements CellObserver {
        int row, col;
        Cell cell;
        @Override
        public void updateCell(int row, int col, Cell cell) {
            this.row = row;
            this.col = col;
            this.cell = cell;
        }
    }
}