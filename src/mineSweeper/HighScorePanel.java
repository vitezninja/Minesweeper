 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mineSweeper;

import dataBase.HighScore;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

/**
 * The panel containing the highscores.
 *
 * @author tothv
 */
class HighScorePanel extends JPanel {

    /**
     * The connection to the easy table in the database.
     */
    private dataBase.HighScores highScores;
    
    /**
     * Names of players who are on this score board.
     */
    private String[] names;
    
    /**
     * Times of players who are on the scoar board.
     */
    private int[] times;

    /**
     * Parent of this component.
     */
    private GameWindowGUI parent;
    
    /**
     * Constuctor for the highscore panel.
     */
    public HighScorePanel(GameWindowGUI parent) {
        this.parent = parent;
        this.setLayout(new BorderLayout());
        this.names = new String[10];
        this.times = new int[10];
        try {
            highScores = new dataBase.HighScores(10, Difficulty.EASY);
        } catch (SQLException ex) {
            Logger.getLogger(HighScorePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Updates the high score panel with the information in the table that its
     * given.
     *
     * @param highScores the connection to the table to read from
     */
    public void updateHighScores(dataBase.HighScores highScores) {
        names = new String[10];
        times = new int[10];
        try {
            int i = 0;
            for (HighScore highScore : highScores.getHighScores()) {
                names[i] = highScore.getName();
                times[i] = highScore.getTime();
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(HighScorePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Show the specified highScore difficulty.
     * 
     * @param dif the difficulty
     */
    public void showScores(Difficulty dif) {
        highScores.changeDifficulty(dif);
        updateHighScores(highScores);
        repaint();
    }

    /**
     * Puts a highscore into the table of a difficulty.
     *
     * @param name the player name
     * @param time the players time
     * @param dif the difficulty
     */
    public void putHighScore(String name, int time, Difficulty dif) {
        try {
            highScores.changeDifficulty(dif);
            highScores.putHighScore(name, time);
        } catch (SQLException ex) {
            Logger.getLogger(HighScorePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Overrides the paintComponent to draw out the highscores.
     * 
     * @param grphcs the graphics
     */
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setFont(new Font("Arial", Font.BOLD, 16));

        //99,20
        g2.drawString("Név", 129-3*5, 19);
        g2.drawString("Idő", 307-3*5, 19);
        g2.fillRect(218, 0, 4, 340);
        g2.fillRect(40, 0, 4, 340);
        
        for (int i = 0; i < names.length; i++) {
            g2.fillRect(0, 27+(i*31), 400, 4);
            g2.drawString(Integer.toString(i+1)+".",15-(Integer.toString(i+1).length()*3), 50+(i*31));
            if(names[i] != null) {
                g2.drawString(names[i], 129-(names[i].length()*4), 50+(i*31));
                g2.drawString(Integer.toString(times[i])+" sec", 307-((Integer.toString(times[i]).length()+4)*4), 50+(i*31));
            }
            else{
                g2.drawString("-", 129-1*5, 50+(i*31));
                g2.drawString("-", 307-1*5, 50+(i*31));
            }
        }
    }
}
