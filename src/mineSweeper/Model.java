/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mineSweeper;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.Timer;

/**
 * Model of the game.
 *
 * @author tothv
 */
public class Model {

    /**
     * The current time elapsed from the start of the game.
     */
    private int time;

    /**
     * The size of the game(how many squares are in a row/column).
     */
    private final int size;

    /**
     * An array of the mines of the game.
     */
    private ArrayList<Point> mines;

    /**
     * An array of the flags of the game.
     */
    private ArrayList<Point> flags;

    /**
     * An array of the tiles that are not mines and haven't been procesd yet.
     */
    private ArrayList<Point> notMines;

    /**
     * An array of the tiles that are not mines and have been procesd yet.
     */
    private ArrayList<Point> processedNotMines;

    /**
     * The current position on the board.
     */
    private Point currentPosition;

    /**
     * Value if the game is over.
     */
    private boolean gameOver;

    /**
     * The timer of the gui.
     */
    private Timer timer;

    /**
     * Constructor for the game model.
     *
     * @param size the size of the board.
     */
    public Model(int size) {
        resetTimer();
        this.size = size;
        this.gameOver = false;
        this.mines = new ArrayList<>();
        this.flags = new ArrayList<>();
        this.processedNotMines = new ArrayList<>();
        this.currentPosition = new Point(0, 0);
        int totaleMinesAtStart;
        switch (size) {
            case 9:
                totaleMinesAtStart = 10;
                break;
            case 16:
                totaleMinesAtStart = 40;
                break;
            case 20:
                totaleMinesAtStart = 99;
                break;
            default:
                totaleMinesAtStart = 0;
                break;
        }
        ArrayList<Point> tiles = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tiles.add(new Point(i, j));
            }
        }
        Collections.shuffle(tiles);
        for (int i = 0; i < totaleMinesAtStart; i++) {
            Point thisTile = tiles.remove(0);
            mines.add(thisTile);
        }
        this.notMines = new ArrayList<>(tiles);
    }

    /**
     * Moves the current positons x value.
     *
     * @param value in which direction to move, 1 is up -1 is down
     * @return the current position
     */
    public Point moveX(int value) {
        if (value < 0) {
            if (currentPosition.x > 0 && currentPosition.x < size) {
                currentPosition.x += value;
            }
        } else {
            if (currentPosition.x >= 0 && currentPosition.x < size - 1) {
                currentPosition.x += value;
            }
        }
        return currentPosition;
    }

    /**
     * Moves the current psitions y value.
     *
     * @param value in which direction to move, 1 is right -1 is left
     * @return the current position
     */
    public Point moveY(int value) {
        if (value < 0) {
            if (currentPosition.y > 0 && currentPosition.y < size) {
                currentPosition.y += value;
            }
        } else {
            if (currentPosition.y >= 0 && currentPosition.y < size - 1) {
                currentPosition.y += value;
            }
        }
        return currentPosition;
    }

    /**
     * Check if there is a mine in the current position.
     *
     * @return if there is mine in the current psition
     */
    public boolean isThereAMine() {
        return mines.contains(currentPosition);
    }

    /**
     * Check if there is a flag in the given position.
     *
     * @param p the point where we check
     * @return if there is a flag in the given position
     */
    public boolean isFlaged(Point p) {
        return flags.contains(p);
    }

    /**
     * Puts or removes a flag in the current position.
     *
     * @param value 1 to place else to remove
     */
    public void setFlag(int value) {
        if (!processedNotMines.contains(currentPosition)) {
            if (value == 1) {
                flags.add(new Point(currentPosition.x, currentPosition.y));
            } else {
                flags.remove(new Point(currentPosition.x, currentPosition.y));
            }
        }
    }

    /**
     * Checks if the game is won yet.
     *
     * @return if the game is won yet
     */
    public boolean isGameWon() {
        return notMines.isEmpty() && size*size == processedNotMines.size()+flags.size();
    }

    /**
     * Interacts with the field.
     */
    public void interactWithField() {
        Queue<Point> queue = new LinkedList<>();
        if (!isFlaged(currentPosition) && !isThereAMine()) {
            notMines.remove(new Point(currentPosition.x, currentPosition.y));
            processedNotMines.add(new Point(currentPosition.x, currentPosition.y));
            queue.add(currentPosition);
            if (minesAround(currentPosition) == 0) {
                while (!queue.isEmpty()) {
                    Point next = queue.remove();
                    if (minesAround(next) == 0) {
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                Point p = new Point(next.x + i - 1, next.y + j - 1);
                                if (notMines.contains(p) && minesAround(p) == 0) {
                                    if (!isFlaged(p)) {
                                        queue.add(p);
                                        notMines.remove(p);
                                        processedNotMines.add(p);
                                    }
                                } else if (notMines.contains(p)) {
                                    if (!isFlaged(p)) {
                                        notMines.remove(p);
                                        processedNotMines.add(p);
                                    }
                                }
                            }
                        }
                    } else {
                        Point up = new Point(next.x - 1, next.y);
                        Point down = new Point(next.x + 1, next.y);
                        Point left = new Point(next.x, next.y - 1);
                        Point right = new Point(next.x, next.y + 1);
                        if (notMines.contains(up) && minesAround(up) == 0) {
                            if (!isFlaged(up)) {
                                queue.add(up);
                                notMines.remove(up);
                                processedNotMines.add(up);
                            }
                        } else if (notMines.contains(up)) {
                            if (!isFlaged(up)) {
                                notMines.remove(up);
                                processedNotMines.add(up);
                            }
                        }
                        if (notMines.contains(down) && minesAround(down) == 0) {
                            if (!isFlaged(down)) {
                                queue.add(down);
                                notMines.remove(down);
                                processedNotMines.add(down);
                            }
                        } else if (notMines.contains(down)) {
                            if (!isFlaged(down)) {
                                notMines.remove(down);
                                processedNotMines.add(down);
                            }
                        }
                        if (notMines.contains(left) && minesAround(left) == 0) {
                            if (!isFlaged(left)) {
                                queue.add(left);
                                notMines.remove(left);
                                processedNotMines.add(left);
                            }
                        } else if (notMines.contains(left)) {
                            if (!isFlaged(left)) {
                                notMines.remove(left);
                                processedNotMines.add(left);
                            }
                        }
                        if (notMines.contains(right) && minesAround(right) == 0) {
                            if (!isFlaged(right)) {
                                queue.add(right);
                                notMines.remove(right);
                                processedNotMines.add(right);
                            }
                        } else if (notMines.contains(right)) {
                            if (isFlaged(right)) {
                                notMines.remove(right);
                                processedNotMines.add(right);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Getter for the time.
     *
     * @return the time
     */
    public int getTime() {
        return time;
    }

    /**
     * Resets the timer.
     */
    public void resetTimer() {
        time = 0;
    }

    /**
     * Increments the timer.
     */
    public void incrementTime() {
        time++;
    }

    /**
     * Getter for the current psition.
     *
     * @return the current positon as a point
     */
    public Point getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Getter for the array of mines.
     *
     * @return array of mines
     */
    public ArrayList<Point> getMines() {
        return mines;
    }

    /**
     * Getter for the array of flags.
     *
     * @return array of flags
     */
    public ArrayList<Point> getFlags() {
        return flags;
    }

    /**
     * Getter for the array of prcessed field that are not mines.
     *
     * @return
     */
    public ArrayList<Point> getProcessedNotMines() {
        return processedNotMines;
    }

    /**
     * Getter for the game over value.
     *
     * @return if the game is over
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Setter for the game over value.
     *
     * @param gameOver the value to change the game over
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Checks for how many mines are around the given possition.
     *
     * @param point point to check around
     * @return the amount of mines around the point
     */
    public int minesAround(Point point) {
        int c = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Point p = new Point(point.x + i - 1, point.y + j - 1);
                if (mines.contains(p)) {
                    c++;
                }
            }
        }
        return c;
    }

    /**
     * Getter for the size.
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Getter for the timer.
     *
     * @return the timer
     */
    public Timer getTimer() {
        return timer;
    }

    /**
     * Sets up the timer.
     *
     * @param gameWindowGUI the main window
     */
    public void setupTimer(GameWindowGUI gameWindowGUI) {
        if (timer == null) {
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    incrementTime();
                    gameWindowGUI.updateTimeLabel();
                }
            });
        }
    }
}
