/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;
import mineSweeper.Difficulty;
import static mineSweeper.Difficulty.EASY;
import static mineSweeper.Difficulty.HARD;
import static mineSweeper.Difficulty.MEDIUM;

/**
 * Class that connects to a MySQL database and is able to get its table data.
 *
 * @author tothv
 */
public class HighScores {

    /**
     * Maximum amount of scores that can be stored in the table.
     */
    private int maxScores;

    /**
     * The statement to insert into the table.
     */
    private PreparedStatement insertStatement;

    /**
     * The statement to delete from the table.
     */
    private PreparedStatement deleteStatement;

    /**
     * The connection to the table.
     */
    private Connection connection;

    /**
     * The difficulity of the table.
     */
    private Difficulty difficulty;

    /**
     * Constructor for the HighScores class.
     *
     * @param maxScores maximum amount of entries in tha table.
     * @param dif the difficulity of the table.
     * @throws SQLException when there is a problem with the connection to the
     * database/table.
     */
    public HighScores(int maxScores, Difficulty dif) throws SQLException {
        this.maxScores = maxScores;
        this.difficulty = dif;
        Properties connectionProps = new Properties();
        
        connectionProps.put("user", "newuser");
        connectionProps.put("password", "asd123");
        connectionProps.put("serverTimezone", "UTC");
        String dbURL = "jdbc:mysql://localhost:3306/minesweeper_highscores_db";
        connection = DriverManager.getConnection(dbURL, connectionProps);

        String insertQuery;
        String deleteQuery;

        switch (dif) {
            case EASY:
                insertQuery = "INSERT INTO MINESWEEPER_HIGHSCORES_EASY (NAME, TIME) VALUES (?, ?)";
                deleteQuery = "DELETE FROM MINESWEEPER_HIGHSCORES_EASY WHERE TIME=?";
                break;
            case MEDIUM:
                insertQuery = "INSERT INTO MINESWEEPER_HIGHSCORES_MEDIUM (NAME, TIME) VALUES (?, ?)";
                deleteQuery = "DELETE FROM MINESWEEPER_HIGHSCORES_MEDIUM WHERE TIME=?";
                break;
            case HARD:
                insertQuery = "INSERT INTO MINESWEEPER_HIGHSCORES_HARD (NAME, TIME) VALUES (?, ?)";
                deleteQuery = "DELETE FROM MINESWEEPER_HIGHSCORES_HARD WHERE TIME=?";
                break;
            default:
                throw new AssertionError();
        }
        insertStatement = connection.prepareStatement(insertQuery);
        deleteStatement = connection.prepareStatement(deleteQuery);
    }

    /**
     * Get the contents of a table and puts it into an array.
     *
     * @return the contents of the table.
     * @throws SQLException when there is a problem with the connection to the
     * database/table.
     */
    public ArrayList<HighScore> getHighScores() throws SQLException {
        String query;
        switch (difficulty) {
            case EASY:
                query = "SELECT * FROM MINESWEEPER_HIGHSCORES_EASY";
                break;
            case MEDIUM:
                query = "SELECT * FROM MINESWEEPER_HIGHSCORES_MEDIUM";
                break;
            case HARD:
                query = "SELECT * FROM MINESWEEPER_HIGHSCORES_HARD";
                break;
            default:
                throw new AssertionError();
        }
        ArrayList<HighScore> highScores = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet results = stmt.executeQuery(query);
        while (results.next()) {
            String name = results.getString("NAME");
            int time = results.getInt("TIME");
            highScores.add(new HighScore(name, time));
        }
        sortHighScores(highScores);
        return highScores;
    }

    /**
     * Puts new entry into the table if there is space if there isn't it can
     * delete an old entry if neccecery.
     *
     * @param name of the player
     * @param time time of the player
     * @throws SQLException when there is a problem with the connection to the
     * database/table.
     */
    public void putHighScore(String name, int time) throws SQLException {
        ArrayList<HighScore> highScores = getHighScores();
        if (highScores.size() < maxScores) {
            insertScore(name, time);
        } else {
            int leastScore = highScores.get(highScores.size() - 1).getTime();
            if (leastScore > time) {
                deleteScores(leastScore);
                insertScore(name, time);
            }
        }
    }

    /**
     * Sort the high scores in descending order.
     *
     * @param highScores array of highscores
     */
    private void sortHighScores(ArrayList<HighScore> highScores) {
        Collections.sort(highScores, new Comparator<HighScore>() {
            @Override
            public int compare(HighScore t, HighScore t1) {
                return t.getTime() - t1.getTime();
            }
        });
    }

    /**
     * Insert into the table.
     *
     * @param name name of the player
     * @param time time of the player
     * @throws SQLException when there is a problem with the connection to the
     * database/table.
     */
    private void insertScore(String name, int time) throws SQLException {
        String insertQuery;
        switch (difficulty) {
            case EASY:
                insertQuery = "INSERT INTO MINESWEEPER_HIGHSCORES_EASY (NAME, TIME) VALUES (?, ?)";
                break;
            case MEDIUM:
                insertQuery = "INSERT INTO MINESWEEPER_HIGHSCORES_MEDIUM (NAME, TIME) VALUES (?, ?)";
                break;
            case HARD:
                insertQuery = "INSERT INTO MINESWEEPER_HIGHSCORES_HARD (NAME, TIME) VALUES (?, ?)";
                break;
            default:
                throw new AssertionError();
        }
        insertStatement = connection.prepareStatement(insertQuery);
        
        insertStatement.setString(1, name);
        insertStatement.setInt(2, time);
        insertStatement.executeUpdate();
    }

    /**
     * Deletes all the highscores with time.
     *
     * @param time the timr to be deleted
     * @throws SQLException when there is a problem with the connection to the
     * database/table.
     */
    private void deleteScores(int time) throws SQLException {
                String deleteQuery;

        switch (difficulty) {
            case EASY:
                deleteQuery = "DELETE FROM MINESWEEPER_HIGHSCORES_EASY WHERE TIME=?";
                break;
            case MEDIUM:
                deleteQuery = "DELETE FROM MINESWEEPER_HIGHSCORES_MEDIUM WHERE TIME=?";
                break;
            case HARD:
                deleteQuery = "DELETE FROM MINESWEEPER_HIGHSCORES_HARD WHERE TIME=?";
                break;
            default:
                throw new AssertionError();
        }
        deleteStatement = connection.prepareStatement(deleteQuery);
        
        deleteStatement.setInt(1, time);
        deleteStatement.executeUpdate();
    }
    
    public void changeDifficulty(Difficulty dif){
        this.difficulty = dif;
    }
}
