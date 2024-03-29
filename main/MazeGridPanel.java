package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import generator.*;
import solver.*;
import util.Cell;

public class MazeGridPanel extends JPanel {

    private static final long serialVersionUID = 7237062514425122227L;
    private final List<Cell> grid = new ArrayList<Cell>();
    private List<Cell> currentCells = new ArrayList<Cell>();

    public MazeGridPanel(int rows, int cols) {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                grid.add(new Cell(x, y));
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        // +1 pixel on width and height so bottom and right borders can be drawn.
        return new Dimension(Maze.WIDTH + 1, Maze.HEIGHT + 1);
    }

    public void generate(int index) {
        switch (index) {
            case 0:
                new recursiveBacktracking(grid, this);
                break;
            case 1:
                new huntAndKill(grid, this);
                break;
            case 2:
                new growingTree(grid, this);
                break;
            default:
                new recursiveBacktracking(grid, this);
                break;
        }
    }

    public void solve(int index) {
        switch (index) {
            case 0:
                new DFSolve(grid, this);
                break;
            case 1:
                new BFSolve(grid, this);
                break;
            case 2:
                new DijkstraSolve(grid, this);
                break;
            default:
                new DFSolve(grid, this);
                break;
        }
    }

    public void resetSolution() {
        for (Cell c : grid) {
            c.setDeadEnd(false);
            c.setPath(false);
            c.setDistance(-1);
            c.setParent(null);
        }
        repaint();
    }

    public void setCurrent(Cell current) {
        if(currentCells.size() == 0) {
            currentCells.add(current);
        } else {
            currentCells.set(0, current);
        }
    }

    public void setCurrentCells(List<Cell> currentCells) {
        this.currentCells = currentCells;
    }

    @Override
    protected void paintComponent(Graphics g) {
        int x2 = grid.get(0).getX() * Maze.W;
        int y2 = grid.get(0).getY() * Maze.W;
        super.paintComponent(g);
        for (Cell c : grid) {
            c.draw(g);
        }
        for (Cell c : currentCells) {
            if (c != null) c.displayAsColor(g, Color.ORANGE);
        }
        grid.get(0).displayAsColor(g, Color.decode("#34eb5c")); // start cell
        g.setColor(Color.decode("#323232"));
        g.drawLine(x2, y2, x2 + Maze.W, y2);
        g.drawLine(x2, y2 + Maze.W, x2, y2);
        grid.get(grid.size() - 1).displayAsColor(g, Color.decode("#34e8eb")); // end or goal cell
        g.setColor(Color.decode("#323232"));
        x2 = grid.get(grid.size() - 1).getX() * Maze.W;
        y2 = grid.get(grid.size() - 1).getY() * Maze.W;
        g.drawLine(x2+Maze.W, y2+Maze.W, x2, y2+Maze.W);
        g.drawLine(x2+Maze.W, y2, x2+Maze.W, y2+Maze.W);
    }
}
