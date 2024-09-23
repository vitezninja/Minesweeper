/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mineSweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * The panel for the game to be shown in.
 *
 * @author tothv
 */
class GameField extends JPanel {

    /**
     * Its parent its in.
     */
    private GameWindowGUI parent;

    /**
     * The gamemodel that is run so simulate the game.
     */
    private Model model;

    /**
     * The size of the game.
     */
    private final int size;

    /**
     * The dimensions of the squares that are drawn out.
     */
    private int dimension;

    /**
     * Construcot for the game field.
     *
     * @param parent the frame this panel is in
     * @param size the size of the game
     */
    public GameField(GameWindowGUI parent, int size) {
        this.parent = parent;
        model = new Model(size);
        this.size = size;

        switch (size) {
            case 9:
                dimension = 50;
                break;
            case 16:
                dimension = 40;
                break;
            case 20:
                dimension = 33;
                break;
            default:
                dimension = 5;
                break;
        }

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "pressed up");
        this.getActionMap().put("pressed up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                model.moveX(-1);
                repaint();
            }
        });
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "pressed down");
        this.getActionMap().put("pressed down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                model.moveX(1);
                repaint();
            }
        });
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "pressed left");
        this.getActionMap().put("pressed left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                model.moveY(-1);
                repaint();
            }
        });
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), "pressed right");
        this.getActionMap().put("pressed right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                model.moveY(1);
                repaint();
            }
        });
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "pressed interact");
        this.getActionMap().put("pressed interact", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!model.isFlaged(model.getCurrentPosition())) {
                    if (model.isThereAMine()) {
                        showAllBombs();
                        repaint();
                        parent.showGameOverMessage();
                    } else {
                        model.interactWithField();
                        repaint();
                        if (model.isGameWon()) {
                            parent.showGameIsWonMessage();
                        }
                    }
                }
            }
        });
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "pressed flag");
        this.getActionMap().put("pressed flag", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (model.isFlaged(model.getCurrentPosition())) {
                    model.setFlag(0);
                } else {
                    model.setFlag(1);
                }
                repaint();
                if (model.isGameWon()) {
                    parent.showGameIsWonMessage();
                }
            }
        });
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pressed esc");
        this.getActionMap().put("pressed esc", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                parent.showTimeOutMessage();
            }
        });
        
        JButton easterEgg = new JButton();
        easterEgg.setPreferredSize(new Dimension(10,10));
        easterEgg.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent ae) {
            parent.addToHighScores("Beni", 0, Difficulty.EASY);
            parent.addToHighScores("Beni", 0, Difficulty.MEDIUM);
            parent.addToHighScores("Beni", 0, Difficulty.HARD);
        }
        });
        easterEgg.setOpaque(false);
        easterEgg.setContentAreaFilled(false);
        easterEgg.setBorderPainted(false);
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
        easterEgg.setLocation(50,50);
        this.add(easterEgg);
    }

    /**
     * Getter for the model.
     *
     * @return the model
     */
    public Model getModel() {
        return model;
    }

    /**
     * Shows all bomb of the game.
     */
    public void showAllBombs() {
        model.setGameOver(true);
    }

    /**
     * Overrides the paintComponent to draw out the game field.
     *
     * @param grphcs the graphics
     */
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2 = (Graphics2D) grphcs;
        
        int dimDiv2 = dimension / 2;
        int dimDiv10 = dimension / 10;
        
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0    , dimension * size + (size + 1) * dimDiv10, dimension * size + (size + 1) * dimDiv10);
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!model.getProcessedNotMines().contains(new Point(j, i))) {
                    g2.setColor(Color.WHITE);
                    g2.fillRect(i * dimension + dimDiv10 * (i + 1), j * dimension + dimDiv10 * (j + 1), dimension, dimension);
                } else {
                    g2.setColor(Color.BLACK);
                    int value = model.minesAround(new Point(j, i));
                    if (value != 0) {
                        g2.setColor(Color.RED);
                        g2.setFont(new Font("Arial", Font.BOLD, 12));
                        g2.drawString(Integer.toString(value), i * dimension + dimDiv10 * (i + 1) + dimDiv2 - dimDiv10, j * dimension + dimDiv10 * (j + 1) + dimDiv2 + dimDiv2 / 10);
                    }
                }
            }
        }
        
        g2.setColor(Color.BLACK);
        for (int i = 0; i < size + 1; i++) {
            g2.fillRect((dimension + dimDiv10) * i, 0, dimDiv10, dimension * (size) + ((size + 1) * dimDiv10));
            g2.fillRect(0, i * (dimension + dimDiv10), dimension * (size) + ((size + 1) * dimDiv10), dimDiv10);
        }
                
        for (Point flag : model.getFlags()) {
            g2.setColor(Color.BLACK);
            g2.fillRect(dimDiv10 + (flag.y * dimension + dimDiv10 * (flag.y + 1)), dimension - dimDiv10 + (flag.x * dimension + dimDiv10 * (flag.x + 1)), dimension - dimDiv10 * 2, dimDiv10);
            g2.fillRect(dimDiv10 * 2 + (flag.y * dimension + dimDiv10 * (flag.y + 1)), dimension - dimDiv10 * 2 + (flag.x * dimension + dimDiv10 * (flag.x + 1)), dimension - dimDiv10 * 4, dimDiv10);
            g2.fillRect(dimDiv2 - dimDiv2 / 10 + (flag.y * dimension + dimDiv10 * (flag.y + 1)), dimDiv10 * 2 + (flag.x * dimension + dimDiv10 * (flag.x + 1)), dimDiv10, dimension - dimDiv10 * 4);
            int[] x = {dimDiv10 + (flag.y * dimension + dimDiv10 * (flag.y + 1)), dimDiv2 - dimDiv2 / 10 + dimDiv10 + (flag.y * dimension + dimDiv10 * (flag.y + 1)), dimDiv2 - dimDiv2 / 10 + dimDiv10 + (flag.y * dimension + dimDiv10 * (flag.y + 1))};
            int[] y = {dimDiv10 * 3 + (flag.x * dimension + dimDiv10 * (flag.x + 1)), dimDiv10 * 4 + (flag.x * dimension + dimDiv10 * (flag.x + 1)), dimDiv10 * 2 + (flag.x * dimension + dimDiv10 * (flag.x + 1))};
            g2.setColor(Color.RED);
            g2.fillPolygon(x, y, 3);
        }
                
        if (model.isGameOver()) {
            for (Point mine : model.getMines()) {
                g2.setColor(Color.WHITE);
                g2.fillRect(mine.y * dimension + dimDiv10 * (mine.y + 1), mine.x * dimension + dimDiv10 * (mine.x + 1), dimension, dimension);
                g2.setColor(Color.BLACK);
                g2.fillOval(dimDiv10 * 2 + (mine.y * dimension + dimDiv10 * (mine.y + 1)), dimDiv10 * 2 + (mine.x * dimension + dimDiv10 * (mine.x + 1)), dimension - dimDiv10 * 4, dimension - dimDiv10 * 4);
                g2.fillRect(dimDiv2 - dimDiv2 / 10 + (mine.y * dimension + dimDiv10 * (mine.y + 1)), dimDiv10 + (mine.x * dimension + dimDiv10 * (mine.x + 1)), dimDiv10, dimension - dimDiv10 * 2);
                g2.fillRect(dimDiv10 + (mine.y * dimension + dimDiv10 * (mine.y + 1)), dimDiv2 - dimDiv2 / 10 + (mine.x * dimension + dimDiv10 * (mine.x + 1)), dimension - dimDiv10 * 2, dimDiv10);
                int[] x1 = {dimDiv10 * 2 - dimDiv2 / 10 + (mine.y * dimension + dimDiv10 * (mine.y + 1)), dimDiv10 * 3 - dimDiv2 / 10 + (mine.y * dimension + dimDiv10 * (mine.y + 1)), dimension - dimDiv10 * 2 + dimDiv2 / 10 + (mine.y * dimension + dimDiv10 * (mine.y + 1)), dimension - dimDiv10 * 3 + dimDiv2 / 10 + (mine.y * dimension + dimDiv10 * (mine.y + 1))};
                int[] y1 = {dimDiv10 * 3 - dimDiv2 / 10 + (mine.x * dimension + dimDiv10 * (mine.x + 1)), dimDiv10 * 2 - dimDiv2 / 10 + (mine.x * dimension + dimDiv10 * (mine.x + 1)), dimension - dimDiv10 * 3 + dimDiv2 / 10 + (mine.x * dimension + dimDiv10 * (mine.x + 1)), dimension - dimDiv10 * 2 + dimDiv2 / 10 + (mine.x * dimension + dimDiv10 * (mine.x + 1))};
                g2.fillPolygon(x1, y1, 4);
                int[] x2 = {dimDiv10 * 2 - dimDiv2 / 10 + (mine.y * dimension + dimDiv10 * (mine.y + 1)), dimDiv10 * 3 - dimDiv2 / 10 + (mine.y * dimension + dimDiv10 * (mine.y + 1)), dimension - dimDiv10 * 2 + dimDiv2 / 10 + (mine.y * dimension + dimDiv10 * (mine.y + 1)), dimension - dimDiv10 * 3 + dimDiv2 / 10 + (mine.y * dimension + dimDiv10 * (mine.y + 1))};
                int[] y2 = {dimension - dimDiv10 * 3 + dimDiv2 / 10 + (mine.x * dimension + dimDiv10 * (mine.x + 1)), dimension - dimDiv10 * 2 + dimDiv2 / 10 + (mine.x * dimension + dimDiv10 * (mine.x + 1)), dimDiv10 * 3 - dimDiv2 / 10 + (mine.x * dimension + dimDiv10 * (mine.x + 1)), dimDiv10 * 2 - dimDiv2 / 10 + (mine.x * dimension + dimDiv10 * (mine.x + 1))};
                g2.fillPolygon(x2, y2, 4);
                g2.setColor(Color.WHITE);
                g2.fillRect(dimDiv10 * 3 + dimDiv2 / 10 + (mine.y * dimension + dimDiv10 * (mine.y + 1)), dimDiv10 * 3 + dimDiv2 / 10 + (mine.x * dimension + dimDiv10 * (mine.x + 1)), dimDiv10 + dimDiv2 / 10, dimDiv10 + dimDiv2 / 10);
            }
        }

        Shape ring = createRingShape(dimDiv2 + dimension * (model.getCurrentPosition().y) + (dimDiv10 * (model.getCurrentPosition().y + 1)), dimDiv2 + dimension * (model.getCurrentPosition().x) + (dimDiv10 * (model.getCurrentPosition().x + 1)), dimDiv2, dimDiv10);
        g2.setColor(Color.RED);
        g2.fill(ring);
        g2.draw(ring);
    }

    /**
     * Creates a ring shape.
     *
     * @param centerX x coordinate of the center of the rings
     * @param centerY y coordinate of the center of the rings
     * @param outerRadius the outer radius of the ring
     * @param thickness the thickness of the ring
     * @return the ring shape
     */
    private Shape createRingShape(double centerX, double centerY, double outerRadius, double thickness) {
        Ellipse2D outer = new Ellipse2D.Double(centerX - outerRadius, centerY - outerRadius, outerRadius + outerRadius, outerRadius + outerRadius);
        Ellipse2D inner = new Ellipse2D.Double(centerX - outerRadius + thickness, centerY - outerRadius + thickness, outerRadius + outerRadius - thickness - thickness, outerRadius + outerRadius - thickness - thickness);
        Area area = new Area(outer);
        area.subtract(new Area(inner));
        return area;
    }
}
