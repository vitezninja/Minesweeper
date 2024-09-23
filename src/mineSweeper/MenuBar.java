/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mineSweeper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

/**
 * The menu bar of the gui.
 *
 * @author tothv
 */
public class MenuBar extends JMenuBar {

    /**
     * The difficulty that is currently selected for the game dropdown.
     */
    private Difficulty gameDifficulty = Difficulty.EASY;

    /**
     * The difficulty that is currently selected for the highscore dropdown.
     */
    private Difficulty highScoreDifficulty = Difficulty.EASY;

    /**
     * Parent of this component.
     */
    private GameWindowGUI parent;
    
    /**
     * Constructor for the menubar.
     *
     * @param showHighScoresAction the action to do when clicked on the button
     * that shows the highscores
     * @param startNewGameAction the action to do when clicked on the button in
     * that starts a new game
     * @param homeScreenAction the action to do when clicked on the homescreen
     * button
     */
    public MenuBar(Action showHighScoresAction, Action startNewGameAction, Action homeScreenAction, GameWindowGUI parent) {
        this.parent = parent;
        JMenu newGameMenu = new JMenu("Új játék");
        JMenuItem startNewGame = new JMenuItem(startNewGameAction);
        startNewGame.setText("Indítás");
        newGameMenu.add(startNewGame);
        newGameMenu.addSeparator();

        ButtonGroup gameGroup = new ButtonGroup();

        JRadioButtonMenuItem gameEasy = new JRadioButtonMenuItem();
        gameEasy.setText("Könnyű");
        gameEasy.setSelected(true);
        gameEasy.addActionListener(gameActionListener(Difficulty.EASY));
        gameGroup.add(gameEasy);

        JRadioButtonMenuItem gameMedium = new JRadioButtonMenuItem();
        gameMedium.setText("Közepes");
        gameMedium.addActionListener(gameActionListener(Difficulty.MEDIUM));
        gameGroup.add(gameMedium);

        JRadioButtonMenuItem gameHard = new JRadioButtonMenuItem();
        gameHard.setText("Nehéz");
        gameHard.addActionListener(gameActionListener(Difficulty.HARD));
        gameGroup.add(gameHard);

        newGameMenu.add(gameEasy);
        newGameMenu.add(gameMedium);
        newGameMenu.add(gameHard);

        JMenu highScoreMenu = new JMenu("Rekordok");
        JMenuItem showHighScores = new JMenuItem(showHighScoresAction);
        showHighScores.setText("Keresés");
        highScoreMenu.add(showHighScores);
        highScoreMenu.addSeparator();

        ButtonGroup highScoreGroup = new ButtonGroup();

        JRadioButtonMenuItem highScoreEasy = new JRadioButtonMenuItem();
        highScoreEasy.setText("Könnyű");
        highScoreEasy.setSelected(true);
        highScoreEasy.addActionListener(highScoreActionListener(Difficulty.EASY));
        highScoreGroup.add(highScoreEasy);

        JRadioButtonMenuItem highScoreMedium = new JRadioButtonMenuItem();
        highScoreMedium.setText("Közepes");
        highScoreMedium.addActionListener(highScoreActionListener(Difficulty.MEDIUM));
        highScoreGroup.add(highScoreMedium);

        JRadioButtonMenuItem highScoreHard = new JRadioButtonMenuItem();
        highScoreHard.setText("Nehéz");
        highScoreHard.addActionListener(highScoreActionListener(Difficulty.HARD));
        highScoreGroup.add(highScoreHard);

        highScoreMenu.add(highScoreEasy);
        highScoreMenu.add(highScoreMedium);
        highScoreMenu.add(highScoreHard);

        JMenuItem homeScreen = new JMenuItem("Főmenü");
        homeScreen.addActionListener(homeScreenAction);

        add(newGameMenu);
        add(highScoreMenu);
        add(homeScreen);

    }

    /**
     * Action listener for when the radio buttons change in the dropdown menu
     * for the new game selector.
     *
     * @param dif the difficulty the current game difficulty is set to
     * @return the action listener the method creates
     */
    private ActionListener gameActionListener(Difficulty dif) {
        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameDifficulty = Difficulty.valueOf(dif.name());
            }
        };
        return action;
    }

    /**
     * Action listener for when the radio buttons change in the dropdown menu
     * for the highscore selector.
     *
     * @param dif the difficulty the current game difficulty is set to
     * @return the action listener the method creates
     */
    private ActionListener highScoreActionListener(Difficulty dif) {
        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highScoreDifficulty = Difficulty.valueOf(dif.name());
            }
        };
        return action;
    }

    /**
     * Getter for the game difficulty.
     *
     * @return the game difficluty
     */
    public Difficulty getGameDifficulty() {
        return gameDifficulty;
    }

    /**
     * Getter for the highscore difficulty.
     *
     * @return the highscore difficulty
     */
    public Difficulty getHighScoreDifficulty() {
        return highScoreDifficulty;
    }
}
