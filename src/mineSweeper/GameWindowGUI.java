/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mineSweeper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static mineSweeper.Difficulty.EASY;
import static mineSweeper.Difficulty.HARD;
import static mineSweeper.Difficulty.MEDIUM;

/**
 * The class responsible of handleing the gui of the game.
 *
 * @author tothv
 */
public class GameWindowGUI {

    /**
     * The frame of the gui.
     */
    private JFrame frame;

    /**
     * The panel where the game will be shown.
     */
    private GameField gameField;

    /**
     * The panel where the highscores will be shown.
     */
    private HighScorePanel highScorePanel;

    /**
     * The menubar.
     */
    private MenuBar menuBar;

    /**
     * The label where the time is shown.
     */
    private JLabel timeLabel;

    /**
     * The panel of the homescreen.
     */
    private JPanel homeScreenPanel;

    /**
     * Constructor of the GameWindowGUI.
     */
    public GameWindowGUI() {

        frame = new JFrame();
        frame.setTitle("Aknakereső");
        URL url = GameWindowGUI.class.getResource("icon.png");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(url));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        timeLabel = new JLabel();
        frame.setLayout(new BorderLayout());

        menuBar = new MenuBar(showHighScoreAction, startNewGameAction, homeScreenAction, this);
        frame.setJMenuBar(menuBar);
        setMinimumAndMaximumSize();

        homeScreenPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics grphcs) {
                super.paintComponent(grphcs);
                Graphics2D g2 = (Graphics2D) grphcs;
                g2.setFont(new Font("Ariel", Font.PLAIN, 16));
                String[] homeText = {"A piros karika jelzi a jelenlegi helyzetünk a táblán.", "Mozogni a WASD billentyűkel lehet.", "Zászló lerakni az ENTER-el lehet.", "Mezőt felfedni pedig a SPACE lenyomásával tudunk.", "Az ESC lenyomásával meg lehet állitani a játékot."};
                int yPos = 100;
                for (int i = 0; i < homeText.length; i++) {
                    g2.drawString(homeText[i], 10, yPos + 20 * i);
                }
            }
        };
        highScorePanel = new HighScorePanel(this);

        frame.getContentPane().add(homeScreenPanel);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);        
    }

    /**
     * Getter for the frame of the gui.
     *
     * @return the frame
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Action to show the selected highscore difficluty.
     */
    private final Action showHighScoreAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Difficulty difficulty = menuBar.getHighScoreDifficulty();
            highScorePanel.showScores(difficulty);
            showHighScore();
            setMinimumAndMaximumSize();
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
    };

    /**
     * Action to creat and show the game field.
     */
    private final Action startNewGameAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameField != null) {
                clearGameField();
            }
            frame.getContentPane().remove(highScorePanel);
            frame.getContentPane().remove(homeScreenPanel);
            Difficulty difficulty = menuBar.getGameDifficulty();

            switch (difficulty) {
                case EASY:
                    gameField = new GameField(GameWindowGUI.this, 9);
                    frame.setMinimumSize(new Dimension(515, 576));
                    frame.setMaximumSize(new Dimension(515, 576));
                    break;
                case MEDIUM:
                    gameField = new GameField(GameWindowGUI.this, 16);
                    frame.setMinimumSize(new Dimension(723, 784));
                    frame.setMaximumSize(new Dimension(723, 784));
                    break;
                case HARD:
                    gameField = new GameField(GameWindowGUI.this, 20);
                    frame.setMinimumSize(new Dimension(737, 799));
                    frame.setMaximumSize(new Dimension(737, 799));
                    break;
            }
            frame.getContentPane().add(gameField, BorderLayout.CENTER);
            gameField.getModel().setupTimer(GameWindowGUI.this);
            frame.add(timeLabel, BorderLayout.NORTH);
            gameField.getModel().getTimer().start();
            updateTimeLabel();
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
    };

    /**
     * Action to show the homescreen.
     */
    private final Action homeScreenAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameField != null) {
                gameField.getModel().getTimer().stop();
                clearGameField();
            }
            hideHighScore();
            setMinimumAndMaximumSize();
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
    };

    /**
     * Method to update the time labels text.
     */
    public void updateTimeLabel() {
        timeLabel.setText("Eltelt idő: " + gameField.getModel().getTime() + " sec");
    }

    /**
     * Shows a window when the game is over.
     */
    public void showGameIsWonMessage() {
        gameField.getModel().getTimer().stop();
        String input = JOptionPane.showInputDialog(frame, "Gratulálok nyertél! \nAz időd: " + gameField.getModel().getTime() + " mp!\nÍrd be a neved (maximum 12 karakter):", "Nyert!", JOptionPane.PLAIN_MESSAGE);
        if (input != null) {
            while(input.length() > 12){
                input = JOptionPane.showInputDialog(frame, "A mentett név maxiumum 12 karakter lehet!\nÍrd be a neved:", "Figyelmeztetés!", JOptionPane.WARNING_MESSAGE);
                if(input == null){
                    clearGameField();
                    return;
                }
            }
            switch (gameField.getModel().getSize()) {
                case 9:
                    addToHighScores(input, gameField.getModel().getTime(), Difficulty.EASY);
                    break;
                case 16:
                    addToHighScores(input, gameField.getModel().getTime(), Difficulty.MEDIUM);
                    break;
                case 20:
                    addToHighScores(input, gameField.getModel().getTime(), Difficulty.HARD);
                    break;
                default:
                    throw new AssertionError();
            }
        }
        clearGameField();
    }

    /**
     * Shows a window when the game is lost.
     */
    public void showGameOverMessage() {
        gameField.getModel().getTimer().stop();
        JOptionPane.showMessageDialog(frame, "Sajnos vesztettél!", "Vesztettél!", JOptionPane.PLAIN_MESSAGE);
        clearGameField();
        frame.repaint();

    }

    /**
     * Shows a window when the game is paused.
     */
    public void showTimeOutMessage() {
        gameField.getModel().getTimer().stop();
        JOptionPane.showMessageDialog(frame, "Megállítottad a játékot.\nFolytatáshoz nyomj az Ok-ra.", "Info", JOptionPane.INFORMATION_MESSAGE);
        gameField.getModel().getTimer().start();
    }

    /**
     * Clears the game field and hides it.
     */
    private void clearGameField() {
        frame.getContentPane().remove(gameField);
        frame.getContentPane().remove(timeLabel);
        if(gameField != null){
            gameField.getModel().getTimer().stop();
        }
        gameField = null;
        frame.getContentPane().add(homeScreenPanel);
        setMinimumAndMaximumSize();
        frame.repaint();
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    /**
     * Hides the highscore panel.
     */
    public void hideHighScore() {
        frame.getContentPane().remove(highScorePanel);
        frame.getContentPane().add(homeScreenPanel);
        frame.repaint();
    }

    /**
     * Shows the highscore panel.
     */
    public void showHighScore() {
        if (gameField != null) {
            gameField.getModel().getTimer().stop();
            frame.getContentPane().remove(gameField);
            frame.getContentPane().remove(timeLabel);
        }
        frame.getContentPane().remove(homeScreenPanel);
        frame.getContentPane().add(highScorePanel);
        frame.repaint();
    }

    /**
     * Adds a new entry to the highscores.
     *
     * @param name name of the player
     * @param time time of the player
     * @param dif difficulty the game was played on
     */
    public void addToHighScores(String name, int time, Difficulty dif) {
        highScorePanel.putHighScore(name, time, dif);
    }

    /**
     * Sets the minimum and maximum window size.
     */
    private void setMinimumAndMaximumSize() {
        frame.setMinimumSize(new Dimension(400, 400));
        frame.setMaximumSize(new Dimension(400, 400));
    }

}
