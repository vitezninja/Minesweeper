/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataBase;

/**
 * This class stores the data of a highscore.
 *
 * @author tothv
 */
public class HighScore {

    /**
     * Name of the player.
     */
    private final String name;
    /**
     * Time of the player.
     */
    private final int time;

    /**
     * Constructor for the HighScore class.
     *
     * @param name name of the player
     * @param time time of the player
     */
    public HighScore(String name, int time) {
        this.name = name;
        this.time = time;
    }

    /**
     * Getter for the name of the player.
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the time of the player
     *
     * @return the time of the player
     */
    public int getTime() {
        return time;
    }

    /**
     * ToString method override to reppresent the current player and his score
     *
     * @return
     */
    @Override
    public String toString() {
        return "HighScore{" + "name=" + name + ", score=" + time + '}';
    }

}
