package nl.tudelft.jpacman.level;

import java.util.Map;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.sprite.AnimatedSprite;
import nl.tudelft.jpacman.sprite.Sprite;
import nl.tudelft.jpacman.ui.ScorePanel;

/**
 * A player operated unit in our game.
 *
 * @author Jeroen Roosen
 */
public class Player extends Unit {

    /**
     * The amount of points accumulated by this player.
     */
    private int score;

    /**
     * The animations for every direction.
     */
    private final Map<Direction, Sprite> sprites;

    /**
     * The animation that is to be played when Pac-Man dies.
     */
    private final AnimatedSprite deathSprite;

    /**
     * The maximum amount of lives a player can have.
     */
    public static final int MAX_LIVES = 3;

    /**
     * Number of lives remaining.
     */
    private int remainingLives;

    /**
     * {@link Unit} iff this player died by collision, <code>null</code> otherwise.
     */
    private Unit killer;

    /**
     * Creates a new player with a score of 0 points.
     *
     * @param spriteMap
     *                       A map containing a sprite for this player for every
     *                       direction.
     * @param deathAnimation
     *                       The sprite to be shown when this player dies.
     */
    protected Player(Map<Direction, Sprite> spriteMap, AnimatedSprite deathAnimation) {
        this.score = 0;
        this.remainingLives = MAX_LIVES;
        this.sprites = spriteMap;
        this.deathSprite = deathAnimation;
        deathSprite.setAnimating(false);
    }

    /**
     * Returns the amount of lives remaining for this player.
     *
     * @return <code>int</code> The amount of lives remaining.
     */
    public int getRemainingLives() {
        return remainingLives;
    }

    /**
     * Returns whether this player is alive or not.
     *
     * @return <code>true</code> iff the player is alive.
     */
    public boolean isAlive() {
        return remainingLives > 0;
    }

    /**
     * Kills the player.
     *
     * @param killer The unit that caused the player to die.
     */
    public void kill(Unit killer) {
        this.kill();
        this.setKiller(killer);
    }

    /**
     * Returns the unit that caused the death of Pac-Man.
     *
     * @return <code>Unit</code> iff the player died by collision, otherwise
     *         <code>null</code>.
     */
    public Unit getKiller() {
        return killer;
    }

    /**
     * Sets the cause of death.
     *
     * @param killer is set if collision with ghost happens.
     */
    public void setKiller(Unit killer) {
        this.killer = killer;
    }

    /**
     * Returns the amount of points accumulated by this player.
     *
     * @return The amount of points accumulated by this player.
     */
    public int getScore() {
        return score;
    }

    @Override
    public Sprite getSprite() {
        if (isAlive()) {
            return sprites.get(getDirection());
        }
        return deathSprite;
    }

    /**
     * Adds points to the score of this player.
     *
     * @param points
     *               The amount of points to add to the points this player already
     *               has.
     */
    public void addPoints(int points) {
        score += points;
    }

    /**
     * Kills this player, reducing the amount of lives by one.
     */
    public void kill() {
        this.remainingLives--;
        if (this.isAlive()) {
            deathSprite.setAnimating(false);
            this.killer = null;
        } else {
            deathSprite.restart();
        }
    }

    /**
     * Builds the display text for this player.
     *
     * @param formatter The formatter used to format the score.
     * @return The display text for this player.
     */
    public String getDisplayText(ScorePanel.ScoreFormatter formatter) {
        String score = "";
        if (!isAlive()) {
            score = "You died. ";
        }
        score += formatter.format(this);
        score += "  Lives: " + remainingLives;
        return score;
    }

}
