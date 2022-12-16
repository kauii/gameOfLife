package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class JBoard extends JComponent implements MouseListener, ComponentListener {
    private int size = 14;
    private Point[][] points;

    public JBoard(int width, int height) {
        Point point = new Point();

        addMouseListener(this);
        addComponentListener(this);
        setBackground(Color.GREEN);
        setOpaque(true);
    }

    private void initialize(int width, int height) {
        points = new Point[width][height];

        for (int x = 0; x < points.length; x++) {
            for (int y = 0; y < points[x].length; y++) {
                points[x][y] = new Point();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / size;

    }

    @Override
    public void componentResized(ComponentEvent e) {
        int w = getWidth() / size + 1;
        int h = getHeight() / size + 1;
        initialize(w, h);

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
